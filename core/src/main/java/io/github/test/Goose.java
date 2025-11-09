package io.github.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import java.util.LinkedList;

public class Goose {
    Texture gooseTexture;
    public float gooseX;
    public float gooseY;
    public float speed;
    public float normalSpeed = 300.0F;
    private boolean paused = false;
    private float pauseTimer = 0.0F;
    private float pathFindTimer = 0.0F;
    private GameMap map;
    private int tileSize = 32;
    private int mapWidthTiles = 64;
    private int mapHeightTiles = 64;
    private boolean[][] collisionGrid;
    private int pauseCounter = 0;
    private float lastPlayerX = 0.0F;
    private float lastPlayerY = 0.0F;
    private float width = 32.0F;
    private float height = 32.0F;
    private float dx = 0.0F;
    private float dy = 0.0F;
    private float moveTimer = 2.0F;
    private boolean active = true;
    private LinkedList<Path> path;
    boolean canSee;
    public Goose(String givenTexture, float x, float y, GameMap givenMap) {
        this.gooseTexture = new Texture(givenTexture);
        this.gooseX = x;
        this.gooseY = y;
        this.speed = this.normalSpeed;
        this.map = givenMap;
        this.collisionGrid = this.extractCollisionGrid(givenMap);
    }

    private boolean[][] extractCollisionGrid(GameMap givenMap) {
        boolean[][] grid = new boolean[this.mapWidthTiles][this.mapHeightTiles];
        int xPosition = 0;
        while (xPosition < this.mapWidthTiles) {
            int yPosition = 0;
            while (yPosition < this.mapHeightTiles) {
                int tileX = xPosition;
                int tileY = yPosition;
                if (givenMap.collisionLayer != null) {
                    if (givenMap.collisionLayer.getCell(tileX, tileY).getTile().getProperties().containsKey("blocked") ||
                            givenMap.collisionLayer.getCell(tileX, tileY).getTile().getProperties().containsKey("left") ||
                            givenMap.collisionLayer.getCell(tileX, tileY).getTile().getProperties().containsKey("right"))
                     {
                        grid[xPosition][yPosition] = true;
                    }
                }
                yPosition++;
            }
            xPosition++;
        }
        boolean[][] finalGrid = grid;

        return finalGrid;
    }

    public void update(float delta, Player player) {
        if (this.paused == true) {
            float pauseTimer = this.pauseTimer;
            pauseTimer = pauseTimer - delta;
            this.pauseTimer = pauseTimer;
            if (pauseTimer <= 0.0F) {
                this.paused = false;
                this.pauseCounter = 0;
            }
            return;
        }

        int gooseTileX = (int) (this.gooseX / (float) this.tileSize);
        int gooseTileY = (int) (this.gooseY / (float) this.tileSize);
        int playerTileX = (int) (player.playerX / (float) this.tileSize);
        int playerTileY = (int) (player.playerY / (float) this.tileSize);

        float distance = this.getDistance(gooseTileX, gooseTileY, playerTileX, playerTileY);
         canSee = this.lineOfSight(gooseTileX, gooseTileY, playerTileX, playerTileY);


        if (canSee == true) {
            this.speed = 250.0F;
            this.pathFinding(delta, player);
        } else if (distance >= 5.0F) {
            this.speed = 200.0F;
            this.pathFinding(delta, player);
        } else if (distance >= 5.0F && distance <= 10.0F) {
            this.speed = 180.0F;
            if (Math.random() < 0.7) {
                this.pathFinding(delta, player);
            } else {
                this.moveRandom(delta);
            }
        } else if (distance < 5.0F) {
            this.speed = 150.0F;
            this.moveRandom(delta);
        }

        Rectangle gooseHitbox = this.getGooseHitbox();
        Rectangle playerHitbox = player.getBounds();
        if (gooseHitbox.overlaps(playerHitbox) == true) {
            player.takeDamage(10.0F);
            this.pause();
        }

        this.lastPlayerX = player.playerX;
        this.lastPlayerY = player.playerY;
    }

    private float getDistance(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        int result = dx * dx + dy * dy;

        return (float) Math.sqrt(result);
    }

    private boolean lineOfSight(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int steps = Math.max(dx, dy);
        if (steps == 0) {
            return true;
        }
        for (int i = 0; i <= steps; i++) {
            float ratio = (float) i / (float) steps;
            int checkX = (int) ((float) x1 + ratio * (float) (x2 - x1));
            int checkY = (int) ((float) y1 + ratio * (float) (y2 - y1));
            if (checkX < 0) return false;
            if (checkX >= this.mapWidthTiles) return false;
            if (checkY < 0) return false;
            if (checkY >= this.mapHeightTiles) return false;

            if (this.collisionGrid[checkX][checkY]) {
                boolean blocked = false;
                return blocked;
            }
        }
        boolean canSee = true;
        return canSee;
    }

    private void pathFinding(float delta, Player player) {
        if (pathFindTimer > 0){
            pathFindTimer = pathFindTimer-delta;
            return;
        }
        int playerTileX = (int) (player.playerX / (float) this.tileSize);
        int playerTileY = (int) (player.playerY / (float) this.tileSize);

        if (this.path == null || this.path.isEmpty() ||
                (int) (this.lastPlayerX / (float) this.tileSize) != playerTileX ||
                (int) (this.lastPlayerY / (float) this.tileSize) != playerTileY) {
            findPath((int) (this.gooseX / (float) this.tileSize), (int) (this.gooseY / (float) this.tileSize),
                    (int) (player.playerX / (float) this.tileSize), (int) (player.playerY / (float) this.tileSize));
        }
        if (this.path != null && !this.path.isEmpty()) {
            Path target = this.path.get(0);
            float targetX = (float) (target.x * this.tileSize) + (float) this.tileSize / 2.0F;
            float targetY = (float) (target.y * this.tileSize) + (float) this.tileSize / 2.0F;
            float dx = targetX - this.gooseX;
            float dy = targetY - this.gooseY;
            float result = dx * dx + dy * dy;

            float distance = (float) Math.sqrt(result);
            if (distance < 5.0F) {
                this.path.remove(0);
            } else {
                float oldX = this.gooseX;
                float oldY = this.gooseY;

                this.gooseX += dx / distance * this.speed * delta;
                this.gooseY += dy / distance * this.speed * delta;

                if (this.collideWithWall(this.gooseX, this.gooseY)) {
                    this.gooseX = oldX;
                    this.gooseY = oldY;
                    this.path = null;
                }
            }
        }
    }

