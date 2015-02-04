package com.e005.DaysGoneBy;

import java.util.Random;

public class Enemy {
	private float x,y;
	private float mapX,mapY;
	private float width,height;
	private float angle;
	private float speed;
	
	private long lastHit;
	private long knockBack;
	
	public int myType;
	
	public boolean alive = false;
	
	public Enemy(int type){
		lastHit = 0;
		x = 0;
		y = 0;
		angle = 0;
		width = 0;
		height = 0;
		speed = 1;
		alive = false;
		myType = type;
	}
	//Basic set function
	public void setWidth(float width){
		this.width = width;
	}
	public void setHeight(float height){
		this.height = height;
	}
	
	//Basic return functions
	public int myType(){
		return myType;
	}
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public float getWidth(){
		return width;
	}
	public float getHeight(){
		return height;
	}
	public boolean alive(){
		return alive;
	}
	public float returnAngle(){
		return angle;
	}
	public float rMapX(){
		return mapX;
	}
	public float rMapY(){
		return mapY;
	}
	
	//Set timing of last hit so they do not constantly attack
	public void giveHit(){
		lastHit = System.nanoTime();
	}

	//Check to make sure enough time has passed before making an attack
	public boolean canHit(){
		if(System.nanoTime() - lastHit > 150000000L){
			return true;
		}
		else{
			return false;
		}
	}
	
	//Shift functions
	public void shiftVertical(boolean up, int moveSpeed){
			if(up){
				y -= moveSpeed;
			}
			else{
				y += moveSpeed;
			}
	}
	public void shiftHorizontal(boolean right, int moveSpeed){
			if(right){
				x -= moveSpeed;
				
			}
			else{
				x += moveSpeed;
			}
	}
	public void shiftHorizontal(float degree, int moveSpeed){
		x += degree * moveSpeed;
	}
	public void shiftVertical(float degree, int moveSpeed){
		y += degree * moveSpeed;
	}
	
	//Spawn procedure
	public void spawn(float mapX, float mapY, float width, float height, int zone){

		int random;
		float randomX,randomY;
		
		//Zone is where player currently is
		//This loop makes sure the enemy does not spawn where the player is
		do{
			random = (int) (Math.random()*4 + 1);
		}while(random == zone);
		
		randomX = (float) (Math.random()*150);
		randomY = (float) (Math.random()*150);
		if(random == 1){
			x = (float) (mapX + 200 + randomX);
			y = (float) (mapY + height - 200 - randomY);
		}
		else if(random == 2){
			x = (float) (mapX + width - 200 - randomX) ;
			y = (float) (mapY + height - 200 - randomY);
		}
		else if(random == 3){
			x = (float) (mapX + width - 200 - randomX);
			y = (float) (mapY + 200 + randomY);	
		}
		else if(random == 4){
			x = (float)(mapX + 200 + randomX);
			y = (float)(mapY + 200 + randomY);
		}
		this.mapX = width - (x- mapX);
		this.mapY = height - (y- mapY);
		alive = true;

		calculateAngle(width/2,height/2);
	}
	
	//Update full enemy 
	public void update(int width, int height){
		calculateAngle(width/2,height/2);
		float testX,testY;
		testX = (float) (x + speed*(Math.cos(angle*Math.PI/180)));
		testY = (float) (y + speed*(Math.sin(angle*Math.PI/180)));
		
		if(alive && !inCircle(testX,testY,width/2,height/2,this.width/6)){
			x += speed*(Math.cos(angle*Math.PI/180));
			y += speed*(Math.sin(angle*Math.PI/180));
			mapX -= speed*(Math.cos(angle*Math.PI/180));
			mapY -= speed*(Math.sin(angle*Math.PI/180));
		}	
	}
	
	//Update just x component of enemy
	//incase enemy is not able to move vertically
	public void updateX(int width, int height){
		calculateAngle(width/2,height/2);
		float testX,testY;
		testX = (float) (x + speed*(Math.cos(angle*Math.PI/180)));
		testY = (float) (y + speed*(Math.sin(angle*Math.PI/180)));
		
		if(alive && !inCircle(testX,testY,width/2,height/2,this.width/6)){
			x += speed*(Math.cos(angle*Math.PI/180));
			mapX -= speed*(Math.cos(angle*Math.PI/180));
		}
	}
	
	//Update just y component of enemy
	//incase enemy is not able to move horizontally
	public void updateY(int width, int height){
		calculateAngle(width/2,height/2);
		float testX,testY;
		testX = (float) (x + speed*(Math.cos(angle*Math.PI/180)));
		testY = (float) (y + speed*(Math.sin(angle*Math.PI/180)));
		
		if(alive && !inCircle(testX,testY,width/2,height/2,this.width/6)){
			y += speed*(Math.sin(angle*Math.PI/180));
			mapY -= speed*(Math.sin(angle*Math.PI/180));
		}
	}
	
	public float fakeX(){
		return (float) (mapX - speed*(Math.cos(angle*Math.PI/180)));
	}
	public float fakeY(){
		return (float) (mapY - speed*(Math.sin(angle*Math.PI/180)));
	}
	
	
	public void calculateAngle(float centerX, float centerY){
		if( x != centerX){
			angle = (float) (Math.atan2((centerY - y),( centerX -x )) / (Math.PI / 180));
			
		}
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
	public boolean inCircle(float inX, float inY, float circleX, float circleY, float radius){
		if(distance(inX,circleX,inY,circleY) < radius){
			return true;
		}
		else{
			return false;
		}
	}
}
