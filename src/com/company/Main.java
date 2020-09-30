package com.company;

import Jeux.Morpion.Vues.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class Main {

    public static void main(String[] args) {
        /*
	try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	}
    	catch(Exception e) {
      e.printStackTrace();
    	}
	*/
        JFrame fenetre = new JFrame("Morpion");

        GrilleDuMorpion grille = new GrilleDuMorpion();
	/*
	try {
      UIManager.setLookAndFeel(new MetalLookAndFeel());
    	}
    	catch(Exception e) {
      e.printStackTrace();
    	}
	*/

        VueGrille vg = new VueGrille(grille);
        fenetre.getContentPane().add(vg,BorderLayout.CENTER);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {
            e.printStackTrace();
        }


        VuePartie vp = new VuePartie(grille);
        vp.setBorder(BorderFactory.createTitledBorder("Informations"));
        fenetre.getContentPane().add(vp,BorderLayout.EAST);

        VueNouveau vn = new VueNouveau(grille);
        fenetre.getContentPane().add(vn,BorderLayout.SOUTH);
        fenetre.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0) ;
            }
        });
        fenetre.setJMenuBar(new MenuMorpion(grille));
        fenetre.setBounds(250,150,450,380);
        fenetre.setResizable(false);
        fenetre.setVisible(true);
    }
}
