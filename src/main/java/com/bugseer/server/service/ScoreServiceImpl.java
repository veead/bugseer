package com.bugseer.server.service;

import java.beans.PropertyDescriptor;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

import com.bugseer.server.dao.ScoreDao;
import com.bugseer.server.model.ScoreBean;
import com.bugseer.server.ws.ScoreWS;
import com.bugseer.server.ws.Source;
import com.bugseer.server.ws.Type;

@Service("scoreService")
public class ScoreServiceImpl implements ScoreService {
	@Autowired
	private ScoreDao scoreDao;

	@Transactional(readOnly = true)
	public Response readScores(Source source, Type type) {
		if (source == Source.CSV) {
			CsvToBean<ScoreBean> csvToBean = new CsvToBean<ScoreBean>() {
				@Override
				protected Object convertValue(String value, PropertyDescriptor prop) throws InstantiationException, IllegalAccessException {
					if (prop.getName().equals("score")) {
						return new BigDecimal(value);
					} else if (prop.getName().equals("numBugs")) {
						return Integer.parseInt(value);
					}
					return super.convertValue(value, prop);
				}
			};

			Map<String, String> columnMapping = new HashMap<String, String>();
			columnMapping.put("filename", "filename");
			columnMapping.put("score", "score");
			columnMapping.put("x", "x");
			columnMapping.put("y", "y");
			columnMapping.put("num_bugs", "numBugs");

			HeaderColumnNameTranslateMappingStrategy<ScoreBean> strategy = new HeaderColumnNameTranslateMappingStrategy<ScoreBean>();
			strategy.setType(ScoreBean.class);
			strategy.setColumnMapping(columnMapping);

			CSVReader reader = new CSVReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("scores.csv")));
			return Response.ok(new ScoreWS(csvToBean.parse(strategy, reader))).build();
		} else {
			return Response.ok(new ScoreWS(scoreDao.fetchScores(type))).build();
		}
	}
}
