package com.epam.training.gen.ai.model;

import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.message.ChatMessageContentType;

public record ChatRoleMessage(
        AuthorRole authorRole,
        String message,
        ChatMessageContentType type
) {
}
