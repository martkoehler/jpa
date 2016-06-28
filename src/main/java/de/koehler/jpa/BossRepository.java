package de.koehler.jpa;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by mart on 24.08.15.
 */
public class BossRepository {
    private EntityManager entityManager;

    public BossRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<Boss> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Boss> query = cb.createQuery(Boss.class);

        Root<Boss> root = query.from(Boss.class);

        root.fetch(Boss_.minions, JoinType.LEFT);

        query.select(root).distinct(true);

        TypedQuery<Boss> emQuery = entityManager.createQuery(query);

        return emQuery.getResultList();

    }

    public List<Boss> findMultiSelect() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Boss> root = query.from(Boss.class);
        ListJoin<Boss, Minion> minionJoin = root.join(Boss_.minions);
        query.multiselect(root, minionJoin);

        TypedQuery<Tuple> emQuery = entityManager.createQuery(query);
        List<Tuple> result = emQuery.getResultList();

        List<Boss> bosses = new ArrayList<>();
        for(Tuple tuple: result) {
            bosses.add((Boss) tuple.get(0));
        }
        return bosses;

    }

    public List<Boss> findNPlusOne() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Boss> query = cb.createQuery(Boss.class);

        Root<Boss> root = query.from(Boss.class);
        query.select(root);

        TypedQuery<Boss> emQuery = entityManager.createQuery(query);

        return emQuery.getResultList();

    }

    public static void main(String[] args) {
        File dir = new File("/Users/mart/Desktop/test/");
        File[] files = dir.listFiles();

        if(files == null) {
            System.out.println("got null");
        } else {
            System.out.println("got " + files.length);
        }


        try (Stream<java.nio.file.Path> list = Files.list(dir.toPath()).filter(path -> path.startsWith("test"))) {
        } catch (IOException e) {
        }
    }
}
