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
package uk.azdev.openfire.friendlist.messageprocessors;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.azdev.openfire.ConnectionEventListener;
import uk.azdev.openfire.common.ActiveGameInfo;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;
import uk.azdev.openfire.friendlist.IFriendListener;
import uk.azdev.openfire.net.messages.incoming.FriendGameInfoMessage;
import uk.azdev.openfire.net.util.IOUtil;

@RunWith(JMock.class)
public class FriendGameInfoMessageProcessorTest {

	private static final long ALICE_ID = 10L;
	private static final long BOB_ID = 11L;
	private static final long CAROL_ID = 12L;
	private static final long DAVE_ID = 13L;

	private static final SessionId ALICE_SID = new SessionId(100);
	private static final SessionId BOB_SID = new SessionId(101);
	private static final SessionId CAROL_SID = new SessionId(102);
	private static final SessionId DAVE_SID = new SessionId(103);

	Mockery context = new JUnit4Mockery();

	@Test
	public void testProcessMessage() {
		
		final ConnectionEventListener connEventListenerMock = context.mock(ConnectionEventListener.class);
		final IFriendListener friendListenerMock = context.mock(IFriendListener.class);
		final Friend alice = new Friend(ALICE_ID, "alice", "Alice", ALICE_SID);
		final Friend bob = new Friend(BOB_ID, "bob", "Bob", BOB_SID);
		final Friend carol = new Friend(CAROL_ID, "carol", "Carol", CAROL_SID);
		final Friend dave = new Friend(DAVE_ID, "dave", "Dave", DAVE_SID);
		
		context.checking(new Expectations() {{
			one(connEventListenerMock).friendsListUpdated();
			one(friendListenerMock).gameInfoChanged(alice);
			one(friendListenerMock).gameInfoChanged(bob);
			one(friendListenerMock).gameInfoChanged(carol);
			one(friendListenerMock).gameInfoChanged(dave);
		}});
		
		FriendsList list = new FriendsList(new Friend("me"));
		list.addFriend(alice);
		list.addFriend(bob);
		list.addFriend(carol);
		list.updateFriendGame(CAROL_SID, new ActiveGameInfo(1001L, IOUtil.getInetSocketAddress(0xC0A8000C, 10001)));
		list.addFriend(dave);

		list.addFriendListener(alice, friendListenerMock);
		list.addFriendListener(bob, friendListenerMock);
		list.addFriendListener(carol, friendListenerMock);
		list.addFriendListener(dave, friendListenerMock);
		
		FriendGameInfoMessageProcessor processor = new FriendGameInfoMessageProcessor(list, connEventListenerMock);
		
		FriendGameInfoMessage message = new FriendGameInfoMessage();
		ActiveGameInfo aliceGame = new ActiveGameInfo(1000L, IOUtil.getInetSocketAddress(0xC0A8000A, 10000));
		message.addActiveGameInfo(ALICE_SID, aliceGame);
		message.addActiveGameInfo(BOB_SID, aliceGame);
		message.addActiveGameInfo(CAROL_SID, null);
		ActiveGameInfo daveGame = new ActiveGameInfo(1002L, null);
		message.addActiveGameInfo(DAVE_SID, daveGame);
		
		processor.processMessage(message);
		
		assertEquals(aliceGame, list.getFriend(ALICE_ID).getGame());
		assertEquals(aliceGame, list.getFriend(BOB_ID).getGame());
		assertNull(list.getFriend(CAROL_ID).getGame());
		assertEquals(daveGame, list.getFriend(DAVE_ID).getGame());
	}

}
