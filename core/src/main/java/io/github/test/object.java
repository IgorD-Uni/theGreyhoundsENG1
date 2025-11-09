package io.github.test;

import com.badlogic.gdx.graphics.Texture;

import org.w3c.dom.Text;

public class object{

    Texture texture;
    float positionX;
    float positionY;
    float width;
    float height;

    public object(String givenTexture,float givenX, float givenY, float givenWidth, float givenHeight){

        texture = new Texture(givenTexture);
        positionX = givenX;
        positionY = givenY;
        width = givenWidth;
        height = givenHeight;

    }


    public boolean checkCollision(Player givenPlayer){
        if (givenPlayer.playerX-8 > positionX && givenPlayer.playerX <= positionX-16+width){
            if (givenPlayer.playerY-9 > positionY && givenPlayer.playerY <= positionY+height){
                return true;
            }
        }
        return false;
    }
}

