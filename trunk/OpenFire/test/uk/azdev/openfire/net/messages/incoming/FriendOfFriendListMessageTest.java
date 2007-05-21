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
package uk.azdev.openfire.net.messages.incoming;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.testutil.TestUtils;

public class FriendOfFriendListMessageTest {

	private static final long DAVE_UID = 0x78563412L;
	private static final long ELTON_UID = 0xF0DEBC9AL;
	private static final long FIONA_UID = 0x87A9CBEDL;
	private static final long GARY_UID = 0x01214365L;
	
	private static final String DAVE_UNAME = "dave";
	private static final String ELTON_UNAME = "elton";
	private static final String FIONA_UNAME = "fiona";
	private static final String GARY_UNAME = "gary";
	
	private static final String DAVE_DNAME = "Dave";
	private static final String ELTON_DNAME = "Elton";
	private static final String FIONA_DNAME = "Fiona";
	private static final String GARY_DNAME = "Gary";
	
	private static final byte[] DAVE_SID =  { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10 };
	private static final byte[] ELTON_SID = { 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x20 };
	private static final byte[] FIONA_SID = { 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x30 };
	private static final byte[] GARY_SID =  { 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F, 0x40 };
	
	private static final long[] DAVE_CONNECTIONS =  { ELTON_UID, GARY_UID };
	private static final long[] ELTON_CONNECTIONS = { FIONA_UID, GARY_UID };
	private static final long[] FIONA_CONNECTIONS = { DAVE_UID, ELTON_UID };
	private static final long[] GARY_CONNECTIONS =  { DAVE_UID, ELTON_UID };
	
	private FriendOfFriendListMessage message;
	
	@Before
	public void setUp() throws Exception {
		message = new FriendOfFriendListMessage();
	}

	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "friendoffriendlist.sampledata");
		message.readMessageContent(buffer);
		
		assertEquals(4, message.getSecondDegreeOfSeparation().size());
		Friend dave = message.getSecondDegreeOfSeparation().get(0);
		Friend elton = message.getSecondDegreeOfSeparation().get(1);
		Friend fiona = message.getSecondDegreeOfSeparation().get(2);
		Friend gary = message.getSecondDegreeOfSeparation().get(3);
		
		checkFriend(dave, DAVE_UID, DAVE_UNAME, DAVE_DNAME, new SessionId(DAVE_SID));
		checkFriend(elton, ELTON_UID, ELTON_UNAME, ELTON_DNAME, new SessionId(ELTON_SID));
		checkFriend(fiona, FIONA_UID, FIONA_UNAME, FIONA_DNAME, new SessionId(FIONA_SID));
		checkFriend(gary, GARY_UID, GARY_UNAME, GARY_DNAME, new SessionId(GARY_SID));
		
		checkConnections(message.getThirdDegreeOfSeparationFor(dave.getUserId()), DAVE_CONNECTIONS);
		checkConnections(message.getThirdDegreeOfSeparationFor(elton.getUserId()), ELTON_CONNECTIONS);
		checkConnections(message.getThirdDegreeOfSeparationFor(fiona.getUserId()), FIONA_CONNECTIONS);
		checkConnections(message.getThirdDegreeOfSeparationFor(gary.getUserId()), GARY_CONNECTIONS);
	}

	private void checkFriend(Friend f, long expectedUserId, String expectedUserName, String expectedDisplayName, SessionId expectedSessionId) {
		assertEquals(expectedUserId, f.getUserId());
		assertEquals(expectedUserName, f.getUserName());
		assertEquals(expectedDisplayName, f.getDisplayName());
		assertEquals(expectedSessionId, f.getSessionId());
	}
	
	private void checkConnections(List<Long> friendList, long[] expectedFriendList) {
		int i = 0;
		for(long friendId : friendList) {
			assertEquals(expectedFriendList[i], friendId);
			i++;
		}
	}

	@Test
	public void testWriteMessageContent() throws IOException {
		Friend dave = new Friend(DAVE_UID, DAVE_UNAME, DAVE_DNAME);
		dave.setOnline(new SessionId(DAVE_SID));
		Friend elton = new Friend(ELTON_UID, ELTON_UNAME, ELTON_DNAME);
		elton.setOnline(new SessionId(ELTON_SID));
		Friend fiona = new Friend(FIONA_UID, FIONA_UNAME, FIONA_DNAME);
		fiona.setOnline(new SessionId(FIONA_SID));
		Friend gary = new Friend(GARY_UID, GARY_UNAME, GARY_DNAME);
		gary.setOnline(new SessionId(GARY_SID));
		
		message.addFriend(dave, toList(DAVE_CONNECTIONS));
		message.addFriend(elton, toList(ELTON_CONNECTIONS));
		message.addFriend(fiona, toList(FIONA_CONNECTIONS));
		message.addFriend(gary, toList(GARY_CONNECTIONS));
		
		TestUtils.checkMessageOutput(message, this.getClass(), "friendoffriendlist.sampledata");
	}
	
	private List<Long> toList(long[] friendIds) {
		List<Long> list = new ArrayList<Long>(friendIds.length);
		
		for(long friendId : friendIds) {
			list.add(friendId);
		}
		
		return list;
	}

	@Test
	public void testGetMessageId() {
		assertEquals(FriendOfFriendListMessage.FRIEND_OF_FRIEND_LIST_MESSAGE_TYPE, message.getMessageId());
	}

	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance().getClass() == FriendOfFriendListMessage.class);
	}

}
