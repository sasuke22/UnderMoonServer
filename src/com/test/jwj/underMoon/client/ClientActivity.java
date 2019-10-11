package com.test.jwj.underMoon.client;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.java_websocket.WebSocket;
import com.test.jwj.underMoon.DataBase.FriendDao;
import com.test.jwj.underMoon.DataBase.SaveMsgDao;
import com.test.jwj.underMoon.DataBase.UserDao;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.Result;
import com.test.jwj.underMoon.server.WsServer;

/**
 * @author Administrator 客户端线程
 */
public class ClientActivity {
	/*  发送队列， 因为服务器有多个监听客户端的线程，当很多好友一起向他发送消息，每个服务器线程
	   都同时调用此实例的socket争夺send ，并发控制异常。*/
	private LinkedList<TranObject> sendQueue;
	private User user;
//	private Socket mClient; // 客户端连接
	private WsServer mServer;
//	private ClientListenThread mClientListen; // 客户端监听进程
	private ClientSendThread mClientSend; // 客户端发送进程
	private JsonConfig jsonConfig;
	private WebSocket mStream;
	private long mTime;
//	private ObjectOutputStream mOutput;
//	private ObjectInputStream mInput;

	public long getmTime() {
		return mTime;
	}

	public void setmTime(long mTime) {
		this.mTime = mTime;
	}

	public WebSocket getmStream() {
		return mStream;
	}
	//	public ClientActivity(ServerListen mServer, Socket mClient) {
//		user = new User();
//		sendQueue = new LinkedList<TranObject>();
//		this.mServer = mServer;
//		this.mClient = mClient;
//		try {
//			mOutput = new ObjectOutputStream(mClient.getOutputStream());
//			mInput = new ObjectInputStream(mClient.getInputStream());
//		} catch (IOException e) {
//			System.out.println("IO " + e.getMessage());
//		}
//		mClientListen = new ClientListenThread(mInput, this);
//		mClientSend = new ClientSendThread(this);
//		Thread listen = new Thread(mClientListen);
//		Thread send = new Thread(mClientSend);
//		listen.start();
//		send.start();
//	}
	public ClientActivity(WebSocket conn,WsServer mClient){
		sendQueue = new LinkedList<TranObject>();
		this.mServer = mClient;
		this.mStream = conn;
		mClientSend = new ClientSendThread(this);
		new Thread(mClientSend).start();
		jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
	}

//	public Socket getmClient() {
//		return mClient;
//	}
//
//	public void setmClient(Socket mClient) {
//		this.mClient = mClient;
//	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 检查账号和用户名是否存在
	 */
//	public void login(WebSocket conn,TranObject tran) {
//		User user = new Gson().fromJson(tran.getObject().toString(),User.class);
//		// 验证密码和用户名是否存在，若存在则为user对象赋值
//		boolean isExisted = UserDao.login(user);
//		if (isExisted == true) {
//			mStream = conn;
//			mClient.addClient(user.getId(), this);
//			UserDao.updateIsOnline(user.getId(), 1);
//			setUser(user);
//			System.out.println(user.getAccount() + "上线了");
//			tran.setResult(Result.LOGIN_SUCCESS);
//			System.out.println("当前在线人数：" + mClient.size());
//			// 获取好友列表
////			ArrayList<User> friendList = FriendDao.getFriend(user.getId());
////			user.setFriendList(friendList);
//			tran.setObject(user);
//			send(user.getId(),tran);
//		} else{
//			tran.setResult(Result.LOGIN_FAILED);
//			JSONObject json = JSONObject.fromObject(tran,jsonConfig);
//			conn.send(json.toString());
//		}
		/*try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		// 获取离线信息
//		ArrayList<TranObject> offMsg = SaveMsgDao.selectMsg(user.getId());
//		for (int i = 0; i < offMsg.size(); i++)
//			insertQueue(offMsg.get(i));
//		SaveMsgDao.deleteSaveMsg(user.getId());
//	}

	public synchronized void send(int userId,TranObject tran) {
//		try {
//			mOutput.writeObject(tran);
//			mOutput.flush();
//			notify();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		System.out.println("3 ");
		JSONObject json = JSONObject.fromObject(tran,jsonConfig);
		System.out.println("4 " + userId + "," + json.toString());
		try{
			mServer.getClientById(userId).mStream.send(json.toString());
		}catch(Exception e){
			e.printStackTrace();
			SaveMsgDao.insertSaveMsg(user.getId(), tran);
		}
		System.out.println("5 ");
	}

	/**
	 * 客户端下线
	 */
	public void getOffLine() {
		mServer.removeClientById(user.getId());
//		UserDao.updateIsOnline(user.getId(), 0);
	}

