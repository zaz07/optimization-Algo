package aco.bigdata.fsr.ma;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;


import io.aco.bigdata.fsr.ma.Aco_io;

public class AntColonyOptimization {
	
    private static double ALPHA; // alpha contr�le l�importance des ph�romones
    private static double BETA;  // b�ta contr�le la priorit� de distance
    private static double evaporation ; // indique le pourcentage d'�vaporation de la ph�romone � chaque it�ration
    private static double Q ;  //  Q fournit des informations sur la quantit� totale de ph�romone laiss�e sur le chemain par chaque fourmi.
  
    private Random random = new Random();
  
    /* ##############################################*/
  
    private static ArrayList<City> graphVilles;
    private int nbrVilles;
    private static int nbrVilleGlobale;
    private  Double [][] phermoneLevelsMatrix = null;
	private  Double [][] distancesMatrix = null;
	private int nbreFourmis = 1;
	private double probabilityMatrix [];
	private List<Ant> fourmiList = new ArrayList<>();
	
	private int nbreIteration ; // maxIteration
	private ArrayList<Integer> meilleurRoute;// = new ArrayList<Integer>();
    private double meilleurRouteDistance;
	 
    /* ##############################################*/
	 /***
  	   * Au d�but, nous devons initialiser de l'objet ACO en fournissant des matrices de pheromones, distance, probabilit�es et de fourmis:
  	   */
    public AntColonyOptimization(String fichier,int nbreFourmis,int nbreIteration,ParamAco paramAco) {
    	 
    		ALPHA = paramAco.ALPHA;
    	    BETA = paramAco.BETA; 
    	    evaporation =  paramAco.evaporation; 
    	    Q = paramAco.Q;  //
    	
    	
    	graphVilles = Aco_io.chargerGraph(fichier);
    	this.nbrVilles = graphVilles.size();
    	nbrVilleGlobale = graphVilles.size();
    	this.nbreFourmis = nbreFourmis;	
    	this.distancesMatrix = Aco_io.initializeDistances(graphVilles, nbrVilles);
    	this.probabilityMatrix = new double[nbrVilleGlobale];
    	this.nbreIteration = nbreIteration;
    	
    	/* G�neration de groupe de fourmis */
    	  IntStream.range(0, nbreFourmis)
          .forEach(i -> fourmiList.add(new Ant(i,nbrVilleGlobale)));
    	
    	  /* 
 		 System.out.println(" #################### distance ##########################");
 		 for(int i=0;i< distancesMatrix.length; i++) {
 			 //System.out.println("");
 			 for(int k=0;k< distancesMatrix.length; k++) {
 				 System.out.println( distancesMatrix[i][k] + " ");
 			 }
 		 }
 		 */
    }

	
	  
    public static int  getNbrVilleGolable() {
    	return nbrVilleGlobale;
    }
    public static  ArrayList<City>  getgraphVilles() {
    	 ArrayList<City> villes = null;
    	 if(!graphVilles.isEmpty())
    		 villes = graphVilles;
    	 
    	return villes;
    }
	
	  
    /**
     * Lancement de la recherche de solution  ant optimization 
     */
    public void lancerACO() {
      
    	   resoudre();
    } 
    
    /**
     * configuration de la matrice de fourmis pour commencer avec une ville al�atoire:
     */
   
	   
    private void configurerFourmis() {
    	  // System.out.println("configurerFourmis " );
    	   
           for(int i =0;i< fourmiList.size();i++) {
            		Ant f = fourmiList.get(i);
                    f.initFourmiVilleVistees();
                    f.getRandomVilleDepart();
                   // System.out.println("ant N� =" + i + " ville depart " + f.getVilleAcuelle());
           }
    }
    
