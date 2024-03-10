package com.aga.apirest.services.userpost;

import com.aga.apirest.models.Post;
import com.aga.apirest.models.User;
import com.aga.apirest.models.UserPost;

import java.util.List;

public interface IUserPostService {

    public List<UserPost> listUserPost();

    public UserPost getUserPost(int id);

    public void save(UserPost c);

    public void delete(UserPost c);

    public void deleteByUserAndPost(User u, Post p);
}
