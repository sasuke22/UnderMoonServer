package com.test.jwj.underMoon.server;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.TranObjectType;
import com.test.jwj.underMoon.client.ClientActivity;

public class WsServer extends WebSocketServer{
	private ClientActivity client;
	private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
	
	public WsServer(int port){
		super(new InetSocketAddress(port));
	}
	
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("close " + reason);
	}

	@Override
	public void onError(WebSocket conn, Exception e) {
		System.out.println("error " + e.getMessage());
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("message " + message);
		TranObject tran = new Gson().fromJson(message, TranObject.class);
		switch(tran.getTranType()){
		case TranObjectType.LOGIN:
			client.login(conn,tran);
			break;
		case TranObjectType.FRIEND_REQUEST:
			client.friendRequset(tran);
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
	}

	/*
	 * 获得在线用户
	 */
	public WebSocket getClientByAccount(String account) {
		return clients.get(account);
	}

	public void removeClientByAccount(String account) {
		clients.remove(account);
	}

	public void addClient(String account, WebSocket conn) {
		clients.put(account, conn);
	}

	public boolean contatinAccount(String account) {
		return clients.containsKey(account);
	}

	public int size() {
		return clients.size();
	}
	
	public void setClient(ClientActivity client){
		this.client = client;
	}
}
