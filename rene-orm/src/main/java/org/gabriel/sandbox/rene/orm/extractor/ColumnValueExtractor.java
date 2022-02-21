package org.gabriel.sandbox.rene.orm.extractor;


import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ColumnValueExtractor {

  private static final Map<Class<?>, ColumnValueTypeExtractor<?>> extractors = new HashMap<>();

  static {
    extractors.put(Integer.class, new IntegerExtractor());
    extractors.put(String.class, new StringExtractor());
    extractors.put(Long.class, new LongExtractor());
    extractors.put(Double.class, new DoubleExtractor());
    extractors.put(Float.class, new FloatExtractor());
  }

  @SuppressWarnings("unchecked")
  public static <T> ColumnValueTypeExtractor<T> getExtractor(final Class<T> aClass) {
    return (ColumnValueTypeExtractor<T>) extractors.get(aClass);
  }

  static class IntegerExtractor implements ColumnValueTypeExtractor<Integer> {
    @Override public Function<Field, Integer> extract(final ResultSet resultSet) {
      return field -> {
        try {
          return resultSet.getInt(field.getName());
        }
        catch(final SQLException e) {
          e.printStackTrace();
        }
        return null;
      };
    }
  }

  static class StringExtractor implements ColumnValueTypeExtractor<String> {
    @Override public Function<Field, String> extract(final ResultSet resultSet) {
      return field -> {
        try {
          return resultSet.getString(field.getName());
        }
        catch(final SQLException e) {
          e.printStackTrace();
        }
        return null;
      };
    }
  }

  static class LongExtractor implements ColumnValueTypeExtractor<Long> {
    @Override public Function<Field, Long> extract(final ResultSet resultSet) {
      return field -> {
        try {
          return resultSet.getLong(field.getName());
        }
        catch(final SQLException e) {
          e.printStackTrace();
        }
        return null;
      };
    }
  }

  static class DoubleExtractor implements ColumnValueTypeExtractor<Double> {
    @Override public Function<Field, Double> extract(final ResultSet resultSet) {
      return field -> {
        try {
          return resultSet.getDouble(field.getName());
        }
        catch(final SQLException e) {
          e.printStackTrace();
        }
        return null;
      };
    }
  }

  static class FloatExtractor implements ColumnValueTypeExtractor<Float> {
    @Override public Function<Field, Float> extract(final ResultSet resultSet) {
      return field -> {
        try {
          return resultSet.getFloat(field.getName());
        }
        catch(final SQLException e) {
          e.printStackTrace();
        }
        return null;
      };
    }
  }
}
