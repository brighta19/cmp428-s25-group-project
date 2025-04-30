package main;
import java.awt.*;

public class PlatformerGame extends GameBase {
    Player player = new Player(50, 50);
    TileMap map = new TileMap("map1.txt" , 64)

    public void initialize() {}

    public void inGameLoop() {
        if (pressing[UP]) {
        	player.moveUp(10); 
        	Camera.moveUp(10);
        }
        if (pressing[DN]) {
        	player.moveDown(10);
        	Camera.moveDown(10);
        }
        if (pressing[LT]) {
        	player.moveLeft(10);
        	Camera.moveLeft(10);
        }
        if (pressing[RT]) {
        	player.moveRight(10);
        	Camera.moveRight(10);
        }

        Rect[] bounds = TileMap.maps[TileMap.current].getBounds();
        
        for(int i = 0; i < bounds.length; i++) {
        	if(player.overlaps(bounds[i])) {
        		if(player.cameFromAbove()) {
        			player.pushBy(  0,  10);
        			Camera.moveDown(10);
        		}
        		if(player.cameFromBelow()) {
        			player.pushBy(  0, -10);
        			Camera.moveUp(10);
        		}
        		if(player.cameFromLeft() ) {
        			player.pushBy(-10, 0);
        			Camera.moveLeft(10);
        		}
        		if(player.cameFromRight()) {
        			player.pushBy( 10,   0);
        			Camera.moveRight(10);
        		}
        	}
        }
    }

    public void paint(Graphics pen) {
        player.draw(pen);
    }
}
