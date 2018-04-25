package net.greenpoppy.tahiti.boat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import net.greenpoppy.tahiti.resource.OnCreate;


@Builder
@Value
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoatDTO {

    @Null(groups = OnCreate.class)
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

    @Null(groups = OnCreate.class)
    String createdAt;

    @Null(groups = OnCreate.class)
    String updatedAt;

    @JsonCreator
    public BoatDTO(@JsonProperty("name") String name,
                   @JsonProperty("model") String model,
                   @JsonProperty("registrationNumber") String registrationNumber,
                   @JsonProperty("length") Double length,
                   @JsonProperty("width") Double width,
                   @JsonProperty("ownerId") Integer ownerId,
                   @JsonProperty("berthAssignmentId") Integer berthAssignmentId) {
        this.id = null;
        this.name = name;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.length = length;
        this.width = width;
        this.ownerId = ownerId;
        this.berthAssignmentId = berthAssignmentId;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
