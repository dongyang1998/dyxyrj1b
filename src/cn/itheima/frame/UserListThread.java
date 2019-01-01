package cn.itheima.frame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import cn.itheima.utils.ChatUtils;

public class UserListThread extends Thread {
	//消息缓存集合，存储：1).别人给我发的信息；2).我发出的信息
	public static List<String> msgList = new ArrayList<>();
	//存储所有已打开的聊天窗口对象
	public static List<ChatFrame> chatFrameList = new ArrayList<>();
	
	private String loginName;
	private Socket socket;
	private JList jList;
	public UserListThread(String loginName, Socket socket, JList jList) {
		super();
		this.loginName = loginName;
		this.socket = socket;
		this.jList = jList;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public JList getjList() {
		return jList;
	}
	public void setjList(JList jList) {
		this.jList = jList;
	}
	
	@Override
	public void run() {
		try {
			//1.接收服务器端信息
			BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			//2.死循环接收
			while(true){
				String row = in.readLine();
				String strFlag = row.substring(0,1);
				if("1".equals(strFlag)){//好友列表，格式：1#用户名=IP@用户名=IP@用户名=IP....
					String strList = row.substring(2);//strList ="用户名=IP@用户名=IP@用户名=IP"
					//使用@分隔
					String[] msgArray = strList.split("@");
					//创建一个Model--给JList存储信息，显示列表
					DefaultListModel model = new DefaultListModel();
					//遍历刚才的数组
					for(String str : msgArray){//str = "用户名=IP"
						String[] arr = str.split("=");
						//验证，如果是自己，不显示在用户列表上
						if(this.loginName.equals(arr[0]) &&
								this.socket.getInetAddress().getHostAddress().equals(arr[1])){
							//继续循环
							continue;
						}
						model.addElement(arr[0] + "   " + arr[1]);
					}
					//为JList设置新的Model
					this.jList.setModel(model);
				}else if("3".equals(strFlag)){//聊天信息，格式：3#发送者名称@发送者IP@消息内容@接收方名称@接收方IP@发送时间
					String msg = row.substring(2);//msg = "发送者名称@发送者IP@消息内容@接收方名称@接收方IP@发送时间"
					//存储到缓存中
					UserListThread.msgList.add(msg);

					//切割字符串
					String[] msgArray = msg.split("@");
					
					//遍历集合，判断是否已打开与对方聊天窗口
					for(int i = 0;i < UserListThread.chatFrameList.size() ; i++){
						ChatFrame cf = UserListThread.chatFrameList.get(i);
						//窗体的toName和toIP是否与信息中的"发送者姓名和IP"是否匹配
						if(cf.getToName().equals(msgArray[0]) &&
								cf.getToIP().equals(msgArray[1])){
							//在此窗体上显示信息
							msg = ChatUtils.makeMsg(1, msg);
							ChatUtils.append(cf.getTextPane(), msg);
							break;
						}
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
