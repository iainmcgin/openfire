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
package uk.azdev.openfire.friendlist.messageprocessors;

import uk.azdev.openfire.XFireConnection;
import uk.azdev.openfire.net.messages.IMessage;

public class LoginFailureMessageProcessor implements IMessageProcessor {
	
	private final XFireConnection connection;

	public LoginFailureMessageProcessor(XFireConnection connection) {
		this.connection = connection;
	}

	public void processMessage(IMessage message) {
		new Thread(new Runnable() {

			public void run() {
				try {
					connection.loginFailed();
				} catch (Exception e) {
					System.err.println("Error occurred while killing connection controller");
					e.printStackTrace();
				}
			}
		}).start();
	}
}