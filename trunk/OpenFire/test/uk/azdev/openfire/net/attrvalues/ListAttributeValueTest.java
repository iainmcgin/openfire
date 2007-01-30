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
import static uk.azdev.openfire.testutil.TestUtils.*;

import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.net.attrvalues.AttributeValue;
import uk.azdev.openfire.net.attrvalues.AttributeValueFactory;
import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.MaxListSizeExceededException;
import uk.azdev.openfire.net.attrvalues.StringAttributeValue;
import uk.azdev.openfire.net.util.IOUtil;



public class ListAttributeValueTest {
	
	private static final byte[] SAMPLE_ATTR 
		= { 0x01, // string list 
			0x02, 0x00, // 2 strings 
			0x07, 0x00, 0x74, 0x65, 0x73, 0x74, 0x69, 0x6E, 0x67, // 7 character string "testing"
			0x04, 0x00, 0x74, 0x65, 0x73, 0x74 }; // 4 character string "test"
	
	private static final byte[] INT_LIST
		= { 0x02,
			0x03, 0x00,
			0x01, 0x00, 0x00, 0x00,
			0x02, 0x00, 0x00, 0x00,
			0x03, 0x00, 0x00, 0x00 };
	
	private ListAttributeValue value;
	
	@Before
	public void setUp() {
		value = new ListAttributeValue();
	}
	
	@Test
	public void testTypeId() {
		assertEquals(ListAttributeValue.TYPE_ID, value.getTypeId());
	}
	
	@Test
	public void testInitialState() {
		assertEquals(StringAttributeValue.TYPE_ID, value.getItemType());
		assertEquals(0, value.getList().size());
	}
	
	@Test
	public void testReadValue() {
		ByteBuffer buffer = IOUtil.createBuffer(SAMPLE_ATTR.length);
		buffer.put(SAMPLE_ATTR);
		buffer.rewind();
		
		value.readValue(buffer);
		assertEquals(StringAttributeValue.TYPE_ID, value.getItemType());
		assertEquals(2, value.getList().size());
		
		StringAttributeValue firstValue = (StringAttributeValue) value.getList().get(0);
		assertEquals("testing", firstValue.getValue());
		
		StringAttributeValue secondValue = (StringAttributeValue) value.getList().get(1);
		assertEquals("test", secondValue.getValue());
	}
	
	@Test
	public void testWriteValue() {
		value.addValue(new Int32AttributeValue(1));
		value.addValue(new Int32AttributeValue(2));
		value.addValue(new Int32AttributeValue(3));
		
		assertEquals(INT_LIST.length, value.getSize());
		
		ByteBuffer buffer = IOUtil.createBuffer(INT_LIST.length);
		value.writeValue(buffer);
		
		byte[] bytes = new byte[INT_LIST.length];
		buffer.rewind();
		buffer.get(bytes);
		
		checkArray(INT_LIST, bytes);
	}
	
	@Test
	public void testAddValue() {
		assertEquals(StringAttributeValue.TYPE_ID, value.getItemType());
		value.addValue(new Int32AttributeValue());
		assertEquals(Int32AttributeValue.TYPE_ID, value.getItemType());
	}
	
	@Test
	public void testReadAndWriteListWithLargeTypeId() {
		TestableListAttributeValue testValue = new TestableListAttributeValue();
		testValue.addValue(new TestAttributeValue());
		testValue.addValue(new TestAttributeValue());
		testValue.addValue(new TestAttributeValue());
		
		ByteBuffer buffer = IOUtil.createBuffer(testValue.getSize());
		testValue.writeValue(buffer);
		buffer.rewind();
		
		TestableListAttributeValue inValue = new TestableListAttributeValue();
		inValue.readValue(buffer);
		assertEquals(TestAttributeValue.TEST_TYPE_ID, inValue.getItemType());
		assertEquals(3, inValue.getList().size());
	}
	
	@Test
	public void testReadAndWriteLargeList() {
		for(int i=0; i < 40000; i++) {
			value.addValue(new Int32AttributeValue());
		}
		
		ByteBuffer buffer = IOUtil.createBuffer(value.getSize());
		value.writeValue(buffer);
		buffer.rewind();
		
		TestableListAttributeValue inValue = new TestableListAttributeValue();
		inValue.readValue(buffer);
		
		assertEquals(Int32AttributeValue.TYPE_ID, inValue.getItemType());
		assertEquals(40000, inValue.getList().size());
	}
	
	@Test
	public void testToString() {
		value.addValue(new Int32AttributeValue(0));
		value.addValue(new Int32AttributeValue(1));
		value.addValue(new Int32AttributeValue(2));
		
		assertEquals("{ 0 (int32), 1 (int32), 2 (int32) }", value.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMixItemTypes() {
		value.addValue(new Int32AttributeValue());
		value.addValue(new StringAttributeValue());
	}
	
	@Test(expected=MaxListSizeExceededException.class)
	public void testListTooBig() {
		try {
		for(int i=0; i < ListAttributeValue.MAX_LIST_SIZE; i++) {
			value.addValue(new Int32AttributeValue(i));
		}
		} catch(MaxListSizeExceededException e) {
			fail("MaxListSizeExceededException thrown too early");
		}
		
		value.addValue(new Int32AttributeValue(0));
	}
	
	protected class TestableListAttributeValue extends ListAttributeValue {
		@Override
		protected AttributeValueFactory getValueFactory() {
			return new TestAttributeValueFactory();
		}
	}
	
	
	protected class TestAttributeValueFactory extends AttributeValueFactory {
		@Override
		public AttributeValue createAttributeValue(int type) {
			if(type == TestAttributeValue.TEST_TYPE_ID) {
				return new TestAttributeValue();
			}
			
			return super.createAttributeValue(type);
		}
	}
	
	/**
	 * attribute value with deliberately high type value, to
	 * test reading and writing unsigned bytes
	 */
	protected class TestAttributeValue extends Int32AttributeValue {

		public static final int TEST_TYPE_ID = 240;
		
		@Override
		public int getTypeId() {
			return TEST_TYPE_ID;
		}

	}
}
