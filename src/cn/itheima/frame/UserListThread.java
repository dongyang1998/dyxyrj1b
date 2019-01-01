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
	//��Ϣ���漯�ϣ��洢��1).���˸��ҷ�����Ϣ��2).�ҷ�������Ϣ
	public static List<String> msgList = new ArrayList<>();
	//�洢�����Ѵ򿪵����촰�ڶ���
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
			//1.���շ���������Ϣ
			BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			//2.��ѭ������
			while(true){
				String row = in.readLine();
				String strFlag = row.substring(0,1);
				if("1".equals(strFlag)){//�����б���ʽ��1#�û���=IP@�û���=IP@�û���=IP....
					String strList = row.substring(2);//strList ="�û���=IP@�û���=IP@�û���=IP"
					//ʹ��@�ָ�
					String[] msgArray = strList.split("@");
					//����һ��Model--��JList�洢��Ϣ����ʾ�б�
					DefaultListModel model = new DefaultListModel();
					//�����ղŵ�����
					for(String str : msgArray){//str = "�û���=IP"
						String[] arr = str.split("=");
						//��֤��������Լ�������ʾ���û��б���
						if(this.loginName.equals(arr[0]) &&
								this.socket.getInetAddress().getHostAddress().equals(arr[1])){
							//����ѭ��
							continue;
						}
						model.addElement(arr[0] + "   " + arr[1]);
					}
					//ΪJList�����µ�Model
					this.jList.setModel(model);
				}else if("3".equals(strFlag)){//������Ϣ����ʽ��3#����������@������IP@��Ϣ����@���շ�����@���շ�IP@����ʱ��
					String msg = row.substring(2);//msg = "����������@������IP@��Ϣ����@���շ�����@���շ�IP@����ʱ��"
					//�洢��������
					UserListThread.msgList.add(msg);

					//�и��ַ���
					String[] msgArray = msg.split("@");
					
					//�������ϣ��ж��Ƿ��Ѵ���Է����촰��
					for(int i = 0;i < UserListThread.chatFrameList.size() ; i++){
						ChatFrame cf = UserListThread.chatFrameList.get(i);
						//�����toName��toIP�Ƿ�����Ϣ�е�"������������IP"�Ƿ�ƥ��
						if(cf.getToName().equals(msgArray[0]) &&
								cf.getToIP().equals(msgArray[1])){
							//�ڴ˴�������ʾ��Ϣ
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
