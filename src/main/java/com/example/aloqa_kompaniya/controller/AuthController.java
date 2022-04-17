package com.example.aloqa_kompaniya.controller;

import com.example.aloqa_kompaniya.dto.ApiResponse;
import com.example.aloqa_kompaniya.dto.LoginDto;
import com.example.aloqa_kompaniya.dto.RegisterDto;
import com.example.aloqa_kompaniya.entity.Employee;
import com.example.aloqa_kompaniya.security.JwtProvider;
import com.example.aloqa_kompaniya.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    final AuthenticationManager authenticationManager;
    final AuthService authService;
    final JwtProvider jwtProvider;

    @PostMapping("login")
    public HttpEntity<?> login(@RequestBody LoginDto dto){
        String token = jwtProvider.generateToken(dto.getUsername());
        return ResponseEntity.ok().body(token);
    }
    @GetMapping("/me")
    public HttpEntity<?> getMe(Employee employee) { //Parametr
        return ResponseEntity.ok().body("Mana" + employee);
    }
    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody RegisterDto dto) throws MessagingException {
        return ResponseEntity.ok().body(authService.register(dto));
    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> verify(@RequestParam String email, @RequestParam String emailCode) {

        ApiResponse response = authService.verify(email, emailCode);
        return ResponseEntity.status(response.isSuccess()?
                HttpStatus.OK:HttpStatus.CONFLICT).body(response);
    }

}
