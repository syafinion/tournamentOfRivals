package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Character {
    public Rectangle bounds;
    public int health;

    private Texture spriteSheet;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> punchingAnimation;
    private TextureRegion idleFrame;
    private float stateTime;

    // These should match the actual size of the frames in your sprite sheet
    private static final int FRAME_COLS = 7; // Number of columns in the sprite sheet
    private static final int FRAME_ROWS = 6; // Number of rows in the sprite sheet

    public enum Action {
        IDLE,
        RUNNING,
        PUNCHING,
        JUMPING
    }

    private Action currentAction = Action.IDLE;
    private boolean isBot;
    private float attackCooldown;
    private float attackTimer;
    public Character(String imagePath, float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
        health = 100;

        // Assuming each frame is square and the sprite sheet is evenly laid out
        spriteSheet = new Texture(imagePath);
        int frameWidth = spriteSheet.getWidth() / FRAME_COLS;
        int frameHeight = spriteSheet.getHeight() / FRAME_ROWS;

        TextureRegion[][] temp = TextureRegion.split(spriteSheet, frameWidth, frameHeight);

        // The idle animation uses the first frame of the first row
        idleFrame = temp[0][0];


        // Extract the second row for the running animation
        TextureRegion[] runningFrames = new TextureRegion[FRAME_COLS];
        System.arraycopy(temp[1], 0, runningFrames, 0, FRAME_COLS);
        runningAnimation = new Animation<>(0.1f, runningFrames);

        // Extract the third row for the punching animation
        TextureRegion[] punchingFrames = new TextureRegion[FRAME_COLS];
        System.arraycopy(temp[2], 0, punchingFrames, 0, FRAME_COLS);
        punchingAnimation = new Animation<>(0.1f, punchingFrames);

        // If you have a jumping animation, extract it here, similarly

        stateTime = 0f;
        this.isBot = isBot;
        attackCooldown = 1.0f; // Cooldown in seconds between attacks
        attackTimer = 0f;
    }

    public void setAction(Action newAction) {
        if (currentAction != newAction) {
            stateTime = 0f; // Reset the state time on action change
            currentAction = newAction;
        }
    }

    public void attack(Character target) {
        float attackRange = 50f; // Example range value
        if (Math.abs(this.bounds.x - target.bounds.x) <= attackRange && Math.abs(this.bounds.y - target.bounds.y) <= attackRange) {
            System.out.println("Attack hit!");
            target.health -= 10; // Decrease health by 10 on a hit

            if (target.health <= 0) {
                System.out.println("Target defeated!");
                // Handle the defeat of the target (e.g., remove from screen, end game)
            }
        }
    }

    public void draw(SpriteBatch batch, float delta) {
        // Always update state time, animations need it to determine which frame to display
        stateTime += delta;

        TextureRegion currentFrame = null;
        switch (currentAction) {
            case RUNNING:
                currentFrame = runningAnimation.getKeyFrame(stateTime, true);
                break;
            case PUNCHING:
                // This line needs to use the punchingAnimation
                currentFrame = punchingAnimation.getKeyFrame(stateTime, false); // Set false if you don't want to loop
                break;
            case IDLE:
            default:
                currentFrame = idleFrame;
                break;
        }

        // Draw the current frame
        if (currentFrame != null) {
            batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }


    // Implement the attack method here as needed

    public void dispose() {
        if (spriteSheet != null) {
            spriteSheet.dispose(); // Dispose the sprite sheet
        }
    }
}
