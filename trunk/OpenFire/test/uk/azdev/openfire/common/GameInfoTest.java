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
package uk.azdev.openfire.common;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GameInfoTest {

	@Test
	public void testCreateGameInfo() {
		GameInfo gameInfo = new GameInfo(1L, "Test Game", "tg");
		assertEquals(1L, gameInfo.getId());
		assertEquals("Test Game", gameInfo.getName());
		assertEquals("tg", gameInfo.getShortName());
	}
	
	@Test
	public void testEqualsAndHashcode() throws Exception {
		GameInfo a = new GameInfo(1L, "Test Game", "tg");
		GameInfo b = new GameInfo(2L, "Different game", "dg");
		GameInfo c = new GameInfo(1L, "Test Game with different name", "tgblah");
		
		assertTrue(a.equals(c));
		assertTrue(c.equals(a));
		assertTrue(a.hashCode() == c.hashCode());
		assertFalse(a.equals(b));
		assertFalse(a.equals(new Object()));
	}
	
}
