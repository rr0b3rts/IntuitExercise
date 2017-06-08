package com.intuit.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intuit.dao.URLDao;

/**
 * URLService endpoint.  This service is used to track URLs and display stats on the three most tracked domains.
 * 
 * @author rroberts
 *
 */
@Path("/URLService")
public class URLService {

	/**
	 * This endpoint accepts a URL and stores the domain.
	 * 
	 * @param url
	 * @return
	 */
	@GET
	@Path("/add")
	public Response addURL(@QueryParam("url") String url) {
		if(url == null || url.length() == 0) {
			return Response
					.status(Response.Status.NO_CONTENT).entity("Expected url.  None found.").build();
		}
		
		URI uri;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	    String domain = uri.getHost();
	    
	    URLDao.INSTANCE.addURL(domain);
		
		return Response
				.status(200).build();
	}
	
	/**
	 * This endpoint generates results for the 3 most visited domains.
	 * 
	 * @return
	 */
	@GET
	@Path("stats")
	@Produces(MediaType.APPLICATION_JSON)
	public String getStats() {
		Map<String, String> urlMap = URLDao.INSTANCE.getStats();
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		
		return gson.toJson(urlMap);
	}
}
