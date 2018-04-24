package net.greenpoppy.tahiti.resource;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import net.greenpoppy.tahiti.service.BaseService;
import net.greenpoppy.tahiti.service.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


public class BaseResource {
    protected static final String API_PREFIX = "/api";
    protected static final MediaType APPLICATION_MERGE_PATCH_JSON_UTF8 =
        new MediaType("application", "merge-patch+json", StandardCharsets.UTF_8);
    protected static final String APPLICATION_MERGE_PATCH_JSON_UTF8_VALUE =
        "application/merge-patch+json; charset=utf-8";

    protected static final String NOT_FOUND = "not found";
    protected static final String INVALID_DATA = "invalid data";
    protected static final String INCOMPLETE_DATA = "incomplete data";


    protected Update<Boolean> getBooleanUpdate(Map<String, Object> data, String key) {
        if (data.containsKey(key)) {
            return new Update<>((Boolean) data.get(key));
        } else {
            return null;
        }
    }

    protected Update<String> getStringUpdate(Map<String, Object> data, String key) {
        if (data.containsKey(key)) {
            return new Update<>((String) data.get(key));
        } else {
            return null;
        }
    }

    protected Update<Integer> getIntegerUpdate(Map<String, Object> data, String key) {
        if (data.containsKey(key)) {
            return new Update<>((Integer) data.get(key));
        } else {
            return null;
        }
    }

    protected Update<Double> getDoubleUpdate(Map<String, Object> data, String key) {
        if (data.containsKey(key)) {
            //TODO: Figure out how to handle values which are not Doubles, e.g. "hello"
            return new Update<>((Double) data.get(key));
        } else {
            return null;
        }
    }

    protected ResourceException createNotFoundResourceException(String what, Integer id) {
        return new ResourceException(
            HttpStatus.NOT_FOUND,
            NOT_FOUND,
            what + " with id '" + id + "' not found");
    }

    protected ResourceException createIncompleteDataResourceException(BaseService.IncompleteDataException ex) {
        return new ResourceException(
            HttpStatus.BAD_REQUEST,
            INCOMPLETE_DATA,
            "Missing values in create request",
            ex);
    }

    protected ResourceException createInvalidValuesResourceException(BaseService.InvalidValueException ex) {
        String message;
        if (ex.getValueString() == null) {
            message = "Invalid values";
        } else {
            message = "Invalid values: { " + ex.getValueString() + " }";
        }
        return new ResourceException(
            HttpStatus.BAD_REQUEST,
            INVALID_DATA,
            message,
            ex);
    }
}
