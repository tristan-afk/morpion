package Jeux.Morpion;

import java.util.Vector;
import java.util.Enumeration;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import Jeux.Morpion.Vues.*;
import java.io.Serializable;
import Jeux.*;
public class GrilleDuMorpion {
    private int[][] grille = new int[3][3];


    /**Le tableau des joueurs
     */
    private static final JoueurDeMorpion[] joueurs = {new JoueurDeMorpion("",null),new JoueurDeMorpion("",null)};

    /**Le code du joueur 1
     */
    private static final int j1 = 0;

    /**Le code du joueur 2
     */
    private static final int j2 = 1;

    /**Le nombre de victoires du joueur 1
     */
    private int scoreJ1;

    /**Le nombre de victoires du joueur 2
     */
    private int scoreJ2;

    /**Le nombre de matches nuls
     */
    private int nbNuls;

    /**Le nombre de coups joiués
     */
    public int nbDeCoups;

    /**Le texte affiché pour signaler quel joueur doit jouer
     */
    private static String[] aQuiLeTour;

    /**Le joueur qui doit jouer
     */
    private int aQuiDeJouer;

    /**Mis a true quand un joueur à gagné ou que la grille est remplie(match nul)
     */
    private boolean termine = false;

    //Le joueur gagnant et le joueur perdant
    private JoueurDeMorpion gagnant;
    private JoueurDeMorpion perdant;

    /**Les symboles disponibles
     */
    private ImageIcon croix, rond, carre,triangle ;


    public GrilleDuMorpion() {

        croix = new ImageIcon(getClass().getResource("images/croix.gif"));
        rond = new ImageIcon(getClass().getResource("images/rond.gif"));
        carre = new ImageIcon(getClass().getResource("images/carre.gif"));
        triangle = new ImageIcon(getClass().getResource("images/triangle.gif"));
        listeIcones = new ImageIcon[] {croix,rond,carre,triangle};

        scoreJ1 = 0;
        scoreJ2 = 0;
        nbNuls = 0;
        nbDeCoups = 0;

        DonneesJoueur param = new DonneesJoueur(listeIcones);
        param.setVisible(true);
        boolean attendre = true;
        while (attendre) {
            if (param.getOK()) {
                joueurs[0] = param.getJ1();
                joueurs[1] = param.getJ2();
                nbJoueurs = param.getNB();
                param.dispose();
                attendre = false;
            }
        }
        //Insatancie le tableau grille en le remplissant de -1
        //Il sera possible de jouer dans une case si on y trouve -1
        for (int i = 0; i < 3; i++) {
            for (int j = 0;j < 3; j++) {
                grille[i][j] = -1;
            }//for
        }//for

        aQuiLeTour = new String[] {joueurs[0].getNom(),joueurs[1].getNom()};
        aQuiDeJouer = j1;


    }//GrilleDuMorpion

    // ---- Gestion des paramètres -------------------------------------------

    /**Renvoie le joueur QuiDeJouer
     *@return Le nom du joueur aQuiDeJouer
     */
    public JoueurDeMorpion aQuiDeJouer() {
        return joueurs[aQuiDeJouer];
    }

    /**Renvoie le symbole du joueur passé en paramètre
     *@return Le symbole du joueur passé en paramètre
     */
    public ImageIcon symbole(int joueur) {
        return joueurs[joueur].getSymbole();
    }

    /**Renvoie le nom du joueur passé en paramètre
     *@return Le nom du joueur passé en paramètre
     */
    public String nomJoueur(int joueur) {
        return joueurs[joueur].getNom();
    }

    /**Renvoie le nom du joueur aQuiLeTour
     *@return Le nom du joueur aQuiLeTour
     */
    public String aQuiLeTour() {
        return aQuiLeTour[aQuiDeJouer];
    }
    /**
     *
     */
    public boolean termine() {
        return termine;
    }

    /**
     *
     */
    public JoueurDeMorpion gagnant() {
        return gagnant;
    }
    //----- Gestion des vues -------------------------------------------------
    /**Permet d'ajouter une vue
     *@param vm La vue que l'on ajoute
     */

    public void ajouterVue(VueMorpion vm) {
        listeVues.addElement(vm);
    }//ajouterVue(VueMorpion)

    /**Permet de retirer une vue
     *@param vm La vue que l'on retire
     */

