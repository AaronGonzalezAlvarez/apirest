package com.aga.apirest.services.user;

import com.aga.apirest.models.User;
import com.aga.apirest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> listUser() {return userRepository.findAll();}

    @Override
    public User getUser(int id) {return userRepository.findById(id).orElse(null);}

    @Override
    public User save(User c) {
        return userRepository.save(c);
    }

    @Override
    public void delete(User c) {
        userRepository.delete(c);
    }

    @Override
    public User getUserForEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
