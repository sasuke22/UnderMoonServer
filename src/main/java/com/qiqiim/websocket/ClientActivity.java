package com.qiqiim.websocket;

import java.util.LinkedList;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.java_websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import com.qiqiim.constant.ChatEntity;
import com.qiqiim.constant.Result;
import com.qiqiim.constant.TranObject;
import com.qiqiim.constant.User;
import com.qiqiim.webserver.user.service.ChatListService;
import com.qiqiim.webserver.user.service.MsgListService;

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
	@Autowired
	ChatListService chatListImpl;
	@Autowired
	MsgListService msgListImpl;

	public long getmTime() {
		return mTime;
	}

	public void setmTime(long mTime) {
		this.mTime = mTime;
	}

	public WebSocket getmStream() {
		return mStream;
	}
	
	public ClientActivity(WebSocket conn,WsServer wsServer){
		sendQueue = new LinkedList<TranObject>();
		this.mServer = wsServer;
		this.mStream = conn;
		mClientSend = new ClientSendThread(this);
		new Thread(mClientSend).start();
		jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public synchronized void send(int userId,TranObject tran) {
		System.out.println("3 ");
		JSONObject json = JSONObject.fromObject(tran,jsonConfig);
		System.out.println("4 " + userId + "," + json.toString());
		try{
			mServer.getClientById(userId).mStream.send(json.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("5 ");
	}

	/**
	 * 客户端下线
	 */
	public void getOffLine() {
		mServer.removeClientById(user.getId());
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
		System.out.println("包含要发送的那个好友吗？" + tran.getReceiveId() + mServer.contatinId(tran.getReceiveId()));
		ChatEntity chat = (ChatEntity)tran.getObject();
		msgListImpl.insertMessage(chat);//更新消息列表表
		if (mServer.contatinId(tran.getReceiveId())) {
			friendClient = mServer.getClientById(tran.getReceiveId());
			System.out.println("将好友请求发给好友...");
			friendClient.insertQueue(tran);//存到要发送的用户的消息列表中，通过sendThread一个个发送过去
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
