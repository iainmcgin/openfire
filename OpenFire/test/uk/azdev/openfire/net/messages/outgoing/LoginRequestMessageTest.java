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
package uk.azdev.openfire.net.messages.outgoing;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.net.messages.outgoing.LoginRequestMessage;
import uk.azdev.openfire.net.util.IOUtil;
import uk.azdev.openfire.testutil.TestUtils;


public class LoginRequestMessageTest {

	private LoginRequestMessage message;
	
	@Before
	public void setUp() {
		message = new LoginRequestMessage();
	}
	
	@Test
	public void testWriteLoginRequestMessage() throws IOException {
		message.setFlags(0);
		message.setUsername("testuser");
		message.setSalt("d3cd8b9eacb901fc153858786b047d1bb826ea75");
		message.setPassword("testpass");
		
		byte[] expectedBytes = TestUtils.getByteArrayForResource(this.getClass(), "loginrequest.sampledata");
		
		assertEquals(expectedBytes.length, message.getMessageContentSize());
		
		ByteBuffer buffer = IOUtil.createBuffer(expectedBytes.length);
		message.writeMessageContent(buffer);
		TestUtils.checkBytes(expectedBytes, buffer);
	}
	
	@Test
	public void testReadLoginRequestMessage() throws IOException {
		ByteBuffer messageBuffer = TestUtils.getByteBufferForResource(this.getClass(), "loginrequest.sampledata");
		
		message.readMessageContent(messageBuffer);
		
		assertEquals("testuser", message.getUsername());
		assertEquals("d4ea378c5dde16ad506152b9a40d6f42471e8c5f", message.getSaltedPassword());
		assertEquals(0L, message.getFlags());
	}
	
	@Test
	public void testMessageId() {
		assertEquals(LoginRequestMessage.LOGIN_REQUEST_MESSAGE_ID, message.getMessageId());
	}
	
	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance() instanceof LoginRequestMessage);
	}
	
	@Test
	public void testToString() {
		message.setUsername("testuser");
		message.setSalt("d3cd8b9eacb901fc153858786b047d1bb826ea75");
		message.setPassword("testpass");
		message.setFlags(100);
		assertEquals("Login request message\n\tUser name: testuser\n\tSalted password: d4ea378c5dde16ad506152b9a40d6f42471e8c5f\n\tFlags: 100", message.toString());
	}

	@Test(expected=IllegalStateException.class)
	public void testSetPasswordBeforeSalt() {
		message.setPassword("apassword");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetNegativeFlags() {
		message = new LoginRequestMessage();
		message.setFlags(-1L);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetHugeFlag() {
		message = new LoginRequestMessage();
		message.setFlags(9000000000L);
	}

}