	/**
	 * 关闭与客户端的连接
	 */
	public void close() {
		mStream.close();
//		mClientListen.close();
		mClientSend.close();
		if (user.getId() != 0)
			getOffLine();
		System.out.println(user.getAccount() + "下线了...");
	}

	/**
	 * 查找朋友
	 */
	public void searchFriend(TranObject tran) {
		String values[] = ((String) tran.getObject()).split(" ");
		ArrayList<User> list;
//		if (values[0].equals("0"))
//			list = UserDao.selectFriendByAccountOrID(values[1]);
//		else
//			list = UserDao.selectFriendByMix(values);
		System.out.println((String) tran.getObject());
		System.out.println("发送客户端查找的好友列表...");
//		tran.setObject(list);
//		send(tran);
	}

	/**
	 * 上传图片
	 
	public void uploadUserPhotos(TranObject tran){
		int userId = tran.getSendId();
		String photolist = UserDao.getUserPhotosAddress(userId);
		String[] photoId = photolist.split("\\|");
		File picFile = new File("D:\\images"+File.separator+tran.getSendId()+File.separator+photoId.length+".jpg");
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			if (!picFile.exists() && !picFile.isDirectory()) {
				picFile.createNewFile();
			}
			fos = new FileOutputStream(picFile);
			bos = new BufferedOutputStream(fos);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bao);
			oos.writeObject(tran.getObject());
			bos.write(bao.toByteArray());
			bos.flush();
		} catch (IOException e) {
			System.out.print("ex " + e.getMessage().toString());
			e.printStackTrace();
		} finally{
			try {
				if (fos!=null) {
					fos.close();
				}
				if (bos!=null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	*/
	
	/**
	 * 更新用户积分
	 
	public void updateUserScore(TranObject tran) {
		int userId = tran.getSendId();
		int score = (Integer)tran.getObject();
		int res = UserDao.updateScore(userId, score);
		System.out.println("update " + userId + "'s score success " + res);
	}*/
	
	/**
	 * 处理好友请求
	 */
	public void friendRequset(TranObject tran) {
		System.out.println("添加好友");
		int result = tran.getResult();
		if (result == Result.FRIEND_REQUEST_RESPONSE_ACCEPT) {
			System.out.println("接收方id" + tran.getReceiveId());
//			FriendDao.addFriend(tran.getReceiveId(), tran.getSendId());
//			FriendDao.addFriend(tran.getSendId(), tran.getReceiveId());
			System.out.println("添加好友成功....");
			// 向好友发起方 发送自己的信息
			tran.setObject(user);
//			ArrayList<User> friend = UserDao.selectFriendByAccountOrID(tran
//					.getSendId());
//			tran.setObject(friend.get(0));
			tran.setSendName(user.getUserName());
			// 向自己添加好友
//			friend = UserDao.selectFriendByAccountOrID(tran.getReceiveId());
			TranObject tran2 = new TranObject();
//			tran2.setObject(friend.get(0));
			tran2.setResult(tran.getResult());
			tran2.setReceiveId(tran.getSendId());
			tran2.setSendId(tran.getReceiveId());
//			tran2.setSendName(friend.get(0).getUserName());
			tran2.setTranType(tran.getTranType());
			tran2.setSendTime(tran2.getSendTime());
//			send(tran2);
		}
		sendFriend(tran);
	}
	
	/**
	 * 转发消息 将转发的消息发送到 服务器与该客户端连接的 发送队列中
	 */
	public void sendFriend(TranObject tran) {
		ClientActivity friendClient = null;
		System.out.println("包含要发送的那个好友吗？" + tran.getReceiveId()
				+ mServer.contatinId(tran.getReceiveId()));
		if (mServer.contatinId(tran.getReceiveId())) {
			friendClient = mServer.getClientById(tran.getReceiveId());
			System.out.println("将好友请求发给好友...");
			friendClient.insertQueue(tran);
		} else {
			System.out.println("offline:" + tran.getObject().toString());
			SaveMsgDao.insertSaveMsg(user.getId(), tran);
		}

	}

	public void sendMessage(TranObject tran) {
		// 添加到好友的发送队列
		System.out.println("发送聊天信息....");
		sendFriend(tran);
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
