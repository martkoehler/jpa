package de.koehler.jpa;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by mart on 24.08.15.
 */
public class BossRepositoryTest extends AbstractJpaTest {

    private BossRepository repository;

    @Override
    protected IDataSet getDataSet() throws IOException, DataSetException {
        return new FlatXmlDataSet(getClass().getResourceAsStream("/dataset.xml"));
    }


    @Before
    public void init() {
        repository = new BossRepository(entityManager);
    }

    @Test
    public void findBoss() {
        List<Boss> bosses = repository.findAll();

        for(Boss boss : bosses)
        {
            for(Minion minion : boss.getMinions()) {
                assertThat(minion.getName(), notNullValue());
            }
        }
    }

    @Test
    public void findMultiselectBoss() {
        List<Boss> bosses = repository.findMultiSelect();

        for(Boss boss : bosses)
        {
            for(Minion minion : boss.getMinions()) {
                assertThat(minion.getName(), notNullValue());
            }
        }
    }

    @Test
    public void findBossNPlusOne() {
        List<Boss> bosses = repository.findNPlusOne();

        for(Boss boss : bosses)
        {
            for(Minion minion : boss.getMinions()) {
                assertThat(minion.getName(), notNullValue());
            }
        }
    }

    @Test
    public void checkAuditTable() {
        entityManager.getTransaction().begin();
        Boss boss = new Boss();
        Minion minion = new Minion();
        boss.recruit(minion);
        entityManager.persist(boss);
        entityManager.getTransaction().commit();

        List<Boss> auditBoss = AuditReaderFactory.get(entityManager).createQuery().forRevisionsOfEntity(Boss.class, true, true).getResultList();
        assertThat(auditBoss, notNullValue());
        assertThat(auditBoss.isEmpty(), is(false));

        List<Minion> auditMinion = AuditReaderFactory.get(entityManager).createQuery().forRevisionsOfEntity(Minion.class, true, true).getResultList();
        assertThat(auditMinion, notNullValue());
        assertThat(auditMinion.isEmpty(), is(false));
    }

}
