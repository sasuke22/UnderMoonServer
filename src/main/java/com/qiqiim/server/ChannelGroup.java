package com.qiqiim.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChannelGroup {
	private static DefaultChannelGroup GlobalGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ConcurrentMap<Integer, ChannelId> ChannelMap=new ConcurrentHashMap<Integer, ChannelId>();
    public static void addChannel(int user_id,Channel channel){
        GlobalGroup.add(channel);
        ChannelMap.put(Integer.valueOf(user_id),channel.id());
    }
    
    public static synchronized void removeChannel(Channel channel){
        GlobalGroup.remove(channel);
        Iterator<Integer> iter = ChannelMap.keySet().iterator();
        while(iter.hasNext()){
        	Integer key = iter.next();
        	if(ChannelMap.get(key).equals(channel.id())){
        		ChannelMap.remove(key);
        	}
        }
        System.out.print("下线了，剩余"+size()+"人");
//        ChannelMap.remove(channel.id().asShortText());
    }
    
    public static  Channel findChannel(int id){
    	if(ChannelMap.get(Integer.valueOf(id)) == null)
    		return null;
    	else
    		return GlobalGroup.find(ChannelMap.get(Integer.valueOf(id)));
    }
    
    public static void send2All(TextWebSocketFrame tws){
        GlobalGroup.writeAndFlush(tws);
    }
    
    public static int size(){
    	return ChannelMap.size();
    }
}
