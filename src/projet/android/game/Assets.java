package projet.android.game;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class Assets {
	private static SoundPool soundPool;
	public static Bitmap welcome, block, scoreDown, score, startDown, start, 
			pause, pauseDown, shoot, shootDown, spaceship, space, asteroid, galaxie;
	public static int hitID, onJumpID;
	private static MediaPlayer mediaPlayer;

	public static void load() {
		welcome = loadBitmap("welcome.png", false);
		block = loadBitmap("block.png", false);
		scoreDown = loadBitmap("score_button_down.png", true);
		score = loadBitmap("score_button.png", true);
		startDown = loadBitmap("start_button_down.png", true);
		start = loadBitmap("start_button.png", true);
		pauseDown = loadBitmap("pause_button_down.png", true);
		pause = loadBitmap("pause_button.png", true);
		shoot = loadBitmap("shoot_button.png", true);
		shootDown = loadBitmap("shoot_button_down.png", true);
		spaceship = loadBitmap("spaceship5.png", true);
		space = loadBitmap("space.png", false);
		asteroid = loadBitmap("asteroid2.png", true);
		galaxie = loadBitmap("galaxie.png", true);
	}

	public static void onResume() {
		hitID = loadSound("hit.wav");
		onJumpID = loadSound("onjump.wav");
		playMusic("bgmusic.mp3", true);
	}

	public static void onPause() {
		if (soundPool != null) {
			soundPool.release();
			soundPool = null;
		}

		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer = null;
		}
	}

	private static Bitmap loadBitmap(String filename, boolean transparency) {
		InputStream inputStream = null;
		try {
			inputStream = GameMainActivity.assets.open(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Options options = new Options();
		if (transparency) {
			options.inPreferredConfig = Config.ARGB_8888;
		} else {
			options.inPreferredConfig = Config.RGB_565;
		}
		Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
				options);
		return bitmap;
	}

	@SuppressWarnings("deprecation")
	private static int loadSound(String filename) {
		int soundID = 0;
		if (soundPool == null) {
			soundPool = new SoundPool(25, AudioManager.STREAM_MUSIC, 0);
		}
		try {
			soundID = soundPool.load(GameMainActivity.assets.openFd(filename),
					1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return soundID;
	}

	public static void playSound(int soundID) {
		if (soundPool != null) {
			soundPool.play(soundID, 1, 1, 1, 0, 1);
		}
	}

	public static void playMusic(String filename, boolean looping) {
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
		}
		try {
			AssetFileDescriptor afd = GameMainActivity.assets.openFd(filename);
			mediaPlayer.setDataSource(afd.getFileDescriptor(),
					afd.getStartOffset(), afd.getLength());
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.prepare();
			mediaPlayer.setLooping(looping);
			mediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}