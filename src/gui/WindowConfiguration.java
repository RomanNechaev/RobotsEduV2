package gui;

import java.io.Serializable;

public class WindowConfiguration implements Serializable {
    private int m_gameWindowLocationX;
    private int m_gameWindowLocationY;
    private int m_gameWindowWidth;
    private int m_gameWindowHeight;
    private Boolean m_iconState;
    private String m_name;
    private int m_iconNumber;

    public WindowConfiguration(int gameWindowLocationX, int gameWindowLocationY, int gameWindowWidth, int gameWindowHeight, Boolean iconState, String name) {
        m_gameWindowLocationX = gameWindowLocationX;
        m_gameWindowLocationY = gameWindowLocationY;
        m_gameWindowWidth = gameWindowWidth;
        m_gameWindowHeight = gameWindowHeight;
        m_iconState = iconState;
        m_name = name;
    }

    public WindowConfiguration(int gameWindowLocationX, int gameWindowLocationY, int gameWindowWidth, int gameWindowHeight, Integer iconNumber, String name) {
        m_gameWindowLocationX = gameWindowLocationX;
        m_gameWindowLocationY = gameWindowLocationY;
        m_gameWindowWidth = gameWindowWidth;
        m_gameWindowHeight = gameWindowHeight;
        m_name = name;
        m_iconNumber = iconNumber;
    }


    public int getLocationX() {
        return m_gameWindowLocationX;
    }

    public int getLocationY() {
        return m_gameWindowLocationY;
    }

    public void setLocationX(int gameWindowLocationX) {
        m_gameWindowLocationX = gameWindowLocationX;
    }

    public void setLocationY(int gameWindowLocationY) {
        m_gameWindowLocationY = gameWindowLocationY;
    }

    public int getWindowWidth() {
        return m_gameWindowWidth;
    }

    public int getWindowHeight() {
        return m_gameWindowHeight;
    }

    public Boolean getIconState() {
        return m_iconState;
    }

    public Integer getIconNumber() {
        return m_iconNumber;
    }


    public String getName() {
        return m_name;
    }
}