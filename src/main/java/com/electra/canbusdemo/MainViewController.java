package com.electra.canbusdemo;

import com.electra.canbusdemo.CANbus.CANbus_Controller;
import com.electra.canbusdemo.CANbus.Notifiable;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import org.controlsfx.control.*;
import eu.hansolo.medusa.Gauge;

import java.text.ChoiceFormat;
import java.util.HexFormat;
import java.util.Timer;
import java.util.TimerTask;

import static com.electra.canbusdemo.CANbus.CANbus_Controller.getCanBusController;
import static com.electra.canbusdemo.DeviceId.*;
import static java.lang.Integer.parseInt;

public class MainViewController implements Notifiable {
    @FXML
    private TextField idTextField;
    @FXML
    private Button connectButton;
    @FXML
    private Button i_emergencyStopButton;
    @FXML
    private Label i_reverseLabel;
    @FXML
    private Label i_velocitaDesiderataLabel;
    @FXML
    private Label i_coppiaDesiderataLabel;
    @FXML
    private Label i_nmLabel;
    @FXML
    private Label i_ampereLabel;
    @FXML
    private Label i_voltLabel;
    @FXML
    private Label i_kmLabel;
    @FXML
    private Label i_correnteLabel;
    @FXML
    private Label i_tensioneLabel;
    @FXML
    private Label i_velocitaLabel;
    @FXML
    private Button sendButton;
    @FXML
    private TextArea receivedTextArea;
    @FXML
    private TextArea sentTextArea;

    // INPUT WIDGETS -----------------------------------------------------------
    @FXML
    private ToggleSwitch i_forwardReverseSwitchButton;
    @FXML
    private ToggleSwitch i_coppiaVelocitaSwitchButton;
    @FXML
    private TextField i_coppiaTextField;
    @FXML
    private TextField i_velocitaTextField;


    @FXML
    private RadioButton i_sportRadioButton;
    @FXML
    private RadioButton i_ecoRadioButton;
    @FXML
    private RadioButton i_parkingModeRadioButton;
    @FXML
    private RadioButton i_chargingModeRadioButton;
    @FXML
    private TextField i_correnteTextField;
    @FXML
    private TextField i_tensioneTextField;
    @FXML
    private ToggleSwitch i_profiloCaricaSwitchButton;
    @FXML
    private ToggleSwitch i_caricaSwitchButton;
    @FXML
    private ToggleSwitch i_gridResSwitchButton;
    @FXML
    private Label i_customLabel;
    @FXML
    private Label i_resLabel;
    @FXML
    private ToggleSwitch i_contattore1SwitchButton;
    @FXML
    private ToggleSwitch i_contattore2SwitchButton;
    @FXML
    private  Gauge i_setVelocitaCoppiaGauge;
    @FXML
    private ToggleGroup sportEco;
    @FXML
    private ToggleGroup parkingCharging;
    // OUTPUT WIDGETS -----------------------------------------------------------
    @FXML
    private  Gauge o_tensioneBatterieGauge;
    @FXML
    private  Gauge o_correnteBatterieGauge;
    @FXML
    private  Gauge o_socGauge;
    @FXML
    private  Gauge o_tensioneCaricatoreGauge;
    @FXML
    private  Gauge o_correnteCaricatoreGauge;
    @FXML
    private  Gauge o_velocitaMotoreGauge;
    @FXML
    private  Gauge o_coppiaMotoreGauge;
    @FXML
    private Gauge o_temperaturaGauge;
    @FXML
    private  StatusBar o_modalitaCaricatoreStatusButton;
    @FXML
    private  StatusBar o_modalitaTrazioneStatusButton;
    @FXML
    private StatusBar o_statusButtonEmergencyStop;
    @FXML
    private  StatusBar o_statusButtonContattore1;
    @FXML
    private  StatusBar o_statusButtonContattore2;
    @FXML
    private Button o_emergencyStopButton;
    @FXML
    private Button o_saveButton;
    @FXML
    private TextField o_pathFileTextFied;
    @FXML
    private TextField data0TextField, data1TextField, data2TextField, data3TextField, data4TextField,
            data5TextField, data6TextField, data7TextField;

