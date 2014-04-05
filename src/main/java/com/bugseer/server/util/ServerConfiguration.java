package com.bugseer.server.util;

import java.util.Properties;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author
 *
 */
@Getter
@Component("serverConfiguration")
public class ServerConfiguration {
	@Autowired @Qualifier("appProperties")
	private Properties properties;
}