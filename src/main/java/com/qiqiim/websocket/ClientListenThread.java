package com.qiqiim.websocket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.qiqiim.constant.TranObject;
import com.qiqiim.constant.TranObjectType;


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
//		SocketAddress s = client.getmClient().getRemoteSocketAddress();
		while (isRunning) {
			readMsg();
		}
	}

	private void readMsg() {
//		SocketAddress s = client.getmClient().getRemoteSocketAddress();
		try {
			int msgType = read.readInt();
			if (msgType == -1) {
				TranObject tran = (TranObject) read.readObject();
				int type = tran.getTranType();
				switch (type) {
				case TranObjectType.LOGIN:
//					client.login(tran);
					break;
				case TranObjectType.FRIEND_REQUEST:
					client.friendRequset(tran);
					break;
				case TranObjectType.MESSAGE:
					client.sendMessage(tran);
					break;
				default:
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("read class " + e.getMessage());
			e.printStackTrace();
			client.close();
		} catch (EOFException e) {
			System.out.println("read eof " + e.getMessage());
			client.close();
		} catch (IOException e) {
			System.out.println("read io " + e.getMessage());
			e.printStackTrace();
			client.close();
		}
		
	}
	public void close() {
		isRunning = false;
	}
}
	
