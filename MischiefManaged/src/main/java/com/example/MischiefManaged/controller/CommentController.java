package com.example.MischiefManaged.controller;


import com.example.MischiefManaged.domain.Comment;
import com.example.MischiefManaged.domain.User;
import com.example.MischiefManaged.domain.Views;
import com.example.MischiefManaged.service.CommentService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @JsonView(Views.FullMessage.class)
    public Comment create(
            @RequestBody Comment comment,
            @AuthenticationPrincipal User user
    ){
        return commentService.create(comment, user);
    }

}
