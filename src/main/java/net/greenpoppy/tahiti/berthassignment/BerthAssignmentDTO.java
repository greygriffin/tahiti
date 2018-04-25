package net.greenpoppy.tahiti.berthassignment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import net.greenpoppy.tahiti.resource.OnCreate;


@Builder
@AllArgsConstructor
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class BerthAssignmentDTO {

    @Null(groups = OnCreate.class)
    Integer id;

    Integer boatId;

    @NotNull
    Integer berthId;

    @NotNull
    Integer ownerId;

    @NotNull
    Boolean guardDuty;

    @Null(groups = OnCreate.class)
    String createdAt;

    @Null(groups = OnCreate.class)
    String updatedAt;

    @JsonCreator
    public BerthAssignmentDTO(@JsonProperty("boatId") Integer boatId,
                              @JsonProperty("berthId") Integer berthId,
                              @JsonProperty("ownerId") Integer ownerId,
                              @JsonProperty("guardDuty") Boolean guardDuty) {
        this.id = null;
        this.boatId = boatId;
        this.berthId = berthId;
        this.ownerId = ownerId;
        this.guardDuty = guardDuty;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
