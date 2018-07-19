package com.test.jwj.underMoon.DataBase;

public class PoolManager {
	private static class createPool {

        private static DBPool poolImpl = new DBPool();

    }

    /**
     * 内部类单利模式产生使用对象
     * @return
     */
    public static DBPool getInstance() {
        return createPool.poolImpl;
    }
}
