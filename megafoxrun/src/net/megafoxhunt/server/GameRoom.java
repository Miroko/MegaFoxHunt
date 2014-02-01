package net.megafoxhunt.server;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.megafoxhunt.server.KryoNetwork.AddPlayer;
import net.megafoxhunt.server.KryoNetwork.RemovePlayer;

public class GameRoom extends Thread {
	
	public enum RoomState {
		LOBBY, GAME
	};
	
	public static final int MAX_SIZE = 12;

	private static final long UPDATE_RATE_MS = 100;
	
	private ReentrantReadWriteLock playersLock;
	private ArrayList<PlayerConnection> players;
	
	private RoomState roomState;
	
	private boolean roomRunning = true;
	
	public GameRoom(){
		players = new ArrayList<PlayerConnection>(MAX_SIZE);
		this.roomState = RoomState.LOBBY;
		playersLock = new ReentrantReadWriteLock(true);
	}
	
	public void update(double delta){
		switch (roomState) {
		case GAME:
			
			break;
		case LOBBY:
			
			break;
		}
	}
	
	// Delta should stay near UPDATE_RATE_MS
	public void run(){
		long time_last_update = System.currentTimeMillis();
		long time_loop_start;
		long time_sleep;		
		while(roomRunning){
			time_loop_start = System.currentTimeMillis();
			update(time_loop_start - time_last_update);			
			time_sleep = UPDATE_RATE_MS - (System.currentTimeMillis() - time_loop_start);
			if(time_sleep > 0){
				try {
					Thread.sleep(time_sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
			time_last_update = time_loop_start;
		}
	}
	
	public boolean addPlayer(final PlayerConnection player) {
		boolean joinSuccessful = false;
		playersLock.writeLock().lock();
		if (players.size() < MAX_SIZE) {
			players.add(player);
			joinSuccessful = true;
		}
		playersLock.writeLock().unlock();
		
		
		if (joinSuccessful) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					AddPlayer addNewPlayer = new AddPlayer();
					addNewPlayer.id = player.getMyId();
					addNewPlayer.name = player.getName();
					
					playersLock.readLock().lock();
					for(PlayerConnection conn : players) {
						if (conn != player) {
							conn.sendTCP(addNewPlayer);
							
							AddPlayer addOldPlayer = new AddPlayer();
							addOldPlayer.id = conn.getMyId();
							addOldPlayer.name = conn.getName();
							player.sendTCP(addOldPlayer);
						}
					}
					playersLock.readLock().unlock();
				}
			}).start();
		}
		
		return joinSuccessful;
	}
	
	public void removePlayer(final PlayerConnection player) {
		boolean removeSuccessful = false;
		
		playersLock.writeLock().lock();
		removeSuccessful = players.remove(player);
		playersLock.writeLock().unlock();
		
		if (removeSuccessful) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					RemovePlayer removePlayer = new RemovePlayer();
					removePlayer.id = player.getMyId();
					
					playersLock.readLock().lock();
					for(PlayerConnection conn : players) {
						conn.sendTCP(removePlayer);
					}
					playersLock.readLock().unlock();
				}
			}).start();
		}
	}
}