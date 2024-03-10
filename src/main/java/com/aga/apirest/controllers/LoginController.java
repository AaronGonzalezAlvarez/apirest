package com.aga.apirest.controllers;

import com.aga.apirest.models.User;
import com.aga.apirest.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.aga.apirest.utils.JwtUtils.devolverValoresJWT;
import static com.aga.apirest.utils.JwtUtils.getJWT;
import static com.aga.apirest.utils.PwdUtils.getEncryptedPassword;

@RestController
@RequestMapping("login")
@CrossOrigin
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/access")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");
        if (email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Faltan campos obligatorios");
        }

        //comprobar que existe el usuario
        User auxUser = userService.getUserForEmail(email);
        if(auxUser == null){
            return ResponseEntity.badRequest().body("Usuario no existe en la aplicación");
        }

        //comprobar si son correcta las contraseñas
        if(!auxUser.getPassword().equals(getEncryptedPassword(password))){
            return ResponseEntity.badRequest().body("Contraseña incorrecta");
        }

        return ResponseEntity.ok(getJWT(auxUser)); // O cualquier otra respuesta que desees
    }

    @PostMapping("/check")
    public ResponseEntity<String> check(@RequestBody Map<String, String> request) {
        String jwt = request.get("jwt");
        if(jwt.isEmpty()){
            return ResponseEntity.badRequest().body("Campo vacío");
        }
        String jwtData;
        try {
            jwtData = devolverValoresJWT(jwt);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("JWT corructo");
        }

        return ResponseEntity.ok(jwtData);
    }
}


