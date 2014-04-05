package com.bugseer.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;

public class ScoreContainer implements IsWidget {

	private HorizontalLayoutContainer container;
	private SimpleEventBus eventBus;

	public ScoreContainer(SimpleEventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public Widget asWidget() {
		if (container == null) {
			container = new HorizontalLayoutContainer();

			DockLayoutPanel dock = new DockLayoutPanel(Unit.PCT);
			dock.addWest(new ScoreGrid(eventBus), 35);
			dock.add(new ScoreChart(eventBus));

			container.add(dock, new HorizontalLayoutData(1, 1, new Margins(10)));

			container.setBorders(true);
			container.addStyleName("margin-10");
		}

		return container;
	}

}
