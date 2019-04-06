package com.example.MischiefManaged.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@EqualsAndHashCode(of = {"id"})
public class Comment {
    @Id
    @GeneratedValue
    @JsonView(Views.IdName.class)
    private Long id;

    @Column(updatable = false)      //необновляемая величина
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")       //Используем анатацию джексона чтобы задать формат даты и время
    @JsonView(Views.IdName.class)
    private LocalDateTime creationDate;

    @JsonView(Views.IdName.class)
    private String text;        //сам коммент

    @ManyToOne
    @JoinColumn(name = "message_id")
    @JsonView(Views.FullComment.class)
    private  Message message;       //  сообщение к которому крепим коммент

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)      //необновляемое и не пустое
    @JsonView(Views.IdName.class)      //что бы автор отображался в тот момент когда запрашиваем комментрарии к сообщению
    private User author;
}
