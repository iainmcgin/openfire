/*
 * JFire - a Java API to access the XFire instant messaging network.
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
package uk.azdev.openfire.net;


import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import uk.azdev.openfire.net.messages.incoming.BuddyListMessage;
import uk.azdev.openfire.net.messages.incoming.LoginChallengeMessage;
import uk.azdev.openfire.net.messages.incoming.LoginSuccessMessage;
import uk.azdev.openfire.testutil.TestUtils;

public class ChannelReaderTest {

	Mockery mockContext = new Mockery();
	
	@Test
	public void testReadChannel() throws IOException {
		ReadableByteChannel channel = TestUtils.getTestResourceAsChannel(ChannelReaderTest.class, "downstream.sampledata");
		
		final MessageListener listener = mockContext.mock(MessageListener.class);
		mockContext.checking(new Expectations() {{
			one(listener).messageReceived(with(a(LoginChallengeMessage.class)));
			one(listener).messageReceived(with(a(LoginSuccessMessage.class)));
			one(listener).messageReceived(with(a(BuddyListMessage.class)));
		}});
		
		ChannelReader reader = new ChannelReader(channel);
		reader.addMessageListener(listener);
		reader.readChannel();
		
		mockContext.assertIsSatisfied();
	}
}
