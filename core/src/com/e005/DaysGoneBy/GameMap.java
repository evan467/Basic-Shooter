package com.e005.DaysGoneBy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameMap {
	private float drawX,drawY;
	public int[][] checkSpace;
	public Texture mapTexture;
	public Sprite mapSprite;
	
	public GameMap(Texture newMap){
		mapTexture = newMap;
		mapSprite = new Sprite(mapTexture);
		checkSpace = new int[(int) mapSprite.getWidth()][(int) mapSprite.getHeight()];
		
		//MAP SPECIFIC
		//x - Hole
		for(int i = 700; i < 850; i++){
			//y
			for(int r = 550; r < 700; r++){
				checkSpace[i][r] = 1;
			}
		}
		//Top Boards/Exits
		for(int i = 360; i <720; i++){
			for(int r = 0; r < 100; r++)
				checkSpace[i][r] = 1;
		}
		for(int i = 1433; i <1793; i++){
			for(int r = 0; r < 100; r++)
				checkSpace[i][r] = 1;
		}
		
		//Gravel
		for(int i = 550; i <1250; i++){
			for(int r = 1300; r < 1600; r++)
				checkSpace[i][r] = 1;
		}
		
		
		//Columns 
		for(int i = 400; i < 450; i++){
			 for(int r = 400; r < 450; r++){
				 checkSpace[i][r] = 1;
		 	}
		 }
		 for(int i = 400; i < 450; i++){
			 for(int r = 1200; r < 1250; r++){
				 checkSpace[i][r] = 1;
		 	}
		 }
		 for(int i = 1700; i < 1750; i++){
			 for(int r = 1200; r < 1250; r++){
				 checkSpace[i][r] = 1;
		 	}
		 }
		 for(int i = 1700; i < 1750; i++){
			 for(int r = 400; r < 450; r++){
				 checkSpace[i][r] = 1;
		 	}
		 }
		 
		 
	}
	public void setStartMapPosition(float x, float y){
		mapSprite.setPosition(x, y);
		drawX = x;
		drawY = y;
	}
	public void shiftMapX(float deg, float move){
		mapSprite.translateX(deg*move);
		drawX += deg*move;
	}
	public void shiftMapY(float deg, float move){
		mapSprite.translateY(deg*move);
		drawY += deg*move;
	}
	public boolean outOfBounds(float x, float y, float width, float height){

		if(x < mapSprite.getX() || x + width > mapSprite.getX() + mapSprite.getWidth() ||  y < mapSprite.getY() || y + height > mapSprite.getY() + mapSprite.getHeight()){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean nextStepCheck(float x, float y, float width, float height){
		boolean check = false;
	
		if(x - width < 0 || x  > mapSprite.getWidth() ||  y - height < 0 || y  > mapSprite.getHeight()){
			return false;
		}
		else{
			for(int i = (int) x; i >= (x-width); i--){
				for(int c = (int) y; c >= (y-height); c--){
					if(checkSpace[i][c] !=  0  ){
						check = true;
					}
				}
			}
			
			if(check){
				return false;
			}
			else{
				return true;
			}
		}
	}
	
	public float getX(){
		return drawX;
	}
	public float getY(){
		return drawY;
	}
}
