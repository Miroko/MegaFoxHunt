package net.megafox.items;

import net.megafox.game.GameSimulation;
import net.megafoxhunt.server.PlayerConnection;

public abstract class Item {
	
	protected GameSimulation gameSimulation;
	
	private int itemType;
	
	public abstract boolean activate(int x, int y, PlayerConnection player);
	
	public Item(GameSimulation gameSimulation, int itemType) {
		this.gameSimulation = gameSimulation;
		this.itemType = itemType;
	}
	
	public int getItemType() {
		return itemType;
	}
	
	public void setItemType(int itemType) {
		this.itemType = itemType;
	}
}
