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

/**
 * Listener executed when the User removes the finger of the button
 */
public interface OnLongDownListener {

	/**
	 * Called when canceling the long press button, ie when the user removes finger from screen.
	 * @param button
	 */
	public void onCancelLongPress(LongPressButton button);

	/**
	 * Called when user touch and long-presses the screen, write your logic in this method.
	 * @param button
	 */
	public void executeOnDown(LongPressButton button);
	
}