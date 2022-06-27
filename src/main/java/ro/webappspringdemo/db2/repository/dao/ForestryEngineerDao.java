package ro.webappspringdemo.db2.repository.dao;

import ro.webappspringdemo.db2.entities.AllClientsEntity;
import ro.webappspringdemo.db2.model.forestryEngineer.AllClients;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ForestryEngineerDao {
    List<AllClients> getAllClients();

    List<AllClients> getTodayExpire();

    List<AllClients> getExpireByGivenDate(Date date);

    void insertObject(Object object);

    BigDecimal requestId();

    Object getObjectById(String id, Class objectClass);

    void updateObject(Object object);

    void insertClientsFromExcel(List<AllClientsEntity> list);

    List<AllClients> getAniiRestanta(String cnp);
}
