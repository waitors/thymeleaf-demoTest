package com.example.demothymeleaf.repository;

import com.example.demothymeleaf.model.Message;

public interface MessageRepository {
    Iterable<Message> findAll();

    Message save(Message message);

    Message findMessage(Long id);

    void deleteMessage(Long id);
}
