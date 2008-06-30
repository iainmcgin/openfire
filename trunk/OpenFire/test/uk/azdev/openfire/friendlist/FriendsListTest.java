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
import java.util.Set;

import org.hamcrest.Matcher;
import org.hamcrest.beans.HasPropertyWithValue;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.azdev.openfire.common.ActiveGameInfo;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.net.util.IOUtil;

@RunWith(JMock.class)
public class FriendsListTest {

    Mockery context = new JUnit4Mockery();
    
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
		
		assertEquals(4, friends.size());
		assertTrue(friends.contains(alice));
		assertTrue(friends.contains(bob));
		assertTrue(friends.contains(carol));
		assertTrue(friends.contains(dave));
		assertFalse(friends.contains(self));
	}
	
	@Test
	public void testFriendUpdateNotifications() {
	    final IFriendListener listener1 = context.mock(IFriendListener.class);
	    final IFriendListener listener2 = context.mock(IFriendListener.class);
	    
	    final Friend aliceOnline = alice.clone();
	    aliceOnline.setOnline(new SessionId(1001));
	    
	    final Friend aliceOnlineWithStatus = aliceOnline.clone();
	    aliceOnlineWithStatus.setStatus("hai guys");
	    
	    context.checking(new Expectations() {{
	        one(listener1).friendOnline(with(hasProp("online", equal(true))));
	        one(listener2).friendOnline(with(hasProp("online", equal(true))));
	        one(listener1).statusChanged(with(hasProp("status", equal("hai guys"))));
	        one(listener2).statusChanged(with(hasProp("status", equal("hai guys"))));
	        one(listener1).friendOffline(with(hasProp("online", equal(false))));
	        one(listener2).friendOffline(with(hasProp("online", equal(false))));
	    }});
	    
	    friendsList.addFriendListener(alice, listener1);
	    friendsList.addFriendListener(alice, listener2);
	    
	    friendsList.setFriendOnline(alice.getUserId(), new SessionId(1001));
	    friendsList.updateFriendStatus(new SessionId(1001), "hai guys");
	    friendsList.setFriendOffline(alice.getUserId());
	    
	    context.assertIsSatisfied();
	}
	
	@Test
	public void testSetsGameInfoWhenFriendBecomesKnown() {
		alice.setOffline();
		
		ActiveGameInfo gameInfo = new ActiveGameInfo(100L, IOUtil.getInetSocketAddress(0x01020304, 10000));
		SessionId newAliceSid = new SessionId(2000);
		friendsList.updateFriendGame(newAliceSid, gameInfo);
		
		friendsList.setFriendOnline(alice.getUserId(), newAliceSid);
		
		assertEquals(gameInfo, alice.getGame());
	}
	
	private HasPropertyWithValue<Friend> hasProp(String propName, Matcher<?> expected) {
		return new HasPropertyWithValue<Friend>(propName, expected);
	}
}
