package com.bugseer.service;

import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.utils.ParameterizedCollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bugseer.dao.ScoreDao;
import com.bugseer.model.ScoreBean;

@Service("scoreService")
public class ScoreServiceImpl implements ScoreService {
	@Autowired
	private ScoreDao scoreDao;

	@Transactional(readOnly = true)
	public Response readScores() {
		//return Response.ok(scoreDao.fetchAllScores()).build();
		return Response.ok(new GenericEntity<List<ScoreBean>>(scoreDao.fetchAllScores(), new ParameterizedCollectionType<ScoreBean>(ScoreBean.class))).build();
	}
}
