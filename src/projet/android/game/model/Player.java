package projet.android.game.model;

import java.util.ArrayList;

import projet.android.game.Assets;
import android.graphics.Rect;
import android.util.Log;

public class Player {
	
	private float x, y;
	private int width, height;
	private Rect rect, duckRect, ground;
	private boolean isAlive;
	private static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	public Player(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		rect = new Rect();
		isAlive = true;
	}

	public void update(float delta) {
		if(delta < 0 && y > 0) {
			y += -4;
		} else if (delta > 0 && y < 380){
			y += 4;
		} else {
			// DO NOTHING
		}
		updateRects();
	}

	public void updateRects() {
		rect.set((int) x , (int) y, (int) x + width, (int) y + height);
	}
	
	public void shoot() {
		Log.d("DEBUG_TAG","SHOOT!!"); 
		Projectile p = new Projectile(x + width, y + (height/2));
		projectiles.add(p);
	}

	public void die() {
		Assets.playSound(Assets.hitID);
		isAlive = false;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Rect getRect() {
		return rect;
	}

	public Rect getDuckRect() {
		return duckRect;
	}

	public Rect getGround() {
		return ground;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

}