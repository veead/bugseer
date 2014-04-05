package com.bugseer.client;

import java.util.Date;
import java.util.List;

import org.moxieapps.gwt.highcharts.client.Axis;
import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.DateTimeLabelFormats;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.ToolTip;
import org.moxieapps.gwt.highcharts.client.ToolTipData;
import org.moxieapps.gwt.highcharts.client.ToolTipFormatter;
import org.moxieapps.gwt.highcharts.client.plotOptions.Marker;
import org.moxieapps.gwt.highcharts.client.plotOptions.SplinePlotOptions;

import com.bugseer.client.bean.ScoreBean;
import com.bugseer.client.events.GridChartEvent;
import com.bugseer.client.events.handlers.GridChartEventHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;

public class ScoreChart implements IsWidget {

	private Chart chart;
	private CenterLayoutContainer container;

	public ScoreChart(SimpleEventBus eventBus) {
		eventBus.addHandler(GridChartEvent.TYPE, new GridChartEventHandler(){
			public void onEvent(GridChartEvent event) {
				updateChart(event.getScore());
			}
		});
	}

	@Override
	public Widget asWidget() {
		if (container == null) {
			container = new CenterLayoutContainer();
			chart = createTimeChart();
			chart.setSizeToMatchContainer();
			container.add(chart);
		}
		return container;
	}

	private Chart createTimeChart() {
		chart = new Chart()
			.setType(Series.Type.SPLINE)
			.setChartTitleText("Scores")
			.setSplinePlotOptions(new SplinePlotOptions().setMarker(new Marker().setSymbol(Marker.Symbol.CIRCLE)))
			.setToolTip(new ToolTip()
				.setFormatter(new ToolTipFormatter() {
					public String format(ToolTipData toolTipData) {
						return DateTimeFormat.getFormat("d-MMM-yyyy").format(new Date(toolTipData.getXAsLong())) + "<br /><b>" + toolTipData.getYAsDouble() + "</b>";
				}}));

		chart.getXAxis()
			.setType(Axis.Type.DATE_TIME)
			.setDateTimeLabelFormats(new DateTimeLabelFormats()
				.setMonth("%b-%Y"));
		chart.getYAxis()
			.setAxisTitleText("Score")
			.setTickInterval(0.1)
			.setMin(0)
			.setMax(0.5);

		return chart;
	}

	private void updateChart(List<ScoreBean> scores) {
		if (scores.size() == 1) {
			chart.removeAllSeries();
		}
		ScoreBean score = scores.get(scores.size()-1);
		chart.addSeries(chart.createSeries()
				.setName(score.getFilename())
				.setPoints(toNumberArray(score.getX(), score.getY())));
	}

	private Number[][] toNumberArray(String x, String y) {
		String[] xnum = x.split(":");
		String[] ynum = y.split(":");
		Number[][] points = new Number[xnum.length][2];
		for (int i = 0; i<xnum.length; i++) {
			points[i][0] = 1000L * Long.parseLong(xnum[i]);
			points[i][1] = Double.parseDouble(ynum[i]);
		}
		return points;
	}
}
