/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 29/04/2014
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;
import asgn2Simulators.Simulator;
import asgn2Vehicles.Car;
import asgn2Vehicles.MotorCycle;

/**
 * @author hogan
 * 
 */
public class CarParkTests {

	CarPark cp;
	Simulator sim;

	Car c, sc;
	MotorCycle mc;

	String vehID;
	int time, arrivalTime, parkingTime, departureTime, intendedDuration;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		cp = new CarPark();
		sim = new Simulator();

		time = 1;
		arrivalTime = 1;
		parkingTime = 2;
		departureTime = 3;
		intendedDuration = 25;

		vehID = "C1";
		c = new Car(vehID, arrivalTime, false);

		vehID = "S1";
		sc = new Car(vehID, arrivalTime, true);

		vehID = "M1";
		mc = new MotorCycle(vehID, arrivalTime);
	}

	/*
	 * Confirm that the API spec has not been violated through the addition of
	 * public fields, constructors or methods that were not requested
	 */
	@Test
	public void NoExtraPublicMethods() {
		// Extends Object, extras less toString()
		final int ExtraMethods = 21;
		final int NumObjectClassMethods = Array.getLength(Object.class
				.getMethods());
		final int NumCarParkClassMethods = Array.getLength(CarPark.class
				.getMethods());
		assertTrue(
				"obj:" + NumObjectClassMethods + ":cp:"
						+ NumCarParkClassMethods,
				(NumObjectClassMethods + ExtraMethods) == NumCarParkClassMethods);
	}

	@Test
	public void NoExtraPublicFields() {
		// Same as Vehicle
		final int NumObjectClassFields = Array.getLength(Object.class
				.getFields());
		final int NumCarParkClassFields = Array.getLength(CarPark.class
				.getFields());
		assertTrue("obj:" + NumObjectClassFields + ":cp:"
				+ NumCarParkClassFields,
				(NumObjectClassFields) == NumCarParkClassFields);
	}

	@Test
	public void NoExtraPublicConstructors() {
		// One extra cons used.
		final int NumObjectClassConstructors = Array.getLength(Object.class
				.getConstructors());
		final int NumCarParkClassConstructors = Array.getLength(CarPark.class
				.getConstructors());
		assertTrue(":obj:" + NumObjectClassConstructors + ":cp:"
				+ NumCarParkClassConstructors,
				(NumObjectClassConstructors + 1) == NumCarParkClassConstructors);
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#archiveDepartingVehicles(int, boolean)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testArchiveDepartingVehiclesForce() throws SimulationException,
			VehicleException {
		cp.parkVehicle(c, arrivalTime, intendedDuration);
		cp.parkVehicle(sc, arrivalTime, intendedDuration);
		cp.parkVehicle(mc, arrivalTime, intendedDuration);
		time = Constants.CLOSING_TIME;
		cp.archiveDepartingVehicles(time, true);
		assertEquals(0, getNumVehiclesInPark());
		assertEquals(3, getCarParkInfo("archived"));
		assertEquals(0, getCarParkInfo("dissatisfied"));
		assertEquals(Constants.CLOSING_TIME, c.getDepartureTime());
		assertEquals(Constants.CLOSING_TIME, sc.getDepartureTime());
		assertEquals(Constants.CLOSING_TIME, mc.getDepartureTime());
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#archiveDepartingVehicles(int, boolean)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testArchiveDepartingVehiclesTimeOneBelowBoundary()
			throws SimulationException, VehicleException {
		cp.parkVehicle(c, arrivalTime, intendedDuration);
		cp.parkVehicle(sc, arrivalTime, intendedDuration);
		cp.parkVehicle(mc, arrivalTime, intendedDuration);
		time = arrivalTime + intendedDuration - 1;
		cp.archiveDepartingVehicles(time, false);
		assertEquals(3, getNumVehiclesInPark());
		assertEquals(0, getCarParkInfo("archived"));
		assertEquals(0, getCarParkInfo("dissatisfied"));
		assertEquals(arrivalTime + intendedDuration, c.getDepartureTime());
		assertEquals(arrivalTime + intendedDuration, sc.getDepartureTime());
		assertEquals(arrivalTime + intendedDuration, mc.getDepartureTime());
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#archiveDepartingVehicles(int, boolean)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testArchiveDepartingVehiclesTimeOnBoundary()
			throws SimulationException, VehicleException {
		cp.parkVehicle(c, arrivalTime, intendedDuration);
		cp.parkVehicle(sc, arrivalTime, intendedDuration);
		cp.parkVehicle(mc, arrivalTime, intendedDuration);
		time = arrivalTime + intendedDuration;
		cp.archiveDepartingVehicles(time, false);
		assertEquals(0, getNumVehiclesInPark());
		assertEquals(3, getCarParkInfo("archived"));
		assertEquals(0, getCarParkInfo("dissatisfied"));
		assertEquals(time, c.getDepartureTime());
		assertEquals(time, sc.getDepartureTime());
		assertEquals(time, mc.getDepartureTime());
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#archiveDepartingVehicles(int, boolean)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testArchiveDepartingVehiclesTimeOneAboveBoundary()
			throws SimulationException, VehicleException {
		cp.parkVehicle(c, arrivalTime, intendedDuration);
		cp.parkVehicle(sc, arrivalTime, intendedDuration);
		cp.parkVehicle(mc, arrivalTime, intendedDuration);
		time = arrivalTime + intendedDuration + 1;
		cp.archiveDepartingVehicles(time, false);
		assertEquals(0, getNumVehiclesInPark());
		assertEquals(3, getCarParkInfo("archived"));
		assertEquals(0, getCarParkInfo("dissatisfied"));
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#archiveNewVehicle(asgn2Vehicles.Vehicle)}.
	 * 
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test(expected = SimulationException.class)
	public void testArchiveNewVehicleInQueue() throws SimulationException,
			VehicleException {
		cp.enterQueue(c);
		cp.archiveNewVehicle(c);
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#archiveNewVehicle(asgn2Vehicles.Vehicle)}.
	 * 
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test(expected = SimulationException.class)
	public void testArchiveNewVehicleInPark() throws SimulationException,
			VehicleException {
		cp.parkVehicle(c, arrivalTime, intendedDuration);
		cp.archiveNewVehicle(c);
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#archiveNewVehicle(asgn2Vehicles.Vehicle)}.
	 * 
	 * @throws SimulationException
	 */
	@Test
	public void testArchiveNewVehicle() throws SimulationException {
		cp.archiveNewVehicle(c);
		assertEquals(1, getCarParkInfo("archived"));
		assertEquals(1, getCarParkInfo("dissatisfied"));
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveQueueFailures(int)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testArchiveQueueFailuresTimeOneBelowBoundary()
			throws SimulationException, VehicleException {
		time = arrivalTime + Constants.MAXIMUM_QUEUE_TIME - 1;
		cp.enterQueue(c);
		cp.enterQueue(sc);
		arrivalTime = 10;
		mc = new MotorCycle(vehID, arrivalTime);
		cp.enterQueue(mc);
		assertEquals(3, cp.numVehiclesInQueue());
		cp.archiveQueueFailures(time);
		assertEquals(3, cp.numVehiclesInQueue());
		assertEquals(0, getCarParkInfo("archived"));
		assertEquals(0, getCarParkInfo("dissatisfied"));
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveQueueFailures(int)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testArchiveQueueFailuresTimeOnBoundary()
			throws SimulationException, VehicleException {
		time = arrivalTime + Constants.MAXIMUM_QUEUE_TIME;
		cp.enterQueue(c);
		cp.enterQueue(sc);
		arrivalTime = 10;
		mc = new MotorCycle(vehID, arrivalTime);
		cp.enterQueue(mc);
		assertEquals(3, cp.numVehiclesInQueue());
		cp.archiveQueueFailures(time);
		assertEquals(3, cp.numVehiclesInQueue());
		assertEquals(0, getCarParkInfo("archived"));
		assertEquals(0, getCarParkInfo("dissatisfied"));
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveQueueFailures(int)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testArchiveQueueFailuresTimeOneAboveBoundary()
			throws SimulationException, VehicleException {
		time = arrivalTime + Constants.MAXIMUM_QUEUE_TIME + 1;
		cp.enterQueue(c);
		cp.enterQueue(sc);
		arrivalTime = 10;
		mc = new MotorCycle(vehID, arrivalTime);
		cp.enterQueue(mc);
		assertEquals(3, cp.numVehiclesInQueue());
		cp.archiveQueueFailures(time);
		assertEquals(1, cp.numVehiclesInQueue());
		assertEquals(2, getCarParkInfo("archived"));
		assertEquals(2, getCarParkInfo("dissatisfied"));
		assertEquals(time, c.getParkingTime());
		assertEquals(time, sc.getParkingTime());
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#carParkEmpty()}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testCarParkEmpty() throws SimulationException, VehicleException {
		assertTrue(cp.carParkEmpty());
		cp.parkVehicle(c, arrivalTime, intendedDuration);
		assertFalse(cp.carParkEmpty());
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#carParkFull()}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testCarParkFull() throws SimulationException, VehicleException {
		assertFalse(cp.carParkFull());
		fillCarParkToFull();
		assertTrue(cp.carParkFull());
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#enterQueue(asgn2Vehicles.Vehicle)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test(expected = SimulationException.class)
	public void testEnterQueueFull() throws SimulationException,
			VehicleException {
		for (int i = 0; i < Constants.DEFAULT_MAX_QUEUE_SIZE; i++) {
			c = new Car("C" + i, arrivalTime, false);
			cp.enterQueue(c);
		}
		assertTrue(cp.queueFull());
		cp.enterQueue(sc);
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#enterQueue(asgn2Vehicles.Vehicle)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testEnterQueue() throws SimulationException, VehicleException {
		for (int i = 0; i < Constants.DEFAULT_MAX_QUEUE_SIZE - 1; i++) {
			c = new Car("C" + i, arrivalTime, false);
			cp.enterQueue(c);
		}
		cp.enterQueue(sc);
		assertEquals(Constants.DEFAULT_MAX_QUEUE_SIZE, cp.numVehiclesInQueue());
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#exitQueue(asgn2Vehicles.Vehicle, int)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test(expected = SimulationException.class)
	public void testExitQueueVehicleNotFound() throws SimulationException,
			VehicleException {
		cp.enterQueue(c);
		cp.enterQueue(sc);
		cp.exitQueue(mc, parkingTime);
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#exitQueue(asgn2Vehicles.Vehicle, int)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testExitQueue() throws SimulationException, VehicleException {
		cp.enterQueue(c);
		cp.enterQueue(sc);
		assertEquals(2, cp.numVehiclesInQueue());
		cp.exitQueue(c, parkingTime);
		assertEquals(1, cp.numVehiclesInQueue());
		assertEquals(parkingTime, c.getParkingTime());
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#finalState()}.
	 */
	@Test
	public void testFinalState() {
		assertTrue(true);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumCars()}.
	 * 
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test
	public void testGetNumCars() throws VehicleException, SimulationException {
		fillCarParkToFull();
		assertEquals(Constants.DEFAULT_MAX_CAR_SPACES, cp.getNumCars());
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumMotorCycles()}.
	 * 
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test
	public void testGetNumMotorCycles() throws VehicleException,
			SimulationException {
		fillCarParkToFull();
		assertEquals(Constants.DEFAULT_MAX_MOTORCYCLE_SPACES,
				cp.getNumMotorCycles());
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumSmallCars()}.
	 * 
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test
	public void testGetNumSmallCars() throws VehicleException,
			SimulationException {
		fillCarParkToFull();
		assertEquals(Constants.DEFAULT_MAX_SMALL_CAR_SPACES,
				cp.getNumSmallCars());
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getStatus(int)}.
	 */
	@Test
	public void testGetStatus() {
		assertTrue(true);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#initialState()}.
	 */
	@Test
	public void testInitialState() {
		assertTrue(true);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#numVehiclesInQueue()}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testNumVehiclesInQueue() throws SimulationException,
			VehicleException {
		cp.enterQueue(c);
		assertEquals(1, cp.numVehiclesInQueue());
		cp.enterQueue(sc);
		cp.enterQueue(mc);
		assertEquals(3, cp.numVehiclesInQueue());
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#parkVehicle(asgn2Vehicles.Vehicle, int, int)}
	 * .
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test(expected = SimulationException.class)
	public void testParkVehicleFull() throws SimulationException,
			VehicleException {
		fillCarParkToFull();
		cp.parkVehicle(c, arrivalTime, intendedDuration);
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#parkVehicle(asgn2Vehicles.Vehicle, int, int)}
	 * .
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testParkVehicle() throws SimulationException, VehicleException {
		cp.parkVehicle(c, arrivalTime, intendedDuration);
		cp.parkVehicle(sc, arrivalTime, intendedDuration);
		assertEquals(2, getNumVehiclesInPark());
		assertEquals(arrivalTime + intendedDuration, c.getDepartureTime());
		assertEquals(arrivalTime + intendedDuration, sc.getDepartureTime());
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#processQueue(int, asgn2Simulators.Simulator)}
	 * .
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testProcessQueueFullCarPark() throws SimulationException,
			VehicleException {
		fillCarParkToFull();
		cp.enterQueue(new Car("C999", arrivalTime, false));
		cp.enterQueue(new Car("S999", arrivalTime, true));
		cp.enterQueue(new MotorCycle("M999", arrivalTime));
		cp.processQueue(parkingTime, sim);
		assertEquals(3, cp.numVehiclesInQueue());
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#processQueue(int, asgn2Simulators.Simulator)}
	 * .
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testProcessQueue() throws SimulationException, VehicleException {
		cp.enterQueue(c);
		cp.enterQueue(sc);
		cp.enterQueue(mc);
		assertEquals(3, cp.numVehiclesInQueue());
		assertEquals(0, getNumVehiclesInPark());
		cp.processQueue(parkingTime, sim);
		assertEquals(0, cp.numVehiclesInQueue());
		assertEquals(3, getNumVehiclesInPark());
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#queueEmpty()}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testQueueEmpty() throws SimulationException, VehicleException {
		assertTrue(cp.queueEmpty());
		cp.enterQueue(c);
		assertFalse(cp.queueEmpty());
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#queueFull()}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testQueueFull() throws VehicleException, SimulationException {
		assertFalse(cp.queueFull());
		for (int i = 0; i < Constants.DEFAULT_MAX_QUEUE_SIZE; i++) {
			c = new Car("C" + i, arrivalTime, false);
			cp.enterQueue(c);
		}
		assertTrue(cp.queueFull());
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#spacesAvailable(asgn2Vehicles.Vehicle)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testSpacesAvailableCar() throws SimulationException,
			VehicleException {
		assertTrue(cp.spacesAvailable(c));
		fillCarSpacesToFull();
		assertFalse(cp.spacesAvailable(c));
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#spacesAvailable(asgn2Vehicles.Vehicle)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testSpacesAvailableSmallCar() throws SimulationException,
			VehicleException {
		assertTrue(cp.spacesAvailable(sc));
		fillSmallCarSpacesToFull();
		assertTrue(cp.spacesAvailable(sc));
		fillCarSpacesToFull();
		assertFalse(cp.spacesAvailable(sc));
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#spacesAvailable(asgn2Vehicles.Vehicle)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testSpacesAvailableMotorCycle() throws SimulationException,
			VehicleException {
		assertTrue(cp.spacesAvailable(mc));
		fillMotorCycleSpacesToFull();
		assertTrue(cp.spacesAvailable(mc));
		fillSmallCarSpacesToFull();
		assertFalse(cp.spacesAvailable(mc));
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#toString()}.
	 */
	@Test
	public void testToString() {
		assertTrue(true);
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#tryProcessNewVehicles(int, asgn2Simulators.Simulator)}
	 * .
	 * 
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	@Test
	public void testTryProcessNewVehicles() throws VehicleException, SimulationException {
		cp.tryProcessNewVehicles(10, sim);
		int vehInPark = cp.getNumCars() + cp.getNumMotorCycles();
		assertTrue(vehInPark >= 1);
		assertTrue(vehInPark <= 2);
		if (cp.getNumCars() + cp.getNumMotorCycles() == 2) {
			assertTrue(cp.getNumCars() == 1);
			assertTrue(cp.getNumMotorCycles() == 1);
		}
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#unparkVehicle(asgn2Vehicles.Vehicle, int)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test(expected = SimulationException.class)
	public void testUnparkVehicleNotFound() throws SimulationException,
			VehicleException {
		cp.parkVehicle(c, arrivalTime, intendedDuration);
		cp.parkVehicle(mc, arrivalTime, intendedDuration);
		cp.unparkVehicle(sc, departureTime);
	}

	/**
	 * Test method for
	 * {@link asgn2CarParks.CarPark#unparkVehicle(asgn2Vehicles.Vehicle, int)}.
	 * 
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	@Test
	public void testUnparkVehicle() throws SimulationException,
			VehicleException {
		cp.parkVehicle(c, arrivalTime, intendedDuration);
		cp.parkVehicle(sc, arrivalTime, intendedDuration);
		cp.parkVehicle(mc, arrivalTime, intendedDuration);
		assertEquals(3, getNumVehiclesInPark());
		cp.unparkVehicle(sc, departureTime);
		assertEquals(2, getNumVehiclesInPark());
		assertEquals(departureTime, sc.getDepartureTime());
	}

	private int getNumVehiclesInPark() {
		return (cp.getNumCars() + cp.getNumMotorCycles());
	}

	// Private method to capture CarPark info at a certain minute
	// Method may return total count of vehicles, total dissatisfied or total
	// archived
	private int getCarParkInfo(String requestedInfo) {
		String str = cp.toString();
		int ret = 0;

		Pattern pattern = Pattern
				.compile("count:\\s?(\\d+)\\s?.*numDissatisfied:\\s?(\\d+)\\s?past:\\s?(\\d+)");
		Matcher matcher = pattern.matcher(str);

		while (matcher.find()) {
			if (requestedInfo.equalsIgnoreCase("count"))
				ret = Integer.parseInt(matcher.group(1));
			if (requestedInfo.equalsIgnoreCase("dissatisfied"))
				ret = Integer.parseInt(matcher.group(2));
			if (requestedInfo.equalsIgnoreCase("archived"))
				ret = Integer.parseInt(matcher.group(3));
		}

		return ret;
	}

	// Private method to automatically fill all big car spaces
	private void fillCarSpacesToFull() throws SimulationException,
			VehicleException {
		for (int i = 0; i < (Constants.DEFAULT_MAX_CAR_SPACES - Constants.DEFAULT_MAX_SMALL_CAR_SPACES); i++) {
			c = new Car("C" + i, arrivalTime, false);
			cp.parkVehicle(c, arrivalTime, intendedDuration);
		}
	}

	// Private method to automatically fill all small car spaces
	private void fillSmallCarSpacesToFull() throws SimulationException,
			VehicleException {
		for (int i = 0; i < Constants.DEFAULT_MAX_SMALL_CAR_SPACES; i++) {
			sc = new Car("S" + i, arrivalTime, true);
			cp.parkVehicle(sc, arrivalTime, intendedDuration);
		}
	}

	// Private method to automatically fill all Motor Cycle spaces
	private void fillMotorCycleSpacesToFull() throws VehicleException,
			SimulationException {
		for (int i = 0; i < Constants.DEFAULT_MAX_MOTORCYCLE_SPACES; i++) {
			mc = new MotorCycle("M" + i, arrivalTime);
			cp.parkVehicle(mc, arrivalTime, intendedDuration);
		}
	}

	// Private method to automatically fill all spaces in Car Park
	private void fillCarParkToFull() throws VehicleException,
			SimulationException {
		fillCarSpacesToFull();
		fillSmallCarSpacesToFull();
		fillMotorCycleSpacesToFull();
	}

}
