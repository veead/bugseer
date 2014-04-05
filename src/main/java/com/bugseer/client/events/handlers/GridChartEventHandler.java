package com.bugseer.client.events.handlers;

import com.bugseer.client.events.GridChartEvent;
import com.google.gwt.event.shared.EventHandler;

public interface GridChartEventHandler extends EventHandler {
	void onEvent(GridChartEvent event);
}