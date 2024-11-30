package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.history.SimpleKernelHistory;
import com.epam.training.gen.ai.model.Response;
import com.epam.training.gen.ai.model.UserRequest;
import com.epam.training.gen.ai.promt.SimplePromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private SimplePromptService promptService;
    @Autowired
    private SimpleKernelHistory simpleKernelHistory;

    @PostMapping(path = "/postPrompt")
    public ResponseEntity<Response> processUserPrompt(@RequestBody UserRequest request) {
        List<String> chatCompletions = promptService.getChatCompletions(request.getInput());
        return ResponseEntity.ok(new Response(chatCompletions.stream().findFirst().get()));
    }

    @PostMapping(path = "/postPromptWithHistory")
    public ResponseEntity<Response> processUserPromptWithHistory(@RequestBody UserRequest request) {
        String result = simpleKernelHistory.processWithHistory(request.getInput());
        return ResponseEntity.ok(new Response(result));
    }

}
