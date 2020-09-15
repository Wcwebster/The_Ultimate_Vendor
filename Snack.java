/**
 * TheUltimateVendor created by Wyatt Webster on Mac Book Pro
 * Description: Interactive vending machine GUI for purchasing, and re-stocking snacks
 * 
 * Author:      Wyatt Webster (wcwebster@wisc.edu)
 * Date:        April 30
 */

package application;

public class Snack {

  public String name;
  public Double price;
  public int catagory;
  public int quantity; // Number of them left in machine

  /**
   * Constructor that initializes declared variables
   */
  public Snack(String name, Double price, int catagory, int quantity) {
    this.name = name;
    this.price = price;
    this.catagory = catagory;
    this.quantity = quantity;
  }

  /**
   * Updates quantity by amount passed in
   * 
   * @param addedQuantity - the number to be added to the current quantity
   */
  public void updateQuantity(int addedQuantity) {
    quantity += addedQuantity;
  }

}
