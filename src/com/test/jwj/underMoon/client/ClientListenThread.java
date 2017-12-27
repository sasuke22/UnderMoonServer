package com.test.jwj.underMoon.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketAddress;

import com.test.jwj.underMoon.DataBase.UserDao;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.TranObjectType;
import com.test.jwj.underMoon.bean.User;

/**
 * 服务器对客户端的监听监听
 * 
 */
public class ClientListenThread implements Runnable {
	private ClientActivity client;
	private ObjectInputStream read;
	private boolean isRunning;

	public ClientListenThread(ObjectInputStream read, ClientActivity client) {
		this.read = read;
		this.client = client;
		isRunning = true;
	}

	@Override
	public void run() {
		SocketAddress s = client.getmClient().getRemoteSocketAddress();
		while (isRunning) {
			readMsg();
		}
	}

	private void readMsg() {
		SocketAddress s = client.getmClient().getRemoteSocketAddress();
		try {
			TranObject tran = (TranObject) read.readObject();
			TranObjectType type = tran.getTranType();
			switch (type) {
			case REGISTER_ACCOUNT:
				String account = (String) tran.getObject();
				System.out.println(account);
				client.checkAccount(account);
				break;
			case REGISTER:
				client.regist(tran);
				break;
			case LOGIN:
				System.out.println("read login");
				client.login(tran);
				System.out.println("read login fail");
				break;
			case SEARCH_FRIEND:
				client.searchFriend(tran);
				break;
			case FRIEND_REQUEST:
				client.friendRequset(tran);
				break;
			case MESSAGE:
				client.sendMessage(tran);
			default:
				break;
			}
		} catch (ClassNotFoundException e) {
			System.out.println("read class" + e.getMessage());
			e.printStackTrace();
		} catch (EOFException e) {
			System.out.println("read eof" + e.getMessage());
			client.close();
		} catch (IOException e) {
			System.out.println("read io" + e.getMessage());
			e.printStackTrace();
		}
	}
	public void close() {
		isRunning = false;
	}

}
