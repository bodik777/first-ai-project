package com.epam.training.gen.ai.configuration;

import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class ChatConfiguration {

    @Bean
    @SessionScope
    public ChatHistory getChatHistory() {
        return new ChatHistory();
    }

}
