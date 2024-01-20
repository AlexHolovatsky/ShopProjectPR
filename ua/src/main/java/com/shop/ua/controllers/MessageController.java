package com.shop.ua.controllers;
import com.shop.ua.models.Message;
import com.shop.ua.models.User;
import com.shop.ua.services.MessageService;
import com.shop.ua.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {
    @Autowired
    private final MessageService messageService;
    @Autowired
    private final UserService userService;

    @PostMapping("/send-message")
    public String sendMessage(
            @RequestParam("recipientEmail") String recipientEmail,
            @RequestParam("messageTitle") String messageTitle,
            @RequestParam("messageContent") String messageContent) {

        try {
            User receiver = userService.findByEmail(recipientEmail);
            User sender = userService.getCurrentUser();
            if (receiver != null && sender != null) {
                messageService.sendMessage(sender, receiver, messageTitle, messageContent);
            }
            return "redirect:/shop";
        }catch (Exception e){
            e.printStackTrace();
            return "TestNewDesign";
        }
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/TestNewDesign")
    public String getAllMessages(Model model) {
        List<Message> messages = messageService.getAllMessages();
        System.out.println("Number of messages: " + messages.size()); // Додайте цей рядок
        model.addAttribute("messages", messages);
        return "TestNewDesign";
    }


}
