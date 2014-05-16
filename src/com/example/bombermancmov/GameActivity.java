package com.example.bombermancmov;

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import com.example.bombermancmov.model.Game;
import com.example.bombermancmov.model.Level;
import com.example.bombermancmov.model.LevelLoader;
import com.example.bombermancmov.model.Resource;
import com.example.bombermancmov.model.component.MasterNetworkComponent;
import com.example.bombermancmov.model.component.NullMasterNetworkComponent;
import com.example.bombermancmov.model.component.NullPeerNetworkComponent;
import com.example.bombermancmov.model.component.PeerNetworkComponent;
import com.example.bombermancmov.wifi.AcceptNewPeersThread;
import com.example.bombermancmov.wifi.CommandRequest;
import com.example.bombermancmov.wifi.CommandRequestUtil;
import com.example.bombermancmov.wifi.commands.Command;
import com.example.bombermancmov.wifi.commands.DroidMovementCommand;
import com.example.bombermancmov.wifi.commands.NopCommand;
import com.example.bombermancmov.wifi.commands.PlaceBombCommand;
import com.example.bombermancmov.wifi.commands.TryMoveCommand;
import com.example.bombermancmov.wifi.commands.TryStopCommand;
import com.example.bombermancmov.wifi.commands.UpdateTimeCommand;

public class GameActivity extends ActionBarActivity {
	//Tag for notifications
	private static final String TAG = GameActivity.class.getSimpleName();	
	/* UI ELEMENTS */
	//Buttons
	private ImageButton upButton;
	private ImageButton leftButton;
	private ImageButton rightButton;
	private ImageButton downButton;
	private ImageButton bombButton; 
	private ImageButton pauseButton;
	private ImageButton quitButton;
	//Frame & Panel
	private FrameLayout frm;
	private MainGamePanel mGamePanel;
	//Flags
	private boolean btnPaused; // true if button has pause-symbol, false if
							   // button has resume-symbol
	//Updater for Status
	private StatusScreenUpdater statusScreenUpdater;	
	
	/* LOGIC ELEMENTS */
	private PlayerInput playerInput;
	private Level level;
	private Game game;
	//Threads
	private GameLoopThread gameLoopThread;
	private AcceptNewPeersThread acceptPeersThread;
	//Network
	private MasterNetworkComponent masterNetComp;
	private PeerNetworkComponent peerNetComp;
	private Map<String, Command> mCommands;
	//Flags
	private boolean isSinglePlayer;
	private boolean isMaster;
	//Others
	private String fileName;	
	private Resource mResource;
	public String masterHost;	
	//Global flags
	public static final int ROUND_TIME = 1000; // milliseconds
	public static final int MASTER_PORT = 10001;
	private static final int MASTER_ID = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* GUI */
		//Set view
		setContentView(R.layout.activity_game);
		//Get elements
		TextView nameTextView = (TextView) findViewById(R.id.playerName);
		TextView scoreTextView = (TextView) findViewById(R.id.playerScore);
		TextView timeLeftTextView = (TextView) findViewById(R.id.timeLeft);
		TextView numPlayersTextView = (TextView) findViewById(R.id.numberPlayers);
		//set gui flags
		this.btnPaused = true;
		//Set status updater, frame & buttons
		statusScreenUpdater = new StatusScreenUpdater(nameTextView, scoreTextView, timeLeftTextView,numPlayersTextView, this);
		frm = (FrameLayout) findViewById(R.id.frameLayout);
		setOnClickListenersToButtons();		
		
		/* EXTRAS */
		statusScreenUpdater.setPlayerName(getIntent().getStringExtra("playerName"));
		isSinglePlayer = getIntent().getBooleanExtra("isLocal", true);
		isMaster = getIntent().getBooleanExtra("isMaster", true);
		masterHost = getIntent().getStringExtra("masterIp");
		fileName = getIntent().getStringExtra("levelName").replace(" ","").toLowerCase(Locale.ENGLISH) + ".dat";
		// Handle Flags
		if (isSinglePlayer) {
			masterNetComp = new NullMasterNetworkComponent();
			peerNetComp = new NullPeerNetworkComponent();
		} else {
			masterNetComp = new MasterNetworkComponent();
			peerNetComp = new PeerNetworkComponent();
		}	
		//Load Resources: level & sound
		AssetManager am = getAssets();
		try {
			this.level = LevelLoader.loadLevel(am.open(fileName));
		} catch (IOException e) {
			this.level = LevelLoader.loadLevel(null);
		}	
		
		//Panel
		mGamePanel = new MainGamePanel(this);		
		//Start game
		game = new Game(mGamePanel.getResource(), level, isSinglePlayer);
		playerInput = this.game.getPlayerInput();
		//Network commands
		mCommands = initCommands();		
		
