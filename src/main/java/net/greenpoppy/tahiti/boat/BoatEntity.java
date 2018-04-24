package net.greenpoppy.tahiti.boat;

import java.io.Serializable;
import javax.persistence.*;

import lombok.*;

import net.greenpoppy.tahiti.berthassignment.BerthAssignmentEntity;
import net.greenpoppy.tahiti.member.MemberEntity;


@Entity
@Table(name="boats")
@Data
@Builder
public class BoatEntity
    implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String name;

    @Column
    private String model;

    @Column(unique = true)
    String registrationNumber;

    @Column
    Double length;

    @Column
    Double width;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn
    MemberEntity owner;

    @OneToOne(mappedBy = "boat", cascade = CascadeType.ALL, orphanRemoval = true)
    BerthAssignmentEntity berthAssignment;
}
