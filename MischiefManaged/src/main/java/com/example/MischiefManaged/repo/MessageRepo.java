package com.example.MischiefManaged.repo;

import com.example.MischiefManaged.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long> {
}
