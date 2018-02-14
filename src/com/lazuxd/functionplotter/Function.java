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
public class Function extends JPanel implements ActionListener {
    
    private Color c = Color.BLACK;
    private FComp mainComp;
    private FunctionPlotter fp;
    private JButton colorBtn, removeBtn;
    private JLabel fTxt;
    
    public Function(FunctionPlotter fp, FComp mainC) {
    
        this.fp = fp;
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        removeBtn = new JButton(new ImageIcon("remove16.png"));
        removeBtn.setBackground(new Color(0, 0, 0, 0));
        removeBtn.setToolTipText("Dati click pentru a sterge aceasta functie.");
        colorBtn = new ColorButton(fp, this);
        colorBtn.setToolTipText("Dati click pentru a modifica culoarea.");
        fTxt = new JLabel("f" + String.valueOf(fp.getFCount() + 1) + "(x) = ");
        removeBtn.addActionListener(this);
        add(removeBtn);
        add(colorBtn);
        add(fTxt);
        setMainComp(mainC);
        add(mainC);
        setPreferredSize(new Dimension(400, 60));
        this.setToolTipText("<html>Asezati aici componente pentru a construi functii.<br>" +
                                  "Dupa ce inlocuiti toate simbolurile \'?\' functia<br>" +
                                  "se va actualiza automat in graficul alaturat.</html>");
    
    }
    
    //metoda care se foloseste la calcularea valorilor corespunzatoare pe axa OY
    public double f(FPlot fp, double x)
            throws FComp.OutOfDefinitionDomainException,
                    FComp.IncompleteFunctionException {
        return mainComp.getValueIn(fp, x);//aici fiecare componenta apeleaza metodele de calculare a valorilor pentru fiecare
                                          //subcomponenta in parte pana cand ajunge la componentele "x" si "c"(constanta)
    }
    
    public Color getColor() {
        return c;
    }
    
    public void setColor(Color c) {
        this.c = c;
    }
    
    public FComp getMainComp() {
        return mainComp;
    }
    
    public void setMainComp(FComp fc) {
        this.mainComp = fc;
    }
    
    public void draw(FPlot fp, Graphics2D g) {
    
        int x = fp.getX(), y = fp.getY(), w = fp.getWidth(), h = fp.getHeight(), ox = w/2+fp.getDX(), oy = h/2+fp.getDY();
        boolean exception = false;
        //System.out.println(mainComp.getComp1());
        g.setColor(getColor());
        for (int p = 0; p < w; p++) {
        
            try {
                if (!exception) {
                    g.drawLine(p-1, yValueToPixels(fp, f(fp, xPixelsToValue(fp, p-1, ox)), oy), p, yValueToPixels(fp, f(fp, xPixelsToValue(fp, p, ox)), oy));
                } else {
                    exception = false;
                }
            } catch (FComp.OutOfDefinitionDomainException e) {//daca functia nu este definita pe un interval/punct se ridica aceasta exceptie
                                                              //pentru a nu desena numic pe grafic
                exception = true;
            } catch (FComp.IncompleteFunctionException e) {//exceptia asta este pentru a nu desena functia pana nu s-au inlocuit toate
                                                           //simbolurile '?' din functie
                //System.out.println("incomplete");
            }
            
        }
    
    }
    
    private double xPixelsToValue(FPlot fp, int p, int ox) {
    
        return (((double)(p-ox)) / fp.getUnitPixels()) * fp.getUnit();
    
    }
    
    private int yValueToPixels(FPlot fp, double v, int oy) {
    
        return oy - (int)Math.round((v/fp.getUnit())*fp.getUnitPixels());
    
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        fp.removeFunction(this);
        fp.updateFunctionsPanel();
    }
    
    public class ColorButton extends JButton implements ActionListener {
        private Color color = Color.BLACK;
        private FunctionPlotter fp;
        private Function f;
        public ColorButton(FunctionPlotter fp, Function f) {
            this.fp = fp;
            this.f = f;
            setPreferredSize(new Dimension(36, 36));
            addActionListener(this);
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(getColor());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        public Color getColor() {
            return color;
        }
        
        public void setColor(Color c) {
            color = c;
            f.setColor(color);
            repaint();
            fp.repaint();
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            setColor(JColorChooser.showDialog(this, "Select a color", getColor()));
        }
        
    }
    
}
