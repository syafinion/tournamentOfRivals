package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Coin {
    TextureRegion SpriteSheet;
    Animation<TextureRegion> animation;
    Rectangle boundingBox;
    float stateTime;

    public Coin(float xPosition, float yPosition, float coinWidth, float coinHeight,float frameDuration,TextureRegion spriteSheet, TextureRegion... keyFrames ) {
        SpriteSheet = spriteSheet;
        animation = new Animation<>(frameDuration, keyFrames);
        this.boundingBox = new Rectangle(xPosition, yPosition, coinWidth,coinHeight);
        stateTime = 0;
    }

    public void update(float delta) {

        stateTime += delta;
    }

    public boolean intersects(Rectangle otherRectangle){
        return boundingBox.overlaps(otherRectangle);
    }

    public void render(Batch batch){
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

}
