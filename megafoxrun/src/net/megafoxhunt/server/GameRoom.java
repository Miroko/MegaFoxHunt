package net.megafoxhunt.server;

import java.util.ArrayList;

import net.megafoxhunt.entities.StaticEntity;

public class GameRoom extends Thread{
	
	public static final int SIZE = 12;
	
	private static final int UPDATE_FPS = 30;	
	
	
	private ArrayList<StaticEntity> entities;
	
	public GameRoom(){
		entities = new ArrayList<StaticEntity>(12);
	}
	public void update(float delta){
		
	}
	public void run(){
		float delta = 0;
		float time1;		
		time1 = System.currentTimeMillis();
		while(true){
			time1 = System.currentTimeMillis();
			update(delta);
			delta = System.currentTimeMillis() - time1;			
		}
	}
}
