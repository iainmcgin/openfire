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

import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.testutil.TestUtils;

public class OnlineFriendsRequestTest {

	private static final byte[][] EXPECTED_SIDS =
	{
		{ 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F },
		{ 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F },
		{ 0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F }
	};
	
	private OnlineFriendsRequest message;
	
	@Before
	public void setUp() throws Exception {
		message = new OnlineFriendsRequest();
	}

	@Test
	public void testGetMessageId() {
		assertEquals(OnlineFriendsRequest.TYPE_ID, message.getMessageId());
	}

	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance().getClass().equals(OnlineFriendsRequest.class));
	}

	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer messageBuffer = TestUtils.getByteBufferForResource(this.getClass(), "online_friend_request.sampledata");
		message.readMessageContent(messageBuffer);
		
		assertEquals(3, message.getSessionIds().size());
		assertEquals(new SessionId(EXPECTED_SIDS[0]), message.getSessionIds().get(0));
		assertEquals(new SessionId(EXPECTED_SIDS[1]), message.getSessionIds().get(1));
		assertEquals(new SessionId(EXPECTED_SIDS[2]), message.getSessionIds().get(2));
	}

	@Test
	public void testWriteMessageContent() throws IOException {
		for (byte[] sidBytes : EXPECTED_SIDS) {
			message.addSessionId(new SessionId(sidBytes));
		}
		
		TestUtils.checkMessageOutput(message, this.getClass(), "online_friend_request.sampledata");
	}

}
