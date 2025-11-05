package io.github.test;

import static java.lang.Math.min;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class button{
    public Texture buttonTexture;

    Boolean hover = false;
    float positionX;
    float positionY;
    float width;
    float height;
    public button(String givenTexture, float givenX, float givenY,
                  float givenWidth, float givenHeight){

        buttonTexture = new Texture(givenTexture);
        positionX = givenX;
        positionY = givenY;
        height = givenHeight;
        width = givenWidth;


    }

    public boolean checkCollision() {
        hover = false;

        // Mouse position is given relative to top-left corner, and not bottom left. We need to
        // adjust for this. Also adjust for screen size.

        float screenHeight = Gdx.graphics.getHeight();
        float screenWidth= Gdx.graphics.getWidth();



        // Adjust for screen size, finds middle point of map and compares it to the game screen.
        // Immense scale difference problems otherwise.



        float gameScreenSize = min(screenHeight,  screenWidth);
        float scale = gameScreenSize/1024;



        float relativeHitboxX = (positionX*scale + (screenWidth/2)-(gameScreenSize/2));
        float relativeHitboxY = (positionY*scale + (screenHeight/2)-(gameScreenSize/2));

        // Check if the mouse is withing the bounds of the button, (must flip the Y coordinate)
        if (Gdx.input.getX() >= relativeHitboxX
                && Gdx.input.getX() < relativeHitboxX+(width*scale)
                && screenHeight- Gdx.input.getY() >= relativeHitboxY
                && screenHeight- Gdx.input.getY() < relativeHitboxY+(height*scale)) {


            hover = true;
            return hover;
        }
        return hover;
    }


    //Increase size when hovering over the button

    public float getHeight() {
        if (hover){
            return height * 1.3f;
        }
        return height;
    }
    public float getWidth() {
        if (hover) {
            return width * 1.3f;
        }
        return width;
    }

}

