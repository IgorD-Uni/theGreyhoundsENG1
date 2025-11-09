package io.github.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class NegativeEventItem {
    private Texture texture;
    private float x, y;
    private float width = 32, height = 32;
    private boolean active = true;

    public NegativeEventItem(String texturePath, float x, float y) {
        this.texture = new Texture(texturePath);
        this.x = x;
        this.y = y;
    }

    public void render(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
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

