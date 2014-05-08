package com.example.bombermancmov.model.component;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.SurfaceView;

import com.example.bombermancmov.R;

public class SoundComponent {
	
	private MediaPlayer mPlayer;
	private SoundPool sp;
	private int explosionSoundId;
	
	public SoundComponent(SurfaceView surfaceView) {
		mPlayer = MediaPlayer.create(surfaceView.getContext(), R.raw.sound_bomb_1);
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		explosionSoundId = sp.load(surfaceView.getContext(), R.raw.sound_bomb_1, 1);
	}
	public void play() {
		sp.play(explosionSoundId, 1, 1, 0, 0, 1);
		mPlayer.start();
	}
}
