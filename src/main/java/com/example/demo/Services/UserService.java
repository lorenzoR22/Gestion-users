package com.example.demo.Services;

import com.example.demo.DTOs.UserDTO;
import com.example.demo.Entities.Erole;
import com.example.demo.Entities.Role;
import com.example.demo.Entities.User;
import com.example.demo.Exceptions.UsernameAlreadyExists;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream()
                .map(user->new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getMail(),
                        user.getRoles().stream()
                                .map(role-> role.getRole().name())
                                .collect(Collectors.toSet())
                )).toList();
    }

    public UserDTO saveUser(UserDTO userDTO) throws UsernameAlreadyExists {

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UsernameAlreadyExists("Este usuario ya esta registrado");
        }

        Set<Role> roles = userDTO.getRoles().stream()
                .map(role -> roleRepository.findByRole(Erole.valueOf(role))
                        .orElseGet(() -> {
                            Role newRole = new Role(Erole.valueOf(role));
                            return roleRepository.save(newRole);
                        }))
                .collect(Collectors.toSet());

        User newUser = new User(
                userDTO.getId(),
                userDTO.getUsername(),
                bCryptPasswordEncoder.encode(userDTO.getPassword()),
                userDTO.getEmail(),
                roles);
            userRepository.save(newUser);
        return userDTO;
    }

    public User updateUser(UserDTO userDTO){
        User userExistente=userRepository.findById(userDTO.getId())
                .orElseThrow(()->new UsernameNotFoundException("No se encontro el usuario"));

        Set<Role>roles=(userDTO.getRoles().stream()
                .map(role-> {
                    try {
                        return roleRepository.findByRole(Erole.valueOf(role))
                                .orElseThrow(()->new RoleNotFoundException("NO SE ENCONTRO ROLE"));
                    } catch (RoleNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet()));

        userExistente.setUsername(userDTO.getUsername());
        userExistente.setMail(userDTO.getEmail());
        userExistente.setRoles(roles);

       return userRepository.save(userExistente);
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("No se encontro el usuario"));
    }
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(
                ()->new UsernameNotFoundException("no se encontro el usuario")
        );
    }

    public boolean deleteUser(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
