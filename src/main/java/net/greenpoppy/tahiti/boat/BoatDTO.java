package net.greenpoppy.tahiti.boat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;

import net.greenpoppy.tahiti.resource.OnUpdate;


@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoatDTO {

    @NotNull(groups = OnUpdate.class)
    Integer id;

    String name;

    String model;

    String registrationNumber;

    @Positive
    Double length;

    @Positive
    Double width;

    @NotNull
    Integer ownerId;

    Integer berthAssignmentId;
}
