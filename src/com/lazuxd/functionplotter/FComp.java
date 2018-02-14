/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lazuxd.functionplotter;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author lazuxd
 */
public class FComp extends JPanel implements MouseListener, MouseMotionListener {
    
    //private final int LEADING = 7;
    private FComp fc1, fc2;//subcomponentele
    private double c = 1.0;//constantea
    private boolean busy = false;
    private JLabel t1, t2, t3, tq1, tq2;//textul care se afiseaza pt functie(ex. 'sin(', 'cos(', '?')
    private int compNr;
    private FComp.Types type;//tipul
    private FunctionPlotter fp;//referinta catre fereastra principala
    private Component cp;//folosita pentru a colora/decolora portiuni ale componentei in 'mouseEntered'/'mouseExited'
    
    public FComp(FunctionPlotter fp, Types t) {
    
        super();
        this.fp = fp;
        type = t;
        //setPreferredSize(new Dimension(100, 40));
        addMouseListener(this);
        addMouseMotionListener(this);
        t1 = new JLabel(""); t2 = new JLabel(""); t3 = new JLabel("");
        tq1 = new JLabel("?"); tq2 = new JLabel("?");
        this.setBackground(Color.WHITE);
        t1.setBackground(Color.WHITE); t2.setBackground(Color.WHITE); t2.setBackground(Color.WHITE);
        tq1.setBackground(Color.WHITE); tq2.setBackground(Color.WHITE);
        tq1.setOpaque(true); tq2.setOpaque(true);
        Font f = new Font("SansSerif", Font.PLAIN, 14);
        t1.setFont(f); t2.setFont(f); t3.setFont(f); tq1.setFont(f); tq2.setFont(f);
        //System.out.println(t);
        switch (type) {
            case X: t1.setText("x"); compNr = 0; break;
            case CONSTANT: t1.setText(String.valueOf(c)); compNr = 0; break;
            case SIN: t1.setText("sin("); t2.setText(")"); compNr = 1; break;
            case COS: t1.setText("cos("); t2.setText(")"); compNr = 1; break;
            case TG: t1.setText("tg("); t2.setText(")"); compNr = 1; break;
            case CTG: t1.setText("ctg("); t2.setText(")"); compNr = 1; break;
            case ARCSIN: t1.setText("arcsin("); t2.setText(")"); compNr = 1; break;
            case ARCCOS: t1.setText("arccos("); t2.setText(")"); compNr = 1; break;
            case ARCTG: t1.setText("arctg("); t2.setText(")"); compNr = 1; break;
            case ARCCTG: t1.setText("arcctg("); t2.setText(")"); compNr = 1; break;
            case ADD: t1.setText(" + "); compNr = 2; break;
            case SUBTRACT: t1.setText(" - "); compNr = 2; break;
            case MULTIPLY: t1.setText(" * "); compNr = 2; break;
            case DIVIDE: t1.setText(" / "); compNr = 2; break;
            case RAISE: t1.setText(" ^ "); compNr = 2; break;
            case LOG: t1.setText("log"); t2.setText("("); t3.setText(")"); compNr = 2; break;
            case EMPTY:compNr = 1; break;
        }
        //System.out.println(t1.getText());
        switch (type) {
            case EMPTY:
                this.add(tq1);
                break;
            case X:
            case CONSTANT:
                this.add(t1);
                break;
            case SIN:
            case COS:
            case TG:
            case CTG:
            case ARCSIN:
            case ARCCOS:
            case ARCTG:
            case ARCCTG:
                this.add(t1);
                if (fc1 != null) {
                    this.add(fc1);
                } else {
                    this.add(tq1);
                }
                this.add(t2);
                break;
            case ADD:
            case SUBTRACT:
            case MULTIPLY:
            case DIVIDE:
            case RAISE:
                if (fc1 != null) {
                    this.add(fc1);
                } else {
                    this.add(tq1);
                }
                this.add(t1);
                if (fc2 != null) {
                    this.add(fc2);
                } else {
                    this.add(tq2);
                }
                break;
            case LOG:
                this.add(t1);
                if (fc1 != null) {
                    this.add(fc1);
                } else {
                    this.add(tq1);
                }
                this.add(t2);
                if (fc2 != null) {
                    this.add(fc2);
                } else {
                    this.add(tq2);
                }
                this.add(t3);
                break;
        }
        //System.out.println(t1.getForeground());
        
    }
    
    public void setBusy(boolean b) {
    
        busy = b;
        
    }
    
    public boolean isBusy() {
    
        return busy;
        
    }
    
