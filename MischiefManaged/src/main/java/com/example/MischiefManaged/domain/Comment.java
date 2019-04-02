package com.example.MischiefManaged.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table
@Data
@EqualsAndHashCode(of = {"id"})
public class Comment {
    @Id
    @GeneratedValue
    @JsonView(Views.IdName.class)
    private Long id;

    @JsonView(Views.IdName.class)
    private String text;        //сам коммент

    @ManyToOne
    @JoinColumn(name = "message_id")
    private  Message message;       //  сообщение к которому крепим коммент

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)      //необновляемое и не пустое
    @JsonView(Views.FullMessage.class)      //что бы автор отображался в тот момент когда запрашиваем комментрарии к сообщению
    private User author;
}
