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
package uk.azdev.openfire.friendlist;

import static org.junit.Assert.*;

import java.net.InetSocketAddress;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.friendlist.Friend;

public class FriendTest {

	private Friend friend;
	
	@Before
	public void setUp() throws Exception {
		friend = new Friend(100L, "testUser", "Test User");
	}

	@Test
	public void testGetUserId() {
		assertEquals(100L, friend.getUserId());
		friend.setUserId(200L);
		assertEquals(200L, friend.getUserId());
	}

	@Test
	public void testGetUserName() {
		assertEquals("testUser", friend.getUserName());
	}

	@Test
	public void testGetDisplayName() {
		assertEquals("Test User", friend.getDisplayName());
		friend.setDisplayName("New display name");
		assertEquals("New display name", friend.getDisplayName());
	}
	
	@Test
	public void testGetDisplayName_withNoAliasSet() {
		Friend f = new Friend("testFriend");
		assertEquals("testFriend", f.getDisplayName());
		f.setDisplayName("Test Friend");
		assertEquals("Test Friend", f.getDisplayName());
	}

	@Test
	public void testGetStatusString() {
		friend.setStatus("(AFK)");
		assertEquals("(AFK)", friend.getStatus());
	}

	@Test
	public void testIsOnline() {
		assertFalse(friend.isOnline());
		friend.setOnline(new SessionId());
		assertTrue(friend.isOnline());
	}
	
	@Test
	public void testGetSessionId() {
		assertNull(friend.getSessionId());
		SessionId sessionId = new SessionId();
		friend.setOnline(sessionId);
		assertSame(sessionId, friend.getSessionId());
	}

	@Test
	public void testSetOffline() {
		friend.setOnline(new SessionId());
		friend.setAddress(InetSocketAddress.createUnresolved("127.0.0.1", 20000));
		assertTrue(friend.isOnline());
		
		friend.setOffline();
		assertFalse(friend.isOnline());
		assertNull(friend.getSessionId());
		assertNull(friend.getAddress());
	}
	
	@Test
	public void testSetAddress() {
		friend.setAddress(InetSocketAddress.createUnresolved("127.0.0.1", 20000));
		assertNotNull(friend.getAddress());
		assertEquals("127.0.0.1:20000", friend.getAddress().toString());
	}
	
	@Test
	public void testUpdate() {
		Friend updatedFriend = new Friend(100L, "testUser", "New Name");
		updatedFriend.setOnline(new SessionId(10));
		updatedFriend.setStatus("AFK");
		friend.update(updatedFriend);
		assertEquals("New Name", updatedFriend.getDisplayName());
		assertEquals(new SessionId(10), friend.getSessionId());
		assertEquals("AFK", friend.getStatus());
	}
	
	@Test
	public void testUpdate_ignoresChangeInId() {
		Friend updatedFriend = new Friend(101L, "testUser", "Test User");
		friend.update(updatedFriend);
		assertEquals(100L, friend.getUserId());
	}
	
	@Test
	public void testUpdate_withFirstUserName() {
		Friend origFriend = new Friend(100L, null, null);
		Friend updatedFriend = new Friend(100L, "aUser", "A User");
		origFriend.update(updatedFriend);
		assertEquals("aUser", origFriend.getUserName());
	}
	
	@Test
	public void testUpdate_ignoresNewUserName() {
		Friend updatedFriend = new Friend(100L, "newUserName", "Test User");
		friend.update(updatedFriend);
		assertEquals("testUser", friend.getUserName());
	}
	
	@Test
	public void testUpdate_ignoresNulls() {
		Friend updatedFriend = new Friend(101L, null, null);
		friend.update(updatedFriend);
		assertEquals("testUser", friend.getUserName());
		assertEquals("Test User", friend.getDisplayName());
	}
	
	@Test
	public void testToString() {
		assertEquals("Friend[uid=100, uName=testUser, display=\"Test User\"]", friend.toString());
		
		Friend withNoUid = new Friend("user1");
		assertEquals("Friend[uid=???, uName=user1, display=???]", withNoUid.toString());
		
		Friend withNoUserName = new Friend(100L);
		assertEquals("Friend[uid=100, uName=???, display=???]", withNoUserName.toString());
	}
	
	@Test
	public void testEqualsAndHashCode() {
		Friend a = new Friend(100L, "friendA", "Friend A");
		Friend b = new Friend(100L, "friendA", "Friend A");
		Friend c = new Friend(101L, "friendB", "Friend B");
		
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
		assertFalse(a.equals(c));
		assertFalse(c.equals(a));
		assertFalse(a.equals(new Object()));
		
		assertTrue(a.hashCode() == b.hashCode());
		assertFalse(a.hashCode() == c.hashCode());
	}

}
