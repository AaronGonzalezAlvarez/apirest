package com.aga.apirest.services.post;

import com.aga.apirest.models.Post;

import java.util.List;

public interface IPostService {

    public List<Post> listPost();

    public Post getPost(int id);

    public void save(Post c);

    public void delete(Post c);
}
