package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

//this is for the general character, can be used by the main character and the enemy
public abstract class Character {
    // characteristic / hitbox of the character
    float movementSpeed;
    int health;

    /*
    //jumping variables
    float gravity = -9.8f;
    private float verticalVelocity = 0;
    float jumpSpeed = 20;

     */

    Rectangle boundingBox;

    // bullet information
    float bulletWidth, bulletHeight;
    float bulletMovementSpeed;
    float timeBetweenShots;
    float timeSinceLastShot = 0;
    int coinCounter = 0;

    //coin
    float coinWidth, coinHeight;
    TextureRegion spritesheet;

    //graph, need to create the Texture Region class in the main screen
    TextureRegion bulletTextureRegion, characterTextureRegion;

    public Character(float movementSpeed, int health, float xPosition, float yPosition, float width, float height, float bulletWidth, float bulletHeight, float bulletMovementSpeed, float timeBetweenShots, int coinCounter, TextureRegion bulletTextureRegion, TextureRegion characterTextureRegion) {
        this.movementSpeed = movementSpeed;
        this.health = health;
        this.bulletWidth = bulletWidth;
        this.bulletHeight = bulletHeight;
        this.bulletMovementSpeed = bulletMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
        this.bulletTextureRegion = bulletTextureRegion;
        this.characterTextureRegion = characterTextureRegion;
        this.coinCounter = coinCounter;

        //making the hitbox height and weight in 1 boundingBox
        this.boundingBox = new Rectangle(xPosition - width/2, yPosition - height/2, width, height);
    }

    public void hit(Bullet bullet){
        if(health > 0){
            health--;
        }
    }
    public void update(float delta){
        timeSinceLastShot += delta;
    }
    public boolean canFire(){
        return (timeSinceLastShot - timeBetweenShots > 0);
    }

    public boolean intersects(Rectangle otherRectangle){
        return boundingBox.overlaps(otherRectangle);
    }

    public void draw(Batch batch){
        batch.draw(characterTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public void translate(float xChange, float yChange){
        float number;
        boundingBox.setPosition(boundingBox.x+xChange, boundingBox.y+yChange);
    }

    public abstract Bullet fireBullet();

    /*
    public void jump(float delta) {
        //need more update depending on the platform
        float groundLevel = 0; // Assuming ground level is at y = 0

        // Check for jump initiation
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && boundingBox.y == groundLevel) {
            verticalVelocity = jumpSpeed; // Apply initial jump velocity
        }

        // Apply gravity
        verticalVelocity += gravity * delta; // Gravity reduces the vertical velocity over time

        // Update the character's vertical position
        boundingBox.y += verticalVelocity * delta;

        // Check for landing
        if (boundingBox.y <= groundLevel) {
            boundingBox.y = groundLevel; // Correct position to ground level
            verticalVelocity = 0; // Reset vertical velocity
        }
    }

     */


}


