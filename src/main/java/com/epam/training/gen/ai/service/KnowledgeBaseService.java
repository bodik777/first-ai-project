package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.ChatRoleMessage;
import com.epam.training.gen.ai.model.PromptParameters;
import com.epam.training.gen.ai.model.UserRequest;
import com.epam.training.gen.ai.vector.SimpleVectorActions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@AllArgsConstructor
public class KnowledgeBaseService {

    private ChatService chatService;
    private SimpleVectorActions vectorActions;
    private final String PROMPT_TEMPLATE = """
              Assistant helps the company workers with handbook.
              Be brief in your answers.
              If there isn't enough information below, say you don't know.
              Do not generate answers that don't use the context.
              If asking a clarifying question to the user would help, ask the question.
              For tabular information return it as an html table.
              Do not return markdown format.
              If the question is not in English, answer in the language used in the question.
              Answer the query strictly referring the provided context:
              {context}
              Query:
              {query}
            """;

    public List<ChatRoleMessage> complete(String request)
            throws ExecutionException, InterruptedException {
        String vectorResponse = vectorActions.search(request);
        String search = PROMPT_TEMPLATE.replace("{context}", vectorResponse)
                .replace("{query}", request);

        UserRequest userRequest = new UserRequest();
        userRequest.setPromptParameters(new PromptParameters(0d, "gpt-35-turbo"));
        userRequest.setPrompt(search);
        return chatService.complete(userRequest);
    }

}
