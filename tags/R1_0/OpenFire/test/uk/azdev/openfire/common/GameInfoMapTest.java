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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.testutil.TestUtils;

public class GameInfoMapTest {

	private GameInfoMap map;
	
	@Before
	public void setUp() throws Exception {
		map = new GameInfoMap();
	}
	
	@Test
	public void testAddGameInfo() throws Exception {
		GameInfo testGameInfo = new GameInfo(1L, "Test Game", "tg");
		map.addGameInfo(testGameInfo);
		assertEquals(testGameInfo, map.getGameById(1L));
	}
	
	@Test
	public void testLoadFromXFireGamesIni() throws IOException {
		InputStream sampleIni = TestUtils.getTestResource(this.getClass(), "sample_games.ini");
		InputStreamReader reader = new InputStreamReader(sampleIni);
		
		map.loadFromXfireGamesIni(reader);
		assertNull(map.getGameById(-1));
		assertNull(map.getGameById(100));
		assertNotNull(map.getGameById(4097));
		assertNotNull(map.getGameById(4098));
		assertNotNull(map.getGameById(4099));
		
		GameInfo info4097 = map.getGameById(4097);
		assertEquals(4097L, info4097.getId());
		assertEquals("Wolfenstein: Enemy Territory", info4097.getName());
		assertEquals("wet", info4097.getShortName());
		
		GameInfo info4098 = map.getGameById(4098);
		assertEquals(4098L, info4098.getId());
		assertEquals("Dark Age of Camelot", info4098.getName());
		assertEquals("daoc", info4098.getShortName());
		
		GameInfo info4099 = map.getGameById(4099);
		assertEquals(4099L, info4099.getId());
		assertEquals("Dark Age of Camelot - Shrouded Isles", info4099.getName());
		assertEquals("daocsi", info4099.getShortName());
	}

}
