package com.aga.apirest.services.userpost;

import com.aga.apirest.models.Post;
import com.aga.apirest.models.User;
import com.aga.apirest.models.UserPost;
import com.aga.apirest.repository.UserPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPostService implements IUserPostService{

    @Autowired
    UserPostRepository userPostRepository;

    @Override
    public List<UserPost> listUserPost() {return userPostRepository.findAll();}

    @Override
    public UserPost getUserPost(int id) {return userPostRepository.findById(id).orElse(null);}

    @Override
    public void save(UserPost c) {
        userPostRepository.save(c);
    }

    @Override
    public void delete(UserPost c) {
        userPostRepository.delete(c);
    }

    @Override
    public void deleteByUserAndPost(User u, Post p) {
        userPostRepository.deleteByUserPost(u,p);
    }
}
