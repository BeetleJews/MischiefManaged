package com.example.MischiefManaged.repo;

import com.example.MischiefManaged.domain.Message;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {
    @EntityGraph(attributePaths = {"comments"})     //благодаря этой аннатации, при запросе этого метода мы будем получать в жадной манере все наши записи(все сообзения с комментами)
    List<Message> findAll();
}
