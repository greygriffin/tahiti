package net.greenpoppy.tahiti.club;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import net.greenpoppy.tahiti.resource.OnUpdate;


@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
class ClubDTO {

    @NotNull(groups = OnUpdate.class)
    Integer id;   // null when creating, not null in DB

    @NotBlank
    String name;

    @NotBlank
    String abbreviation;

    // Don't include a list of members, there are too many
    // Don't include a list of berths, there are too many

    @JsonCreator
    public ClubDTO(@JsonProperty("id") Integer id,
                   @JsonProperty("name") String name,
                   @JsonProperty("abbreviation") String abbreviation) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
    }
}
