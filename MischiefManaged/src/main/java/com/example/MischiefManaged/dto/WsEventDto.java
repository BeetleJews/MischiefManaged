package com.example.MischiefManaged.dto;

import com.example.MischiefManaged.domain.Views;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor     /*генерирует конструктор с одним параметром для каждого поля в классе. Поля, помеченные @NonNull, имеют проверку на null для этих параметров.*/
@JsonView(Views.Id.class)
public class WsEventDto {
    private ObjectType objectType;      /*в дальнейшим понадобиться работать через вебсокеты не только сообщениями, поэтому задодим енам объекта*/
    private EventType eventType;        /*сейчас тип может быть CREATE, UPDATE, REMOVE, так что сново енам*/

    @JsonRawValue       /*данные будут встроены из json без ковычек и прочего*/
    private String body;
}
