package com.test.jwj.underMoon.DataBase;

import java.sql.Connection;


public interface IMyPool {
	Connection getConnection();

    void createConnection(int count);
}
