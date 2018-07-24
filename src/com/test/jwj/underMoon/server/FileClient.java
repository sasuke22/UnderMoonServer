package com.test.jwj.underMoon.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;

import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.TranObjectType;
import com.test.jwj.underMoon.client.FileListenThread;
import com.test.jwj.underMoon.client.FileSendThread;

public class FileClient {
	private LinkedList<TranObject> sendQueue;
	private Socket mfileClient;
	private OutputStream mOutput;
	private InputStream mInput;
	private FileListenThread mFileListen;
	private FileSendThread mFileSend;
	
	public FileClient(ServerListen serverListen, Socket fileClient) {
		sendQueue = new LinkedList<TranObject>();
//		this.mServer = mServer;
		this.mfileClient = fileClient;
		try {
			mOutput = fileClient.getOutputStream();
			mInput = fileClient.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mFileListen = new FileListenThread(mInput, this);
		mFileSend = new FileSendThread(this);
		Thread listen = new Thread(mFileListen);
		Thread send = new Thread(mFileSend);
		listen.start();
		send.start();
	}

	public Socket getMfileClient() {
		return mfileClient;
	}

	public void setMfileClient(Socket mfileClient) {
		this.mfileClient = mfileClient;
	}
	
	public synchronized void send(TranObject tran) {
		try {
//			mOutput.writeObject(tran);
			mOutput.flush();
			notify();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭与客户端的连接
	 */
	public void close() {
		try {
			mfileClient.close();// socket关闭后，他所在的流也都自动关闭
			mFileListen.close();
			mFileSend.close();
			System.out.println("图片下线了...");
		} catch (IOException e) {
			System.out.println("关闭失败.....");
			e.printStackTrace();
		}
	}
	
	/**
	 * 回复上传图片的成功或失败
	 */
	public void uploadFileSuccess(boolean success) {
		TranObject tran1 = new TranObject();
		tran1.setObject(success ? 1 : 0);
		tran1.setTranType(TranObjectType.UPLOAD_RESULT);
		send(tran1);
	}
	
	/******************************** 对发送队列的异步处理 ***********************************/
	/**
	 * 发送数据 如果是从好友那里发送来的 就先添加到队列 并发控制，因为同步性太强 否则直接发送； 属于发送线程
	 */
	public synchronized void insertQueue(TranObject tran) {
		sendQueue.add(tran);
	}

	public synchronized int sizeOfQueue() {
		return sendQueue.size();
	}

	public synchronized TranObject removeQueueEle(int i) {
		return sendQueue.remove(i);
	}
}
