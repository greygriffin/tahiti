package net.greenpoppy.tahiti.berth;

import lombok.Value;

import net.greenpoppy.tahiti.service.Update;


@Value
public class BerthUpdate {
    Update<String> name;
    Update<Integer> clubId;

    public boolean isEmpty() {
        return (name == null) && (clubId == null);
    }
}
