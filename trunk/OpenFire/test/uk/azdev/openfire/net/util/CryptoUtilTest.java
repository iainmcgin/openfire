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

import uk.azdev.openfire.net.util.CryptoUtil;


public class CryptoUtilTest {

	@Test(expected=RuntimeException.class)
	public void testCannotCreateInstance() {
		new CryptoUtil();
	}
	
	@Test
	public void testSha1HashAsBase64String() {
		// test cases taken from the xfirelib SHA-1 implementation
		// to ensure compatibility with the xfire server
		assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", 
				     CryptoUtil.sha1HashAsHexString("abc"));
		
		assertEquals("84983e441c3bd26ebaae4aa1f95129e5e54670f1", 
			         CryptoUtil.sha1HashAsHexString("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"));
		
		assertEquals("34aa973cd4c4daa4f61eeb2bdbad27316534016f",
					 CryptoUtil.sha1HashAsHexString(generateAMillionAs()));
	}

	private String generateAMillionAs() {
		StringBuffer buffer = new StringBuffer(1000000);
		
		for(int i=0; i < 1000000; i++) {
			buffer.append('a');
		}
		
		return buffer.toString();
	}

}
