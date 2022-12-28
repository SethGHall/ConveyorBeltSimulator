package factory;


import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.PriorityQueue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Seth
 * @param <Box>
 */
public class ConveyorBelt{
    
    private int maxCapacity; 
    private PriorityQueue<Parcel<?>> queue;
    private Machine machine;
    private Dispatcher dispatcher;
    
    public ConveyorBelt(int maxCapacity)
    {   super();
        this.maxCapacity = maxCapacity;
        queue = new PriorityQueue<>(maxCapacity);
        machine = null;
        dispatcher = null;
    }
    
    public ConveyorBelt()
    {   this(5);
    }
    

    public synchronized Parcel<?> retrieveParcel(Dispatcher dispatcher)
    {
        if(queue.size() > 0 && this.dispatcher == dispatcher)
        {
            return queue.poll();
        }
        else return null;
    }
    public synchronized Parcel<?> getParcel(Dispatcher dispatcher)
    {
        if(queue.size() > 0 && this.dispatcher == dispatcher)
        {
            return queue.peek();
        }
        else return null;
    }
    
    public synchronized boolean postParcel(Parcel<?> p, Machine machine)
    {
        if(queue.size() < maxCapacity && this.machine == machine)
        {
            queue.offer(p);
            return true;
        }
        return false;
    }
    
    public synchronized boolean attachMachine(Machine machine)
    {
        if(this.machine == null)
        {   this.machine = machine;
            return true;
        }
        else return false;
    }
    
    public synchronized boolean attachDispatcher(Dispatcher dispatcher)
    {
        if(this.dispatcher == null)
        {   this.dispatcher = dispatcher;
            return true;
        }
        else return false;
    }
    
    public synchronized boolean detachMachine(Machine machine)
    {
        if(this.machine == machine)
        {   this.machine = null;
            return true;
        }
        else return false;
    }
    
    public synchronized boolean detachDispatcher(Dispatcher dispatcher)
    {
        if(dispatcher == this.dispatcher)
        {   this.dispatcher = null;
            return true;
        }
        else return false;
    }
    
    public synchronized int size()
    {
        return queue.size();
    }
    
    public synchronized boolean isEmpty()
    {
        return queue.isEmpty();
    }
    
    public synchronized boolean isFull()
    {
        return (queue.size() >= maxCapacity);
    }
    
    @Override
    public String toString()
    {
        return queue.toString();
    }
    
    public synchronized void drawBelt(Graphics g,int x,int y, int width, int height)
    {   
        Iterator<Parcel<?>> iterator = queue.iterator();
        int parcelWidth = width/maxCapacity;
        for(int i=0;i<maxCapacity;i++)
        {
            if(iterator.hasNext())
            {   Parcel p = iterator.next();
                p.drawBox(g, x+parcelWidth*i, y, parcelWidth, height);
            }
            g.setColor(Color.BLACK);
            g.drawRect(x+parcelWidth*i, y, parcelWidth, height);
        }
        if(machine != null)
        {
            g.setColor(Color.red);
            g.fillOval(x+width, y, parcelWidth, height);
        }
        if(dispatcher != null)
        {
            g.setColor(Color.blue);
            g.fillOval(x-parcelWidth, y, parcelWidth, height);
        }
    }
}
