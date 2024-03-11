package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

//this is for the general character, can be used by the main character and the enemy
public abstract class Character {
    // characteristic / hitbox of the character
    float movementSpeed;
    int health;
    float xPosition, yPosition;
    float width, height;

    // bullet information
    float bulletWidth, bulletHeight;
    float bulletMovementSpeed;
    float timeBetweenShots;
    float timeSinceLastShot = 0;

    //graph, need to create the Texture Region class in the main screen
    TextureRegion bulletTextureRegion, characterTextureRegion;

    public Character(float movementSpeed, int health, float xPosition, float yPosition, float width, float height, float bulletWidth, float bulletHeight, float bulletMovementSpeed, float timeBetweenShots, TextureRegion bulletTextureRegion, TextureRegion characterTextureRegion) {
        this.movementSpeed = movementSpeed;
        this.health = health;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.bulletWidth = bulletWidth;
        this.bulletHeight = bulletHeight;
        this.bulletMovementSpeed = bulletMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
        this.bulletTextureRegion = bulletTextureRegion;
        this.characterTextureRegion = characterTextureRegion;
    }
    public void update(float delta){
        timeSinceLastShot += delta;
    }
    public boolean canFire(){
        return (timeSinceLastShot - timeBetweenShots > 0);
    }

    public boolean intersects(Rectangle otherRectangle){
        Rectangle thisRectangle = new Rectangle(xPosition,yPosition,width,height);
        return thisRectangle.overlaps(otherRectangle);
    }

    public void draw(Batch batch){
        batch.draw(characterTextureRegion, xPosition, yPosition, width, height);
    }

    public abstract Bullet fireBullet();
}
