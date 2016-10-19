package com.show.specialshow;

public interface URLs {
	// 测试环境
//	public static final String BASE_URL = "http://112.124.3.211:10086/Android";
//	public static final String BASE_IMAGE_URL = "http://112.124.3.211:10086/";
	//生产环境
	String BASE_URL = "http://m.teshow.com/Android";
	String BASE_IMAGE_URL = "http://m.teshow.com/";
	/**
	 * 注册
	 */
	String REGISTER_URL = BASE_URL + "/Login/register";
	/**
	 * 发送短信验证码
	 */
	String SMS_CODE_URL = BASE_URL + "/Login/sendphone";
	/**
	 * 找回登录密码
	 */
	String GETBACK_LOGIN_PASSWORD = BASE_URL
			+ "/Login/findpass";
	/**
	 * 登录
	 */
	String LOGIN_URL = BASE_URL + "/Login/login";

	/**
	 * 按地址搜索商铺
	 */
	String SEARCH_SHOP = BASE_URL + "/Shop/index";

	/**
	 * 发布动态
	 */
	String SEND_DYNAMIC = BASE_URL + "/Publish/post";
	/**
	 * 发布秀卡
	 */
	String SEND_SHOW_CARD=BASE_URL+"/Publish/show";
	/**
	 * 发布蹭卡
	 */
	String SEND_BORROW_CARD=BASE_URL+"/Publish/Rub";
	/**
	 * 发布点评
	 */
	String SEND_REVIEW=BASE_URL+"/Publish/review";
	/**
	 * 获得首页动态信息
	 */
	String GET_CIRCLE_DYNAMIC=BASE_URL+"/Shop/info";
	/**
	 * 商户详情
	 */
	String SHOP_DETAILS=BASE_URL+"/Shop/detail";
	/**
	 * 秀坊列表
	 */
	String SHOP_SHOPLIST=BASE_URL+"/Shop/shopList";
	/**
	 * 关注用户
	 */
	String ATTENTION_USER=BASE_URL+"/Space/attention";
	/**
	 * 秀坊商户详情秀卡，蹭卡，点评数据
	 */
	String SHOP_CARD=BASE_URL+"/Shop/card";
	/**
	 * 商户收藏
	 */
	String SHOP_COLLECT=BASE_URL+"/Attention/collect";
	/**
	 * 秀客列表
	 */
	String SPACE_SHOW=BASE_URL+"/Space/show";
	/**
	 * 秀客详情数据
	 */
	String USER_INFO=BASE_URL+"/User/info";
	/**
	 * 秀客秀卡，蹭卡，动态数据
	 */
	String USER_DETAIL=BASE_URL+"/User/detail";
	/**
	 * 获取评论列表
	 */
	String GET_DYNAMIC_COMMENT=BASE_URL+"/Attention/lists";
	/**
	 * 手艺人(员工)详情页数据
	 */
	String APPOINTMENT_STAFF=BASE_URL+"/Appointment/staff";
	/**
	 * 手艺人(员工)详情页秀友点评数据
	 */
	String APPOINMENT_LISTS=BASE_URL+"/Appointment/lists";
	/**
	 * 对手艺人点赞
	 */
	String ATTENTION_STAFFHIT=BASE_URL+"/Attention/staffHit";
	/**
	 * 免费预约服务
	 */
	String APPOINMENT_ADD=BASE_URL+"/Appointment/add";
	/**
	 * 对手艺人点评
	 */
	String APPOINMENT_POST=BASE_URL+"/Appointment/post";
	/**
	 * 更新用户资料
	 */
	String SPACE_SAVEINFO=BASE_URL+"/Space/saveInfo";
	/**
	 * 个人基本资料
	 */
	String SPACE_INDEX=BASE_URL+"/Space/index";
	/**
	 * 手艺人名片资料数据
	 */
	String USER_CARDINFO=BASE_URL+"/User/cardInfo";
	/**
	 * 修改手艺人名片资料
	 */
	String USER_CARDSAVE=BASE_URL+"/User/cardSave";
	/**
	 * 我的服务预约列表数据
	 */
	String APPOINMENT_APPOINTLIST=BASE_URL+"/Appointment/appointList";
	/**
	 * 取消我的服务
	 */
	String APPOINMENT_APPOINTREMOVE=BASE_URL+"/Appointment/appointRemove";
	/**
	 * 删除 我的服务
	 */
	String APPOINMENT_APPOINTDEL=BASE_URL+"/Appointment/appointDel";
	/**
	 * 用户对动态点赞
	 */
	String ATTENTION_HIT=BASE_URL+"/Attention/Hit";
	/**
	 * 对动态评论
	 */
	String ATTENTION_COMMENT=BASE_URL+"/Attention/Comment";
	/**
	 * 我的相册动态  
	 */
	String MYPHOTO_INFO=BASE_URL+"/Myphoto/info";
	/**
	 *我的相册动态 删除  
	 */
	String MYPHOTO_INFODEL=BASE_URL+"/Myphoto/infoDel";
	/**
	 * 我的收藏店铺数据
	 */
	String USER_COLLECTLIST=BASE_URL+"/User/collectList";
	/**
	 * 手艺人作品
	 */
	String USER_PRODUCTION=BASE_URL+"/User/production";
	/**
	 * 手艺人作品删除
	 */
	String USER_PRODUCTIONDEL=BASE_URL+"/User/productionDel";
	/**
	 * 手艺人作品添加
	 */
	String USER_PRODUCTIONADD=BASE_URL+"/User/productionAdd";
	/**
	 * 关于我们
	 */
	String ABOUS_US=BASE_IMAGE_URL+"index.php?g=User&m=Merchant&a=about";
	/**
	 * 商户中心
	 */
	String BUSINESS_CENTER=BASE_IMAGE_URL+"index.php?g=User&m=Merchant&a=merchant&uid=";
	/**
	 * 成为手艺人链接
	 */
	String BECOME_CRAFTSMAN=BASE_IMAGE_URL+"index.php?g=User&m=Merchant&a=type";
	/**
	 * 获取聊过天的人的头像和昵称等数据
	 */
	String SPACE_GETUSERBYID=BASE_URL+"/space/getUserById";
	/**
	 * 根据输入的关键字找到要添加的人
	 */
	String SPACE_GETUSERBYKEYWORD=BASE_URL+"/Space/getUserBykeyword";
	/**
	 * 同意别人的好友请求的请求接口
	 */
	String SPACE_ADDFRIEND=BASE_URL+"/Space/addFriend";
	/**
	 * 删除好友请求接口
	 */
	String SPACE_DELETEFRIEND=BASE_URL+"/Space/deleteFriend";
	/**
	 * 邀请记录
	 */
	String  USER_INVITELIST=BASE_URL+"/User/inviteList";
	/**
	 * 是否有通知信息,大于0有未读消息
	 */
	String  USER_ISMESSAGE=BASE_URL+"/User/isMessage";
	/**
	 * 用户已读与未读消息
	 */
	String USER_MESSAGE=BASE_URL+"/User/message";
	/**
	 * 未读消息变为已读消息请求接口
	 */
	String USER_UPDATEMESSAGE=BASE_URL+"/User/updateMessage";
	/**
	 * 获取好友,关注,粉丝数及详情
	 */
	String USER_USERNUM=BASE_URL+"/User/userNum";
	/**
	 * 获取banner数据
	 */
	String LOGIN_BANNER=BASE_URL+"/login/banner";
	/**
	 * 附近秀坊地图数据
	 */
	String SHOP_NEARSHOP=BASE_URL+"/Shop/nearShop";
	/**
	 * 分店列表数据
	 */
	String SHOP_SHOPBRANCH=BASE_URL+"/Shop/shopBranch";
	/**
	 * 手艺人列表数据
	 */
	String SPACE_STAFFLIST=BASE_URL+"/Space/staffList";
	/**
	 * 更新用户当前坐标
	 */
	String USER_UPDATEXY=BASE_URL+"/user/updateXy";
	/**
	 * qq,weixin第三方登陆接口
	 */
	String QQ_LOGIN=BASE_URL+"/Qq/login";
	/**
	 * 签到页
	 */
	String SIGN_IN=BASE_IMAGE_URL+"index.php?g=User&m=Sign&a=index&uid=";
	/**
	 * 特秀活动，官方赛事，行业动态数据
	 */
	String POSTS_POSTSLIST=BASE_URL+"/Posts/postslist";
}

