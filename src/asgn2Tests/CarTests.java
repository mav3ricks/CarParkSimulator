/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 22/04/2014
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;

import java.lang.reflect.Array;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2Exceptions.VehicleException;
import asgn2Vehicles.Car;
import asgn2Vehicles.Vehicle;

/**
 * @author hogan
 * 
 */
public class CarTests {

	Car C, SC;
	String vehID;
	int arrivalTime;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		arrivalTime = 1;

		vehID = "C1";
		C = new Car(vehID, arrivalTime, false);

		vehID = "S1";
		SC = new Car(vehID, arrivalTime, true);
	}

	/*
	 * Confirm that the API spec has not been violated through the addition of
	 * public fields, constructors or methods that were not requested
	 */
	@Test
	public void NoExtraPublicMethods() {
		// Car Class implements Vehicle, adds isSmall()
		final int NumVehicleClassMethods = Array.getLength(Vehicle.class
				.getMethods());
		final int NumCarClassMethods = Array.getLength(Car.class.getMethods());
		assertTrue("veh:" + NumVehicleClassMethods + ":car:"
				+ NumCarClassMethods,
				(NumVehicleClassMethods + 1) == NumCarClassMethods);
	}

	@Test
	public void NoExtraPublicFields() {
		// Same as Vehicle
		final int NumVehicleClassFields = Array.getLength(Vehicle.class
				.getFields());
		final int NumCarClassFields = Array.getLength(Car.class.getFields());
		assertTrue(
				"veh:" + NumVehicleClassFields + ":car:" + NumCarClassFields,
				(NumVehicleClassFields) == NumCarClassFields);
	}

	@Test
	public void NoExtraPublicConstructors() {
		// Same as Vehicle
		final int NumVehicleClassConstructors = Array.getLength(Vehicle.class
				.getConstructors());
		final int NumCarClassConstructors = Array.getLength(Car.class
				.getConstructors());
		assertTrue(":veh:" + NumVehicleClassConstructors + ":car:"
				+ NumCarClassConstructors,
				(NumVehicleClassConstructors) == NumCarClassConstructors);
	}

	/**
	 * Test method for
	 * {@link asgn2Vehicles.Car#Car(java.lang.String, int, boolean)}.
	 * 
	 * @throws VehicleException
	 */
	@Test(expected = VehicleException.class)
	public void testCarArrivalTimeOneBelowBoundary() throws VehicleException {
		arrivalTime = -1;
		C = new Car(vehID, arrivalTime, false);
	}

	@Test(expected = VehicleException.class)
	public void testCarArrivalTimeOnBoundary() throws VehicleException {
		arrivalTime = 0;
		C = new Car(vehID, arrivalTime, false);
	}

	@Test
	public void testCarArrivalTimeOneAboveBoundary() throws VehicleException {
		arrivalTime = 1;
		C = new Car(vehID, arrivalTime, false);
	}

	/**
	 * Test method for {@link asgn2Vehicles.Car#isSmall()}.
	 */
	@Test
	public void testIsSmall() {
		assertFalse(C.isSmall());
		assertTrue(SC.isSmall());
	}

}
