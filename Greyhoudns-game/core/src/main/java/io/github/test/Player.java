package io.github.test;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

public class Player{
    Texture playerTexture;
    public float speed;
    public float playerX;
    public float playerY;


    public int[] playerVector = {0,0}; // Shows the direction of motion when moving the player.

    public Player(String givenTexture, float x, float y){

        playerTexture = new Texture(givenTexture);
        //position of player, (starting position in this case)
        playerX = x;
        playerY = y;

        //player speed
        speed = 200f;

    }

    //Used this approach as being limited to move only in 4 directions is not smooth or fun to
    //play with. Can move diagonally without increase in speed due to the speed limiter variable.
    //ie, move in unit vector in the 8 possible directions.
    public void move(float delta, GameMap givenMap){
        float oldX = playerX;
        float oldY = playerY;

        float speedLimiter = 1;
        if (playerVector[0]!=0 && playerVector[1]!=0){
            speedLimiter = (float) Math.sqrt(2);

        }
        playerX = playerX + (speed/speedLimiter)*playerVector[0]*delta;
        playerY = playerY + (speed/speedLimiter)*playerVector[1]*delta;



        if (givenMap.checkCollision(this, delta)){
            System.out.println("collision");
            playerX = oldX;
            playerY = oldY;
        }


        //set player vector back to [0,0] so that you do not continue moving after letting go.
        playerVector = new int[]{0,0};
    }
}
