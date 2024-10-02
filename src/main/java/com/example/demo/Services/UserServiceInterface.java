package com.example.demo.Services;

import com.example.demo.DTOs.LoginDTO;
import com.example.demo.DTOs.UserDTO;
import com.example.demo.Entities.User;
import com.example.demo.Exceptions.UsernameAlreadyExists;

import java.util.List;

public interface UserServiceInterface {
    public List<UserDTO> getAllUsers();
    public UserDTO saveUser(UserDTO userDTO)  throws UsernameAlreadyExists;
    public User updateUser(UserDTO userDTO);
    public boolean deleteUser(Long id);
    public User getUserById(Long id);
    public User getUserByUsername(String username);
    }
