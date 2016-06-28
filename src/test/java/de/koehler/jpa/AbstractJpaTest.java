package de.koehler.jpa;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.hibernate.internal.SessionImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by mart on 22.08.15.
 */
public abstract class AbstractJpaTest {

    private static final String PERSISTENCE_UNIT_NAME = "testUnit";

    protected EntityManager entityManager;
    private EntityManagerFactory factory;

    @Before
    public void importDataSet() throws Exception {
        this.factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        entityManager = factory.createEntityManager();
        DatabaseConnection connection = new DatabaseConnection(((SessionImpl) (entityManager.getDelegate())).connection());
        IDataSet dataSet = getDataSet();
        cleanlyInsert(connection, dataSet);
    }

    @After
    public void cleaUp() {
        this.entityManager.close();
    }

    protected abstract IDataSet getDataSet() throws IOException, DataSetException;


    private void cleanlyInsert(DatabaseConnection connection, IDataSet dataSet) throws Exception {
        IDataSet cleanupDataSet = new FlatXmlDataSet(getClass().getResourceAsStream("/empty.xml"));
        DatabaseOperation.DELETE_ALL.execute(connection, cleanupDataSet);
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
    }



}
