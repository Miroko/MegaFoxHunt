package net.megafox.gameroom;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.megafoxhunt.server.PlayerConnection;

public class PlayerContainer {
	
	public ReentrantLock lock;

	private ArrayList<PlayerConnection> players;
	
	public PlayerContainer(int size) { 
		this.players = new ArrayList<PlayerConnection>(size);
		this.lock = new ReentrantLock(true);
	}	
	public void addPlayer(PlayerConnection player) {		
		lock.lock();
		players.add(player);
		lock.unlock();
	}	
	@SuppressWarnings("unchecked")
	public ArrayList<PlayerConnection> getPlayersConcurrentSafe() {		
		return (ArrayList<PlayerConnection>)players.clone();
	}	
	public void removePlayer(PlayerConnection player) {
		lock.lock();
		players.remove(player);
		lock.unlock();
	}	
	public void sendObjectToAll(Object object) {
		for (PlayerConnection player : getPlayersConcurrentSafe()) {
			player.sendTCP(object);
		}
	}	
	public void sendObjectToAllExcept(PlayerConnection ignoreMe, Object object) {
		for (PlayerConnection player : getPlayersConcurrentSafe()) {
			if (player != ignoreMe) {
				player.sendTCP(object);
			}
		}
	}
}