    public Types getType() {
        return type;
    }
    
    public void setComp1(FComp fc) {
    
        if (type == Types.X || type == Types.CONSTANT) return;
        int i;
        
        if (fc1 != null) {
            i = this.getComponentZOrder(fc1);
        } else {
            i = this.getComponentZOrder(tq1);
        }
        
        fc1 = fc;
        //System.out.println(i);
        this.remove(i);
        this.add(fc1, i);
        this.validate();
        
    }
    
    public void setComp2(FComp fc) {
    
        switch (type) {
            case CONSTANT:
            case X:
            case SIN:
            case COS:
            case TG:
            case CTG:
            case ARCSIN:
            case ARCCOS:
            case ARCTG:
            case ARCCTG:
                return;
        }
        
        int i;
        
        if (fc2 != null) {
            i = this.getComponentZOrder(fc2);
        } else {
            i = this.getComponentZOrder(tq2);
        }
        
        fc2 = fc;
        this.remove(i);
        this.add(fc2, i);
        this.validate();
        
    }
    
    //asa cum spune numele metodei, este folosita la operatiunea de drag&drop
    public void drop(FComp fc) {
        
        if (fc.getType() == FComp.Types.CONSTANT) {
            boolean good = false;
            double val = 1.0;
            while (!good) {
                try {
                    val = Double.valueOf(JOptionPane.showInputDialog("Introduceți o constantă:", String.valueOf(val)));
                    good = true;
                } catch (NumberFormatException e) {
                    good = false;
                }
            }
            fc.setConstant(val);
        }
        
        switch (type) {
            case X:
            case CONSTANT:
                break;
            case SIN:
            case COS:
            case TG:
            case CTG:
            case ARCSIN:
            case ARCCOS:
            case ARCTG:
            case ARCCTG:
                if (fc.getLocationOnScreen().x >= this.getLocationOnScreen().x+this.getWidth()/2) {
                    this.setComp1(fc);
                }
                break;
            case ADD:
            case SUBTRACT:
            case MULTIPLY:
            case DIVIDE:
            case RAISE:
                //System.out.println(fc.getLocationOnScreen().x + ", " + this.getLocationOnScreen().x);
                if (fc.getLocationOnScreen().x >= this.getLocationOnScreen().x+this.getWidth()/2)
                    this.setComp2(fc);
                else
                    this.setComp1(fc);
                break;
            case LOG:
                if (fc.getLocationOnScreen().x >= this.getLocationOnScreen().x+this.getWidth()/3 &&
                        fc.getLocationOnScreen().x <= this.getLocationOnScreen().x+2*this.getWidth()/3)
                    this.setComp1(fc);
                else if (fc.getLocationOnScreen().x > this.getLocationOnScreen().x+2*this.getWidth()/3)
                    this.setComp2(fc);
                break;
            case EMPTY:
                this.setComp1(fc);
                //System.out.println(fp.fPlot.getFunction(0).getMainComp() == this);
                //System.out.println("in drop function ----> " + String.valueOf(this.getComp1() == null));
                break;
        }
        this.updateUI();
        //repaint();
        
    }
    
    public FComp getComp1() {
        return fc1;
    }
    
    public FComp getComp2() {
        return fc2;
    }
    
    public void setConstant(double co) {
        c = co;
        t1.setText(String.valueOf(c));
        updateUI();
    }
    
    public double getConstant() {
        return c;
    }
    
    public int getCompNr() {
        return compNr;
    }
    
