package com.aga.apirest.controllers;

import com.aga.apirest.models.Activity;
import com.aga.apirest.models.Message;
import com.aga.apirest.models.Post;
import com.aga.apirest.models.User;
import com.aga.apirest.services.activity.ActivityService;
import com.aga.apirest.services.message.MessageService;
import com.aga.apirest.services.post.PostService;
import com.aga.apirest.services.user.UserService;
import com.aga.apirest.utils.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aga.apirest.utils.JwtUtils.*;
import static com.aga.apirest.utils.PwdUtils.getEncryptedPassword;
import static com.aga.apirest.utils.Utils.isValidEmail;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private PostService postService;

    @Autowired
    private ActivityService activityService;

    @Tag(name = "UserController" , description = "Gestiona el concepto de usuario.")
    @Operation(summary = "Crea un nuevo usuario.", description = "")
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        //comprobar que los datos no vengan nulos
        if(user.getName().isEmpty() || user.getSurname().isEmpty() || user.getPhone() == null || user.getRol() == null || user.getProvince().isEmpty() || user.getNick().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()){
            return ResponseEntity.badRequest().body("Uno o más campos son nulos");
        }
        if(!isValidEmail(user.getEmail())){
            return ResponseEntity.badRequest().body("Email no valido");
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

    @Tag(name = "UserController" , description = "Gestiona el concepto de usuario.")
    @Operation(summary = "Actualiza un usuario.", description = "")
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

    @Tag(name = "UserController" , description = "Gestiona el concepto de usuario.")
    @Operation(summary = "Cambia el rol de un usuario, de consumidor a ofertante.", description = "")
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

    @Tag(name = "UserController" , description = "Gestiona el concepto de usuario.")
    @Operation(summary = "Banea a un usuario, no puede acceder a la aplicación.", description = "")
    @PutMapping("/banUser")
    public ResponseEntity<?> banUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Map<String, String> request) {

        String email = request.get("email");

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);
        User userAux = userService.getUser(idUser);
        if(userAux.getRol() == 3){
            User u = userService.getUserForEmail(email);
            if(u.getBanned() == 0){
                u.setBanned(1);
            } else if (u.getBanned() == 1) {
                u.setBanned(0);
            }
            userService.save(u);
            return ResponseEntity.ok(u);
        }else{
            return ResponseEntity.badRequest().body("No tienes permiso para esta acción.");
        }
    }

    @Tag(name = "UserController" , description = "Gestiona el concepto de usuario.")
    @Operation(summary = "Muestra todos los usuarios.", description = "")
    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }
        return ResponseEntity.ok(userService.listUser());
    }

    @Tag(name = "UserController" , description = "Gestiona el concepto de usuario.")
    @Operation(summary = "Muestra los post de un usuario.", description = "")
    @GetMapping("/getPostForUser/{idUser}")
    public ResponseEntity<?> getPostForUser(@PathVariable String idUser, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {


        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        if(userService.getUser(Integer.parseInt(idUser)) == null){
            return ResponseEntity.badRequest().body("Id no existe");
        }

        User user = userService.getUser(Integer.parseInt(idUser));
        List<Post> posts = user.getPosts();
        int totalPosts = posts.size();

        Map<String, Object> response = new HashMap<>();
        response.put("totalPosts", totalPosts);
        response.put("posts", posts);

        return ResponseEntity.ok(response);
    }

    @Tag(name = "UserController" , description = "Gestiona el concepto de usuario.")
    @Operation(summary = "Busca usuario por name o email.", description = "")
    @GetMapping("/findUserForNickOrEmail/{data}")
    public ResponseEntity<?> findUserForNickOrEmail(@PathVariable String data, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        List<User> users = userService.findByNickOrEmailInclude(data);
        int totalUser = users.size();

        Map<String, Object> response = new HashMap<>();
        response.put("total", totalUser);
        response.put("users", users);

        return ResponseEntity.ok(response);
    }

    @Tag(name = "UserController" , description = "Gestiona el concepto de usuario.")
    @Operation(summary = "Nos da información de un usuario.", description = "")
    @GetMapping("/getUserData/{idUser}")
    public ResponseEntity<?> getUserData(@PathVariable String idUser, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        if(userService.getUser(Integer.parseInt(idUser)) == null){
            return ResponseEntity.badRequest().body("Id no existe");
        }

        User user = userService.getUser(Integer.parseInt(idUser));
        List<Post> posts = user.getPosts();
        List<Activity> activities = user.getActivitiescreated();
        List<Activity> inscription = user.getRegisteredActivities();

        Map<String, Object> response = new HashMap<>();
        response.put("data",user );
        response.put("posts", posts);
        response.put("activity", activities);
        response.put("inscription", inscription);

        return ResponseEntity.ok(response);
    }

    @Tag(name = "UserController" , description = "Gestiona el concepto de usuario.")
    @Operation(summary = "da de baja a un usuario.", description = "")
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }
        int idUser = Integer.parseInt(checkAuth);
        User u = userService.getUser(idUser);

        for(Activity q : u.getRegisteredActivities()){
           int index=  q.getUsers().indexOf(u);
           q.getUsers().remove(index);
            activityService.save(q);
        }

        for(Activity q : u.getActivitiescreated()){
            q.getUsers().clear();
            activityService.delete(q);
        }
        //u.getActivitiescreated().clear();
        for (Post x: u.getPosts()){
            postService.delete(x);
        }
        for (Message x: u.getMessagesAddressee()){
            messageService.delete(x);
        }
        for (Message x: u.getMessagesEmitter()){
            messageService.delete(x);
        }

        SessionUtils.delete(idUser);
        userService.delete(u);
        return ResponseEntity.ok(u);
    }
}
