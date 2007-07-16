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
package uk.azdev.openfire.net.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoundsUtilTest {

	@Test(expected=RuntimeException.class)
	public void testCannotCreateInstance() {
		new BoundsUtil();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCheckInt32Bounds_negative() {
		BoundsUtil.checkInt32Bounds(-1L, "");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCheckInt32Bounds_toBig() {
		BoundsUtil.checkInt32Bounds(BoundsUtil.MAX_INT32_VALUE + 1, "");
	}
	
	@Test
	public void testCheckInt32Bounds() {
		try {
			BoundsUtil.checkInt32Bounds(1000, "");
		} catch(IllegalArgumentException e) {
			fail("IllegalArgumentException thrown incorrectly");
		}
	}

	@Test
	public void testIsInInt32Bounds() {
		assertFalse(BoundsUtil.isInInt32Bounds(-1));
		assertTrue(BoundsUtil.isInInt32Bounds(0));
		assertTrue(BoundsUtil.isInInt32Bounds(1000));
		assertTrue(BoundsUtil.isInInt32Bounds(BoundsUtil.MAX_INT32_VALUE));
		assertFalse(BoundsUtil.isInInt32Bounds(BoundsUtil.MAX_INT32_VALUE + 1));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCheckInt8Bounds_negative() {
		BoundsUtil.checkInt8Bounds(-1, "");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCheckInt8Bounds_tooBig() {
		BoundsUtil.checkInt8Bounds(BoundsUtil.MAX_INT8_VALUE + 1, "");
	}
	
	@Test
	public void testCheckInt8Bounds() {
		try {
			BoundsUtil.checkInt8Bounds(50, "");
		} catch(IllegalArgumentException e) {
			fail("IllegalArgumentException thrown incorrectly");
		}
	}

	@Test
	public void testIsInInt8Bounds() {
		assertFalse(BoundsUtil.isInInt8Bounds(-1));
		assertTrue(BoundsUtil.isInInt8Bounds(0));
		assertTrue(BoundsUtil.isInInt8Bounds(50));
		assertTrue(BoundsUtil.isInInt8Bounds(BoundsUtil.MAX_INT8_VALUE));
		assertFalse(BoundsUtil.isInInt8Bounds(BoundsUtil.MAX_INT8_VALUE + 1));
	}

}
