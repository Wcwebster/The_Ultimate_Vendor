/**
 * TheUltimateVendor created by Wyatt Webster on Mac Book Pro
 * Description: Interactive vending machine GUI for purchasing, and re-stocking snacks
 * 
 * Author:      Wyatt Webster (wcwebster@wisc.edu)
 * Date:        April 30
 */

package application;

/**
 * The class holds a SnackFrequency object used by UltimateVendorGUI and Cart
 * 
 * @author wyattcharleswebster
 */
public class SnackFrequency {

  public String name;
  public int frequency;

  /**
   * Constructor, initializing declared variables
   */
  public SnackFrequency(String name) {
    this.name = name;
    frequency = 1;
  }

  /**
   * Increases frequency of the snack by 1
   */
  public void increaseFrequency() {
    frequency++;
  }

  /**
   * Returns the string version of this object used by the GUI
   */
  @Override
  public String toString() {
    if (frequency == 1)
      return name;
    return name + " x" + frequency;
  }

}
