package com.test.jwj.underMoon.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;

import com.test.jwj.underMoon.DataBase.UserDao;
import com.test.jwj.underMoon.server.FileClient;

public class FileListenThread implements Runnable {
	private FileClient client;
	private InputStream read;
	private boolean isRunning;

	public FileListenThread(InputStream mInput, FileClient fileClient) {
		this.read = mInput;
		this.client = fileClient;
		isRunning = true;
	}

	@Override
	public void run() {
		while (isRunning) {
			readMsg();
		}
	}

	private void readMsg() {
		int msgType;
		try {
			msgType = read.read();
			if (msgType != -1) {
				uploadFile(msgType);
				System.out.println("finish");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void uploadFile(int userId){
		boolean success = false;
		String photolist = UserDao.getUserPhotosAddress(userId);
		if (photolist == null) 
			return;
		String[] photoId = photolist.split("|");
		System.out.println("userid " + userId);
		byte[] buffer = new byte[1024];
		int len = -1;
		RandomAccessFile fileOutStream = null;
		try {
			String path = "D:\\images"+File.separator+userId;
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String abPath = path + File.separator+photoId.length+".jpg";
			System.out.println(abPath);
			File picFile = new File(abPath);
			if (!picFile.exists()) {
				picFile.createNewFile();
			}
			fileOutStream = new RandomAccessFile(picFile, "rwd");
			while ((len = read.read(buffer)) != -1) {
				fileOutStream.write(buffer, 0, len);
			}
			fileOutStream.close();
			picFile = null;
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			client.uploadFileSuccess(success);
			if (fileOutStream != null) {
				try {
					fileOutStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void close() {
		isRunning = false;
	}
}
