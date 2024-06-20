package br.ufpr.tads.msbantadsmanager.core.port;

import java.util.List;
import java.util.Optional;

public interface CrudOperations<T, K> {
  Optional<T> findById(K id);

  List<T> findAll();

  T create(T model);

  T update(T model);

  void delete(K id);
}
