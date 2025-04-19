package com.example.soundarchive.controller;

import com.example.soundarchive.dao.impl.AuthDAO;
import com.example.soundarchive.mapper.UserMapper;
import com.example.soundarchive.model.UserEntity;
import com.example.soundarchive.model.dto.ChangePasswordDTO;
import com.example.soundarchive.model.dto.LoginDTO;
import com.example.soundarchive.model.dto.RegisterDTO;
import com.example.soundarchive.model.dto.UserDTO;
import com.example.soundarchive.service.UserService;
import com.example.soundarchive.util.jwt.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/soundArchive/auth")
public class AuthController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthDAO authDAO;
    private final UserMapper userMapper;

    @Autowired
    public AuthController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil, AuthDAO authDAO, UserMapper userMapper) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.authDAO = authDAO;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterDTO registerDTO){

        if(!registerDTO.getPassword1().equals(registerDTO.getPassword2())) throw new IllegalArgumentException("Password one and two don't match! ");

        UserDTO userDTO = userMapper.mapRegister(registerDTO);
        String encodedPassword = bCryptPasswordEncoder.encode(registerDTO.getPassword1());
        userDTO.setPassword(encodedPassword);
        userDTO.setPermissionLevel(1);
        userDTO = userService.save(userDTO);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        String token = jwtUtil.generateToken(authentication.getName());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    @PutMapping("/changePassword")
    public ResponseEntity<UserDTO> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {

        if(!changePasswordDTO.getOldPassword1().equals(changePasswordDTO.getOldPassword2())) throw new IllegalArgumentException("Old password one and two don't match! ");

        //some logic

        return new ResponseEntity<>(null, HttpStatus.OK);
    }




    @GetMapping("/username/{username}")
    public ResponseEntity<UserEntity> getUsers(@PathVariable("username") String username){

        UserEntity user = authDAO.getUserbyUsername(username);
        user.setPassword(null);

        return new ResponseEntity<>(user,
                HttpStatus.OK);
    }

}
