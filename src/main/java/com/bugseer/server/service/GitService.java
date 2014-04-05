package com.bugseer.server.service;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("score/pull")
public interface GitService {
	@GET
	@Path("/{pullRequest}/")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response readFiles(@PathParam(value = "pullRequest") Integer pullRequest);
}
