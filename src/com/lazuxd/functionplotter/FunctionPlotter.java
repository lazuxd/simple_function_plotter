/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lazuxd.functionplotter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author lazuxd
 */
public class FunctionPlotter extends JFrame implements ActionListener {

    private JPanel componentsPanel;//aici sunt componentele din partea de sus a aplicatiei
    private JPanel centerPanel;
    private JPanel leftPanel;//panoul in care se pot adauga sau sterge functii,contine si butonul "Adauga"
    private JPanel buttonsPanel;
    private JPanel functionsPanel;
    private GlassPanel glassPanel;
    private FPlot fPlot;//aici se deseneaza graficul
    private boolean readyToDrop;
    private boolean drag;
    private FComp dComp, compForDrop;
    private JButton btn1;
    private int fCount = 0;
    public Insets insets;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        FunctionPlotter window = new FunctionPlotter();
        //glassPanel = new GlassPanel();
        //window.setGlassPane();
        //FunctionPlotter fPlotPanel = new FunctionPlotter();
        //window.setContentPane(fPlotPanel);
        //System.out.println(fPlotPanel.getPreferredSize().height);
        window.pack();
        //System.out.println(fPlotPanel.getPreferredSize().height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
    }
    
    public FunctionPlotter() {
    
        super();
        this.pack();
        insets = getInsets();
        readyToDrop = false;
        glassPanel = new GlassPanel(this);
        setGlassPane(glassPanel);
        glassPanel.setVisible(true);
        //glassPanel.add(new JButton("123"));
        //System.out.println(glassPanel.isVisible());
        setLayout(new BorderLayout());
        componentsPanel = new JPanel(new GridLayout(3, 0, 5, 5));
        centerPanel = new JPanel(new BorderLayout());
        fPlot = new FPlot();
        leftPanel = new JPanel(new BorderLayout());
        buttonsPanel = new JPanel(new GridLayout(1, 2));
        functionsPanel = new JPanel(new GridLayout(0, 1));
        //functionsPanel.add(new FComp(this, FComp.Types.ARCCOS));
        addFunction(new Function(this, new FComp(this, FComp.Types.EMPTY)));
        btn1 = new JButton("Adauga");
        btn1.setToolTipText("Adaugati o noua functie.");
        btn1.addActionListener(this);
        buttonsPanel.add(btn1);
        leftPanel.add(functionsPanel, BorderLayout.CENTER);
        leftPanel.add(buttonsPanel, BorderLayout.SOUTH);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (FComp.Types t : FComp.Types.values()) {
            if (t == FComp.Types.EMPTY) continue;
            componentsPanel.add(new FLabel(this, t));
        }
        componentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //componentsPanel.setSize(componentsPanel.getPreferredSize());
        //System.out.println(componentsPanel.getWidth());
        centerPanel.add(leftPanel, BorderLayout.WEST);
        centerPanel.add(fPlot, BorderLayout.CENTER);
        add(componentsPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        this.setTitle("Reprezentarea grafica a functiilor");
        //System.out.println(getSize().height);
        
        
        /////////////////////////////////
        /*
        Function fct = new Function();
        FComp fComp = new FComp(FComp.Types.TG);
        FComp fc1 = new FComp(FComp.Types.DIVIDE), fc2 = new FComp(FComp.Types.X);
        FComp fc3 = new FComp(FComp.Types.CONSTANT);
        fc3.setConstant(Math.PI);
        fComp.setComp1(fc1);
        fc1.setComp1(fc3);
        fc1.setComp2(fc2);
        fct.setMainComp(fComp);
        fPlot.addFunction(fct);
        Function fct2 = new Function();
        FComp fComp2 = new FComp(FComp.Types.DIVIDE);
        FComp fc4 = new FComp(FComp.Types.CONSTANT), fc5 = new FComp(FComp.Types.X);
        fc4.setConstant(Math.PI);
        fComp2.setComp1(fc4);
        fComp2.setComp2(fc5);
        fct2.setMainComp(fComp2);
        fct2.setColor(Color.BLUE);
        fPlot.addFunction(fct2);
        */
        //////////////////////////////////
        
    }
    
    public void addFunction(Function f) {
        //f.setMainComp(new FComp(this, FComp.Types.EMPTY));
        fCount++;
        functionsPanel.add(f);
        fPlot.addFunction(f);
    }
    
    public void removeFunction(Function f) {
        functionsPanel.remove(f);
        fPlot.removeFunction(f);
        fCount--;
    }
    
    public int getFCount() {
        return fCount;
    }
    
    public boolean getDrag() {
        return drag;
    }
    
    public void setDrag(boolean b) {
        drag = b;
    }
    
    public boolean getReadyToDrop() {
        return readyToDrop;
    }
    
    public void setReadyToDrop(boolean b) {
        readyToDrop = b;
    }
    
    public void setCompForDrop(FComp fc) {
        compForDrop = fc;
    }
    
    public FComp getCompForDrop() {
        return compForDrop;
    }
    
    public void updateFunctionsPanel() {
        functionsPanel.updateUI();
    }
    
    public void startDragOperation(FComp.Types t, int x, int y) {
    
        dComp = new FComp(this, t);
        //dComp.setBusy(true);
        //dComp.setForeground(Color.BLACK);
        glassPanel.add(dComp);
        glassPanel.updateUI();
        //functionsPanel.updateUI();
        Dimension d = dComp.getPreferredSize();
        dComp.setBounds(x, y, d.width, d.height);
        setDrag(true);
        //System.out.println(dComp.getComponentCount());
    
    }
    
    public FComp getDComp() {
        return dComp;
    }
    
    public void setDComp(FComp fc) {
        dComp = fc;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btn1) {
            addFunction(new Function(this, new FComp(this, FComp.Types.EMPTY)));
            functionsPanel.updateUI();
        }
    }
    
    class GlassPanel extends JPanel {// folosit pentru efectul de drag&drop
        
        GlassPanel(FunctionPlotter fp) {
            super(null);
            setOpaque(false);
            //addMouseMotionListener(fp);
        }
        
        @Override
        public void paintComponent(Graphics g) {
            //System.out.println("repaint");
        }
    }
    
}
