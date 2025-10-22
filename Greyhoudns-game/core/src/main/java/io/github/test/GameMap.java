package io.github.test;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameMap{
    TiledMap tiles;
    TiledMapTileLayer collisionLayer;
    OrthogonalTiledMapRenderer tMR;

    int[] backgroundLayers = { 1,2,3 };
    int[] foregroundLayers = { 4 };

    boolean collision;


    public GameMap(String givenMap){
        tiles = new TmxMapLoader().load(givenMap);
        tMR = new OrthogonalTiledMapRenderer(tiles);
        collisionLayer = (TiledMapTileLayer) tiles.getLayers().get(0);

    }


    public boolean checkCollision(Player givePlayer, float delta) {
        collision = false;

        // Find collision on each corner of player sprite

        //Bottom left
        collision = collisionLayer.getCell((int) (givePlayer.playerX-8) / 32, (int) givePlayer.playerY / 32).getTile()
            .getProperties().containsKey("blocked");

        if (collision) {
            return collision;
        }

        //Bottom right
        collision = collisionLayer.getCell((int) (givePlayer.playerX+8) / 32, (int) givePlayer.playerY / 32).getTile()
            .getProperties().containsKey("blocked");

        if (collision) {
            return collision;
        }

        //Top left
        collision = collisionLayer.getCell((int) givePlayer.playerX / 32, (int) (givePlayer.playerY-10) / 32).getTile()
            .getProperties().containsKey("blocked");
        if (collision) {
            return collision;
        }

        //Top right
        collision = collisionLayer.getCell((int) (givePlayer.playerX) / 32, (int) (givePlayer.playerY) / 32).getTile()
            .getProperties().containsKey("blocked");

        if (collision) {
            return collision;
        }
        return collision;


    }
}
