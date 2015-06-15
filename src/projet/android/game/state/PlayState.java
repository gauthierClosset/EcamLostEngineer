package projet.android.game.state;

import java.util.ArrayList;

import projet.android.framework.util.Painter;
import projet.android.framework.util.UIButton;
import projet.android.game.Assets;
import projet.android.game.GameMainActivity;
import projet.android.game.model.Asteroid;
import projet.android.game.model.Galaxy;
import projet.android.game.model.Player;
import projet.android.game.model.Projectile;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;

public class PlayState extends State {
	
	private Player player;
	public static ArrayList<Asteroid> blocks;
	public static ArrayList<Projectile> projectiles;
	private Galaxy galaxy;

	private int playerScore = 0;

	private static final int BLOCK_HEIGHT = Assets.asteroid.getHeight() - 30;
	private static final int BLOCK_WIDTH = Assets.asteroid.getWidth() -30;
	private int blockSpeed = -100;

	private static final int PLAYER_WIDTH = Assets.spaceship.getWidth();
	private static final int PLAYER_HEIGHT = Assets.spaceship.getHeight();

	private float recentTouchY;

	// Boolean to keep track of game pauses.
	private boolean gamePaused = false;
	// String displayed when paused;
	private String pausedString = "Game Paused. Tap to resume.";

	// Declare new UIButton
	private UIButton pauseButton;
	private UIButton shootButton;
	
	private float yDownTouch = 0;
	private float yCurrrentMoveTouch = 0;
	
	@Override
	public void init() {
		player = new Player(60, GameMainActivity.GAME_HEIGHT/2 - PLAYER_HEIGHT/2, PLAYER_WIDTH, PLAYER_HEIGHT);
		blocks = new ArrayList<Asteroid>();
		galaxy = new Galaxy(800, 100);
		for (int i = 0; i < 8; i++) {
			Asteroid b = new Asteroid(i * 150, GameMainActivity.GAME_HEIGHT - 95, BLOCK_WIDTH, BLOCK_HEIGHT);
			blocks.add(b);
		}

		pauseButton = new UIButton(752, 0, 800, 48, Assets.pause, Assets.pauseDown);
		shootButton = new UIButton(700, 350, 800, 450, Assets.shoot, Assets.shootDown);
		
		projectiles = new ArrayList<Projectile>();
	}

	// Overrides onPause() from State.
	// Called when Activity is pausing.
	@Override
	public void onPause() {
		gamePaused = true;
	}

	@Override
	public void update(float delta) {
		// If game is paused, do not update anything.
		if (gamePaused) {
			return;
		}

		if (!player.isAlive()) {
			setCurrentState(new GameOverState(playerScore / 100));
		}
		playerScore += 1;
		if (playerScore % 500 == 0 && blockSpeed > -280) {
			blockSpeed -= 10;
		}
		galaxy.update(delta);
		player.update(0);
		
		
		projectiles = player.getProjectiles();
		updateBlocks(delta);
		updateProjectiles();
	}

	private void updateBlocks(float delta) {
		for (int i = 0; i < blocks.size(); i++) {
			Asteroid b = blocks.get(i);
			b.update(delta, blockSpeed);
			if (b.isVisible()) {
				if (Rect.intersects(b.getRect(), player.getRect())) {
					b.onCollide(player);
				} else {
					// DO NOTHING
				}
			}
		}
	}
	
	private void updateProjectiles() {
		for (int j = 0; j < projectiles.size(); j++) {
			Projectile p = projectiles.get(j);
			p.update();
			for (int i = 0; i < blocks.size(); i++) {
				Asteroid b = blocks.get(i);
				if(Rect.intersects(p.getRect(), b.getRect()) == true && projectiles.size() != 0) {
					Log.d("DEBUG_TAG", "COLLISION DETECTED");
					p.die();
					b.die();
					playerScore +=5 ;
				}
			}
		}
	}

