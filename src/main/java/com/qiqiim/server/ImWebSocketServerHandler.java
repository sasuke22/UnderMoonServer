/**
 ***************************************************************************************
 *  @Author     1044053532@qq.com   
 *  @License    http://www.apache.org/licenses/LICENSE-2.0
 ***************************************************************************************
 */
package com.qiqiim.server;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import org.apache.commons.lang.StringUtils;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.qiqiim.constant.ChatEntity;
import com.qiqiim.constant.Constants;
import com.qiqiim.constant.Result;
import com.qiqiim.constant.TranObject;
import com.qiqiim.constant.TranObjectType;
import com.qiqiim.constant.User;
import com.qiqiim.server.connertor.impl.ImConnertorImpl;
import com.qiqiim.server.model.MessageWrapper;
import com.qiqiim.server.model.proto.MessageProto;
import com.qiqiim.server.proxy.MessageProxy;
import com.qiqiim.util.ImUtils;
import com.qiqiim.webserver.user.dao.NewMsgListDao;
import com.qiqiim.webserver.user.dao.UserDao;
import com.qiqiim.webserver.user.service.ChatListService;
import com.qiqiim.webserver.user.service.MsgListService;
import com.qiqiim.webserver.user.service.impl.ChatListServiceImpl;
import com.qiqiim.webserver.user.service.impl.MsgListServiceImpl;
import com.qiqiim.websocket.ClientActivity;
//@Sharable
@Component
public class ImWebSocketServerHandler extends SimpleChannelInboundHandler{

	private final static Logger log = LoggerFactory.getLogger(ImWebSocketServerHandler.class);
	private WebSocketServerHandshaker handshaker;
//	@Autowired
//	private ChatListService chatListServiceImpl;
//	@Autowired
//	private MsgListService msgListServiceImpl;
	public static ImWebSocketServerHandler handler;
//    private ImConnertorImpl connertor = null;
//    private MessageProxy proxy = null;

//    public ImWebSocketServerHandler() {
//        this.connertor = connertor;
//        this.proxy = proxy;
//    }
    
    @PostConstruct
    public void init(){
//    	handler = this;
//    	handler.msgListServiceImpl = this.msgListServiceImpl;
    }
	
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object o) throws Exception {
    	System.out.println("user event");
    	 String sessionId = ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_ID).get();
    	//发送心跳包
    	if (o instanceof IdleStateEvent && ((IdleStateEvent) o).state().equals(IdleState.WRITER_IDLE)) {
			  if(StringUtils.isNotEmpty(sessionId)){
				  MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
				  builder.setCmd(Constants.CmdType.HEARTBEAT);
			      builder.setMsgtype(Constants.ProtobufType.SEND);
				  ctx.channel().writeAndFlush(builder);
			  } 
 			 log.debug(IdleState.WRITER_IDLE +"... from "+sessionId+"-->"+ctx.channel().remoteAddress()+" nid:" +ctx.channel().id().asShortText());
 	    } 
	        
	    //如果心跳请求发出70秒内没收到响应，则关闭连接
	    if ( o instanceof IdleStateEvent && ((IdleStateEvent) o).state().equals(IdleState.READER_IDLE)){
			log.debug(IdleState.READER_IDLE +"... from "+sessionId+" nid:" +ctx.channel().id().asShortText());
			Long lastTime = (Long) ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_HEARBEAT).get();
	     	if(lastTime == null || ((System.currentTimeMillis() - lastTime)/1000>= Constants.ImserverConfig.PING_TIME_OUT))
	     	{
	     		ctx.channel().close();
	     	}
	     	ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_HEARBEAT).set(null);
	    }
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object req) throws Exception {
		System.out.println("channel read:"+req.toString());
		//要求Upgrade为websocket，过滤掉get/Post
		if (req instanceof FullHttpRequest){
            //以http请求形式接入，但是走的是websocket
                handleHttpRequest(ctx, (FullHttpRequest) req);
        }else if (req instanceof  WebSocketFrame){
            //处理websocket客户端的消息
            handlerWebSocketFrame(ctx, (WebSocketFrame) req);
        }
//		  try {
//			   String sessionId = connertor.getChannelSessionId(ctx);
                // inbound
//                if (message.getMsgtype() == Constants.ProtobufType.SEND) {
//                	ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_HEARBEAT).set(System.currentTimeMillis());
//                    MessageWrapper wrapper = proxy.convertToMessageWrapper(sessionId, message);
//                    if (wrapper != null)
//                        receiveMessages(ctx, wrapper);
//                }
//                // outbound
//                if (message.getMsgtype() == Constants.ProtobufType.REPLY) {
//                	MessageWrapper wrapper = proxy.convertToMessageWrapper(sessionId, message);
//                	if (wrapper != null)
//                      receiveMessages(ctx, wrapper);
//                }
//	        } catch (Exception e) {
//	            log.error("ImWebSocketServerHandler channerRead error.", e);
//	            throw e;
//	        }
		 
	}
	
	private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
        // 判断是否关闭链路的指令
		System.out.println("handle webscoket frame");
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
        	System.out.println("本例程仅支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        System.out.println("服务端收到：" + request);
        TranObject tran = JSONObject.parseObject(request, TranObject.class);
        switch(tran.getTranType()){
//		case TranObjectType.HEART_BEAT:
//			clients.get(tran.getSendId()).setmTime(System.currentTimeMillis());
//			break;
		case TranObjectType.LOGIN:
			handleLogin(ctx,tran);
			break;
//		case TranObjectType.FRIEND_REQUEST:
//			clients.get(tran.getSendId()).friendRequset(tran);
//			break;
		case TranObjectType.MESSAGE:
			System.out.println("1");
			TextWebSocketFrame tws = new TextWebSocketFrame(JSON.toJSONString(tran));
	        // 群发
			System.out.println("2:"+tran.getReceiveId());
			Channel another_channel = ChannelGroup.findChannel(tran.getReceiveId());
			System.out.println("3:"+another_channel);
			if(another_channel != null)//对方在线，直接发送过去
				another_channel.writeAndFlush(tws);
//			ChatEntity chat = JSONObject.toJavaObject((JSONObject)tran.getObject(), ChatEntity.class);
//			handler.msgListServiceImpl.insertMessage(chat.getUserId(),chat.getAnotherId(),chat.getContent());
//			NewMsgListDao.insertMessage(chat);
			System.out.println("7");
			break;
		default:
			break;
		}
    }
    /**
     * 唯一的一次http请求，用于创建websocket
     * */
    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) {
    	System.out.println("handle http request");
        //要求Upgrade为websocket，过滤掉get/Post
        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
        	System.out.println("bad request");
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:8400/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }
	
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	log.info("ImWebSocketServerHandler  join from "+ImUtils.getRemoteAddress(ctx)+" nid:" + ctx.channel().id().asShortText());
    }

    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.debug("ImWebSocketServerHandler Disconnected from {" +ctx.channel().remoteAddress()+"--->"+ ctx.channel().localAddress() + "}");
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug("ImWebSocketServerHandler channelActive from (" + ImUtils.getRemoteAddress(ctx) + ")");
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("ImWebSocketServerHandler channelInactive from (" + ImUtils.getRemoteAddress(ctx) + ")");
        ChannelGroup.removeChannel(ctx.channel());
