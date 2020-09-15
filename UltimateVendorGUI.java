/**
 * TheUltimateVendor created by Wyatt Webster on Mac Book Pro
 * Description: Interactive vending machine GUI for purchasing, and re-stocking snacks
 * 
 * Author:      Wyatt Webster (wcwebster@wisc.edu)
 * Date:        April 30
 */

package application;

import java.io.BufferedReader;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * This class creates Vending<achine and cart objects, and provides an interactive GUI allowing
 * users to use the vending machine and interact with its contents.
 * 
 * @author wyattcharleswebster
 */
public class UltimateVendorGUI extends Application implements EventHandler<ActionEvent> {
  @SuppressWarnings("unused")
  private List<String> args;
  private static final int WINDOW_WIDTH = 1020; // Declaring final variables
  private static final int WINDOW_HEIGHT = 650;
  private static final String APP_TITLE = "The Ultimate Vendor";
  private Button doneButton; // Declaring reactive buttons
  private Button checkoutBtn;
  private Button writeToFileBtn;
  private Button restockBtn;
  private Button logBtn;
  private ListView<SnackFrequency> cartListView; // List view displaying items in users cart
  private VendingMachine vendor; // VendingMachine object holding data of what's in the machine
  private String csvFile; // String holding a file path to be read data from
  private Cart cart; // Cart object holds data of what's in cart
  private Label total; // Label displaying total cost
  private String log; // String holding log of purchases made on machine
  private BorderPane root; // GUI Main BorderPane object

  /**
   * This method executes when the program is run. From here, launch()
   * 
   * @param args - command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * This class is called before the GUI is built by the start() method. Here, the variables are
   * initialized and the machine is stocked with items.
   */
  private void setup() {
    doneButton = new Button("Done");
    checkoutBtn = new Button("Checkout");
    writeToFileBtn = new Button("Save Contents To File");
    restockBtn = new Button("Re-Stock Machine");
    logBtn = new Button("View Purchase Log");
    cartListView = new ListView<SnackFrequency>();
    vendor = new VendingMachine();
    csvFile = "";
    cart = new Cart();
    total = new Label("TOTAL: ");
    log = "";
    reStockMachine(false); // Re-stock called with false parameter meaning its the initial setup
  }


