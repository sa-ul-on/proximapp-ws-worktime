package com.proximapp.worktime.repo.impl;


import com.proximapp.worktime.entity.Movimento;
import com.proximapp.worktime.repo.IAccessRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccessRepo implements IAccessRepo {

    private static String queryAddMovimento;

    public void addMovimento(long userId, String data, boolean entrata) throws SQLException {
        Connection connection = null;
        PreparedStatement addMovimento = null;

            connection = ConnectionPool.getConnection();
            addMovimento = connection.prepareStatement(queryAddMovimento);

            System.out.println("Eseguo connessione...");

            addMovimento.setLong(1, userId);
            addMovimento.setString(2,data);
            addMovimento.setBoolean(3,entrata);
            addMovimento.executeUpdate();

            System.out.println("Aggiunto " + userId + data + entrata);
    }
    static{

        queryAddMovimento =  "INSERT INTO public.movimento(idutente, dataentrata, entrata) VALUES ( ? , ? ,?);";
    }

}
