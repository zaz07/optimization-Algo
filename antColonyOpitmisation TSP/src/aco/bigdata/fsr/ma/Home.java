package aco.bigdata.fsr.ma;

public class Home {

public static void main (String[] args)  {
			
		
ParamAco paramAco =new ParamAco();  
paramAco.ALPHA = 0.5; // alpha contr�le l�importance des ph�romones
paramAco.BETA = 7;  // b�ta contr�le la priorit� de distance
paramAco.evaporation = 0.33; // indique le pourcentage d'�vaporation de la ph�romone � chaque it�ration
paramAco.Q = 1e-3;  //  Q fournit des informations sur la quantit� totale de ph�romone laiss�e sur le parcours par chaque fourmi.
			
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
