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

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.net.attrvalues.AttributeMap;
import uk.azdev.openfire.net.attrvalues.AttributeValue;
import uk.azdev.openfire.net.attrvalues.DuplicateAttributeException;
import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.attrvalues.StringKeyedMapAttributeValue;
import uk.azdev.openfire.net.attrvalues.TooManyAttributesException;
import uk.azdev.openfire.net.util.IOUtil;
import uk.azdev.openfire.testutil.TestUtils;


public class StringKeyedAttributeMapTest {

	private StringKeyedAttributeMap map;
	
	@Before
	public void setUp() {
		map = new StringKeyedAttributeMap();
	}
	
	@Test
	public void testReadMapWithEmbeddedMap() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "map_within_map.sampledata");
		map.read(buffer);
		
		assertEquals(1, map.numAttributes());
		assertTrue(map.hasAttribute("mainattr"));
		AttributeValue<?> value = map.getAttributeValue("mainattr");
		assertTrue(value instanceof StringKeyedMapAttributeValue);
		
		StringKeyedAttributeMap stringMap = ((StringKeyedMapAttributeValue)value).getValue();
		assertEquals(3, stringMap.numAttributes());
		assertTrue(stringMap.hasAttribute("abc"));
		assertEquals(1L, stringMap.getInt32AttributeValue("abc"));
		assertTrue(stringMap.hasAttribute("xyz"));
		assertEquals("test", stringMap.getStringAttributeValue("xyz"));
		assertTrue(stringMap.hasAttribute("omg"));
		assertEquals(1337L, stringMap.getInt32AttributeValue("omg"));
	}
	
	@Test
	public void testWriteMapWithEmbeddedMap() throws IOException {
		byte[] expectedBytes = TestUtils.getByteArrayForResource(this.getClass(), "map_within_map.sampledata");
		
		StringKeyedMapAttributeValue value = new StringKeyedMapAttributeValue();
		StringKeyedAttributeMap stringMap = value.getValue();
		stringMap.addAttribute("abc", 1);
		stringMap.addAttribute("xyz", "test");
		stringMap.addAttribute("omg", 1337L);
		
		map.addAttribute("mainattr", value);
		
		ByteBuffer buffer = IOUtil.createBuffer(map.getSize());
		map.write(buffer);
		buffer.rewind();
		TestUtils.checkBytes(expectedBytes, buffer);
	}
	
	@Test
	public void testToString() {
		map.addAttribute("testing", new Int32AttributeValue(10));
		map.addAttribute("testing2", new StringAttributeValue("hello"));
		
		assertEquals("testing = 10 (int32)\ntesting2 = \"hello\"\n", map.toString());
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddAttribute_withNullValue() {
		map.addAttribute("test", (AttributeValue)null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetStringValue_withWrongType() {
		map.addAttribute("test", 1);
		map.getStringAttributeValue("test");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetInt32Value_withWrongType() {
		map.addAttribute("test", "value");
		map.getInt32AttributeValue("test");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetStringList_withWrongType() {
		ListAttributeValue value = new ListAttributeValue();
		value.addValue(new Int32AttributeValue(0));
		map.addAttribute("test", value);
		
		map.getAttributeValueAsList("test", new StringAttributeValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetInt32List_withWrongType() {
		ListAttributeValue value = new ListAttributeValue();
		value.addValue(new StringAttributeValue("value"));
		map.addAttribute("test", value);
		
		map.getAttributeValueAsList("test", new Int32AttributeValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetList_withNonListValue() {
		map.addAttribute("test", new Int32AttributeValue(0));
		map.getAttributeValueAsList("test", new StringAttributeValue());
	}
	
	@Test(expected=DuplicateAttributeException.class)
	public void testAddAttributeTwice() {
		map.addAttribute("test", new StringAttributeValue("value"));
		map.addAttribute("test", new StringAttributeValue("value2"));
	}
	
	@Test(expected=TooManyAttributesException.class)
	public void testAddTooManyAttributes() {
		try {
			for(int i=0; i < AttributeMap.MAX_ATTRIBUTES; i++) {
				map.addAttribute(Integer.toString(i), new Int32AttributeValue(i));
			}
		} catch(TooManyAttributesException e) {
			fail("TooManyAttributesException thrown too early");
		}
		
		map.addAttribute("thekillerblow", new Int32AttributeValue(666));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddAttributeWithOversizedName() {
		String oversizedName = generateOversizedName();
		map.addAttribute(oversizedName, new Int32AttributeValue(0));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetMissingAttribute() {
		map.getAttributeValue("missingAttribute");
	}

	private String generateOversizedName() {
		StringBuffer buffer = new StringBuffer(StringKeyedAttributeMap.MAX_KEY_LENGTH + 1);
		for(int i=0; i < StringKeyedAttributeMap.MAX_KEY_LENGTH + 1; i++) {
			buffer.append('x');
		}
		
		return buffer.toString();
	}
}
