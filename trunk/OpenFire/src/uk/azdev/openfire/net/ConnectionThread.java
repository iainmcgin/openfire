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
package uk.azdev.openfire.net;

public abstract class ConnectionThread implements Runnable {

	protected ConnectionStateListener listener;
	protected boolean plannedStop;
	private Thread associatedThread;
	
	public ConnectionThread(ConnectionStateListener listener) {
		this.listener = listener;
		plannedStop = false;
	}
	
	public void waitForExit() throws InterruptedException {
		associatedThread.join();
	}
	
	public void setPlannedStop() throws InterruptedException {
		plannedStop = true;
	}
	
	public void start(String threadName) {
		associatedThread = new Thread(this, threadName);
		associatedThread.setDaemon(true);
		associatedThread.start();
	}
	
	public void run() {
		try {
			doProcessing();
		} catch(Exception e) {
			listener.connectionError(e);
		}
	}

	protected abstract void doProcessing() throws Exception;
}
