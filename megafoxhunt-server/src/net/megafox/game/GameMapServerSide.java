package net.megafox.game;

import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

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
		loadCollisionMap(config.getBinaryMapPath());
	}
	private void loadCollisionMap(String path){
		collisionMap = new int[getWidth()][getHeight()];	
		
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
			        collisionMap[col][(getHeight()-1) - row] = n;
			        System.out.print(n);
			    }
			    row++;
			    System.out.println();
			}
			br.close();
		} catch (NumberFormatException | IOException e) {			
			e.printStackTrace();
		}

	}
}