	@Override
	public void render(Painter g) {
		g.drawImage(Assets.space, 0, 0);
		renderPlayer(g);
		renderBlocks(g);
		renderGalaxy(g);
		renderProjectiles(g);
		renderScore(g);
		shootButton.render(g);

		// If game is Paused, draw additional UI elements:
		if (gamePaused) {
			// ARGB is used to set an ARGB color.
			// See note accompanying listing 10.05.
			g.setColor(Color.argb(153, 0, 0, 0));
			g.fillRect(0, 0, GameMainActivity.GAME_WIDTH,GameMainActivity.GAME_HEIGHT);
			g.drawString(pausedString, 235, 240);
		} else {			
			// Draw pauseButton if not paused.
			pauseButton.render(g);
		}
	}

	private void renderScore(Painter g) {
		g.setFont(Typeface.SANS_SERIF, 25);
		g.setColor(Color.GRAY);
		g.drawString("" + playerScore / 100, 20, 30);
	}

	private void renderPlayer(Painter g) {
		g.drawImage(Assets.spaceship, (int) player.getX(),(int) player.getY());
	}

	
	private void renderBlocks(Painter g) {
		for (int i = 0; i < blocks.size(); i++) {
			Asteroid b = blocks.get(i);
			if (b.isVisible()) {
				g.drawImage(Assets.asteroid, (int) b.getX(), (int) b.getY());
			}
		}
	}
	
	private void renderProjectiles(Painter g) {
		for (int i = 0; i < projectiles.size(); i++) {
			Projectile p = projectiles.get(i);
			if(p.isVisible()) {
				g.setColor(Color.GREEN);
				g.fillRect(Math.round(p.getX()), Math.round(p.getY()), 10, 5);
			}
		}
	}

	private void renderGalaxy(Painter g) {
		g.drawImage(Assets.galaxie, (int) galaxy.getX(), (int) galaxy.getY());
	}

	@Override
	public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
			
		// get pointer index from the event object
	    int pointerIndex = e.getActionIndex();

	    // get pointer ID
	    int pointerId = e.getPointerId(pointerIndex);

	    // get masked (not specific to a pointer) action
	    int maskedAction = e.getActionMasked();
		
	    switch (maskedAction) {
	    	case MotionEvent.ACTION_DOWN: {
	    		PointF f = new PointF();
	    		f.x = e.getX(pointerIndex);
	    		f.y = e.getY(pointerIndex);
	    		scaledX = Math.round(f.x);
	        	scaledY = Math.round(f.y);
	        	shootButton.onTouchDown(scaledX, scaledY);
	        	if(shootButton.isPressed(scaledX, scaledY)) {
	        		//mActiveButtonDownPointers.put(pointerId, f);
	        	} else {
	        		//recentTouchY = scaledY;
	        		pauseButton.onTouchDown(scaledX, scaledY);
	        		//mActiveDownPointers.put(pointerId, f);
	                yDownTouch = scaledY;
	                //Log.d("DEBUG_TAG", "DOWNTOUCH : " + yDownTouch);
	        	}
	    		break;
	    	}
	    	
	    	case MotionEvent.ACTION_UP: {
	    		PointF f = new PointF();
	    		f.x = e.getX(pointerIndex);
	    		f.y = e.getY(pointerIndex);
	    		scaledX = Math.round(f.x);
	        	scaledY = Math.round(f.y);
	        	if(shootButton.isPressed(scaledX, scaledY)) {
	        		player.shoot();
	        		shootButton.cancel();
	        	} else {
	        		yCurrrentMoveTouch = 0;
	        		yDownTouch = 0;
	        		if (gamePaused) {
	    				gamePaused = false;
	    				// Must cancel pauseButton before onTouch() method returns.
	    				pauseButton.cancel();
	    				return true;
	    			}

	    			// If Touch Up triggers PauseButton, pause the game.
	    			if (pauseButton.isPressed(scaledX, scaledY)) {
	    				pauseButton.cancel();
	    				gamePaused = true;
	    			} else {
	    				pauseButton.cancel();
	    			}
	        	}
	    		break;
	    	}
	    	
	    	case MotionEvent.ACTION_MOVE: {
	    		if(shootButton.isPressed(scaledX, scaledY) == false) {
	    			yCurrrentMoveTouch = e.getY(pointerIndex);
	    			float diffY = yCurrrentMoveTouch - yDownTouch;
	    			player.update(diffY);
	    		} else {
	    			//DO NOTHING
	    		}
	    	}
	    }
	    
		return true;
	}
}