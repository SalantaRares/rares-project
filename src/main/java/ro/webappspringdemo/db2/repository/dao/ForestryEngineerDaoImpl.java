package ro.webappspringdemo.db2.repository.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.webappspringdemo.db2.entities.AllClientsEntity;
import ro.webappspringdemo.db2.model.forestryEngineer.AllClients;
import ro.webappspringdemo.db2.repository.query.ForestryEngineerQueries;
import ro.webappspringdemo.utils.UtilsRepository;
import ro.webappspringdemo.utils.hibernate.CustomParameter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
@Transactional(timeout = 300)
public class ForestryEngineerDaoImpl implements ForestryEngineerDao {
    @Autowired
    SessionFactory sessionFactory;

    public ForestryEngineerDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<AllClients> getAllClients() {
        return UtilsRepository.getDataAsListByAliasBean(sessionFactory, ForestryEngineerQueries.GET_ALL_INFO, AllClients.class);
    }

    public List<AllClients> getTodayExpire() {
        return UtilsRepository.getDataAsListByAliasBean(sessionFactory, ForestryEngineerQueries.GET_EXPIRY_TODAY_DATE, AllClients.class);
    }

    @Override
    public List<AllClients> getExpireByGivenDate(Date date) {
        List<CustomParameter> list = new ArrayList<>();
        list.add(new CustomParameter(ForestryEngineerQueries.P_DATE, date, Date.class));
        return UtilsRepository.getDataAsListByAliasBeanWithParams(sessionFactory, ForestryEngineerQueries.GET_EXPIRY_BY_GIVEN_DATE, AllClients.class, list);
    }

    @Override
    public void insertObject(Object object) {
        try {
            UtilsRepository.create(sessionFactory, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BigDecimal requestId() {
        return UtilsRepository.getNextValue(sessionFactory, ForestryEngineerQueries.SEQ_LOGS_UTILIZATORI_RARES);
    }

    @Override
    public Object getObjectById(String id, Class objectClass) {
        return UtilsRepository.getObjectById(sessionFactory, objectClass, id);
    }

    @Override
    public void updateObject(Object object) {
        UtilsRepository.update(sessionFactory, object);
    }

    @Override
    public void insertClientsFromExcel(List<AllClientsEntity> list) {
        try {
            for (AllClientsEntity imp : list) {
                UtilsRepository.create(sessionFactory, imp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AllClients> getAniiRestanta(String cnp) {
        List<CustomParameter> list = new ArrayList<>();
        list.add(new CustomParameter(ForestryEngineerQueries.P_CNP, cnp, String.class));
        return UtilsRepository.getDataAsListByAliasBeanWithParams(sessionFactory, ForestryEngineerQueries.GET_ANII_RESTANTA, AllClients.class, list);
    }
}
