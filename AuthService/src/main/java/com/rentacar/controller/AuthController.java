package com.rentacar.controller;

import com.rentacar.dto.request.LoginRequestDto;
import com.rentacar.dto.request.RegisterRequestDto;
import com.rentacar.entity.Auth;
import com.rentacar.exception.AuthServiceException;
import com.rentacar.exception.ErrorType;
import com.rentacar.service.AuthService;
import com.rentacar.utility.JwtTokenManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.rentacar.constants.RestApiUrls.*;


@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenManager jwtTokenManager;

    @PostMapping(REGISTER)
    public ResponseEntity<Boolean> register(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> doLogin(@RequestBody @Valid LoginRequestDto dto){
        Optional<Auth> auth = authService.doLogin(dto);
        if (auth.isEmpty())
            throw new AuthServiceException(ErrorType.ERROR_INVALID_LOGIN_PARAMETER);
        Optional<String> token = jwtTokenManager.createToken(auth.get().getId());
        if (token.isEmpty())
            throw new AuthServiceException(ErrorType.ERROR_CREATE_TOKEN);
        return ResponseEntity.ok(token.get());
    }
}