    private void moveRandom(float delta) {
        this.moveTimer -= delta;
        if (this.moveTimer <= 0.0F) {
            int[][] directions = new int[][]{
                    {0, 1}, {0, -1}, {-1, 0}, {1, 0},
                    {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
            };
            int randomIndex = (int) (Math.random() * directions.length);
            float directionX = (float) directions[randomIndex][0];
            float directionY = (float) directions[randomIndex][1];
            float result = directionX * directionX + directionY * directionY;

            float length = (float) Math.sqrt(result);
            if (length > 0) {
                this.dx = directionX / length;
                this.dy = directionY / length;
            }
            this.moveTimer = 20.0F;
        }

        float moveX = this.dx * this.speed * delta;
        float moveY = this.dy * this.speed * delta;

        float newX = this.gooseX + moveX;
        float newY = this.gooseY + moveY;

        int tileX = (int) (newX / (float) this.tileSize);
        int tileY = (int) (newY / (float) this.tileSize);

        if (tileX >= 0 && tileX < this.mapWidthTiles && tileY >= 0 && tileY < this.mapHeightTiles) {
            if (!this.collisionGrid[tileX][tileY]) {
                this.gooseX = newX;
                this.gooseY = newY;
            } else {
                int[][] newDirections = new int[][]{
                        {0, 1}, {0, -1}, {-1, 0}, {1, 0},
                        {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
                };
                int newIndex = (int) (Math.random() * newDirections.length);
                float newDirectionX = (float) newDirections[newIndex][0];
                float newDirectionY = (float) newDirections[newIndex][1];
                float newResult = newDirectionX * newDirectionX + newDirectionY * newDirectionY;
                float len = (float) Math.sqrt(newResult);
                if (len > 0) {
                    this.dx = newDirectionX / len;
                    this.dy = newDirectionY / len;
                }
            }
        } else {
            int[][] newDirections = new int[][]{
                    {0, 1}, {0, -1}, {-1, 0}, {1, 0},
                    {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
            };
            int newIndex = (int) (Math.random() * newDirections.length);
            float newDirectionX = (float) newDirections[newIndex][0];
            float newDirectionY = (float) newDirections[newIndex][1];
            float newResult = newDirectionX * newDirectionX + newDirectionY * newDirectionY;
            float len = (float) Math.sqrt(newResult);
            if (len > 0) {
                this.dx = newDirectionX / len;
                this.dy = newDirectionY / len;
            }
        }
    }


    public void render(SpriteBatch batch) {
        if (this.active) {
            batch.draw(this.gooseTexture, this.gooseX - 16.0F, this.gooseY - 16.0F, this.width, this.height);
        }
    }

    private Rectangle getGooseHitbox() {
        return new Rectangle(this.gooseX , this.gooseY , 32.0F, 32.0F);
    }

    private void pause() {
        this.paused = true;
        this.pauseTimer = 3.0F;
    }

    private void findPath(int startX, int startY, int endX, int endY) {
        LinkedList<Path> nodeCheck = new LinkedList<>();
        boolean[][] visitedTiles = new boolean[this.mapWidthTiles][this.mapHeightTiles];
        Path startingNode = new Path(startX, startY, null);
        nodeCheck.add(startingNode);
        visitedTiles[startX][startY] = true;
        int[][] optionsDirection = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
        while (!nodeCheck.isEmpty()) {
            Path currentNode = nodeCheck.removeFirst();

            if (currentNode.x == endX && currentNode.y == endY) {
                LinkedList<Path> resultPath = new LinkedList<>();
                Path current = currentNode;

                while (current != null) {
                    resultPath.addFirst(current);
                    current = current.previous;
                }
                if (canSee) {
                    this.path = resultPath;
                    return;
                }
                pathFindTimer = 0.01f;
                this.path = resultPath;

                return;
            }

            for (int i = 0; i < 4; i++) {
                int[] direction = optionsDirection[i];
                int adjacentX = currentNode.x + direction[0];
                int adjacentY = currentNode.y + direction[1];

                if (this.isWalkable(adjacentX, adjacentY) && !visitedTiles[adjacentX][adjacentY]) {
                    visitedTiles[adjacentX][adjacentY] = true;
                    Path newNode = new Path(adjacentX, adjacentY, currentNode);
                    nodeCheck.addLast(newNode);
                }
            }
        }
        //this.path = null;

    }

    private class Path {
        int x;
        int y;
        Path previous;

        Path(int x, int y, Path previous) {
            this.x = x;
            this.y = y;
            this.previous = previous;
        }
    }

    private boolean isWalkable(int x, int y) {
        if (x < 0 || x >= this.mapWidthTiles || y < 0 || y >= this.mapHeightTiles) {
            return false;
        }

        return !this.collisionGrid[x][y];
    }

    private boolean collideWithWall(float newX, float newY) {


        return false;
    }

    public void dispose() {
        this.gooseTexture.dispose();
    }
}