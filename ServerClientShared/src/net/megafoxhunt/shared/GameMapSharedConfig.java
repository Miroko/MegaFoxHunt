package net.megafoxhunt.shared;

public class GameMapSharedConfig {
	
	public static final GameMapSharedConfig DEBUG_MAP = new GameMapSharedConfig("Debug", "data/basic_map.tmx", "data/basic_map.txt", 35, 25, 10, 5 );	
	
	private String name;
	private String tiledMapPath;
	private String binaryMapPath;
	
	private int maxPickupItems;
	public int getMaxPickupItems(){return maxPickupItems;}
	
	private int maxPowerups;
	public int getMaxPowerups(){return maxPowerups;}
	
	private int width;
	public int getWidth(){return width;}
	
	private int height;
	public int getHeight(){return height;}
	
	public GameMapSharedConfig(String name, String tiledMapPath, String binaryMapPath, int width, int height, int maxPickupItems, int maxPowerups){
		this.name = name;
		this.tiledMapPath = tiledMapPath;
		this.binaryMapPath = binaryMapPath;
		this.width = width;
		this.height = height;
		this.maxPickupItems = maxPickupItems;
		this.maxPowerups = maxPowerups;
	}
	public String getName(){
		return name;
	}
	public String getBinaryMapPath(){
		return binaryMapPath;		
	}
	public String getTiledMapPath(){
		return tiledMapPath;
	}

}
