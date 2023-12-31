package com.conversor.comboItem;

/** ComboItem */
public class ComboItem {
  private String name;
  private String id;

  public ComboItem(String name, String id) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return name;
  }
}
