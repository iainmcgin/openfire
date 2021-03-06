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

import static org.junit.Assert.*;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.hamcrest.Matcher;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.core.AllOf;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.incoming.LoginSuccessMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientConfigurationMessage;

@RunWith(JMock.class)
public class LoginSuccessMessageProcessorTest {

	Mockery context = new JUnit4Mockery();
	
	@Test
	public void testProcessMessage() throws UnknownHostException {
		LoginSuccessMessage message = new LoginSuccessMessage();
		message.setSessionId(new SessionId(100));
		message.setNick("Test User");
		message.setUserId(150L);
		message.setPublicIp((Inet4Address)InetAddress.getByAddress(new byte[] { 127, 0, 0, 1}));
		
		OpenFireConfiguration config = new OpenFireConfiguration();
		config.setClientLanguage("de");
		config.setActiveSkin("MySkin");
		config.setActiveTheme("MyTheme");
		config.setPartner("InCrime");
		config.setNetworkPort(12345);
		
		
		Friend self = new Friend("me");
		FriendsList friendsList = new FriendsList(self);
		
		final IMessageSender messageSender = context.mock(IMessageSender.class);
		context.checking(new Expectations() {{
			ArrayList<Matcher<ClientConfigurationMessage>> conditions = new ArrayList<Matcher<ClientConfigurationMessage>>();
			conditions.add(new HasPropertyWithValue<ClientConfigurationMessage>("language", equal("de")));
			conditions.add(new HasPropertyWithValue<ClientConfigurationMessage>("activeSkin", equal("MySkin")));
			conditions.add(new HasPropertyWithValue<ClientConfigurationMessage>("activeTheme", equal("MyTheme")));
			conditions.add(new HasPropertyWithValue<ClientConfigurationMessage>("partner", equal("InCrime")));
			
			one(messageSender).sendMessage(with(new AllOf<ClientConfigurationMessage>(conditions)));
		}});
		
		LoginSuccessMessageProcessor processor = new LoginSuccessMessageProcessor(messageSender, friendsList, config);
		
		processor.processMessage(message);
		
		assertTrue(self.isOnline());
		assertEquals(new SessionId(100), self.getSessionId());
		assertEquals("Test User", self.getDisplayName());
		assertEquals(150L, self.getUserId());
		assertEquals("/127.0.0.1:12345", self.getAddress().toString());
	}
}
