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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;

public abstract class AbstractServerEmulator implements Runnable {

	private ServerSocket socket;
	private Thread myThread;
	private boolean errorOccurred;
	private CountDownLatch latch;
	
	public AbstractServerEmulator() throws IOException {
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.configureBlocking(true);
		channel.socket().bind(new InetSocketAddress(0));
		socket = channel.socket();
		errorOccurred = false;
		latch = new CountDownLatch(1);
	}
	
	public void start() {
		myThread = new Thread(this, "ServerEmulator");
		myThread.start();
	}
	
	public int getBoundPort() {
		return socket.getLocalPort();
	}
	
	public void stop() throws InterruptedException {
		latch.countDown();
		myThread.join();
	}
	
	public boolean didErrorOccur() {
		return errorOccurred;
	}
	
	public void run() {
		try {
			Socket acceptedConnection = socket.accept();
			SocketChannel channel = acceptedConnection.getChannel();
			channel.configureBlocking(true);
			
			doWork(channel);

			latch.await();
			
			acceptedConnection.close();
			socket.close();
		} catch (Exception e) {
			System.err.println("Exception occurred in server emulator");
			e.printStackTrace();
			errorOccurred = true;
		}
	}
	
	public abstract void doWork(SocketChannel channel) throws IOException;
}