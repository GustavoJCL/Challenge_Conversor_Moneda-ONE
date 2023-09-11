package com.conversor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CountryCurrencyAPI {
  private static final String API_URL = "https://restcountries.com/v3.1/all";
  private HttpURLConnection conn;

  public CountryCurrencyAPI() {
    URL url;
    try {
      url = new URL(API_URL);
      conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public JsonArray callAPI() throws NullPointerException, IOException {
    JsonArray resultArray = new JsonArray();

    BufferedReader br =
        new BufferedReader(new InputStreamReader((conn.getInputStream())));

    Gson gson = new Gson();
    JsonObject[] jsonArray = gson.fromJson(br, JsonObject[].class);
    for (JsonObject jsonObject : jsonArray) {
      JsonObject countryObject = new JsonObject();
      countryObject.addProperty(
          "name",
          jsonObject.getAsJsonObject("name").get("common").getAsString());

      JsonObject currencies = jsonObject.getAsJsonObject("currencies");
      countryObject.add("currencies", currencies);
      resultArray.add(countryObject);
    }
    // Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    // String prettyJson = prettyGson.toJson(resultArray);
    // System.out.println(prettyJson);

    conn.disconnect();
    return resultArray;
  }
}
