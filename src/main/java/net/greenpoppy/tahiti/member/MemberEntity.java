package net.greenpoppy.tahiti.member;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import lombok.*;

import net.greenpoppy.tahiti.berthassignment.BerthAssignmentEntity;
import net.greenpoppy.tahiti.boat.BoatEntity;
import net.greenpoppy.tahiti.club.ClubEntity;


@Entity
@Table(name="users")
@Data
@Builder
public class MemberEntity
    implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Column
    String streetAddress;

    @Column
    String postCode;

    @Column
    String city;

    @Column
    String country;

    @Column
    String phoneNumber;

    @Column(nullable = false, unique = true)
    String email;   // == username, hence unique

    @Column(nullable = false)
    String password;  // stored one-way-encoded

    @ManyToOne(optional=false)     // Should really be many-to-many
    @JoinColumn
    ClubEntity club;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    List<BoatEntity> boats;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    List<BerthAssignmentEntity> berthAssignments;
}
