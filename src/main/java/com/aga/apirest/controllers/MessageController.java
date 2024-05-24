package com.aga.apirest.controllers;

import com.aga.apirest.models.Message;
import com.aga.apirest.models.Post;
import com.aga.apirest.models.User;
import com.aga.apirest.services.message.MessageService;
import com.aga.apirest.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static com.aga.apirest.utils.JwtUtils.checkJWTForEndPointReturnId;

@RestController
@RequestMapping("message")
@CrossOrigin
public class MessageController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Tag(name = "MessageController" , description = "Maneja el envio de mensajes entre usuarios.")
    @Operation(summary = "Se utiliza para enviar un nuevo mensaje a un usuario", description = "")
    @PostMapping("/newMessage")
    public ResponseEntity<?> newMessage(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Map<String, String> request) {

        String destinoEmail = request.get("email");
        String titulo = request.get("titulo");
        String contenido = request.get("contenido");
        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);
        User desnitoUser = userService.getUserForEmail(destinoEmail);
        User auxUser = userService.getUser(idUser);
        if(desnitoUser == null){
            return ResponseEntity.badRequest().body("No existe: " + destinoEmail);
        }

        if (desnitoUser.getEmail().equals(auxUser.getEmail())) {
            return ResponseEntity.badRequest().body("No puedes enviarte a ti mismo un mensaje");
        }

        if(titulo.isEmpty() || contenido.isEmpty()){
            return ResponseEntity.badRequest().body("Titulo o cuerpo vacío");
        }


        messageService.save(new Message(titulo,contenido,LocalDateTime.now(),userService.getUser(idUser),desnitoUser));

        return ResponseEntity.ok("Mensaje enviado");
    }

    @Tag(name = "MessageController" , description = "Maneja el envio de mensajes entre usuarios.")
    @Operation(summary = "Muestra todos los mensajes de usuario, los enviados y los recibidos.", description = "")
    @GetMapping("/getMessageUser")
    public ResponseEntity<?> getMessageUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);
        User u = userService.getUser(idUser);

        return ResponseEntity.ok(messageService.messageForUser(u.getEmail()));
    }

    @Tag(name = "MessageController" , description = "Maneja el envio de mensajes entre usuarios.")
    @Operation(summary = "Borra mensaje del usuario", description = "")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestParam String id) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);

        if(!checkId(Integer.parseInt(id))){
            return ResponseEntity.badRequest().body("Id no existe en la base de datos");
        }

        //buscamos el post y el usuario
        Message p = messageService.getMessage(Integer.parseInt(id));
        messageService.delete(p);
        return ResponseEntity.ok("mensaje eliminado");
    }

    private boolean checkId(int id){
        boolean check = true;
        Message aux = messageService.getMessage(id);
        if(aux == null){
            check = false;
        }
        return check;
    }

}
