package com.bugseer.client.bean;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface ScoreProperties extends PropertyAccess<ScoreBean> {
		@Path("filename")
		ModelKeyProvider<ScoreBean> key();

		ValueProvider<ScoreBean, String> filename();

		ValueProvider<ScoreBean, Double> score();

		ValueProvider<ScoreBean, String> x();

		ValueProvider<ScoreBean, String> y();

		ValueProvider<ScoreBean, Integer> numBugs();
}