//        String sessionId = connertor.getChannelSessionId(ctx);
//        receiveMessages(ctx,new MessageWrapper(MessageWrapper.MessageProtocol.CLOSE, sessionId,null, null));  
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("ImWebSocketServerHandler (" + ImUtils.getRemoteAddress(ctx) + ") -> Unexpected exception from downstream." + cause);
    }

    public void handleLogin(ChannelHandlerContext ctx, TranObject tran){
		System.out.println("user:" + tran.getObject());
		User user = new Gson().fromJson(tran.getObject().toString(),User.class);
		// 验证密码和用户名是否存在，若存在则为user对象赋值
		System.out.println("1:"+user.getAccount());
		boolean isExisted = UserDao.login(user);
		if (isExisted == true) {
			System.out.println("2:");
			ChannelGroup.addChannel(user.getId(),ctx.channel());
			System.out.println("3:");
			tran.setResult(Result.LOGIN_SUCCESS);
			System.out.println(user.getAccount() + "上线了,当前在线人数：" + ChannelGroup.size());
			tran.setObject(user);
			System.out.println("4:");
			tran.setReceiveId(user.getId());
			System.out.println("5:");
			TextWebSocketFrame tws = new TextWebSocketFrame(JSON.toJSONString(tran));
	        ctx.channel().writeAndFlush(tws);
			// 获取离线信息
//			ArrayList<TranObject> offMsg = SaveMsgDao.selectMsg(user.getId());
//			for (int i = 0; i < offMsg.size(); i++)
//				newClient.insertQueue(offMsg.get(i));
//			SaveMsgDao.deleteSaveMsg(user.getId());
		} else{
			tran.setResult(Result.LOGIN_FAILED);
			TextWebSocketFrame tws = new TextWebSocketFrame(JSON.toJSONString(tran, SerializerFeature.WriteDateUseDateFormat));
	        // 群发
//	        ChannelSupervise.send2All(tws);
	        // 返回【谁发的发给谁】
	         ctx.channel().writeAndFlush(tws);
		}
	}

    /**
     * to send message
     *
     * @param hander
     * @param wrapper
     
    private void receiveMessages(ChannelHandlerContext hander, MessageWrapper wrapper) {
    	System.out.println("channel read");
    	//设置消息来源为Websocket
    	wrapper.setSource(Constants.ImserverConfig.WEBSOCKET);
        if (wrapper.isConnect()) {
       	    connertor.connect(hander, wrapper); 
        } else if (wrapper.isClose()) {
        	connertor.close(hander,wrapper);
        } else if (wrapper.isHeartbeat()) {
        	connertor.heartbeatToClient(hander,wrapper);
        }else if (wrapper.isGroup()) {
        	connertor.pushGroupMessage(wrapper);
        }else if (wrapper.isSend()) {
        	connertor.pushMessage(wrapper);
        } else if (wrapper.isReply()) {
        	connertor.pushMessage(wrapper.getSessionId(),wrapper);
        }  
    }*/
}
