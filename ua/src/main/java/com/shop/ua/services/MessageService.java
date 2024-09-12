package com.shop.ua.services;


import com.shop.ua.models.Message;
import com.shop.ua.models.User;
import com.shop.ua.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void sendMessage(User sender, User receiver, String title, String content) {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setTitle(title);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());
        messageRepository.save(message);
    }

    public List<Message> getReceivedMessages(User user) {
        return messageRepository.findByReceiver(user);
    }

    public List<Message> getSentMessages(User user) {
        return messageRepository.findBySender(user);
    }


    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
