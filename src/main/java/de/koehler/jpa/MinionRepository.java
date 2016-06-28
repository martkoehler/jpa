package de.koehler.jpa;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

public class MinionRepository {
    private EntityManager entityManager;

    public MinionRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Minion find(final String name) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Minion> query = cb.createQuery(Minion.class);

        Root<Minion> root = query.from(Minion.class);
        root.fetch(Minion_.boss, JoinType.LEFT);

        query
                .select(root)
                .where(cb.equal(root.get(Minion_.name), name));

        TypedQuery<Minion> emQuery = entityManager.createQuery(query);

        return emQuery.getSingleResult();

    }

}
