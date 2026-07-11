package com.shobu.service;

import com.shobu.api.dto.request.LogInRequest;
import com.shobu.api.dto.request.SignUpRequest;
import com.shobu.api.dto.response.AuthResponse;
import com.shobu.api.errors.apiExceptions.InvalidLoginException;
import com.shobu.api.errors.apiExceptions.SignUpException;
import com.shobu.data.entity.PlayerProfile;
import com.shobu.data.repository.PlayerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class AuthService {
    private final PlayerRepository playerRepository;
    private  final BCryptPasswordEncoder passwordEncoder;

    public AuthService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public AuthResponse loginUser(LogInRequest request){
        PlayerProfile player = playerRepository.findByPlayerName(request.username())
                .orElseThrow(InvalidLoginException::new);

       if (!passwordEncoder.matches(request.password(), player.getPlayerPassword())){
           throw new InvalidLoginException();
       }

       return new AuthResponse(player.getPlayerId(), player.getPlayerName());
    }

    public AuthResponse signupUser(SignUpRequest request){
        if (request.username().isBlank()  || request.password().isBlank()){
            throw new SignUpException("Username or Password is Blank");
        }

        if (playerRepository.findByPlayerName(request.username()).isPresent()){
            throw new SignUpException("Username " + request.username() + "allready exists");
        }

        String passwordHash = passwordEncoder.encode(request.password());

        PlayerProfile player = new PlayerProfile(request.username(), passwordHash);

        PlayerProfile savedPlayer = playerRepository.save(player);

        return new AuthResponse(savedPlayer.getPlayerId(), savedPlayer.getPlayerName());

    }

}

