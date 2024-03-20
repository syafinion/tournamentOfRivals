package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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

    //for coin animation use
        //also use private SpriteBatch batch
    private Texture texture;
    private Animation<TextureRegion> animation;
    private float elapsedTime= 0f;

    private TextureRegion mainCharacterTextureRegion, mainCharacterBulletTextureRegion, backgroundTextureRegion, enemyTextureRegion, enemyBulletTextureRegion;

    //world parameters
    private final int WORLD_WIDTH = 256;
    private final int WORLD_HEIGHT = 128;

    //game objects
    private Character mainCharacter;
    private Character enemy;

    private Coin coin1, coin2;
    private LinkedList<Coin> coinLinkedList;

    private LinkedList<Bullet> playerBulletLists;
    private LinkedList<Bullet> enemyBulletLists;

    BattleScreen(){
        //setting the screen
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //coin animation
        batch = new SpriteBatch();
        texture = new Texture("Coins.png");

        int frameWidth = texture.getWidth() / 6;
        int frameHeight = texture.getHeight();

        float frameDuration = 0.1f;
        TextureRegion[][] frames = TextureRegion.split(texture, frameWidth, frameHeight);
        TextureRegion[] animationFrames = frames[0];
        animation = new Animation<>(frameDuration, animationFrames);


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
        mainCharacter = new MainCharacter(30, 100, WORLD_WIDTH/8, 7.5f, 15, 15, 3, 1,30,0.5f,0, mainCharacterBulletTextureRegion, mainCharacterTextureRegion);
        enemy = new Enemy(30, 100, WORLD_WIDTH/1.5f, 7.5f, 15, 15, 5, 1,30,0.8f,0, enemyBulletTextureRegion, enemyTextureRegion);

        //coin position ( add the coin and position on here)
        coin1 = new Coin(200, 5, 10, 5, frameDuration, null, animationFrames);
        coin2 = new Coin(100, 78, 10, 5, frameDuration, null, animationFrames);
//        coin3 =();
//        coin4 =();

        coinLinkedList = new LinkedList<>();
        enemyBulletLists = new LinkedList<>();
        playerBulletLists = new LinkedList<>();

        batch = new SpriteBatch();
    }


    @Override
    public void show() {
        addCoin();
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;
        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);
        batch.begin();

        detectInput(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCharacter.update(delta);
        enemy.update(delta);

        //render coin
        coinCollision(delta);

        System.out.println(mainCharacter.coinCounter);
        //temporary background
        //renderBackground();

        mainCharacter.draw(batch);
        enemy.draw(batch);

        renderBullet(delta);

        detectCollision();

        batch.end();
    }

    public void renderBullet(float delta) {
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

    public void coinCollision(float delta){
        ListIterator<Coin> iterator = coinLinkedList.listIterator();

            while(iterator.hasNext()){
                Coin coin = iterator.next();

                if(mainCharacter.intersects(coin.boundingBox)){
                    mainCharacter.coinCounter++;
                    iterator.remove();
                }
                coin.render(batch);
                coin.update(delta);
            }
        }
    public void addCoin(){
        coinLinkedList.add(coin1);
        coinLinkedList.add(coin2);
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
        downLimit = - mainCharacter.boundingBox.y;
        rightLimit = WORLD_WIDTH - mainCharacter.boundingBox.x - mainCharacter.boundingBox.width;
        upLimit = WORLD_HEIGHT - mainCharacter.boundingBox.y - mainCharacter.boundingBox.height;

        // movement of the player
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


        //movement of the second player, aka the chaser
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0) {

            enemy.translate(Math.min(enemy.movementSpeed * delta, rightLimit), 0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && upLimit > 0) {

            enemy.translate(0f, Math.min(enemy.movementSpeed * delta, upLimit));

        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0) {

            enemy.translate(Math.max(-enemy.movementSpeed * delta, leftLimit),0f);

        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && downLimit < 0) {

            enemy.translate(0f, Math.max(-enemy.movementSpeed * delta, downLimit));

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
