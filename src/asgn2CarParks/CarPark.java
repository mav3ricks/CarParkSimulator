/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2CarParks 
 * 21/04/2014
 * 
 */
package asgn2CarParks;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;
import asgn2Simulators.Simulator;
import asgn2Vehicles.Car;
import asgn2Vehicles.MotorCycle;
import asgn2Vehicles.Vehicle;

/**
 * The CarPark class provides a range of facilities for working with a car park
 * in support of the simulator. In particular, it maintains a collection of
 * currently parked vehicles, a queue of vehicles wishing to enter the car park,
 * and an historical list of vehicles which have left or were never able to gain
 * entry.
 * 
 * The class maintains a wide variety of constraints on small cars, normal cars
 * and motorcycles and their access to the car park. See the method javadoc for
 * details.
 * 
 * The class relies heavily on the asgn2.Vehicle hierarchy, and provides a
 * series of reports used by the logger.
 * 
 * @author hogan
 * 
 */
public class CarPark {

	private String status;
	private int maxCarSpaces, maxSmallCarSpaces, maxMotorCycleSpaces,
			maxQueueSize, count, numDissatisfied;
	private List<Vehicle> currentlyParked, archived;
	private Queue<Vehicle> currentlyQueued;

	/**
	 * CarPark constructor sets the basic size parameters. Uses default
	 * parameters
	 */
	public CarPark() {
		this(Constants.DEFAULT_MAX_CAR_SPACES,
				Constants.DEFAULT_MAX_SMALL_CAR_SPACES,
				Constants.DEFAULT_MAX_MOTORCYCLE_SPACES,
				Constants.DEFAULT_MAX_QUEUE_SIZE);
	}

	/**
	 * CarPark constructor sets the basic size parameters.
	 * 
	 * @param maxCarSpaces
	 *            maximum number of spaces allocated to cars in the car park
	 * @param maxSmallCarSpaces
	 *            maximum number of spaces (a component of maxCarSpaces)
	 *            restricted to small cars
	 * @param maxMotorCycleSpaces
	 *            maximum number of spaces allocated to MotorCycles
	 * @param maxQueueSize
	 *            maximum number of vehicles allowed to queue
	 */
	public CarPark(int maxCarSpaces, int maxSmallCarSpaces,
			int maxMotorCycleSpaces, int maxQueueSize) {
		this.maxCarSpaces = maxCarSpaces;
		this.maxSmallCarSpaces = maxSmallCarSpaces;
		this.maxMotorCycleSpaces = maxMotorCycleSpaces;
		this.maxQueueSize = maxQueueSize;

		this.status = "";

		this.count = 0;
		this.numDissatisfied = 0;

		this.currentlyParked = new CopyOnWriteArrayList<Vehicle>();
		this.archived = new CopyOnWriteArrayList<Vehicle>();
		this.currentlyQueued = new ConcurrentLinkedQueue<Vehicle>();
	}

	/**
	 * Archives vehicles exiting the car park after a successful stay. Includes
	 * transition via Vehicle.exitParkedState().
	 * 
	 * @param time
	 *            int holding time at which vehicle leaves
	 * @param force
	 *            boolean forcing departure to clear car park
	 * @throws VehicleException
	 *             if vehicle to be archived is not in the correct state
	 * @throws SimulationException
	 *             if one or more departing vehicles are not in the car park
	 *             when operation applied
	 */
	public void archiveDepartingVehicles(int time, boolean force)
			throws VehicleException, SimulationException {
		for (Vehicle v : this.currentlyParked) {
			if (force || v.getDepartureTime() <= time) {
				this.unparkVehicle(v, time);
				this.archived.add(v);
				this.status += this.setVehicleMsg(v, "P", "A");
			}
		}
	}

	/**
	 * Method to archive new vehicles that don't get parked or queued and are
	 * turned away
	 * 
	 * @param v
	 *            Vehicle to be archived
	 * @throws SimulationException
	 *             if vehicle is currently queued or parked
	 */
	public void archiveNewVehicle(Vehicle v) throws SimulationException {
		if (v.isQueued())
			throw new SimulationException(
					"CarPark archiveNewVehicle(): Vehicle is currently in queue, isQueued: "
							+ v.isQueued());
		if (v.isParked())
			throw new SimulationException(
					"CarPark archiveNewVehicle(): Vehicle is currently in park, isParked: "
							+ v.isParked());
		this.archived.add(v);
		this.numDissatisfied++;
		this.status += this.setVehicleMsg(v, "N", "A");
	}

