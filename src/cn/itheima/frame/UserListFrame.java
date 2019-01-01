package cn.itheima.frame;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.itheima.utils.ChatUtils;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;

public class UserListFrame extends JFrame {

	private JPanel contentPane;
	private String loginName;
	private Socket socket;
	private JList list;
	

	/**
	 * Create the frame.
	 * @param socket 
	 * @param loginName 
	 */
	public UserListFrame(String loginName, Socket socket) {
		this.loginName = loginName;
		this.socket = socket;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 230, 387);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 60));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		String msg = ChatUtils.getText("�Լ���" + this.loginName, "�����б�");
		JLabel label = new JLabel(msg);
		
		panel.add(label, BorderLayout.CENTER);
		
		list = new JList();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Ĭ���Ǵ���"�����¼�"��������Ҫʶ��˫��"
				if(e.getClickCount() == 2){
					//�����ⲿ����
					toOpenChatFrame();
				}
			}
		});
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"\u5434\u5F66\u7956  [192.168.100.101]", "\u6210\u9F99  [192.168.100.102]"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		contentPane.add(list, BorderLayout.CENTER);
		
		//1.���ñ��⣺
		this.setTitle("�����б�");
		//2.�����߳�
		new UserListThread(this.loginName,this.socket,list).start();
		
	}



	protected void toOpenChatFrame() {
		//1.��ȡ������Ϣ
		String user = (String) this.list.getSelectedValue();//user = "����   127.0.0.1"
		String[] userArray = user.split("   ");
		
		//2.��ʾ���촰��
		ChatFrame chatFrame = new ChatFrame(this.loginName,this.socket,userArray[0],userArray[1]);
		chatFrame.setVisible(true);
		
		//3.�����촰�ڴ洢��������
		UserListThread.chatFrameList.add(chatFrame);
	}

}
