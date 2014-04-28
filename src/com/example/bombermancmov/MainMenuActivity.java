package com.example.bombermancmov;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity implements OnClickListener{
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.menu);
	    
	    Button newSingleGame = (Button)findViewById(R.id.bSingle);
	    Button newMultiGame = (Button)findViewById(R.id.bMulti);
	    Button options = (Button)findViewById(R.id.bOptions);
	    Button exit = (Button)findViewById(R.id.bExit);
	    
	    newSingleGame.setOnClickListener(this);
	    newMultiGame.setOnClickListener(this);
	    options.setOnClickListener(this);
	    exit.setOnClickListener(this);
	}

	public void onClick(View view) {
	    //check which button was clicked with its id
	    switch(view.getId()) {
	        case R.id.bExit:
	            finish();
	            break;
	        case R.id.bSingle:
	            Intent intent = new Intent(this, SingleGameActivity.class);
	            this.startActivity(intent);
	            break;	        
	    }
	}
}
