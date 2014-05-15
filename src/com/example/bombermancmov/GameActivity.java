package com.example.bombermancmov;

import java.io.IOException;
import java.util.Locale;

import android.content.res.AssetManager;
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

import com.example.bombermancmov.model.Level;
import com.example.bombermancmov.model.LevelLoader;

public class GameActivity extends ActionBarActivity {

	private static final String TAG = GameActivity.class.getSimpleName();
	private FrameLayout frm;
	private MainGamePanel mGamePanel;
	private PlayerInput playerInput;
	private ImageButton upButton, leftButton, rightButton, downButton,
			pauseButton, bombButton, quitButton;
	private boolean btnPaused; // true if button has pause-symbol, false if
								// button has resume-symbol
	private Level level;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// updater
		TextView nameTextView = (TextView) findViewById(R.id.playerName);
		TextView scoreTextView = (TextView) findViewById(R.id.playerScore);
		TextView timeLeftTextView = (TextView) findViewById(R.id.timeLeft);
		TextView numPlayersTextView = (TextView) findViewById(R.id.numberPlayers);
		StatusScreenUpdater statusScreen = new StatusScreenUpdater(
				nameTextView, scoreTextView, timeLeftTextView,
				numPlayersTextView, this);

		frm = (FrameLayout) findViewById(R.id.frameLayout);
		pauseButton = (ImageButton) findViewById(R.id.buttonPause);
		this.btnPaused = true;
		setOnClickListenersToButtons();

		// TODO flags bleh dX
		//Get Extras
		String playerName = getIntent().getStringExtra("playerName");
		statusScreen.setPlayerName(playerName);
		boolean isSinglePlayer = getIntent().getBooleanExtra("isLocal", true);
		boolean isMaster = getIntent().getBooleanExtra("isMaster", true);
		String masterIp = getIntent().getStringExtra("masterIp");
		
		//Load level from extra
		String fileName = getIntent().getStringExtra("levelName").replace(" ","").toLowerCase(Locale.ENGLISH) + ".dat";
		AssetManager am = getAssets();
		try {
			this.level = LevelLoader.loadLevel(am.open(fileName));
		} catch (IOException e) {
			this.level = LevelLoader.loadLevel(null);
		}		
		
		mGamePanel = new MainGamePanel(this, statusScreen, level, isSinglePlayer, isMaster, masterIp);
		playerInput = mGamePanel.getGame().getPlayerInput();
		
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
		
		quitButton = (ImageButton) findViewById(R.id.buttonQuit);
		quitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		pauseButton = (ImageButton) findViewById(R.id.buttonPause);
		pauseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btnPaused) {
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
	}
}
