package com.conversor;

import com.conversor.comboItem.ComboItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class currencyConverterUI {
  private currencyConverterAPI api;
  private JFrame frame;
  private Color backgroundColor;
  private Color textColor;
  private JPanel panel;
  private JTextField sourceCurrencyField;
  private JTextField targetCurrencyField;
  private JTextField amountFieldText;
  private JTextField amountField;
  private JButton convertButton;
  private JTextField resultField;
  private String apiKey;
  private JsonObject responseAPI;
  private JsonArray coutryCurrencyResponse;

  // private AutocompleteJComboBox sourceCombo;
  // private AutocompleteJComboBox targetCombo;

  private JComboBox<ComboItem> sourceCombo;
  private JComboBox<ComboItem> targetCombo;

  public currencyConverterUI() {
    apiKey = "cur_live_qetF1QgMtGj1NYdiTAORP97OyVH1tIz27MD8Oj87";
    try {
      coutryCurrencyResponse = new CountryCurrencyAPI().callAPI();
    } catch (NullPointerException | IOException e) {
      coutryCurrencyResponse = null;
      System.err.println("An error ocurred while connecting to the API" + e.getMessage());
    }
    // sourceCombo = getCurrencySearchableButton();
    // targetCombo = getCurrencySearchableButton();
    sourceCombo = getComboItem();
    targetCombo = getComboItem();
  }

  private JComboBox<ComboItem> getComboItem() {
    JComboBox<ComboItem> combo = new JComboBox<>();
    ArrayList<ComboItem> optionList = new ArrayList<>();
    for (JsonElement countryCurrency : coutryCurrencyResponse) {
      String countryName = countryCurrency.getAsJsonObject().get("name").getAsString();
      JsonElement currenciesElement = countryCurrency.getAsJsonObject().get("currencies");
      String[] currenciesArray;
      if (currenciesElement != null && !currenciesElement.isJsonNull()) {
        JsonObject currenciesObject = currenciesElement.getAsJsonObject();
        Set<String> currencies = new HashSet<>();
        for (String currencyCode : currenciesObject.keySet()) {
          currencies.add(currencyCode);
        }
        currenciesArray = currencies.toArray(new String[0]);
      } else {
        currenciesArray = new String[0]; // empty array if "currencies" is null
      }

      for (String currency : currenciesArray) {
        optionList.add(new ComboItem(countryName + " (" + currency + ")", currency));
      }
    }
    for (ComboItem option : optionList) {
      combo.addItem(option);
    }
    return combo;
  }

  // public StringSearchable getStringSearchable() {
  // List<String> sourceOptions = new ArrayList<String>();
  //
  // for (JsonElement countryCurrency : coutryCurrencyResponse) {
  // String countryName =
  // countryCurrency.getAsJsonObject().get("name").getAsString();
  //
  // JsonElement currenciesElement =
  // countryCurrency.getAsJsonObject().get("currencies");
  //
  // String[] currenciesArray;
  //
  // if (currenciesElement != null && !currenciesElement.isJsonNull()) {
  // JsonObject currenciesObject = currenciesElement.getAsJsonObject();
  // Set<String> currencies = new HashSet<>();
  // for (String currencyCode : currenciesObject.keySet()) {
  // currencies.add(currencyCode);
  // }
  // currenciesArray = currencies.toArray(new String[0]);
  // } else {
  // currenciesArray = new String[0]; // empty array if "currencies" is null
  // }
  //
  // for (String currency : currenciesArray) {
  // sourceOptions.add(countryName + " (" + currency + ")");
  // }
  // }
  // sourceOptions.add("Other");
  // return new StringSearchable(sourceOptions);
  // }
  //
  // /**
  // * @return
  // */
  // private AutocompleteJComboBox getCurrencySearchableButton() {
  //
  // StringSearchable sourceSearchable = getStringSearchable();
  // AutocompleteJComboBox sourceCombo = new
  // AutocompleteJComboBox(sourceSearchable);
  // return sourceCombo;
  // }

  /** */
  public void launchConverterUI() {

    api = new currencyConverterAPI(apiKey, "PEN");
    frame = new JFrame("Currency Converter");
    frame.setSize(10000, 15000);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    backgroundColor = Color.decode("#1e1e2e");
    textColor = Color.decode("#cdd6f4");

    panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(backgroundColor);

    sourceCurrencyField = new JTextField("Ingrese la moneda");
    sourceCurrencyField.setForeground(textColor);
    sourceCurrencyField.setBackground(backgroundColor);
    sourceCurrencyField.setEditable(false);

    targetCurrencyField = new JTextField("Ingrese la moneda a convertir");
    targetCurrencyField.setForeground(textColor);
    targetCurrencyField.setBackground(backgroundColor);
    targetCurrencyField.setEditable(false);

    amountFieldText = new JTextField("Ingrese la cantidad a convertir");
    amountFieldText.setForeground(textColor);
    amountFieldText.setBackground(backgroundColor);
    amountFieldText.setEditable(false);

    amountField = new JTextField();
    // amountField.setForeground(textColor);
    // amountField.setBackground(backgroundColor);

    convertButton = new JButton("Convertir");
    convertButton.setForeground(textColor);
    convertButton.setBackground(backgroundColor);

    resultField = new JTextField("Resultado");
    resultField.setForeground(textColor);
    resultField.setBackground(backgroundColor);
    resultField.setEditable(false);

    panel.add(sourceCurrencyField);
    // panel.add(sourceCombo);
    panel.add(sourceCombo);
    panel.add(targetCurrencyField);
    // panel.add(targetCombo);
    panel.add(targetCombo);
    panel.add(amountFieldText);
    panel.add(amountField);
    panel.add(convertButton);
    panel.add(resultField);

    convertButton.addActionListener(e -> clickConvertButton());

    frame.getContentPane().add(panel);
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * @throws IOException
   * @throws URISyntaxException
   */
  private void clickConvertButton() {
    ComboItem selectedItemSource = (ComboItem) sourceCombo.getSelectedItem();
    ComboItem selectedItemTarget = (ComboItem) targetCombo.getSelectedItem();
    api.setCurrencyBase(selectedItemSource.getId());
    try {
      api.connect();
    } catch (IOException | URISyntaxException e) {
      System.err.println("An error ocurred while connecting to the API" + e.getMessage());
    }
    responseAPI = api.getResponse();
    Double sourceAmount = Double.parseDouble(amountField.getText());
    Double targetAmount = sourceAmount
        * responseAPI
            .getAsJsonObject("data")
            .getAsJsonObject(selectedItemTarget.getId())
            .get("value")
            .getAsDouble();
    resultField.setText(String.valueOf(targetAmount));
  }
}