  /**
   * This class is called from launch() and builds the GUI
   */
  @Override
  public void start(Stage primaryStage) {
    args = this.getParameters().getRaw();
    root = new BorderPane(); // Create BorderPane
    root.setPadding(new Insets(10, 10, 10, 10)); // Creates spacing around each BorderPane section

    setup(); // Call setup method to build vending machine contents and initialize variables

    // Building CENTER:
    root.setCenter(getSnackMenu()); // Set center to GridPane returned by getSnackMenu()

    // Building LEFT:
    VBox leftPanelTop = new VBox(); // Create and customize VBox's to hold buttons and labels
    leftPanelTop.setMaxWidth(220);
    leftPanelTop.setMinWidth(220);
    leftPanelTop.setBackground(new Background(
            new BackgroundFill(Color.rgb(199, 198, 232, .5), new CornerRadii(10), null)));
    leftPanelTop.setBorder(new Border(new BorderStroke(Color.rgb(199, 198, 232, .5),
            BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(10))));
    VBox leftPanelBtm = new VBox();
    leftPanelBtm.setMaxWidth(220);
    leftPanelBtm.setMinWidth(220);
    VBox leftPanel = new VBox();
    leftPanelBtm.setMaxWidth(220);
    leftPanelBtm.setMinWidth(220);

    Label instructionsLabel = new Label(); // Create and labels and set events on the buttons
    instructionsLabel.setText(" Instructions:");
    instructionsLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
    Label infoLabel = new Label();
    infoLabel.setText("\n (1). Click on items to\n add them to cart.\n\n "
            + "(2). Then press\n \"Checkout\" to remove\n the items from the\n machine.\n ");
    infoLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
    infoLabel.setTextFill(Color.GREY);
    Label dataOptionsLabel = new Label();
    dataOptionsLabel.setText("\n\nData Functions:\n ");
    dataOptionsLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
    dataOptionsLabel.setTextFill(Color.GREEN);
    writeToFileBtn.setOnAction(this);
    logBtn.setOnAction(this);
    restockBtn.setOnAction(this);
    restockBtn.setStyle("-fx-background-color: #FFD0D0; -fx-border-color: #ff0000; -fx-border-width: 1px; ");

    leftPanelTop.getChildren().addAll(instructionsLabel, infoLabel); // Add labels/buttons to VBox's
    leftPanelBtm.getChildren().addAll(dataOptionsLabel, writeToFileBtn, new Label(""), logBtn,
            new Label(""), restockBtn);
    leftPanel.getChildren().addAll(leftPanelTop, leftPanelBtm);
    root.setLeft(leftPanel); // Set VBox to left

    // Building RIGHT:
    cartListView.setPrefWidth(180); // Create labels and set actions on buttons
    total.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
    Label cartLabel = new Label("Cart: ");
    cartLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
    cartLabel.setTextFill(Color.GREEN);
    checkoutBtn.setOnAction(this);
    VBox rightPanel = new VBox(cartLabel, cartListView, total); // Create VBox, add items to here
    rightPanel.getChildren().add(checkoutBtn);
    rightPanel.setMaxWidth(200);
    rightPanel.setMinWidth(200);
    root.setRight(rightPanel); // Set VBox to right

    // Building TOP:
    Label topLabel = new Label("The Ultimate Vendor"); // Create label
    topLabel.setText("The Ultimate Vendor");
    topLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
    topLabel.setTextFill(Color.GREEN);
    HBox topPanel = new HBox(topLabel); // Create HBox to hold label
    topPanel.setAlignment(Pos.CENTER);
    root.setTop(topPanel); // Set HBox to the top

    // Building BOTTOM:
    doneButton.setOnAction(this); // Add this button into the events handled by action
    GridPane bottomPanel = new GridPane(); // Create HBox to hold button
    bottomPanel.add(doneButton, 0, 0, 1, 1);
    bottomPanel.setHgap(10);
    root.setBottom(bottomPanel); // Add HBox to the bottom

    // Create a new scene with the BorderPane, and add it to the stage
    Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    primaryStage.setTitle(APP_TITLE);
    primaryStage.setScene(mainScene);
    primaryStage.show();
  }


  /**
   * Builds GridPane holding the vending machines contents
   * 
   * @return GridPane - displaying all of the vending machines items
   */
  public GridPane getSnackMenu() {
    GridPane snackMenu = new GridPane(); // Create GridPane and customize it
    snackMenu.setBackground(new Background(
            new BackgroundFill(Color.rgb(199, 232, 198, .5), new CornerRadii(10), null)));
    snackMenu.setBorder(new Border(new BorderStroke(Color.rgb(199, 232, 198, .5),
            BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(10))));
    snackMenu.setHgap(10);
    snackMenu.setVgap(10);
    snackMenu.setMaxWidth(540);
    snackMenu.setMaxHeight(540);

    Alert soldOut = new Alert(AlertType.ERROR); // Art to be shown if snack is out of stock
    soldOut.setContentText("Snack Is Out Of Stock");

    Label snackText = new Label(); // Label holding snack name, is added to each button
    snackText.setFont(Font.font("Verdana", FontWeight.NORMAL, 10));
    snackText.setTextFill(Color.BLACK);

    int row = 0;
    int column = 0;
    for (String snack : vendor.getSnackNames()) { // For each snack in machine, add button
      if (vendor.getSnack(snack).quantity != 0) { // Only add button if quantity isn't 0
        snackText.setText(snack);
        Button snackButton = new Button(snack + "\n( $" + vendor.getSnack(snack).price + " )");
        snackButton.setMinWidth(125);
        snackButton.setMinHeight(125);
        // Event executed with the button is pressed
        snackButton.setOnAction((event) -> {
          // If snack quantity in cart is equal to that in the machine, show soldOut Alert
          if (cart.getSnackFrequencyObject(snack) != null && cart
                  .getSnackFrequencyObject(snack).frequency == vendor.getSnack(snack).quantity) {
            soldOut.show();
          } else {
            Boolean inCart = cart.contains(snack); // Variable holding if it was already in the cart
            cart.addSnack(snack, vendor.getSnack(snack).price); // Add snack name/price to cart
            total.setText("TOTAL: $" + cart.getCartCost()); // Update total cost label
            if (inCart) // If already in the cart, remove non-updated object from list view
              cartListView.getItems().remove(cart.getSnackFrequencyObject(snack));
            // Add updated nackFrequency object to list view
            cartListView.getItems().add(cart.getSnackFrequencyObject(snack));
          }
        });
        snackMenu.add(snackButton, column, row, 1, 1); // Add button to GridPane
        if (column == 3) { // Update row and column variables
          column = 0;
          row++;
        } else
          column++;
      }
    }
    return snackMenu;
  }


