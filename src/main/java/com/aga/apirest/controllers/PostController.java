package com.aga.apirest.controllers;

import com.aga.apirest.models.Post;
import com.aga.apirest.models.User;
import com.aga.apirest.services.post.PostService;
import com.aga.apirest.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.aga.apirest.utils.JwtUtils.checkJWTForEndPointReturnId;

@RestController
@RequestMapping("post")
@CrossOrigin
public class PostController {

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addPost(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Post post) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);

        if(post.getTitle().isEmpty() || post.getText().isEmpty()){
            return ResponseEntity.badRequest().body("Uno o más campos son nulos");
        }
        //creamos nuevo post
        Post p = new Post(post.getTitle(),post.getText(), LocalDateTime.now(),userService.getUser(idUser));
        postService.save(p);

        return ResponseEntity.ok("Nueva publicación");
    }

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
        Post p = postService.getPost(Integer.parseInt(id));
        postService.delete(p);
        return ResponseEntity.ok("Post eliminado");
    }


    @PutMapping("/update")
    public ResponseEntity<?> updatePost(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Post post) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);

        if(!checkId(post.getId())){
            return ResponseEntity.badRequest().body("Id no existe en la base de datos");
        }

        if(post.getTitle().isEmpty() || post.getText().isEmpty()){
            return ResponseEntity.badRequest().body("Uno o más campos son nulos");
        }

        Post p = postService.getPost(post.getId());
        p.setText(post.getText());
        p.setTitle(post.getTitle());
        postService.save(p);
        return ResponseEntity.ok("Post actualizado");
    }


    @GetMapping("/getPostUser")
    public ResponseEntity<?> getPostUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);
        User u = userService.getUser(idUser);

        return ResponseEntity.ok(postService.findPostsByUserEmail(u.getEmail()));
    }

    @GetMapping("/getPostTotalUsers")
    public ResponseEntity<?> getPostTotalUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }
        return ResponseEntity.ok(postService.orderDatePosts());
    }

    private boolean checkId(int id){
        boolean check = true;
        Post aux = postService.getPost(id);
        if(aux == null){
            check = false;
        }
        return check;
    }
}
