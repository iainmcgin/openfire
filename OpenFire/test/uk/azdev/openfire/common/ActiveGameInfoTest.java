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
package uk.azdev.openfire.common;


import static org.junit.Assert.*;

import java.net.InetSocketAddress;

import org.junit.Test;

public class ActiveGameInfoTest {

	@Test
	public void testAccessors() {
		ActiveGameInfo gameInfo = new ActiveGameInfo(1000L, new InetSocketAddress("127.0.0.1", 10000));
		assertEquals(1000L, gameInfo.getGameId());
		assertEquals("/127.0.0.1:10000", gameInfo.getGameAddress().toString());
	}
	
	@Test
	public void testEquals() {
		ActiveGameInfo ag1 = new ActiveGameInfo(1000L, new InetSocketAddress("127.0.0.1", 10000));
		ActiveGameInfo ag2 = new ActiveGameInfo(1000L, new InetSocketAddress("127.0.0.1", 10000));
		ActiveGameInfo ag3 = new ActiveGameInfo(1000L, new InetSocketAddress("127.0.0.2", 10000));
		ActiveGameInfo ag4 = new ActiveGameInfo(1000L, new InetSocketAddress("127.0.0.1", 10001));
		ActiveGameInfo ag5 = new ActiveGameInfo(1001L, new InetSocketAddress("127.0.0.1", 10000));
		
		assertTrue(ag1.equals(ag2));
		assertTrue(ag2.equals(ag1));
		assertFalse(ag1.equals(ag3));
		assertFalse(ag1.equals(ag4));
		assertFalse(ag1.equals(ag5));
		assertFalse(ag1.equals(new Object()));
		
	}

}
