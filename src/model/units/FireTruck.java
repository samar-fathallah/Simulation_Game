package model.units;

import exceptions.CannotTreatException;
import model.disasters.Fire;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		
		
		
		getTarget().getDisaster().setActive(false);
		
		
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} 
		else if (target.getFireDamage() > 0)
			target.setFireDamage(target.getFireDamage() - 10);

		if (target.getFireDamage() == 0)
			jobsDone();

	
}
}
