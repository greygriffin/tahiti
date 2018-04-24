package net.greenpoppy.tahiti.berth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;


@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class BerthDTO {

    Integer id;

    @NotBlank
    String name;

    Integer berthAssignmentId;

    Integer clubId;

    @JsonCreator
    public BerthDTO(@JsonProperty("id") Integer id,
                    @JsonProperty("name") String name,
                    @JsonProperty("berthAssignmentId") Integer berthAssignmentId,
                    @JsonProperty("clubId") Integer clubId) {
        this.id = id;
        this.name = name;
        this.berthAssignmentId = berthAssignmentId;
        this.clubId = clubId;
    }
}
