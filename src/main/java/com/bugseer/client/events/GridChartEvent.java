package com.bugseer.client.events;

import java.util.List;

import com.bugseer.client.bean.ScoreBean;
import com.bugseer.client.events.handlers.GridChartEventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class GridChartEvent extends GwtEvent<GridChartEventHandler>{

	public static Type<GridChartEventHandler> TYPE = new Type<GridChartEventHandler>();

	private List<ScoreBean> score;

	public GridChartEvent(List<ScoreBean>score){
		this.score = score;
	}

	public List<ScoreBean>getScore() {
		return score;
	}

	@Override
	protected void dispatch(GridChartEventHandler handler) {
		handler.onEvent(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GridChartEventHandler> getAssociatedType() {
		return TYPE;
	}

}