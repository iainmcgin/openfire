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

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.azdev.openfire.ConnectionEventListener;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.conversations.IConversation;
import uk.azdev.openfire.conversations.IConversationStore;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.bidirectional.ChatMessage;

@RunWith(JMock.class)
public class ChatMessageProcessorTest {
	
	private static final int OTHER_SID = 1000;
	
	Mockery context = new JUnit4Mockery();
	
	@Test
	public void testProcessMessage_forContentMessage() {
		final IConversationStore conversationStore = context.mock(IConversationStore.class);
		final ConnectionEventListener connListener = context.mock(ConnectionEventListener.class);
		final IMessageSender messageSender = context.mock(IMessageSender.class);
		final IConversation conversation = context.mock(IConversation.class);
		
		ChatMessageProcessor processor = new ChatMessageProcessor(conversationStore, connListener, messageSender);
		
		final ChatMessage message = new ChatMessage();
		message.setSessionId(new SessionId(OTHER_SID));
		message.setContentPayload(1L, "hello");
		
		final ChatMessage expectedAckMessage = new ChatMessage();
		expectedAckMessage.setSessionId(new SessionId(OTHER_SID));
		expectedAckMessage.setAcknowledgementPayload(1L);
		
		context.checking(new Expectations() {{
			one(conversationStore).getConversation(new SessionId(OTHER_SID));
			will(returnValue(conversation));
			
			one(conversation).receiveMessage(with(equal(message)));
			one(messageSender).sendMessage(with(equal(expectedAckMessage)));
			one(connListener).conversationUpdate(new SessionId(OTHER_SID));
		}});
		
		processor.processMessage(message);
	}
	
	@Test
	public void testProcessMessage_forNonContentMessage() {
		final IConversationStore conversationStore = context.mock(IConversationStore.class);
		final ConnectionEventListener connListener = context.mock(ConnectionEventListener.class);
		final IMessageSender messageSender = context.mock(IMessageSender.class);
		final IConversation conversation = context.mock(IConversation.class);
		
		ChatMessageProcessor processor = new ChatMessageProcessor(conversationStore, connListener, messageSender);
		
		final ChatMessage message = new ChatMessage();
		message.setSessionId(new SessionId(OTHER_SID));
		message.setAcknowledgementPayload(1L);
		
		context.checking(new Expectations() {{
			one(conversationStore).getConversation(new SessionId(OTHER_SID));
			will(returnValue(conversation));
			
			one(conversation).receiveMessage(with(equal(message)));
			never(connListener).conversationUpdate(new SessionId(OTHER_SID));
		}});
		
		processor.processMessage(message);
	}
	
	

}