    private RadioButton lastSelectedSportEco;

    private RadioButton lastSelectedParkingCharging;
    @FXML
    private ComboBox<String> deviceComboBox;

    private ObservableList<String> canBusDevice_List = FXCollections.observableArrayList(); //Observable = c'è un osservatore che sa quando viene modificata

    private CANbus_Controller canBusController;
    private String canBusDevice;
    private final int MAX_SHOW_MEX = 500;
    private int sendID = 0, sendCycle = 0, receiveID = 0, receiveCycle = 0;

    private void checkTextFieldInput(KeyEvent keyEvent, TextField textField){ //Verifica che il carattere sia tra quelli concessi
        String newChar = keyEvent.getCharacter().toUpperCase();
        String keyFilter = "0123456789ABCDEF";

        if (!keyFilter.contains(newChar) || textField.getText().length() > 1) { //Verifica che non ci siano più di due caratteri
            keyEvent.consume(); //elimina il carattere
        }
    }

    //Aggiunto Giada
    private void checkNumberFieldInput(KeyEvent keyEvent, TextField textField){ //Verifica che il carattere sia tra quelli concessi
        String newChar = keyEvent.getCharacter().toUpperCase();
        String keyFilter = "0123456789";

        if (!keyFilter.contains(newChar) || (textField.getText().length() > 1 && !(textField.getText().equals("10") && newChar.equals("0")))  ) { //Verifica che non ci siano più di due caratteri
            keyEvent.consume(); //elimina il carattere
        }
    }

    @FXML
    public void initialize (){ //Called automatically by the framework
     //   sendButton.setDisable(true);  //Il pulsante di invio è disabilitato perché non è aperta la connessione
        canBusDevice_List.addAll(); //Lista di dispositivi collegati
        deviceComboBox.setItems(canBusDevice_List); //menu a tendina in alto a sinistra con dispositivi collegati
        canBusController = getCanBusController();  //CanBUS viene inizializzata come singleton
        canBusController.setNotifiable(this); //Se succede qualcosa notificalo a quest'oggetto


        i_velocitaTextField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            checkNumberFieldInput(keyEvent, i_velocitaTextField);
        });

        i_coppiaTextField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            checkNumberFieldInput(keyEvent, i_coppiaTextField);
        });


        i_sportRadioButton.setToggleGroup(sportEco);
        i_ecoRadioButton.setToggleGroup(sportEco);

        i_parkingModeRadioButton.setToggleGroup(parkingCharging);
        i_chargingModeRadioButton.setToggleGroup(parkingCharging);

        // Initialize Gauges

//        o_tensioneBatterieGauge = new Gauge();
//        o_socGauge = new Gauge();
//        o_coppiaMotoreGauge = new Gauge();
 //       o_temperaturaGauge = new Gauge();
