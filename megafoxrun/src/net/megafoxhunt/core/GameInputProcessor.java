package net.megafoxhunt.core;


import net.megafoxhunt.entities.EntityMovable;


import net.megafoxhunt.ui.TouchJoystick;
import net.megafoxhunt.shared.KryoNetwork.ActivateItem;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class GameInputProcessor extends InputAdapter implements GestureListener {
		
	private int last_direction = -1;
	private int previousKey;
	
	private GameNetwork network;
	private TouchJoystick touchJoystick;
	
	private int numFingersOnScreen = 0;
	
	public GameInputProcessor(GameNetwork network, TouchJoystick touchJoystick) {
		this.touchJoystick = touchJoystick;
		this.network = network;
	}
	
	private void sendDirection(int direction){
		// IF DIRECTION HAS CHANGED
		EntityMovable entity = network.getLocalUser().getControlledEntity();
		if (entity == null) return;
		entity.setDirection(direction);
		last_direction = direction;
	}

	public boolean keyDown(int k) {
		if (k == Keys.SPACE || k == Keys.ENTER) return true;
		
		
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
			//sendDirection(EntityMovable.DIRECTION_STOP);
		}
		
		if (k == Keys.SPACE) {
			network.getKryoClient().sendTCP(new ActivateItem());
		}
		/*
		if(k == Keys.ENTER){
			network.getKryoClient().sendTCP(new GoInHole());
		}
		*/
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		numFingersOnScreen++;
		handleTouchInput(screenX, screenY);
		
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touchJoystick.mouseUp(screenX, screenY);
		
		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		handleTouchInput(screenX, screenY);
		
		return super.touchDragged(screenX, screenY, pointer);
	}
	
	private void handleTouchInput(int mouseX, int mouseY) {
		//int width = Gdx.graphics.getWidth();
		//int height = Gdx.graphics.getHeight();
		
		touchJoystick.mouseDown(mouseX, mouseY);
		
		/*
		int calculatedTopAndDownAreas = (int)(height * 0.35f);
		
		if (mouseY < calculatedTopAndDownAreas) sendDirection(EntityMovable.DIRECTION_UP);
		else if (mouseY > (height - calculatedTopAndDownAreas)) sendDirection(EntityMovable.DIRECTION_DOWN);
		else if (mouseX <= (width / 2)) sendDirection(EntityMovable.DIRECTION_LEFT);
		else if (mouseX > (width / 2)) sendDirection(EntityMovable.DIRECTION_RIGHT);*/
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		touchJoystick.tap(x, y);
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if(Math.abs(velocityX)>Math.abs(velocityY)) {
			if(velocityX>0) sendDirection(EntityMovable.DIRECTION_RIGHT);
			else sendDirection(EntityMovable.DIRECTION_LEFT);
		} else {
			if(velocityY>0) sendDirection(EntityMovable.DIRECTION_DOWN);
			else sendDirection(EntityMovable.DIRECTION_UP);
		}
		
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
}
