package com.bugseer.client;

/**
 * @author
 *
 */
import java.util.ArrayList;
import java.util.List;

import com.bugseer.client.bean.ScoreAutoBeanFactory;
import com.bugseer.client.bean.ScoreBean;
import com.bugseer.client.bean.ScoreList;
import com.bugseer.client.bean.ScoreProperties;
import com.bugseer.client.events.GridChartEvent;
import com.bugseer.client.reader.DataRecordJsonReader;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.loader.HttpProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.RefreshEvent;
import com.sencha.gxt.widget.core.client.event.RefreshEvent.RefreshHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.AggregationNumberSummaryRenderer;
import com.sencha.gxt.widget.core.client.grid.AggregationRowConfig;
import com.sencha.gxt.widget.core.client.grid.AggregationSafeHtmlRenderer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.grid.SummaryType.AvgSummaryType;
import com.sencha.gxt.widget.core.client.grid.SummaryType.SumSummaryType;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class ScoreGrid implements IsWidget {

	private static final Double CUTOFF = 1.2D;
	private VerticalLayoutContainer container;
	private SimpleEventBus eventBus;
	Grid<ScoreBean> grid;

	public ScoreGrid(SimpleEventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public Widget asWidget() {
		if (container == null) {
			container = new VerticalLayoutContainer();

			HorizontalLayoutContainer horizontalLayoutContainer = new HorizontalLayoutContainer();
			final TextField textField = new TextField();
			TextButton button = new TextButton("Fetch Pull#");
			final Label label = new Label();
			label.setStyleName("message-label");
			horizontalLayoutContainer.add(textField, new HorizontalLayoutData(0.25, -1, new Margins(0,5,0,0)));
			horizontalLayoutContainer.add(button, new HorizontalLayoutData(0.15, -1, new Margins(0,0,0,5)));
			horizontalLayoutContainer.add(label, new HorizontalLayoutData(0.60, -1, new Margins(0,0,0,15)));
			container.add(horizontalLayoutContainer, new VerticalLayoutData(700, 50));


			final ScoreAutoBeanFactory factory = GWT.create(ScoreAutoBeanFactory.class);
			final DataRecordJsonReader reader = new DataRecordJsonReader(factory, ScoreList.class);
			ScoreProperties props = GWT.create(ScoreProperties.class);
			final ListStore<ScoreBean> store = new ListStore<ScoreBean>(props.key());

			String path = GWT.getHostPageBaseURL() + "api/score/list.json";
			final ListLoader<ListLoadConfig, ListLoadResult<ScoreBean>> loader = resetLoader(path, factory, reader, store);

			ColumnConfig<ScoreBean, String> fileColumn = new ColumnConfig<ScoreBean, String>(props.filename(), 100, "filename");
			ColumnConfig<ScoreBean, Double> scoreColumn = new ColumnConfig<ScoreBean, Double>(props.score(), 60, "score");
			ColumnConfig<ScoreBean, Integer> numBugsColumn = new ColumnConfig<ScoreBean, Integer>(props.numBugs(), 30, "numBugs");
			final NumberFormat number = NumberFormat.getFormat("#.####");
			scoreColumn.setCell(new AbstractCell<Double>() {
				@Override
				public void render(com.google.gwt.cell.client.Cell.Context context, Double value, SafeHtmlBuilder sb) {
					if (value > CUTOFF) {
						sb.appendHtmlConstant("<span style='color: red; font-weight: bold'>" + number.format(value) + "</span>");
					} else {
						sb.appendHtmlConstant(number.format(value));
					}
				}
			});

			RowNumberer<ScoreBean> numberer = new RowNumberer<ScoreBean>(new IdentityValueProvider<ScoreBean>());
			numberer.setWidth(30);
			List<ColumnConfig<ScoreBean, ?>> l = new ArrayList<ColumnConfig<ScoreBean, ?>>();
			l.add(numberer);
			l.add(fileColumn);
			l.add(scoreColumn);
			l.add(numBugsColumn);
			ColumnModel<ScoreBean> cm = new ColumnModel<ScoreBean>(l);
			AggregationRowConfig<ScoreBean> aggregation = new AggregationRowConfig<ScoreBean>();
			aggregation.setRenderer(fileColumn, new AggregationSafeHtmlRenderer<ScoreBean>("Score"));
			aggregation.setRenderer(scoreColumn, new AggregationNumberSummaryRenderer<ScoreBean, Number>(new AvgSummaryType<Number>()));
			aggregation.setRenderer(numBugsColumn, new AggregationNumberSummaryRenderer<ScoreBean, Number>(new SumSummaryType<Number>()));
			cm.addAggregationRow(aggregation);

			grid = new Grid<ScoreBean>(store, cm) {
				@Override
				protected void onAfterFirstAttach() {
					super.onAfterFirstAttach();
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							loader.load();
						}
					});
				}
			};
			grid.addRefreshHandler(new RefreshHandler() {
				@Override
				public void onRefresh(RefreshEvent event) {
					if (grid.getStore().size() > 100) {
						label.setText("");
					} else {
						int count = 0;
						for (ScoreBean score : grid.getStore().getAll()) {
							if (score.getScore()> CUTOFF) {
								count++;
							}
						}
						if (count > 0) {
							label.setText("You have " + count +" bug prone files");
						} else {
							label.setText("Nice, LGTM!");
						}
					}
				}
			});

			SelectionChangedHandler<ScoreBean> handler = new SelectionChangedHandler<ScoreBean>() {
				@Override
				public void onSelectionChanged(SelectionChangedEvent<ScoreBean> event) {
					if (event.getSelection().size() > 0) {
						eventBus.fireEvent(new GridChartEvent(event.getSelection()));
					}
				}
			};
			GridSelectionModel<ScoreBean> rowSelectionModel = new GridSelectionModel<ScoreBean>();
			rowSelectionModel.addSelectionChangedHandler(handler);
			grid.setSelectionModel(rowSelectionModel);

			grid.getView().setForceFit(true);
			grid.setLoader(loader);
			grid.setLoadMask(true);
			grid.setBorders(true);

			FramedPanel panel = new FramedPanel();
			VerticalLayoutContainer gridContainer = new VerticalLayoutContainer();
			SimpleComboBox<String> type = createGridContainer(gridContainer);
			// we want to change selection model on select, not value change which  fires on blur
			type.addSelectionHandler(new SelectionHandler<String>() {
				@Override
				public void onSelection(SelectionEvent<String> event) {
					String path = GWT.getHostPageBaseURL() + "api/score/list.json?type=" + event.getSelectedItem();
					ListLoader<ListLoadConfig, ListLoadResult<ScoreBean>> loader = resetLoader(path, factory, reader, store);
					grid.setLoader(loader);
					loader.load();
				}
			});
			gridContainer.add(grid, new VerticalLayoutData(1, 1));


			panel.setHeadingText("List of files");
			panel.setCollapsible(true);
			panel.setAnimCollapse(true);
			panel.setWidget(gridContainer);
			panel.addStyleName("margin-10");
			panel.setButtonAlign(BoxLayoutPack.CENTER);
			button.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					if (!textField.getText().isEmpty()) {
						String path = GWT.getHostPageBaseURL() + "api/score/pull/" + textField.getText() +".json";
						ListLoader<ListLoadConfig, ListLoadResult<ScoreBean>> loader = resetLoader(path, factory, reader, store);
						grid.setLoader(loader);
						loader.load();
					}
				}
			});
			container.add(panel, new VerticalLayoutData(-1, 1));
		}
		return container;
	}

	private ListLoader<ListLoadConfig, ListLoadResult<ScoreBean>> resetLoader(String path, ScoreAutoBeanFactory factory, DataRecordJsonReader reader, ListStore<ScoreBean> store) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, path);
		HttpProxy<ListLoadConfig> proxy = new HttpProxy<ListLoadConfig>(builder);
		ListLoader<ListLoadConfig, ListLoadResult<ScoreBean>> loader = new ListLoader<ListLoadConfig, ListLoadResult<ScoreBean>>(proxy, reader);
		loader.useLoadConfig(factory.create(ListLoadConfig.class).as());
		loader.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, ScoreBean, ListLoadResult<ScoreBean>>(store));
		return loader;
	}

	private SimpleComboBox<String> createGridContainer(VerticalLayoutContainer gridContainer) {
		ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);
		toolBar.add(new LabelToolItem("Type of Files"));

		SimpleComboBox<String> type = new SimpleComboBox<String>(new StringLabelProvider<String>());
		type.setTriggerAction(TriggerAction.ALL);
		type.setEditable(false);
		type.setWidth(100);
		type.add("ALL");
		type.add("FRONTEND");
		type.add("BACKEND");

		type.setValue("ALL");
		toolBar.add(type);
		gridContainer.add(toolBar, new VerticalLayoutData(1, -1));
		return type;
	}
}