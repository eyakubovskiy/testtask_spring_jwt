package ru.eyakubovskiy.testtask_spring_oauth2.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="messages")
public class Message {
    @Id
    @Column(name = "id")
    private Long id;

    //@Column(name = "name")
    @ManyToOne
    @JoinTable(name = "user_messages",
            joinColumns = {@JoinColumn(name = "message_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private User user;

    @Column(name = "message")
    private String message;
}