		frm.addView(mGamePanel);
		Log.d(TAG, "View added");
	}
	
	/** THREAD handling */
	public void startThread() {
		Log.d(TAG, "Thread starts");
		// create & start the game loop thread
		gameLoopThread = new GameLoopThread(this);
		gameLoopThread.start();
		// create master thread
		if (isMaster) {
			try {
				masterNetComp.createServerSocket(MASTER_PORT);
				acceptPeersThread = new AcceptNewPeersThread(masterNetComp);
				acceptPeersThread.start();
			} catch (IOException e) {
				Log.e(TAG, "IO new server socket: " + e.getMessage());
			}
		}
	}
	
	public void shutDownThreads() {
		gameLoopThread.setRunning(false);		
		shutDown(gameLoopThread);
		
		Log.d(TAG, "Game loop thread was shut down cleanly");

		if (isMaster && acceptPeersThread != null) {
			acceptPeersThread.interrupt();
			try {
				masterNetComp.closeServerSocket();
				shutDown(acceptPeersThread);
				Log.d(TAG, "Accept peers thread was shut down cleanly");
			} catch (IOException e) {
				Log.e(TAG, "IO closing sockets: " + e.getMessage());
			}
		}
	}
	
	private void shutDown(Thread thread) {
		while (true) {
			try {
				thread.join();
				break;
			} catch (InterruptedException e) {
				Log.d(TAG, "Surface destroyed: " + e.getMessage());
			}
		}
	}
	
	public void pauseThread() {
		gameLoopThread.pause();
	}

	public void resumeThread() {
		gameLoopThread.unPause();
	}
	
	/** UPDATE handling */
	public void update(long timePassed) {
		if (isMaster) {
			updateAsMaster(timePassed);
		} else {
			updateAsPeer();
		}
		updateStatusScreen();
	}
	
	private void updateStatusScreen() {
		this.statusScreenUpdater.runOnUiThread(new Runnable() {
			@Override
			public void run() {	
				statusScreenUpdater.setPlayerScore((int)game.getPlayerByNumber(game.getPlayerInput().getPlayerId()).getPoints());
				statusScreenUpdater.setTimeLeft((int)(game.getTimeLeft() / 1000));
				statusScreenUpdater.setNumPlayers(game.getLeftOpponents());
				
				if(game.isFinished() || !game.getPlayerByNumber(game.getPlayerInput().getPlayerId()).isAlive()) {
					if(mGamePanel.getFinishDialog() == null) {
						mGamePanel.buildEnddialog();
					}
				} 
			}
		});
	}
		
	/**
	 * Receive player inputs from peers and itself and executes them. Then,
	 * updates game and, finally, sends game updates to peers.
	 * 
	 * @param timePassed
	 */
	private void updateAsMaster(long timePassed) {
		List<CommandRequest> cmdRequests;
		try {
			cmdRequests = masterNetComp.receiveCommandRequests();
			CommandRequestUtil.executeCommandRequests(cmdRequests, mCommands);
		} catch (OptionalDataException e) {
			Log.e(TAG,
					"OptionalData master receive command requests: "
							+ e.getMessage());
		} catch (ClassNotFoundException e) {
			Log.e(TAG,
					"ClassNotFound master receive command requests: "
							+ e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "IO master receive command requests: " + e.getMessage());
		}
		game.getPlayerInput().setPlayerId(MASTER_ID);
		cmdRequests = game.getPlayerInput().consumeCommandRequests();
		CommandRequestUtil.executeCommandRequests(cmdRequests, mCommands);
		
		game.update(timePassed);
		
		cmdRequests = CommandRequestUtil.extractCommandRequests(game);
		try {
			masterNetComp.sendCommandRequests(cmdRequests);
		} catch (IOException e) {
			Log.e(TAG, "IO master send command requests: " + e.getMessage());
		}
	}

	/**
	 * First, peer sends its player inputs to master. Then, receives the game
	 * updates from master and executes them.
	 */
	private void updateAsPeer() {
		List<CommandRequest> cmdRequests;
		try {
			requestPlayerIdFromMaster();
			cmdRequests = game.getPlayerInput().consumeCommandRequests();
			try {
				peerNetComp.sendCommandRequests(cmdRequests);
			} catch (IOException e) {
				Log.e(TAG, "IO peer sending cmd requests: " + e.getMessage());
			}
			try {
				cmdRequests = peerNetComp.receiveCommandRequests();
				CommandRequestUtil.executeCommandRequests(cmdRequests, mCommands);
			} catch (OptionalDataException e) {
				Log.e(TAG,
						"OptionalData peer receive command requests: "
								+ e.getMessage());
			} catch (ClassNotFoundException e) {
				Log.e(TAG,
						"ClassNotFound peer receive command requests: "
								+ e.getMessage());
			} catch (IOException e) {
				Log.e(TAG,
						"IO peer receive command requests: " + e.getMessage());
			}
			
		} catch (ClassNotFoundException e) {
			Log.e(TAG,
					"ClassNotFound peer new client socket: " + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "IO peer new client socket: " + e.getMessage());
		}
	}
	
	/**
	 * If not yet connected, connects to client and sets player id.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void requestPlayerIdFromMaster() throws ClassNotFoundException,
			IOException {
		peerNetComp.createClientSocket(masterHost, MASTER_PORT);
		int playerId = peerNetComp.getPlayerId();
		PlayerInput playerInput = game.getPlayerInput();
		playerInput.setPlayerId(playerId);
	}
	
	/** NETWORK handling */
	private Map<String, Command> initCommands() {
		Map<String, Command> commands = new HashMap<String, Command>();
		commands.put(NopCommand.CODE, new NopCommand());
		commands.put(TryMoveCommand.CODE, new TryMoveCommand(game));
		commands.put(TryStopCommand.CODE, new TryStopCommand(game));
		commands.put(PlaceBombCommand.CODE, new PlaceBombCommand(game));
		commands.put(DroidMovementCommand.CODE, new DroidMovementCommand(game));
		commands.put(UpdateTimeCommand.CODE, new UpdateTimeCommand(game));
		return commands;
	}

	/** GUI handling */
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
					pauseThread();
					pauseButton.setImageResource(R.drawable.button_resume);
					btnPaused = false;
				} else {
					resumeThread();
					pauseButton.setImageResource(R.drawable.button_pause);
					btnPaused = true;
				}
			}
		});
	}
	
	/** GETTERS & SETTERS */
	public boolean isSinglePlayer() {
		return isSinglePlayer;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public Resource getmResource() {
		return mResource;
	}
	
	public Game getGame() {
		return game;
	}
	
	public MainGamePanel getmGamePanel() {
		return mGamePanel;
	}
}
