package com.aga.apirest.services.post;

import com.aga.apirest.models.Post;
import com.aga.apirest.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService implements IPostService{

    @Autowired
    PostRepository postRepository;

    @Override
    public List<Post> listPost()  {return postRepository.findAll();}

    @Override
    public Post getPost(int id) {return postRepository.findById(id).orElse(null);}

    @Override
    public void save(Post c) {
        postRepository.save(c);
    }

    @Override
    public void delete(Post c) {
        postRepository.delete(c);
    }
}
