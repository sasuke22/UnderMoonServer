package com.test.jwj.underMoon.client;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;

import com.test.jwj.underMoon.DataBase.UserDao;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.TranObjectType;

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
					client.login(tran);
					break;
				case SEARCH_FRIEND:
					client.searchFriend(tran);
					break;
				case FRIEND_REQUEST:
					client.friendRequset(tran);
					break;
				case MESSAGE:
					client.sendMessage(tran);
					break;
				case ALL_CONTRIBUTES:
					client.getAllContributes(tran);
					break;
				case TODAY_CONTRIBUTES:
					client.getTodayContributes(tran);
					break;
				case INVITATION_DETAIL:
					client.getInvitationDetail(tran);
					break;
				case ADD_CONTRIBUTE:
					client.addContribute(tran);
					break;
				case ENLIST:
					client.addEnlist(tran);
					break;
				case MY_CONTRIBUTES:
					client.getMyContributes(tran);
					break;
				case SAVE_USER_INFO:
					client.saveUserInfo(tran);
					break;
				case GET_ENLIST:
					client.getEnlist(tran);
					break;
				case GET_ENLIST_NAME:
					client.getEnlistName(tran);
					break;
				case GET_USER_INFO:
					client.getUserInfo(tran);
					break;
				case GET_USER_PHOTOS:
					client.getUserPhotos(tran);
					break;
				case UPLOAD_USER_PHOTOS:
					client.uploadUserPhotos(tran);
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

	public void uploadFile(int userId){
		boolean success = false;
		String photolist = UserDao.getUserPhotosAddress(userId);
		if (photolist == null) 
			return;
		String[] photoId = photolist.split("\\|");
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
}
	
