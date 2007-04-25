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
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.testutil.TestUtils;

public class LoginChallengeMessageTest {

	private static String EXPECTED_SALT = "cfb67a640fc290e41ce5e41bc6b5ad22847af2a3";
	
	private LoginChallengeMessage message;

	@Before
	public void setUp() {
		message = new LoginChallengeMessage();
	}
	
	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance() instanceof LoginChallengeMessage);
	}

	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "loginchallenge.sampledata");
		message.readMessageContent(buffer);
		verifyMessage(message);
	}

	@Test
	public void testWriteMessageContent() throws IOException {
		message.setSalt(EXPECTED_SALT);
		TestUtils.checkMessageOutput(message, this.getClass(), "loginchallenge.sampledata");
	}
	
	@Test
	public void testToString() {
		message.setSalt(EXPECTED_SALT);
		assertEquals("Login Challenge Message\n\tSalt: " + EXPECTED_SALT, message.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetSaltOfWrongSize() {
		message.setSalt("tooshort");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetNullSalt() {
		message.setSalt(null);
	}
	
	public static LoginChallengeMessage createTestMessage() {
		LoginChallengeMessage message = new LoginChallengeMessage();
		message.setSalt(EXPECTED_SALT);
		
		return message;
	}
	
	public static void verifyMessage(LoginChallengeMessage message) throws IOException {
		assertEquals(EXPECTED_SALT, message.getSalt());
	}

}
