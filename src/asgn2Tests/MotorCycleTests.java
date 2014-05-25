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
import asgn2Simulators.Constants;
import asgn2Vehicles.MotorCycle;

/**
 * @author hogan
 *
 */
public class MotorCycleTests {
	
	MotorCycle mc;
	String vehID;
	int arrivalTime, parkingTime, departureTime, intendedDuration;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		vehID = "MC1";
		arrivalTime = 1;
		parkingTime = 2;
		departureTime = 3;
		intendedDuration = 25;
		
		mc = new MotorCycle(vehID, arrivalTime);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.MotorCycle#MotorCycle(java.lang.String, int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testMotorCycleArrivalTimeOneBelowBoundary() throws VehicleException {
		arrivalTime = -1;
		mc = new MotorCycle(vehID, arrivalTime);
	}

	/**
	 * Test method for {@link asgn2Vehicles.MotorCycle#MotorCycle(java.lang.String, int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testMotorCycleArrivalTimeOnBoundary() throws VehicleException {
		arrivalTime = 0;
		mc = new MotorCycle(vehID, arrivalTime);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.MotorCycle#MotorCycle(java.lang.String, int)}.
	 * @throws VehicleException 
	 */
	@Test
	public void testMotorCycleArrivalTimeOneAboveBoundary() throws VehicleException {
		arrivalTime = 1;
		mc = new MotorCycle(vehID, arrivalTime);
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getVehID()}.
	 */
	@Test
	public void testGetVehID() {
		assertEquals(vehID, mc.getVehID());
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getArrivalTime()}.
	 */
	@Test
	public void testGetArrivalTime() {
		assertEquals(arrivalTime, mc.getArrivalTime());
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterQueuedState()}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testEnterQueuedStateAlreadyInQueue() throws VehicleException {
		mc.enterQueuedState();
		mc.enterQueuedState();
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterQueuedState()}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testEnterQueuedStateAlreadyInPark() throws VehicleException {
		mc.enterParkedState(parkingTime, intendedDuration);
		mc.enterQueuedState();
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterQueuedState()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testEnterQueuedState() throws VehicleException {
		mc.enterQueuedState();
		assertTrue(mc.isQueued());
		assertFalse(mc.wasQueued());
		assertFalse(mc.isParked());
		assertFalse(mc.wasParked());
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitQueuedState(int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testExitQueuedStateInPark() throws VehicleException {
		mc.enterParkedState(parkingTime, intendedDuration);
		mc.exitQueuedState(parkingTime);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitQueuedState(int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testExitQueuedStateNotInQueue() throws VehicleException {
		mc.exitQueuedState(parkingTime);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitQueuedState(int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testExitQueuedStateExitTimeOneBelowBoundary() throws VehicleException {
		mc.enterQueuedState();
		mc.exitQueuedState(arrivalTime - 1);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitQueuedState(int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testExitQueuedStateExitTimeOnBoundary() throws VehicleException {
		mc.enterQueuedState();
		mc.exitQueuedState(arrivalTime);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitQueuedState(int)}.
	 * @throws VehicleException 
	 */
	@Test
	public void testExitQueuedStateExitTimeOneAboveBoundary() throws VehicleException {
		mc.enterQueuedState();
		mc.exitQueuedState(arrivalTime + 1);
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitQueuedState(int)}.
	 * @throws VehicleException 
	 */
	@Test
	public void testExitQueuedState() throws VehicleException {
		mc.enterQueuedState();
		mc.exitQueuedState(parkingTime);
		assertFalse(mc.isQueued());
		assertTrue(mc.wasQueued());
		assertEquals(parkingTime, mc.getParkingTime());
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testEnterParkedStateAlreadyInPark() throws VehicleException {
		mc.enterParkedState(parkingTime, intendedDuration);
		mc.enterParkedState(parkingTime, intendedDuration);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testEnterParkedStateInQueue() throws VehicleException {
		mc.enterQueuedState();
		mc.enterParkedState(parkingTime, intendedDuration);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testEnterParkedStateParkingTimeOneBelowBoundary() throws VehicleException {
		parkingTime = -1;
		mc.enterParkedState(parkingTime, intendedDuration);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testEnterParkedStateParkingTimeOnBoundary() throws VehicleException {
		parkingTime = 0;
		mc.enterParkedState(parkingTime, intendedDuration);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 * @throws VehicleException 
	 */
	@Test
	public void testEnterParkedStateParkingTimeOneAboveBoundary() throws VehicleException {
		parkingTime = 1;
		mc.enterParkedState(parkingTime, intendedDuration);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testEnterParkedStateIntendedDurationOneBelowBoundary() throws VehicleException {
		intendedDuration = Constants.MINIMUM_STAY - 1;
		mc.enterParkedState(parkingTime, intendedDuration);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 * @throws VehicleException 
	 */
	@Test
	public void testEnterParkedStateIntendedDurationOnBoundary() throws VehicleException {
		intendedDuration = Constants.MINIMUM_STAY;
		mc.enterParkedState(parkingTime, intendedDuration);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 * @throws VehicleException 
	 */
	@Test
	public void testEnterParkedStateIntendedDurationOneAboveBoundary() throws VehicleException {
		intendedDuration = Constants.MINIMUM_STAY + 1;
		mc.enterParkedState(parkingTime, intendedDuration);
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 * @throws VehicleException 
	 */
	@Test
	public void testEnterParkedState() throws VehicleException {
		mc.enterParkedState(parkingTime, intendedDuration);
		assertFalse(mc.isQueued());
		assertTrue(mc.isParked());
		assertFalse(mc.wasParked());
		assertEquals(parkingTime, mc.getParkingTime());
		assertEquals(parkingTime + intendedDuration, mc.getDepartureTime());
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState()}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testExitParkedStateNotInPark() throws VehicleException {
		mc.exitParkedState(parkingTime + intendedDuration);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState()}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testExitParkedStateInQueue() throws VehicleException {
		mc.enterQueuedState();
		mc.exitParkedState(parkingTime + intendedDuration);
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState(int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testExitParkedStateDepartureTimeOneBelowBoundary() throws VehicleException {
		departureTime = parkingTime - 1;
		mc.enterParkedState(parkingTime, intendedDuration);
		mc.exitParkedState(departureTime);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState(int)}.
	 * @throws VehicleException 
	 */
	@Test
	public void testExitParkedStateDepartureTimeOnBoundary() throws VehicleException {
		departureTime = parkingTime;
		mc.enterParkedState(parkingTime, intendedDuration);
		mc.exitParkedState(departureTime);
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState(int)}.
	 * @throws VehicleException 
	 */
	@Test
	public void testExitParkedStateDepartureTimeOneAboveBoundary() throws VehicleException {
		departureTime = parkingTime + 1;
		mc.enterParkedState(parkingTime, intendedDuration);
		mc.exitParkedState(departureTime);
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testExitParkedState() throws VehicleException {
		mc.enterParkedState(parkingTime, intendedDuration);
		mc.exitParkedState(parkingTime + intendedDuration);
		assertFalse(mc.isQueued());
		assertFalse(mc.isParked());
		assertTrue(mc.wasParked());
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isParked()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testIsParked() throws VehicleException {
		assertFalse(mc.isParked());
		mc.enterParkedState(parkingTime, intendedDuration);
		assertTrue(mc.isParked());
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isQueued()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testIsQueued() throws VehicleException {
		assertFalse(mc.isQueued());
		mc.enterQueuedState();
		assertTrue(mc.isQueued());
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getParkingTime()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testGetParkingTime() throws VehicleException {
		mc.enterParkedState(parkingTime, intendedDuration);
		assertEquals(parkingTime, mc.getParkingTime());
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getDepartureTime()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testGetDepartureTime() throws VehicleException {
		mc.enterParkedState(parkingTime, intendedDuration);
		assertEquals(parkingTime + intendedDuration, mc.getDepartureTime());
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#wasQueued()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testWasQueued() throws VehicleException {
		assertFalse(mc.wasQueued());
		mc.enterQueuedState();mc.exitQueuedState(parkingTime);
		assertTrue(mc.wasQueued());
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#wasParked()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testWasParked() throws VehicleException {
		assertFalse(mc.wasParked());
		mc.enterParkedState(parkingTime, intendedDuration);
		mc.exitParkedState(departureTime);
		assertTrue(mc.wasParked());
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isSatisfied()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testIsSatisfied() throws VehicleException {
		assertFalse(mc.isSatisfied());
		mc.enterQueuedState();
		mc.exitQueuedState(parkingTime);
		assertFalse(mc.isSatisfied());
		mc.enterParkedState(parkingTime, intendedDuration);
		mc.exitParkedState(departureTime);
		assertTrue(mc.isSatisfied());
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#toString()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testToString() throws VehicleException {
		assertTrue(true);
	}

}
