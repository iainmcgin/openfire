/*
 * JFire - a Java API to access the XFire instant messaging network.
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
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.net.util.BoundsUtil;
import uk.azdev.openfire.testutil.TestUtils;

public class KeepaliveMessageTest {

	private KeepaliveMessage message;
	
	@Before
	public void setUp() throws Exception {
		message = new KeepaliveMessage();
	}

	@Test
	public void testGetMessageId() {
		assertEquals(KeepaliveMessage.KEEP_ALIVE_MESSAGE_ID, message.getMessageId());
	}

	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance() instanceof KeepaliveMessage);
	}

	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "keepalive.sampledata");
		message.readMessageContent(buffer);
		
		assertEquals(1L, message.getValue());
		assertEquals(1, message.getStatsList().size());
		assertEquals(1L, message.getStatsList().get(0));
	}

	@Test
	public void testWriteMessageContent() throws IOException {
		message.setValue(1L);
		
		List<Long> statsList = new ArrayList<Long>();
		statsList.add(1L);
		
		message.setStatsList(statsList);
		
		TestUtils.checkMessageOutput(message, this.getClass(), "keepalive.sampledata");
	}
	
	private static final String EXPECTED_TOSTRING 
		= "Keepalive Message\n"
		+ "\tValue: 100\n"
		+ "\tStats:\n"
		+ "\t\t1\n"
		+ "\t\t2\n"
		+ "\t\t3";
	
	@Test
	public void testToString() {
		message.setValue(100L);
		
		List<Long> statsList = new ArrayList<Long>();
		statsList.add(1L);
		statsList.add(2L);
		statsList.add(3L);
		message.setStatsList(statsList);
		
		assertEquals(EXPECTED_TOSTRING, message.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetStatsListToNull() {
		message.setStatsList(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetValueNegative() {
		message.setValue(-1L);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetValueTooLarge() {
		message.setValue(BoundsUtil.MAX_INT32_VALUE + 1);
	}

}
