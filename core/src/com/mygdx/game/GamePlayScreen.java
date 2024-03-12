package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GamePlayScreen extends ScreenAdapter {
    final Game game;
    OrthographicCamera camera;
    SpriteBatch batch;
    Character playerCharacter;
    Character npcCharacter; // Add this if you want an NPC to interact with
    ShapeRenderer shapeRenderer;
    public GamePlayScreen(final Game game) {
        this.game = game;
        camera = new OrthographicCamera();
        shapeRenderer = new ShapeRenderer();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        // Initialize your character and an NPC here, adjust the positions as needed
        playerCharacter = new Character("firstguy-removebg.png", 100, 100, 64, 64);
        npcCharacter = new Character("firstguy-removebg.png", 300, 100, 64, 64); // Make sure you have npc.png

    }

    private void update(float delta) {
        // Movement speed can be adjusted as needed
        float moveSpeed = 200 * delta;

        // Movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerCharacter.bounds.x -= moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerCharacter.bounds.x += moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerCharacter.bounds.y += moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerCharacter.bounds.y -= moveSpeed;
        }

        // Attack
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            playerCharacter.setAction(Character.Action.PUNCHING); // Start punching
            playerCharacter.attack(npcCharacter);
        } else {
            // If no action key is pressed, set the character to idle
            playerCharacter.setAction(Character.Action.IDLE);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(playerCharacter.bounds.x, playerCharacter.bounds.y + playerCharacter.bounds.height + 5, playerCharacter.health, 5);
        shapeRenderer.end();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        playerCharacter.draw(batch, delta); // Update this line
        npcCharacter.draw(batch, delta); // And this line, if NPC has an animation
        batch.end();
    }

    @Override
    public void hide() {
        batch.dispose();
        shapeRenderer.dispose();
        playerCharacter.dispose(); // You should implement this method
        npcCharacter.dispose(); // You should implement this method
    }


    // Implement other Screen methods as needed
}
