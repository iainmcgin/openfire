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

import java.nio.ByteBuffer;

import static uk.azdev.openfire.net.util.IOUtil.*;

/**
 * The "did" attribute value, which comes packaged as part of
 * message type 400. It's purpose is, at present, unknown.
 */
public class DidAttributeValue implements AttributeValue {

	public static final int TYPE_ID = 6;
	private static final int DID_SIZE = 22;
	
	private byte[] value;
	
	public DidAttributeValue() {
		value = new byte[DID_SIZE];
	}
	
	public int getSize() {
		return DID_SIZE;
	}

	public int getTypeId() {
		return TYPE_ID;
	}

	public AttributeValue newInstance() {
		return new DidAttributeValue();
	}

	public void readValue(ByteBuffer buffer) {
		buffer.get(value);
	}

	public void writeValue(ByteBuffer buffer) {
		buffer.put(value);
	}
	
	@Override
	public String toString() {
		return "DID:<" + printByteArray(value) + ">";
	}

}