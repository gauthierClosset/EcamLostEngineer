package projet.android.game.state;

import projet.android.framework.util.Painter;
import projet.android.framework.util.UIButton;
import projet.android.game.Assets;
import android.view.MotionEvent;

public class MenuState extends State {
	
	private UIButton playButton, scoreButton;
	
	@Override
	public void init() {
		playButton = new UIButton(150, 330, 318, 389, Assets.start,
				Assets.startDown);
		scoreButton = new UIButton(482, 330, 650, 389, Assets.score,
				Assets.scoreDown);
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void render(Painter g) {
		g.drawImage(Assets.welcome, 0, 0);
		playButton.render(g);
		scoreButton.render(g);
	}

	@Override
	public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			playButton.onTouchDown(scaledX, scaledY);
			scoreButton.onTouchDown(scaledX, scaledY);
		}
		if (e.getAction() == MotionEvent.ACTION_UP) {
			if (playButton.isPressed(scaledX, scaledY)) {
				playButton.cancel();
				setCurrentState(new PlayState());
			} else if (scoreButton.isPressed(scaledX, scaledY)) {
				scoreButton.cancel();
				setCurrentState(new ScoreState());
			} else {
				playButton.cancel();
				scoreButton.cancel();
			}
		}
		return true;
	}
}