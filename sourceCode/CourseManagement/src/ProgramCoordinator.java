import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class ProgramCoordinator {
	
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
	
	public ProgramCoordinator(String _userName,String _userType){
		userName=_userName;
		userType=_userType;
		try {
			initWindow();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static String getCurSemesterAndWeek(){
		String sql="select * from semesterandweek";
		try {
			Statement stmt=SqlOp.getConnection();   		//get connection, for excuting static SQL statement
			ResultSet rs=stmt.executeQuery(sql);			//returns a single ResultSet object
			if(rs.next()){									//Database figure the next data,if don't have, return false, exit loop
				int semester=rs.getInt(1);					//else, store the data into rs
				int week=rs.getInt(2);
				return semester+","+week;					//return array[semester,week]
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	
						//Initialize window
	public static void initWindow() throws SQLException, ClassNotFoundException{
		UIManager.put("OptionPane.okButtonText",   "OK"); 
		UIManager.put("OptionPane.yesButtonText",   "Yes");
		UIManager.put("OptionPane.noButtonText",   "No"); 			//set visual appearance
		
		JFrame f=new JFrame();
		f.setSize(900, 600);//window default size
		f.setResizable(false);  									//cannot change the window size
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE); 				//user click close button, then ...
		
		//set window title centering
		f.setFont(new Font("System", Font.PLAIN, 14));				//design the appearance of characters，  System font，int style, size
		Font font = f.getFont();
		FontMetrics fm = f.getFontMetrics(font); 					//get current font metrics指标
        int x = fm.stringWidth("Course Management System");			//x=the width of this String
        int y = fm.stringWidth(" ");
        int z = f.getWidth()/2 - (x/2);								//f， the width of whole window
        int w = z/y;
        String pad ="";
        pad = String.format("%"+w+"s", pad);  						//format String
        f.setTitle(pad+"Course Management System"); 				//make it in the central 
		f.setLocationRelativeTo(null);								//put window in the center of screen
		f.setLayout(null);											//empty the layout management system, then add components
		f.setVisible(true); 										//window can be seen, must have
		initProgramCoordinatorWindow(f);			
		f.setVisible(true);
	}
	
	
	
	
	public static void initProgramCoordinatorWindow(JFrame f){
		JPanel jp=new JPanel();										//creat intermediate container
		String sw=getCurSemesterAndWeek();
		systemSemester=Integer.valueOf(sw.split(",")[0]);  			//convert String into int
		systemWeek=Integer.valueOf(sw.split(",")[1]);	   			//divided by , read the 1st data in array
		jlabel=new JLabel("Name: "+userName+", Semester: "+systemSemester+", Week: "+systemWeek);
		jp.add(jlabel);
		jp.setBounds(0, 10, 900, 40);    							//set boundary
		JButton btnAddCourse= new JButton("Add Courses");
		JButton btnGSP= new JButton("Grant Special Permissions");
		jp.add(btnAddCourse);
		jp.add(btnGSP);
		f.add(jp);
		
		JPanel jpCenter=new JPanel();
		jpCenter.setLayout(null);
		jpCenter.setBounds(100, 50, 900, 500);		
		f.add(jpCenter);
		
		JLabel jlCourseName=new JLabel("course name");
		jlCourseName.setBounds(20, 60, 80, 30);
		
		JTextField jtfCourseName=new JTextField();
		jtfCourseName.setBounds(100, 60, 300, 30);
		jtfCourseName.setColumns(200);
		jpCenter.add(jlCourseName);
		jpCenter.add(jtfCourseName);
		
		JLabel jlPreCourse=new JLabel("prerequisites");
		jlPreCourse.setBounds(20, 100, 80, 30);
		JTextField jtfPreCourse=new JTextField();
		jtfPreCourse.setBounds(100, 100, 300, 30);
		jtfPreCourse.setColumns(200);
		jpCenter.add(jlPreCourse);
		jpCenter.add(jtfPreCourse);
		JLabel jlHint=new JLabel("Separated by commas,e.g.: computer system,java programming");
		jlHint.setBounds(400, 100, 400, 30);
		jpCenter.add(jlHint);
		
		JLabel jlCourseSemester=new JLabel("semester");
		jlCourseSemester.setBounds(20, 150, 80, 30);
		JTextField jtfCourseSemester=new JTextField();
		jtfCourseSemester.setBounds(100, 150, 300, 30);
		jtfCourseSemester.setColumns(200);
		jpCenter.add(jlCourseSemester);
		jpCenter.add(jtfCourseSemester);
		
		JButton jBtnAdd=new JButton("add course");
		jBtnAdd.setBounds(200, 210, 100, 30);
		jpCenter.add(jBtnAdd);
		
		JLabel jlCourseNameGSP=new JLabel("course name");
		jlCourseNameGSP.setBounds(20, 60, 80, 30);
		JTextField jtfCourseNameGSP=new JTextField();
		jtfCourseNameGSP.setBounds(100, 60, 300, 30);
		jtfCourseNameGSP.setColumns(200);
		jpCenter.add(jlCourseNameGSP);
		jpCenter.add(jtfCourseNameGSP);
		
		JLabel jlPreCourseGSP=new JLabel("student name");
		jlPreCourseGSP.setBounds(20, 100, 80, 30);
		JTextField jtfPreCourseGSP=new JTextField();
		jtfPreCourseGSP.setBounds(100, 100, 300, 30);
		jtfPreCourseGSP.setColumns(200);
		jpCenter.add(jlPreCourseGSP);
		jpCenter.add(jtfPreCourseGSP);

		JLabel jlCourseSemesterGSP=new JLabel("semester");
		jlCourseSemesterGSP.setBounds(20, 150, 80, 30);
		JTextField jtfCourseSemesterGSP=new JTextField();
		jtfCourseSemesterGSP.setBounds(100, 150, 300, 30);
		jtfCourseSemesterGSP.setColumns(200);
		jpCenter.add(jlCourseSemesterGSP);
		jpCenter.add(jtfCourseSemesterGSP);
		
		JButton jBtnAddGSP=new JButton("grant permission");
		jBtnAddGSP.setBounds(150, 210, 150, 30);
		jpCenter.add(jBtnAddGSP);
		
		jlCourseNameGSP.setVisible(false);
		jtfCourseNameGSP.setVisible(false);
		jlPreCourseGSP.setVisible(false);
		jtfPreCourseGSP.setVisible(false);
		jlCourseSemesterGSP.setVisible(false);
		jtfCourseSemesterGSP.setVisible(false);
		jBtnAddGSP.setVisible(false);
		
		btnAddCourse.addActionListener(new ActionListener(){  //do sth when triggered by some operate

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jlCourseName.setVisible(true);
				jtfCourseName.setVisible(true);
				jlPreCourse.setVisible(true);
				jtfPreCourse.setVisible(true);
				jlHint.setVisible(true);
				jlCourseSemester.setVisible(true);
				jtfCourseSemester.setVisible(true);
				jBtnAdd.setVisible(true);
				
				jlCourseNameGSP.setVisible(false);
				jtfCourseNameGSP.setVisible(false);
				jlPreCourseGSP.setVisible(false);
				jtfPreCourseGSP.setVisible(false);
				jlCourseSemesterGSP.setVisible(false);
				jtfCourseSemesterGSP.setVisible(false);
				jBtnAddGSP.setVisible(false);
				
				
			}
			
		});
		btnGSP.addActionListener(new ActionListener(){    

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jlCourseName.setVisible(false);
				jtfCourseName.setVisible(false);
				jlPreCourse.setVisible(false);
				jtfPreCourse.setVisible(false);
				jlHint.setVisible(false);
				jlCourseSemester.setVisible(false);
				jtfCourseSemester.setVisible(false);
				jBtnAdd.setVisible(false);
				
				jlCourseNameGSP.setVisible(true);
				jtfCourseNameGSP.setVisible(true);
				jlPreCourseGSP.setVisible(true);
				jtfPreCourseGSP.setVisible(true);
				jlCourseSemesterGSP.setVisible(true);
				jtfCourseSemesterGSP.setVisible(true);
				jBtnAddGSP.setVisible(true);
			}
			
		});

		jBtnAdd.addActionListener(new ActionListener(){ 

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String cName=jtfCourseName.getText();
				String pCourses=jtfPreCourse.getText().trim();		//delete space
				String sem=jtfCourseSemester.getText();
				
				if(cName.isEmpty()||sem.isEmpty()){
					JOptionPane.showMessageDialog(null, "Failed adding new course", "Info",JOptionPane.PLAIN_MESSAGE);
					return;								//null means display in default frame， info means left up area characters
					 
				}
				if(Integer.valueOf(sem)*12+4<(systemSemester*12+systemWeek)){
					JOptionPane.showMessageDialog(null, "Failed adding new course", "Info",JOptionPane.PLAIN_MESSAGE);
					return;								//cannot add course after week 4
				}
				String sql="insert into course (courseName,semester,maxLoad,curNumber) values ('"+cName+"',"+sem+",0,0)";
				
				try {
					Statement stmt=SqlOp.getConnection();
					stmt.execute(sql);
					if(pCourses.isEmpty()){				//if the prerequisite is null， then execute the sql above， then return
						return;
					}
					String[] preCArr=pCourses.split(",");
					for(int i=0;i<preCArr.length;i++){
						sql="INSERT INTO prerequisites (courseName, preCourseName)  SELECT * FROM (SELECT '"+cName+"','"+preCArr[i]+"') AS tmp WHERE NOT EXISTS ( SELECT courseName,preCourseName FROM prerequisites WHERE courseName= '"+cName+"' and preCourseName='"+preCArr[i]+"' ) LIMIT 1 ;"; //只查询第一个
						//sql="if not exists (select * from prerequisites where courseName='"+cName+"' and preCourseName='"+preCArr[i]+"' ) insert into prerequisites ('"+cName+"','"+preCArr[i]+"')";
						stmt.execute(sql);
					}
					JOptionPane.showMessageDialog(null, "Add the course successfully", "Info",JOptionPane.PLAIN_MESSAGE);
					
				} catch (SQLException | ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Failed adding new course", "Info",JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				}
			}
			
		});
		
		
		
		jBtnAddGSP.addActionListener(new ActionListener(){	

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String cName=jtfCourseNameGSP.getText();
				String sName=jtfPreCourseGSP.getText().trim();
				String sem=jtfCourseSemesterGSP.getText();
				if(cName.isEmpty()||sem.isEmpty()||sName.isEmpty()){
					JOptionPane.showMessageDialog(null, "Failed granting special permissions", "Info",JOptionPane.PLAIN_MESSAGE);
					return;
				}
				if(Integer.valueOf(sem)*12+1<(systemSemester*12+systemWeek)){
					JOptionPane.showMessageDialog(null, "Failed granting special permissions", "Info",JOptionPane.PLAIN_MESSAGE);
					return;								//cannot grant special permissions after week 1
				}
				String sql="INSERT INTO studentcourse (stuName, stuCourseName,stuCourseSemester)  SELECT * FROM (SELECT '"+sName+"','"+cName+"',"+sem+") AS tmp WHERE NOT EXISTS ( SELECT stuName, stuCourseName,stuCourseSemester FROM studentcourse WHERE stuName= '"+sName+"' and stuCourseName='"+cName+"' and stuCourseSemester="+sem+" ) LIMIT 1 ;";
				try {
					Statement stmt=SqlOp.getConnection();
					stmt.execute(sql);
					JOptionPane.showMessageDialog(null, "Grant special permissions successfully", "Info",JOptionPane.PLAIN_MESSAGE);
				} catch (SQLException | ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Failed granting special permissions", "Info",JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				}
			}
			
		});
		f.setVisible(true);
	}
}
