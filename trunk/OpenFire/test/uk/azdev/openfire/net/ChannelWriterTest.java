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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

import org.junit.Test;

import uk.azdev.openfire.net.messages.outgoing.ClientInformationMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientVersionMessage;
import uk.azdev.openfire.net.messages.outgoing.LoginRequestMessage;
import uk.azdev.openfire.testutil.TestUtils;

public class ChannelWriterTest {

	@Test
	public void testWriteMessages() throws IOException {
		
		ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
		WritableByteChannel byteChannel = Channels.newChannel(bytesStream);
		
		ChannelWriter writer = new ChannelWriter(byteChannel);
		
		writer.writeOpeningStatement();
		writer.writeMessage(createTestClientInfoMessage());
		writer.writeMessage(createTestClientVersionMessage());
		writer.writeMessage(createTestLoginRequestMessage());
		
		writer.close();
		byte[] writtenBytes = bytesStream.toByteArray();
		
		byte[] expectedBytes = TestUtils.getByteArrayForResource(ChannelWriterTest.class, "upstream.sampledata");
		TestUtils.checkArray(expectedBytes, writtenBytes);
	}
	
	@Test
	public void testClose() throws IOException {
		ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
		WritableByteChannel byteChannel = Channels.newChannel(bytesStream);
		ChannelWriter writer = new ChannelWriter(byteChannel);
		
		assertTrue(byteChannel.isOpen());
		writer.close();
		assertFalse(byteChannel.isOpen());
	}
	
	private ClientInformationMessage createTestClientInfoMessage() {
		ClientInformationMessage message = new ClientInformationMessage();
		message.setVersion("3.2.0.0");
		message.setSkinList(new String[] { "Xfire", "standard", "Separator", "XF_URL" });
		
		return message;
	}
	
	private ClientVersionMessage createTestClientVersionMessage() {
		ClientVersionMessage message = new ClientVersionMessage();
		message.setVersion(67);
		
		return message;
	}
	
	private LoginRequestMessage createTestLoginRequestMessage() {
		LoginRequestMessage message = new LoginRequestMessage();
		message.setFlags(0);
		message.setUsername("testuser");
		message.setSalt("d3cd8b9eacb901fc153858786b047d1bb826ea75");
		message.setPassword("testpass");
		
		return message;
	}
	
}
