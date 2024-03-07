package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Character {
    public Texture image;
    public Rectangle bounds;

    public Character(String imagePath, float x, float y, float width, float height) {
        image = new Texture(imagePath);
        bounds = new Rectangle(x, y, width, height);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(image, bounds.x, bounds.y);
    }

    // Method to allow this character to attack another character
    public void attack(Character target) {
        float attackRange = 50f; // Example range value
        if (Math.abs(this.bounds.x - target.bounds.x) < attackRange && Math.abs(this.bounds.y - target.bounds.y) < attackRange) {
            // Perform attack logic
            System.out.println("Attack hit!");
            // Example: reduce the health of the target or change its state
            // For demonstration, let's just move the target slightly to show an effect
            target.bounds.x += 10; // This is just a placeholder to show the attack effect
        }
    }

}
