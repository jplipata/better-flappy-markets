package com.flappy.markets.GameObjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Building extends Scrollable {
	private Random r;
	private Rectangle barUp;

	private float groundY;
	
	private boolean isScored = false;
	
	public Building(float x, float y, int width, int height, float scrollSpeed, float groundY) {
		super(x, y, width, height, scrollSpeed);
		
		r = new Random();
		barUp = new Rectangle();
		this.groundY = groundY;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		barUp.set(position.x, position.y, width, height);
	}
	
	@Override
	public void reset(float newX) {
		super.reset(newX);
		height = r.nextInt(90) + 15;
		isScored = false;
	}
	
	public void onRestart(float x, float scrollSpeed) {
        velocity.x = scrollSpeed;
        reset(x);
    }
	
	public Rectangle getBarUp() {
		return barUp;
	}

	public boolean isScored() {
		return isScored;
	}
	
	public void setScored(boolean b) {
		isScored = b;
	}
}
