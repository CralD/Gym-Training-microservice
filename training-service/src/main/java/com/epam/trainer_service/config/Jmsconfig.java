package com.epam.trainer_service.config;

import com.epam.trainer_service.dto.TrainerWorkloadDto;
import jakarta.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import java.util.HashMap;
import java.util.Map;

    @Configuration
    @EnableJms
    public class Jmsconfig {
        @Value("${spring.activemq.broker-url}")
        private String brokerUrl;
        @Value("${spring.activemq.user}")
        private String user;
        @Value("${spring.activemq.password}")
        private String password;

        @Autowired
        private ConnectionFactory connectionFactory;

        @Bean
        public MessageConverter jacksonJmsMessageConverter() {
            MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
            Map<String, Class<?>> typeIdMappings = new HashMap<String, Class<?>>();
            typeIdMappings.put("TrainerWorkloadDto", TrainerWorkloadDto.class);
            converter.setTypeIdMappings(typeIdMappings);
            converter.setTargetType(MessageType.TEXT);
            converter.setTypeIdPropertyName("_TYPE");
            return converter;
        }

        @Bean
        public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
            DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
            factory.setConnectionFactory(connectionFactory);
            factory.setMessageConverter(jacksonJmsMessageConverter());
            factory.setDestinationResolver(new DynamicDestinationResolver());
            return factory;
        }

    }
