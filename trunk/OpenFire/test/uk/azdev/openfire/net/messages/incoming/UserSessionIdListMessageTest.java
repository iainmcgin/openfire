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

import uk.azdev.openfire.testutil.TestUtils;

public class UserSessionIdListMessageTest {

	private static final String EXPECTED_TOSTRING 
		= "User Session ID List Message\n"
		+ "\t1 -> 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10\n"
		+ "\t2 -> 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10\n"
		+ "\t3 -> 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10\n"
		+ "\t4 -> 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10\n"
		+ "\t5 -> 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10\n"
		+ "\t6 -> 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10\n"
		+ "\t7 -> 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10\n"
		+ "\t8 -> 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10\n"
		+ "\t9 -> 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10";

	private UserSessionIdListMessage message;
	
	private static byte[] EXPECTED_SID 
		= new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 
		               0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10 };
	                                                   
	
	@Before
	public void setUp() throws Exception {
		message = new UserSessionIdListMessage();
	}

	@Test
	public void testGetMessageId() {
		assertEquals(UserSessionIdListMessage.USER_SESSION_ID_LIST_MESSAGE_ID, message.getMessageId());
	}

	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance() instanceof UserSessionIdListMessage);
	}

	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "usersidlist.sampledata");
		message.readMessageContent(buffer);
		
		Set<Long> userIdSet = message.getUserIdList();
		assertEquals(9, userIdSet.size());
		
		for(long userId : userIdSet) {
			TestUtils.checkArray(EXPECTED_SID, message.getSessionIdForUser(userId));
		}
	}

	@Test
	public void testWriteMessageContent() throws IOException {
		for(int i=1; i <= 9; i++) {
			message.addUserMapping(i, EXPECTED_SID);
		}
		
		TestUtils.checkMessageOutput(message, this.getClass(), "usersidlist.sampledata");
	}
	
	@Test
	public void testToString() throws IOException {
		for(int i=1; i <= 9; i++) {
			message.addUserMapping(i, EXPECTED_SID);
		}
		
		assertEquals(EXPECTED_TOSTRING, message.toString());
	}

}