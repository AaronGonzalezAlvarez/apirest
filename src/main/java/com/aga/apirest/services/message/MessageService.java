package com.aga.apirest.services.message;

import com.aga.apirest.models.Message;
import com.aga.apirest.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MessageService implements IMessageService{

    @Autowired
    MessageRepository messageRepository;

    @Override
    public List<Message> listMessage() {return messageRepository.findAll();}

    @Override
    public Message getMessage(int id) {return messageRepository.findById(id).orElse(null);}

    @Override
    public void save(Message c) {
        messageRepository.save(c);
    }

    @Override
    public void delete(Message c) {
        messageRepository.delete(c);
    }

    @Override
    public List<Message> messageForUser(String email) {
        return messageRepository.messageForUser(email);
    }
}
