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

import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;

public class UnrolledListAttributeValueTest {

	private ListAttributeValue list;
	private UnrolledListAttributeValue<Long> unrolledList;
	
	@Before
	public void setUp() throws Exception {
		list = new ListAttributeValue();
		list.addValue(new Int32AttributeValue(10));
		list.addValue(new Int32AttributeValue(20));
		list.addValue(new Int32AttributeValue(30));
		list.addValue(new Int32AttributeValue(40));
		unrolledList = new UnrolledListAttributeValue<Long>(list);
	}


	@Test(expected=IllegalStateException.class)
	public void testAdd() {
		unrolledList.add(40L);
	}

	@Test(expected=IllegalStateException.class)
	public void testAddAtIndex() {
		unrolledList.add(2, 40L);
	}

	@Test(expected=IllegalStateException.class)
	public void testAddAll() {
		unrolledList.addAll(new LinkedList<Long>());
	}

	@Test(expected=IllegalStateException.class)
	public void testAddAllC() {
		unrolledList.addAll(5, new LinkedList<Long>());
	}

	@Test(expected=IllegalStateException.class)
	public void testClear() {
		unrolledList.clear();
	}

	@Test
	public void testContains() {
		assertTrue(unrolledList.contains(40L));
		assertFalse(unrolledList.contains(15L));
	}

	@Test(expected=IllegalStateException.class)
	public void testContainsAll() {
		unrolledList.containsAll(new LinkedList<Long>());
	}

	@Test
	public void testGet() {
		assertEquals(20L, unrolledList.get(1));
	}

	@Test(expected=IllegalStateException.class)
	public void testIndexOf() {
		unrolledList.indexOf(20L);
	}

	@Test
	public void testIsEmpty() {
		assertFalse(unrolledList.isEmpty());
	}

	@Test
	public void testIterator() {
		Iterator<Long> iter = unrolledList.iterator();
		assertEquals(10L, iter.next());
		assertEquals(20L, iter.next());
		assertEquals(30L, iter.next());
		assertEquals(40L, iter.next());
		assertFalse(iter.hasNext());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testRemoveFromIterator() {
		Iterator<Long> iter = unrolledList.iterator();
		iter.next();
		iter.remove();
	}

	@Test(expected=IllegalStateException.class)
	public void testLastIndexOf() {
		unrolledList.lastIndexOf(30L);
	}

	@Test(expected=IllegalStateException.class)
	public void testListIterator() {
		unrolledList.listIterator();
	}

	@Test(expected=IllegalStateException.class)
	public void testListIteratorFromIndex() {
		unrolledList.listIterator(3);
	}

	@Test(expected=IllegalStateException.class)
	public void testRemoveObject() {
		unrolledList.remove(3);
	}

	@Test(expected=IllegalStateException.class)
	public void testRemoveInt() {
		unrolledList.remove(new Long(20L));
	}

	@Test(expected=IllegalStateException.class)
	public void testRemoveAll() {
		unrolledList.removeAll(new LinkedList<Long>());
	}

	@Test(expected=IllegalStateException.class)
	public void testRetainAll() {
		unrolledList.retainAll(new LinkedList<Long>());
	}

	@Test(expected=IllegalStateException.class)
	public void testSet() {
		unrolledList.set(1, new Long(10L));
	}

	@Test
	public void testSize() {
		assertEquals(4, unrolledList.size());
	}

	@Test(expected=IllegalStateException.class)
	public void testSubList() {
		unrolledList.subList(1, 2);
	}

	@Test
	public void testToArray() {
		assertEquals(new Long[] { 10L, 20L, 30L, 40L }, unrolledList.toArray());
	}

	@Test
	public void testToArrayWithProvidedArray() {
		Long[] smallArray = new Long[2];
		Long[] correctArray = new Long[4];
		Long[] largeArray = new Long[6];
		assertNotSame(smallArray, unrolledList.toArray(smallArray));
		assertSame(correctArray, unrolledList.toArray(correctArray));
		assertEquals(new Long[] { 10L, 20L, 30L, 40L }, correctArray);
		assertSame(largeArray, unrolledList.toArray(largeArray));
		assertEquals(null, largeArray[4]);
	}

}
