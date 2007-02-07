/*
 * JFire - a Java API to access the XFire instant messaging network.
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
package uk.azdev.openfire.net.messages.incoming;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


import uk.azdev.openfire.net.attrvalues.AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.SessionIdAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;
import uk.azdev.openfire.net.util.IOUtil;

public class BuddyStatusMessage extends StringMapBasedMessage {

	public static final int BUDDY_STATUS_MESSAGE_ID = 154;

	private static final String SESSION_ID_LIST_KEY = "sid";
	private static final String STATUS_LIST_KEY = "msg";

	private Map<byte[], String> sessionIdToStatusMap;
	
	public BuddyStatusMessage() {
		sessionIdToStatusMap = new LinkedHashMap<byte[], String>();
	}
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		ListAttributeValue sessionIdListValue = ((ListAttributeValue)map.getAttributeValue(SESSION_ID_LIST_KEY));
		List<String> statusList = map.getAttributeValueAsStringList(STATUS_LIST_KEY);
		
		Iterator<AttributeValue> sessionIdIter = sessionIdListValue.getList().iterator();
		Iterator<String> statusIter = statusList.iterator();
		while(sessionIdIter.hasNext()) {
			SessionIdAttributeValue sidValue = (SessionIdAttributeValue)sessionIdIter.next();
			String status = statusIter.next();
			sessionIdToStatusMap.put(sidValue.getSessionId(), status);
		}
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		ListAttributeValue sessionIdList = new ListAttributeValue();
		ListAttributeValue statusList = new ListAttributeValue();
		for(Entry<byte[], String> entry : sessionIdToStatusMap.entrySet()) {
			sessionIdList.addValue(new SessionIdAttributeValue(entry.getKey()));
			statusList.addValue(new StringAttributeValue(entry.getValue()));
		}
		
		map.addAttribute(SESSION_ID_LIST_KEY, sessionIdList);
		map.addAttribute(STATUS_LIST_KEY, statusList);
	}

	public int getMessageId() {
		return BUDDY_STATUS_MESSAGE_ID;
	}

	public IMessage newInstance() {
		return new BuddyStatusMessage();
	}

	public Set<byte[]> getSessionIdSet() {
		return sessionIdToStatusMap.keySet();
	}

	public Object getStatusForSessionId(byte[] sessionId) {
		return sessionIdToStatusMap.get(sessionId);
	}

	public void addStatus(byte[] sessionId, String status) {
		sessionIdToStatusMap.put(sessionId, status);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Buddy Status Message");
		buffer.append("\n\tStatuses:");
		
		for(Entry<byte[], String> entry : sessionIdToStatusMap.entrySet()) {
			buffer.append("\n\t");
			buffer.append(IOUtil.printByteArray(entry.getKey()));
			buffer.append(" -> \"");
			buffer.append(entry.getValue());
			buffer.append("\"");
		}
		return buffer.toString();
	}

}
