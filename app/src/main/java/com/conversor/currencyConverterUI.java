package com.conversor;

import com.conversor.searchableComboBox.AutocompleteJComboBox;
import com.conversor.searchableComboBox.StringSearchable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
  private JTextField amountField;
  private JButton convertButton;
  private JTextField resultField;
  private String apiKey;
  private JsonObject responseAPI;
  private JsonArray coutryCurrencyResponse;
  private AutocompleteJComboBox sourceCombo;
  private AutocompleteJComboBox targetCombo;

  public currencyConverterUI() {
    apiKey = "cur_live_qetF1QgMtGj1NYdiTAORP97OyVH1tIz27MD8Oj87";
    try {
      coutryCurrencyResponse = new CountryCurrencyAPI().callAPI();
    } catch (NullPointerException | IOException e) {
      coutryCurrencyResponse = null;
      System.err.println("An error ocurred while connecting to the API" + e.getMessage());
    }
    sourceCombo = getCurrencySearchableButton();
    targetCombo = getCurrencySearchableButton();
  }

  public StringSearchable getStringSearchable() {
    List<String> sourceOptions = new ArrayList<String>();

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
        sourceOptions.add(countryName + " (" + currency + ")");
      }
    }
    return new StringSearchable(sourceOptions);
  }

  /**
   * @return
   */
  private AutocompleteJComboBox getCurrencySearchableButton() {

    StringSearchable sourceSearchable = getStringSearchable();
    AutocompleteJComboBox sourceCombo = new AutocompleteJComboBox(sourceSearchable);
    return sourceCombo;
  }

  /** */
  public void launchConverterUI() {

    api = new currencyConverterAPI(apiKey, "PEN");
    frame = new JFrame("Currency Converter");
    frame.setSize(1000, 1500);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    backgroundColor = Color.decode("#1e1e2e");
    textColor = Color.decode("#cdd6f4");

    panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(backgroundColor);

    sourceCurrencyField = new JTextField("Enter source currency");
    sourceCurrencyField.setForeground(textColor);
    sourceCurrencyField.setBackground(backgroundColor);
    sourceCurrencyField.setEditable(false);

    targetCurrencyField = new JTextField("Enter target currency");
    targetCurrencyField.setForeground(textColor);
    targetCurrencyField.setBackground(backgroundColor);
    targetCurrencyField.setEditable(false);

    amountField = new JTextField("Enter amount");
    amountField.setForeground(textColor);
    amountField.setBackground(backgroundColor);

    convertButton = new JButton("Convert");
    convertButton.setForeground(textColor);
    convertButton.setBackground(backgroundColor);

    resultField = new JTextField("Result");
    resultField.setForeground(textColor);
    resultField.setBackground(backgroundColor);
    resultField.setEditable(false);

    panel.add(sourceCurrencyField);
    panel.add(sourceCombo);
    panel.add(targetCurrencyField);
    panel.add(targetCombo);
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
    api.setCurrencyBase(sourceCurrencyField.getText());
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
            .getAsJsonObject(targetCurrencyField.getText())
            .get("value")
            .getAsDouble();
    resultField.setText(String.valueOf(targetAmount));
  }
}
