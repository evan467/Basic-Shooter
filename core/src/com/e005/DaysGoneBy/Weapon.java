package com.e005.DaysGoneBy;

public class Weapon {
	private int clipSize, shotCoolDown;
	private int inClip;
	private int reloadTime;
	private float sX,sY;
	private String name;
	
	public Weapon(){
		name = null;
	}
	
	public void setClip(int clip){
		clipSize = clip;
	}
	public void setReloadTime(int reload){
		reloadTime = reload;
	}
	public void setShotCoolDown(int coolDown){
		shotCoolDown = coolDown;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setS(float x, float y){
		this.sX = x;
		this.sY = y;
	}
	
	public float shootX(){
		return sX;
	}
	public float shootY(){
		return sY;
	}
	public int getClipSize(){
		return clipSize;
	}
	public int getReloadTime(){
		return reloadTime;
	}
	public String getName(){
		return this.name;
	}
	public int getShotCoolDown(){
		return shotCoolDown;
	}
	
	public Weapon(Weapon copy){
		this.name = copy.name;
		this.clipSize = copy.clipSize;
		this.reloadTime = copy.reloadTime;
		this.shotCoolDown = copy.shotCoolDown;
	}
}
