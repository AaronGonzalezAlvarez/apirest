package com.aga.apirest.controllers;

import com.aga.apirest.models.User;
import com.aga.apirest.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.aga.apirest.utils.JwtUtils.*;
import static com.aga.apirest.utils.PwdUtils.getEncryptedPassword;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        //comprobar que los datos no vengan nulos
        if(user.getName().isEmpty() || user.getSurname().isEmpty() || user.getPhone() == null || user.getRol() == null || user.getProvince().isEmpty() || user.getNick().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()){
            return ResponseEntity.badRequest().body("Uno o más campos son nulos");
        }
        //cifrar contraseña
        String pwd = getEncryptedPassword(user.getPassword());
        user.setPassword(pwd);
        try {
            userService.save(user);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Campo nick o email ya existen en la aplicación");
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody User user) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);

        if(user.getName().isEmpty() || user.getSurname().isEmpty() || user.getPhone() == null || user.getProvince().isEmpty()){
            return ResponseEntity.badRequest().body("Uno o más campos son nulos");
        }
        //obtener usuario de base de datos
        User u = userService.getUser(idUser);
        //update
        u.setName(user.getName());
        u.setSurname(user.getSurname());
        u.setPhone(user.getPhone());
        u.setProvince(user.getProvince());
        userService.save(u);
        return ResponseEntity.ok(u);
    }

    @PutMapping("/changeRolTwo")
    public ResponseEntity<?> changeRolTwo(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);

        int rol = Integer.valueOf(checkJWTForEndPointReturnRol(authorizationHeader));

        if(rol == 2){
            return ResponseEntity.badRequest().body("Ya no puede subir de rol");
        }

        //obtener usuario de base de datos
        User u = userService.getUser(idUser);
        //update
        u.setRol(2);
        userService.save(u);
        return ResponseEntity.ok(u);
    }
}
