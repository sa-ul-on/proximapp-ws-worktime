package com.proximapp.worktime.repo.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {

	public static Connection getConnection() {
		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:postgresql://kandula.db.elephantsql.com:5432/puznfhit", "puznfhit", "oiiIvdKt-U81bsaj1TdiUOZ8QxIjb1sM");
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}



