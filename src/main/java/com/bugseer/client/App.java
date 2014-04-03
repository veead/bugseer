package com.bugseer.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Hello world!
 *
 */
public class App implements EntryPoint {

	public void onModuleLoad() {
		RootPanel.get().add(new ScoreGrid());
	}
}