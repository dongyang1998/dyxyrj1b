package cn.itheima.frame;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import cn.itheima.utils.InitListener;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtIP;
	private JTextField txtPort;
	private JTextField txtLoginName;
	private Socket socket = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//����λ�ã���С(x,y,width,height)
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblip = new JLabel("\u670D\u52A1\u5668IP\uFF1A");
		lblip.setHorizontalAlignment(SwingConstants.RIGHT);
		lblip.setBounds(51, 44, 84, 21);
		contentPane.add(lblip);
		
		txtIP = new JTextField();
		txtIP.setText("127.0.0.1");
		txtIP.setBounds(138, 44, 188, 21);
		contentPane.add(txtIP);
		txtIP.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("\u670D\u52A1\u5668\u7AEF\u53E3\uFF1A");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(51, 94, 84, 15);
		contentPane.add(lblNewLabel);
		
		
		txtPort = new JTextField();
		txtPort.setText("10000");
		txtPort.setBounds(138, 91, 114, 21);
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		
		JLabel label = new JLabel("\u6635\u79F0\uFF1A");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(81, 144, 54, 15);
		contentPane.add(label);
		
		txtLoginName = new JTextField();
		txtLoginName.setText("\u738B\u601D\u806A");
		txtLoginName.setBounds(138, 141, 114, 21);
		contentPane.add(txtLoginName);
		txtLoginName.setColumns(10);
		
		JButton button = new JButton("\u767B    \u9646");
		//��¼��ť
		button.addActionListener(new ActionListener() {
			//���Լ�����ť�ĵ����¼���һ��������������ִ���������
			public void actionPerformed(ActionEvent e) {
				//�����û��б���--�����ⲿ��ķ���
				toUserListFrame();
			}
		});
		button.setBounds(246, 197, 93, 23);
		contentPane.add(button);
		
		JButton button_1 = new JButton("\u5173    \u95ED");
		button_1.setBounds(81, 197, 93, 23);
		contentPane.add(button_1);
		
		//���ô�����У�
		this.setLocationRelativeTo(null);
		//���ñ���
		this.setTitle("��¼������");
	}

	protected void toUserListFrame() {
		//1.�Ӵ���������ݣ�
		String ip = this.txtIP.getText();//��ȡ��������IP���ı�����ı����ݣ�
		String port = this.txtPort.getText();//��ȡ���������˿��ı�����ı����ݣ�
		final String loginName = this.txtLoginName.getText();//��ȡ���ǳ��ı�����ı����ݣ�
		
		//2.��֤����--ֻ������֤��ֻ��֤�Ƿ�Ϊ��
		//String���trim()��ȥ���ַ������˵�"�ո�"
		if(ip != null && ip.trim().length() == 0){
			//�����Ի���
			JOptionPane.showMessageDialog(this, "����д������IP!");
			//��"������IP"�ı����ȡ����
			this.txtIP.requestFocus();
			//����������
			return;
		}
		if(port == null || port.trim().length() == 0){
			//�����Ի���
			JOptionPane.showMessageDialog(this, "����д�������˿ڣ�");
			//��"�������˿�"�ı����ȡ����
			this.txtPort.requestFocus();
			//����������
			return;
		}
		if(loginName == null|| loginName.trim().length() == 0){
			//�����Ի���
			JOptionPane.showMessageDialog(this, "����д�ǳƣ�");
			//��"�ǳ�"�ı����ȡ����
			this.txtLoginName.requestFocus();
			//����������
			return;
		}
		//3.���ӷ�����������"�ǳ�"��Ϣ������ͬһIP����û���¼����ͬIP�£�����ͬ�ǳ��û���
		
		try {
			socket = new Socket(ip,Integer.parseInt(port));
			//��ȡ�����������"�ǳ�"��Ϣ:��ʽ��0#��¼��
			//ע�⣺���Ƿ�����Ϣ������"��"����ʽ���ͣ�0#��¼��\r\n
			//�������˻���"��"����ʽ���գ������ȽϷ��㡣
			OutputStream out = socket.getOutputStream();
			//Ϊ�������е���ʽ���ͣ����ǹ���һ��"�ַ���"--PrintWriter(��ӡ��)
			//�ڶ���������true:��ʾ�������Զ�ˢ��
			PrintWriter pw = new PrintWriter(out,true);
			//������Ϣ
			pw.println("0#" + loginName);//�����Ϣ + ������з� + ˢ��
			//4.���շ������ģ���֤�����Ϣ
			BufferedReader bufIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String row = bufIn.readLine();//4#������Ϣ/OK
			row = row.substring(2);
			if(!"ok".equalsIgnoreCase(row)){
				JOptionPane.showMessageDialog(this, row);
				return;
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//5.�жϣ�������������¼�������Ի��򣬸�֪��¼�û�;
		//      ����������¼������
		
		//6.�����Լ�
		this.dispose();
		
		//7.��ʾ�û��б���--ʹ���߳�
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserListFrame userListFrame = new UserListFrame(loginName,socket);//�����������
					userListFrame.setVisible(true);//��ʾ����
					
					//8.������ʱ��
					Timer timer = new Timer(10,new InitListener(userListFrame));
					timer.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
}
