package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Character {
    public Texture image;
    public Rectangle bounds;
    public int health;

    private Animation<TextureRegion> attackAnimation;
    private float stateTime;

    private int frameWidth; // Width of each frame in the sprite sheet
    private int frameHeight; // Height of each frame in the sprite sheet

    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> punchingAnimation;
    private TextureRegion idleFrame;

    public enum Action {
        IDLE,
        RUNNING,
        PUNCHING,
        JUMPING
    }
    private Action currentAction = Action.IDLE; // Start with the IDLE action

    public void setAction(Action newAction) {
        // Only reset state time if the action changes
        if (currentAction != newAction) {
            stateTime = 0f;
            currentAction = newAction;
        }
    }

    public Character(String imagePath, float x, float y, float width, float height) {
        image = new Texture(imagePath);
        bounds = new Rectangle(x, y, width, height);
        health = 100; // Starting health

        frameWidth = 256; // The width of a single frame
        frameHeight = 341; // The height of a single frame (round number expected, check for padding)


        Texture attackSheet = new Texture("newspirte.png"); // This needs to be a sprite sheet
        // Split the sprite sheet into frames
        TextureRegion[][] temp = TextureRegion.split(attackSheet, frameWidth, frameHeight); // frameWidth and frameHeight are the dimensions of a single frame
        Array<TextureRegion> attackFrames = new Array<>();
        for (TextureRegion[] regionArray : temp) {
            for (TextureRegion region : regionArray) {
                attackFrames.add(region);
            }
        }
        attackAnimation = new Animation<>(0.1f, attackFrames);
        stateTime = 0f;
    }

    public void draw(SpriteBatch batch, float delta) {
        // Only animate if the character is not idle
        if (currentAction != Action.IDLE) {
            stateTime += delta; // Accumulate elapsed animation time
        }

        TextureRegion currentFrame;
        switch (currentAction) {
            case RUNNING:
                // Get the running frame
                currentFrame = runningAnimation.getKeyFrame(stateTime, true);
                break;
            case PUNCHING:
                // Get the punching frame
                currentFrame = punchingAnimation.getKeyFrame(stateTime, true);
                break;
            // Add cases for other actions...
            default:
                // Default to idle frame
                currentFrame = idleFrame;
                break;
        }

        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
    }



    // Method to allow this character to attack another character
    public void attack(Character target) {
        float attackRange = 50f; // Example range value
        if (Math.abs(this.bounds.x - target.bounds.x) < attackRange && Math.abs(this.bounds.y - target.bounds.y) < attackRange) {
            System.out.println("Attack hit!");
            target.health -= 10; // Decrease health by 10 on a hit
            target.bounds.x += 10; // This is just a visual cue for the attack hit

            if (target.health <= 0) {
                System.out.println("Target defeated!");
                // Handle the defeat of the target (e.g., remove from screen, end game)
            }
        }
    }

    public void dispose() {
        image.dispose();
        // Dispose other textures if you have any
    }


}