//        o_velocitaMotoreGauge = new Gauge();
//        o_correnteCaricatoreGauge = new Gauge();
//        o_tensioneCaricatoreGauge = new Gauge();
        i_caricaSwitchButton = new ToggleSwitch();

        o_correnteBatterieGauge.setAnimated(true);
        o_tensioneBatterieGauge.setAnimated(true);
        o_socGauge.setAnimated(true);
        o_coppiaMotoreGauge.setAnimated(true);
        o_temperaturaGauge.setAnimated(true);
        o_velocitaMotoreGauge.setAnimated(true);
        o_correnteCaricatoreGauge.setAnimated(true);
        o_tensioneCaricatoreGauge.setAnimated(true);

        i_setVelocitaCoppiaGauge.setAnimated(true);

        i_emergencyStopButton.getStyleClass().add("button-red");

        deviceComboBox.setOnMouseClicked(event -> { //set = solo un listener per l'evento
            canBusDevice_List.removeAll(canBusDevice_List); //Pulisce la lista  (menu in alto a sinistra
            canBusDevice_List.addAll(canBusController.getAvailableHandlers()); //aggiorna la lista con i dispositivi disponibili
        });

        i_forwardReverseSwitchButton.setOnMouseClicked(event -> {
            if(i_forwardReverseSwitchButton.isSelected()){
                i_sportRadioButton.setDisable(true);
                i_ecoRadioButton.setDisable(true);
            }
            else{
                i_sportRadioButton.setDisable(false);
                i_ecoRadioButton.setDisable(false);
            }
           // if(connectButton.getText().equals("Connect")){
           //     fireAlarm(Alert.AlertType.ERROR, "Warning", "Please select a valid CAN bus adapter Device.");
           // }
           // else{
                try {
                    //send(VCU_Velocity);
                    handleReceivedMessages(Inverter, "0000000001000000"); //per testing
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
          //  }

        });
        i_coppiaVelocitaSwitchButton.setOnMouseClicked(event -> {
            if(i_coppiaVelocitaSwitchButton.isSelected()){
                i_coppiaTextField.setText("0");
                i_coppiaTextField.setDisable(true);
                i_velocitaTextField.setDisable(false);
            }
            else{
                i_coppiaTextField.setDisable(false);
                i_velocitaTextField.setDisable(true);
                i_velocitaTextField.setText("0");
            }
        });

        i_coppiaTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                i_setVelocitaCoppiaGauge.setValue(parseInt(i_coppiaTextField.getText()));
                try {
                    send(VCU_Pair);
                    System.out.println("a");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                event.consume(); // Consuma l'evento per evitare che altri gestori lo ricevano.
            }
        });

        i_correnteTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                i_setVelocitaCoppiaGauge.setValue(parseInt(i_velocitaTextField.getText()));
                try {
                    send(VCU_Charging);
                    System.out.println("a");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                event.consume(); // Consuma l'evento per evitare che altri gestori lo ricevano.
            }
        });

        i_tensioneTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    send(VCU_Charging);
                    System.out.println("a");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                event.consume(); // Consuma l'evento per evitare che altri gestori lo ricevano.
            }
        });

        i_parkingModeRadioButton.setOnMouseClicked(event -> {
            i_gridResSwitchButton.setDisable(true);
            i_profiloCaricaSwitchButton.setDisable(true);
            i_customLabel.setDisable(true);
            i_resLabel.setDisable(true);
            i_tensioneTextField.setDisable(true);
            i_correnteTextField.setDisable(true);
            i_ampereLabel.setDisable(true);
            i_voltLabel.setDisable(true);
            i_correnteLabel.setDisable(true);
            i_tensioneLabel.setDisable(true);
            try {
                send(VCU_Charging);
                send(VCU_Velocity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        i_chargingModeRadioButton.setOnMouseClicked(event -> {
            i_gridResSwitchButton.setDisable(false);
            i_profiloCaricaSwitchButton.setDisable(false);
            i_customLabel.setDisable(false);
            i_resLabel.setDisable(false);
            i_tensioneTextField.setDisable(false);
            i_correnteTextField.setDisable(false);
            i_ampereLabel.setDisable(false);
            i_voltLabel.setDisable(false);
            i_correnteLabel.setDisable(false);
            i_tensioneLabel.setDisable(false);
            try {
                send(VCU_Charging);
                send(VCU_Velocity);
                System.out.println("a");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        i_gridResSwitchButton.setOnMouseClicked(event -> {
            try {
                send(VCU_Charging);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        i_profiloCaricaSwitchButton.setOnMouseClicked(event -> {
            try {
                send(VCU_Charging);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        i_contattore1SwitchButton.setOnMouseClicked(event -> {
            try {
                send(VCU_Velocity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        i_contattore2SwitchButton.setOnMouseClicked(event -> {
            try {
                send(VCU_Pair);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

    }

    @FXML
    void connectButtonAction() { //quando si preme sul pulsante connect
        if(connectButton.getText().equals("Connect")){ //Se la scritta sul pulsante è connect
            canBusDevice = deviceComboBox.getValue(); //Assegna il dispositivo
            if (!canBusController.isConnected()) { //Se il dispositivo non è connesso
                if (canBusDevice == null || canBusDevice.equals("")) { //Controlla se nella combobox è null o una stringa vuota
                        fireAlarm(Alert.AlertType.ERROR, "Warning", "Please select a valid CAN bus adapter Device.");
                        return;
                    }
                }

            if (!canBusController.connect(canBusDevice)) { //true se la connessione va a buon fine, false se ci sono problemi con la connessione
                String mex = "Connection with the CANbus failed!!\n";
                mex += "Possible causes:\n";
                mex += "  - the selected device is wrong;\n" +
                        "  - the device is not connected to the USB port;\n\n";
                mex += "If the problem persist, try to unplug and replug the device.";

                fireAlarm(Alert.AlertType.ERROR, "Connection ERROR!!", mex);
                return;
            }

         //   sendButton.setDisable(false); //abilita il pulsante di invio del messaggio
            connectButton.setText("Disconnect"); //La scritta sul pulsante diventa disconnect
        } else { //Se si preme sul pulsante e la scritta non è "connect" (è disconnect)
            canBusController.disconnect();  //disconnette il dispositivo
         //   sendButton.setDisable(true);
            connectButton.setText("Connect");
        }


    }

    /*
    @FXML
    public void clearAllButtonAction(){ //Azione del pulsante clear all
        receiveID = receiveCycle = sendID = sendCycle = 0; //reinizializza tutte le variabili
        receivedTextArea.clear(); //pulisce le text area
        sentTextArea.clear();
    }
*/
    /*
    @FXML
    public void sendButtonAction() throws Exception {  // Può generare eccezioni se il parametro di fromHexDigits ha più di 8 byte o se contiene caratteri non esadecimali ma siamo sicuri che non è così quindi non serve il try catch
        byte data[] =
                {
                        (byte) HexFormat.fromHexDigits(data0TextField.getText()), //i dati dalle textfield sono trasformati in byte dll'esadecimale
                        (byte) HexFormat.fromHexDigits(data1TextField.getText()),
                        (byte) HexFormat.fromHexDigits(data2TextField.getText()),
                        (byte) HexFormat.fromHexDigits(data3TextField.getText()),
                        (byte) HexFormat.fromHexDigits(data4TextField.getText()),
                        (byte) HexFormat.fromHexDigits(data5TextField.getText()),
                        (byte) HexFormat.fromHexDigits(data6TextField.getText()),
                        (byte) HexFormat.fromHexDigits(data7TextField.getText())
                };

        canBusController.sendCommand(HexFormat.fromHexDigits(idTextField.getText()), data); //manda l'id in esadecimale e il dato
        sendID++;
        if(sendID-(sendCycle*MAX_SHOW_MEX) > MAX_SHOW_MEX){ //evita che troppe linee di testo vengano inviate alla text area di invio e la pulisce dpo un po'
            sendCycle++;
            sentTextArea.clear();
        }
       // sentTextArea.appendText("[" + sendID + "]: " + "ID: " + idTextField.getText().toUpperCase() + " data: " +   //il messaggio viene aggiunto alla text area
       //         HexFormat.of().formatHex(data).toUpperCase() + "\n");
    }
*/
    public void send(String id) throws Exception {
        byte[] data={};
        switch (id) {
            case VCU_Velocity: {
                if(i_forwardReverseSwitchButton.isDisabled()){
                    data = new byte[]
                            {
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits(i_contattore1SwitchButton.isSelected() ? "1" : "0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0")
                            };
                }
                else {
                    data = new byte[]
                            {
                                    (byte) HexFormat.fromHexDigits(i_forwardReverseSwitchButton.isSelected() ? "0" : "1"),
                                    (byte) HexFormat.fromHexDigits(i_forwardReverseSwitchButton.isSelected() ? "0" : "1"),
                                    (byte) HexFormat.fromHexDigits(i_forwardReverseSwitchButton.isSelected() ? "1" : "0"),
                                    (byte) HexFormat.fromHexDigits(i_velocitaTextField.getText()),
                                    (byte) HexFormat.fromHexDigits(i_contattore1SwitchButton.isSelected() ? "1" : "0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0")
                            };
                    if(data[3]>(byte)100){
                        fireAlarm(Alert.AlertType.ERROR, "Warning", "Please insert a valid input for velocity.");
                        return;
                    }
                    else if(data[1]==(byte)1&&data[2]==(byte)1){
                        fireAlarm(Alert.AlertType.ERROR, "Warning", "Forward and Reverse modes cannot be activated at the same time");
                        return;
                    }

                }
            }
            break;
            case VCU_Pair: {
                if(i_forwardReverseSwitchButton.isDisabled()){
                    data = new byte[]
                            {
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits(i_contattore2SwitchButton.isSelected() ? "1" : "0"),
                                    (byte) HexFormat.fromHexDigits(i_emergencyStopButton.getText().equals("STOP") ? "0" : "1"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0")
                            };
                }
                else {
                    data = new byte[]
                            {
                                    (byte) HexFormat.fromHexDigits(i_coppiaTextField.getText()),
                                    (byte) HexFormat.fromHexDigits(i_contattore2SwitchButton.isSelected() ? "1" : "0"),
                                    (byte) HexFormat.fromHexDigits(i_emergencyStopButton.getText().equals("STOP") ? "0" : "1"),
                                    (byte) HexFormat.fromHexDigits(i_sportRadioButton.isSelected() ? "1" : "0"),
                                    (byte) HexFormat.fromHexDigits(i_ecoRadioButton.isSelected() ? "1" : "0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0")
                            };

                    if(data[0]>(byte)100){
                        fireAlarm(Alert.AlertType.ERROR, "Warning", "Please insert a valid input for pair.");
                        return;
                    }
                    else if(data[3]==(byte)1&&data[4]==(byte)1){
                        fireAlarm(Alert.AlertType.ERROR, "Warning", "Sport and Eco modes cannot be activated at the same time");
                        return;
                    }
                }
            }
            case VCU_Charging: {
                if(i_gridResSwitchButton.isDisabled()){
                    data = new byte[]
                            {
                                    (byte) HexFormat.fromHexDigits(i_parkingModeRadioButton.isSelected() ? "1" : "0"),
                                    (byte) HexFormat.fromHexDigits(i_chargingModeRadioButton.isSelected() ? "1" : "0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                                    (byte) HexFormat.fromHexDigits("0"),
                            };
                }
                else{
                    data = new byte[]
                            {
                                    (byte) HexFormat.fromHexDigits(i_parkingModeRadioButton.isSelected() ? "1" : "0"),
                                    (byte) HexFormat.fromHexDigits(i_chargingModeRadioButton.isSelected() ? "1" : "0"),
                                    (byte) HexFormat.fromHexDigits(i_gridResSwitchButton.isSelected() ? "0" : "1"),
                                    (byte) HexFormat.fromHexDigits(i_gridResSwitchButton.isSelected() ? "1" : "0"),
                                    (byte) HexFormat.fromHexDigits(i_correnteTextField.getText()),
                                    (byte) HexFormat.fromHexDigits(i_tensioneTextField.getText()),
                                    (byte) HexFormat.fromHexDigits(i_profiloCaricaSwitchButton.isSelected() ? "0" : "1"),
                                    (byte) HexFormat.fromHexDigits(i_profiloCaricaSwitchButton.isSelected() ? "1" : "0"),
                            };

                   if(data[0]==(byte)1&&data[1]==(byte)1) {
                       fireAlarm(Alert.AlertType.ERROR, "Warning", "Parking and Charging modes cannot be activated at the same time");
                       return;
                   }
                   else if(data[2]==(byte)1&&data[3]==(byte)1) {
                       fireAlarm(Alert.AlertType.ERROR, "Warning", "Grid and Res modes cannot be activated at the same time");
                       return;
                   }
                   else if(data[6]==(byte)1&&data[7]==(byte)1) {
                       fireAlarm(Alert.AlertType.ERROR, "Warning", "Cost and Custom modes cannot be activated at the same time");
                       return;
                   }

                }

            }
            break;
            default:
                System.err.println("Id errato");
                break;
        }
        canBusController.sendCommand(HexFormat.fromHexDigits(id), data);
    }
    public void handleReceivedMessages(String id, String data) throws Exception {

        System.out.println(data);

        switch (id){
            case Charger_Battery:
                o_socGauge.setValue(HexFormat.fromHexDigits(data.substring(2,4)));
                break;
            case Charger:
                o_correnteCaricatoreGauge.setValue(HexFormat.fromHexDigits(data.substring(0,2)));
                o_tensioneCaricatoreGauge.setValue(HexFormat.fromHexDigits(data.substring(2,6)));
                break;
            case Inverter_Battery:
                o_temperaturaGauge.setValue(HexFormat.fromHexDigits(data.substring(2,4)));
                o_correnteBatterieGauge.setValue(HexFormat.fromHexDigits(data.substring(8,10)));
                o_tensioneBatterieGauge.setValue(HexFormat.fromHexDigits(data.substring(10,12)));
                break;
            case ChargingMode:
                if(HexFormat.fromHexDigits(data.substring(0,2))==0)
                    o_modalitaCaricatoreStatusButton.setText("");
                else if(HexFormat.fromHexDigits(data.substring(0,2))==1)
                    o_modalitaCaricatoreStatusButton.setText("   GRID");
                else if(HexFormat.fromHexDigits(data.substring(0,2))==2)
                    o_modalitaCaricatoreStatusButton.setText("     RES");
                break;
            case Inverter:
                o_velocitaMotoreGauge.setValue(HexFormat.fromHexDigits(data.substring(0,2)));
                o_coppiaMotoreGauge.setValue(HexFormat.fromHexDigits(data.substring(2,4)));
                if(HexFormat.fromHexDigits(data.substring(4,6))==0){
                    o_statusButtonContattore1.getStyleClass().remove("status-bar-red");
                    o_statusButtonContattore1.getStyleClass().add("status-bar-green");
                }
                else if(HexFormat.fromHexDigits(data.substring(4,6))==1){
                    o_statusButtonContattore1.getStyleClass().remove("status-bar-green");
                    o_statusButtonContattore1.getStyleClass().add("status-bar-red");
                }

                if(HexFormat.fromHexDigits(data.substring(6,8))==0) {
                    o_statusButtonContattore2.getStyleClass().remove("status-bar-red");
                    o_statusButtonContattore2.getStyleClass().add("status-bar-green");
                }
                else if(HexFormat.fromHexDigits(data.substring(6,8))==1){
                    o_statusButtonContattore2.getStyleClass().remove("status-bar-green");
                    o_statusButtonContattore2.getStyleClass().add("status-bar-red");
                }

                if(HexFormat.fromHexDigits(data.substring(8,10))==0)
                    o_modalitaTrazioneStatusButton.setText("");
                else if(HexFormat.fromHexDigits(data.substring(8,10))==1)
                    o_modalitaTrazioneStatusButton.setText(" SPORT");
                else if(HexFormat.fromHexDigits(data.substring(8,10))==2)
                    o_modalitaTrazioneStatusButton.setText("  ECO");
                break;
            default:
                break;
        }
    }
    private void fireAlarm(Alert.AlertType type, String title, String contentText){ //Genera una finestra di "allarme"
        Alert alert = new Alert(type); //type cambia l'icona, title il nome della finestra, contentText il testo di allarme
        alert.initOwner(MainApplication.stage); //Evita che l'utente possa interagire con l'applicazione finchè la finestra dell allarme è aperta
        alert.setTitle(title);
        alert.setContentText(contentText);
        //alert.setX(owner.getX());
        alert.showAndWait();
    }

    @Override
    public void _notify(String data) { //override del metodo notify dell'interfaccia notifiable
        Platform.runLater(() -> { //run later perché ci sono due thread diversi per inviare e ricevere
            receiveID++;
            if (receiveID-(receiveCycle*MAX_SHOW_MEX) > MAX_SHOW_MEX){
                receiveCycle++;
                receivedTextArea.clear();
            }
            //  receivedTextArea.appendText("[" + receiveID + "]: " + data + "\n");
           String[] data1=data.split(" ");
            try {
                handleReceivedMessages(data1[1], data1[3]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Funzione di esempio, triggerata dall'emergency stop, per verificare l'animazione delle gauge
    @FXML
    public void EmercencyStopAction(){
        if (i_emergencyStopButton.getText().equals("STOP")) {
            i_emergencyStopButton.getStyleClass().remove("button-red");
            i_emergencyStopButton.setText("RUN");
            i_emergencyStopButton.getStyleClass().add("button-green");
            i_contattore1SwitchButton.setSelected(false);
            i_contattore2SwitchButton.setSelected(false);
            setDisableWidgets(true);
            try{
                send(VCU_Pair);
                send(VCU_Velocity);
                send(VCU_Charging);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else {
            i_emergencyStopButton.getStyleClass().remove("button-green");
            i_emergencyStopButton.setText("STOP");
            i_emergencyStopButton.getStyleClass().add("button-red");
            setDisableWidgets(false);
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    double randomArray [] = {0,0,0,0,0,0,0,0,0};
                    for(int i = 0; i< 9;i++){
                        randomArray[i]= 10 + (Math.random() * (100 - 10));
                    }

                    o_correnteBatterieGauge.setValue(randomArray[0]);
                    o_tensioneBatterieGauge.setValue(randomArray[1]);
                    o_socGauge.setValue(randomArray[2]);
                    o_coppiaMotoreGauge.setValue(randomArray[3]);
                    o_temperaturaGauge.setValue(randomArray[4]);
                    o_velocitaMotoreGauge.setValue(randomArray[5]);
                    o_correnteCaricatoreGauge.setValue(randomArray[6]);
                    o_tensioneCaricatoreGauge.setValue(randomArray[7]);

                    i_setVelocitaCoppiaGauge.setValue(randomArray[8]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 1000);
    }

    public void setDisableWidgets(boolean disable){
        i_coppiaTextField.setDisable(disable);
        i_ecoRadioButton.setDisable(disable);
        i_chargingModeRadioButton.setDisable(disable);
        i_parkingModeRadioButton.setDisable(disable);
        i_sportRadioButton.setDisable(disable);
        i_correnteTextField.setDisable(disable);
        i_tensioneTextField.setDisable(disable);
        i_caricaSwitchButton.setDisable(disable);
        i_velocitaTextField.setDisable(disable);
        i_coppiaVelocitaSwitchButton.setDisable(disable);
        i_contattore1SwitchButton.setDisable(disable);
        i_contattore2SwitchButton.setDisable(disable);
        i_forwardReverseSwitchButton.setDisable(disable);
        i_gridResSwitchButton.setDisable(disable);
        i_profiloCaricaSwitchButton.setDisable(disable);
        i_customLabel.setDisable(disable);
        i_resLabel.setDisable(disable);
        i_reverseLabel.setDisable(disable);
        i_coppiaDesiderataLabel.setDisable(disable);
        i_velocitaDesiderataLabel.setDisable(disable);
        i_nmLabel.setDisable(disable);
        i_kmLabel.setDisable(disable);
        i_ampereLabel.setDisable(disable);
        i_voltLabel.setDisable(disable);
        i_correnteLabel.setDisable(disable);
        i_velocitaLabel.setDisable(disable);
        i_tensioneLabel.setDisable(disable);
    }

    @FXML
    private void handleRadioButtonSportEco() {

        RadioButton currentRadioButton = (RadioButton) sportEco.getSelectedToggle();

        if (currentRadioButton == lastSelectedSportEco) {
            // Se il RadioButton corrente è lo stesso di quello precedentemente selezionato, deseleziona
            currentRadioButton.setSelected(false);
            lastSelectedSportEco = null;  // Resetta l'ultimo RadioButton selezionato

        } else {
            // Seleziona il RadioButton corrente
            lastSelectedSportEco = currentRadioButton;
        }

        try {
            send(VCU_Pair);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleRadioButtonParkingCharging() {

        RadioButton currentRadioButton = (RadioButton) sportEco.getSelectedToggle();

        if (currentRadioButton == lastSelectedParkingCharging) {
            // Se il RadioButton corrente è lo stesso di quello precedentemente selezionato, deseleziona
            currentRadioButton.setSelected(false);
            lastSelectedParkingCharging = null;  // Resetta l'ultimo RadioButton selezionato

        } else {
            // Seleziona il RadioButton corrente
            lastSelectedParkingCharging = currentRadioButton;
        }

        try {
            send(VCU_Charging);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

