package com.e005.DaysGoneBy;

public class GroundItem {
	private float x,y;
	public int amountP,amountM,amountS,amountR;
	private int zone;
	
	public void GroundItem(){
		x = 0;
		y = 0;
	}
	public void spawn(float mapX, float mapY, float width, float height, int zone){

		int random;
		float randomX,randomY;
		//Make sure that the next item box does not spawn where the player is
		do{
			random = (int) (Math.random()*4 + 1);
		}while(random == zone);
		this.zone = random;
		
		randomX = (float) (Math.random()*150);
		randomY = (float) (Math.random()*150);
		
		if(random == 1){
			x = (mapX + 275);
			y = (mapY + height - 275);
		}
		else if(random == 2){
			x =  (mapX + width - 275) ;
			y =(mapY + height - 275);
		}
		else if(random == 3){
			x = (mapX + width - 275);
			y = (mapY + 275);
		}
		else if(random == 4){
			x = (float)(mapX + 275);
			y = (float)(mapY + 275);
		}
		
		//Randomly set the amount of ammo given
		amountP = (int) (Math.random()*15 + 1);
		amountM = (int) (Math.random()*30 + 1);
		amountS = (int) (Math.random()*5 + 1);
		amountR = (int) (Math.random()*2 + 1);
	}
	
	//Shift functions
	public void shiftHorizontal(float degree, int moveSpeed){
		x += degree * moveSpeed;
	}
	public void shiftVertical(float degree, int moveSpeed){
		y += degree * moveSpeed;
	}
	
	//Basic return functions
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
}
