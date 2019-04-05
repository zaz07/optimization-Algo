package aco.bigdata.fsr.ma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.IntStream;





public class Ant {

	private static int NombreVillesGlobal = AntColonyOptimization.getNbrVilleGolable();
	private static HashMap<Integer,Boolean>  villeVisitees = new HashMap<Integer, Boolean>(NombreVillesGlobal);
	private static ArrayList<City> villes = AntColonyOptimization.getgraphVilles();
	private  ArrayList<Integer> route;
	private int villeAcuelle;
	private int numDelaFoumi;

	public Ant(int numDelaFoumi,int nbrVilleGlobale) {
		
		this.numDelaFoumi =numDelaFoumi;
		route = new ArrayList<Integer>();
		initFourmiVilleVistees();
		
		
	}
	
	//#####################################################################//

	public int getNumDelaFoumi() {
		return numDelaFoumi;
	}

	protected double getdistanceDeMaRoute( Double [][] distancesMatrix) {
		
		double distance =0.0;
		
		for (int i = 0; i < this.route.size() -1; i++) {
			distance += distancesMatrix[this.route.get(i)][this.route.get(i+1)]; 
		}
		// on ajoute même la distance de retour vers la ville départ.
		distance += distancesMatrix[this.route.get(this.route.size() -1)][this.route.get(0)];
		return distance;
	}
	
	
	public void initFourmiVilleVistees() {
		if (!villeVisitees.isEmpty())
		IntStream.range(0,NombreVillesGlobal).forEach(x -> villeVisitees.put(Integer.parseInt(villes.get(x).getName()),false));
	}
	
	public int getRandomVilleDepart() {
		int depart = new Random().nextInt(NombreVillesGlobal);
		ajouterAlaRoute(depart);
		villeAcuelle = depart;
		return depart;
	}

	
	protected  ArrayList<Integer> maRoute() {
		return route;
	}

	protected  void ajouterAlaRoute(int ville) {
		villeVisitees.put(ville,true);
		route.add(ville);
		villeAcuelle = ville;
		//System.out.println("========>>>>> route ==========> "+ route.toString());
		return ;
	}
	protected int getVilleAcuelle() {		
		return villeAcuelle;
	}
}// class