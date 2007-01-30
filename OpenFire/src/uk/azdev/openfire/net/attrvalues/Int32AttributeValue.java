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

import uk.azdev.openfire.net.util.BoundsUtil;

import static uk.azdev.openfire.net.util.IOUtil.*;

/**
 * Represents an unsigned 32-bit value. The methods for
 * getting and setting values in this class take long
 * integers to compensate for Java's lack of unsigned types,
 * but boundary conditions are still enforced.
 * @author Iain
 *
 */
public class Int32AttributeValue implements AttributeValue {

	public static final int TYPE_ID = 2;
	private long value;

	public Int32AttributeValue(long value) {
		setValue(value);
	}
	
	public Int32AttributeValue() {
		this(0);
	}

	public long getValue() {
		return value;
	}
	
	public void setValue(long value) {
		BoundsUtil.checkInt32Bounds(value, "value");
		this.value = value;
	}
	
	public int getSize() {
		return 4;
	}

	public int getTypeId() {
		return TYPE_ID;
	}

	public void readValue(ByteBuffer buffer) {
		this.value = readUnsignedInt(buffer);
	}

	public void writeValue(ByteBuffer buffer) {
		buffer.putInt((int)value);
	}

	public AttributeValue newInstance() {
		return new Int32AttributeValue();
	}

	@Override
	public String toString() {
		return Long.toString(value) + " (int32)";
	}
	
}
