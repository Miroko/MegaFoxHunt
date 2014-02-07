package net.megafoxhunt.core;

import net.megafoxhunt.entities.AliveEntity;
import net.megafoxhunt.server.KryoNetwork.Move;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter {
		
	private int last_direction = -1;
	
	private void sendDirection(int direction){
		// IF DIRECTION HAS CHANGED
		if(direction != last_direction){
    		AliveEntity entity = GameNetwork.getUser().getControlledEntity();
    		entity.setDirection(direction);
    		// SEND MOVE COMMAND
    		Move move = new Move(entity.getId(), direction, (int)entity.getX(), (int)entity.getY());
    		GameNetwork.getClient().sendTCP(move);
    		last_direction = direction;
		}
	}
	
	public boolean keyDown(int k) {    	
		if(k == Keys.UP) {
			sendDirection(AliveEntity.DIRECTION_UP);
		}
		if(k == Keys.LEFT) {
			sendDirection(AliveEntity.DIRECTION_LEFT);
		}
		if(k == Keys.DOWN) {
			sendDirection(AliveEntity.DIRECTION_DOWN);
		}
		if(k == Keys.RIGHT) {
			sendDirection(AliveEntity.DIRECTION_RIGHT);
		}
		return true;
	}
	
	public boolean keyUp(int k) {
		// KEY UP STOP		
		sendDirection(AliveEntity.DIRECTION_STOP);
		return true;
	}
}
