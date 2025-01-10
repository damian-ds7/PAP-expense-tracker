package com.example.expenseapi.web;

import com.example.expenseapi.dto.AuthRequestDTO;
import com.example.expenseapi.dto.AuthResponseDTO;
import com.example.expenseapi.dto.RefreshTokenDTO;
import com.example.expenseapi.pojo.RefreshToken;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.RefreshTokenRepository;
import com.example.expenseapi.service.RefreshTokenService;
import com.example.expenseapi.service.UserService;
import com.example.expenseapi.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name="Authentication Controller", description = "Controller to enable registration")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered."),
            @ApiResponse(responseCode = "409", description = "A user with the provided email already exists.")
    })
    public ResponseEntity<HttpStatus> registerUser(@RequestBody User user) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticates a user with email and password, returning a JWT token upon successful authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful. JWT token returned.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access forbidden.",
                    content = @Content)
    })
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword())
        );
        String accessToken = jwtUtil.generateAccessToken(authRequestDTO.getEmail());
        RefreshToken refreshToken = refreshTokenService.findOrCreateToken(authRequestDTO.getEmail());
        return new ResponseEntity<>(new AuthResponseDTO(
                accessToken,
                refreshToken.getToken()),
                HttpStatus.OK);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh Access Token", description = "Generates a new access token using a valid refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token successfully refreshed.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJh..."))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = @Content)
    })
    public ResponseEntity<String> refreshToken(@RequestBody RefreshTokenDTO token) {
        Optional<RefreshToken> refreshToken = refreshTokenService.findByToken(token.getRefreshToken());
        if (refreshToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (refreshTokenService.isTokenExpired(token.getRefreshToken())) {
            refreshTokenRepository.delete(refreshToken.get());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String accessToken = jwtUtil.generateAccessToken(refreshToken.get().getUser().getEmail());
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }
}
