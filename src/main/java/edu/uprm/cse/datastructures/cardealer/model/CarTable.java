package edu.uprm.cse.datastructures.cardealer.model;

import edu.uprm.cse.datastructures.cardealer.util.TwoThreeTree;

public class CarTable {
	
  private static TwoThreeTree<Long ,Car> ttt = new TwoThreeTree<Long,Car>(new KeyComparator(),new CarComparator());

  private CarTable(){}
  
  //returns the instance of the list
  public static TwoThreeTree<Long ,Car> getInstance(){
    return ttt;
  }
  
  //creates a new instance of the list
  public static void resetCars() {
	  ttt = new TwoThreeTree <Long,Car>(new KeyComparator(), new CarComparator());
  }
  
}                       