package com.bugseer.server.dao;

import java.util.List;

import com.bugseer.server.model.ScoreBean;

/**
 * @author
 *
 */
public interface ScoreDao {
	public List<ScoreBean> fetchAllScores();
}
