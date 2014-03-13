package net.megafoxhunt.core;


import net.megafoxhunt.entities.EntityMovable;
import net.megafoxhunt.ui.TouchJoystick;
import net.megafoxhunt.shared.KryoNetwork.ActivateItem;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter {
		
	private int last_direction = -1;
	private int previousKey;
	
	private GameNetwork network;
	private TouchJoystick touchJoystick;
	
	public GameInputProcessor(GameNetwork network, TouchJoystick touchJoystick) {
		this.touchJoystick = touchJoystick;
		this.network = network;
	}
	
	private void sendDirection(int direction){
		// IF DIRECTION HAS CHANGED
		if(direction != last_direction){
			EntityMovable entity = network.getLocalUser().getControlledEntity();
			if (entity == null) return;
    		entity.setDirection(direction);
    		last_direction = direction;
		}
	}
	
	public boolean keyDown(int k) {
		if (k == Keys.SPACE) return true;
		
		if(k == Keys.UP) {
			sendDirection(EntityMovable.DIRECTION_UP);
		}
		if(k == Keys.LEFT) {
			sendDirection(EntityMovable.DIRECTION_LEFT);
		}
		if(k == Keys.DOWN) {
			sendDirection(EntityMovable.DIRECTION_DOWN);
		}
		if(k == Keys.RIGHT) {
			sendDirection(EntityMovable.DIRECTION_RIGHT);
		}
		previousKey = k;
		return true;
	}
	
	public boolean keyUp(int k) {
		// KEY UP STOP		
		if(k == previousKey){
			sendDirection(EntityMovable.DIRECTION_STOP);
		}
		
		if (k == Keys.SPACE) {
			network.getKryoClient().sendTCP(new ActivateItem());
		}
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touchJoystick.mouseDown(screenX, screenY);
		
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touchJoystick.mouseUp(screenX, screenY);
		
		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		touchJoystick.mouseDown(screenX, screenY);
		
		return super.touchDragged(screenX, screenY, pointer);
	}
	
	
}
