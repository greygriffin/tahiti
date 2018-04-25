package net.greenpoppy.tahiti.club;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import net.greenpoppy.tahiti.resource.OnCreate;


@Value
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class ClubDTO {

    @Null(groups = OnCreate.class)
    Integer id;   // null when creating, not null in DB

    @NotBlank
    String name;

    @NotBlank
    String abbreviation;

    // Don't include a list of members, there are too many
    // Don't include a list of berths, there are too many

    @Null(groups = OnCreate.class)
    String createdAt;

    @Null(groups = OnCreate.class)
    String updatedAt;

    @JsonCreator
    public ClubDTO(@JsonProperty("name") String name,
                   @JsonProperty("abbreviation") String abbreviation) {
        this.id = null;
        this.name = name;
        this.abbreviation = abbreviation;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