  /**
   * This method re-stocks the machine by prompting the user for a file path and then loading all
   * items in that file into the machine.
   * 
   * @param reStock - If true, this is simply a re-stock and the re-stock dialog should be
   *                displayed. If false, this is the initial stocking, and the loading stage dialog
   *                should be displayed.
   */
  public void reStockMachine(Boolean reStock) {
    // Creates Pop Up dialog for user to enter a path to a CSV file holding snack data
    TextInputDialog dialog = new TextInputDialog("Type here...");
    String header;
    if (reStock) {
      dialog.setTitle("Re-Stock Screen");
      header = "To restock the machine, please type in a csv file path to load contents from a file";
    } else {
      dialog.setTitle("Loading Stage");
      header = "To launch The Ultimate Vender, please do ONE of the following:\n"
              + "\t(1). Click any button to load machine with default contents\n"
              + "\t(2). Type in a csv file path to load contents from a file";
    }
    dialog.setHeaderText(header);
    dialog.setContentText("FilePath:");

    // Saves what user entered as csvFile when they click OK
    csvFile = "";
    Optional<String> result = dialog.showAndWait();
    result.ifPresent(name -> csvFile = name);

    // Add items to vending machine object by reading file
    BufferedReader reader = null;
    Boolean done = false;
    String line = "";
    while (!done) {
      try {
        reader = new BufferedReader(new FileReader(csvFile));
        while ((line = reader.readLine()) != null) {
          String[] snack = line.split(",");
          vendor.addSnack(new Snack(snack[0], Double.parseDouble(snack[1]),
                  Integer.parseInt(snack[2]), Integer.parseInt(snack[3])));
        }
        done = true;
      } catch (IOException | NumberFormatException e) {
        // If reStocking or csvFile is already the default path, then stock nothing, done is true
        if (reStock
                || csvFile == "/Users/wyattcharleswebster/eclipse-workspace(400)/The_Ultimate_Vendor/Snacks.csv") {
          csvFile = "";
          done = true;
        } else // Else, change attempt to load default path
          csvFile =
                  "/Users/wyattcharleswebster/eclipse-workspace(400)/The_Ultimate_Vendor/Snacks.csv";
      } catch (FullMachineException e) {
        // If machine is full, the file is continued to be read, and the items not added into the
        // machine are displayed to the user
        String snacksNotAdded = "";
        try {
          String[] snack = line.split(",");
          snacksNotAdded += snack[0] + " x" + Integer.parseInt(snack[3]) + ", ";
          while ((line = reader.readLine()) != null) {
            snack = line.split(",");
            snacksNotAdded += snack[0] + " x" + Integer.parseInt(snack[3]) + ", ";
          }
        } catch (IOException | NumberFormatException f) {
        }
        Alert fullMachine = new Alert(AlertType.ERROR);
        fullMachine.setContentText(e.getMessage() + snacksNotAdded);
        fullMachine.showAndWait();
        done = true;
      } finally { // Close file
        if (reader != null) {
          try {
            reader.close();
          } catch (IOException e) {
          }
        }
      }
    }
    root.setCenter(getSnackMenu()); // Rebuild snack selection GUI
  }

