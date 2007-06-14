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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Properties;

import uk.azdev.openfire.net.ProtocolConstants;

public class OpenFireConfiguration {
	
	private String xfireGamesIniPath = "xfire_games.ini";
	private String username = "";
	private String password = "";
	private String netAddr = "";
	private int netPort = 0;
	private int localPort = 0;
	private String longVersion = "3.2.0.0";
	private long shortVersion = 75;
	private String clientLanguage = "us";
	private String[] skinList = { "XFire", "standard", "Separator", "XF_URL" }; 
	private String activeSkin = "XFire";
	private String activeTheme = "default";
	private String partner = "";
	private String xfireServerHostName = ProtocolConstants.XFIRE_SERVER_NAME;
	private int xfireServerPortNum = ProtocolConstants.XFIRE_SERVER_PORT;
	
	public String getXfireGamesIniPath() {
		return xfireGamesIniPath;
	}
	
	public Reader getXfireGamesIni() {
		try {
			return new FileReader(xfireGamesIniPath);
		} catch (FileNotFoundException e) {
			// this should already have been checked when the config was parsed
			throw new RuntimeException("XFire games ini file does not exist, but existence was already checked!");
		}
	}
	
	public void setXfireGamesIniPath(String xfireGamesIniPath) {
		this.xfireGamesIniPath = xfireGamesIniPath;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getLongVersion() {
		return longVersion;
	}
	
	public void setLongVersion(String longVersion) {
		this.longVersion = longVersion;
	}

	public long getShortVersion() {
		return shortVersion;
	}
	
	public void setShortVersion(long shortVersion) {
		this.shortVersion = shortVersion;
	}

	public String getClientLanguage() {
		return clientLanguage;
	}
	
	public void setClientLanguage(String clientLanguage) {
		this.clientLanguage = clientLanguage;
	}

	public String getActiveSkin() {
		return activeSkin;
	}
	
	public void setActiveSkin(String activeSkin) {
		this.activeSkin = activeSkin;
	}

	public String getActiveTheme() {
		return activeTheme;
	}
	
	public void setActiveTheme(String activeTheme) {
		this.activeTheme = activeTheme;
	}

	public String getPartner() {
		return partner;
	}
	
	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String[] getSkinList() {
		return skinList;
	}
	
	public void setSkinList(String[] skinList) {
		this.skinList = skinList;
	}

	public String getXfireServerHostName() {
		return xfireServerHostName;
	}
	
	public void setXfireServerHostName(String xfireServerHostName) {
		this.xfireServerHostName = xfireServerHostName;
	}
	
	public int getXfireServerPortNum() {
		return xfireServerPortNum;
	}
	
	public void setXfireServerPortNum(int xfireServerPortNum) {
		this.xfireServerPortNum = xfireServerPortNum;
	}
	
	public static OpenFireConfiguration readConfig(Reader configReader) throws IOException, MissingMandatoryPropertyException {
		OpenFireConfiguration config = new OpenFireConfiguration();
		Properties properties = new Properties();
		properties.load(configReader);
		
		config.setUsername(getMandatoryProp(properties, "username", "client username"));
		config.setPassword(getMandatoryProp(properties, "password", "client username"));
		config.setUsername(getMandatoryProp(properties, "username", "client username"));
		config.setUsername(getMandatoryProp(properties, "username", "client username"));
		config.setUsername(getMandatoryProp(properties, "username", "client username"));
		config.setUsername(getMandatoryProp(properties, "username", "client username"));
		
		return config;
	}

	private static String getMandatoryProp(Properties properties, String key, String propDesc) throws MissingMandatoryPropertyException {
		if(properties.getProperty(key) == null) {
			throw new MissingMandatoryPropertyException(key, propDesc);
		}
		
		return properties.getProperty(key);
	}
}
