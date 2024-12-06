package com.epam.training.gen.ai.configuration;

import com.epam.training.gen.ai.model.OpenAiClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.gen-ai")
public record GenAiConfigurationProperties(
        OpenAiClientProperties openaiClient,
        double temperature
) {

}
