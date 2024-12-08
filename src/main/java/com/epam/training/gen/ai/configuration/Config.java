package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.epam.training.gen.ai.plugins.HomeControlPlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
@EnableConfigurationProperties(GenAiConfigurationProperties.class)
@RequiredArgsConstructor
public class Config {

    private final GenAiConfigurationProperties genAiConfigurationProperties;

    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(genAiConfigurationProperties.openaiClient().key()))
                .endpoint(genAiConfigurationProperties.openaiClient().endpoint())
                .buildAsyncClient();
    }

    @Bean
    public InvocationContext invocationContext() {
        return InvocationContext.builder()
//                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .withPromptExecutionSettings(PromptExecutionSettings.builder()
                        .withTemperature(genAiConfigurationProperties.temperature())
                        .build())
                .build();
    }

    @Bean
    public ChatCompletionService chatCompletionService(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(genAiConfigurationProperties.openaiClient().deploymentName())
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }

    @Bean
    public Kernel kernel(ChatCompletionService chatCompletionService,
                         KernelPlugin creatorInfoPlugin) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .withPlugin(creatorInfoPlugin)
                .build();
    }

    @Bean
    public KernelPlugin getHomeControlPlugin() {
        return KernelPluginFactory.createFromObject(
                new HomeControlPlugin(), "HomeControlPlugin");
    }

    @Bean
    @SessionScope
    public ChatHistory getChatHistory() {
        return new ChatHistory();
    }

}
