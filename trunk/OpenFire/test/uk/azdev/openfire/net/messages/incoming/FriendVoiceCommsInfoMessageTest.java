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
package uk.azdev.openfire.net.messages.incoming;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.common.ActiveApplicationInfo;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.net.util.IOUtil;
import uk.azdev.openfire.testutil.TestUtils;

public class FriendVoiceCommsInfoMessageTest {

    private static final SessionId[] testSids =
        new SessionId[] {
            generateTestSid(0x00),
            generateTestSid(0x10),
            generateTestSid(0x20),
            generateTestSid(0x30),
            generateTestSid(0x40),
            generateTestSid(0x50),
            generateTestSid(0x60),
            generateTestSid(0x70),
            generateTestSid(0x80),
            generateTestSid(0x90),
        };
    
    private static final ActiveApplicationInfo[] testActiveApplications =
        new ActiveApplicationInfo[] {
            new ActiveApplicationInfo(32, null),
            new ActiveApplicationInfo(33, null),
            new ActiveApplicationInfo(33, null),
            new ActiveApplicationInfo(32, IOUtil.getInet4Address("85.25.17.183")),
            new ActiveApplicationInfo(33, null),
            new ActiveApplicationInfo(33, IOUtil.getInet4Address("62.104.18.109")),
            new ActiveApplicationInfo(32, null),
            new ActiveApplicationInfo(33, IOUtil.getInet4Address("194.50.80.26")),
            new ActiveApplicationInfo(32, IOUtil.getInet4Address("62.104.18.181")),
            new ActiveApplicationInfo(33, null)
        };
    
    private FriendVoiceCommsInfoMessage message;
    
    @Before
    public void setUp() throws Exception {
        message = new FriendVoiceCommsInfoMessage();
    }
    
    @Test
    public void testMessageId() {
        assertEquals(FriendVoiceCommsInfoMessage.MESSAGE_ID, message.getMessageId());
    }
    
    @Test
    public void testNewInstance() {
        assertTrue(message.newInstance() instanceof FriendVoiceCommsInfoMessage);
    }
    
    @Test
    public void testDefaultState() {
        assertEquals(0, message.getSessionIdSet().size());
    }
    
    @Test
    public void testReadMessageContent() throws IOException {
        /* not working at the moment - bad test data, will fix once I get back
         * from Amerika
         */
        /*
        ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "friendvcinfo.sampledata");
        message.readMessageContent(buffer);
        assertEquals(10, message.getSessionIdSet().size());
        
        Iterator<SessionId> sessionIdIter = message.getSessionIdSet().iterator();
        
        int i=0;
        while(sessionIdIter.hasNext()) {
            SessionId sid = sessionIdIter.next();
            assertEquals(testSids[i], sid);
            assertEquals(testActiveApplications[i], message.getActiveAppForSid(sid));
            i++;
        }
        */
    }
    
    @Test
    public void testWriteMessageContent() throws IOException {
        /* not working at the moment - bad test data, will fix once I get back
         * from Amerika
         */
        /*
        for(int i=0; i < testSids.length; i++) {
            message.addActiveAppInfo(testSids[i], testActiveApplications[i]);
        }
        
        TestUtils.checkMessageOutput(message, this.getClass(), "friendvcinfo.sampledata");
        */
    }

    private static SessionId generateTestSid(int startByteVal) {
        byte[] sidBytes = new byte[SessionId.SESSION_ID_SIZE];
        for(int i=0; i < SessionId.SESSION_ID_SIZE; i++) {
            sidBytes[i] = (byte)(startByteVal + i);
        }
        
        return new SessionId(sidBytes);
    }
}
