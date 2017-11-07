package com.google.maps.api;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.Fare;
import com.google.maps.model.TravelMode;

public class GoogleMapAPI {	
	public DistanceInfo calculateDistanceDuration(GeoApiContext context, String[] origins, String[] destinations, 
			TravelMode mode, String language) {
		DistanceInfo distanceInfo = new DistanceInfo();		
		try {
			DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
					.origins(origins)
					.destinations(destinations)
					.mode(mode)
//					.avoid(RouteRestriction.TOLLS)
//					.units(Unit.IMPERIAL)
					.language(language).await();
		
					/*
					 *         .avoid(RouteRestriction.TOLLS)
					 *         .units(Unit.IMPERIAL)
					 */
			if (matrix.originAddresses != null) {
				distanceInfo.setOriginAddressList(matrix.originAddresses);
//				System.out.println("Origin Addresses: " + matrix.originAddresses[0]);
			}
			if (matrix.destinationAddresses != null) {
				distanceInfo.setDestinationAddress(matrix.destinationAddresses);
//				System.out.println("Destination Addresses: " + matrix.destinationAddresses[0]);
			}

			for (DistanceMatrixRow row : matrix.rows) {				
				for (DistanceMatrixElement cell : row.elements) {
					if (cell.distance != null) {
						distanceInfo.distance = cell.distance.toString();
//						System.out.println("Distance: " + cell.distance.toString());
					}
	
					if (cell.duration != null) {
						distanceInfo.duration = cell.duration.toString();
//						System.out.println("Duration: " + cell.duration.toString());
					}
					if (cell.durationInTraffic != null) {
						distanceInfo.durationInTraffic = cell.durationInTraffic.toString();
//						System.out.println("Duration in Traffic: " + cell.durationInTraffic.toString());
					}
					Fare fare = cell.fare;
					if (fare!= null) {						
						distanceInfo.fare = fare.value + " " + fare.currency;
//						System.out.println("money: " + money);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distanceInfo;
	}
	
	public DirectionInfo calculateDirection(GeoApiContext context, String origin, String destination, TravelMode mode, String language) {
		DirectionInfo directionInfo = new DirectionInfo();
		try {
			DirectionsResult result = DirectionsApi.newRequest(context).mode(mode)
					// .waypoints("Charlestown,MA", "Lexington,MA")
					.origin(origin).destination(destination).language(language).await();

			DirectionsRoute[] directionsRoutes = result.routes;
			for (DirectionsRoute directionsRoute : directionsRoutes) {
				DirectionsLeg[] directionsLegs = directionsRoute.legs;
				for (DirectionsLeg directionsLeg : directionsLegs) {
					directionInfo.arrivalTime = directionsLeg.arrivalTime;
					directionInfo.departureTime = directionsLeg.departureTime;
					if (directionsLeg.distance != null)
						directionInfo.distance = directionsLeg.distance.humanReadable;
					if (directionsLeg.duration != null)
						directionInfo.duration = directionsLeg.duration.humanReadable;
					if (directionsLeg.durationInTraffic != null)
						directionInfo.durationInTraffic = directionsLeg.durationInTraffic.humanReadable;

					directionInfo.startAddress = directionsLeg.startAddress;
					directionInfo.endAddress = directionsLeg.endAddress;
					directionInfo.startLocation = directionsLeg.startLocation;
					directionInfo.endLocation = directionsLeg.endLocation;

					DirectionsStep[] directionsSteps = directionsLeg.steps;
					for (DirectionsStep directionsStep : directionsSteps) {
						String htmlInstructions = directionsStep.htmlInstructions;
						directionInfo.setDirectionsStepList(htmlInstructions);
//						System.out.println(htmlInstructions);
					}
				}

			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return directionInfo;
	}
	
	private String readConfig() {
		StringBuilder sb = new StringBuilder();
		String filePath = "config.txt";
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath), "UTF-8")); // read document format in "UTF-8"
			String str = null;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
		} catch (Exception e) {
			System.err.println("Please create your config.txt with your google server key in JSON format:");
			System.err.println("{\"serverkey\": \"your google server key\"}");
//			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return sb.toString();
	}

	class ConfigInfo {
		public String serverkey = "";
	}
	public String getServerKey() {
		String JSON = readConfig();
		Gson gson = new Gson();		
		ConfigInfo ci = gson.fromJson(JSON, ConfigInfo.class);
		
		return ci.serverkey;
	}
	
	public static void main(String[] args) throws Exception {
		GoogleMapAPI calDisDur = new GoogleMapAPI();
		String serverKey = calDisDur.getServerKey();
		GeoApiContext context = new GeoApiContext().setApiKey(serverKey);
		/*
		 * 捷運關渡站
		 * 捷運市政府站
		 * 台北市北投區知行路293巷9弄11號
		 */
		String[] origins = new String[] {"捷運關渡站"};
		String[] destinations = new String[] {"捷運市政府站"};
		String origin = "捷運關渡站";
		String destination = "捷運市政府站";
		
		/*
		 * 1. DRIVING
		 * 2. WALKING
		 * 3. BICYCLING
		 * 4. TRANSIT
		 */
		TravelMode mode = TravelMode.TRANSIT;
		/*
		 * 1. "zh-TW"
		 * 2. "en-AU"
		 */
		String language = "zh-TW";
		/*
		 * avoid
		 * 1. tolls
		 * 2. highways
		 * 3. ferries
		 * units
		 * 1. metric 
		 * 2. imperial
		 * region
		 * 
		 * status
		 * OK
		 * NOT_FOUND
		 * ZERO_RESULTS
		 * MAX_WAYPOINTS_EXCEEDED
		 * INVALID_REQUEST
		 * OVER_QUERY_LIMIT
		 * REQUEST_DENIED
		 * UNKNOWN_ERROR
		 * 
		 */						
		DistanceInfo distanceInfo = calDisDur.calculateDistanceDuration(context, origins, destinations, mode, language);
		distanceInfo.printInfo();
		System.out.println("=============================");
		DirectionInfo directionInfo = calDisDur.calculateDirection(context, origin, destination, mode, language);
		directionInfo.printInfo();
	}
}
