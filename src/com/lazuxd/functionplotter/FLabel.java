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
public class FLabel extends JPanel implements MouseListener, MouseMotionListener {
    
    //de tipul clasei acesteia sunt componentele din partea de sus a ferestrei
    protected final int STATE_NORMAL = 0;
    protected final int STATE_HOVER = 1;
    protected int state;
    protected FComp.Types type;
    protected String text;
    protected FunctionPlotter fp;
    
    public FLabel(FunctionPlotter fp, FComp.Types t) {
    
        this.fp = fp;
        type = t;
        setPreferredSize(new Dimension(100, 40));
        addMouseListener(this);
        addMouseMotionListener(this);
        switch (type) {
            case X: text = "x"; break;
            case CONSTANT: text = "c"; break;
            case SIN: text = "sin(?)"; break;
            case COS: text = "cos(?)"; break;
            case TG: text = "tg(?)"; break;
            case CTG: text = "ctg(?)"; break;
            case ARCSIN: text = "arcsin(?)"; break;
            case ARCCOS: text = "arccos(?)"; break;
            case ARCTG: text = "arctg(?)"; break;
            case ARCCTG: text = "arcctg(?)"; break;
            case ADD: text = "? + ?"; break;
            case SUBTRACT: text = "? - ?"; break;
            case MULTIPLY: text = "? * ?"; break;
            case DIVIDE: text = "? / ?"; break;
            case RAISE: text = "? ^ ?"; break;
            case LOG: text = "log?(?)"; break;
            case EMPTY: text = "?"; break;
        }
        this.setToolTipText("Trageti cu mouse-ul aceasta componenta si o asezati mai jos pentru a forma functii.");
    
    }
    
    @Override
    public void paintComponent(Graphics g1) {
    
        int w = getWidth(), h = getHeight();
        int tw, th;
        FontMetrics fm;
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (state == STATE_NORMAL) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, w, h);
            //g.setColor();
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.PLAIN, 14));
            fm = g.getFontMetrics();
            tw = fm.stringWidth(text);
            th = fm.getAscent();
            g.drawString(text, (w-tw)/2, (h+th)/2);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, w, h);
            g.setColor(Color.CYAN);
            g.setStroke(new BasicStroke(3));
            g.drawRect(0, 0, w-1, h-1);
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.ITALIC, 14));
            fm = g.getFontMetrics();
            tw = fm.stringWidth(text);
            th = fm.getAscent();
            g.drawString(text, (w-tw)/2, (h+th)/2-5);
        }
    
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        
    }

    @Override
    public void mousePressed(MouseEvent me) {
        
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        //System.out.println(fp.getComponentAt(me.getPoint()));
        if (fp.getDComp() == null) return;
        if (!fp.getReadyToDrop()) {
            ((JPanel)fp.getGlassPane()).remove(fp.getDComp());
            ((JPanel)fp.getGlassPane()).repaint();
            fp.setDrag(false);
        } else {
            //System.out.println("released - else");
            fp.getCompForDrop().drop(fp.getDComp());
            ((JPanel)fp.getGlassPane()).remove(fp.getDComp());
            ((JPanel)fp.getGlassPane()).repaint();
            fp.setDrag(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        state = STATE_HOVER;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent me) {
        state = STATE_NORMAL;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!fp.getDrag()) {
            fp.startDragOperation(type, e.getXOnScreen()-fp.getX()-fp.insets.left+5, e.getYOnScreen()-fp.getY()-fp.insets.top+5);
        } else {
            fp.getDComp().setLocation(e.getXOnScreen()-fp.getX()-fp.insets.left+5, e.getYOnScreen()-fp.getY()-fp.insets.top+5);
        }
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        
    }
    
}