    public double getValueIn(FPlot fp, double x) throws OutOfDefinitionDomainException,
                                                        IncompleteFunctionException {
        
        if (type == Types.X) return x;
        else if (type == Types.CONSTANT) return c;
        
        if ((compNr == 1 && fc1 == null) || (compNr == 2 && (fc1 == null || fc2 == null))) {
            throw new IncompleteFunctionException();
        }
        
        double a, b;
        a = fc1.getValueIn(fp, x);
        switch (type) {
            case EMPTY:
                return a;
            case ADD:
                return a + fc2.getValueIn(fp, x);
            case SUBTRACT:
                return a - fc2.getValueIn(fp, x);
            case MULTIPLY:
                return a * fc2.getValueIn(fp, x);
            case DIVIDE:
                b = fc2.getValueIn(fp, x);
                if (b == 0.0) throw new OutOfDefinitionDomainException();
                return a / b;
            case RAISE:
                if (a <= 0.0) throw new OutOfDefinitionDomainException();
                b = fc2.getValueIn(fp, x);
                return Math.pow(a, b);
            case LOG:
                b = fc2.getValueIn(fp, x);
                if (a <= 0.0 || a == 1.0 || b <= 0.0) throw new OutOfDefinitionDomainException();
                return Math.log(b) / Math.log(a);
            case SIN:
                return Math.sin(a);
            case COS:
                return Math.cos(a);
            case TG:
                {double d = fp.getUnit() / fp.getUnitPixels();
                int k = (int) Math.round(a/(Math.PI/2));
                double r = Math.abs(a - k * (Math.PI/2));
                if (k % 2 != 0 && r <= d) throw new OutOfDefinitionDomainException();
                return Math.tan(a);}
            case CTG:
                {double d = fp.getUnit() / fp.getUnitPixels();
                int k = (int) Math.round(a/Math.PI);
                double r = Math.abs(a - k * Math.PI);
                if (r <= d) throw new OutOfDefinitionDomainException();
                return 1/Math.tan(a);}
            case ARCSIN:
                if (a > 1 || a < -1) throw new OutOfDefinitionDomainException();
                return Math.asin(a);
            case ARCCOS:
                if (a > 1 || a < -1) throw new OutOfDefinitionDomainException();
                return Math.acos(a);
            case ARCTG:
                return Math.atan(a);
            default: // ARCCTG
                return Math.PI/2 - Math.atan(a);
        }
        
    }
    /*
    @Override
    public Dimension getPreferredSize() {
        //Graphics g = this.getGraphics();
        FontMetrics fm;
        Dimension d;
        int w1 = 0, w2 = 0;
        //g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        fm = this.getFontMetrics(new Font("SansSerif", Font.PLAIN, 14));
        switch (compNr) {
            case 0:
                w1 = 0;
                w2 = 0;
                break;
            case 1:
                if (fc1 != null) {
                    w1 = fc1.getPreferredSize().width;
                } else {
                    w1 = fm.stringWidth("?");
                }
                w2 = 0;
                break;
            case 2:
                if (fc1 != null) {
                    w1 = fc1.getPreferredSize().width;
                } else {
                    w1 = fm.stringWidth("?");
                }   if (fc2 != null) {
                    w2 = fc2.getPreferredSize().width;
                } else {
                    w2 = fm.stringWidth("?");
                }
                break;
        }
        d = new Dimension(fm.stringWidth(t1+t2+t3)+w1+w2+2*LEADING, fm.getAscent()+2*LEADING);
        //g.dispose();
        return d;
    }
    
    public void draw(Graphics2D g, int pos) {
        
        int l = getPreferredSize().height - LEADING;
        FontMetrics fm;
        Dimension d = getPreferredSize();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, d.width, d.height);
        //System.out.println(d.width);
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        fm = g.getFontMetrics();
        //th = fm.getAscent();
        //tw = fm.stringWidth(text);
        //g.drawString(text, (w-tw)/2, (h+th)/2);
        
        switch (compNr) {
            case 0:
                g.drawString(t1, pos, l);
                break;
            case 1:
                g.drawString(t1, pos, l);
                if (fc1 != null) {
                    fc1.draw(g, pos+fm.stringWidth(t1));
                    g.drawString(t2, pos+fm.stringWidth(t1)+fc1.getPreferredSize().width, l);
                } else {
                    g.drawString("?", pos+fm.stringWidth(t1), l);
                    
                g.drawString(t2, pos+fm.stringWidth(t1+"?"), l);
                }
                break;
            case 2:
                if (type != Types.LOG) {
                    if (fc1 != null) {
                        fc1.draw(g, pos);
                        pos += fc1.getPreferredSize().width;
                    } else {
                        g.drawString("?", pos, l);
                        pos += fm.stringWidth("?");
                    }
                    g.drawString(t1, pos, l);
                    pos += fm.stringWidth(t1);
                    if (fc2 != null) {
                        fc2.draw(g, pos);
                    } else {
                        g.drawString("?", pos, l);
                    }
                } else {
                    g.drawString(t1, pos, l);
                    pos += fm.stringWidth(t1);
                    if (fc1 != null) {
                        fc1.draw(g, pos);
                        pos += fc1.getPreferredSize().width;
                    } else {
                        g.drawString("?", pos, l);
                        pos += fm.stringWidth("?");
                    }
                    g.drawString(t2, pos, l);
                    if (fc2 != null) {
                        fc2.draw(g, pos);
                        pos += fc2.getPreferredSize().width;
                    } else {
                        g.drawString("?", pos, l);
                        pos += fm.stringWidth("?");
                    }
                    g.drawString(t3, pos, l);
                }
        }
    }
    
    @Override
    public void paintComponent(Graphics g1) {
        
        Graphics2D g = (Graphics2D) g1;
        draw(g, LEADING);
        
        /*
        switch (type) {
            case X: t1 = "x"; break;
            case CONSTANT: t1 = String.valueOf(c); break;
            case SIN: t1 = "sin("; t2 = ")"; break;
            case COS: t1 = "cos("; t2 = ")"; break;
            case TG: t1 = "tg("; t2 = ")"; break;
            case CTG: t1 = "ctg("; t2 = ")"; break;
            case ARCSIN: t1 = "arcsin("; t2 = ")"; break;
            case ARCCOS: t1 = "arccos("; t2 = ")"; break;
            case ARCTG: t1 = "arctg("; t2 = ")"; break;
            case ARCCTG: t1 = "arcctg("; t2 = ")"; break;
            case ADD: t1 = " + "; break;
            case SUBTRACT: t1 = " - "; break;
            case MULTIPLY: t1 = " * "; break;
            case DIVIDE: t1 = " / "; break;
            case RAISE: t1 = " ^ "; break;
            case LOG: t1 = "log"; t2 = "("; t3 = ")"; break;
            case EMPTY: break;
        }
        
        
    }
    */
    @Override
    public void mouseClicked(MouseEvent me) {
        
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        
    }
    
