package aco.bigdata.fsr.ma;

public class Home {

public static void main (String[] args)  {
			
		
ParamAco paramAco =new ParamAco();  
paramAco.ALPHA = 0.5; // alpha contrôle l’importance des phéromones
paramAco.BETA = 7;  // bêta contrôle la priorité de distance
paramAco.evaporation = 0.33; // indique le pourcentage d'évaporation de la phéromone à chaque itération
paramAco.Q = 1e-3;  //  Q fournit des informations sur la quantité totale de phéromone laissée sur le parcours par chaque fourmi.
			
			/**
			  	//berlin52.txt
			 	//burma14.txt
			 	//a280.txt
			 */
			
 String fichier ="a280";
int nbreFourmi = 200;
int nbreIteration = 2;

AntColonyOptimization antColony = new AntColonyOptimization(fichier,nbreFourmi, nbreIteration,paramAco);
			  
antColony.lancerACO();
			 
		}//main
		
	}//Home
