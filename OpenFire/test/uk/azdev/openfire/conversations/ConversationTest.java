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
package uk.azdev.openfire.conversations;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.core.AllOf;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.bidirectional.ChatMessage;

@RunWith(JMock.class)
public class ConversationTest {

	private Mockery context = new JUnit4Mockery();
	
	private Friend self;
	private Friend peer;
	private IMessageSender messageSender;
	private OpenFireConfiguration config;
	private Conversation conv;

	private IConversationListener list1;

	private IConversationListener list2;
	
	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery();

		self = new Friend(100L, "me", "Me", new SessionId(1000));
		FriendsList friendsList = new FriendsList(self);
		peer = new Friend(200L, "alice", "Alice", new SessionId(2000));
		friendsList.addFriend(peer);
		messageSender = context.mock(IMessageSender.class);
		config = new OpenFireConfiguration();
		
		conv = new Conversation(friendsList, peer.getUserId(), messageSender, config);
		
		list1 = context.mock(IConversationListener.class);
		list2 = context.mock(IConversationListener.class);
		conv.addChatListener(list1);
		conv.addChatListener(list2);
	}

	@Test
	public void testReceiveMessage_typing() {
		context.checking(new Expectations() {{
			one(list1).peerIsTyping();
			one(list2).peerIsTyping();
			
			List<Matcher<ChatMessage>> clientInfoMessageConditions = new LinkedList<Matcher<ChatMessage>>();
			clientInfoMessageConditions.add(a(ChatMessage.class));
			clientInfoMessageConditions.add(new HasPropertyWithValue<ChatMessage>("peerInfoMessage", equal(true)));
			one(messageSender).sendMessage(with(new AllOf<ChatMessage>(clientInfoMessageConditions)));
		}});
		
		ChatMessage message = new ChatMessage();
		message.setTypingPayload(10L, 1L);
		conv.receiveMessage(message);
	}
	
	@Test
	public void testReceiveMessage_typingStopped() {
		context.checking(new Expectations() {{
			one(list1).peerIsNotTyping();
			one(list2).peerIsNotTyping();
			
			List<Matcher<ChatMessage>> clientInfoMessageConditions = new LinkedList<Matcher<ChatMessage>>();
			clientInfoMessageConditions.add(a(ChatMessage.class));
			clientInfoMessageConditions.add(new HasPropertyWithValue<ChatMessage>("peerInfoMessage", equal(true)));
			one(messageSender).sendMessage(with(new AllOf<ChatMessage>(clientInfoMessageConditions)));
		}});
		
		ChatMessage message = new ChatMessage();
		message.setTypingPayload(10L, 0L);
		conv.receiveMessage(message);
	}
	
	@Test
	public void testReceiveMessage_chatMessage() {
		context.checking(new Expectations() {{
			one(list1).chatLogUpdated();
			one(list2).chatLogUpdated();
			
			List<Matcher<ChatMessage>> clientInfoMessageConditions = new LinkedList<Matcher<ChatMessage>>();
			clientInfoMessageConditions.add(a(ChatMessage.class));
			clientInfoMessageConditions.add(new HasPropertyWithValue<ChatMessage>("peerInfoMessage", equal(true)));
			one(messageSender).sendMessage(with(new AllOf<ChatMessage>(clientInfoMessageConditions)));
		}});
		
		ChatMessage message = new ChatMessage();
		message.setContentPayload(10L, "Hello!");
		conv.receiveMessage(message);
		
		assertEquals("Hello!", conv.getLastMessage().getMessage());
		assertEquals("Alice", conv.getLastMessage().getOriginator().getDisplayName());
	}

	@Test
	public void testSendMessage() {
		context.checking(new Expectations() {{
			List<Matcher<ChatMessage>> firstMessageConditions = new LinkedList<Matcher<ChatMessage>>();
			firstMessageConditions.add(a(ChatMessage.class));
			firstMessageConditions.add(new HasPropertyWithValue<ChatMessage>("contentMessage", equal(true)));
			firstMessageConditions.add(new HasPropertyWithValue<ChatMessage>("messageIndex", equal(1L)));
			firstMessageConditions.add(new HasPropertyWithValue<ChatMessage>("message", equal("hello")));
			one(messageSender).sendMessage(with(new AllOf<ChatMessage>(firstMessageConditions)));
			
			List<Matcher<ChatMessage>> clientInfoMessageConditions = new LinkedList<Matcher<ChatMessage>>();
			clientInfoMessageConditions.add(a(ChatMessage.class));
			clientInfoMessageConditions.add(new HasPropertyWithValue<ChatMessage>("peerInfoMessage", equal(true)));
			one(messageSender).sendMessage(with(new AllOf<ChatMessage>(clientInfoMessageConditions)));
			
			List<Matcher<ChatMessage>> secondMessageConditions = new LinkedList<Matcher<ChatMessage>>();
			secondMessageConditions.add(a(ChatMessage.class));
			secondMessageConditions.add(new HasPropertyWithValue<ChatMessage>("contentMessage", equal(true)));
			secondMessageConditions.add(new HasPropertyWithValue<ChatMessage>("messageIndex", equal(2L)));
			secondMessageConditions.add(new HasPropertyWithValue<ChatMessage>("message", equal("how are you")));
			one(messageSender).sendMessage(with(new AllOf<ChatMessage>(secondMessageConditions)));
			
			exactly(2).of(list1).chatLogUpdated();
			exactly(2).of(list2).chatLogUpdated();
		}});
		
		conv.sendMessage("hello");
		conv.sendMessage("how are you");
	}

	@Test
	public void testGetChatLog() {
		context.checking(new Expectations() {{
			ignoring(list1);
			ignoring(list2);
			ignoring(messageSender);
		}});
		
		conv.sendMessage("hello");
		conv.receiveMessage(createResponseMessage(1L, "hey"));
		conv.sendMessage("how are you");
		conv.receiveMessage(createResponseMessage(1L, "aye not bad"));
		
		assertEquals("Me: hello\nAlice: hey\nMe: how are you\nAlice: aye not bad\n", conv.getChatLog(false));
	}

	private ChatMessage createResponseMessage(long messageIndex, String message) {
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setSessionId(new SessionId(2000));
		chatMessage.setContentPayload(messageIndex, message);
		
		return chatMessage;
	}

	@Test
	public void testGetLastMessage() {
		context.checking(new Expectations() {{
			ignoring(list1);
			ignoring(list2);
			ignoring(messageSender);
		}});
		
		conv.sendMessage("hello");
		assertEquals("Me: hello\n", conv.getLastMessage().toString(false));
		conv.receiveMessage(createResponseMessage(1L, "hey"));
		assertEquals("Alice: hey\n", conv.getLastMessage().toString(false));
		conv.sendMessage("how are you");
		assertEquals("Me: how are you\n", conv.getLastMessage().toString(false));
	}
	
	@Test
	public void testGetMessageCount() {
		context.checking(new Expectations() {{
			ignoring(list1);
			ignoring(list2);
			ignoring(messageSender);
		}});
		
		conv.sendMessage("hello");
		conv.receiveMessage(createResponseMessage(1L, "hey"));
		conv.sendMessage("how are you");
		assertEquals(3, conv.getNumberMessages());
	}
	
	@Test
	public void testGetMessageByIndex() {
		context.checking(new Expectations() {{
			ignoring(list1);
			ignoring(list2);
			ignoring(messageSender);
		}});
		
		conv.sendMessage("hello");
		conv.receiveMessage(createResponseMessage(1L, "hey"));
		conv.sendMessage("how are you");
		
		ConversationLogLine logLine = conv.getChatLogLine(1);
		assertEquals("hey", logLine.getMessage());
		assertEquals(conv.getPeer(), logLine.getOriginator());
		assertEquals(conv.getSelf(), conv.getChatLogLine(2).getOriginator());
	}
	
	@Test
	public void testGetPeer() {
		assertEquals(peer, conv.getPeer());
	}

}
