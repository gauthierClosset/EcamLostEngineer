package projet.android.game.model;

import projet.android.framework.util.RandomNumberGenerator;
import android.graphics.Rect;

public class Asteroid {
	private float x, y;
	private int width, height;
	private Rect rect;
	private boolean visible;

	public Asteroid(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		rect = new Rect((int) x + 40, (int) y + 40, (int) x + width, (int) y + height - 40);
		visible = false;
	}

	public void update(float delta, float velX) {
		x += velX * delta;
		updateRect();
		if (x <= -50) {
			reset();
		}
	}

	public void updateRect() {
		rect.set((int) x + 40, (int) y + 40, (int) x + width, (int) y + height);
	}

	public void reset() {
		visible = true;
		int random = RandomNumberGenerator.getRandInt(370);
		y = random;
		x += 1000;
		updateRect();
	}

	public void onCollide(Player p) {
		visible = false;
		p.die();
	}
	
	//public void onCollide(Block b) {
		//visible = false;
		//die();
	//}
	
	public void die() {
		visible = false;
		int random = RandomNumberGenerator.getRandInt(370);
		y = random;
		x += 1000;
		updateRect();
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public boolean isVisible() {
		return visible;
	}

	public Rect getRect() {
		return rect;
	}
}