package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.IncompatibleTargetException;
import model.disasters.Disaster;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;
import simulation.Rescuable;
import simulation.Simulator;
import view.GameView;

public class CommandCenter implements SOSListener,ActionListener {

	private Simulator engine;
	
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private ArrayList<Unit> emergencyUnits;
	
	private GameView view; 
	private JButton [][] grid;
	
	private JPanel gridM;
    private JPanel gridU;
    private JPanel gridR;
    private JPanel gridL;
    private JPanel gridB;
    
    private JTextArea casul;
    private JTextArea info;
    private JTextArea disasterinfo;
    private JTextArea curcyc;
    
    private JScrollPane scroll;
    private JScrollPane scroll2;
    
    private JButton ambulanceButt;
    private JButton diseaseControlUnitButt;
    private JButton fireTrucjButt;
    private JButton evacuatorButt;
    private JButton gasControlUnitButt;
    private JButton respondingButt;
    private Rescuable buttarget;
    private Unit unitbut;
	

	public CommandCenter() throws Exception {
	
		engine = new Simulator(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();
		grid=new JButton[10][10];
		view=new GameView();
		respondingButt=new JButton();
		
		//middle grid
		gridM=new JPanel();
		view.addPanel(gridM);
		gridM.setLayout(new GridLayout(10,10));
		gridM.setPreferredSize(new Dimension (1000,1000));
		gridM.setBackground(Color.GRAY);
		
		//upper grid
		gridU= new JPanel();
		gridU.setBackground(Color.GRAY);
		gridU.setPreferredSize(new Dimension (100,100));
		view.add(gridU,BorderLayout.NORTH);
		
		//right grid
		gridR= new JPanel();
		gridR.setBackground(Color.GRAY);
		gridR.setPreferredSize(new Dimension (250,250));
		view.add(gridR,BorderLayout.EAST);
		
		//left grid
		gridL= new JPanel();
		gridL.setBackground(Color.GRAY);
		gridL.setPreferredSize(new Dimension (250,250));
		view.add(gridL,BorderLayout.WEST);
		
		//text areas
	    casul=new JTextArea();
	    
	    info=new JTextArea();
	    info.setEditable(false);
	    
	    curcyc=new JTextArea();
	    
	    disasterinfo=new JTextArea();
	    disasterinfo.setEditable(false);
	    
	   
	   //current cycle
	    curcyc.setText("EL CYCLE DOK-HA : "+ engine.currentCycle);
	    curcyc.setEditable(false);
	    gridU.add(curcyc , BorderLayout.EAST );
	    
		
		//next cycle button
		JButton nxtcyc=new JButton(); 
		nxtcyc.setText("NEXT CYCLE YAHBAL");
		nxtcyc.setPreferredSize(new Dimension (200,50));
		nxtcyc.addActionListener(this);
		gridU.add(nxtcyc,BorderLayout.CENTER);
		
		//number of calculate casualties
		casul.setText("3ADAD ELGOSAS :"+ engine.calculateCasualties());
		casul.setEditable(false);
		gridU.add(casul,BorderLayout.WEST);
		
	
		
		
		
		
		
		//scroll window
		scroll = new JScrollPane (disasterinfo, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(250,250));
		gridR.add(scroll, BorderLayout.NORTH);
		
		//scroll window 2
		scroll2 = new JScrollPane (info, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll2.setPreferredSize(new Dimension(250,250));
	    gridR.add(scroll2,BorderLayout.WEST);
	    
	    //the left panel
	    this.ambulanceButt = new  JButton(new ImageIcon(((new ImageIcon("amb.png")).getImage()).getScaledInstance(100, 55, java.awt.Image.SCALE_SMOOTH)));
	    this.fireTrucjButt = new  JButton(new ImageIcon(((new ImageIcon("fire.png")).getImage()).getScaledInstance(100, 55, java.awt.Image.SCALE_SMOOTH)));
	    this.evacuatorButt = new  JButton(new ImageIcon(((new ImageIcon("ev.png")).getImage()).getScaledInstance(100, 55, java.awt.Image.SCALE_SMOOTH)));
	    this.gasControlUnitButt = new  JButton(new ImageIcon(((new ImageIcon("gas.png")).getImage()).getScaledInstance(100, 55, java.awt.Image.SCALE_SMOOTH)));
	    this.diseaseControlUnitButt = new  JButton(new ImageIcon(((new ImageIcon("dis.png")).getImage()).getScaledInstance(100, 55, java.awt.Image.SCALE_SMOOTH)));
	    
	    //ambulance butt 
	    ambulanceButt.setText("Ambulance");
	    diseaseControlUnitButt.setFont(new Font("Arial", Font.PLAIN, 10));
	    ambulanceButt.setPreferredSize(new Dimension (200,100));
	    ambulanceButt.addActionListener(this);
	    gridL.add(ambulanceButt,BorderLayout.CENTER);
	  
        //responding butt
	    
	    respondingButt.setText("Respond");
	    respondingButt.setFont(new Font("Arial", Font.PLAIN, 10));
	    respondingButt.setPreferredSize(new Dimension (200,100));
	    respondingButt.addActionListener(this);
	    gridL.add(respondingButt,BorderLayout.CENTER);
	    
	    //diseaseControlUnitButt
	    diseaseControlUnitButt.setText("Disease Control");
	    diseaseControlUnitButt.setFont(new Font("Arial", Font.PLAIN, 10));
	    diseaseControlUnitButt.setPreferredSize(new Dimension (200,100));
	    diseaseControlUnitButt.addActionListener(this);
	    gridL.add(diseaseControlUnitButt,BorderLayout.CENTER);
	    
	    //fireTrucjButt
	    fireTrucjButt.setText("Fire Truck ");
	    fireTrucjButt.setPreferredSize(new Dimension (200,100));
	    fireTrucjButt.setFont(new Font("Arial", Font.PLAIN, 10));
	    fireTrucjButt.addActionListener(this);
	    gridL.add(fireTrucjButt,BorderLayout.CENTER);
	    
	    //evacuatorButt
	    evacuatorButt.setText("Evacuator ");
	    evacuatorButt.setPreferredSize(new Dimension (200,100));
	    evacuatorButt.setFont(new Font("Arial", Font.PLAIN, 10));
	    evacuatorButt.addActionListener(this);
	    gridL.add(evacuatorButt,BorderLayout.CENTER);
	    
	    //gasControlUnitButt
	    gasControlUnitButt.setText("GasControl");
	    gasControlUnitButt.setPreferredSize(new Dimension (200,100));
	    gasControlUnitButt.setFont(new Font("Arial", Font.PLAIN, 10));
	    gasControlUnitButt.addActionListener(this);
	    gridL.add(gasControlUnitButt,BorderLayout.CENTER);
	    
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				JButton b=new JButton();
				grid[i][j]=b;
				gridM.add(b);
				b.addActionListener(this);
			}
		}
		for (int i=0;i<visibleCitizens.size();i++){
			Citizen  c =visibleCitizens.get(i);
			JButton tmp=grid[c.getLocation().getX()][c.getLocation().getY()];
			tmp.setText(tmp.getText()+"Citizen position ("+c.getLocation().getX()+","+c.getLocation().getY()+")");
			tmp.addActionListener(this);
			
		}
		for (int i=0;i<visibleBuildings.size();i++){
			ResidentialBuilding r=visibleBuildings.get(i);
			JButton tmp=grid[r.getLocation().getX()][r.getLocation().getY()];
			tmp.setText(tmp.getText()+"Building position ("+r.getLocation().getX()+","+r.getLocation().getY()+")");
			tmp.addActionListener(this);
			
		}
		
		view.revalidate();

	}

