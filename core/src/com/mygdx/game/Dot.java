package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;

// can make an extension of dot 1 and dot2
public class Dot {
    Texture dotTexture;
    Rectangle boundingBox;
    public Dot (float xPosition, float yPosition, float width, float height, Texture dotTexture){
        this.dotTexture = dotTexture;
        this.boundingBox = new Rectangle(xPosition, yPosition,width, height );
    }

    //hitbox of the dot or the character, this is also applicable if hitting the other dot or character
    public boolean intersects(Rectangle otherRectangle){
        return boundingBox.overlaps(otherRectangle);
    }

    public void draw(Batch batch){
        batch.draw(dotTexture, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    //i dont think you'll need this translate
    public void translate(float xChange, float yChange){
        boundingBox.setPosition(boundingBox.x+xChange, boundingBox.y+yChange);
    }

}
