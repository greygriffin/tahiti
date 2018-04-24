package net.greenpoppy.tahiti.club;

import lombok.Value;

import net.greenpoppy.tahiti.service.Update;


@Value
public class ClubUpdate {
    Update<String> name;
    Update<String> abbreviation;

    public boolean isEmpty() {
        return (name == null) && (abbreviation == null);
    }
}
