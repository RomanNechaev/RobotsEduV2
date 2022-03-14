package gui;

import java.io.Serializable;

public class GameWindowConfiguration implements Serializable, Iconfiguration {
    private int m_gameWindowLocationX;
    private int m_gameWindowLocationY;
    private int m_gameWindowWidth;
    private int m_gameWindowHeight;
    private boolean m_iconState;
    private String m_name;

    public GameWindowConfiguration(int gameWindowLocationX, int gameWindowLocationY, int gameWindowWidth, int gameWindowHeight, Boolean iconState,String name) {
        m_gameWindowLocationX = gameWindowLocationX;
        m_gameWindowLocationY = gameWindowLocationY;
        m_gameWindowWidth = gameWindowWidth;
        m_gameWindowHeight = gameWindowHeight;
        m_iconState = iconState;
        m_name = name;
    }

    @Override
    public int getLocationX() {
        return m_gameWindowLocationX;
    }

    @Override
    public int getLocationY() {
        return m_gameWindowLocationY;
    }

    @Override
    public void setLocationX(int gameWindowLocationX) {
        m_gameWindowLocationX = gameWindowLocationX;
    }

    @Override
    public void setLocationY(int gameWindowLocationY) {
        m_gameWindowLocationY = gameWindowLocationY;
    }

    @Override
    public int getWindowWidth() {
        return m_gameWindowWidth;
    }

    @Override
    public int getWindowHeight() {
        return m_gameWindowHeight;
    }

    @Override
    public Boolean getIconState() {
        return m_iconState;
    }

    public String getName()
    {
        return m_name;
    }
}
