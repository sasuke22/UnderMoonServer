package com.qiqiim.webserver.user.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.domain.AlipayTradeAppPayModel;
//import com.alipay.api.internal.util.AlipaySignature;
//import com.alipay.api.request.AlipayTradeAppPayRequest;
//import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qiqiim.constant.Article;
import com.qiqiim.constant.ChatEntity;
import com.qiqiim.constant.CommentDetail;
import com.qiqiim.constant.Complain;
import com.qiqiim.constant.Constants;
import com.qiqiim.constant.MeetingDetail;
import com.qiqiim.constant.Message;
import com.qiqiim.constant.User;
import com.qiqiim.server.model.MessageWrapper;
import com.qiqiim.server.model.proto.MessageBodyProto;
import com.qiqiim.server.model.proto.MessageProto;
import com.qiqiim.server.proxy.MessageProxy;
import com.qiqiim.server.session.SessionManager;
import com.qiqiim.webserver.base.controller.BaseController;
import com.qiqiim.webserver.dwrmanage.connertor.DwrConnertor;
import com.qiqiim.webserver.sys.service.FilesInfoService;
import com.qiqiim.webserver.user.dao.ArticleDao;
import com.qiqiim.webserver.user.dao.CommentsDao;
import com.qiqiim.webserver.user.dao.ComplainDao;
import com.qiqiim.webserver.user.dao.ContributesDao;
import com.qiqiim.webserver.user.dao.FriendDao;
import com.qiqiim.webserver.user.dao.GoodsDao;
import com.qiqiim.webserver.user.dao.MsgListDao;
import com.qiqiim.webserver.user.dao.NewChatListDao;
import com.qiqiim.webserver.user.dao.NewMsgListDao;
import com.qiqiim.webserver.user.dao.UserDao;
import com.qiqiim.webserver.user.model.GoodsBean;
import com.qiqiim.webserver.user.model.ImFriendUserData;
import com.qiqiim.webserver.user.model.ImFriendUserInfoData;
import com.qiqiim.webserver.user.model.ImGroupUserData;
import com.qiqiim.webserver.user.model.ImUserData;
import com.qiqiim.webserver.user.model.UserAccountEntity;
import com.qiqiim.webserver.user.model.UserInfoEntity;
import com.qiqiim.webserver.user.model.UserMessageEntity;
import com.qiqiim.webserver.user.service.ChatListService;
import com.qiqiim.webserver.user.service.MsgListService;
import com.qiqiim.webserver.user.service.UserAccountService;
import com.qiqiim.webserver.user.service.UserDepartmentService;
import com.qiqiim.webserver.user.service.UserMessageService;
import com.qiqiim.webserver.util.Pager;
import com.qiqiim.webserver.util.Query;
import com.qiqiim.webserver.util.SmsUtil;
@Controller
public class ImController extends BaseController{
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private UserAccountService userAccountServiceImpl;
	@Autowired
	private UserDepartmentService userDepartmentServiceImpl;
	@Autowired
	private FilesInfoService filesInfoServiceImpl;
	@Autowired
	private UserMessageService userMessageServiceImpl;
	@Autowired
	private DwrConnertor dwrConnertorImpl;
//	@Autowired
//	private MsgListService msgListServiceImpl;
//	@Autowired
//	private ChatListService chatListServiceImpl;
	@Autowired
	private MessageProxy proxy;
	
	private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuqvR/vs7EV/H1r2RkOporp2NhRctxWOIRJp6PjJGHANqJVshOAaBQ6e4+WIav4fpWMRuLstRpSS65t0L0YUOtsV9wSl0Xz50l9tyTMFTcmJGwokv1pSMRjW8RtjnjzhPEy2Xh5IDPXR/N7NR874lc4zrKuLcKsZON/u+tI/sZZH84NHzVjZ61SKOSdfNn3fweZrwjqIS7hvYDuylZNj7b0BJtxHO6NVTv/QuefQxZ1WDnxm7h56+8qYQNFaSB2A+pMFdx35hpANX2hlAF+sL1CwgLfuM8KiiS+FzgTLy/fVRKaOrJvE9KUhEPbb7sskWYXFP0AaSlmlYm0GnWXmtOwIDAQAB";
	
	private final String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCWEtrIbT9Xy6LORoFKWkyhH/JBGavRPwf+SDlOHImAHybgWg1rBgoXmZufLvFlW7DqPGwxacxEpd7hOZhShYRahVhZRhgjsbvIROV5iuh42S2/WmFZ5YDN3niKhEttyxwoWYhgVI6Oh1kqGgtotgAVeqteqPPBCb5jCzhlVoq5TOS6KEqA4mnSh8i9nDdOOgnOmXJnT7ZoK76n4P74ADvStXohqKABq9trqdvrBWb78x3/jB6N/m3JnsfyPYydBnVKYXWK0oGBNVErdJUp8JhkvQj6H9f8KgkN8XKH9UMVzcN9/3qj7sSvoVxScS1UJGztYgoWRo7afglZYE/OAYFnAgMBAAECggEAJuKJx8bu07vS2mnQVEijdFhHt/CD4XrYgl2KY5/nTMhFlXof4ew1rznA5fUO3Tlt1LOFQSRtphfocSkwO6mWyrGkW7VaydzikBix8NU6OR3kyaEMenOJ9U3Ao/t1Y+RtzlKskHE4YtvVEaCf9ii6StZ3EtbqGcmBiD4/BZrv5OUNfQd5EitRjbK1r41vK/65lY1v0ZyDeStu5rRSQN5Qn9zKKIjjaPXviGP1Yo2pCgAyFUIa/eNoaw7wqomp2Up1e6AtmvWgH/t1DnqatfyLyLGjNaRML8oPhMEugSjmIC/chw92/pWY6TAV2Vfr8iy0R6RE59ivOuGhi0kHRK4kUQKBgQDpQ9ImgU4M1Vvw8SDcRuGk4KeHte35V9RbmIiyUF5mHXPf5tG7tGkW65sWn6hquKMHcYVAUFUBwIpsWWstHfRz9JZGxDz68+1ijnybCjXGXoJBVOfT1e8zky7/nl1UGYMrWkvYr3P0nNjDqEX7hDoUh3bRlz7jXvtys2Nis6PlXwKBgQCks1VPWKfj8jUQQNGq/6iDQe5mBrhcsJjf5kaCwi/a86IWIcSTYLu+DtjZL8JM0T8sBAVyfsCmJSj8SAa5mrboBZTFaOZ+6hfXEOAjuVEc/fY3l9bLuEOeTiuVLKEBOczh76Fj5x0gBd0g/NEohiulcdXdg0W8ORa7DrHqlvmY+QKBgQCNCk7krUZOCCuhUYq25bzFfniNW/lZzDtAbsgoWOPbBm/rr5qczgbErwyE72BbtuwMMh2Jt4jOmGaaAK8HBpeqDPdYLotYiWi9ML4y2EePe9FyQy4xLaeGHbZLJKv1j7951Q0LJXsNKlD+bJ5z541eoFG9hJ+nxuRug/zRzyCILQKBgQCHSOciLeh6TFFZ8GRI2YdJibaRB6QYPtbT0wrIDUnRx520IDif9i1AiGGGxLwM7TO+q+7thUApOQzZbTBY9MSZATyaivgJ969tcOcrcOU3s0Ozln1RCSJBvmP+PJJjt16bl4Ix1X0O+MISfpgveUYQt9i8A0Acw6fwLrnlv+11wQKBgQCRj261ACARPL0TOBZJxjR+iFXEDsoZLUhiwNnK3Fr/EaCDKEaN92F4NbTXYY/Xf/vD5v4+Jyupe8EL4Ka2Y3fksAWkk++YR9ouQxniTzclhM5Yf5DYe8Nuy5M1RqxDmdecGYru+/nX6HhhKdtimuBoI+Hjjq2vEt/kUHLJLyNtFw==";
	
