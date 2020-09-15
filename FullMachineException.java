/**
 * TheUltimateVendor created by Wyatt Webster on Mac Book Pro
 * Description: Interactive vending machine GUI for purchasing, and re-stocking snacks
 * 
 * Author:      Wyatt Webster (wcwebster@wisc.edu)
 * Date:        April 30
 */

package application;

/**
 * This class provides a custom exception used by only vending machines
 * 
 * @author wyattcharleswebster
 */
@SuppressWarnings("serial")
public class FullMachineException extends Exception {   
  /**
   * Constructs exception being thrown
   * 
   * @param errorMessage - Message passed from where the exception was thrown
   */
  public FullMachineException(String errorMessage) {
      super(errorMessage);
  }
}
