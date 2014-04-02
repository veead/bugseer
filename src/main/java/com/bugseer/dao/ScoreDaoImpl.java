package com.bugseer.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bugseer.model.Score;
import com.bugseer.model.ScoreBean;

@Component("scoreDao")
public class ScoreDaoImpl implements ScoreDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional(readOnly=true)
	public List<ScoreBean> fetchAllScores() {
		return readByQueryBean();
	}

	private List<ScoreBean> readByCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Score.class);
		criteria.setResultTransformer(Transformers.aliasToBean(ScoreBean.class));
		Criteria executableCriteria = criteria.getExecutableCriteria(sessionFactory.getCurrentSession());
		@SuppressWarnings("unchecked")
		List<ScoreBean> scores = executableCriteria.list();
		return scores;
	}

	private List<Score> readByQuery() {
		Session session = sessionFactory.getCurrentSession();
		String queryString = "SELECT filename, score, x, y, numBugs from " + Score.class.getName();
		Query query = session.createQuery(queryString);

		@SuppressWarnings("unchecked")
		List<Score> result = query.list();
		return result;
	}

	private List<ScoreBean> readByQueryBean() {
		Session session = sessionFactory.getCurrentSession();
		String queryString = "SELECT filename, score, x, y, numBugs from " + Score.class.getName();
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
					(Integer) row[4]));
		}
		return scoreBeans;
	}

}
