package net.greenpoppy.tahiti.berth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
}
