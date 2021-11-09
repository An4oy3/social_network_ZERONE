package ru.skillbox.socialnetwork.data.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Person author;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private Person recipient;


    @Column(name = "message_text")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_status", length = 16)
    private ReadStatus readStatus;

}
