package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ChatRoleMessage;
import com.epam.training.gen.ai.model.UserRequest;
import com.epam.training.gen.ai.service.ChatService;
import com.epam.training.gen.ai.service.DialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;
    @Autowired
    private DialService dialService;

    @GetMapping(path = "/models")
    public ResponseEntity<Object> getModels()
            throws IOException, InterruptedException {
        return ResponseEntity.ok(dialService.getModels());
    }

    @PostMapping(path = "/chat")
    public ResponseEntity<List<ChatRoleMessage>> processUserPrompt(@RequestBody UserRequest request) {
        return ResponseEntity.ok(chatService.complete(request));
    }

}
