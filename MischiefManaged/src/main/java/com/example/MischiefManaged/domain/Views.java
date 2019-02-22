package com.example.MischiefManaged.domain;

public final class Views {
    public interface Id {}       //интерфейсы маркеры что бы помечать то что возвращать в JsonViews

    public interface IdName extends Id{}

    public interface FullMessage extends Id {}

}
