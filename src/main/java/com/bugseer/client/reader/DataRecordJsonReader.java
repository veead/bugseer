package com.bugseer.client.reader;

import com.bugseer.client.bean.ScoreBean;
import com.bugseer.client.bean.ScoreList;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.sencha.gxt.data.shared.loader.JsonReader;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoadResultBean;

public class DataRecordJsonReader extends JsonReader<ListLoadResult<ScoreBean>, ScoreList> {
	public DataRecordJsonReader(AutoBeanFactory factory, Class<ScoreList> rootBeanType) {
		super(factory, rootBeanType);
	}

	@Override
	protected ListLoadResult<ScoreBean> createReturnData(Object loadConfig, ScoreList incomingData) {
		return new ListLoadResultBean<ScoreBean>(incomingData.getFilescores());
	}
}
