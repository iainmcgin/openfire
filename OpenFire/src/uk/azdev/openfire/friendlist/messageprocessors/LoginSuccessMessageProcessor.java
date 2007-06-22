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
package uk.azdev.openfire.friendlist.messageprocessors;

import java.net.InetSocketAddress;

import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.incoming.LoginSuccessMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientConfigurationMessage;

public class LoginSuccessMessageProcessor implements IMessageProcessor {

	private IMessageSender messageSender;
	private OpenFireConfiguration config;
	private Friend self;

	public LoginSuccessMessageProcessor(IMessageSender messageSender, Friend self, OpenFireConfiguration config) {
		this.messageSender = messageSender;
		this.config = config;
		this.self = self;
	}

	public void processMessage(IMessage msg) {
		LoginSuccessMessage message = (LoginSuccessMessage)msg;
		self.setUserId(message.getUserId());
		self.setDisplayName(message.getNick());
		self.setOnline(message.getSessionId());
		self.setAddress(new InetSocketAddress(message.getPublicIp(), config.getNetworkPort()));
		
		ClientConfigurationMessage clientConfig = new ClientConfigurationMessage();
		clientConfig.setLanguage(config.getClientLanguage());
		clientConfig.setActiveSkin(config.getActiveSkin());
		clientConfig.setActiveTheme(config.getActiveTheme());
		clientConfig.setPartner(config.getPartner());
		
		messageSender.sendMessage(clientConfig);
	}
}