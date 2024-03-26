package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.Rectangle;
import java.util.LinkedList;

public class GamePlayScreen implements Screen {
    private Game game;
    Rectangle boundingBox; // this is some kind of hitbox
    private Texture mazeTexture;
    private SpriteBatch batch;
    private Texture dotTexture,coinTexture;
    private Coin coin1, coin2, coin3;
    private Animation<TextureRegion> coinAnimation;
    private LinkedList<Coin> coinLinkedList;
    private float dotX, dotY;
    private  int dotWidth, dotHeight; //for setting the hitbox width and height using the bounding area
    private int x,y;//coordinates
    private final float DOT_SIZE = 30.0f; // This should be the size of your dot
    private final float MOVE_AMOUNT = 2.0f;
    private boolean[][] collisionMap;
    private final float UNIT_PER_PIXEL = 1.0f;

    private final float START_X = 50f;
    private final float START_Y = 50f;
    private final float COLLISION_BUFFER_RATIO = 0.1f;

    Rectangle dot = new Rectangle(x,y, dotWidth, dotHeight);//for making the hitbox for the dot but havent made it so leave it as it be




    public GamePlayScreen(Game game) {
        this.game = game;

        //coin animation
        batch = new SpriteBatch();
        coinTexture = new Texture("Coins.png");

        int frameWidth = coinTexture.getWidth() / 6;
        int frameHeight = coinTexture.getHeight();

        float frameDuration = 0.1f;
        TextureRegion[][] frames = TextureRegion.split(coinTexture, frameWidth, frameHeight);
        TextureRegion[] animationFrames = frames[0];
        coinAnimation = new Animation<>(frameDuration, animationFrames);
        coinLinkedList = new LinkedList<>();
        batch = new SpriteBatch();

        //still very dirty assigning need to improve but it's working
        coin1 = new Coin(198, 304, 10, 10, frameDuration, null, animationFrames);
        coin2 = new Coin(97, 78, 10, 10, frameDuration, null, animationFrames);
        coin3 = new Coin(300, 128, 10, 10, frameDuration, null, animationFrames);

        coinLinkedList.add(coin1);
        coinLinkedList.add(coin2);
        coinLinkedList.add(coin3);
    }

    @Override
    public void show() {
        mazeTexture = new Texture("Game Map.jpg");
        dotTexture = new Texture("dot.png"); // Make sure you have a dot.png in your assets
        batch = new SpriteBatch();

        // Initialize the dot's starting position
        dotX = START_X; // Adjust this to be within the bounds of your maze
        dotY = START_Y; // Adjust this as well

        createCollisionMap();

    }

    private void createCollisionMap() {
        if (!mazeTexture.getTextureData().isPrepared()) {
            mazeTexture.getTextureData().prepare();
        }
        Pixmap pixmap = mazeTexture.getTextureData().consumePixmap();

        collisionMap = new boolean[pixmap.getWidth()][pixmap.getHeight()];
        // We will invert the y-axis here
        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                // LibGDX pixmap coordinates start at the top left, so invert the y-axis
                int pixel = pixmap.getPixel(x, pixmap.getHeight() - 1 - y);
                // Check for non-white pixels, which we'll assume to be walls
                collisionMap[x][y] = (pixel != 0xFFFFFFFF);
            }
        }

        // For debugging: Print out the collision map to the console
        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                System.out.print(collisionMap[x][y] ? "1" : "0");
            }
            System.out.println();
        }

        // Don't forget to dispose of the pixmap once you're done with it
        pixmap.dispose();
    }


    private void handleInput() {
        float potentialX = dotX;
        float potentialY = dotY;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            potentialX -= MOVE_AMOUNT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            potentialX += MOVE_AMOUNT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            potentialY += MOVE_AMOUNT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            potentialY -= MOVE_AMOUNT;
        }

        if (!collidesWithWall(potentialX, potentialY)) {
            dotX = potentialX;
            dotY = potentialY;
        }
    }

    private boolean collidesWithWall(float x, float y) {
        // Reduce the collision check to the center of the dot to avoid edge issues
        int centerX = (int) ((x + DOT_SIZE / 2) / UNIT_PER_PIXEL);
        int centerY = (int) ((y + DOT_SIZE / 2) / UNIT_PER_PIXEL);

        // Apply a small buffer around the dot's center for collision detection
        int bufferX = (int) (DOT_SIZE * COLLISION_BUFFER_RATIO / UNIT_PER_PIXEL);
        int bufferY = (int) (DOT_SIZE * COLLISION_BUFFER_RATIO / UNIT_PER_PIXEL);

        // Check the area around the dot's center for collisions, using the buffer
        for (int checkX = centerX - bufferX; checkX <= centerX + bufferX; checkX++) {
            for (int checkY = centerY - bufferY; checkY <= centerY + bufferY; checkY++) {
                // Check if this point is a wall
                if (isWall(checkX, checkY)) {
                    return true; // Collision found at this point
                }
            }
        }

        return false; // No collision found around the dot's center
    }

    private boolean isWall(int x, int y) {
        // Check if the coordinates are out of bounds
        if (x < 0 || x >= collisionMap.length || y < 0 || y >= collisionMap[0].length) {
            return true; // Treat out-of-bounds as a wall
        }
        return collisionMap[x][y];
    }

    private void renderCoin(float delta){ }







    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput(); // Handle the input before drawing

        batch.begin();
        batch.draw(mazeTexture, 0, 0);
        batch.draw(dotTexture, dotX, dotY, DOT_SIZE, DOT_SIZE); // Draw the dot
        coin1.render(batch);
        coin2.render(batch);
        coin3.render(batch);
        coin1.update(delta);
        coin2.update(delta);
        coin3.update(delta);
        batch.end();
    }


    @Override
    public void resize(int width, int height) {
        // Handle screen resizing
    }

    @Override
    public void pause() {
        // Handle game pausing
    }

    @Override
    public void resume() {
        // Handle game resuming
    }

    @Override
    public void hide() {
        // Handle hiding the screen
    }

    @Override
    public void dispose() {
        batch.dispose();
        mazeTexture.dispose();
        dotTexture.dispose();
    }

}