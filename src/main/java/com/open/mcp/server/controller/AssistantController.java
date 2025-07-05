package com.open.mcp.server.controller;

import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/ai-assistant")
public class AssistantController {

    @Autowired
    private AzureOpenAiChatModel azureOpenAiChatModel;

    // You have already registered multiple ToolCallbackProviders, here you can select one or merge them
    @Autowired
    private List<ToolCallbackProvider> toolCallbackProviders;
    
    // Store chat history, the key is the session ID, and the value is the message list
    private final ConcurrentHashMap<String, List<Message>> chatSessions = new ConcurrentHashMap<>();

    @Autowired
    public AssistantController(AzureOpenAiChatModel azureOpenAiChatModel, List<ToolCallbackProvider> toolCallbackProviders) {
        this.azureOpenAiChatModel = azureOpenAiChatModel;
        this.toolCallbackProviders = toolCallbackProviders;
    }

    /**
     * Single Q&A, does not save session state
     */
    @GetMapping("/ai/ask")
    public String ask(@RequestParam String question) {
        ChatClient.Builder builder = ChatClient.builder(azureOpenAiChatModel);
        // Dynamically inject all MCP tools
        for (ToolCallbackProvider provider : toolCallbackProviders) {
            builder.defaultTools(provider.getToolCallbacks());
        }
        ChatClient chatClient = builder.build();
        return chatClient.prompt(question).call().content();
    }
    
    /**
     * Create a new session
     * @return Session ID and initial state
     */
    @PostMapping("/session/create")
    public Map<String, Object> createSession() {
        String sessionId = UUID.randomUUID().toString();
        List<Message> messages = new ArrayList<>();
        
        // Add system message as the start of the session
        messages.add(new SystemMessage("我是供应链小助手。"));
        chatSessions.put(sessionId, messages);
        
        return Map.of(
            "sessionId", sessionId,
            "status", "Session created",
            "message", "Welcome to use the supply chain assistant, you can start asking related questions"
        );
    }
    
    /**
     * Send a message and get a reply in the current session
     * @param sessionId Session ID
     * @param message User message
     * @param systemPrompt System prompt, used to define the role played by the large model, optional parameter
     * @return AI reply
     */
    @PostMapping("/session/chat")
    public Map<String, Object> chat(@RequestParam String sessionId, @RequestParam String message, 
                                    @RequestParam(required = false) String systemPrompt) {
        // Check if the session exists
        if (!chatSessions.containsKey(sessionId)) {
            return Map.of("error", "Session does not exist, please create a session first");
        }
        
        // Get session history
        List<Message> messages = chatSessions.get(sessionId);
        
        // If a new system prompt is provided, update the system message
        if (systemPrompt != null && !systemPrompt.trim().isEmpty()) {
            // Remove the old system message (if exists)
            messages.removeIf(msg -> msg instanceof SystemMessage);
            // Add new system message
            messages.add(0, new SystemMessage(systemPrompt));
        }
        
        // Add user message to history
        UserMessage userMessage = new UserMessage(message);
        messages.add(userMessage);
        
        // Build prompt containing the complete history
        Prompt prompt = new Prompt(messages);
        
        // Create ChatClient and add tools
        ChatClient.Builder builder = ChatClient.builder(azureOpenAiChatModel);
        // Dynamically inject all MCP tools
        for (ToolCallbackProvider provider : toolCallbackProviders) {
            builder.defaultTools(provider.getToolCallbacks());
        }
        ChatClient chatClient = builder.build();
        
        // Get reply
        String content = chatClient.prompt(prompt).call().content();
        
        // Add assistant reply to session history
        AssistantMessage assistantMessage = new AssistantMessage(content);
        messages.add(assistantMessage);
        
        // Update session state
        chatSessions.put(sessionId, messages);
        
        return Map.of(
            "sessionId", sessionId,
            "response", content,
            "messageCount", messages.size()
        );
    }
    
    /**
     * Get session history
     * @param sessionId Session ID
     * @return Session history messages
     */
    @GetMapping("/session/history")
    public Map<String, Object> getSessionHistory(@RequestParam String sessionId) {
        if (!chatSessions.containsKey(sessionId)) {
            return Map.of("error", "Session does not exist");
        }
        
        List<Message> messages = chatSessions.get(sessionId);
        List<Map<String, String>> history = new ArrayList<>();
        
        for (Message message : messages) {
            Map<String, String> entry = new HashMap<>();
            entry.put("role", message.getMessageType().toString());
            
            // Get content according to message type
            String content = "";
            if (message instanceof UserMessage) {
                content = ((UserMessage) message).getText();
            } else if (message instanceof AssistantMessage) {
                content = ((AssistantMessage) message).getText();
            } else if (message instanceof SystemMessage) {
                content = ((SystemMessage) message).getText();
            }
            
            entry.put("content", content);
            history.add(entry);
        }
        
        return Map.of(
            "sessionId", sessionId,
            "history", history,
            "messageCount", messages.size()
        );
    }
    
    /**
     * Clear session history or reset session
     * @param sessionId Session ID
     * @param reset Whether to reset (true means keep the session ID but clear history and initialize, false means completely delete the session)
     * @return Operation result
     */
    @PostMapping("/session/clear")
    public Map<String, String> clearSession(@RequestParam String sessionId, @RequestParam(required = false, defaultValue = "false") boolean reset) {
        if (!chatSessions.containsKey(sessionId)) {
            return Map.of("status", "Session does not exist");
        }
        
        if (reset) {
            // Reset session, keep ID but clear history
            List<Message> messages = new ArrayList<>();
            messages.add(new SystemMessage("我是供应链小助手。"));
            chatSessions.put(sessionId, messages);
            return Map.of("status", "Session reset");
        } else {
            // Completely delete session
            chatSessions.remove(sessionId);
            return Map.of("status", "Session deleted");
        }
    }
}