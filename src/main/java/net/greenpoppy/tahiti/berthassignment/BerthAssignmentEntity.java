package net.greenpoppy.tahiti.berthassignment;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.*;

import net.greenpoppy.tahiti.berth.BerthEntity;
import net.greenpoppy.tahiti.boat.BoatEntity;
import net.greenpoppy.tahiti.entity.BaseEntity;
import net.greenpoppy.tahiti.member.MemberEntity;


@Entity
@Table(name="berth_assignments", uniqueConstraints={@UniqueConstraint(columnNames = {"boat_id" , "berth_id"}),
                                                    @UniqueConstraint(columnNames = {"berth_id", "owner_id"})})
@Data
@EqualsAndHashCode(callSuper=true)
@Builder
public class BerthAssignmentEntity
    extends BaseEntity
    implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @OneToOne
    @JoinColumn(unique = true)
    BoatEntity boat;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    BerthEntity berth;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn
    MemberEntity owner;

    @Column(nullable = false)
    @NotNull
    Boolean guardDuty;
}