    /**
     *choisir la prochaine ville pour toutes les fourmis,
     * chaque fourmi tente de suivre les traces des autres fourmis;
     */
    private  void deplacerFroumis() {
       
     for(int i=0;i<nbrVilleGlobale;i++) {
    	 for(int k=0;k<nbreFourmis;k++) { 
            		//System.out.println("");
            		//System.out.println("Fourmi N� "+ f.getNumDelaFoumi());
    		 Ant f = fourmiList.get(k);
            		int vpro = choisirVilleProchaine(f);  
            		if ((vpro==-1) || (f.maRoute().size() != nbrVilles )) {
            		f.ajouterAlaRoute(vpro);
            		//System.out.println( " route Fourmi: "+ f.getNumDelaFoumi()+ " >>> " + f.maRoute());
            		//System.out.println( "Fourmi N� " + f.getNumDelaFoumi() +">>>>>>>>>>>   ville Acutelle "+ f.getVilleAcuelle());
            		//System.out.println("");
            		}
            		
            	}//forEach fourmi
    }
    }
   
    /**
     *choisir une ville de destination pour toutes les fourmis,
     *en �vitant qu'aucune fourmi ne revisite pas une ville
     *d�ja visit�e.
     */
    private int choisirVilleProchaine(Ant ant) {
    	//TODO
    	//System.out.println("choisirVilleProchaine");
    	// Pour donner la chance � une fourmi de choisir al�atoirement une ville prochaine � visiter .
    	   	
    	
        // Si non la fourmi va choisir une ville prochaine selon la proba. calcul�e.
    	calculerProbabiliteVilleProchaine(ant);
        double r = random.nextDouble();
        double total = 0;
        int i=-1;
        for ( i = 0; i < nbrVilleGlobale -1; i++) {
            total += probabilityMatrix[i];
            if (total >= r ) {
            	//System.out.println(">>>>>>>>>>>  prochaine ville "+ i);     	
            	if(ant.maRoute().contains(i)) continue;
                return i;
            }
        }
        	return i;
       
    }
    
    /** S�lectionner la ville suivante
     *  Calculer les probabilit� de d�placement vers chaque ville du tableau pour, 
     *  Fourmis preferent suivre des pistes courtes et dont le niveau de pheromone est fort. 
     */
    public void calculerProbabiliteVilleProchaine(Ant ant) {
     
    // System.out.println("calculerProbabiliteVilleProchaine ");
        int villeActu = ant.getVilleAcuelle();
        ArrayList<Integer>  maroute = ant.maRoute();
        double pheromone = 0.0;
        if(!maroute.isEmpty()) {
        	// System.out.println("calculerProbabiliteVilleProchaine Route " + maroute.size() );
        for (int dest = 0; dest < nbrVilleGlobale; dest++) {
            if (!maroute.contains(dest)) {
            //	System.out.println("phermoneLevelsMatrix["+ villeActu +"][ " + dest+"] >>> " + phermoneLevelsMatrix[villeActu][ dest]);
            	pheromone +=Math.pow(phermoneLevelsMatrix[villeActu][ dest],ALPHA) * Math.pow(1.0/distancesMatrix[villeActu][ dest],BETA);
           //	 System.out.println("calculerProbabiliteVilleProchaine pheromone " + pheromone );
            }
        }//for
        
        for (int dest = 0; dest < nbrVilleGlobale; dest++) {
        	 if (maroute.contains(dest)) {
                probabilityMatrix [dest] = 0.0;
            } else {
                double numerator = Math.pow(phermoneLevelsMatrix[villeActu][ dest],ALPHA) * Math.pow(1.0 / distancesMatrix[villeActu][ dest],BETA);
              //  System.out.println("calculerProbabiliteVilleProchaine numerator " + numerator );
                probabilityMatrix [dest] = numerator / pheromone;
                
                //System.out.println("calculerProbabiliteVilleProchaine   probabilityMatrix [" + dest +"] " +   probabilityMatrix [dest] );
            }
        }
        }else {
        	System.out.println("aucune ville a �t� visit�e !!!!");
        }
        
    }
    
