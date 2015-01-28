package com.e005.DaysGoneBy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player {
	public static final int MAX_WEAPONS = 4;
	public float rotation;
	
	private float positionX, positionY;
	private float centerX, centerY;
	private float mapX,mapY;
	private float shootX, shootY;
	private int health = 100;
	
	
	public Weapon[] weaponArray = new Weapon[MAX_WEAPONS];
	public int[] weaponAmmo = new int[MAX_WEAPONS];
	
	public int selectedItem = 0;
	public Weapon currentWeapon = new Weapon();
	
	
	public Player(){
		
		
		shootX = 0;
		shootY = 0;
			
		
		health=100;
		selectedItem = 0;
		rotation = 0;
		for(int i = 0; i < MAX_WEAPONS; i++){
			weaponArray[i] = new Weapon();
		}
		//Weapons
        weaponArray[0].setName("Pistol");
        weaponArray[0].setClip(15);
        weaponArray[0].setReloadTime(20);
        weaponArray[0].setShotCoolDown(15);
        weaponArray[0].setS(17, 20);
        weaponAmmo[0] = 30;
        
        weaponArray[1].setName("Machine Gun");
        weaponArray[1].setClip(30);
        weaponArray[1].setReloadTime(30);
        weaponArray[1].setShotCoolDown(5);
        weaponArray[1].setS(17, 25);
        weaponAmmo[1] = 30;
        
        weaponArray[2].setName("Shotgun");
        weaponArray[2].setClip(2);
        weaponArray[2].setReloadTime(20);
        weaponArray[2].setShotCoolDown(20);
        weaponArray[2].setS(12, 25);
        weaponAmmo[2] = 10;
        
        weaponArray[3].setName("Rocket Launcher");
        weaponArray[3].setClip(2);
        weaponArray[3].setReloadTime(60);
        weaponArray[3].setShotCoolDown(20);
        weaponArray[3].setS(19, 20);
        weaponAmmo[3] = 6;
        
        currentWeapon = weaponArray[selectedItem];
	}

	public void setRotation(float rotation){
		this.rotation = rotation;
		shootX = (float) (shootX*Math.cos(rotation) - shootY*Math.sin(rotation));
		shootY = (float) (shootY*Math.cos(rotation) + shootX*Math.sin(rotation));
	}
	public float getRotation(){
		return rotation;
	}
	
	
	public void setPosition(float x, float y){
		positionX = x;
		positionY = y;
	}
	public void setCenter(float x, float y){
		centerX = x;
		centerY = y;
		setShoot(x,y);
	}
	
	public float getX(){
		return mapX;
	}
	public float getY(){
		return mapY;
	}
	public float getShootX(){
		return shootX;
	}
	public float getShootY(){
		return shootY;
	}
	public void setShoot(float x, float y){
		shootX = x;
		shootY = y;
		
	}
	public void setMap(float x, float y){
		mapX = x;
		mapY = y;
		//havent set yet
	}
	public void shiftX(float deg, float moveSpeed){
		mapX += deg * moveSpeed;
	}
	public void shiftY(float deg, float moveSpeed){
		mapY += deg*moveSpeed;
	}
	
	public void setHP(int hp){
		health = hp;
	}
	public void getHP(int hp){
		health += hp;
	}
	public void hit(){
		health -= 1;
	}
	public int getHP(){
		return health;
	}
	
	public int getItem(){
		return selectedItem;
	}
	public void forwardItem(){
		selectedItem += 1;
		if(selectedItem > 3){
			selectedItem = 0;
		}
		currentWeapon = weaponArray[selectedItem];
	}
	public void backwordItem(){
		selectedItem -= 1;
		if(selectedItem < 0){
			selectedItem = 3;
		}
		currentWeapon = weaponArray[selectedItem];
	}
	public Weapon returnWeapon(){
		return currentWeapon;
	}

	public void reset(){
		health=100;
		selectedItem = 0;
		rotation = 0;
		for(int i = 0; i < MAX_WEAPONS; i++){
			weaponArray[i] = new Weapon();
		}
		//Weapons
		weaponArray[0].setName("Pistol");
        weaponArray[0].setClip(15);
        weaponArray[0].setReloadTime(20);
        weaponArray[0].setShotCoolDown(15);
        weaponAmmo[0] = 30;
        
        weaponArray[1].setName("Machine Gun");
        weaponArray[1].setClip(30);
        weaponArray[1].setReloadTime(30);
        weaponArray[1].setShotCoolDown(5);
        weaponAmmo[1] = 30;
        
        weaponArray[2].setName("Shotgun");
        weaponArray[2].setClip(2);
        weaponArray[2].setReloadTime(20);
        weaponArray[2].setShotCoolDown(20);
        weaponAmmo[2] = 10;
        
        weaponArray[3].setName("Rocket Launcher");
        weaponArray[3].setClip(2);
        weaponArray[3].setReloadTime(60);
        weaponArray[3].setShotCoolDown(20);
        weaponAmmo[3] = 6;
        
        currentWeapon = weaponArray[selectedItem];
	}
}
