package com.example.bombermancmov;

import java.lang.reflect.Field;

import com.example.bombermancmov.model.Level;
import com.example.bombermancmov.model.LevelLoader;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameActivity extends ActionBarActivity {

	private static final String TAG = GameLoopThread.class.getSimpleName();
	private FrameLayout frm;
	private ImageButton upButton, leftButton, rightButton, downButton, pauseButton, bombButton, quitButton;
	private boolean btnPaused; //true if button has pause-symbol, false if button has resume-symbol

	private MainGamePanel mGamePanel;
	private PlayerInput playerInput;
	
	private Level level;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// save for posterior use
		TextView nameTextView = (TextView) findViewById(R.id.playerName);
		TextView scoreTextView = (TextView) findViewById(R.id.playerScore);
		TextView timeLeftTextView = (TextView) findViewById(R.id.timeLeft);
		TextView numPlayersTextView = (TextView) findViewById(R.id.numberPlayers);
		
		this.btnPaused = true;
		
		frm = (FrameLayout) findViewById(R.id.frameLayout);
		
		setOnClickListenersToButtons();
		
		StatusScreenUpdater statusScreen = new StatusScreenUpdater(nameTextView,
				scoreTextView, timeLeftTextView, numPlayersTextView, this);
		
		//get extras
		String playerName = getIntent().getStringExtra("playerName");
		boolean isLocal = getIntent().getBooleanExtra("isLocal", true);
		int playerId = getIntent().getIntExtra("playerId", 0);		
		//Load level from extra
		this.level = LevelLoader.loadLevel(getIntent().getStringExtra("levelName"), getResources().openRawResource(R.raw.level1));
		
		statusScreen.setPlayerName(playerName);
		if (isLocal) {
			mGamePanel = new MainGamePanel(this, statusScreen, level, true); //true for isSinglePlayer
			playerInput = new LocalPlayerInput(mGamePanel);
		} else {
			mGamePanel = new MainGamePanel(this, statusScreen, level, false);
			playerInput = new RemotePlayerInput(mGamePanel.getGame(), playerId);
		}
		frm.addView(mGamePanel);
		
		Log.d(TAG, "View added");
	}

	private void setOnClickListenersToButtons() {
		leftButton = (ImageButton) findViewById(R.id.buttonLeft);
		leftButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					playerInput.tryMoveLeft();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					playerInput.tryStop();
				}
				return true;
			}
		});

		upButton = (ImageButton) findViewById(R.id.buttonUp);
		upButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					playerInput.tryMoveUp();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					playerInput.tryStop();
				}
				return true;
			}

		});

		downButton = (ImageButton) findViewById(R.id.buttonDown);
		downButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					playerInput.tryMoveDown();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					playerInput.tryStop();
				}

				return true;
			}
		});

		rightButton = (ImageButton) findViewById(R.id.buttonRight);
		rightButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					playerInput.tryMoveRight();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					playerInput.tryStop();
				}
				return true;
			}
		});

		bombButton = (ImageButton) findViewById(R.id.buttonBomb);
		bombButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playerInput.placeBomb();
			}
		});
		
		pauseButton = (ImageButton) findViewById(R.id.buttonPause);
		pauseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(btnPaused) {
					mGamePanel.pauseThread();
					pauseButton.setImageResource(R.drawable.button_resume);
					btnPaused = false;
				} else {
					mGamePanel.resumeThread();
					pauseButton.setImageResource(R.drawable.button_pause);
					btnPaused = true;
				}			
			}
		});
		
		quitButton = (ImageButton) findViewById(R.id.buttonQuit);
		quitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGamePanel.quitThread();
				finish();
			}
		});
	}
}
