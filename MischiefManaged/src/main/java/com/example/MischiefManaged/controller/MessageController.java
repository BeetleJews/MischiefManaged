package com.example.MischiefManaged.controller;


import com.example.MischiefManaged.domain.Message;
import com.example.MischiefManaged.domain.Views;
import com.example.MischiefManaged.dto.EventType;
import com.example.MischiefManaged.dto.ObjectType;
import com.example.MischiefManaged.repo.MessageRepo;
import com.example.MischiefManaged.util.WsSender;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageRepo messageRepo;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageController(MessageRepo messageRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.FullMessage.class);        /*При помощи JsonView ограничиваем поля, которые будем отправлять клиенту 2х (до id и текста)*/
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> list() {
        return messageRepo.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message) {
        message.setCreationDate(LocalDateTime.now());
        Message updatedMessage = messageRepo.save(message);

        wsSender.accept(EventType.CREATE, updatedMessage);

        return updatedMessage;
    }

    @PutMapping("{id}")
    public Message update(
            @PathVariable("id") Message messageFromDb,      //сообщение которое бунем менять
            @RequestBody Message message)       //сообщение на которое нужно изменить
    {
        message.setCreationDate(LocalDateTime.now());
        BeanUtils.copyProperties(message, messageFromDb, "id");     //копирует из message в messageFromDb все поля, кроме id
        Message updatedMessage = messageRepo.save(messageFromDb);

        wsSender.accept(EventType.UPDATE, updatedMessage);

        return updatedMessage;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }


}
