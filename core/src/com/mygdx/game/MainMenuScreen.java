
package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class MainMenuScreen extends ScreenAdapter {
    private Stage stage;
    private Game game;

    public MainMenuScreen(Game game) {
        this.game = game;
    }

    public void show() {
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this.stage);

        // Create a new empty Skin
        Skin skin = new Skin();

        // Create a BitmapFont
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        // Create a TextButtonStyle
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.fontColor = Color.WHITE;
        skin.add("default", textButtonStyle);

        Table table = new Table();
        table.setFillParent(true);
        this.stage.addActor(table);

        // Create buttons with the new style
        TextButton playButton = new TextButton("PLAY", skin);
        TextButton settingsButton = new TextButton("SETTINGS", skin);
        TextButton tutorialButton = new TextButton("TUTORIAL", skin);
        TextButton quitButton = new TextButton("QUIT", skin);



        playButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            }
        });
        settingsButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            }
        });
        tutorialButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            }
        });
        quitButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        table.add(playButton).fillX().uniformX();
        table.row().pad(10.0F, 0.0F, 10.0F, 0.0F);
        table.add(settingsButton).fillX().uniformX();
        table.row();
        table.add(tutorialButton).fillX().uniformX();
        table.row();
        table.add(quitButton).fillX().uniformX();
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        Gdx.gl.glClear(16384);
        this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 0.033333335F));
        this.stage.draw();
    }

    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
    }

    public void hide() {
        this.stage.dispose();
    }
}