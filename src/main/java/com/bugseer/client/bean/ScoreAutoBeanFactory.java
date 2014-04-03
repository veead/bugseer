package com.bugseer.client.bean;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;

public interface ScoreAutoBeanFactory extends AutoBeanFactory {
	AutoBean<ScoreList> items();
	AutoBean<ListLoadConfig> loadConfig();
}