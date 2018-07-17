import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;

public class Main {
	private static String userName="";
	private static String userType="";
	private static int systemSemester=-1;
	private static int systemWeek=-1;
	private static String curCourseName="";
	private static int curCourseSemester=-1;
	private static int curCoursemaxLoad=-1;
	private static int curCourseNumber=-1;
	private static int curRowSelected=-1;
	private static Vector rowData;
	private static JLabel jlabel;
	private static DefaultTableModel  tableModel;
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Login();
	}
	
	//login
	public static void Login(){
		JFrame f=new JFrame();
		f.setTitle("Login Page");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setSize(400,300);
		f.setLocation(200,200);
		Container con=f.getContentPane();	
		con.setLayout(new GridLayout(4,1));
		JPanel pan1=new JPanel();
		con.add(pan1);
		JPanel pan2=new JPanel();
		JLabel name=new JLabel("Name       ");
		pan2.add(name);
		TextField tf_name=new TextField(20);
		tf_name.setText("");
		pan2.add(tf_name);
		con.add(pan2);
		JPanel pan3=new JPanel();
		JLabel pass = new JLabel("Password");
		pan3.add(pass);
		JPasswordField password=new JPasswordField(15);
		password.setEchoChar('*');
		pan3.add(password);
		con.add(pan3);
		JPanel pan4 = new JPanel();
		JButton b_log=new JButton("Login");
		pan4.add(b_log);
		con.add(pan4);
		f.setVisible(true);
		
		b_log.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String name=tf_name.getText();
				String pwd=password.getText();
				if(name.isEmpty()||pwd.isEmpty()){
					JOptionPane.showMessageDialog(null, "Username and password cannot be empty", "Warning",JOptionPane.PLAIN_MESSAGE);
					return;
				}
				else{
					String sql="select * from users where name='"+name+"' and password='"+pwd+"'";
					try {
						Statement stmt=SqlOp.getConnection();
						ResultSet rs=stmt.executeQuery(sql);
						if(rs.next()){
							System.out.println("login successfully");
							f.dispose();
							userType=rs.getString(3);
							userName=name;
							if(userType.equals("admin")){
								new Admin(userName,userType);
							}
							else if(userType.equals("student")){
								new Student(userName,userType);
							}
							else if(userType.equals("lecturer")){
								new Lecturer(userName,userType);
							}
							else if(userType.equals("program coordinator")){
								new ProgramCoordinator(userName,userType);
							}
							
						}
						else{
							JOptionPane.showMessageDialog(null, "Wrong username or password", "Warning",JOptionPane.PLAIN_MESSAGE);
						}
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		});
	}
	
	
}
