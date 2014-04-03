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
import com.bugseer.client.reader.DataRecordJsonReader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.client.loader.HttpProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class ScoreGrid implements IsWidget {

	private FramedPanel panel;

	@Override
	public Widget asWidget() {
		if (panel == null) {
			ScoreAutoBeanFactory factory = GWT.create(ScoreAutoBeanFactory.class);

			DataRecordJsonReader reader = new DataRecordJsonReader(factory, ScoreList.class);

			String path = "api/score/list.json";
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, path);
			HttpProxy<ListLoadConfig> proxy = new HttpProxy<ListLoadConfig>(builder);

			final ListLoader<ListLoadConfig, ListLoadResult<ScoreBean>> loader = new ListLoader<ListLoadConfig, ListLoadResult<ScoreBean>>(proxy, reader);
			loader.useLoadConfig(factory.create(ListLoadConfig.class).as());

			ScoreProperties props = GWT.create(ScoreProperties.class);

			ListStore<ScoreBean> store = new ListStore<ScoreBean>(props.key());
			loader.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, ScoreBean, ListLoadResult<ScoreBean>>(store));

			ColumnConfig<ScoreBean, String> cc1 = new ColumnConfig<ScoreBean, String>(props.filename(), 100, "filename");
			ColumnConfig<ScoreBean, String> cc2 = new ColumnConfig<ScoreBean, String>(props.score(), 165, "score");
			ColumnConfig<ScoreBean, String> cc3 = new ColumnConfig<ScoreBean, String>(props.x(), 300, "x");
			ColumnConfig<ScoreBean, String> cc4 = new ColumnConfig<ScoreBean, String>(props.y(), 300, "y");
			ColumnConfig<ScoreBean, String> cc5 = new ColumnConfig<ScoreBean, String>(props.numBugs(), 65, "numBugs");

			List<ColumnConfig<ScoreBean, ?>> l = new ArrayList<ColumnConfig<ScoreBean, ?>>();
			l.add(cc1);
			l.add(cc2);
			l.add(cc3);
			l.add(cc4);
			l.add(cc5);
			ColumnModel<ScoreBean> cm = new ColumnModel<ScoreBean>(l);

			Grid<ScoreBean> grid = new Grid<ScoreBean>(store, cm);
			grid.getView().setForceFit(true);
			grid.setLoader(loader);
			grid.setLoadMask(true);
			grid.setBorders(true);
			grid.getView().setEmptyText("Please hit the load button.");

			panel = new FramedPanel();
			panel.setHeadingText("List of files");
			panel.setCollapsible(true);
			panel.setAnimCollapse(true);
			panel.setWidget(grid);
			panel.setPixelSize(575, 350);
			panel.addStyleName("margin-10");
			panel.setButtonAlign(BoxLayoutPack.CENTER);
			panel.addButton(new TextButton("Load Json", new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					loader.load();
				}
			}));
		}

		return panel;
	}
}