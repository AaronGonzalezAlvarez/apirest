package com.aga.apirest.controllers;

import com.aga.apirest.models.Post;
import com.aga.apirest.models.User;
import com.aga.apirest.models.UserPost;
import com.aga.apirest.services.post.PostService;
import com.aga.apirest.services.user.UserService;
import com.aga.apirest.services.userpost.UserPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aga.apirest.utils.JwtUtils.checkJWTForEndPointReturnId;

@RestController
@RequestMapping("post")
@CrossOrigin
public class PostController {

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserPostService userPostService;

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
        Post p = new Post(post.getTitle(),post.getText(), LocalDateTime.now());
        postService.save(p);

        //relacionamos el post con el usuario que lo ha creado
        User user = userService.getUser(idUser);

        UserPost up = new UserPost(user,p);
        userPostService.save(up);

        return ResponseEntity.ok(up);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Post post) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);

        if(!checkId(post.getId())){
            return ResponseEntity.badRequest().body("Id no existe en la base de datos");
        }

        //buscamos el post y el usuario
        Post p = postService.getPost(post.getId());
        User u = userService.getUser(idUser);
        //primero borramos la relacion intermedia
        userPostService.deleteByUserAndPost(u,p);
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
        ArrayList<Post> posts = new ArrayList<>(0);
        for(UserPost x:u.getUserPostsCreator()){
            posts.add(x.getPost());
        }
        Map<String, Object> json = new HashMap<>();
        json.put("totalPosts", posts.size());
        json.put("posts", posts);

        return ResponseEntity.ok(json);
    }

    @GetMapping("/getPostTotalUser")
    public ResponseEntity<?> getPostTotalUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        List<UserPost> up = userPostService.listUserPost();

        return ResponseEntity.ok("json");
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
