package com.example.MischiefManaged.repo;

import com.example.MischiefManaged.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {
}
