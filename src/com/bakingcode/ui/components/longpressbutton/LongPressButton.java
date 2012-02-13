/*
 * Copyright 2012 Christian Panadero Martinez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bakingcode.ui.components.longpressbutton;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;

/**
 * LongPressButton class detects the onLongClick and executes users-code defined in the OnDownListener every X milliseconds.
 * This button is necessary when managing long list values and you need a button to iterate, for example in a value picker.
 *  
 * @author <a href="http://bakingcode.com">Christian Panadero Martinez @ BakingCode.com</a>
 */
public class LongPressButton extends Button implements OnLongClickListener {

	private final static long LAST_DELAY_DEFAULT_VALUE = -1;
	
	/**
	 * Flag of component, controls when the button is pressed
	 */
	private boolean isDown;
	
	/**
	 * Listener, For more information: {@link OnLongDownListener}
	 */
	private OnLongDownListener listener;
	
	/**
	 * Handler used for execute the runnable at a delayed time
	 */
	private final Handler handlerDown = new Handler();
	
	/**
	 * Number of milliseconds to re-execute the runnable when the user is pressing button
	 */
	private long millisecondsToExecute = 1000;
	
	/**
	 * Private var to check last executed action
	 */
	private long millisecondsLastDelay = LAST_DELAY_DEFAULT_VALUE;
	
	/**
	 * The number of milliseconds for execute with 2x speed, set this var equals to millisecondsToExecute for deactivate this feature
	 */
	private long millisecondsForSuperSpeed = 500; 
	
	/**
	 * Runnable for executing users-code when the user has the finger pressing button
	 */
	private final Runnable executeOnDown = new Runnable() {
		
		@Override
		public void run() {

			if (isDown && listener != null) {
				listener.executeOnDown(LongPressButton.this);
			}
			
			//  If delayed for more than the defined "super speed milliseconds", set 2x time execution
			long milliExecute = System.currentTimeMillis() - millisecondsLastDelay > (millisecondsForSuperSpeed + millisecondsToExecute) ? millisecondsToExecute / 2 : millisecondsToExecute;
			
			handlerDown.postDelayed(this, milliExecute);
		}
		
	};
	
	/*
	 * Constructors
	 */
	public LongPressButton(Context context) {
		
		super(context);
		init();
		
	}

	public LongPressButton(Context context, AttributeSet attrs, int defStyle) {
		
		super(context, attrs, defStyle);
		init();
		
	}
	
	public LongPressButton(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		init();
		
	}
	
	/**
	 * Called when the Button need to be initializated
	 */
	private void init() {
		this.setOnLongClickListener(this);
	}

	/*
	 * User interaction
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		cancelLongPressHandler(event.getAction());
		return super.onTouchEvent(event);
		
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		
		cancelLongPressHandler(event.getAction());
		return super.onTrackballEvent(event);
		
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		cancelLongPressHandler(keyCode);
		return super.onKeyUp(keyCode, event);
		
	}
	
	@Override
	public boolean onLongClick(View v) {
		
		isDown = true;
		millisecondsLastDelay = System.currentTimeMillis();
		handlerDown.post(executeOnDown);
		return true;
		
	}

	/*
	 * Cancelations
	 */
	private void cancelLongPressHandler(int event) {

		switch (event) {
		
		// Motion Events
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			
		// Key Events
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			
			isDown = false;
			millisecondsLastDelay = LAST_DELAY_DEFAULT_VALUE;
			handlerDown.removeCallbacks(executeOnDown);
			
			if (listener != null) {
				listener.onCancelLongPress(this);
			}
			
			break;
			
		}

	}

	/*
	 * Getters & setters
	 */
	/**
	 * Flag of component, controls when the button is pressed
	 * @return if the user has the finger long-pressing the button
	 */
	public boolean isDown() {
		
		return isDown;
		
	}

	/**
	 * Listener, {@link OnDownListener}
	 * @return Gets the listener
	 */
	public OnLongDownListener getOnDownListener() {
		
		return listener;
		
	}

	/**
	 * Sets the listener, {@link OnDownListener}
	 * @param listener Sets the listener
	 */
	public void setOnDownListener(OnLongDownListener listener) {
		
		this.listener = listener;
		
	}

	/**
	 * Gets the number of milliseconds to re-execute the runnable when the user is pressing button
	 * @return The milliseconds to execute 
	 */
	public long getMillisecondsToExecute() {
		
		return millisecondsToExecute;
		
	}

	/**
	 * Sets the number of milliseconds to re-execute the runnable when the user is pressing button
	 * @param millisecondsToExecute sets number of milliseconds
	 */
	public void setMillisecondsToExecute(long millisecondsToExecute) {
		
		this.millisecondsToExecute = millisecondsToExecute;
		
	}
	
	/**
	 * Gets the number of seconds to re-execute the runnable when the user is pressing button
	 * @return The seconds to execute 
	 */
	public long getSecondsToExecute() {
		
		return millisecondsToExecute / 1000;
		
	}

	/**
	 * Sets the number of seconds to re-execute the runnable when the user is pressing button
	 * @param secondsToExecute sets number of seconds
	 */
	public void setSecondsToExecute(long secondsToExecute) {
		
		this.millisecondsToExecute = (millisecondsToExecute * 1000);
		
	}

	/**
	 * Gets the number of milliseconds for execute with 2x speed, set this var equals to millisecondsToExecute for deactivate this feature
	 * @return number of milliseconds
	 */
	public long getMillisecondsForSuperSpeed() {
		
		return millisecondsForSuperSpeed;
		
	}

	/**
	 * Sets the number of milliseconds for execute with 2x speed, set this var equals to millisecondsToExecute for deactivate this feature
	 * @param millisecondsForSuperSpeed number of milliseconds
	 */
	public void setMillisecondsForSuperSpeed(long millisecondsForSuperSpeed) {
		
		this.millisecondsForSuperSpeed = millisecondsForSuperSpeed;
		
	}

}
