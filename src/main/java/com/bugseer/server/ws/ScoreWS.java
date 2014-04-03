package com.bugseer.server.ws;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bugseer.server.model.ScoreBean;

@Getter @Setter @NoArgsConstructor
@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScoreWS implements Serializable {
	private static final long serialVersionUID = -74940562759017153L;

	private List<ScoreBean> filescores;

	public ScoreWS(List<ScoreBean> filescores) {
		this.filescores = filescores;
	}

}
