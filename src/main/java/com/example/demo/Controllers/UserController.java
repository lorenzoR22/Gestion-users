package com.example.demo.Controllers;

import com.example.demo.Config.Jwt.JwtUtils;
import com.example.demo.DTOs.LoginDTO;
import com.example.demo.DTOs.UserDTO;
import com.example.demo.Entities.User;
import com.example.demo.Exceptions.UsernameAlreadyExists;
import com.example.demo.Services.UserServiceInterface;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios")
    public String getAllUsuarios(Model model){
        model.addAttribute("usuarios",userServiceInterface.getAllUsers());
        return "usuarios";
    }

    @GetMapping("/signin")
    public String showLoginForm(Model model){
        model.addAttribute("loginUsuario",new LoginDTO());
        return "index";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("loginUsuario") LoginDTO loginDTO, HttpServletResponse response){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt= jwtUtils.generateToken(loginDTO.getUsername());
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        User userAuthenticaded= userServiceInterface.getUserByUsername(loginDTO.getUsername());

        if(userAuthenticaded.getRoles().stream().anyMatch(role->"ADMIN".equals(role.getRole().name()))){
            return "redirect:/usuarios";
        }else{
            return "templateUsuario";
        }
    }

    @GetMapping("/logoutt")
    public String logout(HttpServletRequest request, HttpServletResponse res) {
        Cookie[] cookies2 = request.getCookies();
        for (Cookie cookie : cookies2) {
            if (cookie.getName().equals("jwt")) {
                cookie.setMaxAge(0);
                res.addCookie(cookie);
            }
        }
        return "redirect:/signin";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("nuevoUsuario",new UserDTO());
        return "register";
    }

    @PostMapping("/registerForm")
    public String saveUser(@ModelAttribute("nuevoUsuario") UserDTO user) throws UsernameAlreadyExists {
        userServiceInterface.saveUser(user);
        return "redirect:/signin";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/updateUsuario/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        User user=userServiceInterface.getUserById(id);
        model.addAttribute("update",new UserDTO(id,
                user.getUsername(),
                user.getPassword(),
                user.getMail(),
                user.getRoles().stream()
                        .map(rol->rol.getRole().name())
                        .collect(Collectors.toSet())));

        return "updateUsuario";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("update") UserDTO user){
        userServiceInterface.updateUser(user);
        return "redirect:/usuarios";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id")Long id){
        userServiceInterface.deleteUser(id);
        return "redirect:/usuarios";
    }

}
