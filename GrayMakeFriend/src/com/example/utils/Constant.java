package com.example.utils;

import com.example.model.UserInfo;

public class Constant {
	public static boolean Debug=true;
	
	public static String UserId;
	
	public static String tokenId;
	
	public static UserInfo userinfo;
	

	public static String getUserId() {
		return UserId;
	}

	public static void setUserId(String id){
		
		Constant.UserId=id;
		
	}
	
	public static String getToken() {
		return tokenId;
	}

	public static void setToken(String tokenId) {
		Constant.tokenId = tokenId;
	}





	public static final String ContextUrl = "http://10.141.70.240:8080/spcms/mobile/";




	
	public static String buildUrl(String suffix){
		return ContextUrl +"news/"+ suffix + ".jspx";
	}
	
	public static String buildUrl2(String suffix){
		return ContextUrl +"forum/"+ suffix + ".jspx";
	}
	public static String buildFriendsUrl(String suffix){
		return ContextUrl +"friends/"+ suffix + ".jspx";
	}
	
	public static String buildUrl(String suffix, String namespace){
		return ContextUrl + namespace +"/"+ suffix + ".jspx";
	}
	
	
	/**
	 * ��ȡ������Ŀ ��������� �������� ��
	 * 
	 */
	public static String getCategoryList = buildUrl("getCategoryList");
	
	/**
	 * ������Ŀid��ȡ��Ӧ����
	 * 
	 * @param id
	 *            ��Ŀid
	 * @param type �������� ��ͨ1 ͼƬ 2 ���� 3 ��������Ϊ"1,2,3" ��ѯ3����������
	 * @param order ��������˵�� Ŀǰ����Ϊ3 �����������
	 * @param pageSize
	 * @param pageNow
	 */
	public static String getNewsListByCategoryId = buildUrl("getNewsListByCategoryId");
	
	/**
	 * ��ȡ���Ŷ�Ӧid�µ���ϸ����
	 * 
	 * @param id ��������id
	 */
	public static String  getNewsById = buildUrl("getNewsById");
	
	/**
	 * ��ȡ��Ӧ����id�µ�����
	 * 
	 * @param id
	 * @param pageSize
	 * @param pageNow
	 */
	public static String getCommentListByNewsId = buildUrl("getCommentListByNewsId");
	
	/**
	 * ��������
	 * 
	 * @param userId
	 *            �û�id
	 * @param text
	 *            ��������
	 * @param tokenId
	 *            Ŀǰδ��������Ϊ��
	 * @param newsId
	 *            ����id
	 */
	public static String commitNewsComment = buildUrl("commitNewsComment");
	
	/**
	 * �ظ����� (����)
	 * @param userId
	 * @param tokenId
	 * @param text
	 */
	public static String replyNews = buildUrl("replyNews");
	
	public static String getSectionList = buildUrl2("getSectionList"); 
	
	/*  �ϴ�ͼƬ*/
	public static String uploadImage = buildFriendsUrl("o_upload_image_mobile");
	/*  ����ID��ȡ�û�������Ϣ*/
	public static String getBaseInfoById = buildFriendsUrl("getBaseInfoById");
/*  ����ID��ȡ�û���ϸ��Ϣ*/
	public static String getUserInfoById = buildFriendsUrl("getDetailById");
	/*  ����ID��ȡ��������*/
	public static String getConditionsById = buildFriendsUrl("getConditionsById");
	/*  ����ID��ȡ����������Ϣ*/
	public static String getLifeInfo = buildFriendsUrl("getLifeInfo");
	/*�޸��û���ϸ��Ϣ*/
	public static String updateUserInfo = buildFriendsUrl("updateUserInfo");
	/*�޸���������*/
	public static String updateConditions = buildFriendsUrl("updateConditions");
	/*�޸ĸ���������Ϣ*/
	public static String updateLifeInfo = buildFriendsUrl("updateLifeInfo");
	/*�޸�ͷ��*/
	public static String updateUserImage = buildFriendsUrl("updateUserImage");
	/*ע�ύ���û�*/
	public static String register = buildFriendsUrl("register");
	/*��½*/
	public static String login = buildFriendsUrl("login");
	/*�޸�����*/
	public static String changePassword = buildFriendsUrl("changePassword");
	
	/**
	 *��ȡ�û����б���Ϣ
	 * @param 
	 * @author AGY
	 */
	public static String getUsersList = buildFriendsUrl("getAllUsersInfo");
	
	/**
	 * ��ȡ��ǰ�û���˽��
	 * @author Administrator//agy
	 */
	public static String getUserSiXinList = buildFriendsUrl("getMessagesById");
	
	/**
	 * ����˽��
	 * @author Administrator//agy
	 */
	public static String sendMessageById = buildFriendsUrl("sendMessageById");
	/**
	 * �ı�˽��״̬Ϊ�Ѷ�
	 * @author Administrator//agy
	 */
	public static String readMessageById = buildFriendsUrl("readMessageById");
	
	/**
	 * ��ȡ��ǰ�û���ע���û�
	 * @author agy
	 */
	public static String getUserCareList = buildFriendsUrl("getUserCareFriend");
	

	public static String saveInfo = buildUrl("saveInfo", "info");

	/**
	 * @author agy   ��ȡ�û���ϸ��Ϣ��url
	 */
	public static String getUserInfoDetails = buildFriendsUrl("getUserInfoDetails");
	
	/**
	 * @author agy  �����ע����
	 * 
	 */
	public static String addFriend = buildFriendsUrl("addCareFriend");
	
	/**
	 * @author agy ȡ����ע����
	 */
	public static String removeFriend = buildFriendsUrl("removeCarefriend");
	/**
	 * @author agy  ��ѯ�Ƿ��Ѿ��Ǻ���
	 */
	public static String checkIsFriend = buildFriendsUrl("checkCareFriend");
	/**
	 * @author agy  ��ѯ�Ƿ��Ѿ��Ǻ���
	 */
	public static String selectUsersList = buildFriendsUrl("selectUsersList");
	
}
