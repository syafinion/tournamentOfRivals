package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Character;

public class Bullet {
    // physical characteristic
    float movementSpeed;

    // position and dimension
    float xPosition, yPosition;
    float bulletWidth, bulletHeight;

    // graph

    TextureRegion bulletTextureRegion;

    public Bullet(float movementSpeed, float xPosition, float yPosition, float bulletWidth, float bulletHeight, TextureRegion bulletTextureRegion) {
        this.movementSpeed = movementSpeed;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.bulletWidth = bulletWidth;
        this.bulletHeight = bulletHeight;
        this.bulletTextureRegion = bulletTextureRegion;
    }

    void draw(Batch batch) {
        batch.draw(bulletTextureRegion, xPosition, yPosition, bulletWidth, bulletHeight);
    }
    public Rectangle getBoundingBox(){
        return new Rectangle(xPosition, yPosition, bulletWidth,bulletHeight);
    }
}

