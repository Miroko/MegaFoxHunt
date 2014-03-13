package net.megafox.items;

import net.megafox.game.GameSimulation;

public abstract class Item {
	
	protected GameSimulation gameSimulation;
	
	public Item(GameSimulation gameSimulation) {
		this.gameSimulation = gameSimulation;
	}
	
	public void activateItem(int x, int y) {
		gameSimulation.useItem(this, x, y);
	}
}
