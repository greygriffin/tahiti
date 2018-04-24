package net.greenpoppy.tahiti.berthassignment;

import javax.validation.constraints.NotNull;

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
}
