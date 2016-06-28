package de.koehler.jpa;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MinionRepositoryTest extends AbstractJpaTest {
    @Override
    protected IDataSet getDataSet() throws IOException, DataSetException {
        return new FlatXmlDataSet(getClass().getResourceAsStream("/dataset.xml"));
    }

    @Test
    public void findBob() throws Exception {
        Minion bob = entityManager.find(Minion.class, 1L);
        assertThat(bob.getName(), is("Bob"));
    }

    @Test
    public void findBobReference() throws Exception {
        Minion bob = entityManager.getReference(Minion.class, 1L);
        assertThat(bob.getName(), is("Bob"));
    }

    @Test
    public void findBobRepository() throws Exception {
        MinionRepository minionRepository = new MinionRepository(entityManager);
        Minion bob = minionRepository.find("Bob");
        assertThat(bob.getName(), is("Bob"));
        assertThat(bob.getBoss().getName(), is("Gru"));
    }

    @Test
    public void findKevinRepository() throws Exception {
        MinionRepository minionRepository = new MinionRepository(entityManager);
        Minion kevin = minionRepository.find("Kevin");
        assertThat(kevin.getName(), is("Kevin"));
        assertThat(kevin.getBoss(), notNullValue());
    }

    @Test
    public void followGru() throws Exception {
        MinionRepository minionRepository = new MinionRepository(entityManager);
        entityManager.getTransaction().begin();

        Minion kevin = minionRepository.find("Kevin");
        assertThat(kevin.getName(), is("Kevin"));
        assertThat(kevin.getBoss(), notNullValue());

        Boss gru = entityManager.find(Boss.class, 1L);

        kevin.follow(gru);

        entityManager.getTransaction().commit();

        kevin = minionRepository.find("Kevin");
        assertThat(kevin.getName(), is("Kevin"));
        assertThat(kevin.getBoss(), is(gru));
    }
}
