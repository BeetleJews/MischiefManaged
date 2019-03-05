package com.example.MischiefManaged.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@ToString(of = {"id", "text"})
@EqualsAndHashCode(of = {"id"})
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)           //очень крутая штука которая позваляет видеть определенные поля в зависимости от вызова(например помечая в конструкторе метод анатацией @JsonView(Views.IdName.class) нам вернется id и text, тк они помечены этими маркерами)
    private Long id;
    @JsonView(Views.IdName.class)
    private String text;

    @Column(updatable = false)      //необновляемая величина
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")       //Используем анатацию джексона чтобы задать формат даты и время
    @JsonView(Views.FullMessage.class)
    private LocalDateTime creationDate;

    @JsonView(Views.FullMessage.class)
    private String link;
    @JsonView(Views.FullMessage.class)
    private String linkTitle;
    @JsonView(Views.FullMessage.class)
    private String linkDescription;
    @JsonView(Views.FullMessage.class)
    private String linkCover;

}
