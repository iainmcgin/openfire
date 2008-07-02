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
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.common.ActiveGameInfo;
import uk.azdev.openfire.common.GameInfo;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.testutil.TestUtils;

public class FriendGameInfoMessageTest {

	private static final SessionId[] testSids =
		new SessionId[] {
			generateTestSid(0x00),
			generateTestSid(0x10),
			generateTestSid(0x20),
			generateTestSid(0x30),
			generateTestSid(0x40),
			generateTestSid(0x50),
			generateTestSid(0x60),
			generateTestSid(0x70),
			generateTestSid(0x80),
			generateTestSid(0x90),
		};
	
	private static final ActiveGameInfo[] testActiveGames =
		new ActiveGameInfo[] {
			null,
			new ActiveGameInfo(new GameInfo(4216, null, null), null),
			new ActiveGameInfo(new GameInfo(4216, null, null), null),
			new ActiveGameInfo(new GameInfo(4684, null, null), new InetSocketAddress("85.25.17.183", 28960)),
			new ActiveGameInfo(new GameInfo(4215, null, null), null),
			new ActiveGameInfo(new GameInfo(4880, null, null), new InetSocketAddress("62.104.18.109", 17567)),
			new ActiveGameInfo(new GameInfo(4216, null, null), null),
			new ActiveGameInfo(new GameInfo(4331, null, null), new InetSocketAddress("194.50.80.26", 27017)),
			new ActiveGameInfo(new GameInfo(4880, null, null), new InetSocketAddress("62.104.18.181",17567)),
			new ActiveGameInfo(new GameInfo(4578, null, null), null)
		};
	
	private static SessionId generateTestSid(int startByteVal) {
		byte[] sidBytes = new byte[SessionId.SESSION_ID_SIZE];
		for(int i=0; i < SessionId.SESSION_ID_SIZE; i++) {
			sidBytes[i] = (byte)(startByteVal + i);
		}
		
		return new SessionId(sidBytes);
	}
	
	public FriendGameInfoMessage message;
	
	@Before
	public void setUp() throws Exception {
		message = new FriendGameInfoMessage();
	}
	
	@Test
	public void testMessageId() {
		assertEquals(FriendGameInfoMessage.FRIEND_GAME_INFO_MESSAGE_ID, message.getMessageId());
	}
	
	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance() instanceof FriendGameInfoMessage);
	}
	
	@Test
	public void testDefaultState() {
		assertEquals(0, message.getSessionIdSet().size());
	}
	
	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "friendgameinfo.sampledata");
		message.readMessageContent(buffer);
		assertEquals(10, message.getSessionIdSet().size());
		
		Iterator<SessionId> sessionIdIter = message.getSessionIdSet().iterator();
		
		int i=0;
		while(sessionIdIter.hasNext()) {
			SessionId sid = sessionIdIter.next();
			assertEquals(testSids[i], sid);
			assertEquals(testActiveGames[i], message.getActiveGameInfoForSid(sid));
			i++;
		}
	}
	
	@Test
	public void testWriteMessageContent() throws IOException {
		
		for(int i=0; i < testSids.length; i++) {
			message.addActiveGameInfo(testSids[i], testActiveGames[i]);
		}
		
		TestUtils.checkMessageOutput(message, this.getClass(), "friendgameinfo.sampledata");
	}
	
	private static final String EXPECTED_TOSTRING
		= "Friend Game Info Message" +
	      "\n\tSID:<00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F> -> no game" +
		  "\n\tSID:<10 11 12 13 14 15 16 17 18 19 1A 1B 1C 1D 1E 1F> -> GID4216 (no server info)" +
		  "\n\tSID:<20 21 22 23 24 25 26 27 28 29 2A 2B 2C 2D 2E 2F> -> GID4216 (no server info)" +
		  "\n\tSID:<30 31 32 33 34 35 36 37 38 39 3A 3B 3C 3D 3E 3F> -> GID4684 @ /85.25.17.183:28960" +
		  "\n\tSID:<40 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F> -> GID4215 (no server info)" +
		  "\n\tSID:<50 51 52 53 54 55 56 57 58 59 5A 5B 5C 5D 5E 5F> -> GID4880 @ /62.104.18.109:17567" +
		  "\n\tSID:<60 61 62 63 64 65 66 67 68 69 6A 6B 6C 6D 6E 6F> -> GID4216 (no server info)" +
		  "\n\tSID:<70 71 72 73 74 75 76 77 78 79 7A 7B 7C 7D 7E 7F> -> GID4331 @ /194.50.80.26:27017" +
		  "\n\tSID:<80 81 82 83 84 85 86 87 88 89 8A 8B 8C 8D 8E 8F> -> GID4880 @ /62.104.18.181:17567" +
		  "\n\tSID:<90 91 92 93 94 95 96 97 98 99 9A 9B 9C 9D 9E 9F> -> GID4578 (no server info)";
	
	@Test
	public void testToString() {
		for(int i=0; i < testSids.length; i++) {
			message.addActiveGameInfo(testSids[i], testActiveGames[i]);
		}
		
		assertEquals(EXPECTED_TOSTRING, message.toString());
	}
}
