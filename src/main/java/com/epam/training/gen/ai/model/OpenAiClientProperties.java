package com.epam.training.gen.ai.model;

public record OpenAiClientProperties(
        String endpoint,
        String modulesPath,
        String key,
        String deploymentName
) {
}