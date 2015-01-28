package com.e005.DaysGoneBy;


import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;


public class DaysGoneBy implements ApplicationListener, InputProcessor, GestureListener {
	private double version = 0.66;
	
	public static final int MAX_BULLETS = 20;
	public static final int MAX_ENEMIES = 30;
	public static final int MAX_MOVESPEED = 3;
	public static final int MAX_PISTOL_CLIP = 15;
	public static final int MAX_EXPLOSION = 5;
	public static final int MAX_DEAD = 50;
	
	private boolean firstIn;
	
    private SpriteBatch batch;
    private BitmapFont font,font2,font3;

    
    private int maxEnemy;
    private int drawSize;
    private boolean drawButton;
    
    private int width,height;
    
    
    private Texture arrowTexture,sideArrowTexture;
    private Texture bulletTexture;
    private Texture[] enemyTexture = new Texture[4];
    private Texture healthBarTexture;
    private Texture healthbarFull;
    private Texture menuButtonTexture;
    private Texture[] deadEnemyTexture = new Texture[4];
  
    private Texture ammoBoxTexture;
    
    private Texture[] explosionTexture = new Texture[3];
    private Texture[] menuWritingTexture = new Texture[5];
    
    private Texture thumbStickBaseTexture;
    private Texture thumbStickTopTexture;
    
    private Texture messageBoxTexture;
    private Texture backButtonTexture;
    private Texture reloadButtonTexture;
    
    private Texture[] playerTextures = new Texture[5];
    private Texture[] settingMenu = new Texture[11];
    private Texture checkBoxTexture;
    
    private Sprite[] checkBoxSprite = new Sprite [3];
    private Sprite[] settingMenuSprite = new Sprite[11];
	private Sprite[] playerSprite = new Sprite[5];
	private Sprite[] menuWritingSprite = new Sprite[5];
    private Sprite[] menuSprite = new Sprite[4];
    
    private Sprite ammoBox;
    
    private Sprite[] bulletSprite = new Sprite[MAX_BULLETS];
    private Sprite[] arrowSprite = new Sprite[4];
    private Sprite[] enemySprite = new Sprite[MAX_ENEMIES];
    private Sprite[][] deadEnemySprite = new Sprite[MAX_DEAD][4];
    private Sprite[][] explosionSprite = new Sprite[MAX_EXPLOSION][3];
    private Sprite thumbStickTop;
    private Sprite backButtonSprite;
    private Sprite reloadButtonSprite;
   
    
    private Sprite healthBarSprite;
    private Sprite healthBarFSprite;

    private Player player = new Player();
    
    private Enemy[] enemy = new Enemy[MAX_ENEMIES];
    
    private Bullet[] bullets = new Bullet[MAX_BULLETS];
    
    private Explosion[] explosion = new Explosion[MAX_EXPLOSION];
    
    private GameMap gameMap;
    
    private int currentShotCount = 0;
    private int currentShotCoolDown = 0;
    private int currentReloadCoolDown = 0;
    private int kills = 0;
    
    private boolean[][] deadDraw = new boolean[MAX_DEAD][4];
    private int deadDrawCount = 0;
    
    private boolean reload = false;
    private boolean halfWayReload = false;
    private int halfWayWeapon = 0;
    
    
    private long halfWayRResetS;
    private long halfWayWResetS;
    private int drawChoice,buttonChoice,enemyChoice = 0;
     
    
    private ThumbStick thumbRight = new ThumbStick();
    
    
    private int currentClip = MAX_PISTOL_CLIP;
    private int[] weaponClip = new int [4];
    private int menuChoice = 0;
    private boolean start = true;
    private int moveSpeed;
  
    
    private GroundItem ammoBoxItem;
    
    class TouchInfo{
    	public float touchX = 0;
    	public float touchY = 0;
    	public float startX,startY = 0;
    	public float disX,disY;
    	public boolean touched = false;
    	public boolean dragged = false;
    	public long touchedStart;
    	public float getDisX(){
    		disX = touchX - startX;
    		return disX;
    	}
    	public float getDisY(){
    		disY = touchY - startY;
    		return disY;
    	}
    	public float getDis(){
    		float dis = (float) Math.sqrt(Math.pow(getDisX(), 2)+Math.pow(getDisY(), 2));
    		return dis;
    	}
    	
    	public long touchTime(){
    		return (System.nanoTime() - touchedStart);
    	}
    	public int purpose = 0;
    	//CODE, 0 = nothing/default, 1 = control1, 2 = control2, 3 = select, 4 =swipeWeapon, 5 = swipeReload, 6 = machineFire, 7 = rbutton, 8 = leftWB, 9 = rightWB
    	
    }
    
    class MessageBox{
    	public float x,y = 0;
    	public float width, height = 0;
    	public String message = null;
    	public long displayTime = 0;
    	public long startTime = 0;
    	public Sprite sprite;
    	
    	public void setMessage(long display, String message){
    		displayTime = display;
    		startTime = System.nanoTime();
    		this.message = message;
    	}
    	public void reset(){
    		message = null;
    		displayTime = 0;
    		startTime = 0;
    	}
    	
    }
    private MessageBox messageBox = new MessageBox();
    
    private Map<Integer,TouchInfo> touches = new HashMap<Integer,TouchInfo>();
    //private boolean availableA; 
    /*
    private float accelX;
    private float accelY;
    private float accelZ;
    */
    private Preferences preferences;
    /*
    protected Preferences getPrefs() {
       if(preferences==null){
          preferences = Gdx.app.getPreferences(PREFS_NAME);
       }
       return preferences;
    }
    */
    
    public int getHighScore(){
    	return preferences.getInteger("HighScore", 0);
    }
    public void setHighScore(int score){
    	int oldHigh = getHighScore();
    	if(oldHigh < score){
    		preferences.putInteger("HighScore", score);
    	}
    	preferences.flush();
    }
    public void setSettings(int drawSize, boolean buttonDraw, int maxEnemy){
    	preferences.putInteger("DrawSize", drawSize);
    	preferences.putBoolean("ButtonDraw", buttonDraw);
    	preferences.putInteger("MaxEnemy", maxEnemy);
    	preferences.flush();
    }
    public int getDrawSize(){
    	return preferences.getInteger("DrawSize",1);
    }
    public int getMaxEnemy(){
    	return preferences.getInteger("MaxEnemy",10);
    }
    public boolean getButtonDraw(){
    	return preferences.getBoolean("ButtonDraw",true);
    }
    
