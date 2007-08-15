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

public class OutgoingInvitationMessageTest {

	private OutgoingInvitationMessage message;
	
	@Before
	public void setUp() throws Exception {
		message = new OutgoingInvitationMessage();
	}

	@Test
	public void testGetMessageId() {
		assertEquals(OutgoingInvitationMessage.TYPE_ID, message.getMessageId());
	}

	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance().getClass().equals(OutgoingInvitationMessage.class));
	}

	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer messageBuffer = TestUtils.getByteBufferForResource(this.getClass(), "outgoing_invite.sampledata");
		message.readMessageContent(messageBuffer);
		
		assertEquals("testuser", message.getUserName());
		assertEquals("hello there please add me", message.getMessage());
	}

	@Test
	public void testWriteMessageContent() throws IOException {
		message.setUserName("testuser");
		message.setMessage("hello there please add me");
		
		TestUtils.checkMessageOutput(message, OutgoingInvitationMessageTest.class, "outgoing_invite.sampledata");
	}

}
