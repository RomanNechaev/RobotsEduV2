package gui.state;


import java.io.Serializable;

public class WindowConfiguration implements Serializable {
    private int windowLocationX;
    private int windowLocationY;
    private int windowWidth;
    private int windowHeight;
    private Boolean iconState;
    private String name;
    private int iconNumber;

    public WindowConfiguration(int windowLocationX, int windowLocationY, int windowWidth, int windowHeight, Boolean iconState, String name) {
        this.windowLocationX = windowLocationX;
        this.windowLocationY = windowLocationY;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.iconState = iconState;
        this.name = name;
    }

    public WindowConfiguration() {

    }

    public WindowConfiguration(int windowLocationX, int windowLocationY, int windowWidth, int windowHeight, Integer iconNumber, String name) {
        this.windowLocationX = windowLocationX;
        this.windowLocationY = windowLocationY;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.name = name;
        this.iconNumber = iconNumber;
    }

    @Override
    public String toString() {
        return "windowLocationX:" + windowLocationX + "\s"
                + "windowLocationY:" + windowLocationY + "\s"
                + "windowWidth:" + windowWidth + "\s"
                + "windowHeight:" + windowHeight + "\s"
                + "iconState:" + iconState + "\s"
                + "name:" + name + "\s"
                + "iconNumber:" + iconNumber;
    }


    public int getWindowLocationX() {
        return windowLocationX;
    }

    public int getWindowLocationY() {
        return windowLocationY;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public Boolean getIconState() {
        return iconState;
    }

    public Integer getIconNumber() {
        return iconNumber;
    }

    public String getName() {
        return name;
    }
}