package io.github.test;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameMap{
    TiledMap tiles;
    TiledMapTileLayer collisionLayer;
    OrthogonalTiledMapRenderer tMR;

    int[] backgroundLayers = {0,1,2,3 };
    int[] foregroundLayers = { 4,5 };

    boolean collision;


    public GameMap(String givenMap){
        tiles = new TmxMapLoader().load(givenMap);
        tMR = new OrthogonalTiledMapRenderer(tiles);
        collisionLayer = (TiledMapTileLayer) tiles.getLayers().get(0);

    }


    public boolean checkCollision(Player givePlayer, float delta) {
        collision = false;

        // Find collision on each corner of player sprite against full walls.

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
        collision = collisionLayer.getCell((int) givePlayer.playerX / 32, (int) (givePlayer.playerY+12) / 32).getTile()
            .getProperties().containsKey("blocked");
        if (collision) {
            return collision;
        }

        //Top right
        collision = collisionLayer.getCell((int) (givePlayer.playerX) / 32, (int) (givePlayer.playerY+12) / 32).getTile()
            .getProperties().containsKey("blocked");

        if (collision) {
            return collision;
        }

        // Compare with half walls, ie on left side of tile or right side of tile.

        //  !-----------------------------------------------------------------------------------!
        // Wall on RIGHT

        //Bottom left
        collision = collisionLayer.getCell((int) (givePlayer.playerX-16) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("right");


        // This extra check is necessary to make sure the player can touch the wall from both sides.
        if (collision && collisionLayer.getCell((int) (givePlayer.playerX-8) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("right")) {
            return collision;
        }

        //Bottom right
        collision = collisionLayer.getCell((int) (givePlayer.playerX-16) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("right");

        if (collision && collisionLayer.getCell((int) (givePlayer.playerX-8) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("right")) {
            return collision;
        }



        //Top left
        collision = collisionLayer.getCell((int) (givePlayer.playerX-16) / 32, (int) (givePlayer.playerY+12) / 32).getTile()
                .getProperties().containsKey("right");

        if (collision && collisionLayer.getCell((int) (givePlayer.playerX-8) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("right")) {
            return collision;
        }



        //Top right
        collision = collisionLayer.getCell((int) (givePlayer.playerX-16) / 32, (int) (givePlayer.playerY+12) / 32).getTile()
                .getProperties().containsKey("right");

        if (collision && collisionLayer.getCell((int) (givePlayer.playerX -8) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("right")) {
            return collision;
        }

        //  !-----------------------------------------------------------------------------------!
        // Wall on LEFT


        //Bottom left
        collision = collisionLayer.getCell((int) (givePlayer.playerX+16) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("left");


        // This extra check is necessary to make sure the player can touch the wall from both sides.
        if (collision && collisionLayer.getCell((int) (givePlayer.playerX+8) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("left")) {
            System.out.println("1");
            return collision;
        }

        //Bottom right
        collision = collisionLayer.getCell((int) (givePlayer.playerX+16) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("left");

        if (collision && collisionLayer.getCell((int) (givePlayer.playerX+8) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("left")) {
            System.out.println("2");
            return collision;
        }


        //Top left
        collision = collisionLayer.getCell((int) (givePlayer.playerX+16) / 32, (int) (givePlayer.playerY+12) / 32).getTile()
                .getProperties().containsKey("left");

        if (collision && collisionLayer.getCell((int) (givePlayer.playerX+8) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("left")) {
            System.out.println("3");
            return collision;
        }


        //Top right
        collision = collisionLayer.getCell((int) (givePlayer.playerX+16) / 32, (int) (givePlayer.playerY+12) / 32).getTile()
                .getProperties().containsKey("left");

        if (collision && collisionLayer.getCell((int) (givePlayer.playerX+8) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("left")) {
            System.out.println("4");
            return collision;
        }
        collision = false;
        return collision;
    }
    public boolean checkVictory(Player givePlayer, float delta) {
        collision = false;


        //Bottom left
        collision = collisionLayer.getCell((int) (givePlayer.playerX-8) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("victory");

        if (collision) {
            return true;
        }

        //Bottom right
        collision = collisionLayer.getCell((int) (givePlayer.playerX+8) / 32, (int) givePlayer.playerY / 32).getTile()
                .getProperties().containsKey("victory");

        if (collision) {
            return true;
        }

        //Top left
        collision = collisionLayer.getCell((int) givePlayer.playerX / 32, (int) (givePlayer.playerY+12) / 32).getTile()
                .getProperties().containsKey("victory");
        if (collision) {
            return true;
        }

        //Top right
        collision = collisionLayer.getCell((int) (givePlayer.playerX) / 32, (int) (givePlayer.playerY+12) / 32).getTile()
                .getProperties().containsKey("victory");

        if (collision) {
            return true;
        }
        return false;
    }
}
