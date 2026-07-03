package model;

import javax.swing.*;
import java.awt.*;

public class Egg {

    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private Image image;

    public Egg(int x,int y){

        this.x=x;
        this.y=y;
        width=18;
        height=24;
        speed=5;

        image=new ImageIcon("src/resources/images/egg.png").getImage();

    }

    public void draw(Graphics g){
        g.drawImage(image,x,y,width,height,null);
    }

    public void move(){
        y+=speed;
    }

    public boolean isOutOfScreen(){
        return y>600;
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
