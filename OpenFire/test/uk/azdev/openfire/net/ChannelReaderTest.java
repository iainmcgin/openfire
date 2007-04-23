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


import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

import org.junit.Test;

import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.UnknownInt8MapBasedMessage;
import uk.azdev.openfire.net.messages.UnknownStringMapBasedMessage;
import uk.azdev.openfire.net.messages.incoming.BuddyListMessage;
import uk.azdev.openfire.net.messages.incoming.LoginChallengeMessage;
import uk.azdev.openfire.net.messages.incoming.LoginSuccessMessage;
import uk.azdev.openfire.testutil.TestUtils;

public class ChannelReaderTest {

	@Test
	public void testReadMessage() throws IOException {
		ReadableByteChannel inputChannel = TestUtils.getTestResourceAsChannel(ChannelReaderTest.class, "downstream.sampledata");
		ChannelReader reader = new ChannelReader(inputChannel);
		
		assertTrue(reader.readMessage() instanceof LoginChallengeMessage);
		assertTrue(reader.readMessage() instanceof LoginSuccessMessage);
		assertTrue(reader.readMessage() instanceof BuddyListMessage);
		
		IMessage message = reader.readMessage();
		assertTrue(message instanceof UnknownInt8MapBasedMessage);
		assertEquals(155, message.getMessageId());
		
		message = reader.readMessage();
		assertTrue(message instanceof UnknownStringMapBasedMessage);
		assertEquals(156, message.getMessageId());
		
		assertNull(reader.readMessage());
	}
	
	@Test
	public void testClose() throws IOException {
		ReadableByteChannel inputChannel = TestUtils.getTestResourceAsChannel(ChannelReaderTest.class, "downstream.sampledata");
		ChannelReader reader = new ChannelReader(inputChannel);
		
		assertTrue(inputChannel.isOpen());
		reader.close();
		assertFalse(inputChannel.isOpen());
	}
}
