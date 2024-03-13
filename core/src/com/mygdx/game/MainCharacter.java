package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

class MainCharacter extends Character{
    public MainCharacter(float movementSpeed, int health, float xPosition, float yPosition, float width, float height, float bulletWidth, float bulletHeight, float bulletMovementSpeed, float timeBetweenShots, TextureRegion bulletTextureRegion, TextureRegion characterTextureRegion) {
        super(movementSpeed, health, xPosition, yPosition, width, height, bulletWidth, bulletHeight, bulletMovementSpeed, timeBetweenShots, bulletTextureRegion, characterTextureRegion);
    }

    public Bullet fireBullet(){

        Bullet bullet;
        bullet = new Bullet(bulletMovementSpeed, boundingBox.x + boundingBox.width,boundingBox.y + boundingBox.height/2, bulletWidth, bulletHeight, bulletTextureRegion);

        timeSinceLastShot = 0;

        return bullet;
    }
}
