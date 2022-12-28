package factory;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Seth
 */
public class FactorySimulator extends JPanel implements ActionListener{
    
    private final Timer timer;
    private final JButton addMachine;
    private final JButton addDispatcher;
    private final JButton removeMachine;
    private final JButton removeDispatcher;
    private final DrawPanel drawPanel;
    private final List<Dispatcher> dispatchers;
    private final List<Machine> machines;
    private final ConveyorBelt[] belts;
    private final JLabel label;
    private int NUM_BELTS = 5;
    private int MAX_BELT_CAP = 8;
    private JSlider maxConsumptionTime,maxProductionTime;
    
    
    public FactorySimulator()
    {   super(new BorderLayout());
        try
        {  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){}

        belts = new ConveyorBelt[NUM_BELTS];
        for(int i=0;i<belts.length;i++)
            belts[i] = new ConveyorBelt(MAX_BELT_CAP);
        
        dispatchers = new ArrayList<>();
        machines = new ArrayList<>();
        
        
        timer = new Timer(20,this);

        addMachine = new JButton("Add Machine");
        addDispatcher= new JButton("Add Dispatcher");
        removeMachine = new JButton("remove Machine");
        removeDispatcher= new JButton("remove Dispatcher");
        addMachine.addActionListener((ActionListener)this);
        addDispatcher.addActionListener((ActionListener)this);
        removeMachine.addActionListener((ActionListener)this);
        removeDispatcher.addActionListener((ActionListener)this);
        drawPanel = new DrawPanel();
        
        super.add(drawPanel,BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel();
        
        
        maxConsumptionTime = new JSlider(Machine.MIN_CONSUMPTION_TIME+1, 1000, Machine.MAX_CONSUMPTION_TIME);
        maxConsumptionTime.setBorder(BorderFactory.createTitledBorder("Max Consumption Time"));
        maxConsumptionTime.addChangeListener((ChangeEvent e) -> {
            Machine.MAX_CONSUMPTION_TIME = maxConsumptionTime.getValue();
        });
        
        maxProductionTime = new JSlider(Machine.MIN_PRODUCTION_TIME+1, 1000, Machine.MAX_PRODUCTION_TIME);
        maxProductionTime.setBorder(BorderFactory.createTitledBorder("Max Production Time"));
        maxProductionTime.addChangeListener((ChangeEvent e) -> {
            Machine.MAX_PRODUCTION_TIME = maxProductionTime.getValue();
        });
        southPanel.add(addDispatcher);
        southPanel.add(removeDispatcher);
        southPanel.add(maxConsumptionTime);
        southPanel.add(addMachine);
        southPanel.add(removeMachine);
        southPanel.add(maxProductionTime);
        southPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        super.add(southPanel,BorderLayout.SOUTH);
        
        label = new JLabel(">>> Number of Dispatchers = 0,\t Number of Machines = 0");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        super.add(label,BorderLayout.NORTH);
        
        timer.start();
    }
    
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        
        if(source == addMachine)
        {   Machine machine = new Machine(belts);
            Thread t = new Thread(machine);
            machines.add(machine);
            t.start();
        }
        else if(source == removeMachine && machines.size() > 0)
        {
            Machine m = machines.remove(machines.size()-1);
            m.requestStop();
        }
        if(source == addDispatcher)
        {   Dispatcher dispatcher = new Dispatcher(belts);
            Thread t = new Thread(dispatcher);
            dispatchers.add(dispatcher);
            t.start();
        }
        else if(source == removeDispatcher && dispatchers.size() > 0)
        {
            Dispatcher d = dispatchers.remove(dispatchers.size()-1);
            d.requestStop();
        }
        label.setText(">>> Number of Dispatchers = "+dispatchers.size()+",\t Number of Machines = "+machines.size());
        
        drawPanel.repaint();
    }
    
    private class DrawPanel extends JPanel
    {
        public DrawPanel()
        {   super();
            super.setBackground(Color.white);
            super.setPreferredSize(new Dimension(500,500));
        }
        
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            int H_OFFSET = 10;
            int W_OFFSET = 100;
            
            int beltWidth = getWidth() - W_OFFSET*2;
            int beltHeight = getHeight()/NUM_BELTS - 2*H_OFFSET;
            for(int i=0;i<belts.length;i++)
            {
                belts[i].drawBelt(g, W_OFFSET, H_OFFSET+beltHeight*i+H_OFFSET*i, beltWidth, beltHeight);
            }

        }
    }
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Factory Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new FactorySimulator());
        frame.pack();                                      //pack frame
        frame.setVisible(true);                                      //show the frame
    }
}