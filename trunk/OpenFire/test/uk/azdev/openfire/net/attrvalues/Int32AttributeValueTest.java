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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.util.BoundsUtil;
import uk.azdev.openfire.net.util.IOUtil;



public class Int32AttributeValueTest {

	// one million pre-encoded
	private static final byte[] SAMPLE_ATTR = { 0x40, 0x42, 0x0F, 0x00 };
	
	private Int32AttributeValue value;
	
	@Before
	public void setUp() {
		value = new Int32AttributeValue();
	}
	
	@Test
	public void testSetValue() {
		assertEquals(0L, value.getValue());
		value.setValue(10);
		assertEquals(10L, value.getValue());
	}

	@Test
	public void testGetSize() {
		assertEquals(4, value.getSize());
	}

	@Test
	public void testGetTypeId() {
		assertEquals(Int32AttributeValue.TYPE_ID, value.getTypeId());
	}

	@Test
	public void testReadValue() {
		ByteBuffer buffer = IOUtil.createBuffer(SAMPLE_ATTR.length);
		
		buffer.put(SAMPLE_ATTR);
		buffer.rewind();
		
		value.readValue(buffer);
		assertEquals(1000000L, value.getValue());
	}

	@Test
	public void testWriteValue() {
		value.setValue(50);
		ByteBuffer buffer = IOUtil.createBuffer(value.getSize());
		value.writeValue(buffer);
		
		buffer.rewind();
		Int32AttributeValue readValue = new Int32AttributeValue();
		readValue.readValue(buffer);
		
		assertEquals(50L, readValue.getValue());
	}

	@Test
	public void testNewInstance() {
		assertTrue(value.newInstance() instanceof Int32AttributeValue);
	}
	
	@Test
	public void testToString() {
		value.setValue(50);
		assertEquals("50 (int32)", value.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValueTooBig() {
		value.setValue(BoundsUtil.MAX_INT32_VALUE + 1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValueTooBigViaConstructor() {
		new Int32AttributeValue(BoundsUtil.MAX_INT32_VALUE + 1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNegativeValue() {
		value.setValue(-1);
	}
}
