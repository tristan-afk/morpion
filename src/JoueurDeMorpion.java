package Jeux.Morpion;
import javax.swing.ImageIcon;
import Jeux.Joueur;
public class JoueurDeMorpion extends Joueur {
    /**Le symbole associé au joueur*/

    private ImageIcon symbole;

    /**Crée un nouveau joueur à partir des paramètres
     *@param nom Le nom du joueur
     *@param symbole e symbole associé au joueur
     */

    public JoueurDeMorpion(String nom,ImageIcon symbole) {
        super(nom);
        this.symbole = symbole;
    }//JoueurDeMorpion(String,ImageIcon)

    /**Renvoie le symbole associé au joueur
     *@return Le symbole associé au joueur
     */
    public ImageIcon getSymbole(){
        return this.symbole;
    }//getSymbole()

    /**Modifie le symbole associé au joueur
     *@param symbole Le nouveau symbole associé au joueur
     */
    public void setSymbole(ImageIcon symbole){
        this.symbole = symbole;
    }//setSymbole(ImageIcon)

}
