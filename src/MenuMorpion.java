import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class MenuMorpion extends JMenuBar {
    private static GrilleDuMorpion grille;
    private static final String messageAide = ""+
            "Ce programme vous permet de jouer\n"+
            "au Morpion contre, au choix,\n"+
            "un adversaire humain ou l'ordinateur.\n"+
            "\nAu lancement du programme, chaque joueur entre\n"+
            "son nom et choisit un symbole dans la liste.\n"+
            "Dans le cas d'un jeu \"un joueur\", le joueur\n"+
            "choisit le symbole de l'ordinateur.\n"+
            "\nChaque joueur joue a tour de rôle.\n"+
            "Une fois la partie terminée, le jeu vous invite a rejouer.\n"+
            "Vous continuez la partie en cours, les scores évoluants\n"+
            "après chaque partie.\n"+
            "\nLe choix d'une nouvelle partie dans le menu \"Partie\"\n"+
            "réinitialise les scores à zéro.";

    private static final String messageApd = ""+

            "\nVersion : Juin 2004\n";


    public MenuMorpion(GrilleDuMorpion grille) {
        super();
        this.grille = grille;
        JMenu fichier = new JMenu("Partie");
        fichier.setMnemonic(KeyEvent.VK_P);
        JMenuItem nouveau = new JMenuItem("Nouvelle Partie");
        nouveau.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK));
        nouveau.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int k =JOptionPane.showConfirmDialog(null,"Cette action entrainera la remise à zéro des scores.\nEtes vous sur(e) ?","Nouvelle partie",JOptionPane.YES_NO_OPTION);
                if (k == JOptionPane.OK_OPTION) MenuMorpion.grille.reinitialiser();
            }
        });
        fichier.add(nouveau);
        JMenuItem quit = new JMenuItem("Quitter");
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,InputEvent.CTRL_MASK));
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fichier.add(quit);
        this.add(fichier);
        JMenu help = new JMenu("?");
        JMenuItem aide = new JMenuItem("Aide");
        aide.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,InputEvent.CTRL_MASK));
        aide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,messageAide,"Aide",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        help.add(aide);
        JMenuItem apd = new JMenuItem("A propos de ...");
        apd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK));
        apd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,messageApd,"A propos de ...",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        help.add(apd);
        this.add(help);
    }
}
