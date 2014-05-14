package net.megafoxhunt.core;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameResources {

	public static final int DEFAULT_ANIMATION = 0;
	public static final int FRONT_ANIMATION = 1;
	public static final int BACK_ANIMATION = 2;
	
	public static final int FOURTH_ANIMATION = 4;
	public static final int FIFTH_ANIMATIN = 5;
	public static final int SIXTH_ANIMATION = 6;
	
	public Texture ITEMS_TEXTURE;
	public Texture CHARACTERS_TEXTURE;
	
	public Animation[] BERRY_ANIMATIONS = new Animation[10];
	public Animation[] BARRICADE_ANIMATIONS = new Animation[10];
	public Animation[] FOX_ANIMATIONS = new Animation[10];
	public Animation[] DOG_ANIMATIONS  = new Animation[10];
	public Animation[] POWERRUP_ANIMATIONS = new Animation[10];
	public Animation[] BOMB_ANIMATIONS = new Animation[10];
	public Animation[] PICKUP_ITEM_ANIMATIONS = new Animation[10];
	
	public Texture lobbyBackground;	
	public Texture menuBackground;
	
	public Texture namefieldTexture;
	
	public Texture connectButtonUpTexture;
	public Texture connectButtonDownTexture;
	
	public Texture quitButtonUpTexture;
	public Texture quitButtonDownTexture;
	
	public Texture preferDogButtonUpTexture;
	public Texture preferDogButtonDownTexture;
	
	public Texture preferFoxButtonUpTexture;
	public Texture preferFoxButtonDownTexture;
	
	public Texture readyButtonUpTexture;
	public Texture readyButtonDownTexture;

	public Texture resumeButtonUpTexture;
	public Texture resumeButtonDownTexture;
	
	public Texture newgameButtonUpTexture;
	public Texture newgameButtonDownTexture;
	
	public Texture btnNormal;
	public Texture btnPressed;
	
	public Texture circle;
	public Texture joystick;
	
	public Music MUSIC;	
	public Music LOSE_ANTHEM;
	public Music VICTORY_ANTHEM;	
	public Music ROUND_START;
	
	public Sound BERRY_EAT;	
	public Sound BOMB_EXPLOSION;
	public Sound BOMB_FUSE;
	public Sound BARRICADE_BUILD;
	public Sound PICKUP;	
	public Sound BUBBLE;
	public Sound MENU_BUTTON;
	public Sound CAUGHT;
	public Sound GOT_CAUGHT;
	
	public BitmapFont BASIC_FONT;
		
	public void init(){						
		 ITEMS_TEXTURE = new Texture("data/items.png");
		 ITEMS_TEXTURE.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		 
		 CHARACTERS_TEXTURE = new Texture("data/characters.png");
		 CHARACTERS_TEXTURE.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		 
		 BOMB_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(ITEMS_TEXTURE, 0.125f, 0, 312, 120, 120 , 2, 8);
		 BARRICADE_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(ITEMS_TEXTURE, 1f, 0, 552, 80, 80 , 1, 1);
		 BERRY_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(ITEMS_TEXTURE, 0.042f, 0, 0, 64, 64 , 2, 10);
		 PICKUP_ITEM_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(ITEMS_TEXTURE, 0.066f, 0, 128, 64, 64, 1, 12);
		 PICKUP_ITEM_ANIMATIONS[FOURTH_ANIMATION] = generateAnimation(ITEMS_TEXTURE, 0.066f, 0, 192, 120, 120, 1, 7);
		 POWERRUP_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(ITEMS_TEXTURE, 1f, 0, 632, 80, 80, 1, 1);
		 
		 FOX_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(CHARACTERS_TEXTURE, 0.042f, 0, 0, 104, 64 , 1, 10);
		 FOX_ANIMATIONS[FRONT_ANIMATION] = generateAnimation(CHARACTERS_TEXTURE, 0.042f, 0, 64, 64, 104 , 1, 10);
		 FOX_ANIMATIONS[BACK_ANIMATION] = generateAnimation(CHARACTERS_TEXTURE, 0.042f, 0, 168, 64, 104, 1, 10);
		 
		 FOX_ANIMATIONS[FOURTH_ANIMATION] = generateAnimation(CHARACTERS_TEXTURE, 0.042f, 0, 277, 104, 64 , 1, 10);
		 FOX_ANIMATIONS[FIFTH_ANIMATIN] = generateAnimation(CHARACTERS_TEXTURE, 0.042f, 0, 341, 64, 104 , 1, 10);
		 FOX_ANIMATIONS[SIXTH_ANIMATION] = generateAnimation(CHARACTERS_TEXTURE, 0.042f, 0, 445, 64, 104, 1, 10);
		 
		 DOG_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(CHARACTERS_TEXTURE, 0.042f, 0, 575, 187, 114, 1, 8);
		 DOG_ANIMATIONS[FRONT_ANIMATION] = generateAnimation(CHARACTERS_TEXTURE, 0.042f, 0, 839, 79, 175, 1, 8);
		 DOG_ANIMATIONS[BACK_ANIMATION] = generateAnimation(CHARACTERS_TEXTURE, 0.042f, 0, 689, 79, 150, 1, 8);
		 
		 lobbyBackground = new Texture("data/tausta_lobby2.png");
		 lobbyBackground.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		 
		 menuBackground = new Texture("data/tausta2.png");
		 menuBackground.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		 
		 namefieldTexture = new Texture("data/Player_tausta2.png");
		 
		 connectButtonUpTexture = new Texture("data/connect_nappula_up2.png"); 
		 connectButtonDownTexture = new Texture("data/connect_nappula_down2.png"); 
		 
		 quitButtonUpTexture = new Texture("data/quit_nappula_up2.png");
		 quitButtonDownTexture = new Texture("data/quit_nappula_down2.png");
		 
		 preferDogButtonUpTexture = new Texture("data/preferdog_nappula_up2.png");
		 preferDogButtonDownTexture = new Texture("data/preferdog_nappula_down2.png");		 
		 
		 preferFoxButtonUpTexture = new Texture("data/preferfox_nappula_up2.png");
		 preferFoxButtonDownTexture = new Texture("data/preferfox_nappula_down2.png");	
		 
		 readyButtonUpTexture = new Texture("data/ready_nappula_up2.png");
		 readyButtonDownTexture = new Texture("data/ready_nappula_down2.png");
		 
		 resumeButtonUpTexture = new Texture("data/resume_nappula_up2.png");
		 resumeButtonDownTexture = new Texture("data/resume_nappula_down2.png");
		 
		 newgameButtonUpTexture = new Texture("data/newgame_nappula_up2.png");
		 newgameButtonDownTexture = new Texture("data/newgame_nappula_down2.png");
		 
		 circle = new Texture(Gdx.files.internal("data/circle.png"));
		 circle.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		 
		 joystick = new Texture(Gdx.files.internal("data/joystick.png"));
		 joystick.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		 
		 btnNormal = new Texture("data/btn_normal.png");
		 btnPressed = new Texture("data/btn_pressed.png");
		 
		 MUSIC = Gdx.audio.newMusic(Gdx.files.internal("data/audio/music.mp3"));
		 
		 BERRY_EAT = Gdx.audio.newSound(Gdx.files.internal("data/audio/berry_eat.mp3"));
		 PICKUP = Gdx.audio.newSound(Gdx.files.internal("data/audio/berry_eat.mp3"));
		 
		 BOMB_EXPLOSION = Gdx.audio.newSound(Gdx.files.internal("data/audio/bomb_explosion.mp3"));
		 BOMB_FUSE = Gdx.audio.newSound(Gdx.files.internal("data/audio/bomb_fuse.mp3"));
		 
		 BARRICADE_BUILD = Gdx.audio.newSound(Gdx.files.internal("data/audio/bomb_explosion.mp3"));
		 
		 LOSE_ANTHEM = Gdx.audio.newMusic(Gdx.files.internal("data/audio/music.mp3"));
		 VICTORY_ANTHEM = Gdx.audio.newMusic(Gdx.files.internal("data/audio/music.mp3"));		
		 
		 ROUND_START = Gdx.audio.newMusic(Gdx.files.internal("data/audio/music.mp3"));		
		 
		 MENU_BUTTON = Gdx.audio.newSound(Gdx.files.internal("data/audio/bomb_explosion.mp3"));	
		 
		 CAUGHT = Gdx.audio.newSound(Gdx.files.internal("data/audio/bomb_explosion.mp3"));	
		 GOT_CAUGHT = Gdx.audio.newSound(Gdx.files.internal("data/audio/bomb_explosion.mp3"));
		 
		 BUBBLE = Gdx.audio.newSound(Gdx.files.internal("data/audio/bubble.mp3"));
	
		 BASIC_FONT = new BitmapFont();
	}
	
	private Animation generateAnimation(Texture texture, float frameDuration, int startX, int startY, int width, int height, int rows, int cols) {
		Animation animation = null;
		TextureRegion[] frames = new TextureRegion[rows * cols];
		
		int framesIndex = 0;
		
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				frames[framesIndex] = new TextureRegion(texture, x * width + startX, y * height + startY, width, height);
				framesIndex++;
			}
		}
		
		animation = new Animation(frameDuration, frames);
        return animation;
	}
	
	private Animation generateAnimation(Texture texture, float frameDuration, int cols, int rows){
		Animation animation = null;
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / cols, texture.getHeight() / rows); 
        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                        frames[index++] = tmp[i][j];
                }
        }
        animation = new Animation(frameDuration, frames);
        return animation;
	}
	
	public void dispose(){
		MUSIC.dispose();
		BERRY_EAT.dispose();
		BOMB_EXPLOSION.dispose();		
		LOSE_ANTHEM.dispose();
		VICTORY_ANTHEM.dispose();	
		ROUND_START.dispose();		
		BOMB_EXPLOSION.dispose();
		BOMB_FUSE.dispose();
		BARRICADE_BUILD.dispose();
		PICKUP.dispose();	
		MENU_BUTTON.dispose();
		CAUGHT.dispose();
		GOT_CAUGHT.dispose();
		BUBBLE.dispose();

		namefieldTexture.dispose();
		
		connectButtonUpTexture.dispose();
		connectButtonDownTexture.dispose();

		quitButtonUpTexture.dispose();
		quitButtonDownTexture.dispose();
		
		preferDogButtonUpTexture.dispose();
		preferDogButtonDownTexture.dispose();
		
		preferFoxButtonUpTexture.dispose();
		preferFoxButtonDownTexture.dispose();		
		
		readyButtonUpTexture.dispose();
		readyButtonDownTexture.dispose();
		
		circle.dispose();
		joystick.dispose();
		
		btnNormal.dispose();
		btnPressed.dispose();
		
		BASIC_FONT.dispose();
	}
	
	
}
