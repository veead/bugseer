package com.bugseer.dao;

import java.util.List;

import com.bugseer.model.ScoreBean;

/**
 * @author
 *
 */
public interface ScoreDao {
	public List<ScoreBean> fetchAllScores();
}
