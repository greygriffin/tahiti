package net.greenpoppy.tahiti.boat;

import lombok.Value;

import net.greenpoppy.tahiti.service.Update;


@Value
public class BoatUpdate {
    Update<String> name;
    Update<String> model;
    Update<String> registrationNumber;
    Update<Double> length;
    Update<Double> width;
    Update<Integer> ownerId;

    public boolean isEmpty() {
        return (name == null) &&
            (model == null) &&
            (registrationNumber == null) &&
            (length == null) &&
            (width == null) &&
            (ownerId == null);
    }
}
