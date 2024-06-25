package com.alhajj.cms.controllers;

import com.alhajj.cms.model.UserEntity;
import com.alhajj.cms.model.dto.UserDto;
import com.alhajj.cms.services.UserService;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    UserService userService;
    ModelMapper mapper;
    public UserController(UserService userService, ModelMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> createuser(@RequestBody UserDto userDto) {
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        UserEntity savedUser = userService.saveUser(userEntity);
        return new ResponseEntity<>(mapper.map(savedUser, UserDto.class), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<UserEntity> userEntity = userService.getAllUsers();
        List<UserDto> userDto = userEntity.stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDto> getUserByID(@PathVariable("id") Long id) {
        Optional<UserEntity> userEntity = userService.getUserById(id);
        return userEntity.map(entity -> new ResponseEntity<>(mapper.map(entity, UserDto.class), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        if(!userService.isExist(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            userDto.setId(id);
            UserEntity userEntity = mapper.map(userDto, UserEntity.class);
            UserEntity saveUser = userService.saveUser(userEntity);
            return new ResponseEntity<>(mapper.map(saveUser, UserDto.class),HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