    public void retirerVue(VueMorpion vm) {
        listeVues.removeElement(vm);
    }//retirerVue(VueMorpion)

    /**Méthode utilisée pour signaler aux vues
     *un changement du modele
     */
    private void signalerChangement() {
        Enumeration e = listeVues.elements();
        while(e.hasMoreElements()) {
            VueMorpion vm = (VueMorpion)(e.nextElement());
            vm.changement();
        }//while
    }//signalerChangement()

    //-- Consultation de la grille ---------------------------------------------------

    /**Renvoie le code du joueur qui a placé son symbole sur la case<BR>
     *Renvoie -1 si la case est vide
     *@param ligne La ligne de la grille
     *@param colonne La colonne de la grille
     *@return Le code du joueur qui a placé son symbole sur la case
     *@throws IndexOutOfBoundsException
     */
    public int symboleEn(int ligne,int colonne) throws IndexOutOfBoundsException {
        if (ligne > 2 && colonne > 2) throw new IndexOutOfBoundsException("Hors de la grille");
        return grille[ligne][colonne];
    }//symboleEn(int,int)

    /**Teste si l'on peut jouer sur la case
     *@param ligne La ligne de la grille
     *@param colonne La colonne de la grille
     */

    public void placerSymboleEn(int ligne, int colonne) {
        if (grille[ligne][colonne] == -1 && !termine) {
            grille[ligne][colonne] = aQuiDeJouer;
            nbDeCoups += 1;
            if(nbDeCoups == 9 && !testerAlignement(aQuiDeJouer)){
                termine = true;
                nbNuls += 1;
            }
            else if (testerAlignement(aQuiDeJouer)) {
                termine = true;
                gagnant = joueurs[aQuiDeJouer];
                if (aQuiDeJouer == 0){
                    scoreJ1+= 1;
                }
                else if (aQuiDeJouer == 1){
                    scoreJ2+= 1;
                }
            }
            aQuiDeJouer = 1 - aQuiDeJouer;
            signalerChangement();
            if (aQuiDeJouer == 1 && nbJoueurs == 1 && !termine) {
                jouerCPU();
            }
        }
    }//placerSymboleEn

    /**Teste si le joueur passer en parametre à aligner trois symboles
     *@param joueur Le code du joueur dont on teste la victoire
     */
    public boolean testerAlignement(int joueur) {

        boolean ligne = (grille[0][0]== joueur && grille[0][0] == grille[0][1] && grille[0][0] == grille[0][2]) ||
                (grille[1][0]== joueur && grille[1][0] == grille[1][1] && grille[1][0] == grille[1][2]) ||
                (grille[2][0]== joueur && grille[2][0] == grille[2][1] && grille[2][0] == grille[2][2]);

        boolean colonne =  (grille[0][0]== joueur && grille[0][0] == grille[1][0] && grille[0][0] == grille[2][0]) ||
                (grille[0][1]== joueur && grille[0][1] == grille[1][1] && grille[0][1] == grille[2][1]) ||
                (grille[0][2]== joueur && grille[0][2] == grille[1][2] && grille[0][2] == grille[2][2]);

        boolean diagonale = (grille[1][1]== joueur && grille[0][0]== grille[1][1] && grille[0][0]== grille[2][2]) ||
                (grille[1][1]== joueur && grille[0][2]== grille[1][1] && grille[1][1]== grille[2][0]);

        return (ligne || colonne || diagonale);
    }//testerAlignement(int)

    //--- Pour consulter les scores ---------------------------------------------------
    /**Renvoie le score du 1er joueur
     *@return le score du premier joueur
     */
    public int getScoreJ1() {
        return scoreJ1;
    }

    /**Renvoie le score du 2ème joueur
     *@return Le score du 2ème joueur
     */
    public int getScoreJ2() {
        return scoreJ2;
    }

    /**Renvoie le nombre de matches nuls
     *@return Le nombre de matches nuls
     */
    public int getNbNuls() {
        return nbNuls;
    }
    //----- Pour reinitialiser certains indicateurs ------------------------------

    /**Reiniialise termine a false, la grille et le nombre de coups joués
     */

    public void recommencer() {
        nbDeCoups =0;
        premierDsLeCoin = false;
        gagnant = null;
        perdant = null;
        termine = false;
        for (int i = 0; i < 3 ; i++){
            for (int j  = 0; j < 3; j++) {
                grille[i][j] = -1;
            }
        }
        signalerChangement();
        if (aQuiDeJouer == 1 && nbJoueurs == 1) {
            jouerCPU();
        }
    }

