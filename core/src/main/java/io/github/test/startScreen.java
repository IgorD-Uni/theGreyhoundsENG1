package io.github.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.ScreenUtils;

public class startScreen{

    Texture screen;
    Texture buttonTexture;
    Boolean displaying;

    button startButton;
    SpriteBatch spriteBatch;




    public startScreen(String givenScreen){
        screen = new Texture(givenScreen);

        displaying = true;
        spriteBatch = new SpriteBatch();

        startButton = new button("startButton.png", 100, 100, 300,110);

    }

    public void displayScreen(){
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);


        // Displays start screen until the button is pressed
        if (startButton.checkCollision() && Gdx.input.isTouched()){
            displaying = false;
        }

        //Allows button to change size
        float buttonWidth = startButton.getWidth();
        float buttonHeight = startButton.getHeight();


        spriteBatch.begin();
        spriteBatch.draw(screen, 0, 0, 1024, 1024);

        //Draw button
        spriteBatch.draw(startButton.buttonTexture, startButton.positionX, startButton.positionY,
                buttonWidth, buttonHeight);

        spriteBatch.end();

    }


}
