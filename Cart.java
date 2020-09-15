/**
 * TheUltimateVendor created by Wyatt Webster on Mac Book Pro
 * Description: Interactive vending machine GUI for purchasing, and re-stocking snacks
 * 
 * Author:      Wyatt Webster (wcwebster@wisc.edu)
 * Date:        April 30
 */

package application;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the objects present in the users cart
 * 
 * @author wyattcharleswebster
 */
public class Cart {
  private List<String> snacks; // List of snack names
  private List<SnackFrequency> snackFreqs; // List of snack frequency objects
  private Double cartCost; // Cost of cart

  /**
   * Cart constructor. Initializes declared variables
   */
  public Cart() {
    snacks = new ArrayList<String>();
    snackFreqs = new ArrayList<SnackFrequency>();
    cartCost = 0.0;
  }

  /**
   * Addeds a newly selected snack into the Cart
   * 
   * @param name  - name of snack being added
   * @param price - price of snack being added
   */
  public void addSnack(String name, Double price) {
    if (snacks.contains(name)) { // If snack is in cart, increase quantity
      getSnackFrequencyObject(name).increaseFrequency();
    } else { // Else, add to cart as new snack
      snacks.add(name);
      snackFreqs.add(new SnackFrequency(name));
    }
    cartCost += price;
  }

  /**
   * Returns the SnackFrequency object with the passed in name
   * 
   * @param name - name of snack being returned
   * @return SnackFrequency - obj with name passed in
   */
  public SnackFrequency getSnackFrequencyObject(String name) {
    for (SnackFrequency snack : snackFreqs) {
      if (snack.name.equals(name)) {
        return snack;
      }
    }
    return null;
  }

  /**
   * Returns true if cart contains snack with name
   * 
   * @param name - name of snack
   * @return true if snack is in cart, false otherwise
   */
  public boolean contains(String name) {
    return snacks.contains(name);
  }

  /**
   * @return Double - the cost of the cart
   */
  public Double getCartCost() {
    return cartCost;
  }

  /**
   * @return List - of SnackFrequency objects in cart
   */
  public List<SnackFrequency> getContents() {
    return snackFreqs;
  }
}
