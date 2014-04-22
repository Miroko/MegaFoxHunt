package net.megafoxhunt.core;

import net.megafoxhunt.shared.GameMapSharedConfig;

public class MapHandler {
	
	public GameMapClientSide currentMap;

	public void switchMap(String name){	
		if(name.equals(GameMapSharedConfig.DEBUG_MAP.getName())){
			currentMap = new GameMapClientSide(GameMapSharedConfig.DEBUG_MAP);
		} else if (name.equals(GameMapSharedConfig.TEST_MAP.getName())) {
			currentMap = new GameMapClientSide(GameMapSharedConfig.TEST_MAP);
		} else if (name.equals(GameMapSharedConfig.MAP1.getName())) {
			currentMap = new GameMapClientSide(GameMapSharedConfig.MAP1);
		} else if (name.equals(GameMapSharedConfig.MAP2.getName())) {
			currentMap = new GameMapClientSide(GameMapSharedConfig.MAP2);
		}
	}	
	public void dispose() {
		if(currentMap != null){
			currentMap.dispose();
		}
		currentMap = null;
	}
	

}
