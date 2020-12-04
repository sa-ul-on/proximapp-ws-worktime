package com.proximapp.worktime.repo;

import java.sql.SQLException;

public interface IAccessRepo {
    void addMovimento(long userId, String data, boolean entrata) throws SQLException;

}
