package emballage.service;

import model.Carton;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cedric on 03/05/2017.
 * Algorithme optimisé d'emballage des articles dans des colis.
 */
public class EmballageOptimiseService {

    /**
     * Contenance d'un colis
     */
    @Value("${contenance.colis}")
    public Integer contenance;

    /**
     * Méthode permettant d'optimiser le nombre de colis à utiliser (En fonction d'une chaine d'article)
     * @param articles chaine de numéro de 1 à 9
     * @return la chaine de caractères des articles par colis
     */
    public String robotOptimise(String articles) {

        // Récupération de la liste des articles dans un tableau d'Integer
        List<Integer> lstArticles = this.listArticles(articles);
        Collections.sort(lstArticles,Collections.<Integer>reverseOrder());

        // Création d'un premier carton (vide)
        List<Carton> lstCartons = new ArrayList<Carton>();
        lstCartons.add(new Carton());

        // Boucle sur la liste des articles à "emballer"
        for (Integer article : lstArticles) {
            boolean articleDansCarton = false;
            int numeroDuCarton = 0;
            // Tant que l'article en cours n'est pas dans un carton, nous cherchons à le placer
            while(!articleDansCarton) {
                // Si aucune place dans les cartons déjà créés => Création d'un nouveau carton
                if (numeroDuCarton == lstCartons.size()){
                    Carton carton = new Carton();
                    carton.setArticles(article.toString());
                    carton.setPoids(article);
                    lstCartons.add(carton);
                    articleDansCarton=true;
                    // Ajout de l'article dans le carton actuel s'il y a de la place libre.
                } else if (this.espaceLibre(lstCartons.get(numeroDuCarton),article)){
                    lstCartons.get(numeroDuCarton).setArticles(article.toString());
                    lstCartons.get(numeroDuCarton).setPoids(article);
                    articleDansCarton=true;
                } else {
                    // Si pas de place dans le carton, nous passons au carton suivant
                    numeroDuCarton+=1;
                }
            }
        }
        // retour colis
        return this.formatageColis(lstCartons);

    }

    /**
     * Méthode permettant de transformer la liste des colis en chaine de caractere séparer par de "/"
     * @param lstColis la liste des colis à tranformer
     * @return la chaîne séparée par des "/" pour représenter les articles contenus dans un carton.
     */
    public String formatageColis(List<Carton> lstColis) {
        String emballageOptimal = "";
        int premiereIteration = 0;
        for (Carton colis : lstColis) {
            if(premiereIteration==0) {
                emballageOptimal += colis.getArticles();
            }else{
                emballageOptimal += "/"+colis.getArticles();
            }
            premiereIteration++;
        }
        return emballageOptimal;
    }


    /**
     * Méthode permettant de calculer si un article peut rentrer dans un colis
     * @param colis Colis en test
     * @param article poids de l'article à insérer dans le colis
     * @return true si l'article peut rentrer dans le carton false sinon.
     */
    public boolean espaceLibre(Carton colis,Integer article) {
        if ((colis.getPoids() + article) <= contenance){
            return true;
        }
        return false;
    }

    /**
     * Méthode permettant de transformer une chaine d'article en list d'integer.
     * @param articles La chaîne d'article à transformer
     * @return la liste des articles (Au format Integer)
     */
    public List<Integer> listArticles(String articles){
        List<Integer> list = new ArrayList<Integer>();
        for(char ch : articles.toCharArray()) {
            if (isInteger(String.valueOf(ch))) {
                list.add(Integer.valueOf(String.valueOf(ch)));
            }
        }
        return list;
    }

    /**
     * Méthode permttant de tester si la chaine de caractere passée en parametre est un Integer
     * @param s La String à tester
     * @return true si la chaine passée en parametre est un Integer, false sinon
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

}
