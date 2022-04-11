package example.greetings.Controller;

import example.greetings.Models.ChatMessage;
import example.greetings.Models.Message;
import example.greetings.Models.User;
import example.greetings.interfaces.ChatMessageRepo;
import example.greetings.interfaces.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    ChatMessageRepo MsRepo;

    @Autowired
    private UserRepo userRepo;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage,
                                   @AuthenticationPrincipal User user,
                                   @RequestParam String text,
                                   Model model) {

        //userRepo.findByUsername(user);
        ChatMessage message =new ChatMessage(text,user);
        MsRepo.save(message);
        Iterable<ChatMessage> messages= MsRepo.findAll();
        model.addAttribute("messages", messages);

        return message;
    }


    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor,
                               @AuthenticationPrincipal User user,
                               @RequestParam String type,
                               Model model){

        // Add username in web socket session
//        ChatMessage message =new ChatMessage(ChatMessage.MessageType.JOIN,user);
//        MsRepo.save(message);
//        Iterable<ChatMessage> messages= MsRepo.findAll();
//        model.addAttribute("messages", messages);
        headerAccessor.getSessionAttributes().put("username", user.getUsername());
        return chatMessage;
    }
}