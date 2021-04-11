/**
 ***************************************************************************************
 *  @Author     1044053532@qq.com   
 *  @License    http://www.apache.org/licenses/LICENSE-2.0
 ***************************************************************************************
 */
package com.qiqiim.webserver.user.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.qiqiim.constant.Constants;
import com.qiqiim.constant.User;
import com.qiqiim.server.connertor.ImConnertor;
import com.qiqiim.server.group.ImChannelGroup;
import com.qiqiim.server.model.MessageWrapper;
import com.qiqiim.server.model.proto.MessageBodyProto;
import com.qiqiim.server.model.proto.MessageProto;
import com.qiqiim.server.session.SessionManager;
import com.qiqiim.webserver.base.controller.BaseController;
import com.qiqiim.webserver.user.dao.FriendDao;
import com.qiqiim.webserver.user.dao.UserDao;

@Controller
@RequestMapping("/user/imuser")
public class SessionController extends BaseController{
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private ImConnertor connertor;
	    
	   
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public String list(@RequestParam Map<String, Object> params,HttpServletRequest request){
		request.setAttribute("allsession", sessionManager.getSessions());
		return "user/userlist";
	}
	
	/**
	 * 列表
	 */
	@RequestMapping(value="/msgbroadcast", produces="text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String broadcast(@RequestParam String msgContent,String session,HttpServletRequest request){
		 
		 MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
         builder.setCmd(Constants.CmdType.MESSAGE);
         builder.setSender("-1");
         builder.setMsgtype(Constants.ProtobufType.NOTIFY);
         MessageBodyProto.MessageBody.Builder  msg =  MessageBodyProto.MessageBody.newBuilder();
         msg.setContent(msgContent); 
         builder.setContent(msg.build().toByteString());
         if(StringUtils.isNotEmpty(session)){
        	 //推送到个人
        	 MessageWrapper  msgWrapper = new MessageWrapper(MessageWrapper.MessageProtocol.NOTIFY, session, null,builder);
        	 connertor.pushMessage(msgWrapper);
         }else{
        	 //推送到所有用户
        	 ImChannelGroup.broadcast(builder);
         }
	     return JSONArray.toJSONString(1);
    }
	
	
	/**
	 * 列表
	 */
	@RequestMapping(value="/offline", produces="text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String offlineUser(@RequestParam String msgContent,String session,HttpServletRequest request){
		 boolean result = false;
		 MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
         builder.setCmd(Constants.CmdType.MESSAGE);
         builder.setSender("-1");
         builder.setMsgtype(Constants.ProtobufType.NOTIFY);
         MessageBodyProto.MessageBody.Builder  msg =  MessageBodyProto.MessageBody.newBuilder();
         if(StringUtils.isNotEmpty(msgContent)){
        	  msg.setContent(msgContent); 
         }else{
        	  msg.setContent("已被系统强制下线"); 
         } 
         builder.setContent(msg.build().toByteString());
         if(StringUtils.isNotEmpty(session)){
        	 //推送到个人
        	 MessageWrapper  msgWrapper = new MessageWrapper(MessageWrapper.MessageProtocol.NOTIFY, session, null,builder);
        	 connertor.pushMessage(msgWrapper);
        	 connertor.close(session);
        	 result = true;
         }else{
        	 //广播下线消息，所有用户下线
        	 ImChannelGroup.broadcast(builder);
        	 ImChannelGroup.disconnect();
         }
	     return JSONArray.toJSONString(result);
    }
	
	/**
	 * 检查是否有过注册
	 */
	@RequestMapping(value="/registaccount")
	@ResponseBody
	public boolean registAccount(@RequestParam("account")String account,HttpServletRequest request,HttpServletResponse response){
		return false;//UserDao.selectAccount(account);
	}
	
	/**
	 * 注册
	 */
	@RequestMapping(value="/regist",method=RequestMethod.POST)
	@ResponseBody
	public int regist(@RequestParam("file")MultipartFile file,HttpServletRequest request,HttpServletResponse response){
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		User user = new Gson().fromJson(req.getParameter("user"), User.class);
		int userId = UserDao.insertInfo(user);
		if (!file.isEmpty()) {
			String userIdPath = "/www/wwwroot/images" + File.separator + userId;
			File parent = new File(userIdPath);
			if (!parent.exists()) {
				parent.mkdirs();
			}
			File head = new File(userIdPath + File.separator + "0.jpg");
			try {
				if (!head.exists()) {
					head.createNewFile();
				}
				file.transferTo(head);
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
		}
		return userId;
	}
	
	/**
	 * 登录
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public int login(HttpServletRequest request,HttpServletResponse response){
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		User user = new Gson().fromJson(req.getParameter("user"), User.class);
//		user = UserDao.login(user);
		if (user != null) {
			//TODO 应该是要参照ImController里的login逻辑，在打开对话框的时候才将用户加入server，或使用相同的client中的对话方式
		}
		return 1;
	}
	
	/**
	 * 查找好友
	 */
	@RequestMapping(value="/searchfriend")
	@ResponseBody
	public ArrayList<User> regist(@RequestParam("filter")String filter,HttpServletRequest request,HttpServletResponse response){
		String values[] = filter.split(" ");
		ArrayList<User> list;
		if (values[0].equals("0"))
			list = UserDao.selectFriendByAccountOrID(values[1]);
		else
			list = UserDao.selectFriendByMix(values);
		return list;
	}
	
	/**
	 * 添加好友
	 */
	@RequestMapping(value="/friendrequest")
	@ResponseBody
	public int friendRequest(@RequestParam("result")boolean result,HttpServletRequest request,HttpServletResponse response){
		//TODO 这里要根据发送的是添加好友请求，还是已经同意添加的请求进行区分
		if (result) {
			MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
			try{
				FriendDao.addFriend(Integer.parseInt(req.getParameter("receiveId")), Integer.parseInt(req.getParameter("sendId")));
				FriendDao.addFriend(Integer.parseInt(req.getParameter("sendId")), Integer.parseInt(req.getParameter("receiveId")));
			} catch(SQLException e){
				return -1;
			}
			return 1;
		} else
			return -1;
	}
	
	
}


 