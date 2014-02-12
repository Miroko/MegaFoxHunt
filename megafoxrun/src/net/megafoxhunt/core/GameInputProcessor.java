package net.megafoxhunt.core;


import net.megafoxhunt.entities.Entity;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter {
		
	private int last_direction = -1;
	private int previousKey;
	
	private void sendDirection(int direction){
		// IF DIRECTION HAS CHANGED
		if(direction != last_direction){
			Entity entity = GameNetwork.getUser().getControlledEntity();
    		entity.setDirection(direction);
    		last_direction = direction;
		}
	}
	
	public boolean keyDown(int k) {    	
		if(k == Keys.UP) {
			sendDirection(Entity.DIRECTION_UP);
		}
		if(k == Keys.LEFT) {
			sendDirection(Entity.DIRECTION_LEFT);
		}
		if(k == Keys.DOWN) {
			sendDirection(Entity.DIRECTION_DOWN);
		}
		if(k == Keys.RIGHT) {
			sendDirection(Entity.DIRECTION_RIGHT);
		}
		previousKey = k;
		return true;
	}
	
	public boolean keyUp(int k) {
		// KEY UP STOP		
		if(k == previousKey){
			sendDirection(Entity.DIRECTION_STOP);
		}
		return true;
	}
}
