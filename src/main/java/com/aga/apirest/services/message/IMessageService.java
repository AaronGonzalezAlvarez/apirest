package com.aga.apirest.services.message;

import com.aga.apirest.models.Message;

import java.util.List;

public interface IMessageService {

    public List<Message> listMessage();

    public Message getMessage(int id);

    public void save(Message c);

    public void delete(Message c);

    public List<Message> messageForUser(String email);
}
