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

import uk.azdev.openfire.net.util.BoundsUtil;
import uk.azdev.openfire.testutil.TestUtils;

public class ClientVersionMessageTest {

	private ClientVersionMessage message;
	
	@Before
	public void setUp() {
		message = new ClientVersionMessage();
	}
	
	@Test
	public void testReadMessage() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "clientversion.sampledata");
		message.readMessageContent(buffer);
		
		assertEquals(67L, message.getVersion());
	}
	
	@Test
	public void testWriteMessage() throws IOException {
		message.setVersion(67);
		TestUtils.checkMessageOutput(message, this.getClass(), "clientversion.sampledata");
	}
	
	@Test
	public void testGetMessageId() {
		assertEquals(3, message.getMessageId());
	}

	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance() instanceof ClientVersionMessage);
	}
	
	@Test
	public void testToString() {
		message.setVersion(150);
		assertEquals("Client Version Message\n\tVersion: 150", message.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetNegativeVersion() {
		message.setVersion(-1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetHugeVersion() {
		message.setVersion(BoundsUtil.MAX_INT32_VALUE + 1);
	}

}
