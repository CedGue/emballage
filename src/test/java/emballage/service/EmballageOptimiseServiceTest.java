package emballage.service;

import model.Carton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class EmballageOptimiseServiceTest {

	@InjectMocks
	@Spy
	private EmballageService emballageService;

	@InjectMocks
	@Spy
	private EmballageOptimiseService emballageOptimiseService;

	@Before
	public void setUp() {
		emballageOptimiseService.contenance = 10;
		emballageService.contenance = 10;
		MockitoAnnotations.initMocks(this.getClass());
	}

	/**
	 * Test permettant de vérifier que le robot actuel fonctionne correctement.(Comme énnoncé)
	 */
	@Test
	public void robotActuelTest () {

		String chaine = "163841689525773";
		String resultActuel = this.emballageService.robotActuel(chaine);
		Assert.assertEquals("163/8/41/6/8/9/52/5/7/73",resultActuel);

	}

	/**
	 * Test qui vérifie que le Robot d'emballage otpimisé est plus performant que le robot actuel
	 */
	@Test
	public void robotActuelVsOptimiseTest () {

		List<String> lstChaineATester = this.articleFournitrure();

		for (String chaine : lstChaineATester) {

			String resultActuel = this.emballageService.robotActuel(chaine);
			String resultOptimise = this.emballageOptimiseService.robotOptimise(chaine);
			Integer nbrCartonObtenuOptimise = StringUtils.countOccurrencesOf(resultOptimise,"/") +1;
			Integer nbrCartonObtenuActuel = StringUtils.countOccurrencesOf(resultActuel,"/") +1;

			Assert.assertTrue(nbrCartonObtenuOptimise<=nbrCartonObtenuActuel);
		}

	}

	/**
	 * Méthode de test permettant de vérifier que les colis ne dépassent pas la contenance.
	 * Pour l'algorithme d'emballage optimisé
	 */
	@Test
	public void countContenanceColisRobotOptimiseTest () {

		List<String> lstChaineATester = this.articleFournitrure();
		for (String chaine : lstChaineATester) {
			// Passage des articles au robot optimisé
			String resultOptimise = this.emballageOptimiseService.robotOptimise(chaine);
			List<String> tabColis = Arrays.asList(resultOptimise.split("/"));
			for(String colis : tabColis){
				List<Integer> lstpoids = this.emballageOptimiseService.listArticles(colis);
				int poidsColis = 0;
				for (Integer poidsArticle: lstpoids) {
					poidsColis += poidsArticle;
				}
				Assert.assertTrue(poidsColis <= this.emballageOptimiseService.contenance);
			}
		}
	}

	/**
	 * Test permettant que la méthode espacelibre fonctionne correctement
	 */
	@Test
	public void espaceLibreFalseTest () {
		Carton carton = new Carton();
		carton.setPoids(8);
		Assert.assertEquals(false,this.emballageOptimiseService.espaceLibre(carton,3));
	}

	/**
	 * Test permettant que la méthode espacelibre fonctionne correctement
	 */
	@Test
	public void espaceLibreTrueTest () {
		Carton carton = new Carton();
		carton.setPoids(8);
		Assert.assertEquals(true,this.emballageOptimiseService.espaceLibre(carton,2));
	}

	/**
	 * Test permettant que la méthode de formatage des colis fonctionne correctement
	 */
	@Test
	public void formatageColisTest () {
		List<Carton> lstCartons = new ArrayList<Carton>();
		Carton carton1 = new Carton();
		carton1.setArticles("12");
		lstCartons.add(carton1);
		Carton carton2 = new Carton();
		carton2.setArticles("23");
		lstCartons.add(carton2);
		Assert.assertEquals("12/23",this.emballageOptimiseService.formatageColis(lstCartons));
	}

	/**
	 * Test permettant que la méthode de transformation de la chaine d'article en liste d'integer fonctionne correctement
	 */
	@Test
	public void listArticlesTest () {
		List<Integer> lstResult = new ArrayList<Integer>();
		lstResult = this.emballageOptimiseService.listArticles("12345");

		int i =1;
		for (Integer article : lstResult) {
			Assert.assertEquals(i,article.intValue());
			i++;
		}
	}


	/**
	 * Méthode permettant de récuperer une liste de chaines d'articles aléatoire
	 * @return une liste de chaines d'articles
	 */
	private List<String> articleFournitrure(){
		Random random = new Random();
		List<String> lstChaineATester = new ArrayList<String>();
		// Generation de 50 chaines d'articles aléatoires
		for (int i = 0 ; i<=49 ; i++) {
			String chaineArticle = "";

			// Récupération d'une chaine allant de 50 articles à 150
			int nbrArticle = random.nextInt(150 - 50 + 1) + 50;
			int value = random.nextInt(9 - 1 + 1) + 1;
			for (int j=0; j<nbrArticle;j++){
				chaineArticle += String.valueOf(random.nextInt(9 - 1 + 1) + 1);
			}

			lstChaineATester.add(chaineArticle);
		}
		return lstChaineATester;
	}

}
