package emballage.service;

import model.Carton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by cedric on 03/05/2017.
 * Algorithme actuel d'emballage des articles par colis .
 */
@Service
public class EmballageService {

    /**
     * Contenance d'un colis
     */
    @Value("${contenance.colis}")
    public Integer contenance;

    /**
     * Méthode permettant d'emballer des articles dans des colis (En fonction d'une chaine d'article)
     * @param articles chaine de numéro de 1 à 9
     * @return la chaine de caractères des articles par colis
     */
    public String robotActuel(String articles) {

        List<Carton> lstCartons = new ArrayList<Carton>();

        int numeroDuCarton = 0;
        List<Integer> lstArticles = this.listArticles(articles);
        for (Integer article : lstArticles) {
            boolean articleDansCarton = false;

            while(!articleDansCarton) {
                if (numeroDuCarton == lstCartons.size()){
                    Carton carton = new Carton();
                    carton.setArticles(article.toString());
                    carton.setPoids(article);
                    lstCartons.add(carton);
                    articleDansCarton=true;
                } else if (this.espaceLibre(lstCartons.get(numeroDuCarton),article)){
                    lstCartons.get(numeroDuCarton).setArticles(article.toString());
                    lstCartons.get(numeroDuCarton).setPoids(article);
                    articleDansCarton=true;
                } else {
                    numeroDuCarton+=1;
                    Carton carton = new Carton();
                    carton.setArticles(article.toString());
                    carton.setPoids(article);
                    lstCartons.add(carton);
                    articleDansCarton=true;
                }
            }
        }
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
            list.add(Integer.valueOf(String.valueOf(ch)));
        }
        return list;
    }





}
