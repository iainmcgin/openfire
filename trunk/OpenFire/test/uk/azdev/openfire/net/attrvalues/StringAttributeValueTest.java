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
import static uk.azdev.openfire.testutil.TestUtils.*;

import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.net.attrvalues.StringAttributeValue;
import uk.azdev.openfire.net.util.IOUtil;


public class StringAttributeValueTest {
	
	private static final String TEST_STRING = "testing";
	private static final String LONG_STRING = createLongString(200);
	private static final String EXCESSIVE_STRING = createLongString(100000);
	
	private static final byte[] SAMPLE_ATTR 
		= { 0x07, 0x00, // 7 bytes long 
			0x74, 0x65, 0x73, 0x74, 0x69, 0x6E, 0x67 }; // "testing"
	
	private StringAttributeValue value;
	
	@Before
	public void setUp() {
		value = new StringAttributeValue();
	}
	
	@Test
	public void testSetValue() {
		assertEquals("", value.getValue());
		value.setValue(TEST_STRING);
		assertEquals(TEST_STRING, value.getValue());
	}

	@Test
	public void testGetSize() {
		assertEquals(2, value.getSize());
		value.setValue(TEST_STRING);
		assertEquals(9, value.getSize());
	}

	@Test
	public void testGetTypeId() {
		assertEquals(StringAttributeValue.TYPE_ID, value.getTypeId());
	}

	@Test
	public void testReadValue() {
		ByteBuffer buffer = IOUtil.createBuffer(SAMPLE_ATTR.length);
		
		buffer.put(SAMPLE_ATTR);
		buffer.rewind();
		
		value.readValue(buffer);
		assertEquals(TEST_STRING, value.getValue());
	}

	@Test
	public void testWriteValue() {
		value.setValue(TEST_STRING);
		ByteBuffer buffer = IOUtil.createBuffer(value.getSize());
		value.writeValue(buffer);
		
		checkBytes(SAMPLE_ATTR, buffer);
	}
	
	@Test
	public void testReadAndWriteLongString() {
		value.setValue(LONG_STRING);
		
		ByteBuffer buffer = IOUtil.createBuffer(value.getSize());
		value.writeValue(buffer);
		buffer.rewind();
		
		StringAttributeValue inValue = new StringAttributeValue();
		inValue.readValue(buffer);
		
		assertEquals(LONG_STRING.length(), inValue.getValue().length());
		assertEquals(LONG_STRING, inValue.getValue());
	}

	@Test
	public void testNewInstance() {
		assertTrue(value.newInstance().getClass() == StringAttributeValue.class);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testTooLongString() {
		value.setValue(EXCESSIVE_STRING);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTooLongStringViaConstructor() {
		new StringAttributeValue(EXCESSIVE_STRING);
	}
	
	private static String createLongString(int size) {
		StringBuffer buffer = new StringBuffer(size);
		
		for(int i=0; i < size; i++) {
			buffer.append('x');
		}
		
		return buffer.toString();
	}
}