  /**
   * This method handles all ActionEvents that occur on reactive buttons
   */
  @Override
  public void handle(ActionEvent event) {
    // DONE Button
    if (event.getSource() == doneButton) {
      Stage stage = (Stage) doneButton.getScene().getWindow();
      stage.close();
    }

    // CHECKOUT Button
    else if (event.getSource() == checkoutBtn) {
      log += "PURCHASE:\n";
      // For each snack bought, update the machine and log
      for (SnackFrequency snack : cart.getContents()) {
        vendor.getSnack(snack.name).updateQuantity(0 - snack.frequency);
        log += "\t" + snack.name + ", " + snack.frequency + "\n";
      }
      log += "\t" + total.getText() + "\n";
      cart = new Cart(); // Re-instantiate cart
      cartListView.getItems().clear(); // Clear cartListView
      total.setText("TOTAL: "); // Reset total button

      // Rebuild snack selection GUI
      root.setCenter(getSnackMenu());
    }

    // SAVE Button
    else if (event.getSource() == writeToFileBtn) {
      try {
        FileWriter csvWriter = new FileWriter("UltimateVendorContents.csv");
        for (Snack snck : vendor.getSnackObjects()) { // Write all machine data to csv file
          if (snck.quantity != 0) {
            List<String> snackData = Arrays.asList(snck.name, String.valueOf(snck.price),
                    String.valueOf(snck.catagory), String.valueOf(snck.quantity));
            csvWriter.append(String.join(",", snackData));
            csvWriter.append("\n");
          }
        }
        csvWriter.flush();
        csvWriter.close();
        File f = new File("UltimateVendorContents.csv");
        // Alert user save was successful. Gives them file path by copying it to clip board
        Alert fileWriteComplete = new Alert(AlertType.NONE);
        fileWriteComplete.getDialogPane().getButtonTypes().add(ButtonType.OK);
        fileWriteComplete.setContentText("Save Successful.\nFile Path Copied To Clipboard!");
        fileWriteComplete.show();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection filePath = new StringSelection(f.getAbsolutePath());
        clipboard.setContents(filePath, null); // Copy to clip board
      } catch (FileNotFoundException e) {
      } catch (IOException e) {
      }
    }

    // LOG Button
    else if (event.getSource() == logBtn) {
      Alert showLog = new Alert(AlertType.NONE); // Show purchase log to user through an alert
      showLog.getDialogPane().getButtonTypes().add(ButtonType.OK);
      showLog.setContentText(log);
      showLog.show();
    }

    // RESTOCK Button
    else if (event.getSource() == restockBtn) {
      // Creates Pop Up dialog for re-stocker to enter password
      TextInputDialog passwordDialog = new TextInputDialog("Type here...");
      passwordDialog.setTitle("Password Screen");
      String passwordHeader =
              "Whoops! \nLooks like you need to enter a password to restock the machine. \n(Hint: "
                      + "Its the 5 letter name of the CS-400 instructor)";
      passwordDialog.setHeaderText(passwordHeader);
      passwordDialog.setContentText("Password");
      Optional<String> attempt = passwordDialog.showAndWait();

      if (attempt.isPresent()) {
        if (attempt.get().toLowerCase().equals("debra")) { // If password correct, reStockMachine()
          reStockMachine(true);
        } else { // Else, tell the user it is wrong through an alert
          Alert wrongPassword = new Alert(AlertType.NONE);
          wrongPassword.getDialogPane().getButtonTypes().add(ButtonType.OK);
          wrongPassword.setContentText("Sorry, incorrrect password");
          wrongPassword.show();
        }
      }
    }
  }
}
