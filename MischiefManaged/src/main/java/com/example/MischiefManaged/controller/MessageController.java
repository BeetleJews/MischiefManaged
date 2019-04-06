package com.example.MischiefManaged.controller;


import com.example.MischiefManaged.domain.Message;
import com.example.MischiefManaged.domain.User;
import com.example.MischiefManaged.domain.Views;
import com.example.MischiefManaged.dto.EventType;
import com.example.MischiefManaged.dto.MetaDto;
import com.example.MischiefManaged.dto.ObjectType;
import com.example.MischiefManaged.repo.MessageRepo;
import com.example.MischiefManaged.util.WsSender;
import com.fasterxml.jackson.annotation.JsonView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("message")
public class MessageController {
    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";  //патерн для ссылки
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";      //патерн для картинки

    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);      //Pattern.CASE_INSENSITIVE нужен что бы не проверялся регистр
    private static Pattern IMAGE_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final MessageRepo messageRepo;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageController(MessageRepo messageRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.FullMessage.class);        /*При помощи JsonView ограничиваем поля, которые будем отправлять клиенту 2х (до id и текста)*/
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> list() {
        return messageRepo.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message create(
            @RequestBody Message message,
            @AuthenticationPrincipal User user
            ) throws IOException {
        message.setCreationDate(LocalDateTime.now());
        fillMeta(message);
        message.setAuthor(user);
        Message updatedMessage = messageRepo.save(message);

        wsSender.accept(EventType.CREATE, updatedMessage);

        return updatedMessage;
    }

    @PutMapping("{id}")
    public Message update(
            @PathVariable("id") Message messageFromDb,      //сообщение которое бунем менять
            @RequestBody Message message) throws IOException       //сообщение на которое нужно изменить
    {
        //message.setCreationDate(LocalDateTime.now());
        BeanUtils.copyProperties(message, messageFromDb, "id");     //копирует из message в messageFromDb все поля, кроме id
        fillMeta(messageFromDb);

        Message updatedMessage = messageRepo.save(messageFromDb);

        wsSender.accept(EventType.UPDATE, updatedMessage);

        return updatedMessage;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }

    private void fillMeta(Message message) throws IOException {        //заполнение метаданных
        String text = message.getText();
        Matcher matcher = URL_REGEX.matcher(text);

        if (matcher.find()) {
            String url = text.substring(matcher.start(), matcher.end());        //достаем из текста url

            matcher = IMAGE_REGEX.matcher(url);     //матчим url

            message.setLink(url);

            if (matcher.find()) {
                  message.setLinkCover(url);     //и если это картинка то в linkCover помещаем url
            } else if (!url.contains("youtu")) {     //если это не ссылка с ютуба, то заполняем остальные поля для микроразметки

                MetaDto meta = getMeta(url);        //получаем мктаданные

                message.setLinkCover(meta.getCover());
                message.setLinkTitle(meta.getTitle());
                message.setLinkDescription(meta.getDescription());

            }
        }

    }

    private MetaDto getMeta(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();            //передаем jsoup наш url и он отдает HTML страницу, которая разбирается на doc(смотреть оыщгз документацию)

        Elements title = doc.select("meta[name$=title],meta[property$=title]");     //с помощю CSS селекторов мы делаем запросы и вытаскивать нужную нам информацию($=title означает что мы хотим что бы атрибут заканчивался на указанный паттерн)
        Elements description = doc.select("meta[name$=description],meta[property$=description]");
        Elements cover = doc.select("meta[name$=image],meta[property$=image]");

        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())

        );

    }

    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");      //нужно учесть что мы можем получить пустую строку
    }
}
