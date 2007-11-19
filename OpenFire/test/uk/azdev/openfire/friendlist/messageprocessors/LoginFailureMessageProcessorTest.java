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

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import uk.azdev.openfire.net.ConnectionStateListener;
import uk.azdev.openfire.net.messages.incoming.LoginFailureMessage;

public class LoginFailureMessageProcessorTest {

	Mockery context = new JUnit4Mockery();
	
	@Test
	public void testProcessMessage() {
		final ConnectionStateListener stateListener = context.mock(ConnectionStateListener.class);
		
		context.checking(new Expectations() {{
			one(stateListener).loginFailed();
		}});
		
		LoginFailureMessage message = new LoginFailureMessage();
		message.setReason(1L);
		
		LoginFailureMessageProcessor processor = new LoginFailureMessageProcessor(stateListener);
		processor.processMessage(message);
		
		context.assertIsSatisfied();
	}

}
