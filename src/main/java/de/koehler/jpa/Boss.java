package de.koehler.jpa;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mart on 22.08.15.
 */
@Entity
@Table(name = "BOSS")
@Audited
public class Boss {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "boss", cascade = CascadeType.PERSIST)
    private List<Minion> minions = new ArrayList<>();

    public void recruit(final Minion minion) {
        this.minions.add(minion);
        minion.follow(this);
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public List<Minion> getMinions() {
        return minions;
    }

}
