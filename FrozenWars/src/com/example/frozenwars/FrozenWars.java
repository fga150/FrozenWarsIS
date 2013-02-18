package com.example.frozenwars;

import com.badlogic.gdx.backends.android.AndroidApplication;
import Application.LaunchFrozenWars;

import android.os.Bundle;
import android.view.Menu;

public class FrozenWars extends AndroidApplication{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      initialize(new LaunchFrozenWars(),false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_frozen_wars, menu);
		return true;
	}

}
