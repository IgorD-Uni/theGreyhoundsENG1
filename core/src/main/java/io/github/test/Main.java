package io.github.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    //Declare the starting screen
    startScreen startScreen;
    //Declare all textures
    Player player;



    //Declare camera, viewport as well as map (environmental and visible elements)

     SpriteBatch spriteBatch;
     ExtendViewport viewport;
     GameMap map;

     TiledMapTileLayer colisionLayer;
     OrthogonalTiledMapRenderer tMR;
     private OrthographicCamera camera; 


    @Override
    public void create() {
        //Load in all textures and declare sprite batch ( it allows them to go to the GPU)
        player = new Player("playerSprite.png",100,150);
        spriteBatch = new SpriteBatch();



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
        //camera.position.set(player.playerX, player.playerY, 1); Follow player movement


        // Set camera to the quadrant that the player is currently in
        if (player.playerX < 512 && player.playerY < 512){
            camera.position.set(256,256,1);

        }
        else if (player.playerX >= 512 && player.playerY < 512) {
            camera.position.set(256+512, 256, 1);

        }
        else if (player.playerX < 512 && player.playerY >= 512) {
            camera.position.set(256, 256+512, 1);
        }
        else if (player.playerX >= 512 && player.playerY >= 512) {
            camera.position.set(256+512, 256+512, 1);
        }
        camera.update();

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

        //spriteBatch.draw(backgroundTexture,0,0,worldHeight,worldWidth);


        // Render the foreground (i.e trees)
        spriteBatch.draw(player.playerTexture, player.playerX-16, player.playerY-16 ,32,32);


        spriteBatch.end();
        map.tMR.render(map.foregroundLayers);

        // Check if starting screen needs to be displayed over the map.

        if (startScreen.displaying) {
            startScreen.displayScreen();
        }
    }
}
