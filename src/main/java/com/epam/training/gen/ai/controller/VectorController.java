package com.epam.training.gen.ai.controller;

import com.azure.ai.openai.models.EmbeddingItem;
import com.epam.training.gen.ai.vector.SimpleVectorActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class VectorController {

    @Autowired
    private SimpleVectorActions vectorActions;

    @PostMapping(path = "/buildEmbedding")
    public ResponseEntity<List<EmbeddingItem>> buildEmbedding(@RequestBody String request) {
        return ResponseEntity.ok(vectorActions.getEmbeddings(request));
    }

    @PostMapping(path = "/buildAndSaveEmbedding")
    public ResponseEntity<Object> buildAndSaveEmbedding(@RequestBody String request)
            throws ExecutionException, InterruptedException {
        vectorActions.processAndSaveText(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/search")
    public ResponseEntity<Object> search(@RequestBody String request)
            throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(vectorActions.search(request));
    }

}