    @Override
    public void create() {        
    	preferences = Gdx.app.getPreferences("HighScore");
    	width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        font = new BitmapFont();
        font2 = new BitmapFont();
        font3 = new BitmapFont();
        batch = new SpriteBatch();    
        
        setHighScore(0);
        
        font.setColor(Color.RED);
        font2.setColor(Color.RED);
        font3.setColor(Color.RED);
        font2.scale((float) 1.5);
        font3.scale(2);
        maxEnemy = getMaxEnemy();
        drawButton = getButtonDraw();
        drawSize = getDrawSize();
        gameMap = new GameMap(new Texture(Gdx.files.internal("Map1.png")));
        playerTextures[4] = new Texture(Gdx.files.internal("Player.png"));
        playerTextures[0] = new Texture(Gdx.files.internal("PlayerPistol.png"));
        playerTextures[1] = new Texture(Gdx.files.internal("PlayerMachine.png"));
        playerTextures[2] = new Texture(Gdx.files.internal("PlayerShotgun.png"));
        playerTextures[3] = new Texture (Gdx.files.internal("PlayerRocket.png"));
        
        enemyTexture[0] = new Texture(Gdx.files.internal("Zombie.png"));
        deadEnemyTexture[0] = new Texture(Gdx.files.internal("ZombieDead.png"));
        enemyTexture[1] = new Texture(Gdx.files.internal("Zombie2.png"));
        deadEnemyTexture[1] = new Texture(Gdx.files.internal("ZombieDead2.png"));
        enemyTexture[2] = new Texture(Gdx.files.internal("Zombie3.png"));
        deadEnemyTexture[2] = new Texture(Gdx.files.internal("ZombieDead3.png"));
        enemyTexture[3] = new Texture(Gdx.files.internal("Zombie4.png"));
        deadEnemyTexture[3] = new Texture(Gdx.files.internal("ZombieDead4.png"));
        arrowTexture = new Texture(Gdx.files.internal("Arrow.png"));
        sideArrowTexture = new Texture(Gdx.files.internal("Arrow2.png"));
        thumbStickBaseTexture = new Texture(Gdx.files.internal("ThumbStickBase.png"));
        thumbStickTopTexture = new Texture(Gdx.files.internal("ThumbStickTop.png"));
        thumbStickTop = new Sprite(thumbStickTopTexture);
        bulletTexture = new Texture(Gdx.files.internal("Bullet.png"));
        for(int i = 0; i < MAX_BULLETS; i++){
        	bulletSprite[i] = new Sprite(bulletTexture);
        }
        for(int i = 0; i < MAX_DEAD; i++){
        	for(int d = 0; d < 4; d++){
	        	deadEnemySprite[i][d] = new Sprite(deadEnemyTexture[d]);
	        	deadDraw[i][d] = false;
        	}
        }
        
        messageBoxTexture = new Texture(Gdx.files.internal("MessageBox.png"));
        menuButtonTexture = new Texture(Gdx.files.internal("menuButton.png"));
        menuWritingTexture[0] = new Texture(Gdx.files.internal("playbutton.png"));
        menuWritingTexture[1] = new Texture(Gdx.files.internal("scoresbutton.png"));
        menuWritingTexture[2] = new Texture(Gdx.files.internal("tutorialbutton.png"));
        menuWritingTexture[3] = new Texture(Gdx.files.internal("settingsbutton.png"));
        menuWritingTexture[4] = new Texture(Gdx.files.internal("titleScreenText.png"));
        backButtonTexture = new Texture(Gdx.files.internal("backButton.png"));
        reloadButtonTexture = new Texture(Gdx.files.internal("ReloadB.png"));
        
        ammoBoxTexture = new Texture(Gdx.files.internal("ammoBox.png"));
        
        settingMenu[0] = new Texture(Gdx.files.internal("maxEnemy.png"));
        settingMenu[1] = new Texture(Gdx.files.internal("10.png"));
        settingMenu[2] = new Texture(Gdx.files.internal("20.png"));
        settingMenu[3] = new Texture(Gdx.files.internal("30.png"));
       
        settingMenu[4] = new Texture(Gdx.files.internal("drawSize.png"));
        settingMenu[5] = new Texture(Gdx.files.internal("small.png"));
        settingMenu[6] = new Texture(Gdx.files.internal("medium.png"));
        settingMenu[7] = new Texture(Gdx.files.internal("large.png"));

        settingMenu[8] = new Texture(Gdx.files.internal("drawButton.png"));
        settingMenu[9] = new Texture(Gdx.files.internal("on.png"));
        settingMenu[10] = new Texture(Gdx.files.internal("off.png"));
        
        checkBoxTexture = new Texture(Gdx.files.internal("checkBox.png"));
        
        explosionTexture[0] = new Texture(Gdx.files.internal("explo0.png"));
        explosionTexture[1] = new Texture(Gdx.files.internal("explo1.png"));
        explosionTexture[2] = new Texture(Gdx.files.internal("explo2.png"));
        for(int i = 0; i < MAX_EXPLOSION; i++){
	        explosionSprite[i][0] = new Sprite(explosionTexture[0]);
	        explosionSprite[i][1] = new Sprite(explosionTexture[1]);
	        explosionSprite[i][2] = new Sprite(explosionTexture[2]);
        }
        for(int i = 0; i < 4; i++){
        	menuSprite[i] = new Sprite(menuButtonTexture);
        }
        for(int i = 0; i < 5; i++){
        	menuWritingSprite[i] = new Sprite(menuWritingTexture[i]);
        }
        menuSprite[0].setBounds(0, height - height/3, width/3, height/3);
        menuWritingSprite[0].setBounds(0, height - height/3, width/3, height/3);
        menuSprite[1].setBounds(0, 0, width/3, height/3);
        menuWritingSprite[1].setBounds(0, 0, width/3, height/3);
        menuSprite[2].setBounds(width -width/3, height - height/3, width/3, height/3);
        menuWritingSprite[2].setBounds(width -width/3, height - height/3, width/3, height/3);
        menuSprite[3].setBounds(width - width/3, 0, width/3, height/3);
        menuWritingSprite[3].setBounds(width - width/3, 0, width/3, height/3);
        menuWritingSprite[4].setBounds(0, height/3, width, height/3);
        
        ammoBox = new Sprite(ammoBoxTexture);
        
        backButtonSprite = new Sprite(backButtonTexture);
        reloadButtonSprite = new Sprite(reloadButtonTexture);
        healthBarTexture = new Texture(Gdx.files.internal("healthBarBack.png"));
        healthbarFull = new Texture(Gdx.files.internal("HealthFull.png"));
        
        healthBarSprite = new Sprite(healthBarTexture);
        healthBarFSprite = new Sprite(healthbarFull);
        
     
        healthBarSprite.setX(10);
        healthBarSprite.setY(height - 50);
        healthBarFSprite.setBounds(15,height - 45, Math.round(1.91*player.getHP()),height-38); //hardcoded numbers
        
        player.setCenter(width/2,height/2);
        
        for(int i= 0; i <5; i++){
        	playerSprite[i] = new Sprite(playerTextures[i]);
        }
        
        for(int i = 0; i < 3; i++){
        	checkBoxSprite[i] = new Sprite(checkBoxTexture);
        }
        
        //Setting Menu
        for(int i = 0; i < 11; i++){
        	settingMenuSprite[i] = new Sprite(settingMenu[i]);
        }
      
      
     
        //UpArrow
        arrowSprite[0] = new Sprite(arrowTexture);
        arrowSprite[0].setPosition(width/10,height/3 - height/20);
        //DownArrow
        arrowSprite[1] = new Sprite(arrowTexture);
        arrowSprite[1].flip(true, true);
        arrowSprite[1].setPosition(width/10,height/8 - height/10);
        //RightArrow
        arrowSprite[2] = new Sprite(sideArrowTexture);
        arrowSprite[2].setBounds(5*width/16,0, width/8, width/8);
        //LeftArrow
        arrowSprite[3] = new Sprite(sideArrowTexture);
        arrowSprite[3].flip(true, true);
        arrowSprite[3].setBounds(width/16,0, width/8, width/8);
        
        //Reload
        reloadButtonSprite.setBounds(3*width/16,0,width/8,width/8);
        //ThumbStick

        
        thumbRight.setCenter(width - width/5, height/4);
        thumbRight.setSpriteThumb(new Sprite(thumbStickTopTexture),width);
        thumbRight.setSpriteBase(new Sprite(thumbStickBaseTexture),width);
     
        thumbStickTop.setCenter(width - width/6, height/4);
        
        messageBox.sprite = new Sprite(messageBoxTexture);
        messageBox.sprite.setCenter(width/2, height/5);
        messageBox.x = width/6;
        messageBox.y = height/3;
        
 
        
        //Game Variables
        

        /*
        InputMultiplexer im = new InputMultiplexer();
        GestureDetector gd = new GestureDetector(this);
        im.addProcessor(gd);
        im.addProcessor(this);
        */
        
        
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
        //availableA = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
        for(int i = 0; i < 5; i++){
        	touches.put(i,  new TouchInfo());
        }
    }
    public void setTextures(int size){
    	currentShotCount = 0;
        
		for(int i = 0; i < MAX_BULLETS; i++){
        	bullets[i] = new Bullet();
        }
        for(int i = 0; i < MAX_EXPLOSION; i++){
        	explosion[i] = new Explosion();
        }
        for(int i = 0; i < MAX_ENEMIES; i++){
        	int random = (int) (Math.random()*4 + 1);
        	enemy[i] = new Enemy(random);
        	enemySprite[i] = new Sprite(enemyTexture[random]);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        font2.dispose();
        font3.dispose();
        bulletTexture.dispose();
        arrowTexture.dispose();
        sideArrowTexture.dispose();
        menuButtonTexture.dispose();
        for(int i = 0; i < 4; i++){
        	menuWritingTexture[i].dispose();
        }
        for(int i = 0; i < 4; i++){
        	explosionTexture[i].dispose();
        }
        thumbStickBaseTexture.dispose();
        thumbStickTopTexture.dispose();
        enemyTexture[0].dispose();
        
        healthBarTexture.dispose();
        healthbarFull.dispose();
        
    }

    @Override
    public void render() {  
    	//MENU
    	
    	batch.begin();
    	batch.enableBlending();
    	firstIn = false;
    	if(menuChoice == 0){
	    	Gdx.gl.glClearColor(0, 0, 0, 0);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	        menuSprite[0].setBounds(0, height - height/3, width/3, height/3);
	        menuWritingSprite[0].setBounds(0, height - height/3, width/3, height/3);
	        menuSprite[1].setBounds(0, 0, width/3, height/3);
	        menuWritingSprite[1].setBounds(0, 0, width/3, height/3);
	        menuSprite[2].setBounds(width -width/3, height - height/3, width/3, height/3);
	        menuWritingSprite[2].setBounds(width -width/3, height - height/3, width/3, height/3);
	        menuSprite[3].setBounds(width - width/3, 0, width/3, height/3);
	        menuWritingSprite[3].setBounds(width - width/3, 0, width/3, height/3);
	        menuWritingSprite[4].setBounds(0, height/3, width, height/3);
		        for(int i = 0; i <4; i++){
		        	menuSprite[i].draw(batch);
		        	
		        }
		        for(int i = 0; i <5; i++){
		        	menuWritingSprite[i].draw(batch);
		        }
		    	for(int i = 0 ; i < 5; i ++){
		    		if(touches.get(i).touched){
		    				if(inSquare(menuSprite[0].getX(), menuSprite[0].getWidth(),menuSprite[0].getY(),menuSprite[0].getHeight(), touches.get(i).touchX,touches.get(i).touchY)){
		    					menuChoice =  1;
		    					
		    				}
		    				if(inSquare(menuSprite[2].getX(), menuSprite[2].getWidth(),menuSprite[2].getY(),menuSprite[2].getHeight(), touches.get(i).touchX,touches.get(i).touchY)){
		    					menuChoice =  2;
		    					
		    				}
		    				if(inSquare(menuSprite[3].getX(), menuSprite[3].getWidth(),menuSprite[3].getY(),menuSprite[3].getHeight(), touches.get(i).touchX,touches.get(i).touchY)){
		    					menuChoice =  3;
		    					
		    				}
		    				if(inSquare(menuSprite[1].getX(), menuSprite[1].getWidth(),menuSprite[1].getY(),menuSprite[1].getHeight(), touches.get(i).touchX,touches.get(i).touchY)){
		    					menuChoice =  4;
		    					
		    				}
		    		}
		    		
		    	}
		    	if(menuChoice != 0){
			    	start = true;
			    	firstIn = true;
			    	for(int i = 0 ; i < 5; i ++){
			    		touches.get(i).touched = false;
			    	}
		    	}
    	}
    	if(menuChoice == 2){
    		Gdx.gl.glClearColor(0, 0, 0, 0);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	        if(drawSize == 1){
	        	font.draw(batch, "Movement - Use ThumbStick provided in bottom right Corner", 0, 3*height/4);
	        	font.draw(batch, "Shoot - Tap Point on Screen that is not a button", 0, 3*height/4 - font.getLineHeight()*2);
	        	font.draw(batch, "Reload - Press R Button", 0, 3*height/4 - font.getLineHeight()*4);
	        	font.draw(batch, "OR - Use quick Swipe down followed by Swipe Off ", 0, 3*height/4 - font.getLineHeight()*5);
	        	font.draw(batch, "Change Weapon - Either arrow Key", 0, 3*height/4 - font.getLineHeight()*6);
	        	font.draw(batch, "OR - Use 2 quick swipes in the same direction ", 0, 3*height/4 - font.getLineHeight()*7);
	        	font.draw(batch, "Gesture Note, You must remove your finger from the screen", 0, 3*height/4 - font.getLineHeight()*8);
	        	font.draw(batch, " before you start the second swipe!", 0, 3*height/4 - font.getLineHeight()*9);
	        }
	        else if(drawSize == 2){
	        	font.draw(batch, "Movement - Use ThumbStick provided in bottom right Corner", 0, 3*height/4);
	        	font.draw(batch, "Shoot - Tap Point on Screen that is not a button", 0, 3*height/4 - font.getLineHeight()*2);
	        	font.draw(batch, "Reload - Press R Button", 0, 3*height/4 - font.getLineHeight()*4);
	        	font.draw(batch, "OR - Use quick Swipe down followed by Swipe Off ", 0, 3*height/4 - font.getLineHeight()*5);
	        	font.draw(batch, "Change Weapon - Either arrow Key", 0, 3*height/4 - font.getLineHeight()*6);
	        	font.draw(batch, "OR - Use 2 quick swipes in the same direction ", 0, 3*height/4 - font.getLineHeight()*7);
	        	font.draw(batch, "Gesture Note, You must remove your finger from the screen", 0, 3*height/4 - font.getLineHeight()*8);
	        	font.draw(batch, " before you start the second swipe!", 0, 3*height/4 - font.getLineHeight()*9);
	        }
	        else{
	        	font.draw(batch, "Movement - Use ThumbStick provided in bottom right Corner", 0, 3*height/4);
	        	font.draw(batch, "Shoot - Tap Point on Screen that is not a button", 0, 3*height/4 - font.getLineHeight()*2);
	        	font.draw(batch, "Reload - Press R Button", 0, 3*height/4 - font.getLineHeight()*4);
	        	font.draw(batch, "OR - Use quick Swipe down followed by Swipe Off ", 0, 3*height/4 - font.getLineHeight()*5);
	        	font.draw(batch, "Change Weapon - Either arrow Key", 0, 3*height/4 - font.getLineHeight()*6);
	        	font.draw(batch, "OR - Use 2 quick swipes in the same direction ", 0, 3*height/4 - font.getLineHeight()*7);
	        	font.draw(batch, "Gesture Note, You must remove your finger from the screen", 0, 3*height/4 - font.getLineHeight()*8);
	        	font.draw(batch, " before you start the second swipe!", 0, 3*height/4 - font.getLineHeight()*9);
	        }
	        backButtonSprite.setBounds(4*width/5,0,width/5,height/4);
	        menuWritingSprite[2].setBounds(width/3,3*height/4, width/3, height/3);
	        menuSprite[2].setBounds(width/3,13*height/16, width/3, height/3);
	        
	        backButtonSprite.draw(batch);
	        menuSprite[2].draw(batch);
	        menuWritingSprite[2].draw(batch);
	        for(int i = 0 ; i < 5; i ++){
	    		if(touches.get(i).touched){
			        if(inSquare(backButtonSprite.getX(), width/5,backButtonSprite.getY(),height/4, touches.get(i).touchX,touches.get(i).touchY)){
						menuChoice = 0;
						touches.get(i).touched = false;
			        }
	    		}
	        }
    	}
    	if(menuChoice == 3){
    		Gdx.gl.glClearColor(0, 0, 0, 0);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	        
	        if(firstIn){
		        if(drawSize == 2){
		        	drawChoice = 6;
		        }
		        else if(drawSize == 1){
		        	drawChoice = 5;
		        }
		        else{
		        	drawChoice = 7;
		        }
		        
		        if(maxEnemy == 10){
		        	enemyChoice = 1;
		        }
		        else if(maxEnemy == 30){
		        	enemyChoice = 3;
		        }
		        else{
		        	enemyChoice = 2;
		        }
		        
		        if(drawButton){
		        	buttonChoice = 9;
		        }
		        else{
		        	buttonChoice = 10;
		        }
	        }
	        
	        
	        settingMenuSprite[0].setBounds(0,4*height/7, width/4, height/5);
	        settingMenuSprite[1].setBounds(width/3,4*height/7, width/8, height/5);
	        settingMenuSprite[2].setBounds((3*width/4 - width/3)/2 + width/3,4*height/7, width/8, height/5);
	        settingMenuSprite[3].setBounds(3*width/4,4*height/7, width/8, height/5);
	        checkBoxSprite[0].setBounds(settingMenuSprite[enemyChoice].getX(),settingMenuSprite[enemyChoice].getY(),width/35,width/35);
	        
	        settingMenuSprite[4].setBounds(0,5*height/16, width/4, height/5);
	        settingMenuSprite[5].setBounds(width/3,5*height/16, width/6, height/5);
	        
	        settingMenuSprite[6].setBounds((3*width/4 - width/3)/2 + width/3,5*height/16, width/6, height/5);
	        settingMenuSprite[7].setBounds(3*width/4,5*height/16, 6*height/16, height/5);
	        
	        checkBoxSprite[1].setBounds(settingMenuSprite[drawChoice].getX(),settingMenuSprite[drawChoice].getY(),width/35,width/35);
	        
	        settingMenuSprite[8].setBounds(0,0, width/4, height/5);
	        settingMenuSprite[9].setBounds(width/3,0, width/5, height/5);
	        settingMenuSprite[10].setBounds(14*width/24,0, width/5, height/5);
	        checkBoxSprite[2].setBounds(settingMenuSprite[buttonChoice].getX(),settingMenuSprite[buttonChoice].getY(),width/35,width/35);
	        
	        backButtonSprite.setBounds(4*width/5,0,width/5,height/4);
	        menuWritingSprite[3].setBounds(width/3,3*height/4, width/3, height/3);
	        menuSprite[3].setBounds(width/3,13*height/16, width/3, height/3);
	        for(int i = 0; i < 11; i++){
	        	settingMenuSprite[i].draw(batch);
	        }
	        for(int i=0; i < 3; i++){
	        	checkBoxSprite[i].draw(batch);
	        }
	        backButtonSprite.draw(batch);
	        menuSprite[3].draw(batch);
	        menuWritingSprite[3].draw(batch);
	        
	        for(int i = 0 ; i < 5; i ++){
	    		if(touches.get(i).touched){
	    				if(inSquare(backButtonSprite.getX(), width/5,backButtonSprite.getY(),height/4, touches.get(i).touchX,touches.get(i).touchY)){
	    					menuChoice = 0;
	    					touches.get(i).touched = false;
	    					
	    					if(drawChoice == 6){
	    			        	drawSize = 2;
	    			        }
	    			        else if(drawChoice == 5){
	    			        	drawSize = 1;
	    			        }
	    			        else{
	    			        	drawSize = 3;
	    			        }
	    			        
	    			        if(enemyChoice == 1){
	    			        	maxEnemy = 10;
	    			        }
	    			        else if(enemyChoice == 3){
	    			        	maxEnemy = 30;
	    			        }
	    			        else{
	    			        	maxEnemy = 20;
	    			        }
	    			        
	    			        if(buttonChoice == 9){
	    			        	drawButton = true;
	    			        }
	    			        else{
	    			        	drawButton = false;
	    			        }
	    					setSettings(drawSize,drawButton,maxEnemy);
	    					try {
	    		        	    Thread.sleep(3000);                 //1000 milliseconds is one second.
	    		        	} catch(InterruptedException ex) {
	    		        	    Thread.currentThread().interrupt();
	    		        	}
	    					
	    				}
	    				
	    				for(int b = 1; b < 4;b++){
	    					if(inSquare(settingMenuSprite[b].getBoundingRectangle(),touches.get(i).touchX,touches.get(i).touchY)){
	    						enemyChoice = b;
	    					}
	    				}
	    				
	    				for(int b = 5; b < 8;b++){
	    					if(inSquare(settingMenuSprite[b].getBoundingRectangle(),touches.get(i).touchX,touches.get(i).touchY)){
	    						drawChoice = b;
	    					}
	    				}
	    				
	    				for(int b = 9; b < 11;b++){
	    					if(inSquare(settingMenuSprite[b].getBoundingRectangle(),touches.get(i).touchX,touches.get(i).touchY)){
	    						buttonChoice = b;
	    					}
	    				}	
	    		}
	    	}  
    	}
    	if(menuChoice == 4){
    		Gdx.gl.glClearColor(0, 0, 0, 0);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	        
	        backButtonSprite.setBounds(4*width/5,0,width/5,height/4);
	        menuWritingSprite[1].setBounds(width/3,3*height/4, width/3, height/3);
	        menuSprite[1].setBounds(width/3,13*height/16, width/3, height/3);
	        int highScore;
	        highScore = getHighScore();
	        if(drawSize == 1){
	        	font.draw(batch,"Highscore: " + highScore, 0, height/2);
	        }
	        else if(drawSize == 2){
	        	font2.draw(batch,"Highscore: " + highScore, 0, height/2);
	        }
	        else{
	        	font3.draw(batch,"Highscore: " + highScore, 0, height/2);
	        }
	        
	        backButtonSprite.draw(batch);
	        menuSprite[1].draw(batch);
	        menuWritingSprite[1].draw(batch);
	        
	        
	        for(int i = 0 ; i < 5; i ++){
	    		if(touches.get(i).touched){
	    				if(inSquare(backButtonSprite.getX(), width/5,backButtonSprite.getY(),height/4, touches.get(i).touchX,touches.get(i).touchY)){
	    					menuChoice = 0;
	    					touches.get(i).touched = false;
	    				}	
	    		}
	    		
	    	}
	        
    	}
    	//GAME
    	if(menuChoice == 1){
    		if(start){
    			gameMap = new GameMap(new Texture(Gdx.files.internal("Map1.png")));
    			moveSpeed = (int) (MAX_MOVESPEED);
    			player.reset();
    			player.setShoot(width/2 ,height/2);
    			gameMap.setStartMapPosition(width/2 - gameMap.mapSprite.getWidth()/2,height/2 - gameMap.mapSprite.getHeight()/2);
    			reload = false;
    			kills = 0;
    		    currentShotCount = 0;
    		    ammoBoxItem = new GroundItem();
    		    ammoBoxItem.spawn(gameMap.getX(), gameMap.getY(), gameMap.mapSprite.getWidth(), gameMap.mapSprite.getHeight(),0);
    		    ammoBox.setCenter(ammoBoxItem.getX(),ammoBoxItem.getY());
    		    player.setCenter(width/2,height/2);
    		    for(int i = 0; i < 4; i++){
    		    	playerSprite[i].setOriginCenter();
        		    playerSprite[i].setCenter(width/2,height/2);
    		    }
    		    for(int i = 0; i < 4; i++){
    		    	weaponClip[i] = player.weaponArray[i].getClipSize();
    		    }
    		    currentClip = player.weaponArray[player.selectedItem].getClipSize();
    		    player.setMap(gameMap.mapSprite.getWidth()/2 + playerSprite[4].getWidth()/2,gameMap.mapSprite.getHeight()/2 + playerSprite[4].getHeight()/2);
    		    
    			for(int i = 0; i < MAX_BULLETS; i++){
    	        	bullets[i] = new Bullet();
    	        }
    	        for(int i = 0; i < MAX_EXPLOSION; i++){
    	        	explosion[i] = new Explosion();
    	        }
    	        for(int i = 0; i < maxEnemy; i++){
    	        	int random = (int) (Math.random()*4);
    	        	enemy[i] = new Enemy(random);
    	        	enemySprite[i] = new Sprite(enemyTexture[random]);
    	        	enemy[i].spawn(gameMap.getX(), gameMap.getY(), gameMap.mapSprite.getWidth(), gameMap.mapSprite.getHeight(),0);
    	        	
    	        }
    	        for(int i = 0; i < MAX_DEAD; i++){
    	        	for(int d = 0; d < 4; d++){
    	        		deadDraw[i][d] = false;
    	        	}
    	        }
    	        for(int i = 0; i < 5; i++){
    	        	touches.put(i,  new TouchInfo());
    	        	touches.get(i).touched = false;
    	        }
    	        thumbRight.setCenter(width - width/5, height/4);
    	        thumbStickTop.setCenter(width - width/5, height/4);
    	        start = false;
    	        
    		}
    		
	        Gdx.gl.glClearColor(0, 0, 0, 0);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	        gameMap.mapSprite.draw(batch);
	        //GroundDraw
	        //Dead Enemy
	        for(int d = 0; d < MAX_DEAD; d++){
	        	for(int i = 0; i < 4; i++){
		        	if(deadDraw[d][i] == true){
		        		deadEnemySprite[d][i].draw(batch);
		        	}
	        	}
	        }
	        
	        //input
	        /*
	        if(availableA){
		        accelX = Gdx.input.getAccelerometerX();
		        accelY = Gdx.input.getAccelerometerY();
		        accelZ = Gdx.input.getAccelerometerZ();
	        }
	        if(accelZ < 0 && halfStompTime == 0){
	        	halfStompTime = System.nanoTime();
	        	halfStomp = true;
	        }
	        if((System.nanoTime() - halfStompTime) > 500000000){
	        	halfStomp = false;
	        	stompKill = false;
	        	halfStompTime = 0;
	        }
	        if(halfStomp == true && (System.nanoTime() - halfStompTime) < 500000000 && accelZ > 4){
	        	stompKill = true;
	        }
	        */
	        
	        //Deal With Input
	        for(int t = 0; t < 5;t++){
	        	if(touches.get(t).touched){
	        		
	        		if(touches.get(t).purpose == 0 &&  player.currentWeapon.getName() == "Machine Gun" && touches.get(t).touchTime() > 500000000 && touches.get(t).dragged == false){
	        			touches.get(t).purpose = 6;
	        		}
	        		
	        		if(touches.get(t).purpose == 2 ){
	        			
	        				
	        			if(gameMap.nextStepCheck(player.getX() + thumbRight.getDifX()*moveSpeed, player.getY() + thumbRight.getDifY()*moveSpeed,playerSprite[4].getWidth(),playerSprite[4].getHeight())){
		        			
		        				for(int c = 0; c < maxEnemy; c++){
			        				if(enemy[c].alive()){
			        					enemy[c].shiftHorizontal(thumbRight.getDifX(), moveSpeed);
			        					enemy[c].shiftVertical(thumbRight.getDifY(), moveSpeed);
			        				}
			        			}
			        			for(int c = 0; c < MAX_EXPLOSION; c++){
			        				if(explosion[c].exists == true){
			        					explosion[c].shiftHorizontal(thumbRight.getDifX(), moveSpeed);	
			        					explosion[c].shiftVertical(thumbRight.getDifY(), moveSpeed);
			        				}
			        			}
			        			for(int d = 0; d < MAX_DEAD; d++){
			        				for(int c = 0; c < 4; c++){
				        				if(deadDraw[d][c]){
				        					deadEnemySprite[d][c].translateX(thumbRight.getDifX()*moveSpeed);
				        					deadEnemySprite[d][c].translateY(thumbRight.getDifY()*moveSpeed);
				        				}
			        				}
			        			}
			        			gameMap.shiftMapX(thumbRight.getDifX(), moveSpeed);
			        			gameMap.shiftMapY(thumbRight.getDifY(), moveSpeed);
			        			player.shiftX(thumbRight.getDifX(), moveSpeed);
			        			player.shiftY(thumbRight.getDifY(), moveSpeed);
			        			ammoBoxItem.shiftHorizontal(thumbRight.getDifX(), moveSpeed);
			        			ammoBoxItem.shiftVertical(thumbRight.getDifY(), moveSpeed);
			        			
		        			}
	        			
	        		}
	        		if(Math.sqrt(Math.pow(thumbRight.getThumbX() - thumbRight.getCenterX() ,2) + Math.pow((thumbRight.getThumbY() - thumbRight.getCenterY()),2)) > (thumbRight.getSpriteBase().getWidth()/2) + thumbRight.getSpriteThumb().getWidth()/2){
	        			thumbRight.reset();
	        		}	
	        		if(touches.get(t).purpose == 6 &&  player.currentWeapon.getName() == "Machine Gun"){
	    				if (currentShotCoolDown == 0 && currentReloadCoolDown == 0 && currentClip > 0){
	            			if(currentShotCount >= MAX_BULLETS){
	            				currentShotCount = 0;
	            				bullets[0].reset();
	            			}
	            			bullets[currentShotCount].shoot(touches.get(t).touchX, touches.get(t).touchY, player.getShootX(), player.getShootY(),player.getX(),player.getY());
	            			player.rotation = bullets[currentShotCount].getAngle();
	            			
	            			currentShotCount += 1;
	            			if(currentShotCount >= MAX_BULLETS){
		        				currentShotCount = 0;
							}
	            			currentShotCoolDown = (player.returnWeapon()).getShotCoolDown();;
	            			currentClip -= 1;
	            		}	
	    			}
	        		
	        	}
	        }
	        
	        //update
	        //Bullets
	        //outer C to allow the bullet to update 
	        for(int i = 0; i < MAX_BULLETS; i++){
	        	if(bullets[i].hasBeenShot()){
	        		for(int c = 0; c < 10; c++){
		        		bullets[i].update();
			        	if(!gameMap.nextStepCheck(bullets[i].getMapX(), bullets[i].getMapY(),1,1)){
			    			if(player.getItem() == 3){
		        				for(int r = 0; r < MAX_EXPLOSION; r++){
		        					if(explosion[r].exists == false){
		        						explosion[r] = new Explosion(bullets[i].getX(),bullets[i].getY(), explosionSprite[r][0].getWidth()/2,500000000L);
		        						explosionSprite[r][explosion[r].getNextExplosionSprite()].setCenter(explosion[r].getX(), explosion[r].getY());
		        					}
		        				}
		        			}
			    			bullets[i].reset();
			        	}
		        	
			        	for(int e = 0; e < maxEnemy; e++){
			        		if(enemy[e].alive()){
			        			if(inSquare(enemySprite[e].getBoundingRectangle(),bullets[i].getX(),bullets[i].getY())&& bullets[i].hasBeenShot() == true) {
			        			kills += 1;
			        			enemy[e].alive = false;
				        			if(player.getItem() == 3){
				        				for(int r = 0; r < MAX_EXPLOSION; r++){
				        					if(explosion[r].exists == false){
				        						explosion[r] = new Explosion(bullets[i].getX(),bullets[i].getY(), explosionSprite[r][0].getWidth()/2,500000000L);
				        						explosionSprite[r][explosion[r].getNextExplosionSprite()].setCenter(explosion[r].getX(), explosion[r].getY());
				        					}
				        				}
				        			}
				        			bullets[i].reset();
			        			}	
			        		}
			        	}
	        		}
	        	}
	       }
	        for(int i = 0; i < MAX_BULLETS; i++){
	        	if(bullets[i].hasBeenShot()){
			        bulletSprite[i].setCenter(bullets[i].getX(),bullets[i].getY());
		    		bulletSprite[i].draw(batch);
	        	}
	        }
	        //Enemies
	        for(int i = 0; i < maxEnemy; i++){
	        	for(int c = 0; c < MAX_EXPLOSION;c++){
	        		if(explosion[c].exists == true && enemy[i].alive == true){
		        		if(distance(enemy[i].getX(),explosion[c].getX(),enemy[i].getY(),explosion[c].getY()) < explosion[c].getRadius()){
		        			kills += 1;
		        			enemy[i].alive = false;
		        			
		        		}
	        		}
	        	}
	        	if(enemy[i].alive){
		        	if(gameMap.nextStepCheck(enemy[i].fakeX(),enemy[i].fakeY(),enemySprite[i].getWidth(),enemySprite[i].getHeight())){
		        		enemy[i].update(width, height);
		        	}
		        	else if(gameMap.nextStepCheck(enemy[i].fakeX(),enemy[i].rMapY(),enemySprite[i].getWidth(),enemySprite[i].getHeight())){
		        		enemy[i].updateX(width, height);
		        	}
		        	else if(gameMap.nextStepCheck(enemy[i].rMapY(),enemy[i].fakeY(),enemySprite[i].getWidth(),enemySprite[i].getHeight())){
		        		enemy[i].updateY(width, height);
		        	}
		        	else{
		        		enemy[i].calculateAngle(width/2, height/2);
		        	}
		        	
		        	enemySprite[i].setCenter(enemy[i].getX(),enemy[i].getY());
		        	enemySprite[i].setRotation(enemy[i].returnAngle()- 90);
		        	enemySprite[i].draw(batch);
	        	}

	        	if((inCircle(enemy[i].getX(),enemy[i].getY(),width/2,height/2,playerSprite[4].getWidth())) && enemy[i].alive == true && enemy[i].canHit()){
	        		player.hit();
	        		enemy[i].giveHit();
	        	}	
	        }
	        for(int i = 0; i < maxEnemy; i++){
	        	if(enemy[i].alive == false){
	        		//Draw Dead Guy
	        		deadEnemySprite[deadDrawCount][enemy[i].myType()].setCenter(enemy[i].getX(),enemy[i].getY());
        			deadEnemySprite[deadDrawCount][enemy[i].myType()].setRotation(enemy[i].returnAngle()- 90);
        			deadDraw[deadDrawCount][enemy[i].myType()] = true;
        			deadDrawCount += 1;
        			if(deadDrawCount >= MAX_DEAD){
        				deadDrawCount = 0;
        			}
	        		//Spawn
	        		if(gameMap.getX() > -477 && gameMap.getY() < -465){
	        			enemy[i].spawn(gameMap.getX(), gameMap.getY(), gameMap.mapSprite.getWidth(), gameMap.mapSprite.getHeight(),1);
	        		}
	        		else if(gameMap.getX() < -477 && gameMap.getY() < -465){
	        			enemy[i].spawn(gameMap.getX(), gameMap.getY(), gameMap.mapSprite.getWidth(), gameMap.mapSprite.getHeight(),2);
	        		}
	        		else if(gameMap.getX() < -477 && gameMap.getY() > -465){
	        			enemy[i].spawn(gameMap.getX(), gameMap.getY(), gameMap.mapSprite.getWidth(), gameMap.mapSprite.getHeight(),3);
	        		}
	        		else if(gameMap.getX() > -477 && gameMap.getY() > -465){
	        			enemy[i].spawn(gameMap.getX(), gameMap.getY(), gameMap.mapSprite.getWidth(), gameMap.mapSprite.getHeight(),4);
	        		}
	        		else{
	        			enemy[i].spawn(gameMap.getX(), gameMap.getY(), gameMap.mapSprite.getWidth(), gameMap.mapSprite.getHeight(),0);
	        		}
	        	}
	        }
	        //AmmoBox
	        if(inSquare(ammoBox.getBoundingRectangle(),width/2,height/2)){
	        	player.weaponAmmo[0] += ammoBoxItem.amountP;
	        	player.weaponAmmo[1] += ammoBoxItem.amountM;
	        	player.weaponAmmo[2] += ammoBoxItem.amountS;
	        	player.weaponAmmo[3] += ammoBoxItem.amountR;
	        	if(gameMap.getX() > -477 && gameMap.getY() < -465){
	        		ammoBoxItem.spawn(gameMap.getX(), gameMap.getY(), gameMap.mapSprite.getWidth(), gameMap.mapSprite.getHeight(),1);
        		}
        		else if(gameMap.getX() < -477 && gameMap.getY() < -465){
        			ammoBoxItem.spawn(gameMap.getX(), gameMap.getY(), gameMap.mapSprite.getWidth(), gameMap.mapSprite.getHeight(),2);
        		}
        		else if(gameMap.getX() < -477 && gameMap.getY() > -465){
        			ammoBoxItem.spawn(gameMap.getX(), gameMap.getY(), gameMap.mapSprite.getWidth(), gameMap.mapSprite.getHeight(),3);
        		}
        		else if(gameMap.getX() > -477 && gameMap.getY() > -465){
        			ammoBoxItem.spawn(gameMap.getX(), gameMap.getY(), gameMap.mapSprite.getWidth(), gameMap.mapSprite.getHeight(),4);
        		}
        		else{
        			ammoBoxItem.spawn(gameMap.getX(), gameMap.getY(), gameMap.mapSprite.getWidth(), gameMap.mapSprite.getHeight(),0);
        		}
	        	   	
	        }
	        
	        //Explosions
	        for(int r = 0; r < MAX_EXPLOSION; r++){
	        	if(explosion[r].exists == true){
	        		if(distance(width/2,explosion[r].getX(),height/2,explosion[r].getY()) < explosion[r].getRadius()){
	        			player.hit();
	        		}
	        		
					explosion[r].continueExplode(false,explosionSprite[r][explosion[r].getNextExplosionSprite()].getWidth()/2);	
					explosionSprite[r][explosion[r].getNextExplosionSprite()].setCenter(explosion[r].getX(), explosion[r].getY());
					explosionSprite[r][explosion[r].getNextExplosionSprite()].draw(batch);
				}
				
			}
	        
	        if(currentShotCoolDown > 0){
	        	currentShotCoolDown -= 1;
	        }
	        if(currentReloadCoolDown > 0){
	        	currentReloadCoolDown -= 1;
	        	if(currentReloadCoolDown == 0){
	            	reload = false;
	            	if(player.weaponAmmo[player.getItem()] < player.returnWeapon().getClipSize()){
		        		currentClip = player.weaponAmmo[player.getItem()];
		        		player.weaponAmmo[player.getItem()] = 0;
		        	}
		        	else{
		        		currentClip = player.returnWeapon().getClipSize();
		        		player.weaponAmmo[player.getItem()] -= player.returnWeapon().getClipSize();
		        	}
	            }
	        }
	        ammoBox.setCenter(ammoBoxItem.getX(), ammoBoxItem.getY());
	        ammoBox.draw(batch);
	        if(reload == true){
	        	if(player.weaponAmmo[player.getItem()] > 0){
		        	if(drawSize == 1){
			        	font.draw(batch, "Reloading!", width/2-width/30, height- font.getLineHeight()*2);
			        }
			        else if(drawSize == 2){
			        	font2.draw(batch, "Reloading!", width/2-width/30, height - font2.getLineHeight()*2);
			        }
			        else{
			        	font3.draw(batch, "Reloading!", width/2-width/30, height - font3.getLineHeight()*2);
			        }

	        	}
	        }

	        if(drawSize == 1){
	        	font.draw(batch,"Kills: " + kills,width/2 - width/30,height);
		        font.draw(batch,"Gun: " + player.returnWeapon().getName(),width/2 - width/30,height - font.getLineHeight());
		        font.draw(batch, currentClip + ":" + player.weaponAmmo[player.getItem()],width/15,height-height/15);
	        }
	        else if(drawSize == 2){
	        	font2.draw(batch,"Kills: " + kills,width/2 - width/30,height);
		        font2.draw(batch,"Gun: " + player.returnWeapon().getName(),width/2 - width/30,height - font2.getLineHeight());
		        font2.draw(batch, currentClip + ":" + player.weaponAmmo[player.getItem()],width/15,height-height/15);
	        }
	        else{
	        	font3.draw(batch,"Kills: " + kills,width/2 - width/30,height);
		        font3.draw(batch,"Gun: " + player.returnWeapon().getName(),width/2 - width/30,height - font3.getLineHeight());
		        font3.draw(batch, currentClip + ":" + player.weaponAmmo[player.getItem()],width/15,height-height/15);
	        }
	        
	        if(currentClip == 0 && reload == false){
	        	if(player.weaponAmmo[player.getItem()] == 0){
	        		if(drawSize == 1){
		        		font.draw(batch, "No Ammo!", width/2-width/30, height - font.getLineHeight()*2);
			        }
			        else if(drawSize == 2){
			        	font2.draw(batch, "No Ammo!", width/2-width/30, height - font2.getLineHeight()*2);
			        }
			        else{
			        	font3.draw(batch, "No Ammo!", width/2-width/30, height - font3.getLineHeight()*2);
			        }
	        	}
	        	else{
		        	if(drawSize == 1){
		        		font.draw(batch, "Reload!", width/2-width/30, height - font.getLineHeight()*2);
			        }
			        else if(drawSize == 2){
			        	font2.draw(batch, "Reload!", width/2-width/30, height - font2.getLineHeight()*2);
			        }
			        else{
			        	font3.draw(batch, "Reload!", width/2-width/30, height - font3.getLineHeight()*2);
			        }
	        	}
	        }
	        
	        healthBarSprite.setBounds(width/15,height - height/20,width/4,height-height/15);
	        healthBarFSprite.setBounds(width/15,height - height/20, Math.round(width/400*player.getHP()),height-height/15);
	        //UI Draw
	        healthBarSprite.draw(batch);
	        healthBarFSprite.draw(batch);
	        

	        /*
	        if(availableA){
	        	font.draw(batch, accelX + ", " + accelY + "," + accelZ, 0, height - height/20);
	        }
	        if(messageBox.displayTime + messageBox.startTime  > System.nanoTime() && messageBox.displayTime + messageBox.startTime != 0){
	        	messageBox.sprite.draw(batch);
	        	font.draw(batch, messageBox.message, messageBox.x + width/10, messageBox.y + messageBox.height/2);
	        	
	        }
	        if(messageBox.displayTime + messageBox.startTime  < System.nanoTime()){
	        	messageBox.reset();
	        }
	        */
	        
	        
	        
	        
	        if(player.getRotation() != 0){
	        	for(int i = 0; i < 4; i++){
	        		playerSprite[i].setCenter(width/2,height/2);
	        		playerSprite[i].setRotation(player.getRotation() - 90);
	        	}
	        }
	        playerSprite[player.getItem()].draw(batch);
	        if(drawButton){
		        for(int i = 2; i < 4; i++){
		        	arrowSprite[i].draw(batch);
		        }
		        reloadButtonSprite.draw(batch);
	        }
			
	        thumbRight.getSpriteBase().draw(batch);
	        thumbStickTop.setBounds(thumbRight.getThumbX()- thumbRight.getTRadius(), thumbRight.getThumbY()- thumbRight.getTRadius(), thumbRight.getTRadius()*2, thumbRight.getTRadius()*2);
	        thumbStickTop.draw(batch);

	        
	 
	        if(player.getHP() < 0){
	        	setHighScore(kills);
	        	menuChoice = 0;
	        	start = false;
	        	try {
	        	    Thread.sleep(3000);                 //1000 milliseconds is one second.
	        	} catch(InterruptedException ex) {
	        	    Thread.currentThread().interrupt();
	        	}
	        }  
    	}
    	batch.end();
   }

    

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		if(pointer < 5){
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = Math.abs(screenY - height);
            touches.get(pointer).startX = screenX;
            touches.get(pointer).startY = Math.abs(screenY - height);
            touches.get(pointer).touched = true;
            touches.get(pointer).dragged = false;
            touches.get(pointer).touchedStart = System.nanoTime();
            

            if(distance(touches.get(pointer).touchX, thumbRight.getThumbX(),touches.get(pointer).touchY,thumbRight.getThumbY()) < (thumbRight.getSpriteThumb().getWidth()/2) && menuChoice != 0){
	            	thumbRight.setThumb(screenX,Math.abs(screenY - height));
	            	touches.get(pointer).purpose = 2;
            }
            if(inSquare(width/16, width/8, 0, width/8, touches.get(pointer).touchX, touches.get(pointer).touchY)){
            	touches.get(pointer).purpose = 8;
            }
            if(inSquare(3*width/16, width/8, 0, width/8, touches.get(pointer).touchX, touches.get(pointer).touchY)){
            	touches.get(pointer).purpose = 7;
            }
            if(inSquare(5*width/16, width/8, 0, width/8, touches.get(pointer).touchX, touches.get(pointer).touchY)){
            	touches.get(pointer).purpose = 9;
            }
            
        }
		
        return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(pointer < 5){
			//shoot
			if(menuChoice == 1){
				if((touches.get(pointer).touchTime() < 1500000000) && touches.get(pointer).purpose == 6 || touches.get(pointer).purpose == 0){
	            	if (currentShotCoolDown == 0 && currentReloadCoolDown == 0 && currentClip > 0){
	        			if(currentShotCount >= MAX_BULLETS){
	        				currentShotCount = 0;
	        				bullets[0].reset();
	        			}
	        			bullets[currentShotCount].shoot(touches.get(pointer).touchX,touches.get(pointer).touchY, player.getShootX(), player.getShootY(),player.getX(),player.getY());
	        			player.rotation = bullets[currentShotCount].getAngle();
	        			float baseAngle =  bullets[currentShotCount].getAngle();
	        			currentShotCount += 1;
	        			
	        			if(player.currentWeapon.getName() == "Shotgun"){
	        				for(int i = -2; i < 3; i++){
	        					if(i != 0){
	        						if(currentShotCount >= MAX_BULLETS){
	        	        				currentShotCount = 0;
	        						}
		        					bullets[currentShotCount].shoot((baseAngle + 15*i), player.getShootX(), player.getShootY(),player.getX(),player.getY());
		        					currentShotCount += 1;
	        					}
	        				}
	        			}
	        			currentShotCoolDown = (player.returnWeapon()).getShotCoolDown();
	        			currentClip -= 1;
	        		}	
	            }
				if(touches.get(pointer).purpose == 2){
					thumbRight.reset();
				}
				if(touches.get(pointer).purpose == 4 && Math.abs(halfWayWeapon) == 2){
					if(halfWayWeapon == 2){
						weaponClip[player.getItem()] = currentClip;
						player.forwardItem();
	    				currentShotCoolDown = (player.returnWeapon()).getShotCoolDown();
	    				currentClip = weaponClip[player.getItem()];
					}
					else if(halfWayWeapon == -2){
						weaponClip[player.getItem()] = currentClip;
						player.backwordItem();
	    				currentShotCoolDown = (player.returnWeapon()).getShotCoolDown();
	    				currentClip = weaponClip[player.getItem()];
					}
					halfWayWeapon = 0;
				    halfWayWResetS = 0;
				}
				
				if(touches.get(pointer).purpose == 5 && halfWayRResetS != 0 && halfWayReload == false){
					halfWayReload = true;
				}
				if(touches.get(pointer).purpose == 7){
					reload = true;
				}
				if(drawButton){
					if(touches.get(pointer).purpose == 8){
						weaponClip[player.getItem()] = currentClip;
						player.backwordItem();
	    				currentShotCoolDown = (player.returnWeapon()).getShotCoolDown();
	    				currentClip = weaponClip[player.getItem()];
					}
					if(touches.get(pointer).purpose == 9){
						weaponClip[player.getItem()] = currentClip;
						player.forwardItem();
	    				currentShotCoolDown = (player.returnWeapon()).getShotCoolDown();
	    				currentClip = weaponClip[player.getItem()];
					}
				}
				if(reload == true){
					if(player.weaponAmmo[player.getItem()] >= 0){
						currentReloadCoolDown = player.currentWeapon.getReloadTime();
					}
					
					halfWayRResetS = 0;
					halfWayReload = false;
				}
	            touches.get(pointer).touchX = 0;
	            touches.get(pointer).touchY = 0;
	            touches.get(pointer).startX = 0;
	            touches.get(pointer).startY = 0;
	            touches.get(pointer).touched = false;
	            touches.get(pointer).dragged = false;
	            touches.get(pointer).purpose = 0;
			}
        }
        return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		float swipeXLength,swipeYLength;
		
		screenY = Math.abs(screenY-height);
		
		if(pointer < 5){
			touches.get(pointer).dragged = true;
			swipeXLength =  (screenX - touches.get(pointer).startX);
			swipeYLength =  (screenY - touches.get(pointer).startY);
			touches.get(pointer).touchX = screenX;
			touches.get(pointer).touchY = screenY;
			
			

			if(touches.get(pointer).purpose == 2){
				thumbRight.setThumb(screenX, screenY);
				if(thumbRight.offThumb(screenX, screenY)){
					touches.get(pointer).touchX = 0;
		            touches.get(pointer).touchY = 0;
		            touches.get(pointer).startX = 0;
		            touches.get(pointer).startY = 0;
		            touches.get(pointer).touched = false;
		            touches.get(pointer).dragged = false;
		            touches.get(pointer).purpose = 0;
				};
			}
			
			
			if((Math.abs(swipeXLength) > width/6 || Math.abs(swipeYLength) > width/6) && touches.get(pointer).purpose == 0){
				if(Math.abs(swipeXLength)< Math.abs(swipeYLength) && reload == false  ){
			    	if((swipeYLength < 0 || (swipeYLength > 0 && player.currentWeapon.getName() == "Shotgun")) && halfWayReload == false ){  
			    		touches.get(pointer).purpose = 5;
			    		halfWayRResetS = System.nanoTime();
			        }
			    	else if((System.nanoTime() - halfWayRResetS) < 3000000000L && halfWayReload == true && swipeYLength > 0){
			    		touches.get(pointer).purpose = 5;
				    	reload = true;
				    }
					else{
						halfWayReload = false;
						halfWayRResetS = 0;
					}
			    }
			    else if(Math.abs(swipeXLength)>Math.abs(swipeYLength)){
				    if(halfWayWeapon == 0){	
				    	touches.get(pointer).purpose = 4;
				    	halfWayWResetS = System.nanoTime();
				    	if(swipeXLength > 0){
			    			halfWayWeapon = 1;
			    		}
			    		else{
			    			halfWayWeapon = -1;
			    		}
				    }
				    else if((System.nanoTime() - halfWayWResetS) < 3000000000L){
				    	touches.get(pointer).purpose = 4;
				    	
					    if(swipeXLength > 0 && halfWayWeapon == 1){
				    		halfWayWeapon = 2;
				    	}
					    else if (swipeXLength < 0 && halfWayWeapon == -1){
				    		halfWayWeapon = -2;
				    	}
				    }
				    else{
				    	halfWayWeapon = 0;
				    }
		    	}
			}
		}
		return true;
	}
	public float distance(float x1, float x2, float y1, float y2){
		float dis = (float) Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1-y2,2));
		return dis;
		
	}
	public boolean inSquare(float x1, float width, float y1, float height, float x, float y){
		if((x1 < x) && (x1 + width > x) && (y1 < y )&& ((y1 + height) > y)){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean inSquare(com.badlogic.gdx.math.Rectangle rect, float x, float y){
		if((rect.x < x) && (rect.x + rect.width > x) && (rect.y < y )&& ((rect.y + rect.height) > y)){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean inCircle(float inX, float inY, float circleX, float circleY, float radius){
		if(distance(inX,circleX,inY,circleY) < radius){
			return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	public boolean keyDown(int key){
		if(key == Keys.BACK){
			
		}
		return false;
	}
	
	
	

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	
	

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}
}



