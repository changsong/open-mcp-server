package com.open.mcp.server.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

/*
 * @author dongyang
 * @date 2025/5/14, 15:52
 * @className: ChatController.java
 * @desc:
 */
@RestController
public class ChatController {
    private final AzureOpenAiChatModel chatModel;
    private final ChatClient chatClient;
    
    // Store chat history, key is session ID, value is message list
    private final ConcurrentHashMap<String, List<Message>> chatSessions = new ConcurrentHashMap<>();

    @Autowired
    public ChatController(AzureOpenAiChatModel chatModel, ChatClient.Builder chatClientBuilder, List<ToolCallbackProvider> toolCallbackProviders) {
        this.chatModel = chatModel;
        
        // Configure Builder, add all tool providers
        for (ToolCallbackProvider provider : toolCallbackProviders) {
            chatClientBuilder.defaultTools(provider);
        }
        
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/generate")
    public Map generate(@RequestParam(value = "message") String message) {
        return Map.of("generation", this.chatModel.call(message));
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.chatModel.stream(prompt);
    }

    @GetMapping("/ai/mcp")
    public Map chatImage(@RequestParam(value = "message") String message){
        String response = ChatClient.create(chatModel).prompt()
                .options(AzureOpenAiChatOptions.builder().deploymentName("gpt35-16k-gnkb2").build())
                .user(u -> u.text(message))
                .call()
                .content();
        return Map.of("generation", response);
    }
}
