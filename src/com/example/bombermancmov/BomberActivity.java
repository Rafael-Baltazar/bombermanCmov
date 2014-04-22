package com.example.bombermancmov;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class BomberActivity extends ActionBarActivity {
	
	private static final String TAG = GameLoopThread.class.getSimpleName();
	private FrameLayout frm;
	private Button leftButton, rightButton, upButton, downButton, bombButton;
	private TextView playerNameTextView, playerScoreTextView, timeLeftTextView, numPlayersTextView;
	private MainGamePanel mGamePanel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        // save for posterior use
        playerNameTextView =(TextView)findViewById(R.id.playerName);
        playerScoreTextView =(TextView)findViewById(R.id.playerScore);
        timeLeftTextView =(TextView)findViewById(R.id.timeLeft);
        numPlayersTextView = (TextView)findViewById(R.id.numberPlayers);
        
	    setOnClickListenersToButtons();
	    
		frm=(FrameLayout)findViewById(R.id.frameLayout);
		mGamePanel = new MainGamePanel(this);
	    frm.addView(mGamePanel);
	    
        Log.d(TAG, "View added");
	}

	private void setOnClickListenersToButtons() {
		leftButton=(Button)findViewById(R.id.buttonLeft);
	    leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGamePanel.doAction(0);
			}
		});
	    
		upButton=(Button)findViewById(R.id.buttonUp);
	    upButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGamePanel.doAction(1);
			}
		});
	    
	    downButton=(Button)findViewById(R.id.buttonDown);
	    downButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGamePanel.doAction(2);
			}
		});
	    
	    rightButton=(Button)findViewById(R.id.buttonRight);
	    rightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGamePanel.doAction(3);
			}
		});
	    
	    bombButton=(Button)findViewById(R.id.buttonBomb);
	    bombButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGamePanel.doAction(4);
			}
		});
	}
	
	public void setPlayerName(String playerName){
		playerNameTextView.setText(playerName);
	}
	public void setPlayerScore(float playerScore){
		playerScoreTextView.setText(Float.toString(playerScore));
	}
	public void setTimeLeft(float timeLeft){
		timeLeftTextView.setText(Float.toString(timeLeft));
	}
	public void setNumPlayers(int numPlayers){
		numPlayersTextView.setText(Integer.toString(numPlayers));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
