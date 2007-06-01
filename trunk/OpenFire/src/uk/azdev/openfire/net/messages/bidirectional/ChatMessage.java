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
package uk.azdev.openfire.net.messages.bidirectional;

import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.SessionIdAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.attrvalues.StringKeyedMapAttributeValue;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class ChatMessage extends StringMapBasedMessage {

	public static final int TYPE_ID = 2;
	
	private static final int CONTENT_MESSAGE_TYPE = 0;
	private static final int PEER_INFO_MESSAGE_TYPE = 2;
	private static final int USER_TYPING_MESSAGE_TYPE = 3;
	
	// top level keys
	private static final String SID_KEY = "sid";
	private static final String PEER_MESSAGE_KEY = "peermsg";
	
	// identifies the other keys we can expect within the peermsg map
	private static final String MESSAGE_TYPE_KEY = "msgtype";
	
	// other potential keys within the peerMsg
	private static final String IM_INDEX_KEY = "imindex";
	private static final String TYPING_KEY = "typing";
	private static final String IM_KEY = "im";
	
	// these keys are used by peer info messages, which we don't handle yet
//	private static final String IP_KEY = "ip";
//	private static final String PORT_KEY = "port";
//	private static final String LOCAL_IP_KEY = "localip";
//	private static final String LOCAL_PORT_KEY = "localport";
//	private static final String STATUS_KEY = "status";
//	private static final String SALT_KEY = "salt";
	
	private SessionId sid;
	private long messageType;
	
	private long messageIndex;
	private String message;
	private long typingVal;
	
	public SessionId getSessionId() {
		return sid;
	}
	
	public void setSessionId(SessionId sid) {
		this.sid = sid;
	}
	
	public boolean isContentMessage() {
		return messageType == CONTENT_MESSAGE_TYPE;
	}
	
	public boolean isPeerInfoMessage() {
		return messageType == PEER_INFO_MESSAGE_TYPE;
	}
	
	public boolean isTypingMessage() {
		return messageType == USER_TYPING_MESSAGE_TYPE;
	}
	
	public long getMessageIndex() {
		return messageIndex;
	}
	
	public String getMessage() {
		return message;
	}
	
	public long getTypingVal() {
		return typingVal;
	}
	
	public void setContentPayload(long messageIndex, String message) {
		this.messageType = CONTENT_MESSAGE_TYPE;
		this.messageIndex = messageIndex;
		this.message = message;
	}
	
	public void setTypingPayload(long messageIndex, long typingVal) {
		this.messageType = USER_TYPING_MESSAGE_TYPE;
		this.messageIndex = messageIndex;
		this.typingVal = typingVal;
	}
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		sid = map.getAttributeValue(SID_KEY, new SessionIdAttributeValue());
		StringKeyedAttributeMap payload = map.getAttributeValue(PEER_MESSAGE_KEY, new StringKeyedMapAttributeValue());
		messageType = payload.getAttributeValue(MESSAGE_TYPE_KEY, new Int32AttributeValue());
		
		switch((int)messageType) {
		case CONTENT_MESSAGE_TYPE:
			messageIndex = payload.getAttributeValue(IM_INDEX_KEY, new Int32AttributeValue());
			message = payload.getAttributeValue(IM_KEY, new StringAttributeValue());
			break;
		case PEER_INFO_MESSAGE_TYPE:
			break;
		case USER_TYPING_MESSAGE_TYPE:
			messageIndex = payload.getAttributeValue(IM_INDEX_KEY, new Int32AttributeValue());
			typingVal = payload.getAttributeValue(TYPING_KEY, new Int32AttributeValue());
			break;
		}
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		map.addAttribute(SID_KEY, new SessionIdAttributeValue(sid));
		
		StringKeyedMapAttributeValue payload = new StringKeyedMapAttributeValue();
		StringKeyedAttributeMap payloadMap = payload.getValue();
		
		payloadMap.addAttribute(MESSAGE_TYPE_KEY, new Int32AttributeValue(messageType));
		
		switch((int)messageType) {
		case CONTENT_MESSAGE_TYPE:
			payloadMap.addAttribute(IM_INDEX_KEY, new Int32AttributeValue(messageIndex));
			payloadMap.addAttribute(IM_KEY, new StringAttributeValue(message));
			break;
		case PEER_INFO_MESSAGE_TYPE:
			break;
		case USER_TYPING_MESSAGE_TYPE:
			payloadMap.addAttribute(IM_INDEX_KEY, new Int32AttributeValue(messageIndex));
			payloadMap.addAttribute(TYPING_KEY, new Int32AttributeValue(typingVal));
			break;
		}
		
		map.addAttribute(PEER_MESSAGE_KEY, payload);
	}

	public int getMessageId() {
		return TYPE_ID;
	}

	public IMessage newInstance() {
		return new ChatMessage();
	}
}
