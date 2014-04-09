package com.bugseer.server.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "vw_score")
public class Score implements Serializable {
	private static final long serialVersionUID = 6648244950449963526L;

	@Id
	@Column(name = "filename", length = 512)
	private String filename;

	@Column(name = "score", precision = 22, scale = 10)
	private BigDecimal score;

	@Column(name = "x", length = 2048)
	private String x;

	@Column(name = "y", length = 2048)
	private String y;

	@Column(name = "jira", length = 2048)
	private String jira;

	@Column(name = "num_bugs")
	private Integer numBugs;
}
