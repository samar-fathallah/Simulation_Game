package model.units;

import java.io.IOException;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Injury;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;

	public Unit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;
	}

	public String toString(){
		String p = "Unit ID: " + unitID + "\n" + "Type: " + ("" +Unit.class.getClass())
				.substring(18,("" +Unit.class.getClass()).length()) 
				+ "\n" + "Addresss: (" + location.getX() + ","+ location.getY() +")"+ "\n" +"Steps per cycle: "
				+ stepsPerCycle + "\n" + "State: " + state + "\n";
		if(target != null){
			p += "Target: " + target.toString() + "\n";
		}
		return p;
	}
	
	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	@Override
	public void respond(Rescuable r) throws CannotTreatException,IncompatibleTargetException {
		if (this instanceof FireTruck){
			if (r instanceof Citizen)
				throw new IncompatibleTargetException(this,r,"The unit your trying to send for responding is incompatible");
			  else 
				   if (!(r.getDisaster() instanceof Fire)||!canTreat(r))
						   throw new CannotTreatException(this, r , "The rescuable (Citizen/Building) you're trying to respond to is already safe ");
		}
		else if (this instanceof Evacuator){
			if (r instanceof Citizen)
				throw new IncompatibleTargetException(this,r,"The unit your trying to send for responding is incompatible");
			  else 
				   if (!(r.getDisaster() instanceof Collapse)||!canTreat(r))
						   throw new CannotTreatException(this, r , "The rescuable (Citizen/Building) you're trying to respond to is already safe ");
		}
		
		
		
		else if (this instanceof GasControlUnit){
			if (r instanceof Citizen)
				throw new IncompatibleTargetException(this,r,"The unit your trying to send for responding is incompatible");
				  else 
					   if (!(r.getDisaster() instanceof GasLeak)||!canTreat(r))
							   throw new CannotTreatException(this, r , "The rescuable (Citizen/Building) you're trying to respond to is already safe ");   
							   
		}
		else if (this instanceof Ambulance){
		   if (r instanceof ResidentialBuilding )
			   throw new IncompatibleTargetException(this,r,"The unit your trying to send for responding is incompatible");
		  
	   }
		else if (this instanceof DiseaseControlUnit){
		   if (r instanceof ResidentialBuilding )
			   throw new IncompatibleTargetException(this,r,"The unit your trying to send for responding is incompatible");
		 
	   }
		
		if(!canTreat(r))
			throw new CannotTreatException(this, r , "The rescuable (Citizen/Building) you're trying to respond to is already safe ");
		
		if (target != null && state == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
		
	
	}

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
	}

	public void finishRespond(Rescuable r) {
		target = r;
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX())
				+ Math.abs(t.getY() - location.getY());

	}

	public abstract void treat();

	public void cycleStep() {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
		}
	}

	public void jobsDone() {
		target = null;
		state = UnitState.IDLE;

	}
	public boolean canTreat(Rescuable r){
		if(r instanceof Citizen){
			Citizen c = (Citizen)r;
			if(c.getState() == CitizenState.SAFE){
				return false;
			}
			else
				return true;
		}
		else{
			ResidentialBuilding h = (ResidentialBuilding) r;
			if(h.getFireDamage()==0 && h.getFoundationDamage()==0 && h.getGasLevel()==0 )
				return false;
			else
				return true;
		}
			
			
	}

	
}
