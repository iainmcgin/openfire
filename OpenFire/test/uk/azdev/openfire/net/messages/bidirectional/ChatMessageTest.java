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
package uk.azdev.openfire.net.messages.bidirectional;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.net.messages.bidirectional.ChatMessage;
import uk.azdev.openfire.testutil.TestUtils;

public class ChatMessageTest {

	private ChatMessage message;
	
	private static final byte[] EXPECTED_SID 
		= new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
		               0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10
					 };
	
	
	@Before
	public void setUp() throws Exception {
		message = new ChatMessage();
	}

	@Test
	public void testReadMessageContent_withContentMessage() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "chat_message_content.sampledata");
		message.readMessageContent(buffer);
		
		assertEquals(new SessionId(EXPECTED_SID), message.getSessionId());
		assertTrue(message.isContentMessage());
		assertFalse(message.isTypingMessage());
		assertFalse(message.isPeerInfoMessage());
		assertFalse(message.isAckMessage());
		assertEquals(2L, message.getMessageIndex());
		assertEquals("a hoy hoy", message.getMessage());
	}
	
	@Test
	public void testReadMessageContent_withTypingStatusMessage() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "chat_message_typing.sampledata");
		message.readMessageContent(buffer);
		
		assertEquals(new SessionId(EXPECTED_SID), message.getSessionId());
		assertTrue(message.isTypingMessage());
		assertFalse(message.isContentMessage());
		assertFalse(message.isPeerInfoMessage());
		assertFalse(message.isAckMessage());
		assertEquals(1L, message.getMessageIndex());
		assertEquals(1L, message.getTypingVal());
	}
	
	@Test
	public void testReadMessageContent_withAckMessage() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "chat_message_ack.sampledata");
		message.readMessageContent(buffer);
		
		assertEquals(new SessionId(EXPECTED_SID), message.getSessionId());
		assertTrue(message.isAckMessage());
		assertFalse(message.isTypingMessage());
		assertFalse(message.isContentMessage());
		assertFalse(message.isPeerInfoMessage());
		assertEquals(1L, message.getMessageIndex());
	}
	
	@Test
	public void testReadMessageContent_withPeerInfoMessage() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "chat_message_peerinfo.sampledata");
		message.readMessageContent(buffer);
		
		assertEquals(new SessionId(EXPECTED_SID), message.getSessionId());
		assertTrue(message.isPeerInfoMessage());
		assertFalse(message.isTypingMessage());
		assertFalse(message.isContentMessage());
		assertFalse(message.isAckMessage());
		
		assertEquals("/100.100.100.100:10000", message.getNetAddress().toString());
		assertEquals("/192.168.0.1:30000", message.getLocalAddress().toString());
		assertEquals(100L, message.getStatus());
		assertEquals("cfb67a640fc290e41ce5e41bc6b5ad22847af2a3", message.getSalt());
	}

	@Test
	public void testWriteMessageContent_contentMessage() throws IOException {
		message.setSessionId(new SessionId(EXPECTED_SID));
		message.setContentPayload(2L, "a hoy hoy");
		TestUtils.checkMessageOutput(message, this.getClass(), "chat_message_content.sampledata");
	}
	
	@Test
	public void testWriteMessageContent_typingMessage() throws IOException {
		message.setSessionId(new SessionId(EXPECTED_SID));
		message.setTypingPayload(1L, 1L);
		TestUtils.checkMessageOutput(message, this.getClass(), "chat_message_typing.sampledata");
	}
	
	@Test
	public void testWriteMessageContent_withAckMessage() throws IOException {
		message.setSessionId(new SessionId(EXPECTED_SID));
		message.setAcknowledgementPayload(1L);
		TestUtils.checkMessageOutput(message, this.getClass(), "chat_message_ack.sampledata");
	}
	
	@Test
	public void testGetMessageId() {
		assertEquals(ChatMessage.TYPE_ID, message.getMessageId());
	}
	
	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance().getClass() == ChatMessage.class);
	}
	
	private static final Object EXPECTED_TO_STRING_CONTENT_MSG
		= "Chat Message\n" +
		  "\tPeer SID: SID:<01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10>\n" +
		  "\tType: Content\n" +
		  "\tMessage index: 2\n" +
		  "\tMessage: \"a hoy hoy\"";
	@Test
	public void testToString_withContentMessage() {
		message.setSessionId(new SessionId(EXPECTED_SID));
		message.setContentPayload(2L, "a hoy hoy");
		assertEquals(EXPECTED_TO_STRING_CONTENT_MSG, message.toString());
	}
	
	private static final Object EXPECTED_TO_STRING_TYPING_MSG
	= "Chat Message\n" +
	  "\tPeer SID: SID:<01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10>\n" +
	  "\tType: Typing\n" +
	  "\tMessage index: 1\n" +
	  "\tTyping val: 1";
	
	@Test
	public void testToString_withTypingMessage() {
		message.setSessionId(new SessionId(EXPECTED_SID));
		message.setTypingPayload(1L, 1L);
		assertEquals(EXPECTED_TO_STRING_TYPING_MSG, message.toString());
	}

}
