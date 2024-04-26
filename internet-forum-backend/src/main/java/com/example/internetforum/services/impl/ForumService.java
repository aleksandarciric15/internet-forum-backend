package com.example.internetforum.services.impl;

import com.example.internetforum.exceptions.NotFoundException;
import com.example.internetforum.models.dto.Forum;
import com.example.internetforum.models.entities.ForumEntity;
import com.example.internetforum.repositories.ForumRepository;
import com.example.internetforum.services.ForumServiceInterface;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ForumService  implements ForumServiceInterface {

    private final ForumRepository forumRepository;
    private final ModelMapper modelMapper;

    public ForumService(ForumRepository forumRepository, CommentService commentService, ModelMapper modelMapper) {
        this.forumRepository = forumRepository;
        this.modelMapper = modelMapper;
    }

    public List<Forum> findAllForums() {
        return this.forumRepository.findAll().stream().map(elem -> this.modelMapper.map(elem, Forum.class))
                .collect(Collectors.toList());
    }

    public Forum findById(Integer id){
        ForumEntity forumEntity = this.forumRepository.findById(id).orElseThrow(NotFoundException::new);
        return this.modelMapper.map(forumEntity, Forum.class);
    }
}
