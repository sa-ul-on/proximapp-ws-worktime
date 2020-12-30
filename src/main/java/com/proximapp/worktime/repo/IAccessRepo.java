package com.proximapp.worktime.repo;

import java.util.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

public interface IAccessRepo {
    void addMovimento(long userId, Timestamp data, boolean entrata) throws Exception;
    void mostraMovimenti() throws Exception;
    void mostraMovimentiById(long idUser) throws Exception;
    void mostraMovimentiByData(Date data) throws  Exception;

}
