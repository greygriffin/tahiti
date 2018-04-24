package net.greenpoppy.tahiti.member;

import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;

import net.greenpoppy.tahiti.resource.OnUpdate;


@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberDTO {

    @NotNull(groups = OnUpdate.class)
    Integer id;

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    String streetAddress;

    String postCode;

    String city;

    String country;

    String phoneNumber;

    @NotNull
    @Email
    String email;   // == username

    @NotBlank
    String password;

    @NotNull
    Integer clubId;

    List<Integer> boatIds;

    List<Integer> berthAssignmentIds;
}
