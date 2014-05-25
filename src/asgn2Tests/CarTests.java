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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2Exceptions.VehicleException;
import asgn2Vehicles.Car;

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

	/**
	 * Test method for {@link asgn2Vehicles.Car#Car(java.lang.String, int, boolean)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testCarArrivalTimeOneBelowBoundary() throws VehicleException {
		arrivalTime = -1;
		C = new Car(vehID, arrivalTime, false);
	}
	
	@Test (expected = VehicleException.class)
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
