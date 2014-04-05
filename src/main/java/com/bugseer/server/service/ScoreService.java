package com.bugseer.server.service;


import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bugseer.server.ws.Source;
import com.bugseer.server.ws.Type;

@Path("score")
public interface ScoreService {
	@GET
	@Path("list")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response readScores(@QueryParam("source") Source source, @QueryParam("type") @DefaultValue("ALL") Type type);

}
