package com.bugseer.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bugseer.server.dao.ScoreDao;
import com.bugseer.server.util.ServerConfiguration;
import com.bugseer.server.ws.ScoreWS;

@Service("gitService")
public class GitServiceImpl implements GitService {
	@Autowired
	ServerConfiguration serverConfiguration;
	@Autowired
	private ScoreDao scoreDao;

	public Response readFiles(Integer pullRequest) {
		PullRequestService service = new PullRequestService();
		service.getClient().setCredentials(
				serverConfiguration.getProperties().getProperty("git.username"),
				serverConfiguration.getProperties().getProperty("git.password"));
		RepositoryId repo = new RepositoryId("AppDirect", "AppDirect");
		List<String> files = new ArrayList<String>();
		try {
			for (CommitFile file : service.getFiles(repo, pullRequest)) {
				int lastIndex = file.getFilename().lastIndexOf('/') + 1;
				if (lastIndex > 0) {
					files.add(file.getFilename().substring(lastIndex));
				} else {
					files.add(file.getFilename());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.ok(new ScoreWS(scoreDao.fetchScoresByFiles(files))).build();
	}
}
