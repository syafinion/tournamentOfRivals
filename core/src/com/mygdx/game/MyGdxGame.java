package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	// Since you are extending Game, you no longer need the SpriteBatch and Texture for this class.
	// They will be used in the individual screens.

	@Override
	public void create() {
		// This will work because you're now extending Game, which contains the setScreen method.
		//setScreen(new MainMenuScreen(this));
		setScreen(new GamePlayScreen(this));// only for testing for a moment
	}

	@Override
	public void render() {
		// Calls the render method of the current screen
		super.render();
	}

	@Override
	public void dispose() {
		// Screens should dispose of their own assets.
		// You can also dispose of global assets here if you have any.
	}
}
