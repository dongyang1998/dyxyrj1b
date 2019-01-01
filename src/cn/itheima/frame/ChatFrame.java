package cn.itheima.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import cn.itheima.utils.ChatUtils;

public class ChatFrame extends JFrame {

	private JPanel contentPane;
	private String loginName;
	private Socket socket;
	private String toName;
	private String toIP;
	
	private JTextArea textArea;
	private JTextPane textPane;
	

	

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




	public String getToName() {
		return toName;
	}




	public void setToName(String toName) {
		this.toName = toName;
	}




	public String getToIP() {
		return toIP;
	}




	public void setToIP(String toIP) {
		this.toIP = toIP;
	}




	public JTextArea getTextArea() {
		return textArea;
	}




	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}




	public JTextPane getTextPane() {
		return textPane;
	}




	public void setTextPane(JTextPane textPane) {
		this.textPane = textPane;
	}




	/**
	 * Create the frame.
	 * @param userArray2 
	 * @param userArray 
	 * @param socket 
	 * @param loginName 
	 */
	public ChatFrame(String loginName, Socket socket, String toName, String toIP) {
		this.loginName = loginName;
		this.socket = socket;
		this.toName = toName;
		this.toIP = toIP;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10);
		contentPane.setLayout(layout);
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 40));
		contentPane.add(panel, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(140, 10));
		contentPane.add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(ChatFrame.class.getResource("/cn/itheima/frame/h.png")));
		panel_1.add(lblNewLabel, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_3.setPreferredSize(new Dimension(10, 150));
		panel_2.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		panel_4.setPreferredSize(new Dimension(10, 20));
		panel_3.add(panel_4, BorderLayout.NORTH);
		
		JPanel panel_5 = new JPanel();
		panel_5.setPreferredSize(new Dimension(10, 40));
		panel_3.add(panel_5, BorderLayout.SOUTH);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_6 = new JPanel();
		panel_6.setPreferredSize(new Dimension(300, 10));
		panel_5.add(panel_6, BorderLayout.EAST);
		panel_6.setLayout(null);
		
		JButton button = new JButton("\u5173   \u95ED");
		button.setBounds(25, 10, 93, 23);
		panel_6.add(button);
		
		JButton button_1 = new JButton("\u53D1   \u9001");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMsg();
			}
		});
		button_1.setBounds(207, 10, 93, 23);
		panel_6.add(button_1);
		
		this.textArea = new JTextArea();
		textArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_3.add(textArea, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane, BorderLayout.CENTER);
		
		this.textPane = new JTextPane();
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		
		//1.���ñ���
		this.setTitle("�롾 " + this.toName + "  " + this.toIP + " �� ������...");
		//2.������������
		this.setLocationRelativeTo(null);
		//3.���������ı�����ս���
		textArea.requestFocus();
		
		//4.����Ϣ�����м���������Ϣ������ʾ�������¼��
		List<String> msgList = UserListThread.msgList;
		for(String msg : msgList){//��ʽ������������@������IP@��Ϣ����@���շ�����@���շ�IP@����ʱ��
			//����
			String[] arr = msg.split("@");
			if(arr[0].equals(this.loginName)){//�ҷ�����--��ʾ���ұ�
				msg = ChatUtils.makeMsg(2, msg);
				ChatUtils.append(this.textPane, msg);
				
			}else{//���˷����ҵ�
				msg = ChatUtils.makeMsg(1, msg);
				ChatUtils.append(this.textPane, msg);
			}
		}
		
	}




	protected void sendMsg() {
		//1.��ȡ������Ϣ
		String msg = this.textArea.getText();
		//2.��������˷��ͣ���ʽ��3#����������@������IP@��Ϣ����@���շ�����@���շ�IP@����ʱ��
		StringBuilder bld = new StringBuilder("3#");
		bld.append(this.loginName)
		.append("@")
		.append(this.socket.getInetAddress().getHostAddress())
		.append("@")
		.append(msg)
		.append("@")
		.append(this.toName)
		.append("@")
		.append(this.toIP)
		.append("@")
		.append(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		
		
		//����
		try {
			PrintWriter out = new PrintWriter(this.socket.getOutputStream(),true);
			out.println(bld.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		//����Ϣ�洢�����漯����
		UserListThread.msgList.add(bld.substring(2));
		//��ʾ���Լ��������¼������
		String strMsg = ChatUtils.makeMsg(2, bld.toString().substring(2));
		ChatUtils.append(this.textPane, strMsg);
		
		//���������Ϣ
		this.textArea.setText("");
		//��ȡ����
		this.textArea.requestFocus();
	}	

}
