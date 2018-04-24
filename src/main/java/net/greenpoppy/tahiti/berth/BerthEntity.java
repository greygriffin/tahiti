package net.greenpoppy.tahiti.berth;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;

import net.greenpoppy.tahiti.berthassignment.BerthAssignmentEntity;
import net.greenpoppy.tahiti.club.ClubEntity;


@Entity
@Table(name="berths")
@Data
@Builder
public class BerthEntity
    implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @NotBlank
    String name;

    @OneToOne(mappedBy = "berth", cascade = CascadeType.ALL, orphanRemoval = true)
    BerthAssignmentEntity berthAssignment;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn
    ClubEntity club;
}