	@Override
	public void receiveSOSCall(Rescuable r) {
		
		if (r instanceof ResidentialBuilding) {
			
			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);
			
		} else {
			
			if (!visibleCitizens.contains(r))
				visibleCitizens.add((Citizen) r);
		}
		//scrollable window
		if(disasterinfo.getText()  != null){
			disasterinfo.setText(disasterinfo.getText() + "\n" + r.getDisaster().toString());
		}
		else{
			disasterinfo.setText(r.getDisaster().toString());
		}

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		int px=0;
		int py=0;
		if (!engine.checkGameOver()){
		JButton b=(JButton)e.getSource();
		if(b.getText()!=null && !b.getText().equals("")){
			String t=b.getText();
			
			for(int i=0;i<grid.length;i++){
				for(int j=0;j<grid.length;j++){
				JButton jb =grid [i][j];
				if(jb.equals(b)){
					px=i;
					py=j;
				}
				
				}
						}
//			next cycle button
			if (t.charAt(0)=='N'){
				try{
				engine.nextCycle();
				if (!engine.checkGameOver()){
				casul.setText("3ADAD ELGOSAS :"+ engine.calculateCasualties());
				curcyc.setText("EL CYCLE DOK-HA : "+ engine.currentCycle);
				gridM.removeAll();
				for(int i=0;i<10;i++){
					for(int j=0;j<10;j++){
						JButton bt =new JButton();
//						b.setText("("+i+","+j+")");
						grid[i][j]=bt;
						gridM.add(bt);
						bt.addActionListener(this);
							}
						}
				for (int i=0;i<visibleCitizens.size();i++){
					Citizen  c =visibleCitizens.get(i);
					JButton tmp=grid[c.getLocation().getX()][c.getLocation().getY()];
					tmp.setText(tmp.getText()+"Citizen position ("+c.getLocation().getX()+","+c.getLocation().getY()+")");
					tmp.addActionListener(this);
							}
				for (int i=0;i<visibleBuildings.size();i++){
					ResidentialBuilding r=visibleBuildings.get(i);
					JButton tmp=grid[r.getLocation().getX()][r.getLocation().getY()];
					tmp.setText(tmp.getText()+"Building position ("+r.getLocation().getX()+","+r.getLocation().getY()+")");
					tmp.addActionListener(this);
							}
				for (int i=0;i<emergencyUnits.size();i++){
					Unit u = emergencyUnits.get(i);
					if (u.getTarget() != null){
						if(u.getTarget().getLocation().getX() == u.getLocation().getX() &&
						u.getTarget().getLocation().getY() == u.getLocation().getY()){
						JButton tmp=grid[u.getTarget().getLocation().getX()][u.getTarget().getLocation().getY()];
						if(u.getTarget() instanceof ResidentialBuilding )
						    tmp.setText("Btreat");
						else if (u.getTarget() instanceof Citizen)
							 tmp.setText("Ctreat");
									}
								}
							}	
				
				view.revalidate();				
}
				else 
					info.setText("KDA E7NA 5ALASANA");
				}
				catch (BuildingAlreadyCollapsedException d){
					info.setText("BuildingAlreadyCollapsedException");
				}
				catch(CitizenAlreadyDeadException d){
					info.setText("CitizenAlreadyDeadException");
				}	

				
			}
//			buildings button
			else if(t.charAt(0)=='B'){
	       for(int i=0;i<visibleBuildings.size();i++){
	    	   ResidentialBuilding f= visibleBuildings.get(i);
	    	   if(f.getLocation().getX()==px && f.getLocation().getY()==py){//scrollable window
	    		   info.setText(f.toString());
	    	   buttarget=f;
	    	   }
	       }
	       
			}
			else
//			citizen button
			if(t.charAt(0)=='C'){
				for(int i=0;i<visibleCitizens.size();i++){
			    	  Citizen f= visibleCitizens.get(i);
			    	   if(f.getLocation().getX()==px && f.getLocation().getY()==py){//scrollable window
			    		   info.setText(f.toString());
			    		   buttarget=f;
			    	   }
			       }
			}
//			Ambulance  button 
		else if(t.charAt(0)=='A'){
			for(int i=0;i<emergencyUnits.size();i++){
		    	  Unit f= emergencyUnits.get(i);
		    	   if(f instanceof Ambulance){//scrollable window
		    		   info.setText(f.toString());
		    	   unitbut=f;
		    	   }
		       }
			
		}
//			disease control button
		else if(t.charAt(0)=='D'){
			for(int i=0;i<emergencyUnits.size();i++){
		    	  Unit f= emergencyUnits.get(i);
		    	   if(f instanceof DiseaseControlUnit){//scrollable window
		    		   info.setText(f.toString());
		    	   unitbut=f;
		    	   }
		       }
			
		}
//		gas control button
		else if(t.charAt(0)=='G'){
			for(int i=0;i<emergencyUnits.size();i++){
		    	  Unit f= emergencyUnits.get(i);
		    	   if(f instanceof GasControlUnit){//scrollable window
		    		   info.setText(f.toString());
		    	   unitbut=f;
		    	   }
		       }
			
		}
//		firetruck button
		else if(t.charAt(0)=='F'){
			for(int i=0;i<emergencyUnits.size();i++){
		    	  Unit f= emergencyUnits.get(i);
		    	   if(f instanceof FireTruck){//scrollable window
		    		   info.setText(f.toString());
		    	   unitbut=f;
		    	   }
		       }
			
		}
//			 evacuator button
		else if(t.charAt(0)=='E'){
			for(int i=0;i<emergencyUnits.size();i++){
		    	  Unit f= emergencyUnits.get(i);
		    	   if(f instanceof Evacuator){//scrollable window
		    		   info.setText(f.toString());
		    	   unitbut=f;
		    	   }
		       }
			
		}
		
		
		else if(t.charAt(0)=='R'){
		if (unitbut != null && buttarget !=null){
			try {
				unitbut.respond(buttarget);
				info.setText("GAY YASTA");
			}
			catch (CannotTreatException e3){
				info.setText("MSH 3AREF A-TREAT YASTA");
			}
			catch (IncompatibleTargetException e4){
				info.setText("MSH 3AREF A-TREAT YASTA YAHBAL");
			}
		}
		else {
			info.setText("E5TAR 7AGA YAHBAL");
		}
		}
		}
		}
		}

	public static void main(String[]args) throws Exception{
		CommandCenter c=new CommandCenter();
	}

	
}
