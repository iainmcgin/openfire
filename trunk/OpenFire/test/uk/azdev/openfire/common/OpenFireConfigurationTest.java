/*
 * OpenFire - a Java API to access the XFire instant messaging network.
 * Copyright (C) 2007 Iain McGinniss
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.azdev.openfire.common;


import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class OpenFireConfigurationTest {

	private OpenFireConfiguration configuration;
	
	@Before
	public void setUp() throws Exception {
		configuration = new OpenFireConfiguration();
	}
	
	@Test
	public void testDefaultValues() throws Exception {
		assertEquals("xfire_games.ini", configuration.getXfireGamesIniPath());
	}
	
	@Test
	public void testSetXfireGamesIniPath() {
		configuration.setXfireGamesIniPath("my.ini");
		assertEquals("my.ini", configuration.getXfireGamesIniPath());
	}
	
	@Test
	public void testReadValidConfig() throws IOException, InvalidConfigurationException {
		Reader configReader = getTestConfigReader("valid_config.cfg");
		configuration = OpenFireConfiguration.readConfig(configReader);
		assertEquals("testuser", configuration.getUsername());
		assertEquals("testpass", configuration.getPassword());
		assertEquals("123.221.131.113", configuration.getNetworkAddress());
		assertEquals(12345, configuration.getNetworkPort());
		assertEquals(54321, configuration.getLocalPort());
		assertEquals("C:/Program Files/Xfire/xfire_games.ini", configuration.getXfireGamesIniPath());
		assertEquals(101L, configuration.getShortVersion());
		assertEquals("1.2.3.4", configuration.getLongVersion());
		assertEquals("de", configuration.getClientLanguage());
		assertEquals(new String[] { "Skin1", "Skin2", "Skin3"}, configuration.getSkinList());
		assertEquals("Skin3", configuration.getActiveSkin());
		assertEquals("MyTheme", configuration.getActiveTheme());
		assertEquals("InCrime", configuration.getPartner());
		assertEquals("xfire.azdev.co.uk", configuration.getXfireServerHostName());
		assertEquals(12345, configuration.getXfireServerPortNum());
	}
	
	@Test
	public void testReadConfig_withMissingProperties() throws IOException, InvalidConfigurationException {
		assertParseThrowsMissingPropertyEx("username", "client username (username) was missing from the configuration file");
		assertParseThrowsMissingPropertyEx("password", "client password (password) was missing from the configuration file");
		assertParseThrowsMissingPropertyEx("net.addr", "public network address (net.addr) was missing from the configuration file");
		assertParseThrowsMissingPropertyEx("net.port", "public network port (net.port) was missing from the configuration file");
		assertParseThrowsMissingPropertyEx("local.port", "local port (local.port) was missing from the configuration file");
		assertParseThrowsMissingPropertyEx("games.ini.path", "xfire games ini path (games.ini.path) was missing from the configuration file");
	}
	
	@Test
	public void testReadConfig_withMissingClientVersion() throws IOException, InvalidConfigurationException {
		Properties configProps = readConfigAndRemoveProperty("client.version");
		OpenFireConfiguration config = readConfigFromProps(configProps);
		assertEquals(OpenFireConfiguration.DEFAULT_SHORT_VERSION, config.getShortVersion());
	}
	
	@Test
	public void testReadConfig_withMissingLongVersion() throws IOException, InvalidConfigurationException {
		Properties configProps = readConfigAndRemoveProperty("client.long.version");
		OpenFireConfiguration config = readConfigFromProps(configProps);
		assertEquals(OpenFireConfiguration.DEFAULT_LONG_VERSION, config.getLongVersion());
	}
	
	@Test
	public void testReadConfig_withMissingClientLanguage() throws IOException, InvalidConfigurationException {
		Properties configProps = readConfigAndRemoveProperty("client.language");
		OpenFireConfiguration config = readConfigFromProps(configProps);
		assertEquals(OpenFireConfiguration.DEFAULT_CLIENT_LANGUAGE, config.getClientLanguage());
	}
	
	@Test
	public void testReadConfig_withMissingSkinList() throws IOException, InvalidConfigurationException {
		Properties configProps = readConfigAndRemoveProperty("client.skinlist");
		OpenFireConfiguration config = readConfigFromProps(configProps);
		assertEquals(OpenFireConfiguration.DEFAULT_SKIN_LIST, config.getSkinList());
	}
	
	@Test
	public void testReadConfig_withMissingActiveSkin() throws IOException, InvalidConfigurationException {
		Properties configProps = readConfigAndRemoveProperty("client.activeskin");
		OpenFireConfiguration config = readConfigFromProps(configProps);
		assertEquals(OpenFireConfiguration.DEFAULT_ACTIVE_SKIN, config.getActiveSkin());
	}
	
	@Test
	public void testReadConfig_withMissingActiveTheme() throws IOException, InvalidConfigurationException {
		Properties configProps = readConfigAndRemoveProperty("client.activetheme");
		OpenFireConfiguration config = readConfigFromProps(configProps);
		assertEquals(OpenFireConfiguration.DEFAULT_ACTIVE_THEME, config.getActiveTheme());
	}
	
	@Test
	public void testReadConfig_withMissingPartner() throws IOException, InvalidConfigurationException {
		Properties configProps = readConfigAndRemoveProperty("partner");
		OpenFireConfiguration config = readConfigFromProps(configProps);
		assertEquals(OpenFireConfiguration.DEFAULT_PARTNER, config.getPartner());
	}
	
	@Test
	public void testReadConfig_withMissingXfireHostname() throws IOException, InvalidConfigurationException {
		Properties configProps = readConfigAndRemoveProperty("server.host");
		OpenFireConfiguration config = readConfigFromProps(configProps);
		assertEquals(OpenFireConfiguration.DEFAULT_XFIRE_SERVER_HOSTNAME, config.getXfireServerHostName());
	}
	
	@Test
	public void testReadConfig_withMissingXFirePort() throws IOException, InvalidConfigurationException {
		Properties configProps = readConfigAndRemoveProperty("server.port");
		OpenFireConfiguration config = readConfigFromProps(configProps);
		assertEquals(OpenFireConfiguration.DEFAULT_XFIRE_SERVER_PORT, config.getXfireServerPortNum());
	}
	
	@Test
	public void testReadConfig_withInvalidPropertyValues() throws IOException, InvalidConfigurationException {
		// integer tests
		assertParseThrowsInvalidPropValEx("net.port", "notANumber", "public network port (net.port) is not a valid integer");
		assertParseThrowsInvalidPropValEx("local.port", "notANumber", "local port (local.port) is not a valid integer");
		assertParseThrowsInvalidPropValEx("client.version", "notANumber", "client version (client.version) is not a valid integer");
		assertParseThrowsInvalidPropValEx("server.port", "notANumber", "xfire server port (server.port) is not a valid integer");
		
		// range tests
		assertParseThrowsInvalidPropValEx("net.port", "0", "public network port (net.port) is not in the legal range (1-65535)");
		assertParseThrowsInvalidPropValEx("net.port", "65536", "public network port (net.port) is not in the legal range (1-65535)");
		assertParseThrowsInvalidPropValEx("local.port", "0", "local port (local.port) is not in the legal range (1-65535)");
		assertParseThrowsInvalidPropValEx("local.port", "65536", "local port (local.port) is not in the legal range (1-65535)");
		assertParseThrowsInvalidPropValEx("server.port", "0", "xfire server port (server.port) is not in the legal range (1-65535)");
		assertParseThrowsInvalidPropValEx("server.port", "65536", "xfire server port (server.port) is not in the legal range (1-65535)");
		assertParseThrowsInvalidPropValEx("client.version", "-1", "client version (client.version) is not in the legal range (0-" + ((1L << 32L) - 1L) + ")");
		assertParseThrowsInvalidPropValEx("client.version", Long.toString((1L << 33L)), "client version (client.version) is not in the legal range (0-" + ((1L << 32L) - 1L) + ")");
	}
	
	private OpenFireConfiguration readConfigFromProps(Properties props) throws InvalidConfigurationException {
		OpenFireConfiguration config = OpenFireConfiguration.readConfig(props);
		return config;
	}
	
	private Properties readConfigAndRemoveProperty(String propToRemove) throws IOException {
		Reader configReader = getTestConfigReader("valid_config.cfg");
		Properties configProps = new Properties();
		configProps.load(configReader);
		
		// remove the suggested property
		configProps.remove(propToRemove);
		return configProps;
	}
	
	private void assertParseThrowsMissingPropertyEx(String propToRemove, String expectedMessage) throws IOException, InvalidConfigurationException {
		Properties configProps = readConfigAndRemoveProperty(propToRemove);
		try {
			configuration = OpenFireConfiguration.readConfig(configProps);
			fail("Missing mandatory property exception not thrown");
		} catch(MissingMandatoryPropertyException e) {
			assertEquals(expectedMessage, e.getMessage());
		}
	}
	
	private void assertParseThrowsInvalidPropValEx(String propToChange, String newPropVal, String expectedMessage) throws IOException, InvalidConfigurationException {
		Reader configReader = getTestConfigReader("valid_config.cfg");
		Properties configProps = new Properties();
		configProps.load(configReader);
		
		// change the suggested property
		configProps.setProperty(propToChange, newPropVal);
		
		try {
			configuration = OpenFireConfiguration.readConfig(configProps);
			fail("invalid property exception not thrown");
		} catch(InvalidPropertyValueException e) {
			assertEquals(expectedMessage, e.getMessage());
		}
	}
	
	private Reader getTestConfigReader(String configName) throws IOException {
		InputStream configStream = OpenFireConfiguration.class.getResourceAsStream("testResources/configs/" + configName);
		if(configStream == null) {
			throw new IOException("configuration file \"" + configName + "\" does not exist!");
		}
		return new InputStreamReader(configStream);
	}
}
