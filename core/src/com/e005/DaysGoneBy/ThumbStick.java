package com.e005.DaysGoneBy;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class ThumbStick {
	private float centerX,centerY;
	private float bRadius,tRadius;
	private float thumbX, thumbY;
	private float maxDif, difX,difY;
	private boolean hold,off;
	
	private int angle;
	
	private Sprite spriteThumb,spriteBase;
	
	public ThumbStick(){
		centerX = 0;
		centerY = 0;
		thumbX = 0;
		thumbY = 0;
		maxDif = 0;

	}
	
	private void calcDif(){
		maxDif = this.getSpriteBase().getWidth()/2 + this.getSpriteThumb().getWidth()/2;
		difX = (centerX - thumbX)/maxDif;
		difY = (centerY - thumbY)/maxDif;
		
	} 
	
	public void setCenter(int x, int y){
		centerX = x;
		centerY = y;
		thumbX = x;
		thumbY = y;
	}

	public float getDifX(){
		return difX;
	}
	public float getDifY(){
		return difY;
	}
	public float getMaxDif(){
		return maxDif;
	}
	
	public void setSpriteThumb(Sprite sprite, float width){
		this.spriteThumb = sprite;
		tRadius = width/14;
		this.spriteThumb.setBounds(thumbX - tRadius, thumbY - tRadius, tRadius*2, tRadius*2);
	}
	public void setSpriteBase(Sprite sprite, float width){
		this.spriteBase = sprite;
		bRadius = width/10;
		this.spriteBase.setBounds(centerX - bRadius, centerY - bRadius, bRadius*2, bRadius*2);
	}
	
	public void hold(){
		hold = true;
	}
	public void release(){
		hold = false;
	}
	
	public float getCenterX(){
		return centerX;
	}
	public float getCenterY(){
		return centerY;
	}
	
	public float getThumbX(){
		return thumbX;
	}
	public float getThumbY(){
		return thumbY;
	}
	public float getTRadius(){
		return tRadius;
	}
	
	public void setThumbX(int x){
			thumbX = x;
	}
	public void setThumbY(int y){
			thumbY = y;
	}
	public void setThumb(int x, int y){
		
		if(Math.sqrt(Math.pow(x - getCenterX() ,2) + Math.pow((y - getCenterY()),2)) > (getSpriteBase().getWidth()/2) + getSpriteThumb().getWidth()/2){
			off = true;
		}
		else{
			thumbX = x;
			thumbY = y;
			calcDif();
			off = false;
		}
	}
	public boolean offThumb(int x, int y){
		if(Math.sqrt(Math.pow(x - getCenterX() ,2) + Math.pow((y - getCenterY()),2)) > (getSpriteBase().getWidth()/2) + getSpriteThumb().getWidth()/2){
			reset();
			return true;
		}
		else{
			return false;
		}
	}
	
	public Sprite getSpriteThumb(){
		spriteThumb.setBounds(thumbX - tRadius, thumbY - tRadius, tRadius*2, tRadius*2);
		return spriteThumb;
	}
	public Sprite getSpriteBase(){
		spriteBase.setBounds(centerX - bRadius, centerY - bRadius, bRadius*2, bRadius*2);
		return spriteBase;
	}
	public void reset(){
		thumbX = centerX;
		thumbY = centerY;
	}
}
