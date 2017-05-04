package model;

/**
 * Created by cedric on 02/05/2017.
 */
public class Carton {


    String articles = new String();
    Integer poids = 0;

    public Integer getPoids() {
        return poids;
    }
    public void setPoids(Integer poids) {
        this.poids += poids;
    }

    public String getArticles() {
        return articles;
    }
    public void setArticles(String articles) {
        this.articles += articles;
    }

}
