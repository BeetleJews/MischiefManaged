package com.example.MischiefManaged.service;


import com.example.MischiefManaged.domain.Comment;
import com.example.MischiefManaged.domain.User;
import com.example.MischiefManaged.domain.Views;
import com.example.MischiefManaged.dto.EventType;
import com.example.MischiefManaged.dto.ObjectType;
import com.example.MischiefManaged.repo.CommentRepo;
import com.example.MischiefManaged.util.WsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
public class CommentService {
    private final CommentRepo commentRepo;
    private final BiConsumer<EventType, Comment> wsSender;

    @Autowired      //необязательная
    public CommentService(CommentRepo commentRepo, WsSender wsSender) {
        this.commentRepo = commentRepo;
        this.wsSender = wsSender.getSender(ObjectType.COMMENT, Views.FullComment.class);
    }

    public Comment create(Comment comment, User user) {
        comment.setAuthor(user);
        Comment commentFromDB = commentRepo.save(comment);

        wsSender.accept(EventType.CREATE, commentFromDB);     //применяем wsSender с типом create к нашему комменту

        return commentFromDB;
    }
}
