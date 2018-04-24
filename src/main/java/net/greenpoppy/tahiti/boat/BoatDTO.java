package net.greenpoppy.tahiti.boat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public BoatDTO(@JsonProperty("id") Integer id,
                   @JsonProperty("name") String name,
                   @JsonProperty("model") String model,
                   @JsonProperty("registrationNumber") String registrationNumber,
                   @JsonProperty("length") Double length,
                   @JsonProperty("width") Double width,
                   @JsonProperty("ownerId") Integer ownerId,
                   @JsonProperty("berthAssignmentId") Integer berthAssignmentId) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.length = length;
        this.width = width;
        this.ownerId = ownerId;
        this.berthAssignmentId = berthAssignmentId;
    }
}
