package com.example.demo.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @NotBlank
    @Size(max = 30,message = "El username no puede tener mas de 30 caracteres.")
    private String username;

    @NotBlank
    private String password;

    @Email(message = "Email invalido.")
    private String email;

    @NotBlank
    private Set<String> roles;

    public UserDTO(String username,String password, String email) {
        this.username=username;
        this.password = password;
        this.email = email;
        this.roles = new HashSet<>();
    }
}
