package com.aga.apirest.controllers;

import com.aga.apirest.models.User;
import com.aga.apirest.services.user.UserService;
import com.aga.apirest.utils.SessionUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.aga.apirest.utils.JwtUtils.*;
import static com.aga.apirest.utils.PwdUtils.getEncryptedPassword;

@RestController
@RequestMapping("login")
@CrossOrigin
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final Object o1 = new Object();

    @Autowired
    private UserService userService;

    @PostMapping("/access")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request, HttpServletResponse response) {
        synchronized (o1){
            String email = request.get("email");
            String password = request.get("password");
            if (email.isEmpty() || password.isEmpty()) {
                return ResponseEntity.badRequest().body("Faltan campos obligatorios");
            }

            //comprobar que existe el usuario
            //User auxUser = userService.getUserForEmail(email);
            User auxUser = userService.findByNickOrEmail(email);
            if(auxUser == null){
                return ResponseEntity.badRequest().body("Usuario no existe en la aplicación");
            }

            if(auxUser.getBanned() == 1){
                return ResponseEntity.badRequest().body("Baneado de la aplicación");
            }

            //comprobar si son correcta las contraseñas
            if(!auxUser.getPassword().equals(getEncryptedPassword(password))){
                return ResponseEntity.badRequest().body("Contraseña incorrecta");
            }

            if(!SessionUtils.addSession(auxUser.getId())){
                return ResponseEntity.badRequest().body("Ya hay un usuario en la misma sesión.");
            }

            String jwt = getJWT(auxUser);
            Cookie cookie = new Cookie("jwtMobile", jwt);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 * 24);
            response.addCookie(cookie);

            return ResponseEntity.ok(jwt); // O cualquier otra respuesta que desees
        }
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

    @GetMapping("/CloserSession")
    public void CloserSession(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);
        int idUser = Integer.parseInt(checkAuth);
        SessionUtils.delete(idUser);

    }

    @GetMapping("/ResetSession")
    public void ResetSession() {
        SessionUtils.resetSession();
    }
}


