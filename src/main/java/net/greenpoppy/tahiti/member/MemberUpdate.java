package net.greenpoppy.tahiti.member;

import lombok.Value;

import net.greenpoppy.tahiti.service.Update;


@Value
public class MemberUpdate {
    Update<String> firstName;
    Update<String> lastName;
    Update<String> streetAddress;
    Update<String> postCode;
    Update<String> city;
    Update<String> country;
    Update<String> phoneNumber;
    Update<String> email;
    Update<String> password;
    Update<Integer> clubId;

    public boolean isEmpty() {
        return (firstName == null) &&
            (lastName == null) &&
            (streetAddress == null) &&
            (postCode == null) &&
            (city == null) &&
            (country != null) &&
            (phoneNumber == null) &&
            (email == null) &&
            (password == null) &&
            (clubId == null);
    }
}
