package factory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Seth
 */
public class Dispatcher implements Runnable{

    public boolean stopRequested;
    public ConveyorBelt[] belts;
    
    public Dispatcher(ConveyorBelt[] belts)
    {   this.belts = belts;
        
    }
    
    
    @Override
    public void run() {
        stopRequested = false;

        int beltIndex=0;
        ConveyorBelt connectedBelt = null;
       
        while(!stopRequested)
        {
            //Find a belt that is free:
            if(!belts[beltIndex].isEmpty() && belts[beltIndex].attachDispatcher(this))
            {
               //while(!belts[beltIndex].isEmpty() && !stopRequested)
                {
                    Parcel p = belts[beltIndex].getParcel(this);
                    p.consume();
                    belts[beltIndex].retrieveParcel(this);
                }
                belts[beltIndex].detachDispatcher(this);
            }    
               
            beltIndex = (beltIndex+1)%belts.length;
            try{
                Thread.sleep(5);
            }catch(InterruptedException e){}
        }
    
    }
    public void requestStop() {
        stopRequested = true;
    }
    
}
