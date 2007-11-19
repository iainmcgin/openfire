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

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.common.SessionId;

public class FriendsListTest {

	private FriendsList friendsList;
	
	private Friend self;
	
	private Friend alice;
	private Friend bob;
	private Friend carol;
	private Friend dave;
	
	@Before
	public void setUp() throws Exception {
		self = new Friend("me");
		friendsList = new FriendsList(self);
		
		alice = new Friend(100L, "alice", "Alice", new SessionId(1000));
		bob = new Friend(101L, "bob", "Bob", new SessionId(1001));
		carol = new Friend(102L, "carol", "Carol", new SessionId(1002));
		dave = new Friend(103L, "dave", "Dave", new SessionId(1003));
		friendsList.addFriend(alice, self);
		friendsList.addFriend(bob, self);
		friendsList.addFriend(carol, alice);
		friendsList.addFriend(dave, bob);
	}
	
	@Test
	public void testGetFriend_byUserName() {
		assertEquals(alice, friendsList.getFriend("alice"));
		assertNull(friendsList.getFriend("someguy"));
	}
	
	@Test
	public void testContainsFriend_byUserName() {
		assertTrue(friendsList.containsFriend("alice"));
		assertFalse(friendsList.containsFriend("someguy"));
	}
	
	@Test
	public void testGetOnlineFriend() {
		Friend f = friendsList.getOnlineFriend(new SessionId(1000));
		assertEquals("Incorrect friend returned by getOnlineFriend", alice, f);
		
		assertNull(friendsList.getOnlineFriend(new SessionId(2000)));
	}
	
	@Test
	public void testGetMyFriends() {
		Set<Friend> friends = friendsList.getMyFriends();
	
		assertTrue(friends.contains(alice));
		assertTrue(friends.contains(bob));
		assertFalse(friends.contains(carol));
	}
	
	@Test
	public void testGetAllFriends() {
		List<Friend> friends = friendsList.getAllFriends();
		
		assertEquals(5, friends.size());
		assertTrue(friends.contains(alice));
		assertTrue(friends.contains(bob));
		assertTrue(friends.contains(carol));
		assertTrue(friends.contains(dave));
		assertTrue(friends.contains(self));
	}
}
