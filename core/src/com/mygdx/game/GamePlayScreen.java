package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GamePlayScreen extends ScreenAdapter {
    final Game game;
    OrthographicCamera camera;
    SpriteBatch batch;
    Character playerCharacter;
    Character npcCharacter; // Add this if you want an NPC to interact with

    public GamePlayScreen(final Game game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        // Initialize your character and an NPC here, adjust the positions as needed
        playerCharacter = new Character("firstguy.jpeg", 100, 100, 64, 64);
        npcCharacter = new Character("firstguy.jpeg", 300, 100, 64, 64); // Make sure you have npc.png

    }

    private void update(float delta) {
        // Example input handling for attacking
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            playerCharacter.attack(npcCharacter);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        playerCharacter.draw(batch);
        npcCharacter.draw(batch); // Draw the NPC as well
        batch.end();
    }

    @Override
    public void hide() {
        batch.dispose();
        playerCharacter.image.dispose();
        npcCharacter.image.dispose(); // Dispose of the NPC's texture
    }

    // Implement other Screen methods as needed
}

