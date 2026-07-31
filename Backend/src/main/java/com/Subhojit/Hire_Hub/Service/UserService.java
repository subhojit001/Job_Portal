package com.Subhojit.Hire_Hub.Service;

import com.Subhojit.Hire_Hub.DTO.*;
import com.Subhojit.Hire_Hub.Entity.Type.Roles;
import com.Subhojit.Hire_Hub.Entity.User;
import com.Subhojit.Hire_Hub.Repository.UserRepository;
import com.Subhojit.Hire_Hub.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public SignUpResponse signUp(SignUpRequest signUpRequest){

        User user = userRepository.findByemail(signUpRequest.getEmail()).orElse(null);
        if(user!=null) throw new IllegalArgumentException("User already exist");

        user= User.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .roles(Roles.valueOf(signUpRequest.getRole()))
                .build();

        user = userRepository.save(user);

        return new SignUpResponse(user.getId(),user.getName());
    }

    public LoginResponse login(LoginRequest loginRequest){

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),loginRequest.getPassword()));
        String email = authentication.getName();

        User user = userRepository.findByemail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

         String token = jwtUtil.generateToken(user);

         return new LoginResponse(token);
    }

    public User getCurrentUser(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByemail(email).orElseThrow();

    }

    public userProfileResponse getProfile() {

        User user = getCurrentUser();

        return userProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .resumePath(user.getResumePath())
                .build();
    }

    public userProfileResponse updateProfile(ProfileUpdateRequest request) {
        User user = getCurrentUser();

        if(userRepository.findByemail(request.getEmail()).isPresent()){
            throw new RuntimeException("email already exists");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User updated = userRepository.save(user);

        return userProfileResponse.builder()
                .id(updated.getId())
                .name(updated.getName())
                .email(updated.getEmail())
                .roles(updated.getRoles())
                .resumePath(updated.getResumePath())
                .build();
    }
}
