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
package uk.azdev.openfire.net.attrvalues;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.Int8KeyedAttributeMap;
import uk.azdev.openfire.net.util.IOUtil;
import uk.azdev.openfire.testutil.TestUtils;



public class Int8KeyedAttributeMapTest {

	private Int8KeyedAttributeMap map;
	private byte[] testBytes 
		= new byte[] { 0x02, // 2 attributes 
					   0x4F, // key 4F 
					   0x02, 0x01, 0x00, 0x00, 0x00, // int32 value '1'
					   0x50, // key 50
					   0x02, 0x02, 0x00, 0x00, 0x00 // int32 value '2'
					 };
	
	@Before
	public void setUp() {
		map = new Int8KeyedAttributeMap();
	}
	
	@Test
	public void testReadMap() {
		ByteBuffer buffer = TestUtils.createBufferFromArray(testBytes);
		map.read(buffer);
		assertEquals(2, map.numAttributes());
		assertTrue(map.hasAttribute(0x4F));
		assertEquals(1L, map.getInt32AttributeValue(0x4F));
		assertTrue(map.hasAttribute(0x50));
		assertEquals(2L, map.getInt32AttributeValue(0x50));
	}
	
	@Test
	public void testReadMapWithLists() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "lists_in_map.sampledata");
		map.read(buffer);
		
		assertEquals(2, map.numAttributes());
		assertTrue(map.hasAttribute(0x4F));
		
		List<String> strings = map.getAttributeValueAsList(0x4F, new StringAttributeValue());
		assertEquals(3, strings.size());
		assertEquals("ab", strings.get(0));
		assertEquals("cde", strings.get(1));
		assertEquals("x", strings.get(2));
		
		List<Long> ints = map.getAttributeValueAsList(0x50, new Int32AttributeValue());
		assertEquals(4, ints.size());
		assertEquals(1L, ints.get(0));
		assertEquals(2L, ints.get(1));
		assertEquals(3L, ints.get(2));
		assertEquals(4L, ints.get(3));
	}
	
	@Test
	public void testWriteMap() {
		map.addAttribute(0x4F, new Int32AttributeValue(1));
		map.addAttribute(0x50, new Int32AttributeValue(2));
		
		assertEquals(testBytes.length, map.getSize());
		ByteBuffer output = IOUtil.createBuffer(map.getSize());
		map.write(output);
		output.rewind();
		TestUtils.checkBytes(testBytes, output);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddAttributeWithNegativeKey() {
		map.addAttribute(-1, new Int32AttributeValue());
	}
}
