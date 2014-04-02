package com.bugseer.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.bugseer.model.Jira;

/**
 * @author
 *
 */

@Getter @Setter
public class JiraResponse {
	private List<Jira> jiras;
}
