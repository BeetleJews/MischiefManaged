package com.example.MischiefManaged.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@ToString(of = {"id", "text"})      //почему то не работает @Data, приходится юзать костыль
@EqualsAndHashCode(of = {"id"})
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
