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
package uk.azdev.openfire.net.messages.incoming;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.testutil.TestUtils;

public class LoginSuccessMessageTest {

	private static final SessionId EXPECTED_SESSION_ID 
		= new SessionId(new byte[] { (byte)0x01, (byte)0x86, (byte)0x72, (byte)0x57, 
					                 (byte)0xF0, (byte)0x94, (byte)0x83, (byte)0x45,
					                 (byte)0x96, (byte)0xDA, (byte)0x5B, (byte)0x4B,
					                 (byte)0x9A, (byte)0x44, (byte)0x41, (byte)0x35});
	
	private static final String EXPECTED_TOSTRING
		= "Login Success Message\n" +
		  "\tUser id: 4022250974\n" +
		  "\tSession id: SID:<01 86 72 57 F0 94 83 45 96 DA 5B 4B 9A 44 41 35>\n" +
		  "\tNick: \n" +
		  "\tStatus: 15\n" +
		  "\tDlset: \n" +
		  "\tP2P Set: \n" +
		  "\tClnt Set: \n" +
		  "\tMin rect: 1\n" +
		  "\tMax rect: 1800\n" +
		  "\tCtry: 826\n" +
		  "\tN1: /204.71.190.131\n" +
		  "\tN2: /204.71.190.132\n" +
		  "\tN3: /204.71.190.133\n" +
		  "\tPublic IP: /207.46.232.182";
	
	private LoginSuccessMessage message;
	
	@Before
	public void setUp() {
		message = new LoginSuccessMessage();
	}
	
	@Test
	public void testGetMessageId() {
		assertEquals(LoginSuccessMessage.LOGIN_SUCCESS_MESSAGE_ID, message.getMessageId());
	}

	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance() instanceof LoginSuccessMessage);
	}

	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "loginsuccess.sampledata");
		message.readMessageContent(buffer);
		verifyMessage(message);
	}

	@Test
	public void testWriteMessageContent() throws IOException {
		message = createTestMessage();
		TestUtils.checkMessageOutput(message, this.getClass(), "loginsuccess.sampledata");
	}

	@Test
	public void testToString() throws UnknownHostException {
		message = createTestMessage();
		assertEquals(EXPECTED_TOSTRING, message.toString());
	}
	
	public static LoginSuccessMessage createTestMessage() throws UnknownHostException {
		LoginSuccessMessage message = new LoginSuccessMessage();
		message.setUserId(4022250974L);
		message.setSessionId(EXPECTED_SESSION_ID);
		message.setNick("");
		message.setStatus(15L);
		message.setDlSet("");
		message.setP2pSet("");
		message.setClntSet("");
		message.setMinRect(1L);
		message.setMaxRect(1800L);
		message.setCtry(826L);
		message.setN1(TestUtils.createInet4Address("204.71.190.131"));
		message.setN2(TestUtils.createInet4Address("204.71.190.132"));
		message.setN3(TestUtils.createInet4Address("204.71.190.133"));
		message.setPublicIp(TestUtils.createInet4Address("207.46.232.182"));
		
		return message;
	}
	
	public static void verifyMessage(LoginSuccessMessage message) {
		assertEquals(4022250974L, message.getUserId());
		assertEquals(EXPECTED_SESSION_ID, message.getSessionId());
		assertEquals("", message.getNick());
		assertEquals(15L, message.getStatus());
		assertEquals("", message.getDlSet());
		assertEquals("", message.getP2pSet());
		assertEquals("", message.getClntSet());
		assertEquals(1L, message.getMinRect());
		assertEquals(1800L, message.getMaxRect());
		assertEquals(826L, message.getCtry());
		assertEquals("/204.71.190.131", message.getN1().toString());
		assertEquals("/204.71.190.132", message.getN2().toString());
		assertEquals("/204.71.190.133", message.getN3().toString());
		assertEquals("/207.46.232.182", message.getPublicIp().toString());
	}
}