    public enum Types {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, RAISE, LOG, SIN, COS, TG, CTG, ARCSIN, ARCCOS, ARCTG, ARCCTG, X, CONSTANT, EMPTY;
    }
    
    public class OutOfDefinitionDomainException extends Exception {
        
    }
    
    public class IncompleteFunctionException extends Exception {
        
    }
    
    @Override
    public void mousePressed(MouseEvent me) {
        
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //System.out.println(me.getXOnScreen() + ", " + this.getLocationOnScreen().x);
        if (isBusy()) return;
        if (type == Types.X || type == Types.CONSTANT) return;
        fp.setCompForDrop(this);
        fp.setReadyToDrop(true);
        //System.out.println("entered --> " + String.valueOf(fp.fPlot.getFunction(0).getMainComp() == this));
        
        switch (type) {
            case X:
            case CONSTANT:
                break;
            case SIN:
            case COS:
            case TG:
            case CTG:
            case ARCSIN:
            case ARCCOS:
            case ARCTG:
            case ARCCTG:
                if (me.getXOnScreen() >= this.getLocationOnScreen().x+this.getWidth()/2) {
                    cp = this.getComponent(this.getComponentZOrder((fc1 != null)?fc1:tq1));
                }
                break;
            case ADD:
            case SUBTRACT:
            case MULTIPLY:
            case DIVIDE:
            case RAISE:
                //System.out.println(fc.getLocationOnScreen().x + ", " + this.getLocationOnScreen().x);
                if (me.getXOnScreen() >= this.getLocationOnScreen().x+this.getWidth()/2) {
                    cp = this.getComponent(this.getComponentZOrder((fc2 != null)?fc2:tq2));
                } else {
                    cp = this.getComponent(this.getComponentZOrder((fc1 != null)?fc1:tq1));
                }
                break;
            case LOG:
                if (me.getXOnScreen() >= this.getLocationOnScreen().x+this.getWidth()/3 &&
                        me.getXOnScreen() <= this.getLocationOnScreen().x+2*this.getWidth()/3) {
                    cp = this.getComponent(this.getComponentZOrder((fc1 != null)?fc1:tq1));
                } else if (me.getXOnScreen() > this.getLocationOnScreen().x+2*this.getWidth()/3) {
                    cp = this.getComponent(this.getComponentZOrder((fc2 != null)?fc2:tq2));
                }
                break;
            case EMPTY:
                cp = this.getComponent(this.getComponentZOrder((fc1 != null)?fc1:tq1));
                break;
        }
        //System.out.println(String.valueOf(cp == tq1) + tq1.getBackground());
        if (cp != null) {
            cp.setBackground(Color.CYAN);
            cp.repaint();
        }
        this.updateUI();
        //System.out.println(String.valueOf(cp == tq1) + tq1.getBackground() + tq1.isOpaque());
    }

    @Override
    public void mouseExited(MouseEvent me) {
        if (isBusy()) return;
        fp.setCompForDrop(null);
        fp.setReadyToDrop(false);
        if (cp != null) {
            cp.setBackground(Color.WHITE);
            cp.repaint();
        }
        this.updateUI();
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        
    }
    
    
}
