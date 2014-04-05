package com.bugseer.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.Viewport;

/**
 * Hello world!
 *
 */
public class App implements EntryPoint {

	public void onModuleLoad() {
		SimpleEventBus eventBus = new SimpleEventBus();
		Viewport viewport = new Viewport();
		viewport.add(new ScoreContainer(eventBus));
		RootPanel.get().add(viewport);
	}

}