package net.greenpoppy.tahiti.berthassignment;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import net.greenpoppy.tahiti.resource.OnUpdate;


@Builder
@Value
public class BerthAssignmentDTO {

    @NotNull(groups = OnUpdate.class)
    Integer id;

    Integer boatId;

    @NotNull
    Integer berthId;

    @NotNull
    Integer ownerId;

    @NotNull
    Boolean guardDuty;

    @JsonCreator
    public BerthAssignmentDTO(@JsonProperty("id") Integer id,
                              @JsonProperty("boatId") Integer boatId,
                              @JsonProperty("berthId") Integer berthId,
                              @JsonProperty("ownerId") Integer ownerId,
                              @JsonProperty("guardDuty") Boolean guardDuty) {
        this.id = id;
        this.boatId = boatId;
        this.berthId = berthId;
        this.ownerId = ownerId;
        this.guardDuty = guardDuty;
    }
}
