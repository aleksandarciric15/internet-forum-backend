package com.example.internetforum.services.impl;

import com.example.internetforum.exceptions.*;
import com.example.internetforum.models.dto.*;
import com.example.internetforum.models.entities.PermissionEntity;
import com.example.internetforum.models.entities.UserEntity;
import com.example.internetforum.models.entities.UserPermissionEntity;
import com.example.internetforum.models.enums.UserRole;
import com.example.internetforum.repositories.UserRepository;
import com.example.internetforum.services.AuthServiceInterface;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService implements AuthServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    @PersistenceContext
    private final EntityManager entityManager;

    @Value("${spring.security.oauth2.client.registration.github.clientId}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.github.clientSecret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.github.redirectUri}")
    private String redirectUri;
    @Value("${spring.oauth2.accessToken}")
    private String accessTokenUrl;
    @Value("${spring.oauth2.github.user}")
    private String getUserUrl;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, ModelMapper modelMapper, JwtService jwtService, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.entityManager = entityManager;
    }

    public void registerUser(UserRegisterRequest request) {
        if (this.userRepository.existsByUsername(request.getUsername()))
            throw new AlreadyExistsUsernameException();

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setEmail(request.getEmail());
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setRole(UserRole.FORUM_MEMBER.toString().toLowerCase());

        this.userRepository.saveAndFlush(userEntity);
    }


    public Integer loginUser(UserLoginRequest request) {
        UserEntity userEntity = this.userRepository.findByUsername(request.getUsername()).orElseThrow(NotFoundException::new);

        if (userEntity.isBlocked()) {
            throw new BlockedAccountException();
        }
        if (!userEntity.isApproved()) {
            throw new NotApprovedAccountException();
        }

        String verificationCode = generateRandom();
        userEntity.setVerificationCode(verificationCode);
        this.emailService.sendVerificationCodeToUser(userEntity.getEmail(), verificationCode);
        return userEntity.getId();
    }

    public VerificationUserResponse confirmLoginUser(VerificationUserRequest request) {
        UserEntity userEntity = this.userRepository.findById(request.getId())
                .orElseThrow(NotFoundException::new);
        if (userEntity.getVerificationCode() == null || !
                userEntity.getVerificationCode().equals(request.getVerificationCode())) {
            throw new UnauthorizedException();
        }

        return mapUser(userEntity);
    }

    private VerificationUserResponse mapUser(UserEntity userEntity) {
        VerificationUserResponse response = new VerificationUserResponse();
        response.setPermissions(userEntity.getPermissions().stream()
                .map(UserPermissionEntity::getPermission).toList().stream()
                .map(PermissionEntity::getName).collect(Collectors.toList()));
        response.setUsername(userEntity.getUsername());
        response.setLastName(userEntity.getLastName());
        response.setFirstName(userEntity.getFirstName());
        response.setId(userEntity.getId());
        response.setRole(userEntity.getRole());
        response.setToken(this.jwtService.generateToken(this.modelMapper.map(userEntity, JwtUser.class)));
        userEntity.setVerificationCode(null);
        return response;
    }

    private static String generateRandom() {
        Random random = new Random();
        return String.valueOf(random.nextInt(900000) + 100000);
    }

    public VerificationUserResponse loginUserWithGithub(String code) throws IOException {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(accessTokenUrl);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json");

            JSONObject requestBody = new JSONObject();
            requestBody.put("client_id", clientId);
            requestBody.put("client_secret", clientSecret);
            requestBody.put("code", code);
            requestBody.put("redirect_uri", redirectUri);

            StringEntity requestEntity = new StringEntity(requestBody.toString());
            httpPost.setEntity(requestEntity);

            CloseableHttpResponse response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject jsonResponse = new JSONObject(responseBody);

                String accessToken = jsonResponse.getString("access_token");

                UserGithub userGithub = getUserDetails(accessToken);
                UserEntity userEntity = registerOrLoadUser(userGithub);
                if (userEntity.isBlocked()) {
                    throw new BlockedAccountException();
                }
                if (!userEntity.isApproved()) {
                    throw new NotApprovedAccountException();
                }

                return mapUser(userEntity);
            }

        }
        return null;
    }

    public UserEntity registerOrLoadUser(UserGithub userGithub) {
        if (this.userRepository.findByEmail(userGithub.getEmail()).isPresent()) {
            return this.userRepository.findByEmail(userGithub.getEmail()).get();
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setId(null);
        userEntity.setUsername(userGithub.getLogin());
        userEntity.setPassword(userGithub.getLogin() + generateRandom());
        userEntity.setEmail(userGithub.getEmail());
        userEntity.setRole(UserRole.FORUM_MEMBER.toString().toLowerCase());
        if (userGithub.getName() != null) {
            String[] pom = userGithub.getName().split(" ");
            userEntity.setFirstName(pom[0]);
            userEntity.setLastName(pom[1]);
        } else {
            userEntity.setFirstName(" ");
            userEntity.setLastName(" ");
        }
        userEntity.setBlocked(false);
        userEntity.setApproved(true);
        this.userRepository.saveAndFlush(userEntity);
        this.entityManager.refresh(userEntity);
        return userEntity;
    }

    public UserGithub getUserDetails(String accessToken) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(getUserUrl);
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    UserGithub userGithub = objectMapper.readValue(responseBody, UserGithub.class);
                    userGithub.setEmail(getEmail(accessToken));
                    return userGithub;
                }
                {
                    throw new GithubApiException();
                }
            }


        }
    }

    public String getEmail(String accessToken) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet1 = new HttpGet(getUserUrl + "/emails");
            httpGet1.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

            try (CloseableHttpResponse response1 = httpClient.execute(httpGet1)) {
                if (response1.getStatusLine().getStatusCode() == 200) {
                    String responseBody = EntityUtils.toString(response1.getEntity());
                    JSONArray jsonArray = new JSONArray(responseBody);
                    JSONObject firstEmailObject = jsonArray.getJSONObject(0);
                    return firstEmailObject.getString("email");
                }
                {
                    throw new GithubApiException();
                }
            }
        }
    }
}
