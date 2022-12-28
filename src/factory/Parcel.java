package factory;


import java.awt.Color;
import java.awt.Graphics;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Seth
 * @param <E>
 */
public class Parcel<E> implements Comparable<Parcel<?>>{
    
    private final E element;
    private final Color colour;
    private int consumptionTime;
    private long timeStamp;
    private int priority;
    
    public Parcel(E element, Color colour, int consumptionTime, int priority)
    {   this.element = element;
        this.colour  = colour;
        this.consumptionTime = consumptionTime;
        this.timeStamp = System.nanoTime();
        this.priority = priority;
    }
    
    public void consume()
    {   
        try{
            Thread.sleep(consumptionTime);
        }catch(InterruptedException e){}
    }
    
    @Override
    public String toString(){
        return element.toString();
    }
    
    public void drawBox(Graphics g, int x, int y, int width, int height)
    {   g.setColor(colour);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString(element+"("+priority+")", x+5, y+20);
    }

    @Override
    public int compareTo(Parcel<?> o) {
        if(priority == o.priority)
        {
            return (int)(timeStamp - o.timeStamp);
        }
        else return priority  - o.priority;
        
    }
}
