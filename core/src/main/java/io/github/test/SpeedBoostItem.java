package io.github.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class SpeedBoostItem {
    private float x, y;
    private float width = 32, height = 32;
    private boolean active = true;
    private float scale = 1.5f;

    private Texture texture;

    public SpeedBoostItem(String texturePath, float x, float y) {
        this.texture = new Texture(texturePath);
        this.x = x;
        this.y = y;
    }

    public void render(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, x, y, width*scale, height*scale);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width*scale, height*scale);
    }

    public boolean isActive() {
        return active;
    }

    public void collect() {
        active = false;
    }

    public void respawn() {
        this.active = true;
    }


    public void dispose() {
        texture.dispose();
    }
}