    /**Reinitilise les scores après appel de la méthode recommencer()
     */

    public void reinitialiser(){
        recommencer();
        scoreJ1 = 0;
        scoreJ2 = 0;
        nbNuls = 0;
        signalerChangement();
    }

    /**Permet de faire jouer le CPU
     */
    public void jouerCPU() {
        boolean joue = false;

        int l,c;
        joue = finirCPU();//si le cpu peut finir
        if (!joue) joue = empecherJFinir();
        if (!joue) {
            switch(nbDeCoups) {
                //-------------------------------------------------
                case 0:
                    int k = ((int)(Math.random()*10))%2;
                    if (k == 0) grille[1][1] = aQuiDeJouer;
                    else {
                        l = 2*((int)(Math.random()*10))%2;
                        c = 2*((int)(Math.random()*10))%2;
                        grille[l][c] = aQuiDeJouer;
                    }
                    joue = true;
                    break;
                //--------------------------------------------------
                case 1:
                    if(grille[0][0] == (1 -aQuiDeJouer) ||grille[0][2] == (1 -aQuiDeJouer) || grille[2][0] == (1 -aQuiDeJouer) ||grille[2][2] == (1 -aQuiDeJouer)) {
                        premierDsLeCoin = true;
                    }
                    if (grille[1][1] != -1) {
                        l = 2*((int)(Math.random()*10))%2;
                        c = 2*((int)(Math.random()*10))%2;
                        grille[l][c] = aQuiDeJouer;
                    }
                    else {
                        if (grille[0][0] != -1) grille[2][2] = aQuiDeJouer;
                        else if (grille[2][2] != -1) grille[0][0] = aQuiDeJouer;
                        else if (grille[0][1] != -1) grille[2][1] = aQuiDeJouer;
                        else if (grille[0][2] != -1) grille[2][0] = aQuiDeJouer;
                        else if (grille[1][0] != -1) grille[1][2] = aQuiDeJouer;
                        else if (grille[2][0] != -1) grille[0][2] = aQuiDeJouer;
                        else if (grille[1][2] != -1) grille[1][0] = aQuiDeJouer;
                        else if (grille[2][1] != -1) grille[0][1] = aQuiDeJouer;
                    }
                    joue = true;
                    break;
                //-------------------------------------------------------------
                case 2 :
                    if(grille[1][1] == aQuiDeJouer) {
                        if (grille [0][0] == (1- aQuiDeJouer)) grille[2][2] = aQuiDeJouer;
                        else if (grille [2][2] == (1- aQuiDeJouer)) grille[0][0] = aQuiDeJouer;
                        else if (grille [0][2] == (1- aQuiDeJouer)) grille[2][0] = aQuiDeJouer;
                        else if (grille [2][0] == (1- aQuiDeJouer)) grille[0][2] = aQuiDeJouer;
                        else if (grille [0][1] != -1 || grille [2][1] != -1 || grille [1][2] != -1 || grille [1][0] != -1) {
                            l = 2*((int)(Math.random()*10))%2;
                            c = 2*((int)(Math.random()*10))%2;
                            grille[l][c] = aQuiDeJouer;
                        }
                        else {
                            while (!joue) {
                                l = ((int)(Math.random()*10))%3;
                                c = ((int)(Math.random()*10))%3;
                                if (grille[l][c] == - 1) {
                                    grille[l][c] = aQuiDeJouer;
                                    joue = true;
                                }
                            }//while
                        }
                    }//if
                    else if (grille[1][1] == -1&& (grille[0][0] !=-1 && grille[2][2] !=-1 || grille[0][2] !=-1 && grille[2][0] !=-1)) {
                        grille[1][1]= aQuiDeJouer;
                        joue = true;
                    }//else if
                    else {
                        while (!joue) {
                            l = ((int)(Math.random()*10))%3;
                            c = ((int)(Math.random()*10))%3;
                            if (grille[l][c] == - 1) {
                                grille[l][c] = aQuiDeJouer;
                                joue = true;
                            }
                        }//while
                    }//else
                    joue = true;
                    break;
                //----------------------------------------------
                case 3 :
                    k =((int)(Math.random()*10))%2;
                    if (grille[1][1] == -1 && premierDsLeCoin) {
                        grille[1][1] = aQuiDeJouer;
                        joue = true;
                    }

                    else if (grille[1][1] == (1 - aQuiDeJouer) &&(grille[0][0] != -1 && grille[2][2] != -1 || grille[0][2] != -1 && grille[2][0] != -1)) {
                        while (!joue) {
                            l = 2*(((int)(Math.random()*10))%2);
                            c = 2*(((int)(Math.random()*10))%2);
                            if (grille[l][c] == - 1) {
                                grille[l][c] = aQuiDeJouer;
                                joue = true;
                            }
                        }//while
                    }
                    else if (grille[1][1] == (1 - aQuiDeJouer) && (grille[0][0] == (1 - aQuiDeJouer) ||grille[0][2] == (1 - aQuiDeJouer) ||grille[2][0] == (1 - aQuiDeJouer) ||grille[2][2] == (1 - aQuiDeJouer))) {

                        while (!joue) {
                            l = 2*(((int)(Math.random()*10))%2);
                            c = 2*(((int)(Math.random()*10))%2);
                            if (grille[l][c] == - 1) {
                                grille[l][c] = aQuiDeJouer;
                                joue = true;
                            }
                        }//while

                    }//else if
                    else if (grille[1][1] != -1 && (grille[0][1] != -1 && grille[2][1] != -1 || grille[1][0] != -1 && grille[1][2] != -1)) {
                        while (!joue) {
                            l = 2*(((int)(Math.random()*10))%2);
                            c = 2*(((int)(Math.random()*10))%2);
                            if (grille[l][c] == - 1) {
                                grille[l][c] = aQuiDeJouer;
                                joue = true;
                            }
                        }//while
                    }//else if


                    else if (grille[1][1] == -1 && grille[1][0] == (1 - aQuiDeJouer) && grille[0][1] == (1 - aQuiDeJouer) && grille[0][0] == -1) {
                        grille[0][0] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (grille[1][1] == -1 && grille[0][1] == (1 - aQuiDeJouer) && grille[1][2] == (1 - aQuiDeJouer) && grille[0][2] == -1) {
                        grille[0][2] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (grille[1][1] == -1 && grille[1][2] == (1 - aQuiDeJouer) && grille[2][1] == (1 - aQuiDeJouer) && grille[2][2] == -1) {
                        grille[2][2] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (grille[1][1] == -1 && grille[2][1] == (1 - aQuiDeJouer) && grille[1][0] == (1 - aQuiDeJouer) && grille[2][0] == -1) {
                        grille[2][0] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (grille[0][1] == (1-aQuiDeJouer) && grille[2][0] == (1-aQuiDeJouer)) {
                        grille[0][0] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (grille[0][1] == (1-aQuiDeJouer) && grille[2][2] == (1-aQuiDeJouer)) {
                        grille[0][2] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (grille[2][1] == (1-aQuiDeJouer) && grille[0][2] == (1-aQuiDeJouer)) {
                        grille[2][2] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (grille[2][1] == (1-aQuiDeJouer) && grille[0][0] == (1-aQuiDeJouer)) {
                        grille[2][0] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (grille[1][0] == (1-aQuiDeJouer) && grille[2][2] == (1-aQuiDeJouer)) {
                        grille[2][0] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (grille[1][0] == (1-aQuiDeJouer) && grille[0][2] == (1-aQuiDeJouer)) {
                        grille[0][0] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (grille[1][2] == (1-aQuiDeJouer) && grille[0][0] == (1-aQuiDeJouer)) {
                        grille[0][2] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (grille[1][2] == (1-aQuiDeJouer) && grille[2][0] == (1-aQuiDeJouer)) {
                        grille[2][2] = aQuiDeJouer;
                        joue = true;
                    }

                    else {
                        while (!joue) {
                            l = ((int)(Math.random()*10))%3;
                            c = ((int)(Math.random()*10))%3;
                            if (grille[l][c] == - 1) {
                                grille[l][c] = aQuiDeJouer;
                                joue = true;
                            }
                        }//while
                    }//else
                    joue = true;
                    break;
                //---------------------------------------
                case 4:
                    if (grille[1][1] == aQuiDeJouer) {
                        if (grille[0][0] == aQuiDeJouer) {
                            if (grille[0][1] == -1) {
                                grille[0][1] = aQuiDeJouer;
                                joue = true;
                            }//if
                            else if (grille[1][0] == -1) {
                                grille[1][0] = aQuiDeJouer;
                                joue = true;
                            }//else if
                        }//if 00
                        else if (grille[2][2] == aQuiDeJouer) {
                            if (grille[2][1] == -1) {
                                grille[2][1] = aQuiDeJouer;
                                joue = true;
                            }//if
                            else if (grille[1][2] == -1) {
                                grille[1][2] = aQuiDeJouer;
                                joue = true;
                            }//else if
                        }//else if22

                        else if (grille[0][2] == aQuiDeJouer) {
                            if (grille[1][2] == -1) {
                                grille[1][2] = aQuiDeJouer;
                                joue = true;
                            }//if
                            else if (grille[0][1] == -1) {
                                grille[0][1] = aQuiDeJouer;
                                joue = true;
                            }//else if
                        }//else if02

                        else if (grille[2][0] == aQuiDeJouer) {
                            if (grille[1][0] == -1) {
                                grille[1][0] = aQuiDeJouer;
                                joue = true;
                            }//if
                            else if (grille[2][1] == -1) {
                                grille[2][1] = aQuiDeJouer;
                                joue = true;
                            }//else if
                        }//else if02

                    }//if 11
                    break;
                //-----------------------------------------------
                case 5:
                    if(!joue && grille[1][1] == -1) {
                        grille[1][1] = aQuiDeJouer;
                        joue = true;
                    }

                    else if (grille[0][0] == -1) {
                        grille[0][0] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (!joue && grille[0][2] == -1)	 {
                        grille[0][2] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (!joue && grille[2][0] == -1)	 {
                        grille[2][0] = aQuiDeJouer;
                        joue = true;
                    }
                    else if (!joue && grille[2][2] == -1)	 {
                        grille[2][2] = aQuiDeJouer;
                        joue = true;
                    }

                    else if(!joue) {
                        while (!joue) {
                            l = ((int)(Math.random()*10))%3;
                            c = ((int)(Math.random()*10))%3;
                            if	 (grille[l][c] == - 1) {
                                grille[l][c] = aQuiDeJouer;
                                joue = true;
                            }
                        }

                    }
                    break;
            }//switch
        }//if

        if (!joue) {
            while (!joue) {
                l = ((int)(Math.random()*10))%3;
                c = ((int)(Math.random()*10))%3;
                if (grille[l][c] == - 1) {
                    grille[l][c] = aQuiDeJouer;
                    joue = true;
                }
            }//while
        }
        nbDeCoups += 1;
        if (testerAlignement(aQuiDeJouer)) {
            termine = true;
            gagnant = joueurs[aQuiDeJouer];
            scoreJ2++;
        }
        else if (nbDeCoups == 9) {
            termine = true;
            nbNuls++;
        }
        aQuiDeJouer = 1 - aQuiDeJouer;
        signalerChangement();

    }//jouerCPU()

    /**Permet de faire gagner le CPU si il a deja aligné deux symboles
     *@return Si le CPU a joué
     */
    public boolean finirCPU() {
        boolean joue = false;
        if (( (grille[0][1] == 1 && grille[0][2] == 1) ||
                (grille[1][0] == 1 && grille[2][0] == 1) ||
                (grille[1][1] == 1 && grille[2][2] == 1) ) && grille[0][0] == -1 && !joue) {
            grille[0][0] = 1;
            joue = true;
        }//if
        if (( (grille[0][0] == 1 && grille[0][2] == 1) ||
                (grille[1][1] == 1 && grille[2][1] == 1)) && grille[0][1] == -1 && !joue) {
            grille[0][1] = 1;
            joue = true;
        }//if
        if (( (grille[0][0] == 1 && grille[0][1] == 1) ||
                (grille[2][0] == 1 && grille[1][1] == 1) ||
                (grille[2][2] == 1 && grille[1][2] == 1) ) && grille[0][2] == -1 && !joue) {
            grille[0][2] = 1;
            joue = true;
        }//if
        if (( (grille[0][0] == 1 && grille[2][0] == 1) ||
                (grille[1][1] == 1 && grille[1][2] == 1) ) && grille[1][0] == -1 && !joue) {
            grille[1][0] = 1;
            joue = true;
        }//if
        if (( (grille[0][0] == 1 && grille[2][2] == 1) ||
                (grille[0][1] == 1 && grille[2][1] == 1) ||
                (grille[1][0] == 1 && grille[1][2] == 1) ||
                (grille[0][2] == 1 && grille[2][0] == 1) ) && grille[1][1] == -1 && !joue) {
            grille[1][1] = 1;
            joue = true;
        }//if
        if (( (grille[0][2] == 1 && grille[2][2] == 1) ||
                (grille[1][0] == 1 && grille[1][1] == 1) ) && grille[1][2] == -1 && !joue) {
            grille[1][2] = 1;
            joue = true;
        }//if
        if (( (grille[0][0] == 1 && grille[1][0] == 1) ||
                (grille[0][2] == 1 && grille[1][1] == 1) ||
                (grille[2][1] == 1 && grille[2][2] == 1) ) && grille[2][0] == -1 && !joue) {
            grille[2][0] = 1;
            joue = true;
        }//if
        if (( (grille[2][0] == 1 && grille[2][2] == 1) ||
                (grille[0][1] == 1 && grille[1][1] == 1) ) && grille[2][1] == -1 && !joue) {
            grille[2][1] = 1;
            joue = true;
        }//if
        if (( (grille[0][0] == 1 && grille[1][1] == 1) ||
                (grille[2][0] == 1 && grille[2][1] == 1) ||
                (grille[0][2] == 1 && grille[1][2] == 1) ) && grille[2][2] == -1 && !joue) {
            grille[2][2] = 1;
            joue = true;
        }//if

        return joue;
    }//finirCPU()

    /**Permet au CPU d'empecher le joueur de gagner
     */
    public boolean empecherJFinir() {
        boolean joue = false;
        if (( (grille[0][1] == 0 && grille[0][2] == 0) ||
                (grille[1][0] == 0 && grille[2][0] == 0) ||
                (grille[1][1] == 0 && grille[2][2] == 0) ) && grille[0][0] == -1 && !joue) {
            grille[0][0] = 1;
            joue = true;
        }//if
        if (( (grille[0][0] == 0 && grille[0][2] == 0) ||
                (grille[1][1] == 0 && grille[2][1] == 0)) && grille[0][1] == -1 && !joue) {
            grille[0][1] = 1;
            joue = true;
        }//if
        if (( (grille[0][0] == 0 && grille[0][1] == 0) ||
                (grille[2][0] == 0 && grille[1][1] == 0) ||
                (grille[2][2] == 0 && grille[1][2] == 0) ) && grille[0][2] == -1 && !joue) {
            grille[0][2] = 1;
            joue = true;
        }//if
        if (( (grille[0][0] == 0 && grille[2][0] == 0) ||
                (grille[1][1] == 0 && grille[1][2] == 0) ) && grille[1][0] == -1 && !joue) {
            grille[1][0] = 1;
            joue = true;
        }//if
        if (( (grille[0][0] == 0 && grille[2][2] == 0) ||
                (grille[0][1] == 0 && grille[2][1] == 0) ||
                (grille[1][0] == 0 && grille[1][2] == 0) ||
                (grille[0][2] == 0 && grille[2][0] == 0) ) && grille[1][1] == -1 && !joue) {
            grille[1][1] = 1;
            joue = true;
        }//if
        if (( (grille[0][2] == 0 && grille[2][2] == 0) ||
                (grille[1][0] == 0 && grille[1][1] == 0) ) && grille[1][2] == -1 && !joue) {
            grille[1][2] = 1;
            joue = true;
        }//if
        if (( (grille[0][0] == 0 && grille[1][0] == 0) ||
                (grille[0][2] == 0 && grille[1][1] == 0) ||
                (grille[2][1] == 0 && grille[2][2] == 0) ) && grille[2][0] == -1 && !joue) {
            grille[2][0] = 1;
            joue = true;
        }//if
        if (( (grille[2][0] == 0 && grille[2][2] == 0) ||
                (grille[0][1] == 0 && grille[1][1] == 0) ) && grille[2][1] == -1 && !joue) {
            grille[2][1] = 1;
            joue = true;
        }//if
        if (( (grille[0][0] == 0 && grille[1][1] == 0) ||
                (grille[2][0] == 0 && grille[2][1] == 0) ||
                (grille[0][2] == 0 && grille[1][2] == 0) ) && grille[2][2] == -1 && !joue) {
            grille[2][2] = 1;
            joue = true;
        }//if

        return joue;
    }//finirCPU()

}
