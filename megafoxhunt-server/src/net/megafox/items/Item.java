package net.megafox.items;

import net.megafox.game.GameSimulation;
import net.megafoxhunt.server.PlayerConnection;

public abstract class Item {
	
	protected GameSimulation gameSimulation;
	
	public abstract boolean activate(int x, int y, PlayerConnection player);
	
	public Item(GameSimulation gameSimulation) {
		this.gameSimulation = gameSimulation;
	}
	
}
