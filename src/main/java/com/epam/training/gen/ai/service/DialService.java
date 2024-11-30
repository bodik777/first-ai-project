package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.configuration.GenAiConfigurationProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class DialService {

    private final HttpClient client = HttpClient.newHttpClient();
    @Autowired
    private GenAiConfigurationProperties genAiProps;
    @Autowired
    private ObjectMapper mapper;

    public List<String> getModels() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(genAiProps.openaiClient().endpoint() + genAiProps.openaiClient().modulesPath()))
                .header("Api-Key", genAiProps.openaiClient().key()).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode rootNode = mapper.readTree(response.body());
        JsonNode dataArray = rootNode.get("data");
        List<String> ids = new ArrayList<>();
        dataArray.forEach(node -> ids.add(node.get("model").asText()));
        return ids;
    }

}
