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

import java.util.ArrayList;

import org.hamcrest.Matcher;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.core.AllOf;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.incoming.LoginSuccessMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientConfigurationMessage;


public class LoginSuccessMessageProcessorTest {

	Mockery context = new JUnit4Mockery();
	
	@Test
	public void testProcessMessage() {
		LoginSuccessMessage message = new LoginSuccessMessage();
		message.setSessionId(new SessionId(100));
		
		OpenFireConfiguration config = new OpenFireConfiguration();
		config.setClientLanguage("de");
		config.setActiveSkin("MySkin");
		config.setActiveTheme("MyTheme");
		config.setPartner("InCrime");
		
		Friend self = new Friend("me");
		
		final IMessageSender messageSender = context.mock(IMessageSender.class);
		context.checking(new Expectations() {{
			ArrayList<Matcher<ClientConfigurationMessage>> conditions = new ArrayList<Matcher<ClientConfigurationMessage>>();
			conditions.add(new HasPropertyWithValue<ClientConfigurationMessage>("language", equal("de")));
			conditions.add(new HasPropertyWithValue<ClientConfigurationMessage>("activeSkin", equal("MySkin")));
			conditions.add(new HasPropertyWithValue<ClientConfigurationMessage>("activeTheme", equal("MyTheme")));
			conditions.add(new HasPropertyWithValue<ClientConfigurationMessage>("partner", equal("InCrime")));
			
			one(messageSender).sendMessage(with(new AllOf<ClientConfigurationMessage>(conditions)));
		}});
		
		LoginSuccessMessageProcessor processor = new LoginSuccessMessageProcessor(messageSender, self, config);
		
		processor.processMessage(message);
		context.assertIsSatisfied();
	}
}
