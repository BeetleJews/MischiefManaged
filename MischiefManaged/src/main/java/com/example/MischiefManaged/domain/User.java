package com.example.MischiefManaged.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "usr")
@Data       //при помощи этой lombok анатации, можно не генерить геттеры и сетеры
public class User implements Serializable {
    @Id
    @JsonView(Views.IdName.class)
    private String id;      //id будут приходить от гугла в форсате String, поэтому не Long
    @JsonView(Views.IdName.class)
    private String name;
    @JsonView(Views.IdName.class)
    private String userpic;
    private String email;
    private String gender;
    private String locale;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastVisit;


}
