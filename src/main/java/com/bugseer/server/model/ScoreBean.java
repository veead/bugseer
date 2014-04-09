package com.bugseer.server.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author
 *
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@XmlRootElement(name = "filescore")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScoreBean implements Serializable {
	private static final long serialVersionUID = 6648244950449963126L;

	private String filename;
	private BigDecimal score;
	private String x;
	private String y;
	private String jira;
	private Integer numBugs;

}
