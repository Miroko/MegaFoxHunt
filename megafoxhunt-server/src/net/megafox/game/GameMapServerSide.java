package net.megafox.game;

import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import net.megafox.entities.Berry;
import net.megafox.entities.Empty;
import net.megafox.entities.Entity;
import net.megafox.entities.Wall;
import net.megafoxhunt.shared.GameMapSharedConfig;

public class GameMapServerSide {
	
	public static final int TOTAL_BERRIES = 30;
	public static final int TOTAL_HOLES = 4;
	
	private static final Wall WALL = new Wall();
	private static final Empty EMPTY = new Empty();
	
	private GameMapSharedConfig config;
	
	public String getName(){return config.getName();}
	
	public int getWidth(){return config.getWidth();}
	public int getHeight(){return config.getHeight();}
	
	private Entity[][] collisionMap;
	public Entity[][] getCollisionMap(){return collisionMap;}
	
	public GameMapServerSide(GameMapSharedConfig mapConfig){
		this.config = mapConfig;
		loadCollisionMap(config.getBinaryMapPath());
	}
	private void loadCollisionMap(String path){
		collisionMap = new Entity[getWidth()][getHeight()];	
		
	    try {
			InputStream is = null;			
	    	is = new FileInputStream(path);
	    	BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	    	
	    	String line;
	    	int row = 0;
			while ((line = br.readLine()) != null){
			    String[] nums = line.split(",");
			    for (int col = 0; col < nums.length; col++){
			        int n = Integer.parseInt(nums[col]);
			        
			        // (height - 1 - row) to flip y 
			        collisionMap[col][(getHeight() - 1) - row] = (n == 0 ? EMPTY : WALL);
			    }
			    row++;
			}
			br.close();
		} catch (NumberFormatException | IOException e) {			
			e.printStackTrace();
		}

	}
	
	public boolean isBlocked(int x, int y) {
		if(x > 0 && x < getWidth()){
			if(y > 0 && y < getHeight()){
				if (collisionMap[x][y] == WALL) return true;
			}
		}		
		return false;
	}

	public void addEntity(Entity entity) {
		collisionMap[entity.getX()][entity.getY()] = entity;
	}
	
	public void removeEntity(Entity entity) {
		collisionMap[entity.getX()][entity.getY()] = EMPTY;
	}

	public void setEmpty(int x, int y) {
		collisionMap[x][y] = EMPTY;
	}
	
	public void setWall(int x, int y) {
		collisionMap[x][y] = WALL;
	}
	
	public Entity getEntity(int x, int y) {
		return collisionMap[x][y];
	}
}



