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
package uk.azdev.openfire;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.azdev.openfire.common.Invitation;
import uk.azdev.openfire.common.ReceivedInvitation;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.net.IMessageSender;

@RunWith(JMock.class)
public class ConnectionEventDispatcherTest {

	private Mockery context = new JUnit4Mockery();
	private ConnectionEventListener listener1;
	private ConnectionEventListener listener2;
	private ConnectionEventListener listener3;
	
	private ConnectionEventDispatcher dispatcher;
	
	
	@Before
	public void setUp() throws Exception {
		listener1 = context.mock(ConnectionEventListener.class);
		listener2 = context.mock(ConnectionEventListener.class);
		listener3 = context.mock(ConnectionEventListener.class);
		
		dispatcher = new ConnectionEventDispatcher();
		dispatcher.addListener(listener1);
		dispatcher.addListener(listener2);
		dispatcher.addListener(listener3);
	}

	@Test
	public void testConnectionError() {
		context.checking(new Expectations() {{
			one(listener1).connectionError();
			one(listener2).connectionError();
			one(listener3).connectionError();
		}});
		
		dispatcher.connectionError();
	}

	@Test
	public void testConversationUpdate() {
		final SessionId peerId = new SessionId(100);
		context.checking(new Expectations() {{
			one(listener1).conversationUpdate(with(same(peerId)));
			one(listener2).conversationUpdate(with(same(peerId)));
			one(listener3).conversationUpdate(with(same(peerId)));
		}});
		
		dispatcher.conversationUpdate(peerId);
	}

	@Test
	public void testDisconnected() {
		context.checking(new Expectations() {{
			one(listener1).disconnected();
			one(listener2).disconnected();
			one(listener3).disconnected();
		}});
		
		dispatcher.disconnected();
	}

	@Test
	public void testFriendsListUpdated() {
		context.checking(new Expectations() {{
			one(listener1).friendsListUpdated();
			one(listener2).friendsListUpdated();
			one(listener3).friendsListUpdated();
		}});
		
		dispatcher.friendsListUpdated();
	}

	@Test
	public void testLoginFailed() {
		context.checking(new Expectations() {{
			one(listener1).loginFailed();
			one(listener2).loginFailed();
			one(listener3).loginFailed();
		}});
		
		dispatcher.loginFailed();
	}

	@Test
	public void testInternalError() {
		final Exception e = new Exception("test internal error");
		context.checking(new Expectations() {{
			one(listener1).internalError(e);
			one(listener2).internalError(e);
			one(listener3).internalError(e);
		}});
		
		dispatcher.internalError(e);
	}
	
	@Test
	public void testInviteReceived() {
		Invitation inviteData = new Invitation("bob", "Bob", "hello");
		final ReceivedInvitation invite = new ReceivedInvitation(inviteData, context.mock(IMessageSender.class));
		context.checking(new Expectations() {{
			one(listener1).inviteReceived(with(same(invite)));
			one(listener2).inviteReceived(with(same(invite)));
			one(listener3).inviteReceived(with(same(invite)));
		}});
		
		dispatcher.inviteReceived(invite);
	}

}
