package com.aga.apirest.services.post;

import com.aga.apirest.models.Post;
import com.aga.apirest.models.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPostService {

    public List<Post> listPost();

    public Post getPost(int id);

    public void save(Post c);

    public void delete(Post c);

    public List<Post> findPostsByUserEmail(String user);

    public List<Post> orderDatePosts();
}
