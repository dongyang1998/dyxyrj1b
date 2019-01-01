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
		//设置位置，大小(x,y,width,height)
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
		//登录按钮
		button.addActionListener(new ActionListener() {
			//可以监听按钮的单击事件，一旦发生单击，会执行这个方法
			public void actionPerformed(ActionEvent e) {
				//启动用户列表窗体--调用外部类的方法
				toUserListFrame();
			}
		});
		button.setBounds(246, 197, 93, 23);
		contentPane.add(button);
		
		JButton button_1 = new JButton("\u5173    \u95ED");
		button_1.setBounds(81, 197, 93, 23);
		contentPane.add(button_1);
		
		//设置窗体居中：
		this.setLocationRelativeTo(null);
		//设置标题
		this.setTitle("登录聊天室");
	}

	protected void toUserListFrame() {
		//1.从窗体接收数据；
		String ip = this.txtIP.getText();//获取：服务器IP的文本框的文本内容；
		String port = this.txtPort.getText();//获取：服务器端口文本框的文本内容；
		final String loginName = this.txtLoginName.getText();//获取：昵称文本框的文本内容；
		
		//2.验证数据--只做简单验证：只验证是否为空
		//String类的trim()：去掉字符串两端的"空格"
		if(ip != null && ip.trim().length() == 0){
			//弹出对话框
			JOptionPane.showMessageDialog(this, "请填写服务器IP!");
			//让"服务器IP"文本框获取焦点
			this.txtIP.requestFocus();
			//将方法结束
			return;
		}
		if(port == null || port.trim().length() == 0){
			//弹出对话框
			JOptionPane.showMessageDialog(this, "请填写服务器端口！");
			//让"服务器端口"文本框获取焦点
			this.txtPort.requestFocus();
			//将方法结束
			return;
		}
		if(loginName == null|| loginName.trim().length() == 0){
			//弹出对话框
			JOptionPane.showMessageDialog(this, "请填写昵称！");
			//让"昵称"文本框获取焦点
			this.txtLoginName.requestFocus();
			//将方法结束
			return;
		}
		//3.连接服务器，发送"昵称"信息（允许同一IP多个用户登录，但同IP下，不能同昵称用户）
		
		try {
			socket = new Socket(ip,Integer.parseInt(port));
			//获取输出流，发送"昵称"信息:格式：0#登录名
			//注意：我们发送信息都是以"行"的形式发送：0#登录名\r\n
			//服务器端会以"行"的形式接收，这样比较方便。
			OutputStream out = socket.getOutputStream();
			//为了能以行的形式发送，我们构造一个"字符流"--PrintWriter(打印流)
			//第二个参数：true:表示：开启自动刷新
			PrintWriter pw = new PrintWriter(out,true);
			//发送信息
			pw.println("0#" + loginName);//输出信息 + 输出换行符 + 刷新
			//4.接收服务器的：验证结果信息
			BufferedReader bufIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String row = bufIn.readLine();//4#错误信息/OK
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
		//5.判断，如果：不允许登录，弹出对话框，告知登录用户;
		//      如果：允许登录，继续
		
		//6.销毁自己
		this.dispose();
		
		//7.显示用户列表窗体--使用线程
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserListFrame userListFrame = new UserListFrame(loginName,socket);//创建窗体对象；
					userListFrame.setVisible(true);//显示窗体
					
					//8.启动定时器
					Timer timer = new Timer(10,new InitListener(userListFrame));
					timer.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
}
