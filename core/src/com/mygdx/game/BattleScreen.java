package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.LinkedList;
import java.util.ListIterator;

class BattleScreen implements Screen{
    //screen itself
    private Camera camera;
    private Viewport viewport;

    //graph
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;

    private TextureRegion characterTextureRegion, bulletTextureRegion, backgroundTextureRegion;

    //world parameters
    private final int WORLD_WIDTH = 256;
    private final int WORLD_HEIGHT = 128;

    //game objects
    private Character mainCharacter;

        //just add the enemy here
    private Character lvl1_enemy;

    private LinkedList<Bullet> playerBulletList;
    private LinkedList<Bullet> enemyBulletLists;

    BattleScreen(){
        //setting the screen
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //setting the texture atlas, (using the texture atlas for more efficient memory i guess)
        textureAtlas = new TextureAtlas("images.atlas");

        //background and graph
        backgroundTextureRegion = textureAtlas.findRegion("background");
        characterTextureRegion = textureAtlas.findRegion("char");
        bulletTextureRegion = textureAtlas.findRegion("laserBlue08");

        //initializing the game objects
        //the character dimension is the same for the width and the height
        mainCharacter = new MainCharacter(2, 100, WORLD_WIDTH/8, WORLD_HEIGHT/8, 15, 15, 3, 1,30,0.5f,bulletTextureRegion, characterTextureRegion);

        playerBulletList = new LinkedList<>();

        batch = new SpriteBatch();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCharacter.update(delta);

        //temporary background
        renderBackground(delta);

        mainCharacter.draw(batch);

        renderBullet(delta);

        batch.end();
    }

    public void renderBullet(float delta) {

        if (mainCharacter.canFire()) {
            Bullet bullet = mainCharacter.fireBullet();
            playerBulletList.add(bullet);
        }

        ListIterator<Bullet> iterator = playerBulletList.listIterator();
        while(iterator.hasNext()){
            Bullet bullet = iterator.next();
            bullet.draw(batch);
            bullet.xPosition += bullet.movementSpeed * delta;
            if(bullet.xPosition >= WORLD_WIDTH){
                iterator.remove();
            }
        }

    }


    @Override
    public void resize(int width, int height) {

        viewport.update(width,height, true);

        batch.setProjectionMatrix(camera.combined);
    }

    private void renderBackground(float delta){
        batch.draw(backgroundTextureRegion, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
