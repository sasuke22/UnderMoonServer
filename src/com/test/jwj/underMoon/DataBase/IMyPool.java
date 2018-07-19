package com.test.jwj.underMoon.DataBase;


public interface IMyPool {
	PooledConnection getConnection();

    void createConnection(int count);
}
