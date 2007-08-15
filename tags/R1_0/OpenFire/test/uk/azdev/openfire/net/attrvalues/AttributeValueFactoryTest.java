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

import org.junit.Test;

import uk.azdev.openfire.net.attrvalues.AttributeValueFactory;
import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.Int8KeyedMapAttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.SessionIdAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedMapAttributeValue;
import uk.azdev.openfire.net.attrvalues.UnknownAttributeValueTypeException;


public class AttributeValueFactoryTest {

	@Test
	public void testCreateAttributeValue() {
		AttributeValueFactory factory = new AttributeValueFactory();
		
		assertTrue(factory.createAttributeValue(StringAttributeValue.TYPE_ID) instanceof StringAttributeValue);
		assertTrue(factory.createAttributeValue(Int32AttributeValue.TYPE_ID) instanceof Int32AttributeValue);
		assertTrue(factory.createAttributeValue(SessionIdAttributeValue.TYPE_ID) instanceof SessionIdAttributeValue);
		assertTrue(factory.createAttributeValue(ListAttributeValue.TYPE_ID) instanceof ListAttributeValue);
		assertTrue(factory.createAttributeValue(StringKeyedMapAttributeValue.TYPE_ID) instanceof StringKeyedMapAttributeValue);
		assertTrue(factory.createAttributeValue(Int8KeyedMapAttributeValue.TYPE_ID) instanceof Int8KeyedMapAttributeValue);
	}
	
	@Test(expected=UnknownAttributeValueTypeException.class)
	public void testCreateUnknownAttributeValue() {
		AttributeValueFactory factory = new AttributeValueFactory();
		factory.createAttributeValue(50000);
	}

}
