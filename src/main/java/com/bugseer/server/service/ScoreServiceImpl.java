package com.bugseer.server.service;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bugseer.server.dao.ScoreDao;
import com.bugseer.server.ws.ScoreWS;

@Service("scoreService")
public class ScoreServiceImpl implements ScoreService {
	@Autowired
	private ScoreDao scoreDao;

	@Transactional(readOnly = true)
	public Response readScores() {
		return Response.ok(new ScoreWS(scoreDao.fetchAllScores())).build();
	}
}
