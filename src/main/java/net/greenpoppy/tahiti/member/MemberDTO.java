package net.greenpoppy.tahiti.member;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import net.greenpoppy.tahiti.resource.OnCreate;
import net.greenpoppy.tahiti.resource.OnUpdate;


@Builder
@Value
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberDTO {

    @Null(groups = OnCreate.class)
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

    @Null(groups = OnCreate.class)
    String createdAt;

    @Null(groups = OnCreate.class)
    String updatedAt;

    @JsonCreator
    public MemberDTO(@JsonProperty("firstName") String firstName,
                     @JsonProperty("lastName") String lastName,
                     @JsonProperty("streetAddress") String streetAddress,
                     @JsonProperty("postCode") String postCode,
                     @JsonProperty("city") String city,
                     @JsonProperty("country") String country,
                     @JsonProperty("phoneNumber") String phoneNumber,
                     @JsonProperty("email") String email,
                     @JsonProperty("password") String password,
                     @JsonProperty("clubId") Integer clubId) {
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.postCode = postCode;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.clubId = clubId;
        this.boatIds = new ArrayList<>();
        this.berthAssignmentIds = new ArrayList<>();
        this.createdAt = null;
        this.updatedAt = null;
    }
}
