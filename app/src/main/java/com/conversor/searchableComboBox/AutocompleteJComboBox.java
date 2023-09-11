package com.conversor.searchableComboBox;

import java.util.Set;
import java.util.TreeSet;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

public class AutocompleteJComboBox extends JComboBox<String> {
  private final Searchable<String, String> searchable;

  public AutocompleteJComboBox(Searchable<String, String> searchable) {
    super();
    this.searchable = searchable;
  }

  public void update() {
    SwingUtilities.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            String searchText = getEditor().getItem().toString().toLowerCase();
            Set<String> founds = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            founds.addAll(searchable.search(searchText));
            setEditable(false);
            try {
              removeAllItems();
              if (!founds.contains(searchText)) {
                addItem(searchText);
              }
              for (String s : founds) {
                addItem(s);
              }
              setPopupVisible(true);
            } finally {
              setEditable(true);
            }
          }
        });
  }
}
