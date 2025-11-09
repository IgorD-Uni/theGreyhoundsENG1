package io.github.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    //Declare the starting screen
    startScreen startScreen;
    //Declare all textures
    Player player;
    object statue1;
    SpeedBoostItem speedBoostItem;
    BitmapFont font;
    BitmapFont hudFont;

    Texture victoryScreen;

    boolean victory = false;

    //The speed boost variables
    boolean boostActive = false;
    float boostTimer = 0f;

    //Declare camera, viewport as well as map (environmental and visible elements)

     SpriteBatch spriteBatch;
     ExtendViewport viewport;
     GameMap map;

     TiledMapTileLayer colisionLayer;
     OrthogonalTiledMapRenderer tMR;
     private OrthographicCamera camera;
    ShapeRenderer shapeRenderer;

    //Declarations needed for the negative eevent
    private NegativeEventItem negativeEventItem;
    private Array<StudentSprite> studentSprites;
    private boolean negativeEventActive = false;
    private float negativeEventTimer = 0;

    // Game timer
    private float gameTimer = 300f;
    private BitmapFont timerFont;
    private boolean timeUp = false;
    private Texture timeUpTexture;

    // Respawn timers
    private float speedBoostRespawnTimer = 0f;
    private float negativeEventRespawnTimer = 0f;

    // Counters
    private int speedBoostCount = 0;
    private int negativeEventCount = 0;

    // Item images for display
    private Texture speedBoostIcon;
    private Texture negativeEventIcon;

    // Make enemy (dean/goose)
    private Goose goose;


    @Override
    public void create() {
        //Load in all textures and declare sprite batch ( it allows them to go to the GPU)



        player = new Player("playerSprite.png",200,80);
        statue1= new object("duckStatue.png", 80,1968,100,100);
        spriteBatch = new SpriteBatch();



        // Creating text to show when positive event has been activated
        font = new BitmapFont();
        font.getData().setScale(2f);

        // create a speed boost item
        speedBoostItem = new SpeedBoostItem("speedBike2.png", 430, 200);


        // Viewport allows your screen to stay the same resolution


        //When loading tiled map make sure to have all the required files inluding the sprite sheet
        //TSX file and the entired asset file that leads to assets you have used, else it will not
        //compile.
        viewport = new ExtendViewport(1000,1000,1024,1024);
        map = new GameMap("Maze.tmx");


        //Orthogonal camera just means its 2D and wont change perspective, the parameter are the
        //area the camera has around the player.
        camera = new OrthographicCamera(512, 512);

        //Create starting screen
        startScreen = new startScreen("startScreen.png");

        //Creating the health bar
        shapeRenderer = new ShapeRenderer();
        //Creating the item for the negative event
        negativeEventItem = new NegativeEventItem("negativeEventItem.png", 300, 660);
        studentSprites = new Array<>();

        // Initialize timer font and image
        timerFont = new BitmapFont();
        timerFont.setColor(com.badlogic.gdx.graphics.Color.RED);
        timerFont.getData().setScale(2f);

        timeUpTexture = new Texture("time_up.png");

        speedBoostIcon = new Texture("speedBike2.png");
        negativeEventIcon = new Texture("negativeEventItem.png");

        hudFont = new BitmapFont();
        hudFont.getData().setScale(2f);
        hudFont.setColor(1, 1, 1, 1);

        // Create enemy (dean/goose)

        goose = new Goose("goose.png", 400,100,map);

        victoryScreen = new Texture("victoryScreen.png");

    }



    @Override
    //Seperated into different functions for clarity.
    public void render() {
        input();
        logic();
        draw();

    }

    @Override
    // changes behavior of the viewport when window is resized.
    public void resize(int width, int height){
        viewport.update(width, height, true);
    }

    @Override
    //Negligable in this project as there isnt much complexity, deletes sprites and textures
    //from memory. If we add many screens we may need to use this to avoid a memory leak.
    public void dispose() {
        spriteBatch.dispose();
        player.playerTexture.dispose();
        font.dispose();
        shapeRenderer.dispose();
        negativeEventItem.dispose();
        for (StudentSprite student : studentSprites) {
            student.dispose();
        }
        speedBoostIcon.dispose();
        negativeEventIcon.dispose();
        goose.gooseTexture.dispose();
    }
    //Input function regards anything requiring the user input such as keyboard and mouse
    //actions.
    private void input(){
        //Delta or delta time makes sure that a difference in framerate between devices
        //does not correspond to different speed etc.
        float delta = Gdx.graphics.getDeltaTime();


        // Stops other inputs when starter screen is displaying
        if (startScreen.displaying){
            return;
        }

            //This is for player movement, when keys are pressed it change an array in the Player
            //class that shows [0,0] if its [1,0] move in positive x direction, reverse for -1 and
            // the same applies to y axis. (see more in player class)
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                player.playerVector[0] = 1;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                player.playerVector[0] = -1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                player.playerVector[1] = 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                player.playerVector[1] = -1;
            }

            player.move(delta, map);

    }




    private void logic(){



        // Checking if the player touches the speed boost
        if (speedBoostItem.isActive()) {
            // Create rectangles to check for collision
            Rectangle playerRect = new Rectangle(player.playerX - 16, player.playerY - 16, 32, 32);
            Rectangle itemRect = speedBoostItem.getBounds();

            if (playerRect.overlaps(itemRect)) {
                speedBoostItem.collect(); // hide the item
                applySpeedBoost();// give the player the speed boost
                speedBoostCount++;
            }
            speedBoostRespawnTimer = 50f; // start 50-second respawn countdown
        }
        // creating a timer for the speed boost
        if (boostActive){
            boostTimer -= Gdx.graphics.getDeltaTime();
            if (boostTimer <= 0) {
                player.setSpeed(200f); // resetting speed to normal
                boostActive = false;
            }
        }


        // Set camera to the quadrant that the player is currently in
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if (player.playerX >= 512*i && player.playerY >= 512*j){
                    camera.position.set(256+512*i,256+512*j, 1);
                }

            }
        }

        camera.update();

        if (statue1.checkCollision(player)){
            player.playerX = 1980;
            player.playerY = 1980;
        }

        // If player collides with the negative event item
        if (negativeEventItem.isActive() && player.getBounds().overlaps(negativeEventItem.getBounds())) {
            negativeEventItem.collect();
            negativeEventActive = true;
            negativeEventCount++;
            negativeEventTimer = 30f; // lasts 30 seconds

            float spawnBaseX = player.playerX + 400; // start further to the right
            float spacing = 40f; // distance between each student
            float speed = 250f;

            // Spawn a few rows of students above and below player
            for (int i = 0; i < 6; i++) { // number of students
                float offsetX = i * spacing;
                studentSprites.add(new StudentSprite("student.png", spawnBaseX + offsetX, player.playerY + 35, speed)); // above
                studentSprites.add(new StudentSprite("student.png", spawnBaseX + offsetX, player.playerY - 35, speed)); // below
            }
            negativeEventRespawnTimer = 50f; // start 50-second respawn countdown

        }

        // If event is active
        if (negativeEventActive) {
            negativeEventTimer -= Gdx.graphics.getDeltaTime();

            // Move the students
            for (StudentSprite student : studentSprites) {
                student.update(Gdx.graphics.getDeltaTime());
            }

            // Check if player touches a students to slow down
            for (StudentSprite student : studentSprites) {
                if (student.isActive() && player.getBounds().overlaps(student.getBounds())) {
                    player.applySlowEffect(10f);
                    student.deactivate();
                }
            }

            // If time is up, remove students
            if (negativeEventTimer <= 0) {
                negativeEventActive = false;
                for (StudentSprite student : studentSprites) {
                    student.dispose();
                }
                studentSprites.clear();
            }
        }
        player.updateSlowTimer(Gdx.graphics.getDeltaTime());

        // Game Timer Logic
        if (!timeUp) {
            gameTimer -= Gdx.graphics.getDeltaTime();
            if (gameTimer <= 0) {
                gameTimer = 0;
                timeUp = true;
            }
        }

        // Item Respawn Logic
        if (!speedBoostItem.isActive()) {
            speedBoostRespawnTimer -= Gdx.graphics.getDeltaTime();
            if (speedBoostRespawnTimer <= 0) {
                // Respawn at the same position
                speedBoostItem.respawn();
            }
        }

        if (!negativeEventItem.isActive()) {
            negativeEventRespawnTimer -= Gdx.graphics.getDeltaTime();
            if (negativeEventRespawnTimer <= 0) {
                negativeEventItem.respawn();
            }
        }

        float delta = Gdx.graphics.getDeltaTime();
        if (!startScreen.displaying) {
           goose.update(delta,player);
        }

        if (map.checkVictory(player, delta)){
            victory = true;
        }



    }
    private void draw(){
        // Render on screen, in specific order 1) Background 2) Player 3)Foreground

        //Background and set camera
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);;
        map.tMR.setView(camera);
        map.tMR.render(map.backgroundLayers);
        viewport.apply();


        // Render sprites i.e. player and possible other characters
        spriteBatch.setProjectionMatrix(camera.combined);


        spriteBatch.begin();
        //Render the player
        spriteBatch.draw(player.playerTexture, player.playerX-16, player.playerY-16 ,32,32);
        spriteBatch.draw(goose.gooseTexture, goose.gooseX-32, goose.gooseY-32, 64,64);
        speedBoostItem.render(spriteBatch);
        spriteBatch.end();


        // Render the foreground (i.e trees)
        map.tMR.render(map.foregroundLayers);
        spriteBatch.begin();
        spriteBatch.draw(statue1.texture,statue1.positionX, statue1.positionY, statue1.width, statue1.height);
        spriteBatch.end();
        // Check if starting screen needs to be displayed over the map.
        // Drawing the health bar
        float barWidth = 200;
        float barHeight = 20;
        float border = 3f; // size of black border around red bar

        float x = camera.position.x + (camera.viewportWidth / 2) - barWidth - 20;
        float y = camera.position.y + (camera.viewportHeight / 2) - 40;

        float healthPercent = player.getHealth() / player.getMaxHealth();

        // Draw black background
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(x, y, barWidth, barHeight);

        // Draw smaller red bar (inside the black)
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(x + border, y + border, (barWidth - 2 * border) * healthPercent, barHeight - 2 * border);
        shapeRenderer.end();

        // Drawing the label for the health bar
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        BitmapFont font = new BitmapFont();
        font.setColor(0, 0, 0, 1);
        font.draw(spriteBatch, "Health:", x - 50, y + barHeight - 2);

        // Drawing for the negative event
        negativeEventItem.render(spriteBatch);

        if (negativeEventActive) {
            for (StudentSprite student : studentSprites) {
                student.render(spriteBatch);
            }
        }
        spriteBatch.end();


        // Draw boost timer text if active
        spriteBatch.begin();
        if (boostActive) {
            String message = String.format("Speed Boost: %.0f", boostTimer);
            font.draw(spriteBatch, message, camera.position.x - 240, camera.position.y + 240);
        }

        spriteBatch.end();

        // Draw game timer
        spriteBatch.begin();
        int minutes = (int)(gameTimer / 60);
        int seconds = (int)(gameTimer % 60);
        String timeText = String.format("%02d:%02d", minutes, seconds);
        timerFont.draw(spriteBatch, timeText, camera.position.x - camera.viewportWidth / 2 + 20, camera.position.y - camera.viewportHeight / 2 + 40);
        spriteBatch.end();

        if (timeUp) {
            spriteBatch.begin();
            spriteBatch.draw(timeUpTexture, camera.position.x - camera.viewportWidth / 2,
                    camera.position.y - camera.viewportHeight / 2,
                    camera.viewportWidth, camera.viewportHeight);
            spriteBatch.end();
        }

        // Event counters
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float screenWidth = viewport.getWorldWidth();
        float screenHeight = viewport.getWorldHeight();

        float iconSize = 60;
        float padding = 20;

        // Speed boost icon + counter
        spriteBatch.draw(speedBoostIcon, screenWidth - iconSize - padding - 60, padding, iconSize, iconSize);

        font.setColor(1, 1, 1, 1);
        hudFont.draw(spriteBatch, ": " + speedBoostCount, screenWidth - padding - 40, padding + 30);

        // Negative event icon + counter
        spriteBatch.draw(negativeEventIcon, screenWidth - iconSize - padding - 60, padding + 60, iconSize, iconSize);

        hudFont.draw(spriteBatch, ": " + negativeEventCount, screenWidth - padding - 40, padding + 90);

        spriteBatch.end();

        if (startScreen.displaying) {
            startScreen.displayScreen();
        }

        if (player.getHealth() <= 0){
            spriteBatch.begin();
            spriteBatch.draw(timeUpTexture,0,0,1024,1024);
            spriteBatch.end();
        }

        if (victory){
            spriteBatch.begin();
            spriteBatch.draw(victoryScreen,0,0,1024,1024);
            spriteBatch.end();
        }
    }
    private void applySpeedBoost() {
        player.setSpeed(400f); // double the normal speed
        boostTimer = 30f;      // boost lasts for 30 seconds
        boostActive = true;
        System.out.println("Speed boost activated");
    }
}


