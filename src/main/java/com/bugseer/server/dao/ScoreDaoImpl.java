package com.bugseer.server.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bugseer.server.model.Score;
import com.bugseer.server.model.ScoreBean;
import com.bugseer.server.ws.Type;

@Component("scoreDao")
public class ScoreDaoImpl implements ScoreDao {
	@Autowired
	private SessionFactory sessionFactory;

	//TODO Convert to Criteria + BeanAlias
	@Transactional(readOnly=true)
	public List<ScoreBean> fetchScores(Type type) {
		Session session = sessionFactory.getCurrentSession();
		String queryString = "SELECT filename, score, x, y, jira, numBugs from " + Score.class.getName();
		if (type == Type.FRONTEND) {
			queryString += " WHERE filename like '%css' OR filename like '%js' OR filename like '%html' OR filename like '%tpl'";
		} else if (type == Type.BACKEND) {
			queryString += " WHERE filename like '%java'";
		}
		Query query = session.createQuery(queryString);

		List<ScoreBean> scoreBeans = new ArrayList<ScoreBean>();
		@SuppressWarnings("unchecked")
		List<Object[]> result = query.list();
		for (Object[] row : result) {
			scoreBeans.add(new ScoreBean(
					(String) row[0],
					(BigDecimal) row[1],
					(String) row[2],
					(String) row[3],
					(String) row[4],
					(Integer) row[5]));
		}
		return scoreBeans;
	}

	//TODO Convert to Criteria + BeanAlias
	@Transactional(readOnly=true)
	public List<ScoreBean> fetchScoresByFiles(List<String> filenames) {
		Session session = sessionFactory.getCurrentSession();
		String queryString = "SELECT filename, score, x, y, jira, numBugs from " + Score.class.getName();
		queryString += String.format(" WHERE filename in (%s)", StringUtils.collectionToDelimitedString(filenames, ",", "'", "'"));

		Query query = session.createQuery(queryString);

		List<ScoreBean> scoreBeans = new ArrayList<ScoreBean>();
		@SuppressWarnings("unchecked")
		List<Object[]> result = query.list();
		for (Object[] row : result) {
			scoreBeans.add(new ScoreBean(
					(String) row[0],
					(BigDecimal) row[1],
					(String) row[2],
					(String) row[3],
					(String) row[4],
					(Integer) row[5]));
		}
		return scoreBeans;
	}

}
