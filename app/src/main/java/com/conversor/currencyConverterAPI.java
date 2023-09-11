package com.conversor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/** currencyConverterAPI */
public class currencyConverterAPI {
  private String apiKey;
  private String currencyBase;
  private String route;
  private HttpURLConnection request;

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getCurrencyBase() {
    return currencyBase;
  }

  public void setCurrencyBase(String currencyBase) {
    this.currencyBase = currencyBase;
  }

  public String getRoute() {
    return route;
  }

  /**
   * @param apiKey
   * @param currencyBase
   */
  public currencyConverterAPI(String apiKey, String currencyBase) {
    this.currencyBase = currencyBase;
    this.apiKey = apiKey;
    request = null;
  }

  /**
   * @throws IOException
   * @throws URISyntaxException
   */
  public void connect() throws IOException, URISyntaxException {
    this.route = "https://api.currencyapi.com/v3/latest?apikey="
        + URLEncoder.encode(this.apiKey, StandardCharsets.UTF_8)
        + "&base_currency="
        + URLEncoder.encode(this.currencyBase, StandardCharsets.UTF_8);
    URL url;

    try {
      url = new URI(route).toURL();
      request = (HttpURLConnection) url.openConnection();
      request.setConnectTimeout(5000);
      request.setReadTimeout(5000);
      request.connect();
    } catch (IOException | URISyntaxException e) {
      System.err.println("An error ocurred while connecting to the API" + e.getMessage());
    }
  }

  /**
   * @return
   */
  public JsonObject getResponse() {
    JsonElement root;
    try {
      root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
      return root.getAsJsonObject();
    } catch (IOException e) {
      throw new RuntimeException("Error reading request content", e);
    }
  }
}
