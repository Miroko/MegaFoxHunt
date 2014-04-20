package net.megafox.game;

import java.awt.Point;
import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.megafox.entities.Berry;
import net.megafox.entities.Empty;
import net.megafox.entities.Entity;
import net.megafox.entities.Hole;
import net.megafox.entities.Wall;
import net.megafoxhunt.server.IDHandler;
import net.megafoxhunt.shared.GameMapSharedConfig;

public class GameMapServerSide {
	
	public static final int TOTAL_BERRIES = 30;
	public static final int TOTAL_HOLES = 4;
	
	private static final Wall WALL = new Wall();
	private static final Empty EMPTY = new Empty();
	
	private GameMapSharedConfig config;
	public GameMapSharedConfig getConfig(){return config;}
	
	public String getName(){return config.getName();}
	
	public int getWidth(){return config.getWidth();}
	public int getHeight(){return config.getHeight();}
	
	private Entity[][] collisionMap;
	public Entity[][] getCollisionMap(){return collisionMap;}
	
	private ArrayList<Hole> holes;
	
	private IDHandler idHandler;
	
	private ArrayList<Point> dogSpawns;
	private ArrayList<Point> foxSpawns;
	
	public GameMapServerSide(GameMapSharedConfig mapConfig, IDHandler idHandler){
		this.config = mapConfig;
		this.idHandler = idHandler;
		holes = new ArrayList<Hole>();
		loadCollisionMap(config.getBinaryMapPath());
	}
	private void loadCollisionMap(String path){
		collisionMap = new Entity[getWidth()][getHeight()];
		holes = new ArrayList<Hole>();
		dogSpawns = new ArrayList<Point>();
		foxSpawns = new ArrayList<Point>();

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
			        
			        Entity target = null;
			        
			        if (n == 1 || n == 2 || n == 3 || n == 40 || n == 17 || n == 18 || n == 19 || n == 20 || n == 33 ||
			        		n == 34 || n == 35 || n == 49 || n == 50 || n == 51 || n == 5 || n == 37 || n == 38) {
			        	target = WALL;
			        } else if (n == 66) {
			        	Hole hole = new Hole(col, getHeight() - 1 - row, idHandler.getFreeID());
			        	holes.add(hole);
			        	target = hole;
			        } else {
			        	if (n == 89) foxSpawns.add(new Point(col, getHeight() - 1 - row));
			        	else if (n == 90) dogSpawns.add(new Point(col, getHeight() - 1 - row));
			        	
			        	target = EMPTY;
			        }

			        collisionMap[col][(getHeight() - 1) - row] = target;
			    }
			    row++;
			}

			br.close();
		} catch (NumberFormatException | IOException e) {			
			e.printStackTrace();
		}

	}
	
	public boolean isBlocked(int x, int y) {
		if(x >= 0 && x < getWidth()){
			if(y >= 0 && y < getHeight()){
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
		if (collisionMap[x][y] instanceof Hole) {
			holes.remove(collisionMap[x][y]);
		}
		collisionMap[x][y] = EMPTY;
	}
	
	public void setWall(int x, int y) {
		collisionMap[x][y] = WALL;
	}
	
	public Entity getEntity(int x, int y) {
		return collisionMap[x][y];
	}
	
	public boolean canExplode(int x, int y) {
		if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) return false;
		
		if (collisionMap[x][y] == WALL || collisionMap[x][y] instanceof Hole) return true;
		
		return false;
	}

	public ArrayList<Hole> getHoles() {
		return holes;
	}
	
	public int getHolesSize() {
		return holes.size();
	}
	
	public Point getDogSpawn(int index) {
		if (dogSpawns.size() <= 0) return getRandomEmptyPoint();
		return dogSpawns.get(index % dogSpawns.size());
	}
	
	public Point getFoxSpawn(int index) {
		if (foxSpawns.size() <= 0) return getRandomEmptyPoint();
		return foxSpawns.get(index % foxSpawns.size());
	}
	
	private Point getRandomEmptyPoint() {
		Random ran = new Random();
		
		Point p = null;
		while (p == null) {
			int x = ran.nextInt(getWidth());
			int y = ran.nextInt(getHeight());
			if (collisionMap[x][y] != WALL) {
				p = new Point(x, y);
			}
		}
		
		return p;
	}
}



