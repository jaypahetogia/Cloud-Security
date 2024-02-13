package application;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	
	private boolean isKeyValid(String key, String algorithm) {
	    int length = key.length();
	    switch (algorithm) {
	        case "AES-128":
	            return length == 16;
	        case "AES-192":
	            return length == 24;
	        case "AES-256":
	            return length == 32;
	        case "DES":
	            return length == 8;
	        default:
	            return false;
	    }
	}
	
    @Override
    public void start(Stage primaryStage) throws Exception {
      //textfields for input and output
    	Label textAreaLabel = new Label("Enter text to be encrypted or decrypted");
    	TextArea plaintextArea = new TextArea();
    	plaintextArea.setPromptText("Enter text to be encrypted or decrypted");
    	TextArea encryptedTextArea = new TextArea();
    	encryptedTextArea.setPromptText("Results will show here");
    	encryptedTextArea.setEditable(false);
    	
    //Spinner for Shift Value
    	Spinner<Integer> shiftSpinner = new Spinner<>();
    	SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 25, 3);
    	shiftSpinner.setValueFactory(valueFactory);
    	shiftSpinner.setEditable(true);
    	
    //Dropdown to select algorithm
    	ComboBox<String> algorithmComboBox = new ComboBox<>();
    	algorithmComboBox.setPromptText("Select Algorithm");
    	algorithmComboBox.getItems().addAll("AES", "DES", "Caesar Cipher");
    	
    //Fields for key management
    	Label keyLabel = new Label("Key (16, 24, or 32 characters for AES; 8 characters for DES:");
    	TextField keyField = new TextField();
    	keyField.setPromptText("Enter key");

    	Label usernameLabel = new Label("Username:");
    	TextField usernameField = new TextField();
    	usernameField.setPromptText("Enter username");
    	
    	ComboBox<String> keySizeComboBox = new ComboBox<>();
    	keySizeComboBox.getItems().addAll("AES-128", "AES-192", "AES-256", "DES");
    	keySizeComboBox.setValue("AES-128"); //Default choice

    	HBox keyManagementBox = new HBox(10, usernameLabel, usernameField, keyLabel, keyField);
    	keyManagementBox.setAlignment(Pos.CENTER);
    	
    //Buttons and labels
    	Button clearButton = new Button("Clear");
    	Button encryptButton = new Button("Encrypt");
    	Button decryptButton = new Button("Decrypt");
    	Button saveKeyButton = new Button("Save Key");
    	Button loadKeyButton = new Button("Load Key");
    	Button saveConfigButton = new Button("Save Config");
    	Button loadConfigButton = new Button("Load Config");
  
    	
    //Event Handlers for the Buttons
    	clearButton.setOnAction(e -> {
    	    plaintextArea.clear();
    	    encryptedTextArea.clear();
    	});
    	
    	encryptButton.setOnAction(event -> {
    	    String algorithm = algorithmComboBox.getValue();
    	    String selectedAlgorithm = keySizeComboBox.getValue(); 
    	    String inputText = plaintextArea.getText();
    	    String key = keyField.getText();
    	    String encryptedText = "";

    	    if (!"Caesar Cipher".equals(algorithm) && !isKeyValid(key, selectedAlgorithm)) {
    	        encryptedTextArea.setText("Invalid key length for " + selectedAlgorithm);
    	        return;
    	    }

    	    try {
    	        if ("Caesar Cipher".equals(algorithm)) {
    	            int shift = shiftSpinner.getValue();
    	            encryptedText = CaesarCipher.encrypt(inputText, shift);
    	        } else if ("DES".equals(selectedAlgorithm)) {
    	            encryptedText = DESUtil.encrypt(inputText, key);
    	        } else if (selectedAlgorithm.startsWith("AES")) {
    	            encryptedText = AESUtil.encrypt(inputText, key);
    	        }
    	        // ... (other algorithms)
    	    } catch (Exception e) {
    	        e.printStackTrace(); // Handle exceptions
    	    }

    	    encryptedTextArea.setText(encryptedText);
    	});
    	
    	decryptButton.setOnAction(event -> {
    	    String algorithm = algorithmComboBox.getValue();
    	    String selectedAlgorithm = keySizeComboBox.getValue();
    	    String inputText = plaintextArea.getText();
    	    String key = keyField.getText();
    	    StringBuilder decryptedText = new StringBuilder();

    	    if (!"Caesar Cipher".equals(algorithm) && !isKeyValid(key, selectedAlgorithm)) {
    	        encryptedTextArea.setText("Invalid key length for " + selectedAlgorithm);
    	        return;
    	    }

    	    try {
    	        if ("Caesar Cipher".equals(algorithm)) {
    	            for (int shift = 0; shift < 26; shift++) {
    	                String decrypted = CaesarCipher.decrypt(inputText, shift);
    	                decryptedText.append("Shift ").append(shift).append(": ").append(decrypted).append("\n");
    	            }
    	        } else if ("DES".equals(selectedAlgorithm)) {
    	            decryptedText.append(DESUtil.decrypt(inputText, key));
    	        } else if (selectedAlgorithm.startsWith("AES")) {
    	            decryptedText.append(AESUtil.decrypt(inputText, key));
    	        }
    	        // ... (other algorithms)
    	    } catch (Exception e) {
    	        e.printStackTrace(); // Handle exceptions
    	    }

    	    encryptedTextArea.setText(decryptedText.toString());
    	});

    	
    	saveKeyButton.setOnAction(e -> {
    	    String username = usernameField.getText();
    	    String key = keyField.getText();
    	    try {
    	        DatabaseUtil.saveKey(username, key); //to encrypt key before saving
    	    } catch (Exception ex) {
    	        ex.printStackTrace();
    	    }
    	});

    	loadKeyButton.setOnAction(e -> {
    	    String username = usernameField.getText();
    	    try {
    	        String key = DatabaseUtil.loadKey(username);
    	        keyField.setText(key); //secure handling
    	    } catch (Exception ex) {
    	        ex.printStackTrace(); 
    	    }
    	});
    	
    	HBox comboBoxWithClearButton = new HBox(10, algorithmComboBox, clearButton);
    	comboBoxWithClearButton.setAlignment(Pos.CENTER);
    	
    	HBox buttonBox = new HBox(10, new HBox(new Label("Shift: "), shiftSpinner), encryptButton, decryptButton, saveKeyButton, loadKeyButton, saveConfigButton, loadConfigButton);
        buttonBox.setAlignment(Pos.CENTER);

      //Our Main Layout
        VBox root = new VBox(10, textAreaLabel, plaintextArea, comboBoxWithClearButton, buttonBox, encryptedTextArea, keyManagementBox, keySizeComboBox);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ADD8E6;");

      //Our Scene and Stage
        Scene scene = new Scene(root, 800, 700);
        primaryStage.setTitle("Encryption Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}
