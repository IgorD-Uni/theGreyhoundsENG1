package io.github.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class StudentSprite {
    private Texture texture;
    private float x, y;
    private float width = 32, height = 32;
    private boolean active = true;
    private float speed = 100f;

    public StudentSprite(String texturePath, float startX, float startY) {
        this(texturePath, startX, startY, 100f);
    }

    public StudentSprite(String texturePath, float startX, float startY, float speed) {
        this.texture = new Texture(texturePath);
        this.x = startX;
        this.y = startY;
        this.speed = speed;
    }

    public void update(float delta) {
        if (!active) return;

        // Move left across the screen
        x -= speed * delta;

        // Deactivate if off screen
        if (x + width < 0) {
            active = false;
        }
    }

    public void render(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void deactivate() { active = false; }

    public boolean isActive() { return active; }

    public void dispose() { texture.dispose(); }
}
