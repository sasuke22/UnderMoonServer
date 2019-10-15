package com.qiqiim.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChannelGroup {
	private static DefaultChannelGroup GlobalGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ConcurrentMap<Integer, ChannelId> ChannelMap=new ConcurrentHashMap<Integer, ChannelId>();
    public static void addChannel(int user_id,Channel channel){
        GlobalGroup.add(channel);
        ChannelMap.put(user_id,channel.id());
    }
    
    public static void removeChannel(Channel channel){
        GlobalGroup.remove(channel);
        ChannelMap.remove(channel.id().asShortText());
    }
    
    public static  Channel findChannel(int id){
        return GlobalGroup.find(ChannelMap.get(id));
    }
    
    public static void send2All(TextWebSocketFrame tws){
        GlobalGroup.writeAndFlush(tws);
    }
    
    public static int size(){
    	return ChannelMap.size();
    }
}
