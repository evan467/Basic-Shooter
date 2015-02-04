package com.e005.DaysGoneBy;


public class Bullet {
	public static final int BULLET_SPEED = 3;
	
	private float x,y;
	private float mapX, mapY;
	private float speedX,speedY,angle;
	private boolean shot;
	
	//CODE: 1 = Pistol, 2 = Machine Gun, 3 = Shotgun, 4 = Rocket
	
	public Bullet(){
		x = 0;
		y = 0;
		speedX = 0;
		speedY = 0;
		shot = false;
		angle = 0;
		mapX = 0;
		mapY = 0;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	public float getMapX(){
		return mapX;
	}
	
	public float getMapY(){
		return mapY;
	}
	public float getAngle(){
		return angle;
	}
	public float getfakeX(){
		return x+speedX*BULLET_SPEED;
	}
	public float getfakeY(){
		return y+speedY*BULLET_SPEED;
	}
	
	
	public void reset(){
		x = 0;
		y = 0;
		shot = false;
	}
	public void shoot(float pointX, float pointY, float centerX, float centerY,float mapX, float mapY){
		shot = true;
		x = (int) centerX;
		y = (int) centerY;
		this.mapX = mapX;
		this.mapY = mapY;
		
		if( pointX > centerX){
			angle = (float) (Math.atan2((pointY - centerY),(pointX - centerX)) / (Math.PI / 180));
			
		}
		else if (pointX < centerX){
			angle = (float) (Math.atan2((pointY - centerY),(pointX - centerX)) / (Math.PI / 180));
			
		}
		
		else if (pointX == centerX && (pointY > centerY)){
			angle = 90;
		}
		else if (pointX == centerX && pointY < centerY){
			angle = 270;
		}
		
		speedX = (float) Math.cos(angle * (Math.PI / 180)) * 2;
		speedY = (float) Math.sin(angle * (Math.PI / 180)) * 2;
	}
	public void shoot(float angle, float centerX, float centerY,float mapX, float mapY){
		shot = true;
		this.angle = angle;
		this.x = (int) centerX;
		this.y = (int) centerY;
		this.mapX = mapX;
		this.mapY = mapY;
		speedX = (float) Math.cos(angle * (Math.PI / 180)) * 2;
		speedY = (float) Math.sin(angle * (Math.PI / 180)) * 2;
	}
	
	public boolean hasBeenShot(){
		return shot;
	}
	public void update(){
		x += speedX*BULLET_SPEED;
		y += speedY*BULLET_SPEED;
		mapX -= speedX*BULLET_SPEED;
		mapY -= speedY*BULLET_SPEED;
	}
	
	
}

