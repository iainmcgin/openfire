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
package uk.azdev.openfire.net.messages.incoming;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.testutil.TestUtils;

public class FriendStatusMessageTest {

	private FriendStatusMessage message;
	
	@Before
	public void setUp() throws Exception {
		message = new FriendStatusMessage();
	}

	@Test
	public void testGetMessageId() {
		assertEquals(FriendStatusMessage.FRIEND_STATUS_MESSAGE_ID, message.getMessageId());
	}

	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance() instanceof FriendStatusMessage);
	}

	private static final SessionId EXPECTED_SESSION_ID
		= new SessionId(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
					                 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10 });

	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "friendstatus.sampledata");
		message.readMessageContent(buffer);
		
		Set<SessionId> sessionIdSet = message.getSessionIdSet();
		
		assertEquals(1, sessionIdSet.size());
		SessionId sessionId = sessionIdSet.iterator().next();
		assertEquals(EXPECTED_SESSION_ID, sessionId);
		assertEquals("(AFK) Away From Keyboard", message.getStatusForSessionId(sessionId));
	}

	@Test
	public void testWriteMessageContent() throws IOException {
		message.addStatus(EXPECTED_SESSION_ID, "(AFK) Away From Keyboard");
		TestUtils.checkMessageOutput(message, this.getClass(), "friendstatus.sampledata");
	}

	private static final String EXPECTED_TOSTRING
		= "Friend Status Message\n"
		+ "\tStatuses:\n"
		+ "\t01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10 -> \"(AFK) Away From Keyboard\"";
	
	@Test
	public void testToString() {
		message.addStatus(EXPECTED_SESSION_ID, "(AFK) Away From Keyboard");
		assertEquals(EXPECTED_TOSTRING, message.toString());
	}

}
