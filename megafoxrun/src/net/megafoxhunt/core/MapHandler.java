package net.megafoxhunt.core;

import net.megafoxhunt.shared.GameMapSharedConfig;

public class MapHandler {
	
	public GameMapClientSide currentMap;

	public void switchMap(String name){	
		if(name.equals(GameMapSharedConfig.DEBUG_MAP.getName())){
			currentMap = new GameMapClientSide(GameMapSharedConfig.DEBUG_MAP);
		}	
	}	
	public void dispose() {
		currentMap.dispose();
	}
	

}
