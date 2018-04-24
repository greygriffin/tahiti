package net.greenpoppy.tahiti.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class BaseService {

    protected static Boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    protected static Boolean isBlank(String str) {
        return str.isEmpty() || str.trim().isEmpty();
    }

    protected static Boolean isPositive(Double value) {
        return value >= 0.0;
    }

    protected static Boolean isNotNull(Object obj) {
        return obj != null;
    }

    protected <T> Stream<T> getStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    protected <T> void validateMandatoryCreateValue(String name, T value, Function<T, Boolean> validator, Map<String, Object> createErrors) {
        if ((value == null) || !validator.apply(value)) {
            createErrors.put(name, value);
        }
    }

    protected <T> void validateOptionalCreateValue(String name, T value, Function<T, Boolean> validator, Map<String, Object> createErrors) {
        if ((value != null) && !validator.apply(value)) {
            createErrors.put(name, value);
        }
    }

    protected <T> void validateUpdateValue(String name, Update<T> update, Function<T, Boolean> validator, Map<String, Object> updateErrors) {
        if (update != null) {
            T value = update.getValue();
            if ((value == null) || !validator.apply(value)) {
                updateErrors.put(name, value);
            }
        }
    }

    public class NotFoundException
        extends Exception {
        private final int id;

        public NotFoundException(int id) {
            super("Not found, id: " + id);
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "id: " + id + "; " + super.toString();
        }
    }

    public static class IncompleteDataException
        extends Exception {
    }

    public static class InvalidValueException
        extends Exception {
        private final Map<String, Object> values;

        public InvalidValueException(Map<String, Object> values) {
            super("Invalid values");
            this.values = values;
        }

        public InvalidValueException(Throwable cause) {
            super("Invalid values", cause);
            values = null;
        }

        public Map<String, Object> getValues() {
            return values;
        }

        public String getValueString() {
            if (values == null) {
                return null;
            } else {
                List<String> parts = new ArrayList<>();
                for (String key : values.keySet()) {
                    parts.add(key + ": " + values.get(key));
                }
                return String.join("; ", parts);
            }
        }

        @Override
        public String toString() {
            String valueString = getValueString();
            if (valueString == null) {
                return super.toString();
            } else {
                return "values: {" + valueString + "}; " + super.toString();
            }
        }
    }
}
