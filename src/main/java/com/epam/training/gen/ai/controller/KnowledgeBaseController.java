package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ChatRoleMessage;
import com.epam.training.gen.ai.service.KnowledgeBaseService;
import com.epam.training.gen.ai.vector.SimpleVectorActions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Controller
public class KnowledgeBaseController {

    @Autowired
    private KnowledgeBaseService knowledgeBaseService;
    @Autowired
    private SimpleVectorActions vectorActions;

    @PostMapping(path = "/loadKbDataToVectorDb")
    public ResponseEntity<String> processUserPrompt() {
        loadFileToDb("FoodServices.txt");
        loadFileToDb("OfficeRules.txt");

        return ResponseEntity.ok().build();
    }

    private void loadFileToDb(String file) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file)) {
            if (inputStream != null) {
                String text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                vectorActions.processAndSaveText(text);
            }
        } catch (Exception e) {
            log.error("Unable to load prepared data to vector db", e);
        }
    }

    @PostMapping(path = "/kb")
    public ResponseEntity<List<ChatRoleMessage>> processUserPrompt(@RequestBody String request)
            throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(knowledgeBaseService.complete(request));
    }

}
