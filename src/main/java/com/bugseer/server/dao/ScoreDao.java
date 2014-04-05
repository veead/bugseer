package com.bugseer.server.dao;

import java.util.List;

import com.bugseer.server.model.ScoreBean;
import com.bugseer.server.ws.Type;

/**
 * @author
 *
 */
public interface ScoreDao {
	public List<ScoreBean> fetchScores(Type type);

	public List<ScoreBean> fetchScoresByFiles(List<String> filenames);
}
