package net.megafoxhunt.shared;

public class GameMap {
	
	public static final GameMap DEBUG_MAP = new GameMap("Debug", "data/basic_map.tmx", "data/basic_map.txt", 35, 25 );	
	
	private String name;
	private String tiledMapPath;
	private String binaryMapPath;
	
	private int width;
	public int getWidth(){return width;}
	
	private int height;
	public int getHeight(){return height;}
	
	public GameMap(String name, String tiledMapPath, String binaryMapPath, int width, int height){
		this.name = name;
		this.tiledMapPath = tiledMapPath;
		this.binaryMapPath = binaryMapPath;
		this.width = width;
		this.height = height;
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
