package net.greenpoppy.tahiti.club;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import lombok.*;

import net.greenpoppy.tahiti.berth.BerthEntity;
import net.greenpoppy.tahiti.member.MemberEntity;


@Entity
@Table(name="clubs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubEntity
    implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String abbreviation;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MemberEntity> members;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    List<BerthEntity> berths;
}
