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
package com.bakingcode.ui.components;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * Splash Screen Activity to show on the start of the app, easy to configure you only need to
 * add this Activity as the main Activity of your app and add the 3 parameters for configurate
 * this SplashScreen in AndroidManifest. There are:
 * 
 * <ul>
 * 	<li>INVOKE_LATER_ACTIVITY: The Activity to invoke when the splash screen has finished her work.</li>
 * 	<li>MILLISECONDS_SPLASH_TIME: Number of milliseconds to show this activity.</li>
 * 	<li>SPLASH_IMAGE: The name of the drawable inside your project that you want to show.</li>
 * </ul>
 * 
 * AndroidManifest Sample:
 * 
 * <application android:icon="@drawable/icon" android:label="@string/app_name">
 *   
 *  <meta-data android:name="INVOKE_LATER_ACTIVITY" android:value="com.yourapp.packagename.YourMainActivity" />
 *  <meta-data android:name="MILLISECONDS_SPLASH_TIME" android:value="1000" />
 *  <meta-data android:name="SPLASH_IMAGE" android:value="drawable_name" />
 *   
 *      <activity android:name=".SplashActivity"
 *                android:label="@string/app_name">
 *          <intent-filter>
 *              <action android:name="android.intent.action.MAIN" />
 *              <category android:name="android.intent.category.LAUNCHER" />
 *          </intent-filter>
 *      </activity>
 *
 * </application>
 * 
 * @author <a href="http://bakingcode.com">Christian Panadero Martinez @ BakingCode.com</a>
 */
public class SplashActivity extends Activity {

	private static final String TAG = "SplashActivity";
	
	// Params
	private final static String INVOKE_LATER_ACTIVITY = "INVOKE_LATER_ACTIVITY";
	private Class<?> activityToInvoke;
	
	private final static String MILLISECONDS_SPLASH_TIME = "MILLISECONDS_SPLASH_TIME";
	private int millisecondsToWait; 
	
	private final static String SPLASH_IMAGE = "SPLASH_IMAGE";
	private BitmapDrawable drawable;
	
	// UI
	private ImageView imgScreen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		try {
			
			ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			
			// get class name for invoke later
			String className = bundle.getString(INVOKE_LATER_ACTIVITY);
			activityToInvoke = (Class<?>) Class.forName(className);

			// get Milliseconds to show screen
			millisecondsToWait = bundle.getInt(MILLISECONDS_SPLASH_TIME);
			
			// get the image to show in splash
			String imageResourceName = bundle.getString(SPLASH_IMAGE);
			int identifier = getResources().getIdentifier(imageResourceName, "drawable", this.getPackageName());
			
			if (identifier != 0) {
				
				Bitmap bmp = BitmapFactory.decodeResource(getResources(), identifier);
				drawable = new BitmapDrawable(bmp);
				
			} else {
				
				throw new RuntimeException("Resource ID for SPLASH_IMAGE parameter cannot be found, please fix it, the name must be a valid drawable name.\n" +
						"<meta-data android:name=\"SPLASH_IMAGE\" android:value=\"splash_image\" />");
				
			}
			
		} catch (ClassNotFoundException e) {
			
			// if the class is not found, informe the dev.
			Log.d(TAG, "The class you configured in the AndroidManifest can't be found, please correct it, remember you have to write your complete packge name + class");
			Log.d(TAG, "<meta-data android:name=\"INVOKE_LATER_ACTIVITY\" android:value=\"com.yourapp.packagename.YourActivity\" />");
			Log.d(TAG, "\n");

			e.printStackTrace();
			
			System.exit(0);
			
		} catch (Exception e) {
			
			// If any exceptions, launch configuration msg and shutdown app
			
			Log.d(TAG, "Something is missconfigurated, to configure the splash activity you only need to set in AndroidManifest the splash activity like this: ");
			Log.d(TAG, "<meta-data android:name=\"INVOKE_LATER_ACTIVITY\" android:value=\"com.yourapp.packagename.YourActivity\" />");
			Log.d(TAG, "<meta-data android:name=\"MILLISECONDS_SPLASH_TIME\" android:value=\"1500\" />");
			Log.d(TAG, "<meta-data android:name=\"SPLASH_IMAGE\" android:value=\"splash_image\" />");
			Log.d(TAG, "\n");
			Log.d(TAG, "<activity android:name=\"SplashActivity\">");
			Log.d(TAG, "\t<intent-filter>");
			Log.d(TAG, "\t\t<action android:name=\"android.intent.action.MAIN\" />");
			Log.d(TAG, "\t\t<category android:name=\"android.intent.category.LAUNCHER\" />");
			Log.d(TAG, "\t</intent-filter>");
			Log.d(TAG, "</activity>");
			Log.d(TAG, "\n");
			Log.d(TAG, "Please configure it in the proper way.");
			Log.d(TAG, "\n");

			e.printStackTrace();
			
			System.exit(0);
			
		}
		
		// Start creating UI
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		LinearLayout baseLinearLayout = new LinearLayout(this);
		baseLinearLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		imgScreen = new ImageView(this);
		imgScreen.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		imgScreen.setImageDrawable(drawable);
		baseLinearLayout.addView(imgScreen);
		
		setContentView(baseLinearLayout);
		
	}
	
	/**
	 * Send only the delayed time event that will be executed by the handler
	 */
	@Override
	protected void onResume() {
		
		super.onResume();
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				Intent intent = new Intent(getApplicationContext(), activityToInvoke);
	            startActivity(intent);
				finish();
				
			}
			
		}, millisecondsToWait);
		
	}
	
	/**
	 * Recycle the bitmap prior to destroy
	 */
	@Override
	protected void onDestroy() {
		
		BitmapDrawable bitmapDrawable = (BitmapDrawable)imgScreen.getDrawable();
		
		if (bitmapDrawable != null) {
			Bitmap bitmap = bitmapDrawable.getBitmap();
			bitmap.recycle();
			bitmap = null;
		}
		
		super.onDestroy();
		
	}
	
}
