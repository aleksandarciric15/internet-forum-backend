package com.example.internetforum.services;

import com.example.internetforum.models.dto.ChangeUserPermissionsRequest;
import com.example.internetforum.models.dto.ChangeUserRoleRequest;
import com.example.internetforum.models.dto.User;

import java.util.List;

public interface UserServiceInterface {
    void approveUser(Integer id);
    void blockUser(Integer id);
    void unblockUser(Integer id);
    void changeUserRole(ChangeUserRoleRequest request);
    void changeUserPermissions(ChangeUserPermissionsRequest request);
    List<User> findAll();
}
