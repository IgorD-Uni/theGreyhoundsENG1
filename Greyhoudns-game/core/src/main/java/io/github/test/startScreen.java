package io.github.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class startScreen{
    Texture screen;
    Boolean displaying;

    SpriteBatch spriteBatch;




    public startScreen(String givenScreen){
        screen = new Texture(givenScreen);
        displaying = true;
        spriteBatch = new SpriteBatch();
    }

    public void displayScreen(){
        // Displays start screen until space is pressed
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            displaying = false;
        }

        spriteBatch.begin();
        spriteBatch.draw(screen, 0, 0, 1024, 1024);;
        spriteBatch.end();

    }


}
