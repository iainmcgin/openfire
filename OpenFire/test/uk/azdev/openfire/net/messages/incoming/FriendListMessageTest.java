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
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.testutil.TestUtils;

public class FriendListMessageTest {

	private static final String EXPECTED_TOSTRING 
		= "Friend List Message\n"
		+ "\tUsers:\n"
		+ "\t1 -> user1 \"user1Nick\"\n"
		+ "\t2 -> user2 \"user2Nick\"\n"
		+ "\t3 -> user3 \"user3Nick\"\n"
		+ "\t4 -> user4 \"user4Nick\"\n"
		+ "\t5 -> user5 \"user5Nick\"\n"
		+ "\t6 -> user6 \"user6Nick\"\n"
		+ "\t7 -> user7 \"user7Nick\"\n"
		+ "\t8 -> user8 \"user8Nick\"\n"
		+ "\t9 -> user9 \"user9Nick\"\n"
		+ "\t10 -> user10 \"user10Nick\"";
	
	private FriendListMessage message;
	
	@Before
	public void setUp() throws Exception {
		message = new FriendListMessage();
	}

	@Test
	public void testGetMessageId() {
		assertEquals(FriendListMessage.FRIEND_LIST_MESSAGE_ID, message.getMessageId());
	}

	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance() instanceof FriendListMessage);
	}

	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "friendlist.sampledata");
		message.readMessageContent(buffer);
		
		List<Long> userIdList = message.getUserIdList();
		assertEquals(10, userIdList.size());
		
		for(long i=1; i <= 10; i++) {
			assertTrue(userIdList.contains(i));
			assertEquals("user" + i, message.getUsernameForId(i));
			assertEquals("user" + i + "Nick", message.getUserNickForId(i));
		}
	}

	@Test
	public void testWriteMessageContent() throws IOException {
		for(int i=1; i <= 10; i++) {
			message.addUser(i, "user" + i, "user" + i + "Nick");
		}
		
		TestUtils.checkMessageOutput(message, this.getClass(), "friendlist.sampledata");
	}
	
	@Test
	public void testToString() {
		for(int i=1; i <= 10; i++) {
			message.addUser(i, "user" + i, "user" + i + "Nick");
		}
		
		assertEquals(EXPECTED_TOSTRING, message.toString());
	}

}
