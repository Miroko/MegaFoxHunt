package net.megafox.items;

import net.megafox.game.GameSimulation;
import net.megafoxhunt.server.PlayerConnection;

public abstract class Item {
	
	protected GameSimulation gameSimulation;
	
	public Item(GameSimulation gameSimulation) {
		this.gameSimulation = gameSimulation;
	}
	public void activateItem(int x, int y, PlayerConnection player) {
		gameSimulation.useItem(this, x, y, player);
	}
	
}
