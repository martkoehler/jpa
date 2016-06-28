package de.koehler.jpa;

import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * Created by mart on 22.08.15.
 */
@Entity
@Table(name = "MINION")
@Audited
public class Minion {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 35)
    private String name;

    @Column(name="AGE", nullable = false)
    private int age;

    @ManyToOne
    @JoinColumn(name = "BOSS_ID", referencedColumnName = "ID")
    private Boss boss;

    public void follow(final Boss boss) {
        this.boss = boss;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }

    public Boss getBoss() {
        return boss;
    }
}