	private String commentContent = "";
	/**
	 * 单聊
	 */
	@RequestMapping("/chat")
	public String chat(@RequestParam Map<String, Object> params,HttpServletRequest request){
		request.setAttribute("allsession", sessionManager.getSessions());
		return "chat";
	}
	/**
	 * 群聊
	 */
	@RequestMapping("/groupchat")
	public String group(@RequestParam Map<String, Object> params,HttpServletRequest request){
		request.setAttribute("allsession", sessionManager.getSessions());
		return "groupchat";
	}
	/**
	 * 机器人
	 */
	@RequestMapping("/bot")
	public String list(@RequestParam Map<String, Object> params,HttpServletRequest request){
		request.setAttribute("allsession", sessionManager.getSessions());
		return "bot";
	}
	
	/**
	 * 注册
	 */
	@RequestMapping(value="/regist",method=RequestMethod.POST)
	@ResponseBody
	public int regist(@RequestParam("file")MultipartFile file,HttpServletRequest request,HttpServletResponse response){
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		User user = new Gson().fromJson(req.getParameter("user"), User.class);
		int id = UserDao.insertInfo(user);
		System.out.println("avatar:"+id);
		if(id > 0){
			if (!file.isEmpty()) {
				String userIdPath = "D:\\images" + File.separator + id;
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
				}
			}
			try{
				int recommend = Integer.valueOf(req.getParameter("recommend"));
				UserDao.updateScore(recommend, 10);//推荐人获得10金币
			}catch (Exception e){
				e.printStackTrace();
			}
			
		}
		return id;
	}
	
	/**
	 * 登录IM
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public int login(@RequestParam("account")String account,@RequestParam("password")String password,HttpServletRequest request){
		
		return -1;//login fail
	}
	
	/**
	 * 查找用户
	 */
	@RequestMapping(value="/searchfriend",produces="application/json")
	@ResponseBody
	public HashMap<String,ArrayList<User>> searchFriend(@RequestParam("filter")String filter,@RequestParam("byAccount")String byAccount){
		String values[] = filter.split(" ");
		ArrayList<User> list;
		if (byAccount.equals("true"))
			list = UserDao.selectFriendByAccountOrID(filter);
		else
			list = UserDao.selectFriendByMix(values);
		HashMap<String, ArrayList<User>> map = new HashMap<>();
		map.put("userList", list);
		return map;
	}
	
	/** 
	 *  取得所有聊天用户
	 * @param response
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getusers", produces="text/html;charset=UTF-8")
	@ResponseBody
	public Object getAllUser(HttpServletResponse response,HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception{
		    // 数据格式请参考文档  http://www.layui.com/doc/modules/layim.html  
			if(getLoginUser()!=null){
				
				UserInfoEntity  user = getLoginUser().getUserInfo();	
				ImFriendUserInfoData  my = new ImFriendUserInfoData();
				my.setId(user.getUid());
				my.setAvatar(user.getProfilephoto());
				my.setSign(user.getSignature());
				my.setUsername(user.getName());
				my.setStatus("online");
				
				//模拟群信息
				ImGroupUserData  group = new ImGroupUserData();
				group.setAvatar("http://tva2.sinaimg.cn/crop.0.0.199.199.180/005Zseqhjw1eplix1brxxj305k05kjrf.jpg");
				group.setId(1L);
				group.setGroupname("公司群");
				List<ImGroupUserData>  groups = new ArrayList<ImGroupUserData>();
				groups.add(group);
				
				Map<String, Object> map = new HashMap<String, Object>();
				ImUserData  us = new ImUserData();
				us.setCode("0");
				us.setMsg("");
				map.put("mine", my);
				map.put("group",groups);
				//获取用户分组 及用户
				List<ImFriendUserData> friends = userDepartmentServiceImpl.queryGroupAndUser();
				map.put("friend",friends);
				us.setData(map);
				return JSONArray.toJSON(us);
			}else{
				return JSONArray.toJSON("");
			}
	}
	
	
	/** 
	 * 图片上传
	 * @param file
	 * @param response
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/imgupload", method = RequestMethod.POST)
	@ResponseBody
	public int uploadImgFile(@RequestParam("file") MultipartFile file,
			HttpServletResponse response, HttpServletRequest request) throws Exception {
//		    UserAccountEntity   u = getLoginUser();
//			Long uid = u.getId();
//		 	String path=request.getSession().getServletContext().getRealPath("upload/img/temp/");
//		 	String files = filesInfoServiceImpl.savePicture(file,uid.toString()+UUID.randomUUID().toString(), path);
//		 	Map<String,Object> map = new HashMap<String,Object>();
//		 	 Map<String,String> submap = new HashMap<String,String>();
//			 if(files.length()>0){
//					map.put("code","0");
//					map.put("msg", "上传过成功");  
//				    submap.put("src", request.getContextPath()+"/upload/img/temp/"+files+"?"+new Date().getTime());
//			  }else{
//				  submap.put("src", "");
//				  map.put("code","1");
//				  map.put("msg", "上传过程中出现错误，请重新上传");  
//			  }
//			  map.put("data",submap);
//			  return  JSONArray.toJSON(map);
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		int userId = Integer.parseInt(req.getParameter("userId"));
		if (!file.isEmpty()) {
			String photolist = UserDao.getUserPhotosAddress(userId);
			int lastPhoto;
			if (photolist == null || photolist.equals("")) 
				lastPhoto = 1;
			else{
				String[] photoId = photolist.split("\\|");
				lastPhoto = Integer.parseInt(photoId[photoId.length-1]) + 1;
			}
			String path = "D:\\images" + File.separator + userId;
			File parent = new File(path);
			if (!parent.exists()) {
				parent.mkdirs();
			}
			String pic = path + File.separator + lastPhoto + ".jpg";
			File picFile = new File(pic);
			if (!picFile.exists()) {
				picFile.createNewFile();
			}
			file.transferTo(picFile);
			UserDao.updatePhotos(userId,String.valueOf(lastPhoto));
			return 1;
		}
		return 0;
	}
	
	
	/** 
	 * 文件上传
	 * @param file
	 * @param response
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/fileupload", produces="text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Object uploadAllFile(@RequestParam MultipartFile file,
			HttpServletResponse response, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception {
		
		    UserAccountEntity  u = getLoginUser();
			Long uid = u.getId();
		 	String path=request.getSession().getServletContext().getRealPath("upload/file/temp/");
		 	String files = filesInfoServiceImpl.saveFiles(file, uid.toString()+UUID.randomUUID().toString(), path);
		 	Map<String,Object> map = new HashMap<String,Object>();
		 	Map<String,String> submap = new HashMap<String,String>();
			 if(files.length()>0){
					map.put("code","0");
					map.put("msg", "上传过成功");  
				    submap.put("src", request.getContextPath()+"/upload/file/temp/"+files+"?"+new Date().getTime());
				    submap.put("name", file.getOriginalFilename());
			  }else{
				  submap.put("src", "");
				  map.put("code","1");
				  map.put("msg", "上传过程中出现错误，请重新上传");  
			  }
			  map.put("data",submap);
			  return  JSONArray.toJSON(map);
	}
	
	/**
	 * 模拟最新系统消息
	 * @param response
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public String userMessage(HttpServletResponse response,HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception{
		
		List<UserMessageEntity>  list = new ArrayList<UserMessageEntity>();
		UserMessageEntity  msg = new UserMessageEntity();
		msg.setContent("模拟系统消息");
		msg.setCreatedate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		list.add(msg);
		UserMessageEntity  msgTwo = new UserMessageEntity();
		msgTwo.setContent("模拟系统消息1");
		msgTwo.setCreatedate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		list.add(msgTwo);
		request.setAttribute("msgList", list);
		return "message";
	}
	/**
	 * 取得离线消息
	 * @param response
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getofflinemsg", produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody
	public Object userMessageCount(HttpServletResponse response,HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception{
		Map<String,Object> map =new HashMap<String,Object>();
		if(getLoginUser()!=null){
			map.put("receiveuser", getLoginUser().getId().toString());
		}else{
			map.put("receiveuser", request.getSession().getId());
		}
		List<UserMessageEntity> list = userMessageServiceImpl.getOfflineMessageList(map);
		return JSONArray.toJSON(list);
	} 
	
	/**
	 * 聊天记录
	 * @param response
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/historymessageajax", produces="text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Object userHistoryMessages(HttpServletResponse response,HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception{
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("page", getSkipToPage());
		map.put("limit", getPageSize());
		map.put("senduser", getLoginUser().getId());
		map.put("receiveuser", Long.parseLong(request.getParameter("id")));
		List<UserMessageEntity> list = userMessageServiceImpl.getHistoryMessageList(new Query(map));
		Map<String,List<UserMessageEntity>>  resultMap = new HashMap<String, List<UserMessageEntity>>();
		resultMap.put("data", list);
		return JSONArray.toJSON(resultMap);
	}
	
	/**
	 * 聊天记录页面
	 * @param response
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/historymessage", method = RequestMethod.GET)
	public String userHistoryMessagesPage(HttpServletResponse response,HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception{
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("senduser", getLoginUser().getId());
		map.put("receiveuser", Long.parseLong(request.getParameter("id")));
		int totalsize = userMessageServiceImpl.getHistoryMessageCount(map);
		Pager pager = new Pager(getSkipToPage(),getPageSize(),totalsize);
		request.setAttribute("pager", pager);
		return "/historymessage";
	} 
	
	
	@RequestMapping(value = "/send1msg", method = RequestMethod.GET)
	@ResponseBody
	public Object send1Msg(HttpServletResponse response,HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception{
		String sessionid = request.getSession().getId();
		if(getLoginUser()!=null){
			sessionid = getLoginUser().getId().toString();
		}
		MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
        builder.setCmd(Constants.CmdType.MESSAGE);
        builder.setSender(sessionid);
        builder.setReceiver((String)request.getParameter("receiver"));
        builder.setMsgtype(Constants.ProtobufType.REPLY);
        MessageBodyProto.MessageBody.Builder  msg =  MessageBodyProto.MessageBody.newBuilder();
        msg.setContent((String)request.getParameter("content")); 
        builder.setContent(msg.build().toByteString());
        MessageWrapper wrapper = proxy.convertToMessageWrapper(sessionid, builder.build());
		dwrConnertorImpl.pushMessage(sessionid, wrapper);
		return JSONArray.toJSON("");
	} 
	
	/**
	 * 创建一个meeting
	 */
	@RequestMapping(value = "/createmeeting",method = RequestMethod.POST)
	@ResponseBody
	public int createMeeting(@RequestParam("file") MultipartFile[] files,HttpServletRequest request,HttpServletResponse response
			,ModelMap model){
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		Gson gson = new Gson();
		MeetingDetail meetingDetail = gson.fromJson(req.getParameter("meetingDetail"), MeetingDetail.class);
		int meetingId = ContributesDao.addContribute(meetingDetail,files.length);
		if (meetingId != -1) {
			int restScore = UserDao.updateScore(meetingDetail.id, - 15);
			System.out.println("have files " + files.length);
			if (files != null && files.length > 0) {
				MultipartFile file;
				for (int i = 0;i < files.length;i++) {
					file = files[i];
					if (!file.isEmpty()) {
						String path = "D:\\images" + File.separator + "meeting" + File.separator + meetingId;
						File parent = new File(path);
						if (!parent.exists()) {
							parent.mkdirs();
						}
						String completePath = path + File.separator + i + ".jpg";
						File pic = new File(completePath);
						try {
							if (!pic.exists()) {
								pic.createNewFile();
							}
							file.transferTo(pic);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return restScore;
		}
		return -1;
	}
	
	/**
	 * 获取所有meeting
	 */
	@RequestMapping(value = "/allcontributes",produces="application/json")
	@ResponseBody
	public HashMap<String, ArrayList<MeetingDetail>> getAllContributes(@RequestParam("userId")int userId, @RequestParam("count")int count,@RequestParam("type")int type,
			HttpServletRequest request,HttpServletResponse response){
		ArrayList<MeetingDetail> meetings = null;
		if (type == 0)//根据最新排序
			meetings = ContributesDao.selectContrbutesOrderByDate(userId, count);
		else if (type == 1)//获取女生发布的邀约
			meetings = ContributesDao.selectWomanContrbutes(userId,count);
		else if (type == 2)//获取男生发布的邀约
			meetings = ContributesDao.selectManContrbutes(userId,count);
		else if (type == 3)//根据评论数排序hottest
			meetings = ContributesDao.selectContrbutesOrderByComments(userId,count);
		HashMap<String, ArrayList<MeetingDetail>> map = new HashMap<String,ArrayList<MeetingDetail>>();
		map.put("meetings", meetings);
		return map;
	}
	
	/**
	 * for flutter,根据关键字搜索邀约
	 */
	@RequestMapping(value="/querykeyword",produces="application/json")
	@ResponseBody
	public Map<String,List<MeetingDetail>> queryKeyword(@RequestParam("key") String keyword, @RequestParam("type") int type, @RequestParam("count")int count,
			HttpServletRequest request,HttpServletResponse response){
		List<MeetingDetail> msgList = ContributesDao.selectContributesByKeyword(keyword,type,count);
		HashMap<String,List<MeetingDetail>> map = new HashMap<String,List<MeetingDetail>>();
		map.put("meetings", msgList);
		return map;
	}
	
	/**
	 * 对一个meeting报名
	 * return 1:成功;return -1:失败;return 0:已存在
	 */
	@RequestMapping(value = "/enlist",produces="application/json")
	@ResponseBody
	public int addEnlist(@RequestParam("userId")int userId,@RequestParam("meetingId")int meetingId,
			@RequestParam("userName")String userName,HttpServletRequest request,HttpServletResponse response){
		int registRes = UserDao.updateRegist(userId,meetingId);
		return registRes;
	}
	
	/**
	 * 获取自己发布的所有meeting
	 */
	@RequestMapping(value = "/mycontributes",produces="application/json")
	@ResponseBody
	public HashMap<String,ArrayList<MeetingDetail>> getMyContributes(@RequestParam("userId")int userId,
			@RequestParam("count")int count,HttpServletRequest request,HttpServletResponse response){
		ArrayList<MeetingDetail> meetings = ContributesDao.getMyContributes(userId,count);
		HashMap<String, ArrayList<MeetingDetail>> map = new HashMap<String,ArrayList<MeetingDetail>>();
		map.put("meetings", meetings);
		return map;
	}
	
	/**
	 * 保存user修改的信息
	 */
	@RequestMapping(value = "/saveuserinfo",method=RequestMethod.POST)
	@ResponseBody
	public int saveUserInfo(HttpServletRequest request,HttpServletResponse response){
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		User user = new Gson().fromJson(req.getParameter("userinfo"), User.class);
		return UserDao.saveUserInfo(user);
	}
	
	/**
	 * 获取报名过的邀约列表
	 */
	@RequestMapping(value = "/getenlist",produces="application/json")
	@ResponseBody
	public HashMap<String, ArrayList<MeetingDetail>> getEnlist(@RequestParam("userId")int userId,
			@RequestParam("count")int count,HttpServletRequest request,HttpServletResponse response){
		ArrayList<String> enlist = UserDao.queryRegist(userId);
		ArrayList<MeetingDetail> enlistedContributes = ContributesDao.getMyEnlistMeetings(enlist,count);
		HashMap<String, ArrayList<MeetingDetail>> map = new HashMap<String,ArrayList<MeetingDetail>>();
		map.put("meetings", enlistedContributes);
		return map;
	}
	
	/**
	 * 获取报名者的信息
	 */
	@RequestMapping(value = "/getuserinfo",produces="application/json")
	@ResponseBody
	public User getUserInfo(@RequestParam("userId")int userId,
			HttpServletRequest request,HttpServletResponse response){
		return UserDao.getUserInfo(userId);
	}
	
	/**
	 * 获取用户图片地址
	 */
	@RequestMapping(value = "/getuserpic",produces="application/json")
	@ResponseBody
	public String getUserPhotos(@RequestParam("userId")int userId,
			HttpServletRequest request,HttpServletResponse response){
		return UserDao.getUserPhotosAddress(userId);
	}
	
	/**
	 * 更新用户积分
	 */
	@RequestMapping(value = "/updateuserscore",method=RequestMethod.POST)
	@ResponseBody
	public int updateUserScore(HttpServletRequest request,HttpServletResponse response){
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		int userId = Integer.valueOf(req.getParameter("userId"));
		int score = Integer.valueOf(req.getParameter("score"));
		return UserDao.updateScore(userId, score);
	}
	
	/**
	 * 给客户端生成orderString
	 
	@RequestMapping(value = "/alipay")
	@ResponseBody
	public String alipay(@RequestParam("amount")String amount,HttpServletRequest request,HttpServletResponse response){
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", 
				"2016092400584497", APP_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA2");
		AlipayTradeAppPayRequest aliRequest = new AlipayTradeAppPayRequest();
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody("我是测试数据");//交易具体描述
		model.setSubject("App支付测试Java");//订单关键字
		model.setOutTradeNo("1");//商品唯一订单号，必须数字和英文
		model.setTimeoutExpress("30m");//订单允许的最晚付款时间
		model.setTotalAmount(amount);//订单总金额，精确到小数点后两位
		model.setProductCode("QUICK_MSECURITY_PAY");//固定值，销售产品码
		aliRequest.setBizModel(model);
		aliRequest.setNotifyUrl("http://192.168.107.99:8080/qiqiim-server/alipayresult");//支付宝服务器通知商户服务器指定页面的路径
		try {
	        //这里和普通的接口调用不同，使用的是sdkExecute
	        AlipayTradeAppPayResponse aliResponse = alipayClient.sdkExecute(aliRequest);
	        return aliResponse.getBody();//就是orderString 可以直接给客户端请求，无需再做处理。
	    } catch (AlipayApiException e) {
	        e.printStackTrace();
		}
		return null;
	}*/
	
	/**
	 * 给客户端生成orderString
	 
	@RequestMapping(value = "/alipayresult",method=RequestMethod.POST)
	@ResponseBody
	public boolean alipayResult(HttpServletRequest request,HttpServletResponse response){
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
		    String name = (String) iter.next();
		    String[] values = (String[]) requestParams.get(name);
		    String valueStr = "";
		    for (int i = 0; i < values.length; i++) {
		        valueStr = (i == values.length - 1) ? valueStr + values[i]
		                    : valueStr + values[i] + ",";
		  	}
		    //乱码解决，这段代码在出现乱码时使用。
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		//切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		//boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
		boolean flag = false;
		try {
			flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, "utf-8","RSA2");
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return flag;
	}*/
	
	/**
	 * 聊天图片
	 */
	@RequestMapping(value = "/chatimage",produces="application/json", method = RequestMethod.POST)
	@ResponseBody
	public String chatImage(@RequestParam("file") MultipartFile  file,
			HttpServletResponse response, HttpServletRequest request) throws Exception {
		if (!file.isEmpty()) {
			String path = "D:\\images" + File.separator + "chat";
			File parent = new File(path);
			if (!parent.exists()) {
				parent.mkdirs();
			}
			String pic = System.currentTimeMillis() + ".jpg";
			File picFile = new File(path + File.separator + pic);
			if (!picFile.exists()) {
				picFile.createNewFile();
			}
			file.transferTo(picFile);
			return pic;
		}
		return null;
	}
	
	/**
	 * &flutter,根据id获取meetingDetail
	 */
	@RequestMapping(value = "/invitationdetail",produces="application/json")
	@ResponseBody
	public MeetingDetail getInvitationDetail(@RequestParam("meetingid")String meetingId,
			HttpServletRequest request,HttpServletResponse response){
		MeetingDetail meetingDetail = ContributesDao.getInvitationDetailById(Integer.parseInt(meetingId));
		String path = "D:\\images"+File.separator+"meeting" + File.separator + meetingId;
		File dir = new File(path);
		if (dir.exists()) {
			meetingDetail.setPics(dir.listFiles().length);
		}
		return meetingDetail;
	}
	
	/**
	 * for flutter server app,根据客户端传递的已加载的数量来获取20个meeting
	 */
	@RequestMapping(value = "/morecontributes",produces="application/json")
	@ResponseBody
	public HashMap<String,ArrayList<MeetingDetail>> getAllMoreContributes(@RequestParam("count")int oldCount,
			HttpServletRequest request,HttpServletResponse response){
		ArrayList<MeetingDetail> meetings = ContributesDao.selectAllContrbutesByOldCount(oldCount);
		HashMap<String, ArrayList<MeetingDetail>> map = new HashMap<String,ArrayList<MeetingDetail>>();
		map.put("meetings", meetings);
		return map;
	}
	
	/**
	 * for flutter server app,根据客户端传递的已加载的数量来获取20个meeting
	 */
	@RequestMapping(value = "/unapprovedcontributes",produces="application/json")
	@ResponseBody
	public HashMap<String,ArrayList<MeetingDetail>> getUnapprovedContributes(@RequestParam("count")int oldCount,
			HttpServletRequest request,HttpServletResponse response){
		ArrayList<MeetingDetail> meetings = ContributesDao.selectUnapprovedContrbutesByOldCount(oldCount);
		HashMap<String, ArrayList<MeetingDetail>> map = new HashMap<String,ArrayList<MeetingDetail>>();
		map.put("meetings", meetings);
		return map;
	}
	
	/**
	 * for flutter server app,根据客户端传递的已加载的数量来获取20个meeting
	 */
	@RequestMapping(value = "/refusedcontributes",produces="application/json")
	@ResponseBody
	public HashMap<String,ArrayList<MeetingDetail>> getRefusedContributes(@RequestParam("count")int oldCount,
			HttpServletRequest request,HttpServletResponse response){
		ArrayList<MeetingDetail> meetings = ContributesDao.selectRefusedContrbutesByOldCount(oldCount);
		HashMap<String, ArrayList<MeetingDetail>> map = new HashMap<String,ArrayList<MeetingDetail>>();
		map.put("meetings", meetings);
		return map;
	}
	
	/**
	 * for flutter server app，改meeting的approve
	 */
	@RequestMapping(value = "/changemeetingapprove",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public int changeMeetingApprove(@RequestParam("meetingId")int meetingId,
			@RequestParam("approve")int approve,@RequestParam(value = "reason",required = false)String reason,
			HttpServletRequest request,HttpServletResponse response){
		System.out.println("reason"+reason);
		int result = ContributesDao.changeMeetingApprove(meetingId,approve,reason);
		return result;
	}
	
	/**
	 * for flutter server app,获取20个反馈
	 */
	@RequestMapping(value = "/morearticles",produces="application/json")
	@ResponseBody
	public HashMap<String,ArrayList<Article>> getAllMoreArticles(@RequestParam("count")int oldCount,
			HttpServletRequest request,HttpServletResponse response){
		ArrayList<Article> articles = ArticleDao.selectAllContrbutesByOldCount(oldCount);
		HashMap<String, ArrayList<Article>> map = new HashMap<String,ArrayList<Article>>();
		map.put("articles", articles);
		return map;
	}
	
	/**
	 * for flutter server app,获取20个会员
	 */
	@RequestMapping(value = "/getalluser",produces="application/json")
	@ResponseBody
	public HashMap<String,ArrayList<User>> getAllUsers(@RequestParam("count")int oldCount,@RequestParam("gender")int gender,
			HttpServletRequest request,HttpServletResponse response){
		ArrayList<User> users = UserDao.selectAllUsersByOldCount(oldCount,gender);
		HashMap<String, ArrayList<User>> map = new HashMap<String,ArrayList<User>>();
		map.put("userList", users);
		return map;
	}
	
	/**
	 * for flutter server app，改article的approve
	 */
	@RequestMapping(value = "/changearticleapprove",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public int changeArticleApprove(HttpServletRequest request,HttpServletResponse response){
		Article article = new Gson().fromJson(request.getParameter("article"), Article.class);
		int result = ArticleDao.changeArticleApprove(article);
		return result;
	}
	
	/**
	 * for flutter server app，改article的approve
	 */
	@RequestMapping(value = "/changearticleperfect",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public int changeArticlePerfect(@RequestParam("id")int id,HttpServletRequest request,HttpServletResponse response){
		int result = ArticleDao.changeArticlePerfect(id);
		result = UserDao.updateScore(id, 10);
		return result;
	}
	
	/**
	 * for flutter server app，是否禁用用户
	 */
	@RequestMapping(value = "/disableuser",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public int disableUser(@RequestParam("id")int id,@RequestParam("lock")int lock,HttpServletRequest request,HttpServletResponse response){
		return UserDao.disableUser(id,lock);
	}
	
	/**
	 * for flutter server app，是否置顶邀约
	 */
	@RequestMapping(value = "/topmeeting",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public int topMeeting(@RequestParam("meetingid")int meetingid,@RequestParam("top")int top,HttpServletRequest request,HttpServletResponse response){
		return ContributesDao.topMeeting(meetingid,top);
	}
	
	/**
	 * for flutter server app，是否置顶邀约
	 */
	@RequestMapping(value = "/querypassword",produces="application/json")
	@ResponseBody
	public String queryPassword(@RequestParam("phone")String phone,HttpServletRequest request,HttpServletResponse response){
		return UserDao.queryPassword(phone);
	}
	
	/**
	 * for flutter server app，获取男女人数
	 */
	@RequestMapping(value = "/getusercount",produces="application/json")
	@ResponseBody
	public HashMap<String,Integer> getUserCount(HttpServletRequest request,HttpServletResponse response){
		return UserDao.getUserCount();
	}
	
	/**
	 * for flutter，根据筛选条件查找用户
	 */
	@RequestMapping(value = "/searchbyfilter",produces="application/json")
	@ResponseBody
	public HashMap<String, ArrayList<User>> searchbyfilter(
			@RequestParam("userId")int userId,@RequestParam("gender")int gender,
			@RequestParam("way")int way,@RequestParam("keyword")String keyword,
			@RequestParam("min")int min,@RequestParam("max")int max,
			HttpServletRequest request,HttpServletResponse response){
		ArrayList<User> list = UserDao.selectFriendByFilter(userId,gender,way,keyword,min,max);
		HashMap<String,ArrayList<User>> map = new HashMap<>();
		map.put("userList", list);
		return map;
	}
	
	/**
	 * for flutter android，android app检查更新
	 */
	@RequestMapping(value = "/checkupdate",produces="application/json")
	@ResponseBody
	public String checkUpdate(HttpServletRequest request,HttpServletResponse response){
		File webRootDir = new File("D:\\images");
		File[] files = webRootDir.listFiles();
		if(files == null)
			return "1.0.0";
		for(int i = 0;i < files.length;i++){
			if(files[i].getName().endsWith(".apk")){
				return files[i].getName().substring(4, 9);
			}
		}
		return null;
	}
	
	/**
	 * for flutter，发送验证码
	 */
	@RequestMapping(value = "/getcode",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, String> getCode(
			@RequestParam("phone")String phone,HttpServletRequest request,HttpServletResponse response){
		HashMap<String,String> map = SmsUtil.getInstance().sendSmsReturnErrorCode(phone);
		return map;
	}
	
	/**
	 * for flutter，发送消息
	 */
	@RequestMapping(value = "/sendmsg",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public void sendMsg(HttpServletRequest request,HttpServletResponse response){
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		ChatEntity chat = JSONObject.parseObject(req.getParameter("msg"), ChatEntity.class);
		NewMsgListDao.insertMessage(chat);
	}
	
	/**
	 * for flutter,检查是否有过注册
	 */
	@RequestMapping(value="/checkaccount",produces="application/json")
	@ResponseBody
	public boolean checkAccount(@RequestParam("account")String account,@RequestParam(value = "islogin",required = false)boolean isLogin,HttpServletRequest request,HttpServletResponse response){
		return UserDao.selectAccount(account,isLogin);
	}
	
	/**
	 * for flutter,查询自己的好友
	 */
	@RequestMapping(value="/friend",produces="application/json")
	@ResponseBody
	public HashMap<String, ArrayList<User>> getFriend(@RequestParam("userid")int userId,HttpServletRequest request,HttpServletResponse response){
		ArrayList<User> list = FriendDao.getFriend(userId);
		HashMap<String,ArrayList<User>> map = new HashMap<>();
		map.put("userList", list);
		return map;
	}
	
	/**
	 * for flutter,保存用户信息
	 */
	@RequestMapping(value="/saveinfo",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public int saveInfo(HttpServletRequest request,HttpServletResponse response){
		User user = new Gson().fromJson(request.getParameter("user"), User.class);
		return UserDao.saveUserInfo(user);
	}
	
	/**
	 * for flutter,修改头像
	 */
	@RequestMapping(value="/userhead",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public int changeAvatar(@RequestParam("file")MultipartFile file,@RequestParam("userId")int userId,
			HttpServletRequest request,HttpServletResponse response){
		int result = 0;
		if(file != null){
			String path = "D:\\images" + File.separator + userId;
			File parent = new File(path);
			if (!parent.exists()) {
				parent.mkdirs();
			}
			String completePath = path + File.separator + "0.jpg";
			File pic = new File(completePath);
			try {
				if (!pic.exists()) {
					pic.createNewFile();
				}
				file.transferTo(pic);
				result = 1;
			} catch (IOException e) {
				e.printStackTrace();
				result = -1;
			}
		}
		return result;
	}
	
	/**
	 * for flutter,更改用户密码
	 */
	@RequestMapping(value="/changepassword",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public int changePassword(@RequestParam("userId") int userId,@RequestParam("password") String password,
			HttpServletRequest request,HttpServletResponse response){
		return UserDao.updatePassword(userId,password);
	}
	
	/**
	 * for flutter,是否匿名
	 */
	@RequestMapping(value="/changeshow",produces="application/json")
	@ResponseBody
	public int changePassword(@RequestParam("userId") int userId,@RequestParam("show") boolean show,
			HttpServletRequest request,HttpServletResponse response){
		return UserDao.changeShow(userId,show);
	}
	
	/** 
	 * for flutter,个人相册图片上传
	 * @param file
	 * @param response
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gallery", method = RequestMethod.POST)
	@ResponseBody
	public int gallery(@RequestParam("file") MultipartFile[] files,
			HttpServletResponse response, HttpServletRequest request) throws Exception {
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		int userId = Integer.parseInt(req.getParameter("userId"));
		System.out.println("files come:"+files.length);
		if (files.length > 0) {
			String photolist = UserDao.getUserPhotosAddress(userId);
			int lastPhoto;
			if (photolist == null || photolist.equals("")) 
				lastPhoto = 0;
			else{
				String[] photoId = photolist.split("\\|");
				lastPhoto = Integer.parseInt(photoId[photoId.length-1]);
			}
			String path = "D:\\images" + File.separator + userId;
			File parent = new File(path);
			if (!parent.exists()) {
				parent.mkdirs();
			}
			StringBuilder builder = new StringBuilder();
			for(int i = 0;i < files.length;i++){
				lastPhoto = lastPhoto + 1;
				System.out.println("last:"+ lastPhoto);
				String pic = path + File.separator + lastPhoto + ".jpg";
				File picFile = new File(pic);
				if (!picFile.exists()) {
					picFile.createNewFile();
				}
				files[i].transferTo(picFile);
				if(i != files.length -1)
					builder.append(lastPhoto + "|");
				else
					builder.append(lastPhoto);
			}
			System.out.println("builder:"+ builder.toString());
			UserDao.updatePhotos(userId,builder.toString());
			return 1;
		}
		return 0;
	}
	
	/**
	 * for flutter,更改用户密码
	 */
	@RequestMapping(value="/deletepic",produces="application/json",method = RequestMethod.GET)
	@ResponseBody
	public int deletePic(@RequestParam("userId") int userId,@RequestParam("index") String index,
			@RequestParam("removedPicId") int removedPicId,HttpServletRequest request,HttpServletResponse response){
		int result = UserDao.deletePic(userId,index);
		String pic = "D:\\images" + File.separator + userId + File.separator + removedPicId + ".jpg";
		File picFile = new File(pic);
		picFile.delete();
		return result;
	}
	
	/**
	 * for flutter,给邀约增加评论并报名,付费版
	 */
	@RequestMapping(value="/addmeetingcomment",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public int addMeetingComments(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		CommentDetail comment = JSONObject.parseObject(req.getParameter("commentDetail"),CommentDetail.class);
		System.out.println("add comment:"+comment.commentContent);
		int id;
		if(commentContent.equals(comment.commentContent))
			id = 1;
		else{
			commentContent = comment.commentContent;
			id = CommentsDao.addComment(true, comment);
			if(id > 0){
				ContributesDao.addCommentCount(comment.commentId);
				UserDao.updateRegist(comment.userId, comment.commentId);
				if(req.getParameter("isVip").equals("false"))
				UserDao.updateScore(comment.userId, -1);
			}
		}
		return id;
	}
	
	/**
	 * for flutter,获取邀约的评论
	 */
	@RequestMapping(value="/getmeetingcomments",produces="application/json",method = RequestMethod.GET)
	@ResponseBody
	public HashMap<String,ArrayList<CommentDetail>> getMeetingComments(@RequestParam("meetingId") int meetingId,
			@RequestParam("count") int count,HttpServletRequest request,HttpServletResponse response){
		ArrayList<CommentDetail> comments = CommentsDao.selectCommentsByCount(true, meetingId, count);
		HashMap<String,ArrayList<CommentDetail>> map = new HashMap<>();
		map.put("comments", comments);
		return map;
	}
	
	/**
	 * for flutter,给反馈添加评论
	 */
	@RequestMapping(value="/addarticlecomment",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public int addArticleComment(HttpServletRequest request,HttpServletResponse response){
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		CommentDetail comment = new Gson().fromJson(req.getParameter("commentDetail"), CommentDetail.class);
		int id = CommentsDao.addComment(false, comment);
		ArticleDao.addCommentCount(comment.commentId);
		return id;
	}
	
	/**
	 * for flutter,获取反馈的评论
	 */
	@RequestMapping(value="/getarticlecomments",produces="application/json",method = RequestMethod.GET)
	@ResponseBody
	public HashMap<String,ArrayList<CommentDetail>> getArticleComments(@RequestParam("articleId") int articleId,
			@RequestParam("count") int count,HttpServletRequest request,HttpServletResponse response){
		ArrayList<CommentDetail> comments = CommentsDao.selectCommentsByCount(false, articleId, count);
		HashMap<String,ArrayList<CommentDetail>> map = new HashMap<>();
		map.put("comments", comments);
		return map;
	}
	
	/**
	 * for flutter,删除评论
	 */
	@RequestMapping(value="/deletecomment",produces="application/json",method = RequestMethod.GET)
	@ResponseBody
	public int deleteComment(@RequestParam("id") int id,@RequestParam("isMeeting") boolean isMeeting,
			@RequestParam("commentId") int commentId,HttpServletRequest request,HttpServletResponse response){
		int res = CommentsDao.deleteComment(isMeeting, id);
		if(isMeeting)
			res = ContributesDao.reduceCommentCount(commentId);
		else
			res = ArticleDao.reduceCommentCount(commentId);
		return res;
	}
	
	/**
	 * for flutter,举报评论
	 */
	@RequestMapping(value="/complaincomment",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public int complainComment(HttpServletRequest request,HttpServletResponse response){
		int res;
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		CommentDetail comment = new Gson().fromJson(req.getParameter("commentDetail"), CommentDetail.class);
		boolean isMeeting = Boolean.valueOf(req.getParameter("ismeeting"));
		res = CommentsDao.updateComplain(isMeeting,comment);
		return res;
	}
	
	/**
	 * for flutter server,获取被举报的评论
	 */
	@RequestMapping(value="/getcomplainedcomment",produces="application/json",method = RequestMethod.GET)
	@ResponseBody
	public HashMap<String, ArrayList<CommentDetail>> getComplainedComment(@RequestParam("count") int count, @RequestParam("ismeeting") boolean isMeeting, HttpServletRequest request, HttpServletResponse response){
		ArrayList<CommentDetail> list = null;
		list = CommentsDao.getComplainedComments(count,isMeeting);
		HashMap<String, ArrayList<CommentDetail>> map = new HashMap<String,ArrayList<CommentDetail>>();
		map.put("comments", list);
		return map;
	}
	
	/**
	 * for flutter,创建一个反馈
	 */
	@RequestMapping(value = "/createarticle",method = RequestMethod.POST)
	@ResponseBody
	public int createArticle(@RequestParam("file") MultipartFile[] files,HttpServletRequest request,HttpServletResponse response
			,ModelMap model){
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		System.out.println(req.getParameter("article").toString());
		Article article = gson.fromJson(req.getParameter("article"), Article.class);
		int articleId = ArticleDao.addArticle(article, files.length);
		if (articleId != -1) {
			System.out.println("have files " + files.length);
			if (files != null && files.length > 0) {
				MultipartFile file;
				for (int i = 0;i < files.length;i++) {
					file = files[i];
					if (!file.isEmpty()) {
						String path = "D:\\images" + File.separator + "article" + File.separator + articleId;
						File parent = new File(path);
						if (!parent.exists()) {
							parent.mkdirs();
						}
						String completePath = path + File.separator + i + ".jpg";
						File pic = new File(completePath);
						try {
							if (!pic.exists()) {
								pic.createNewFile();
							}
							file.transferTo(pic);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return articleId;
		}
		return -1;
	}
	
	/**
	 * for flutter,创建一个举报
	 */
	@RequestMapping(value = "/createcomplain",method = RequestMethod.POST)
	@ResponseBody
	public int createComplain(@RequestParam("file") MultipartFile[] files,HttpServletRequest request,HttpServletResponse response
			,ModelMap model){
		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		Complain complain = gson.fromJson(req.getParameter("complain"), Complain.class);
		int complainId = ComplainDao.addComplain(complain, files.length);
		if (complainId != -1) {
			if (files != null && files.length > 0) {
				MultipartFile file;
				for (int i = 0;i < files.length;i++) {
					file = files[i];
					if (!file.isEmpty()) {
						String path = "D:\\images" + File.separator + "complain" + File.separator + complainId;
						File parent = new File(path);
						if (!parent.exists()) {
							parent.mkdirs();
						}
						String completePath = path + File.separator + i + ".jpg";
						File pic = new File(completePath);
						try {
							if (!pic.exists()) {
								pic.createNewFile();
							}
							file.transferTo(pic);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return complainId;
		}
		return -1;
	}
	
	/**
	 * for flutter,获取举报
	 */
	@RequestMapping(value = "/getcomplain",produces="application/json")
	@ResponseBody
	public HashMap<String, List<Complain>> getComplain(@RequestParam("count")int count,HttpServletRequest request,HttpServletResponse response){
		List<Complain> complains = null;
		complains = ComplainDao.selectComplain(count);
		HashMap<String, List<Complain>> map = new HashMap<String,List<Complain>>();
		map.put("complain", complains);
		return map;
	}
	
	/**
	 * for flutter,获取所有反馈
	 */
	@RequestMapping(value = "/getarticles",produces="application/json")
	@ResponseBody
	public HashMap<String, ArrayList<Article>> getArticles(@RequestParam("userId")int userId,
			@RequestParam("count")int count,@RequestParam("type")int type,HttpServletRequest request,HttpServletResponse response){
		ArrayList<Article> articles = null;
		if (userId != -1)//获取自己的反馈
			articles = ArticleDao.getMyArticles(userId, count);
		else if (type == 0)//根据最新
			articles = ArticleDao.selectArticlesOrderByDate(count);
		else if (type == 1)//根据评论数排序hottest
			articles = ArticleDao.selectArticlesOrderByComments(count);
		else if (type == 2)//根据加精perfect
			articles = ArticleDao.selectPerfectArticles(count);
		HashMap<String, ArrayList<Article>> map = new HashMap<String,ArrayList<Article>>();
		map.put("articles", articles);
		return map;
	}
	
	/**
	 * for flutter,删除邀约或者反馈
	 */
	@RequestMapping(value="/deletemeeting",produces="application/json",method = RequestMethod.POST)
	@ResponseBody
	public int deleteMeeting(@RequestParam("meetingid") int meetingId,@RequestParam("ismeeting") boolean isMeeting,
			HttpServletRequest request,HttpServletResponse response){
		int res;
		if(isMeeting)
			res = ContributesDao.deleteMeeting(meetingId);
		else
			res = ArticleDao.deleteArticle(meetingId);
		return res;
	}
	
	/**
	 * for flutter,删除邀约或者反馈
	 */
	@RequestMapping(value="/getscore",produces="application/json")
	@ResponseBody
	public int getScore(@RequestParam("id") int id, HttpServletRequest request,HttpServletResponse response){
		int score = UserDao.getScore(id);
		return score;
	}
	
	/**
	 * for flutter,获取商品列表
	 */
	@RequestMapping(value="/getgoods",produces="application/json")
	@ResponseBody
	public Map<String, List<GoodsBean>> getGoods(HttpServletRequest request,HttpServletResponse response){
		List<GoodsBean> goodsList = GoodsDao.getGoodsList();
		HashMap<String, List<GoodsBean>> map = new HashMap<String, List<GoodsBean>>();
		map.put("goodsList", goodsList);
		return map;
	}
	
	/**
	 * for flutter,获取商品列表
	 */
	@RequestMapping(value="/newlook",produces="application/json")
	@ResponseBody
	public int newLook(@RequestParam("id") int goodsId, HttpServletRequest request,HttpServletResponse response){
		GoodsDao.addNewLook(goodsId);
		return 1;
	}
	
	/**
	 * for flutter,获取聊天列表
	 */
	@RequestMapping(value="/getmsglist",produces="application/json")
	@ResponseBody
	public Map<String,List<Message>> getMsgList(@RequestParam("id") int userId, HttpServletRequest request,HttpServletResponse response){
		List<Message> msgList = NewMsgListDao.queryMessageList(userId);
		HashMap<String,List<Message>> map = new HashMap<String,List<Message>>();
		map.put("list", msgList);
		return map;
	}
	
	/**
	 * for flutter,获取未读消息数量
	 */
	@RequestMapping(value="/queryifunread",produces="application/json")
	@ResponseBody
	public int queryIfUnread(@RequestParam("user_id") int userId, HttpServletRequest request,HttpServletResponse response){
		int unread = NewMsgListDao.queryIfUnread(userId);
		return unread;
	}
	
	/**
	 * for flutter,删除和某人的聊天
	 */
	@RequestMapping(value="/deletemsglist",produces="application/json")
	@ResponseBody
	public void deleteMsgList(@RequestParam("user_id") int userId, @RequestParam("another_id") int anotherId, HttpServletRequest request,HttpServletResponse response){
		NewMsgListDao.deleteMessage(userId, anotherId);
	}
	
	/**
	 * for flutter,设置和某人的聊天已读
	 */
	@RequestMapping(value="/clearunread",produces="application/json")
	@ResponseBody
	public void clearUnread(@RequestParam("user_id") int userId, @RequestParam("another_id") int anotherId, HttpServletRequest request,HttpServletResponse response){
		NewMsgListDao.readMessage(userId, anotherId);
	}
	
	/**
	 * for flutter,获取和某人聊天记录
	 */
	@RequestMapping(value="/getchatlist",produces="application/json")
	@ResponseBody
	public Map<String,List<ChatEntity>> getChatList(@RequestParam("id") int userId, @RequestParam("receive_id") int receive_id,HttpServletRequest request,HttpServletResponse response){
		List<ChatEntity> msgList= NewChatListDao.selectHistoryChat(userId,receive_id);
		HashMap<String,List<ChatEntity>> map = new HashMap<String,List<ChatEntity>>();
		map.put("list", msgList);
		return map;
	}
	
	/**
	 * for flutter,创建一个订单并且跳转url
	 */
	@RequestMapping(value="/createorder",produces="application/json")
	@ResponseBody
	public void createOrder(@RequestParam("userid") int userId,@RequestParam("price") int price,@RequestParam("remark") String remark,
			HttpServletRequest request,HttpServletResponse response){
		String token = "uAq9Uxb64dPitb3HMJrPrRPYHKWeCFVv"; //记得更改 http://codepay.fateqq.com 后台可设置
		String codepay_id ="229949" ;//记得更改 http://codepay.fateqq.com 后台可获得

//		String price=request.getParameter("price"); //表单提交的价格
//		String type=request.getParameter("type"); //支付类型  1：支付宝 2：QQ钱包 3：微信
//		String pay_id=request.getParameter("pay_id"); //支付人的唯一标识
//		String param=request.getParameter("param"); //自定义一些参数 支付后返回
		
		String notify_url="http://103.244.2.254:8080/qiqiim-server/payresult";//通知地址
//		String return_url="";//支付后同步跳转地址

		//参数有中文则需要URL编码
		String url="http://codepay.fateqq.com:52888/creat_order?id="
				+codepay_id+"&pay_id="+userId+"&price="+price+"&type="+1+"&token="+token
				+"&param="+remark+"&notify_url="+notify_url;//+"&return_url="+return_url;

		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * for flutter,用户支付结果通知地址
	 */
	@RequestMapping(value="/payresult",produces="text/html",method = RequestMethod.POST)
	@ResponseBody
	public void payResult(HttpServletRequest request,HttpServletResponse response){
		/**
		*验证通知 处理自己的业务
		*/
//		String key = "nMVsggorZ4XPr2VwXja5WiMtpJTwjaGq"; //记得更改 http://codepay.fateqq.com 后台可设置
//		Map<String,String> params = new HashMap<String,String>(); //申明hashMap变量储存接收到的参数名用于排序
		Map<String, String[]> requestParams = request.getParameterMap(); //获取请求的全部参数
		System.out.println("receive response:"+requestParams.get("money")[0]);
//		String valueStr = ""; //申明字符变量 保存接收到的变量
		String[] pay_no = requestParams.get("pay_no");
		String result;
		if(pay_no[0].length() == 0){
			result = "fail";
		}else{
			String[] id = requestParams.get("pay_id");
			String[] money = requestParams.get("money");
			int int_money = Double.valueOf(money[0]).intValue();
			if(int_money >= 199){//充值的是会员
				UserDao.makeUserVIP(Integer.parseInt(id[0]));
			}else{
				System.out.println("id:"+id[0]);
				System.out.println("price:"+id[0]);
				UserDao.updateScore(Integer.parseInt(id[0]), int_money);
			}
			result = "ok";
		}
		PrintWriter pw =null;
        try {
            pw = response.getWriter();
            pw.write(result);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            pw.close();
        }
//		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
//			String name = (String) iter.next();
//			System.out.println("key:"+name);
//			String[] values = (String[]) requestParams.get(name);
//			valueStr = values[0];
//			System.out.println("value:"+valueStr);
			//乱码解决，这段代码在出现乱码时使用。如果sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
//			params.put(name, valueStr);//增加到params保存
//		}
//		List<String> keys = new ArrayList<String>(params.keySet()); //转为数组
//	  	Collections.sort(keys); //重新排序
//		String prestr = "";
//		String sign= params.get("sign"); //获取接收到的sign 参数
//		
//	        for (int i = 0; i < keys.size(); i++) { //遍历拼接url 拼接成a=1&b=2 进行MD5签名
//	            String key_name = keys.get(i);
//	            String value = params.get(key_name);
//		    	if(value== null || value.equals("") ||key_name.equals("sign")){ //跳过这些 不签名
//		    		continue;
//		    	}
//		    	if (prestr.equals("")){
//		    		prestr =  key_name + "=" + value;
//		    	}else{
//					prestr =  prestr +"&" + key_name + "=" + value;
//		    	}
//	        }
//		MessageDigest md = null;
//		try {
//			md = MessageDigest.getInstance("MD5");
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//		md.update((prestr+key).getBytes());
//		String  mySign = new BigInteger(1, md.digest()).toString(16).toLowerCase();
//		if(mySign.length()!=32)mySign="0"+mySign;
//		if(mySign.equals(sign)){ 
			//编码要匹配 编码不一致中文会导致加密结果不一致
			//参数合法处理业务
//			request.getParameter("pay_no"); //流水号
//			request.getParameter("pay_id"); //用户唯一标识
//			request.getParameter("money"); //付款金额
//			request.getParameter("price"); //提交的金额
//			System.out.println("success");
//		}else{
//			//参数不合法
//			System.out.println("fail");
//		}
	}
}