   /**
    * Mise � jour de pheromones de pistes par les foumis g�n�ree.
    * 1) on modifie le niveau de pheromone par l'�vaporation.
    * 2) chaque fourmi a sa contribution dans la mise � jour.
    * 3) on ajoute m�me la contirbution de pheromone de la fourmie en retournant � la ville d�part.
    */
    private void miseAjourPheromones() {  
    	
    	// on modifie le niveau de pheromone par l'�vaporation.
    	for (int i = 0; i < nbrVilleGlobale ; i++) {
    		for (int j = 0; j < nbrVilleGlobale; j++) {	
    				phermoneLevelsMatrix[i][j] *= evaporation;
    	 											}
    											}        
    	for(Ant f: fourmiList) {
        	double contribution = Q / f.getdistanceDeMaRoute(distancesMatrix);
        	ArrayList<Integer> maRoute = f.maRoute();
        	
        	//System.out.println("contribution ==> " + contribution);
        	  for (int i = 0; i < nbrVilleGlobale -1; i++) {
        		
        		  phermoneLevelsMatrix[maRoute.get(i)][maRoute.get(i+1)]+=contribution;
        		 // System.out.println(" phermoneLevelsMatrix["+maRoute.get(i)+"]["+maRoute.get(i+1)+"] >> " +  phermoneLevelsMatrix[maRoute.get(i)][maRoute.get(i+1)]);
        	  }
        	// on ajoute m�me la contirbution de pheromone de la fourmie en retournant � la ville d�part.
        	  phermoneLevelsMatrix[maRoute.get(nbrVilleGlobale -1)][maRoute.get(0)]+=contribution;
        }
        
      
    }//
    
   /**
    * Selection  la meilleur distance => distance minimale.
    */
    private void miseAjourMeilleurRoute() {
      
    	// initialisation de la meilleur chemain par le chemain de la premire fourmi
        if(meilleurRoute ==null) {
        	meilleurRoute = fourmiList.get(0).maRoute();
        	meilleurRouteDistance = fourmiList.get(0).getdistanceDeMaRoute(distancesMatrix);
        	  
        	
        }     
        // Selectionner le meilleur chemain en comparant les distances parcourues par toutes les fourmis
        // finalement nous gardons la route ayant la distance minimale.
        for(Ant f: fourmiList) {
        	/**
        	 	System.out.println("");
        		System.out.println( "Fourmis N� "+ f.getNumDelaFoumi() +"   Villes parcourue >> " + f.maRoute());
        		System.out.println( "Fourmis N� "+ f.getNumDelaFoumi() +"   Distance parcourue >> " + f.getdistanceDeMaRoute(distancesMatrix));
        	 	System.out.println("");
        	 */
        	
        	if(f.getdistanceDeMaRoute(distancesMatrix) < meilleurRouteDistance) {
        		meilleurRoute = f.maRoute();
        		meilleurRouteDistance = f.getdistanceDeMaRoute(distancesMatrix);
        	}
        }
        
       
    }//miseAjourMeilleurRoute
    
    /**
     * Pour chaque it�ration, nous allons 
     * appleler les m�thodes 
     *   deplacerFroumis();  
         
       * miseAjourPheromones();
             	   
        * miseAjourMeilleurRoute();
     */

    public void resoudre() {
    	
    	// ########################################//
    	configurerFourmis();
        initialiserPheromones();
        
       IntStream.range(0, nbreIteration)
        .forEach(i -> {
          
        	  deplacerFroumis();  
        	 // System.out.println("deplacement N� "+ i);
        	  miseAjourPheromones();
        	 // System.out.println("miseAjourPheromones N� "+ i);	
        	
        	 miseAjourMeilleurRoute();
        	  
        	  //System.out.println("miseAjourMeilleurRoute N� "+ i);	
        });
       System.out.println("Meilleur tour : " + (meilleurRoute.toString()));
        System.out.println("meilleur distance: " +  meilleurRouteDistance);
  
        return;
    }
    

private void initialiserPheromones() {
	
	this.phermoneLevelsMatrix= Aco_io.initialisePhermoneLevels(graphVilles, nbrVilles);
	//System.out.println("pheromone init");
}
    /* ######################  ########################*/

   
}