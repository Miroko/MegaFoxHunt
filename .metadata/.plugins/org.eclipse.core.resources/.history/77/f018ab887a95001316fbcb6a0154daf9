package net.megafoxhunt.server;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.megafoxhunt.server.KryoNetwork.AddPlayer;
import net.megafoxhunt.server.KryoNetwork.RemovePlayer;

import com.esotericsoftware.kryo.Kryo;

public class ConcurrentPlayerContainer {

	private int maxSize;
	private int currentSize;
	private ArrayList<PlayerConnection> players;
	
	private ReentrantReadWriteLock lock;
	
	public ConcurrentPlayerContainer(int maxSize) { 
		this.maxSize = maxSize;
		this.currentSize = 0;
		this.players = new ArrayList<PlayerConnection>(maxSize);
		this.lock = new ReentrantReadWriteLock();
	}
	
	public boolean addPlayer(final PlayerConnection player) {
		if (player == null) return false;
		
		boolean addSuccessful = false;
		
		lock.writeLock().lock();
			if (currentSize < maxSize) {
				currentSize++;
				players.add(player);
				addSuccessful = true;
			}
		lock.writeLock().unlock();
		
		return addSuccessful;
	}
	
	public ArrayList<PlayerConnection> getPlayers() {
		lock.readLock().lock();
			ArrayList<PlayerConnection> copyArray = (ArrayList<PlayerConnection>)players.clone();
		lock.readLock().unlock();
		return copyArray;
	}
	
	public boolean removePlayer(final PlayerConnection player) {
		if (player == null) return false;

		lock.writeLock().lock();
			boolean removeSuccessful = players.remove(player);
			if (removeSuccessful) currentSize--;
		lock.writeLock().unlock();
			
		return removeSuccessful;
	}
	
	public void sendObjectToAll(final Object object) {
		ArrayList<PlayerConnection> copyPlayers = getPlayers();
		for (PlayerConnection player : copyPlayers) {
			player.sendTCP(object);
		}
	}
	
	public void sendObjectToAllExcept(final PlayerConnection ignoreMe, final Object object) {
		ArrayList<PlayerConnection> copyPlayers = getPlayers();
		for (PlayerConnection player : copyPlayers) {
			if (player != ignoreMe) {
				player.sendTCP(object);
			}
		}
	}
}
