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

import static org.junit.Assert.*;

import org.junit.Test;

import uk.azdev.openfire.net.util.IOUtil;

public class ActiveApplicationInfoTest {

	@Test
	public void testToString() {
		ActiveApplicationInfo info = new ActiveApplicationInfo(100L, IOUtil.getInet4Address("192.168.100.20"));
		assertEquals("ActiveApplicationInfo(appId=100,addr=192.168.100.20/192.168.100.20)", info.toString());
	}
	
	@Test
	public void testEqualsAndHashCode() {
		ActiveApplicationInfo a = new ActiveApplicationInfo(100L, IOUtil.getInet4Address("192.168.100.20"));
		ActiveApplicationInfo b = new ActiveApplicationInfo(100L, IOUtil.getInet4Address("192.168.100.20"));
		ActiveApplicationInfo c = new ActiveApplicationInfo(101L, IOUtil.getInet4Address("192.168.100.20"));
		ActiveApplicationInfo d = new ActiveApplicationInfo(100L, IOUtil.getInet4Address("192.168.100.21"));
		ActiveApplicationInfo e = new ActiveApplicationInfo(101L, IOUtil.getInet4Address("192.168.100.21"));
		
		assertTrue(a.equals(b));
		assertTrue(a.hashCode() == b.hashCode());
		assertFalse(a.equals(c));
		assertFalse(a.hashCode() == c.hashCode());
		assertFalse(a.equals(d));
		assertFalse(a.equals(e));
		assertTrue(b.equals(a));
		assertFalse(c.equals(a));
		
	}
	
}
