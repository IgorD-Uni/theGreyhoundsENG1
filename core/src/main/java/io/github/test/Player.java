package io.github.test;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

public class Player{
    Texture playerTexture;
    public float speed;
    public float playerX;
    public float playerY;
    public float health;
    public float maxHealth = 100f;
    public float slowTimer = 0f;
    public float normalSpeed = 200f;


    public int[] playerVector = {0,0}; // Shows the direction of motion when moving the player.

    public Player(String givenTexture, float x, float y){

        playerTexture = new Texture(givenTexture);
        //position of player, (starting position in this case)
        playerX = x;
        playerY = y;

        //player speed
        speed = 200f;

        //player health
        health = maxHealth;

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
            playerX = oldX;
            playerY = oldY;
        }


        //set player vector back to [0,0] so that you do not continue moving after letting go.
        playerVector = new int[]{0,0};
    }
    public Rectangle getBounds() {
        return new Rectangle(playerX - 16, playerY - 16, 32, 32);
    }

    public float getSpeed(){
        return speed;
    }

    public void setSpeed(float newSpeed){
        this.speed = newSpeed;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void takeDamage(float amount) {
        health -= amount;
        if (health < 0) health = 0;
    }

    public void heal(float amount) {
        health += amount;
        if (health > maxHealth) health = maxHealth;
    }

    public void updateSlowTimer(float delta) {
        if (slowTimer > 0) {
            slowTimer -= delta;
            if (slowTimer <= 0) {
                speed = normalSpeed; // reset
            }
        }
    }

    public void applySlowEffect(float duration) {
        this.speed = 100f; // half speed
        this.slowTimer = duration;
    }

}