package net.megafoxhunt.server;

import java.util.ArrayList;

import net.megafoxhunt.entities.StaticEntity;

public class KryoRoom extends Thread{
	
	public static final int SIZE = 12;
	
	private ArrayList<StaticEntity> entities;
	
	public KryoRoom(){
		entities = new ArrayList<StaticEntity>(12);
	}
}
