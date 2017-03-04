package com.rafael;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.stripe.Stripe;

public class Server {
	private int usersConnected = 0;
	private Socket connection;
	
	public Server () {
		Stripe.apiKey = "sk_test_kOjw2kfG28OXE95mM9W5Al8d";
		System.out.println("We created a Server object!");
		runServer();
	}
	

	private void runServer() {
		try {
			ServerSocket server = new ServerSocket(5000, 10);
			while (true) {
				System.out.println("Waiting for connection...");
				connection = server.accept();
				System.out.println("Connected received from: " + connection.getInetAddress().getHostName());
				usersConnected++;
				
				ExecutorService threadPool = Executors.newCachedThreadPool();
				
				threadPool.execute(new ChargeUserRunnable(connection));
				ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) threadPool;
				System.out.printf("There are %d threads currently running%n", threadPoolExecutor.getActiveCount());
				System.out.println("A total of " + usersConnected + " users have connected");
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}
