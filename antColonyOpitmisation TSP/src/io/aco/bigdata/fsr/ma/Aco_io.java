package io.aco.bigdata.fsr.ma;


import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

import aco.bigdata.fsr.ma.*;


public class Aco_io {

	//#####################################################################
	
		public  static Double [][] initializeDistances(ArrayList<City> cities,int nbreVilles)  {
			Double [][] distancesMatrix = new Double [nbreVilles][nbreVilles];
					IntStream.range(0,nbreVilles).forEach(x->{
						City ville = cities.get(x);
						IntStream.range(0,nbreVilles).forEach(y -> distancesMatrix[x][y] = ville.distanceAlaVille(cities.get(y)));
					});
		return distancesMatrix;
		}
		
		public  static Double[][] initialisePhermoneLevels(ArrayList<City> cities,int nbreVilles) {
			Double[][] phermoneLevelsMatrix = new Double [nbreVilles][nbreVilles];
			Random random = new Random();
			IntStream.range(0, nbreVilles).forEach(x->{
				IntStream.range(0,nbreVilles).forEach(y -> phermoneLevelsMatrix[x][y]= new Double(random.nextDouble()));
			});
			return phermoneLevelsMatrix;
		}

		
		
		//#####################################################################
	
	
	/**
	 * renvoi la liste des villes
	 * @param fichier
	 * @return
	 */
	public static ArrayList<City> chargerGraph(String fichier) {
		
		ArrayList<City> villes = new ArrayList<City>();
		
		//###############################################################################
		 //berlin52.txt
		 //burma14.txt
		 //a280.txt
		 // fichier ="burma14";
		 
		 Path file = Paths.get(System.getProperty("user.dir") + "/bin/tsp_files/"+fichier+".txt");
		 villes = read_tsp_data("", file.toString());
		int  nbrDeVille= villes.size();
		 System.out.println("Fichier: "+ file);
		 System.out.println("Fichier: "+ fichier +" 	\n 	Nombre de villes: " + nbrDeVille  );
	
		 System.out.println(" ###### Chargement est fait ######### ");
		 return villes;
	
	}

	
	/**
	 *  charger le fichier tsp
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	public static ArrayList<City>  read_tsp_data(String fileName, String filePath){
		
	
		
		ArrayList<City>  graphCities = new ArrayList<City>();
		  String val ="";
		  double x = 0.00;
		  double y = 0.00;
		 
		 try {
			 BufferedReader in = new BufferedReader(new FileReader(filePath));
		      String line;		       
		      while ((line = in.readLine())!=null) {
		    	  if( (!line.equalsIgnoreCase("NAME: "+ fileName))) {
		    		  if (!line.equalsIgnoreCase("NODE_COORD_SECTION")) {
		    			  	if (!line.equalsIgnoreCase("EOF") && !line.equalsIgnoreCase("")) {   	  
		    			  		StringTokenizer token = new StringTokenizer(line," ");
		    		  
		    		  try {
		    			  
		    			   val =token.nextToken();
		    			  String val1 =token.nextToken();
		    			 // System.out.print(" val1-" + val1);
		    			  String val2 =token.nextToken();
		    			 //System.out.println(" va2-" + val2);
		    			   x = Double.valueOf(val1).doubleValue();
		    			   y = Double.valueOf(val2).doubleValue();
		    			   
		    			   val=String.valueOf(Integer.parseInt(val)-1);
		    			   City ville = new City (val,x,y);	  
		 				  graphCities.add(ville);
		    			
		    		  }catch (java.util.NoSuchElementException ex) {
		    			
		    			  
		    		  }
		    		  }// if
		    		  }// if
	
		    			  }// if
		    	  //index++
		    	  
		    	 
		    	}// while
		      in.close();
		 }catch (Exception e) {
			 e.printStackTrace();
		}
		
		 return graphCities;
		 
	}// read_tsp_data
	
	

	
	
}

