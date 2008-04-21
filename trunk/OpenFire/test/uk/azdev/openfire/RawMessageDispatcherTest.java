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
package uk.azdev.openfire;


import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.incoming.LoginSuccessMessage;

public class RawMessageDispatcherTest {

    private JUnit4Mockery context;
    private RawConnectionListener listener1;
    private RawConnectionListener listener2;
    private RawConnectionListener listener3;
    private RawMessageDispatcher dispatcher;
    
    @Before
    public void setUp() throws Exception {
        context = new JUnit4Mockery();
        listener1 = context.mock(RawConnectionListener.class);
        listener2 = context.mock(RawConnectionListener.class);
        listener3 = context.mock(RawConnectionListener.class);
        
        dispatcher = new RawMessageDispatcher();
        dispatcher.addRawListener(listener1);
        dispatcher.addRawListener(listener2);
        dispatcher.addRawListener(listener3);
    }
    
    @Test
    public void testMessageDispatch() {
        
        final IMessage message = new LoginSuccessMessage();
        
        context.checking(new Expectations() {{
            one(listener1).messageReceived(message);
            one(listener2).messageReceived(message);
            one(listener3).messageReceived(message);
        }});
        
        dispatcher.messageReceived(message);
        context.assertIsSatisfied();
    }

}
