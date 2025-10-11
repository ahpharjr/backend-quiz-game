package com.ahphar.backend_quiz_game.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // For broadcasting messages
        config.setApplicationDestinationPrefixes("/app"); // For incoming client messages (if needed)
    }

    // with web
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // replace with your frontend URL
        registry.addEndpoint("/ws-quizLeaderboard")
                .setAllowedOriginPatterns("http://localhost:5173") //allow Vite frontend
                .withSockJS();

        registry.addEndpoint("/ws-phaseLeaderboard")
                .setAllowedOriginPatterns("http://localhost:5173")
                .withSockJS();
    }
    // Work well with fronted flutter mobile app
    // @Override
    // public void registerStompEndpoints(StompEndpointRegistry registry) {
    //     registry.addEndpoint("/ws-quizLeaderboard").setAllowedOrigins("*").withSockJS();
    //     registry.addEndpoint("/ws-phaseLeaderboard").setAllowedOrigins("*").withSockJS();
    // }
}
