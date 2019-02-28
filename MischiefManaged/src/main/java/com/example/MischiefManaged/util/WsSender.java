package com.example.MischiefManaged.util;


import com.example.MischiefManaged.dto.EventType;
import com.example.MischiefManaged.dto.ObjectType;
import com.example.MischiefManaged.dto.WsEventDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class WsSender {
    private final SimpMessagingTemplate template;       /*Этот Bean отвечает за отправку сообщений через очереди сообщений в Spring*/

    private final ObjectMapper mapper;      /*этот bean сериализует и десериализует наш объект(он определяет какие поля нужно отобразить в JsonView,
                                             но при работе с вебсокитами mapper не получает анатацию JsonView,
                                             поэтому мы должны подложить ее руками)*/

    public WsSender(SimpMessagingTemplate template, ObjectMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    public <T> BiConsumer<EventType, T> getSender(ObjectType objectType, Class view){      /*Когда создаем новый Sender, мы сразу знаем что за объект отправлять клиенту*/ /*Consumer<T> выполняет некоторое действие над объектом типа T, при этом ничего не возвращая(BiConsumer - позваляет принимать 2 аргумента)*/
        ObjectWriter writer = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(view);      /*Определяет JsonView*/

        return (EventType eventType, T payload) -> {        /*Sender может отправлять для данного объекта разные типы операций*/
            String value = null;        /*Если будет ошибка,мы увидим в консоле сообщение об ошибке, а клиенту уйдет пустое сообщение*/
            try{
                value = writer.writeValueAsString(payload);
            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            template.convertAndSend(
                    "/topic/activity",      /*"/topic/activity" это очередь в которую отправляем сообщение*/
                    new WsEventDto(objectType, eventType, value)
            );
        };
    }
}
