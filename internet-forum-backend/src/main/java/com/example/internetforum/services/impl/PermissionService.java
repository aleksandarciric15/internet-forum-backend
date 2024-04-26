package com.example.internetforum.services.impl;

import com.example.internetforum.exceptions.NotFoundException;
import com.example.internetforum.models.dto.Permission;
import com.example.internetforum.models.entities.PermissionEntity;
import com.example.internetforum.repositories.PermissionRepository;
import com.example.internetforum.services.PermissionServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService implements PermissionServiceInterface {

    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    public PermissionService(PermissionRepository permissionRepository, ModelMapper modelMapper) {
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }


    public List<Permission> findAll(){
        return this.permissionRepository.findAll().stream()
                .map(elem -> this.modelMapper.map(elem, Permission.class)).collect(Collectors.toList());
    }

    public Permission findById(Integer id){
        PermissionEntity permissionEntity = this.permissionRepository.findById(id).orElseThrow(NotFoundException::new);
        return this.modelMapper.map(permissionEntity, Permission.class);
    }


}
