package com.bugseer.model;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author
 *
 */
@Getter @Setter
public class Jira {
	private String key;
	private Date updated;
	private String resolution;
}
