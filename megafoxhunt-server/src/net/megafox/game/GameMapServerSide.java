package net.megafox.game;

import net.megafoxhunt.shared.GameMapSharedConfig;

public class GameMapServerSide {
	
	private GameMapSharedConfig config;
	
	public String getName(){return config.getName();}
	
	public int getWidth(){return config.getWidth();}
	public int getHeight(){return config.getHeight();}
	
	private int[][] collisionMap;
	public int[][] getCollisionMap(){return collisionMap;}
	
	public GameMapServerSide(GameMapSharedConfig mapConfig){
		this.config = mapConfig;
		loadCollisionMap();
	}
	private void loadCollisionMap(){
		collisionMap = new int[getWidth()][getHeight()];
	}

}