	/**
	 * Archive vehicles which have stayed in the queue too long
	 * 
	 * @param time
	 *            int holding current simulation time
	 * @throws VehicleException
	 *             if one or more vehicles not in the correct state or if timing
	 *             constraints are violated
	 */
	public void archiveQueueFailures(int time) throws VehicleException {
		for (Vehicle v : this.currentlyQueued) {
			if (time > v.getArrivalTime() + Constants.MAXIMUM_QUEUE_TIME) {
				try {
					this.exitQueue(v, time);
					this.archived.add(v);
					this.numDissatisfied++;
					this.status += this.setVehicleMsg(v, "Q", "A");
				} catch (SimulationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Simple status showing whether carPark is empty
	 * 
	 * @return true if car park empty, false otherwise
	 */
	public boolean carParkEmpty() {
		return this.currentlyParked.size() == 0;
	}

	/**
	 * Simple status showing whether carPark is full
	 * 
	 * @return true if car park full, false otherwise
	 */
	public boolean carParkFull() {
		return this.currentlyParked.size() == (this.maxCarSpaces + this.maxMotorCycleSpaces);
	}

	/**
	 * Method to add vehicle successfully to the queue Precondition is a test
	 * that spaces are available Includes transition through
	 * Vehicle.enterQueuedState
	 * 
	 * @param v
	 *            Vehicle to be added
	 * @throws SimulationException
	 *             if queue is full
	 * @throws VehicleException
	 *             if vehicle not in the correct state
	 */
	public void enterQueue(Vehicle v) throws SimulationException,
			VehicleException {
		if (this.queueFull())
			throw new SimulationException(
					"CarPark enterQueue(): Queue is already full");
		this.currentlyQueued.add(v);
		v.enterQueuedState();
		this.status += this.setVehicleMsg(v, "N", "Q");
	}

	/**
	 * Method to remove vehicle from the queue after which it will be parked or
	 * archived. Includes transition through Vehicle.exitQueuedState.
	 * 
	 * @param v
	 *            Vehicle to be removed from the queue
	 * @param exitTime
	 *            int time at which vehicle exits queue
	 * @throws SimulationException
	 *             if vehicle is not in queue
	 * @throws VehicleException
	 *             if the vehicle is in an incorrect state or timing constraints
	 *             are violated
	 */
	public void exitQueue(Vehicle v, int exitTime) throws SimulationException,
			VehicleException {
		if (!this.currentlyQueued.contains(v))
			throw new SimulationException(
					"CarPark exitQueue(): Vehicle was not found in queue");
		this.currentlyQueued.remove(v);
		v.exitQueuedState(exitTime);
	}

	/**
	 * State dump intended for use in logging the final state of the carpark All
	 * spaces and queue positions should be empty and so we dump the archive
	 * 
	 * @return String containing dump of final carpark state
	 */
	public String finalState() {
		String str = "Vehicles Processed: count: " + this.count + ", logged: "
				+ this.archived.size() + "\nVehicle Record: \n\n";
		for (Vehicle v : this.archived) {
			str += v.toString() + "\n\n";
		}
		return str + "\n";
	}

	/**
	 * Simple getter for number of cars in the car park
	 * 
	 * @return number of cars in car park, including small cars
	 */
	public int getNumCars() {
		int totalC = 0;

		for (Vehicle v : this.currentlyParked) {
			if (v instanceof Car) {
				totalC++;
			}
		}

		return totalC;
	}

	/**
	 * Simple getter for number of motorcycles in the car park
	 * 
	 * @return number of MotorCycles in car park, including those occupying a
	 *         small car space
	 */
	public int getNumMotorCycles() {
		int totalM = 0;

		for (Vehicle v : this.currentlyParked) {
			if (v instanceof MotorCycle) {
				totalM++;
			}
		}

		return totalM;
	}

	/**
	 * Simple getter for number of small cars in the car park
	 * 
	 * @return number of small cars in car park, including those not occupying a
	 *         small car space.
	 */
	public int getNumSmallCars() {
		int totalSC = 0;

		for (Vehicle v : this.currentlyParked) {
			if (v instanceof Car && ((Car) v).isSmall()) {
				totalSC++;
			}
		}

		return totalSC;
	}

	/**
	 * Method used to provide the current status of the car park. Uses private
	 * status String set whenever a transition occurs. Example follows (using
	 * high probability for car creation). At time 262, we have 276 vehicles
	 * existing, 91 in car park (P), 84 cars in car park (C), of which 14 are
	 * small (S), 7 MotorCycles in car park (M), 48 dissatisfied (D), 176
	 * archived (A), queue of size 9 (CCCCCCCCC), and on this iteration we have
	 * seen: car C go from Parked (P) to Archived (A), C go from queued (Q) to
	 * Parked (P), and small car S arrive (new N) and go straight into the car
	 * park<br>
	 * 262::276::P:91::C:84::S:14::M:7::D:48::A:176::Q:9CCCCCCCCC|C:P>A||C:Q>P||
	 * S:N>P|
	 * 
	 * @return String containing current state
	 */
	public String getStatus(int time) {
		String str = time + "::" + this.count + "::" + "P:"
				+ this.currentlyParked.size() + "::" + "C:" + this.getNumCars()
				+ "::S:" + this.getNumSmallCars() + "::M:"
				+ this.getNumMotorCycles() + "::D:" + this.numDissatisfied
				+ "::A:" + this.archived.size() + "::Q:"
				+ this.currentlyQueued.size();
		for (Vehicle v : this.currentlyQueued) {
			if (v instanceof Car) {
				if (((Car) v).isSmall()) {
					str += "S";
				} else {
					str += "C";
				}
			} else {
				str += "M";
			}
		}
		str += this.status;
		this.status = "";
		return str + "\n";
	}

	/**
	 * State dump intended for use in logging the initial state of the carpark.
	 * Mainly concerned with parameters.
	 * 
	 * @return String containing dump of initial carpark state
	 */
	public String initialState() {
		return "CarPark [maxCarSpaces: " + this.maxCarSpaces
				+ " maxSmallCarSpaces: " + this.maxSmallCarSpaces
				+ " maxMotorCycleSpaces: " + this.maxMotorCycleSpaces
				+ " maxQueueSize: " + this.maxQueueSize + "]";
	}

	/**
	 * Simple status showing number of vehicles in the queue
	 * 
	 * @return number of vehicles in the queue
	 */
	public int numVehiclesInQueue() {
		return this.currentlyQueued.size();
	}

	/**
	 * Method to add vehicle successfully to the car park store. Precondition is
	 * a test that spaces are available. Includes transition via
	 * Vehicle.enterParkedState.
	 * 
	 * @param v
	 *            Vehicle to be added
	 * @param time
	 *            int holding current simulation time
	 * @param intendedDuration
	 *            int holding intended duration of stay
	 * @throws SimulationException
	 *             if no suitable spaces are available for parking
	 * @throws VehicleException
	 *             if vehicle not in the correct state or timing constraints are
	 *             violated
	 */
	public void parkVehicle(Vehicle v, int time, int intendedDuration)
			throws SimulationException, VehicleException {
		if (!this.spacesAvailable(v))
			throw new SimulationException(
					"CarPark parkVehicle(): No suitable spaces are available for parking");
		this.currentlyParked.add(v);
		v.enterParkedState(time, intendedDuration);
	}

	/**
	 * Silently process elements in the queue, whether empty or not. If
	 * possible, add them to the car park. Includes transition via
	 * exitQueuedState where appropriate Block when we reach the first element
	 * that can't be parked.
	 * 
	 * @param time
	 *            int holding current simulation time
	 * @throws SimulationException
	 *             if no suitable spaces available when parking attempted
	 * @throws VehicleException
	 *             if state is incorrect, or timing constraints are violated
	 */
	public void processQueue(int time, Simulator sim) throws VehicleException,
			SimulationException {
		while (!this.queueEmpty()) {
			Vehicle v = this.currentlyQueued.peek();
			if (this.spacesAvailable(v)) {
				this.exitQueue(v, time);
				this.parkVehicle(v, time, sim.setDuration());
				this.status += this.setVehicleMsg(v, "Q", "P");
			} else {
				break;
			}
		}
	}

	/**
	 * Simple status showing whether queue is empty
	 * 
	 * @return true if queue empty, false otherwise
	 */
	public boolean queueEmpty() {
		return this.numVehiclesInQueue() == 0;
	}

	/**
	 * Simple status showing whether queue is full
	 * 
	 * @return true if queue full, false otherwise
	 */
	public boolean queueFull() {
		return this.numVehiclesInQueue() == this.maxQueueSize;
	}

	/**
	 * Method determines, given a vehicle of a particular type, whether there
	 * are spaces available for that type in the car park under the parking
	 * policy in the class header.
	 * 
	 * @param v
	 *            Vehicle to be stored.
	 * @return true if space available for v, false otherwise
	 */
	public boolean spacesAvailable(Vehicle v) {
		boolean isAvailable = false;

		int availableSmallCarSpaces = this.maxSmallCarSpaces
				- this.getNumSmallCars();
		int availableMotorCycleSpaces = this.maxMotorCycleSpaces
				- this.getNumMotorCycles();
		int availableBigCarSpaces = (this.maxCarSpaces - this.maxSmallCarSpaces)
				- (this.getNumCars() - this.getNumSmallCars());

		if (this.getNumMotorCycles() > this.maxMotorCycleSpaces) {
			availableSmallCarSpaces += availableMotorCycleSpaces;
		}

		if (this.getNumSmallCars() > this.maxSmallCarSpaces) {
			availableBigCarSpaces += availableSmallCarSpaces;
		}

		if (!this.carParkFull()) {
			if (v instanceof Car && !((Car) v).isSmall()) {
				if (availableBigCarSpaces > 0) {
					isAvailable = true;
				}
			} else if (v instanceof Car && ((Car) v).isSmall()) {
				if (availableSmallCarSpaces > 0
						|| (availableSmallCarSpaces <= 0 && availableBigCarSpaces > 0)) {
					isAvailable = true;
				}
			} else if (v instanceof MotorCycle) {
				if (availableMotorCycleSpaces > 0
						|| (availableMotorCycleSpaces <= 0 && availableSmallCarSpaces > 0)) {
					isAvailable = true;
				}
			}
		}
		return isAvailable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CarPark [count: " + count + " numCars: " + this.getNumCars()
				+ " numSmallCars: " + this.getNumSmallCars()
				+ " numMotorCycles: " + this.getNumMotorCycles() + " queue: "
				+ (this.currentlyQueued.size()) + " numDissatisfied: "
				+ this.numDissatisfied + " past: " + this.archived.size() + "]";
	}

	/**
	 * Method to try to create new vehicles (one trial per vehicle type per time
	 * point) and to then try to park or queue (or archive) any vehicles that
	 * are created
	 * 
	 * @param sim
	 *            Simulation object controlling vehicle creation
	 * @throws SimulationException
	 *             if no suitable spaces available when operation attempted
	 * @throws VehicleException
	 *             if vehicle creation violates constraints
	 */
	public void tryProcessNewVehicles(int time, Simulator sim)
			throws VehicleException, SimulationException {
		String vehID = "C";
		boolean isSmall = false;

		if (sim.newCarTrial()) {
			if (sim.smallCarTrial()) {
				isSmall = true;
			}

			Car c = new Car(vehID + (this.count + 1), time, isSmall);
			this.count++;

			if (this.spacesAvailable(c)) {
				this.parkVehicle(c, time, sim.setDuration());
				this.status += this.setVehicleMsg(c, "N", "P");
			} else {
				if (!this.queueFull()) {
					this.enterQueue(c);
				} else {
					this.archiveNewVehicle(c);
				}
			}
		}

		if (sim.motorCycleTrial()) {
			MotorCycle m = new MotorCycle("MC" + (this.count + 1), time);
			this.count++;

			if (this.spacesAvailable(m)) {
				this.parkVehicle(m, time, sim.setDuration());
				this.status += this.setVehicleMsg(m, "N", "P");
			} else {
				if (!this.queueFull()) {
					this.enterQueue(m);
				} else {
					this.archiveNewVehicle(m);
				}
			}
		}
	}

	/**
	 * Method to remove vehicle from the carpark. For symmetry with parkVehicle,
	 * include transition via Vehicle.exitParkedState. So vehicle should be in
	 * parked state prior to entry to this method.
	 * 
	 * @param v
	 *            Vehicle to be removed from the car park
	 * @throws VehicleException
	 *             if Vehicle is not parked, is in a queue, or violates timing
	 *             constraints
	 * @throws SimulationException
	 *             if vehicle is not in car park
	 */
	public void unparkVehicle(Vehicle v, int departureTime)
			throws VehicleException, SimulationException {
		if (!this.currentlyParked.contains(v))
			throw new SimulationException(
					"CarPark unparkVehicle(): Vehicle not found under list of parked vehicles");
		this.currentlyParked.remove(v);
		v.exitParkedState(departureTime);
	}

	/**
	 * Helper to set vehicle message for transitions
	 * 
	 * @param v
	 *            Vehicle making a transition (uses S,C,M)
	 * @param source
	 *            String holding starting state of vehicle (N,Q,P)
	 * @param target
	 *            String holding finishing state of vehicle (Q,P,A)
	 * @return String containing transition in the form:
	 *         |(S|C|M):(N|Q|P)>(Q|P|A)|
	 */
	private String setVehicleMsg(Vehicle v, String source, String target) {
		String str = "";
		if (v instanceof Car) {
			if (((Car) v).isSmall()) {
				str += "S";
			} else {
				str += "C";
			}
		} else {
			str += "M";
		}
		return "|" + str + ":" + source + ">" + target + "|";
	}
}
