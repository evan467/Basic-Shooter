package com.e005.DaysGoneBy;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Explosion {
	private float startX, startY;
	private float radius, finalRadius;
	private int explosionSpeed;
	private int nextExplosionSprite;
	private long startTime, fullTime, chunk;
	public boolean exists;
	
	public Explosion(){
		this.exists = false;
	}
	
	public Explosion(float startX, float startY, float radius, long fullTime){
		this.startX = startX;
		this.startY = startY;
		this.radius = radius;
		this.startTime = System.nanoTime();
		this.fullTime = fullTime + this.startTime;
		this.chunk = fullTime/3;
		this.exists = true;
		this.nextExplosionSprite = 0;
	}
	public void continueExplode(boolean shortStop, float radius){
		if(this.fullTime <= System.nanoTime() || shortStop == true ){
			this.exists = false;
		}
		if((System.nanoTime()-startTime)>= chunk){
			nextExplosionSprite = 1;
		}
		if((System.nanoTime()-startTime)>= (chunk*2)){
			nextExplosionSprite = 2;
		}
		this.radius = radius;
		
	}
	public void shiftHorizontal(float degree, int moveSpeed){
		startX += degree * moveSpeed;
	}
	public void shiftVertical(float degree, int moveSpeed){
		startY += degree * moveSpeed;
	}
	
	public float getX(){
		return startX;
	}
	public float getY(){
		return startY;	
	}
	public float getRadius(){
		return radius;
	}
	public int getNextExplosionSprite(){
		return this.nextExplosionSprite;
	}
	
	
}
