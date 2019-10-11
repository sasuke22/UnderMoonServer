package com.qiqiim.websocket;

import java.util.concurrent.TimeUnit;

import com.qiqiim.constant.TranObject;


public class ClientSendThread implements Runnable {
	private ClientActivity mClient;
	private boolean isRunning;
	public ClientSendThread(ClientActivity mClient) {
		this.mClient = mClient;
		this.isRunning = true;
	}

	@Override
	public void run() {
		
		while (isRunning) {
			if (mClient.sizeOfQueue() == 0)
				try {
					// 若没有数据则阻塞
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			else {
				TranObject tran = mClient.removeQueueEle(0);
				mClient.send(tran.getReceiveId(),tran);
			}
		}
	}
	public void close() {
		isRunning = false;
	}
}
