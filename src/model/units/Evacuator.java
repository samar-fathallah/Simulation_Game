package model.units;

import exceptions.CannotTreatException;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class Evacuator extends PoliceUnit {

	public Evacuator(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener, int maxCapacity) {
		super(unitID, location, stepsPerCycle, worldListener, maxCapacity);

	}

	@Override
	public void treat() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		
		if (target.getStructuralIntegrity() == 0
				|| target.getOccupants().size() == 0) {
			jobsDone();
			return;
		}

		for (int i = 0; getPassengers().size() != getMaxCapacity()
				&& i < target.getOccupants().size(); i++) {
			getPassengers().add(target.getOccupants().remove(i));
			i--;
		}

		setDistanceToBase(target.getLocation().getX()
				+ target.getLocation().getY());

		}
	public String toString(){
		String s = super.toString();
		int x = this.getPassengers().size();
		s += "\n" + "Num of passengers: " + x + "\n"; 
		for (int i=0;i<getPassengers().size();i++){
			s=s+getPassengers().get(i).toString()+"\n";
		}
		return s;
	}
	
}

