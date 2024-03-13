package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

    private TextureRegion mainCharacterTextureRegion, mainCharacterBulletTextureRegion, backgroundTextureRegion, enemyTextureRegion, enemyBulletTextureRegion;

    //world parameters
    private final int WORLD_WIDTH = 256;
    private final int WORLD_HEIGHT = 128;

    //game objects
    private Character mainCharacter;
    private Character enemy;

        //just add the enemy here
    private Character lvl1_enemy;

    private LinkedList<Bullet> playerBulletLists;
    private LinkedList<Bullet> enemyBulletLists;

    BattleScreen(){
        //setting the screen
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //setting the texture atlas, (using the texture atlas for more efficient memory i guess)
        textureAtlas = new TextureAtlas("images.atlas");

        //background and graph

        backgroundTextureRegion = textureAtlas.findRegion("background");

        //main character
        mainCharacterTextureRegion = textureAtlas.findRegion("char");
        mainCharacterBulletTextureRegion = textureAtlas.findRegion("laserBlue08");

        //enemy
        enemyTextureRegion = textureAtlas.findRegion("Rchar2");
        enemyBulletTextureRegion = textureAtlas.findRegion("laserRed08");

        //initializing the game objects
        //the character dimension is the same for the width and the height
        mainCharacter = new MainCharacter(30, 100, WORLD_WIDTH/8, WORLD_HEIGHT/8, 15, 15, 3, 1,30,0.5f, mainCharacterBulletTextureRegion, mainCharacterTextureRegion);
        enemy = new Enemy(2, 100, WORLD_WIDTH/1.5f, WORLD_HEIGHT/8, 15, 15, 5, 1,30,0.8f, enemyBulletTextureRegion, enemyTextureRegion);

        enemyBulletLists = new LinkedList<>();
        playerBulletLists = new LinkedList<>();

        batch = new SpriteBatch();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();

        detectInput(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCharacter.update(delta);
        enemy.update(delta);

        //temporary background
        //renderBackground();

        mainCharacter.draw(batch);
        enemy.draw(batch);

        renderBullet(delta);

        detectCollision();

        batch.end();
    }

    private void detectCollision(){
        ListIterator<Bullet> iterator = playerBulletLists.listIterator();
        while(iterator.hasNext()){
            Bullet bullet = iterator.next();
            if(enemy.intersects(bullet.boundingBox)){
                enemy.hit(bullet);
                iterator.remove();
            }
        }

        iterator = enemyBulletLists.listIterator();
        while(iterator.hasNext()){
            Bullet bullet = iterator.next();
            if(mainCharacter.intersects(bullet.boundingBox)){
                mainCharacter.hit(bullet);
                iterator.remove();
            }
        }
    }


    //mostly for movement
    public void detectInput(float delta) {
        //limits
        float leftLimit,rightLimit,upLimit, downLimit;

        leftLimit = -mainCharacter.boundingBox.x;

            //change the down limit for the platform
        downLimit = - mainCharacter.boundingBox.y;

        rightLimit = WORLD_WIDTH - mainCharacter.boundingBox.x - mainCharacter.boundingBox.width;
        upLimit = WORLD_HEIGHT - mainCharacter.boundingBox.y - mainCharacter.boundingBox.height;

        // keyboard input

        if (Gdx.input.isKeyPressed(Input.Keys.D) && rightLimit > 0) {

            mainCharacter.translate(Math.min(mainCharacter.movementSpeed * delta, rightLimit), 0f);
            //touch input (mouse)
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && upLimit > 0) {

            mainCharacter.translate(0f, Math.min(mainCharacter.movementSpeed * delta, upLimit));

        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && leftLimit < 0) {

            mainCharacter.translate(Math.max(-mainCharacter.movementSpeed * delta, leftLimit),0f);

        }

        //this is still moving downward, the ducking system maybe reduce the the height by 70% ish, need to test
        if (Gdx.input.isKeyPressed(Input.Keys.S) && downLimit < 0) {

            mainCharacter.translate(0f, Math.max(-mainCharacter.movementSpeed * delta, downLimit));

        }
        //maybe the shield mechanic if possible

        //mouse and touchpad input

    }
    public void renderBullet(float delta) {

        // when i already understand the input from keyboard, change to if input then fire
        // temporary input using the Q button
        if (mainCharacter.canFire() && Gdx.input.isKeyPressed(Input.Keys.Q)) {
            Bullet bullet = mainCharacter.fireBullet();
            playerBulletLists.add(bullet);
        }

        ListIterator<Bullet> iterator = playerBulletLists.listIterator();
        while(iterator.hasNext()){
            Bullet bullet = iterator.next();
            bullet.draw(batch);
            bullet.boundingBox.x += bullet.movementSpeed * delta;
            if(bullet.boundingBox.x >= WORLD_WIDTH){
                iterator.remove();
            }
        }

        if (enemy.canFire()) {
            Bullet bullet = enemy.fireBullet();
            enemyBulletLists.add(bullet);
        }

        iterator = enemyBulletLists.listIterator();
        while(iterator.hasNext()){
            Bullet bullet = iterator.next();
            bullet.draw(batch);
            bullet.boundingBox.x -= bullet.movementSpeed * delta;
            if(bullet.boundingBox.x < 0){
                iterator.remove();
            }
        }

    }


    @Override
    public void resize(int width, int height) {

        viewport.update(width,height, true);

        batch.setProjectionMatrix(camera.combined);
    }

    private void renderBackground(){
        batch.draw(backgroundTextureRegion, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.setProjectionMatrix(camera.combined);
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
