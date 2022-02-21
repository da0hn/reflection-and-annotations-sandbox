package org.gabriel.sandbox.rene.orm;

import java.util.Optional;

interface ReneGetByIdOperation<T> {
  Optional<T> execute(Class<T> aClass, Long id);
}
