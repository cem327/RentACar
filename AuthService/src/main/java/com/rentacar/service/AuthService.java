package com.rentacar.service;

import com.rentacar.dto.request.LoginRequestDto;
import com.rentacar.dto.request.RegisterRequestDto;
import com.rentacar.entity.Auth;
import com.rentacar.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;

    /**
     * Registers a new user.
     *
     * This method registers a new user based on the provided RegisterRequestDto.
     * It creates an Auth entity using the information from the dto, sets the creation and
     * update timestamps, activates the user, and saves the entity to the database using
     * the authRepository.
     *
     * @param dto The RegisterRequestDto containing the user's registration information.
     * @return true if the user registration is successful, false otherwise.
     */
    public Boolean register(RegisterRequestDto dto){
        Auth auth = Auth.builder()
                .password(dto.getPassword())
                .email(dto.getEmail())
                .userName(dto.getUserName())
                .createAt(System.currentTimeMillis())
                .updateAt(System.currentTimeMillis())
                .isActive(true)
                .build();
        authRepository.save(auth);
        return true; // HACK - ALWAYS RETURN TRUE
    }

    /**
     * Performs user login authentication.
     *
     * This method attempts to authenticate a user based on the provided LoginRequestDto.
     * It queries the authRepository to find a user with the specified username and password.
     * If a matching user is found, it returns an Optional containing the authenticated user;
     * otherwise, it returns an empty Optional.
     *
     * @param dto The LoginRequestDto containing the user's login credentials.
     * @return An Optional containing the authenticated user
     */
    public Optional<Auth> doLogin(LoginRequestDto dto){
        return authRepository.findOptionalByUserNameAndPassword(dto.getUserName(),dto.getPassword());

    }


}
