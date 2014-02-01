package net.megafoxhunt.server;

import java.util.ArrayList;
import java.util.Random;

public class IDHandler {
	
	private Random random;
	private ArrayList<Integer> ids;
	
	public IDHandler(){
		random = new Random();
		ids = new ArrayList<Integer>();
	}	
	public int getFreeID(){
		int id = random.nextInt(Integer.MAX_VALUE);
		while(ids.contains(id)){
			id = random.nextInt(Integer.MAX_VALUE);
		}
		ids.add(id);
		return id;
	}
	public boolean isValidID(int id){
		return ids.contains((Integer)id);
	}
	public void freeID(int id){
		ids.remove((Integer)id);
	}
}
