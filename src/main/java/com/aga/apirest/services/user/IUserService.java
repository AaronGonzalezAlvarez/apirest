package com.aga.apirest.services.user;

import com.aga.apirest.models.User;

import java.util.List;

public interface IUserService {

    public List<User> listUser();

    public User getUser(int id);

    public User save(User c);

    public void delete(User c);

    public User getUserForEmail(String email);

    public User findByNick(String nick);

    public User findByNickOrEmail(String nick);

    public List<User> findByNickOrEmailInclude(String x);
}
