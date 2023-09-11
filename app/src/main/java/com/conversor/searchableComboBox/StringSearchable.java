package com.conversor.searchableComboBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringSearchable implements Searchable<String, String> {

  private Trie termsTrie = new Trie();
  private List<String> termsList = new ArrayList<>();

  public StringSearchable(List<String> terms) {
    for (String term : terms) {
      this.termsTrie.insert(term);
      this.termsList.add(term);
    }
  }

  @Override
  public Collection<String> search(String value) {
    List<String> founds = new ArrayList<String>();
    if (termsTrie.startsWith(value)) {
      for (String term : termsList) {
        if (term.startsWith(value)) {
          founds.add(term);
        }
      }
    }
    return founds;
  }
}
