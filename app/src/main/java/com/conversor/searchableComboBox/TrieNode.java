package com.conversor.searchableComboBox;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
  Map<Character, TrieNode> children = new HashMap<>();
  boolean endOfWord;
}
