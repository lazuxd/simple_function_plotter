/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lazuxd.functionplotter;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author lazuxd
 */
public class FPlot extends JPanel implements ActionListener, ComponentListener, MouseMotionListener, MouseListener {
    
    private final ArrayList<Function> functions;// aici sunt stocate functiile pentru a fi desenate
    private double unit;//valoarea pe axa numerelor corespunzatoare a 100 de pixeli de pe ecran
    private int unitPixels = 100; // cei 100 de pixeli specificati mai sus
    private JButton zIn, zOut;
    private JPanel z;
    private boolean drag; // marcheaza momentul cand utilizatorul "trage" de grafic
    private int dX, dY; //distantele pe cele doua axe care decaleaza graficul de la origine, utilizate pentru "drag"
    private int curX, curY;// utilizate in metoda "mouseDragged" pentru a calcula dX si dY
    
    public FPlot() {
    
        super(null);
        drag = false;
        unit = 1.0;
        dX = 0; dY = 0;
        functions = new ArrayList<>();
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setPreferredSize(new Dimension(720, 720));
        //setSize(640, 640);
        z = new JPanel(new GridLayout(2,1, 3, 3));
        zIn = new JButton("+");
        zOut = new JButton("-");
        zIn.setToolTipText("Dati click pentru a mari.");
        zOut.setToolTipText("Dati click pentru a micsora.");
        zIn.addActionListener(this);
        zOut.addActionListener(this);
        z.add(zIn);
        z.add(zOut);
        add(z);
        z.setSize(z.getPreferredSize());
        z.setLocation(getWidth() - z.getWidth() - 20, 20);
        addComponentListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setToolTipText("<html><p>Aici se vor reprezenta functiile.</p>" +
                             "<p>Trageti cu mouse-ul de acest grafic pentru a-l misca.</p></html>");
    
    }
    
    public int getDX() {
        return dX;
    }
    
    public int getDY() {
        return dY;
    }
    
    public Function getFunction(int i) {
        return functions.get(i);
    }
    
    @Override
    public void paintComponent(Graphics g1) {
    
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int x = 0, y = 0, w = getWidth(), h = getHeight(), ox = w/2+dX, oy = h/2+dY;
        BasicStroke s1 = new BasicStroke(1), s2 = new BasicStroke(2);
        Color c = new Color(192, 192, 192, 100);
        FontMetrics fm;
        int fh, fw;
        String s;
        
        g.setColor(getBackground());
        g.fillRect(x, y, w, h);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, w-1, h-1);
        
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        fm = g.getFontMetrics();
        fh = fm.getHeight();
        for (int i = ox + unitPixels; i <= w; i += unitPixels) {
            g.setColor(c); g.setStroke(s1);
            g.drawLine(i, 0, i, h);
            g.setColor(Color.BLACK); g.setStroke(s2);
            g.drawLine(i, oy-10, i, oy+10);
            s = String.valueOf(unit*((i-ox)/unitPixels));
            fw = fm.stringWidth(s);
            g.drawString(s, i-fw/2, oy+15+fh);
        }
        for (int i = ox - unitPixels; i >= 0; i -= unitPixels) {
            g.setColor(c); g.setStroke(s1);
            g.drawLine(i, 0, i, h);
            g.setColor(Color.BLACK); g.setStroke(s2);
            g.drawLine(i, oy-10, i, oy+10);
            s = String.valueOf(unit*((i-ox)/unitPixels));
            fw = fm.stringWidth(s);
            g.drawString(s, i-fw/2, oy+15+fh);
        }
        for (int i = oy + unitPixels; i <= h; i += unitPixels) {
            g.setColor(c); g.setStroke(s1);
            g.drawLine(0, i, w, i);
            g.setColor(Color.BLACK); g.setStroke(s2);
            g.drawLine(ox-10, i, ox+10, i);
            s = String.valueOf(unit*((oy-i)/unitPixels));
            g.drawString(s, ox+15, i+fh/2);
        }
        for (int i = oy - unitPixels; i >= 0; i -= unitPixels) {
            g.setColor(c); g.setStroke(s1);
            g.drawLine(0, i, w, i);
            g.setColor(Color.BLACK); g.setStroke(s2);
            g.drawLine(ox-10, i, ox+10, i);
            s = String.valueOf(unit*((oy-i)/unitPixels));
            g.drawString(s, ox+15, i+fh/2);
        }
        
        g.setColor(Color.BLACK);
        g.setStroke(s2);
        g.drawLine(0, oy, w, oy);
        g.drawLine(ox, 0, ox, h);
        
        g.setStroke(s1);
        for (Function func : functions) {
            //se apeleaza metoda de desenare a fiecarei functii in parte
            func.draw(this, g);
        
        }
    
    }
    
    public void drawPoint(Graphics2D g, int x, int y, Color c) {
    
        g.setColor(c);
        g.drawLine(x, y, x, y);
    
    }
    
    public void addFunction(Function func) {
    
        functions.add(func);
        //repaint();
        //getToolkit().sync();
        
    }
    
    public void removeFunction(Function f) {
    
        functions.remove(f);
        repaint();
        getToolkit().sync();
    
    }
    
    public void zoomIn() {
    
        unit = Double.valueOf(String.format("%.2f", unit - unit/4.0));
        repaint();
        getToolkit().sync();
    
    }
    
    public void zoomOut() {
    
        unit = Double.valueOf(String.format("%.2f", unit + unit/4.0));
        repaint();
        getToolkit().sync();
    
    }
    
    public int getUnitPixels() {
        return unitPixels;
    }
    
    public void setUnitPixels(int px) {
        unitPixels = px;
        repaint();
        getToolkit().sync();
    }
    
    public double getUnit() {
        return unit;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == zIn) {
            zoomIn();
        } else {
            zoomOut();
        }
    }

    @Override
    public void componentResized(ComponentEvent ce) {
        z.setLocation(getWidth() - z.getWidth() - 20, 20);
    }

    @Override
    public void componentMoved(ComponentEvent ce) {
        
    }

    @Override
    public void componentShown(ComponentEvent ce) {
        
    }

    @Override
    public void componentHidden(ComponentEvent ce) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int prevX = curX, prevY = curY;
        curX = e.getX(); curY = e.getY();
        dX += curX-prevX; dY += curY - prevY;
        repaint();
        getToolkit().sync();
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (drag) return;
        curX = e.getX();
        curY = e.getY();
        drag = true;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        drag = false;
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
    
}
