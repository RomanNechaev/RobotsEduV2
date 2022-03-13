package gui;

import java.io.Serializable;

public class Configuration implements Serializable {
    private int m_gameWindowLocationX;
    private int m_gameWindowLocationY;

    public Configuration(int gameWindowLocationX, int gameWindowLocationY) {
        m_gameWindowLocationX = gameWindowLocationX;
        m_gameWindowLocationY = gameWindowLocationY;
    }
    public int getGameWindowLocationX() {
        return m_gameWindowLocationX;
    }

    public void setGameWindowWidth(int gameWindowLocationX) {
        m_gameWindowLocationX = gameWindowLocationX;
    }

    public int getGameWindowLocationY() {
        return m_gameWindowLocationY;
    }

    public void setGameWindowHeight(int gameWindowLocationY) {
        m_gameWindowLocationY = gameWindowLocationY;
    }
}
