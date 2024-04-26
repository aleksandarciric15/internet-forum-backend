package com.example.internetforum.services.impl;

import com.example.internetforum.exceptions.NotFoundException;
import com.example.internetforum.models.dto.ChangeUserPermissionsRequest;
import com.example.internetforum.models.dto.ChangeUserRoleRequest;
import com.example.internetforum.models.dto.User;
import com.example.internetforum.models.entities.PermissionEntity;
import com.example.internetforum.models.entities.UserEntity;
import com.example.internetforum.models.entities.UserPermissionEntity;
import com.example.internetforum.repositories.PermissionRepository;
import com.example.internetforum.repositories.UserPermissionRepository;
import com.example.internetforum.repositories.UserRepository;
import com.example.internetforum.services.UserServiceInterface;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserPermissionRepository userPermissionRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;


    public UserService(UserRepository userRepository, EmailService emailService,
                       UserPermissionRepository userPermissionRepository, PermissionRepository permissionRepository,
                       ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.userPermissionRepository = userPermissionRepository;
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.createTypeMap(UserEntity.class, User.class)
                .addMappings(mapper -> mapper.skip(User::setPermissions));
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> userEntities = this.userRepository.findAll();

        if (!userEntities.isEmpty()) {
            return userEntities.stream()
                    .map(userEntity -> {
                        User user = modelMapper.map(userEntity, User.class);
                        List<String> permissionNames = userEntity.getPermissions().stream()
                                .map(UserPermissionEntity::getPermission)
                                .map(PermissionEntity::getName)
                                .toList();
                        user.setPermissions(permissionNames);
                        return user;
                    }).toList();
        }
        return null;
    }

    @Override
    public void approveUser(Integer id) {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow(NotFoundException::new);
        userEntity.setApproved(true);
        this.emailService.sendUserApprovedMessage(userEntity.getEmail());
    }

    @Override
    public void blockUser(Integer id) {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow(NotFoundException::new);
        userEntity.setBlocked(true);
    }

    @Override
    public void unblockUser(Integer id) {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow(NotFoundException::new);
        userEntity.setBlocked(false);
    }

    @Override
    public void changeUserRole(ChangeUserRoleRequest request) {
        UserEntity userEntity = this.userRepository.findById(request.getId())
                .orElseThrow(NotFoundException::new);
        userEntity.setRole(request.getRole());
    }

    @Override
    public void changeUserPermissions(ChangeUserPermissionsRequest request) {
        UserEntity userEntity = this.userRepository.findById(request.getUserId()).orElseThrow(NotFoundException::new);

        List<String> newPermissions = request.getPermissions();

        List<String> currentPermissions = userEntity.getPermissions().stream()
                .map(UserPermissionEntity::getPermission)
                .map(PermissionEntity::getName)
                .toList();

        // will execute only if newPermissions and currentPermissions don't have same elements
        if (!(new HashSet<>(newPermissions).containsAll(currentPermissions) &&
                newPermissions.size() == currentPermissions.size())) {
            List<UserPermissionEntity> userPermissionEntities = userEntity.getPermissions();
            this.userPermissionRepository.deleteAll(userPermissionEntities);
            for (String permission : request.getPermissions()) {
                PermissionEntity permissionEntity = this.permissionRepository.findByName(permission);
                if (permissionEntity != null) {
                    UserPermissionEntity userPermissionEntity = new UserPermissionEntity();
                    userPermissionEntity.setPermission(permissionEntity);
                    userPermissionEntity.setUser(userEntity);
                    this.userPermissionRepository.saveAndFlush(userPermissionEntity);
                }
            }
        }
    }
}
