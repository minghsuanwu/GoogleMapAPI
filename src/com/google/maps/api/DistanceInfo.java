package com.google.maps.api;

import java.util.ArrayList;

public class DistanceInfo {
	public ArrayList<String> originAddressList = new ArrayList<String>();
	public ArrayList<String> destinationAddressList = new ArrayList<String>();
	public String distance;
	public String duration;
	public String durationInTraffic;
	public String fare;
	
	public void setOriginAddressList(String[] originAddresses) {
		for (String originAddress: originAddresses) {
			if (!this.originAddressList.contains(originAddress))
				this.originAddressList.add(originAddress);
		}
	}
	
	public void setDestinationAddress(String[] destinationAddresses) {
		for (String destinationAddress: destinationAddresses) {
			if (!this.destinationAddressList.contains(destinationAddress))
				this.destinationAddressList.add(destinationAddress);
		}
	}

	public void printInfo() {
		System.out.println("originAddressList: "+originAddressList);
		System.out.println("destinationAddressList: "+destinationAddressList);
		
		if (distance != null) {			
			System.out.println("Distance: " + distance);
		}
		if (duration != null) {
			System.out.println("Duration: " + duration);
		}		
		if (durationInTraffic != null) {
			System.out.println("Duration in Traffic: " + durationInTraffic);
		}
		if (fare != null) {
			System.out.println("Fare: " + fare);
		}
	}

	
	
}
