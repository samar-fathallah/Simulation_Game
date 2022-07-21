package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {

	public Ambulance(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat(){
		
		getTarget().getDisaster().setActive(false);

		Citizen target = (Citizen) getTarget();
		
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getBloodLoss() > 0) {
			target.setBloodLoss(target.getBloodLoss() - getTreatmentAmount());
			if (target.getBloodLoss() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getBloodLoss() == 0)

			heal();
		
	}

	public void respond(Rescuable r) throws CannotTreatException,IncompatibleTargetException{
		 if (r instanceof ResidentialBuilding )
			   throw new IncompatibleTargetException(this,r,"The unit your trying to send for responding is incompatible");
		 if ( r.getDisaster() instanceof Infection)
				throw new CannotTreatException(this, r , "The rescuable (Citizen/Building) you're trying to respond to is already safe ");
		if(!canTreat(r)){
			throw new CannotTreatException(this, r , "The rescuable (Citizen/Building) you're trying to respond to is already safe ");
		
	}
		if (getTarget() != null && ((Citizen) getTarget()).getBloodLoss() > 0
				&& getState() == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
		

		

}
}
