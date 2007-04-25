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

import java.util.List;

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
	public void testGetStatusString() {
		friend.setStatusString("(AFK)");
		assertEquals("(AFK)", friend.getStatusString());
	}

	@Test
	public void testGetFriends() {
		assertEquals(0, friend.getFriends().size());
		Friend friendA = new Friend(101L, "friendA", "Friend A");
		Friend friendB = new Friend(102L, "friendB", "Friend B");
		
		friend.addFriend(friendA);
		friend.addFriend(friendB);
		
		List<Friend> friends = friend.getFriends();
		assertEquals(2, friends.size());
		assertTrue(friends.contains(friendA));
		assertTrue(friends.contains(friendB));
	}
	
	@Test
	public void testAddFriendIsMutual() {
		Friend friendA = new Friend(101L, "friendA", "Friend A");
		
		assertEquals(0, friend.getFriends().size());
		assertEquals(0, friendA.getFriends().size());
		
		friend.addFriend(friendA);
		
		assertEquals(1, friend.getFriends().size());
		assertEquals(1, friendA.getFriends().size());
		
		assertTrue(friend.getFriends().contains(friendA));
		assertTrue(friendA.getFriends().contains(friend));
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
		assertTrue(friend.isOnline());
		friend.setOffline();
		assertFalse(friend.isOnline());
		assertNull(friend.getSessionId());
	}

}
