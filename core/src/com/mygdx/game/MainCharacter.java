package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

class MainCharacter extends Character {

    public MainCharacter(float movementSpeed, int health, float xPosition, float yPosition, float width, float height, float bulletWidth, float bulletHeight, float bulletMovementSpeed, float timeBetweenShots, int coinCounter, TextureRegion bulletTextureRegion, TextureRegion characterTextureRegion) {
        super(movementSpeed, health, xPosition, yPosition, width, height, bulletWidth, bulletHeight, bulletMovementSpeed, timeBetweenShots, coinCounter, bulletTextureRegion, characterTextureRegion);
    }

    public Bullet fireBullet() {

        Bullet bullet;
        bullet = new Bullet(bulletMovementSpeed, boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height / 2, bulletWidth, bulletHeight, bulletTextureRegion);

        timeSinceLastShot = 0;

        return bullet;
    }

/*
    public void update(float delta) {
        super.update(delta);
        //this.jump(delta); // Handle input specifically for the main character
    }

 */

}
