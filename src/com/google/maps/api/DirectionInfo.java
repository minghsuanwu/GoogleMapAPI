package com.google.maps.api;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;

public class DirectionInfo extends DistanceInfo {
	public DateTime arrivalTime;
	public DateTime departureTime;
	public Distance Distance;
	public Duration Duration;
	public Duration DurationInTraffic;
	
	public String startAddress;
	public String endAddress;
	public LatLng startLocation;
	public LatLng endLocation;
	
	public ArrayList<String> directionsStepList = new ArrayList<String>();

	public void setDirectionsStepList(String directionsStep) {
		this.directionsStepList.add(directionsStep);
	}

	public void printInfo() {
		super.printInfo();
		
		if (startAddress != null) {			
			System.out.println("Start Address: " + startAddress);
		}

		if (endAddress != null) {
			System.out.println("End Address: " + endAddress);
		}
		
		if (startLocation != null) {
			System.out.println("Start Location: " + startLocation.lat + " | " + startLocation.lng);
		}
		
		if (endLocation != null) {
			System.out.println("End Location: " + endLocation + " | " + endLocation.lng);
		}
		System.out.println("Directions Step List: ");
		for (int i = 0; i < directionsStepList.size(); i++) {
			String replaceStep = directionsStepList.get(i).replaceAll("<.*?>", " ");
			System.out.println((i+1) + ". " + replaceStep);
		}
	}
}
