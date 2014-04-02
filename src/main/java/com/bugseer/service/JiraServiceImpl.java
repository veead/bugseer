package com.bugseer.service;

import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.apache.cxf.transport.http.HTTPSession;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import com.bugseer.util.PropertiesLoader;

public class JiraServiceImpl implements JiraService {
	private static Properties config = PropertiesLoader.getProperties();

	private DefaultHttpClient httpClient = new DefaultHttpClient();

	public Response readJira(String key) {
		RestTemplate template = new RestTemplate();
		String url = config.getProperty("base.url") + "rest/auth/latest/session";
		template.getInterceptors().add(new ClientHttpRequestInterceptor() {
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
				request.getHeaders().add("Accept-Language", "en");
				return execution.execute(request, body);
			}
		});
		ResponseEntity<String> response = template.getForEntity(url, String.class);
		String body = response.getBody();
		return Response.ok(body).build();
	}

	private void httpClient() {
		try {
			HttpHost targetHost = null;
			HTTPSession session;
			HttpPost post = new HttpPost(config.getProperty("base.url") + "rest/auth/latest/session");
			JSONObject reqContent = new JSONObject();
			reqContent.put("username", config.getProperty("user.name"));
			reqContent.put("password", config.getProperty("user.password"));
			StringEntity se = new StringEntity(reqContent.toString());
			se.setContentType("application/json");
			post.setEntity(se);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpClient.execute(targetHost, post, responseHandler);
			JSONObject obj = new JSONObject(responseBody);
			//session.setName(obj.getJSONObject("session").getString("name"));
			//session.setName(obj.getJSONObject("session").getString("value"));
			CookieStore cookieStore = new BasicCookieStore();
			BasicClientCookie cookie = new BasicClientCookie(obj.getJSONObject(
					"session").getString("name"), obj.getJSONObject("session")
					.getString("value"));
			cookie.setDomain(targetHost.getHostName());
			cookie.setPath("/");
			cookieStore.addCookie(cookie);
			httpClient.setCookieStore(cookieStore);
		} catch (Exception ex) {

		}
	}

}
