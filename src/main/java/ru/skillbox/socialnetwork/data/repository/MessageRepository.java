package ru.skillbox.socialnetwork.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.socialnetwork.data.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
