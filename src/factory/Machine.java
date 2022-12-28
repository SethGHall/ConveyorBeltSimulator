package factory;


import java.awt.Color;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Seth
 */
public class Machine implements Runnable{

    public boolean stopRequested;
    public ConveyorBelt[] belts;
    public static int MIN_PRODUCTION_TIME = 100;
    public static int MAX_PRODUCTION_TIME = 200;
    public static int MIN_CONSUMPTION_TIME = 100;
    public static int MAX_CONSUMPTION_TIME = 200;
    private final Random rand;
    
    
    public Machine(ConveyorBelt[] belts)
    {   this.belts = belts;
        rand = new Random();
    }
    
    @Override
    public void run() {
        stopRequested = false;
        
        int beltIndex=0;
        
        while(!stopRequested)
        {
            //Find a machine that is free:
            if(!belts[beltIndex].isFull() && belts[beltIndex].attachMachine(this))
            {
                while(!belts[beltIndex].isFull() && !stopRequested)
                {
                    belts[beltIndex].postParcel(produceParcel(), this);
                    
                }
                belts[beltIndex].detachMachine(this);
            }    
               
            beltIndex = (beltIndex+1)%belts.length;
            try{
                Thread.sleep(5);
            }catch(InterruptedException e){}
        }
    }
    
    public Parcel<String> produceParcel()
    {   try{
            Thread.sleep(rand.nextInt(MAX_PRODUCTION_TIME-MIN_PRODUCTION_TIME)+MIN_PRODUCTION_TIME);
            
        }catch(InterruptedException e){}
        //public Parcel(E element, Color colour, int consumptionTime, int priority)
        String element = ""+ (char)('A'+rand.nextInt(26));
        int consumptionTime = rand.nextInt(MAX_CONSUMPTION_TIME-MIN_CONSUMPTION_TIME)+MIN_CONSUMPTION_TIME;
        int priority = rand.nextInt(3)+1;
        Color colour = new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat()); 
        Parcel<String> p = new Parcel<>(element,colour,consumptionTime,priority);
        return p;
    } 

    public void requestStop() {
        stopRequested = true;
    }
}
