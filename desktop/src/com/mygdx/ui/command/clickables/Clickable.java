package com.mygdx.ui.command.clickables;

public interface Clickable {
    void redirect();
    void saveObject() throws Exception;
    void logUser();
    void exit();
}
