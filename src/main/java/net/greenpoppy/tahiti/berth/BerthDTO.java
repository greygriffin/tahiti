package net.greenpoppy.tahiti.berth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import net.greenpoppy.tahiti.resource.OnCreate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;


@Builder
@Value
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BerthDTO {

    @Null(groups = OnCreate.class)
    Integer id;

    @NotBlank
    String name;

    Integer berthAssignmentId;

    Integer clubId;

    @Null(groups = OnCreate.class)
    String createdAt;

    @Null(groups = OnCreate.class)
    String updatedAt;

    @JsonCreator
    public BerthDTO(@JsonProperty("name") String name,
                    @JsonProperty("berthAssignmentId") Integer berthAssignmentId,
                    @JsonProperty("clubId") Integer clubId) {
        this.id = null;
        this.name = name;
        this.berthAssignmentId = berthAssignmentId;
        this.clubId = clubId;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
