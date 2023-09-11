package com.conversor.searchableComboBox;

import java.util.Collection;

/**
 * @param <E>
 * @param <V>
 */
public interface Searchable<E, V> {
  public Collection<E> search(V value);
}
