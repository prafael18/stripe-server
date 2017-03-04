package com.rafael;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

public class ChargeUserRunnable implements Runnable {
	private Socket userConnection;
	ObjectInputStream input;
	
	public ChargeUserRunnable (Socket userConnection) {
		this.userConnection = userConnection;
	}
	
	public void run() {
		
		try {
			input = new ObjectInputStream (userConnection.getInputStream());
			System.out.println("Retrieved IO streams");

			
			String tokenId = (String) input.readObject();
			
			System.out.println("We received a message from the client: " + tokenId);
			
			Map<String, Object> customerParams = new HashMap<String, Object>();
			customerParams.put("description", "Customer for lily.thompson@example.com");
			customerParams.put("source", "tok_19qApyHDtVlHkX7j336Jit8R");
			// ^ obtained with Stripe.js
			
			Map<String, Object> chargeMap = new HashMap<String, Object>();
			chargeMap.put("amount", 1000);
			chargeMap.put("currency", "usd");
			chargeMap.put("description", "Example charge");
			chargeMap.put("source", tokenId);

			try {
			 Charge charge = Charge.create(chargeMap);
			 Customer customer = Customer.create(customerParams);
			 System.out.println(customer);
			 System.out.println(charge);
			}
			catch (StripeException e) {
			 e.printStackTrace();
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			try {
				input.close();
				userConnection.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
