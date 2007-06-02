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

import org.hamcrest.beans.HasPropertyWithValue;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.incoming.LoginChallengeMessage;

@RunWith(JMock.class)
public class LoginChallengeMessageProcessorTest {

	Mockery context = new JUnit4Mockery();
	
	@Test
	public void testProcessMessage() {
		OpenFireConfiguration config = new OpenFireConfiguration();
		
		config.setUsername("testuser");
		config.setPassword("testpass");
		
		LoginChallengeMessage message = new LoginChallengeMessage();
		message.setSalt("d3cd8b9eacb901fc153858786b047d1bb826ea75");
		
		final String expectedSaltedPass = "25e1911c4a08f46e864f156335c54e68aed701f0";
		final IMessageSender controller = context.mock(IMessageSender.class);
		context.checking(new Expectations() {{
			one(controller).sendMessage(with(new HasPropertyWithValue<LoginChallengeMessage>("saltedPassword", equal(expectedSaltedPass))));
		}});
		
		LoginChallengeMessageProcessor processor = new LoginChallengeMessageProcessor(controller, config);
		processor.processMessage(message);
	}

}
