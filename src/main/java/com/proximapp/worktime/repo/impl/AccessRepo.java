package com.proximapp.worktime.repo.impl;


import com.proximapp.worktime.repo.IAccessRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class AccessRepo implements IAccessRepo {

    private static String queryAddMovimento;

    @Override
    public void addMovimento(long userId, Timestamp data, boolean entrata) throws Exception {
        Connection connection = null;
        PreparedStatement addMovimento = null;

            connection = ConnectionPool.getConnection();
            addMovimento = connection.prepareStatement(queryAddMovimento);

            System.out.println("Eseguo connessione...");

            addMovimento.setLong(1, userId);
            addMovimento.setTimestamp(2,data);
            addMovimento.setBoolean(3,entrata);
            addMovimento.executeUpdate();

            System.out.println("Aggiunto " + userId + data + entrata);
    }

    @Override
    public void mostraMovimenti() throws Exception {

    }

    @Override
    public void mostraMovimentiById(long idUser) throws Exception {

    }

    @Override
    public void mostraMovimentiByData(Date data) throws Exception {

    }

    static{

        queryAddMovimento =  "INSERT INTO public.movimento(idutente, dataentrata, entrata) VALUES ( ? , ? ,?);";
        //queryFindMovimenti
        //queryFindMovimentiById
        //queryFindMovimentiByUser
    }

}
