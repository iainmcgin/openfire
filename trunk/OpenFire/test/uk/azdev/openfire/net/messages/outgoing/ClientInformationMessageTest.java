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
package uk.azdev.openfire.net.messages.outgoing;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.testutil.TestUtils;

public class ClientInformationMessageTest {

	private ClientInformationMessage message;
	
	@Before
	public void setUp() {
		message = new ClientInformationMessage();
	}
	
	@Test
	public void testSetVersion() {
		message.setVersion("3.2.0.0");
		assertEquals("3.2.0.0", message.getVersion());
	}
	
	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "clientinformation.sampledata");
		message.readMessageContent(buffer);
		
		assertEquals("3.2.0.0", message.getVersion());
		assertEquals(4, message.getSkinList().length);
		assertEquals("Xfire", message.getSkinList()[0]);
		assertEquals("standard", message.getSkinList()[1]);
		assertEquals("Separator", message.getSkinList()[2]);
		assertEquals("XF_URL", message.getSkinList()[3]);
	}
	
	@Test
	public void testWriteMessageContent() throws IOException {
		message.setVersion("3.2.0.0");
		message.setSkinList(new String[] { "Xfire", "standard", "Separator", "XF_URL" });
		
		TestUtils.checkMessageOutput(message, this.getClass(), "clientinformation.sampledata");
	}
	
	@Test
	public void testGetMessageId() {
		assertEquals(ClientInformationMessage.CLIENT_INFO_MESSAGE_ID, message.getMessageId());
	}

	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance() instanceof ClientInformationMessage);
	}

	@Test
	public void testToString() {
		message.setSkinList(new String[] { "Skin1", "Skin2" });
		message.setVersion("1.2.3.4");
		assertEquals("Client information message\n\tSkin list: Skin1 Skin2 \n\tVersion: 1.2.3.4", message.toString());
	}

}
