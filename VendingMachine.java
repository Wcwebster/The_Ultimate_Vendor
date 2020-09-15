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
import java.util.Set;
import java.util.TreeSet;

/**
 * This class represents the VendingMachine object and the contents within. It provides 
 * functionality for adding and removing items and also analyzing the machines total contents
 * 
 * @author wyattcharleswebster
 */
public class VendingMachine {
  private List<Snack> snacks; // List of Snack objects in machine
  private Set<String> snackNames; // Set of names of the Snacks in the machines
  private int size; // Number of total snacks in the machine
  private final int MAX_CAPACITY;

  /**
   * This is the VendingMachine constructor. Here the declared variables are initialized.
   */
  public VendingMachine() {
    snacks = new ArrayList<Snack>();
    snackNames = new TreeSet<String>();
    size = 0;
    MAX_CAPACITY = 400;
  }

  /**
   * Adds a snack object into the vending machine and updates fields as needed
   * 
   * @param snack - the snack object to be added in
   * @throws FullMachineException - if the machine exceeds its max capacity
   */
  public void addSnack(Snack snack) throws FullMachineException {
    if (size > MAX_CAPACITY) // Throw excpetion if max capacity was exceeding on list install
      throw new FullMachineException("The Ultimate Vendor has reached its max capacity of 400 total"
              + " snacks. The items not successfully added are as follows:\n");

    if (snackNames.contains(snack.name)) // If snack is already in machine, update quantity
      getSnack(snack.name).updateQuantity(snack.quantity);
    else { // Else, if number of snacks is under 16 add it in, otherwise throw exception
      if (snacks.size() < 16) {
        snacks.add(snack);
        snackNames.add(snack.name);
      } else
        throw new FullMachineException("The Ultimate Vendor has reached its max capacity of 16 "
                + "different items. The items not successfully added are as follows:\n");
    }

    size += snack.quantity;
  }

  /**
   * Processes a snack being purchased by subtracting 1 from its quantity. If its new quantity is 0,
   * remove it as an option from the machine
   * 
   * @param snackName - name of the snack being purchased
   */
  public void removeSnack(String snackName) {
    Snack purchasedSnack = getSnack(snackName);
    purchasedSnack.updateQuantity(-1);
    size--;
    if (purchasedSnack.quantity == 0) {
      snackNames.remove(snackName);
      snacks.remove(purchasedSnack);
    }
  }

  /**
   * @return - the Set of snack names
   */
  public Set<String> getSnackNames() {
    return snackNames;
  }

  /**
   * @return - the List of snack objects
   */
  public List<Snack> getSnackObjects() {
    return snacks;
  }

  /**
   * Finds the snack object with that name. Returns null if not present
   * 
   * @param name - name of snack to be returned
   * @return Snack - the snack with the name passed in
   */
  public Snack getSnack(String name) {
    for (Snack snack : snacks)
      if (snack.name.equals(name))
        return snack;
    return null;
  }
}
