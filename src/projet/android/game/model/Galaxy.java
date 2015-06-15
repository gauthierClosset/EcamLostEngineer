package projet.android.game.model;

import projet.android.framework.util.RandomNumberGenerator;

public class Galaxy {
	private float x, y;
	private static final int VEL_X = -15;

	public Galaxy(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void update(float delta) {
		x += VEL_X * delta;
		if (x <= -200) {
			// Reset to the right
			x += 1000;
			y = RandomNumberGenerator.getRandIntBetween(0, 800);
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}