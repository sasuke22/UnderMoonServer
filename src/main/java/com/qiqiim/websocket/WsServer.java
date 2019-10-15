package com.qiqiim.websocket;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.qiqiim.constant.Result;
import com.qiqiim.constant.TranObject;
import com.qiqiim.constant.TranObjectType;
import com.qiqiim.constant.User;
import com.qiqiim.webserver.user.dao.UserDao;

public class WsServer extends WebSocketServer{
	private static Map<Integer, ClientActivity> clients = new ConcurrentHashMap<Integer, ClientActivity>();
	private JsonConfig jsonConfig;
	
	public WsServer(int port){
		super(new InetSocketAddress(port));
	}
	
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("close " + reason);
		for(Map.Entry<Integer, ClientActivity> entry : clients.entrySet()){
			if(entry.getValue().getmStream() == conn){
				System.out.println(entry.getKey() + "下线了");
				clients.remove(entry.getKey(),entry.getValue());
				System.out.println("剩余在线人数：" + clients.size());
			}
		}
	}

	@Override
	public void onError(WebSocket conn, Exception e) {
		System.out.println("error " + e.getMessage());
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("message " + message);
		TranObject tran = com.alibaba.fastjson.JSONObject.parseObject(message,  TranObject.class);
		switch(tran.getTranType()){
		case TranObjectType.HEART_BEAT:
			clients.get(tran.getSendId()).setmTime(System.currentTimeMillis());
			break;
		case TranObjectType.LOGIN:
			handleLogin(conn,tran);
			break;
		case TranObjectType.FRIEND_REQUEST:
			clients.get(tran.getSendId()).friendRequset(tran);
			break;
		case TranObjectType.MESSAGE:
			clients.get(tran.getSendId()).sendFriend(tran);
			break;
		default:
			break;
		}
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("open");
	}

	@Override
	public void onStart() {
		System.out.println("onstart");
		jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				if(clients.size() > 0){
				for(Map.Entry<Integer,ClientActivity> entry : clients.entrySet()){
					if(System.currentTimeMillis() - entry.getValue().getmTime() > 50000){
						clients.remove(entry.getValue());
					}
				}
				}
			}
		}, 49000);
	}
	
	public void handleLogin(WebSocket conn, TranObject tran){
		System.out.println("user:" + tran.getObject());
		User user = new Gson().fromJson(tran.getObject().toString(),User.class);
		// 验证密码和用户名是否存在，若存在则为user对象赋值
		System.out.println("1:"+user.getAccount());
		boolean isExisted = UserDao.login(user);
		if (isExisted == true) {
			System.out.println("2:");
			ClientActivity newClient = new ClientActivity(conn,this);
			System.out.println("3:");
			addClient(user.getId(), newClient);
			System.out.println("4:");
			newClient.setUser(user);
			System.out.println("5:");
			System.out.println(user.getAccount() + "上线了,当前在线人数：" + clients.size());
			tran.setResult(Result.LOGIN_SUCCESS);
			tran.setObject(user);
			tran.setReceiveId(user.getId());
			newClient.send(user.getId(),tran);
			// 获取离线信息
//			ArrayList<TranObject> offMsg = SaveMsgDao.selectMsg(user.getId());
//			for (int i = 0; i < offMsg.size(); i++)
//				newClient.insertQueue(offMsg.get(i));
//			SaveMsgDao.deleteSaveMsg(user.getId());
		} else{
			tran.setResult(Result.LOGIN_FAILED);
			JSONObject json = JSONObject.fromObject(tran,jsonConfig);
			conn.send(json.toString());
		}
	}

	/*
	 * 获得在线用户
	 */
	public ClientActivity getClientById(int userId) {
		return clients.get(userId);
	}

	public void removeClientById(int userId) {
		clients.remove(userId);
	}

	public void addClient(int userId, ClientActivity client) {
		clients.put(userId, client);
	}

	public boolean contatinId(int userId) {
		return clients.containsKey(userId);
	}

	public int size() {
		return clients.size();
	}
}
