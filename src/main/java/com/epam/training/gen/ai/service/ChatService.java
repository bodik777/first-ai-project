package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.configuration.GenAiConfigurationProperties;
import com.epam.training.gen.ai.model.ChatRoleMessage;
import com.epam.training.gen.ai.model.PromptParameters;
import com.epam.training.gen.ai.model.UserRequest;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ChatService {

    private final Kernel kernel;
    private final Map<String, ChatCompletionService> chatCompletionServiceMap;
    private final ChatCompletionService chatCompletionService;

    private final GenAiConfigurationProperties genAiConfigurationProperties;
    private final InvocationContext invocationContext;
    private final OpenAIAsyncClient openAIAsyncClient;

    private ChatHistory chatHistory;

    public List<ChatRoleMessage> complete(UserRequest request) {
        chatHistory.addUserMessage(request.getPrompt());
        var response = Objects.requireNonNull(
                getChatCompletionService(request.getPromptParameters().model())
                        .getChatMessageContentsAsync(chatHistory, kernel,
                                getInvocationContext(request.getPromptParameters()))
                        .block());
        chatHistory.addAll(response);
        return chatHistory.getMessages()
                .stream()
                .map(chatMessageContent -> new ChatRoleMessage(chatMessageContent.getAuthorRole(), chatMessageContent.getContent(), chatMessageContent.getContentType()))
                .collect(Collectors.toList());
    }

    private ChatCompletionService getChatCompletionService(String model) {
        return Optional.ofNullable(model)
                .map(m -> chatCompletionServiceMap.computeIfAbsent(m, s -> OpenAIChatCompletion.builder()
                        .withModelId(s).withOpenAIAsyncClient(openAIAsyncClient).build())
                )
                .orElse(chatCompletionService);
    }

    private InvocationContext getInvocationContext(PromptParameters promptParameters) {
        return Optional.ofNullable(promptParameters)
                .map(PromptParameters::temperature)
                .map(temperature -> InvocationContext.copy(invocationContext)
                        .withPromptExecutionSettings(
                                PromptExecutionSettings.builder()
                                        .withTemperature(temperature)
                                        .build()
                        )
                        .build()
                )
                .orElse(invocationContext);
    }

}
