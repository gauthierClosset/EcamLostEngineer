package projet.android.game.model;

import javax.security.auth.Destroyable;

import android.graphics.Rect;

public class Projectile {
	private float x, y, speedX;
	private static boolean visible;
	public static Rect r;
	
	public Projectile(float startX, float startY) {
		x = startX;
		y = startY;
		speedX = 3;
		visible = true;
		
		r = new Rect(Math.round(startX),Math.round(startY), Math.round(startX) + 10, Math.round(startY) - 5);
	}
	

	public void update(){
		x += speedX;
		updateRect();
	}
	
	public void updateRect() {
		r.set(Math.round(x), Math.round(y), Math.round(x) + 10, Math.round(x) - 5);
	}
	
	public void die() {
		visible = false;
		x += 10000;
		updateRect();
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public Rect getRect() {
		return r;
	}
}
