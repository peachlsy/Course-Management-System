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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class Student {
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
	public Student(String _userName,String _userType){
		userName=_userName;
		userType=_userType;
		try {
			initWindow();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void initWindow() throws SQLException, ClassNotFoundException{
		UIManager.put("OptionPane.okButtonText",   "OK");
		UIManager.put("OptionPane.yesButtonText",   "Yes");
		UIManager.put("OptionPane.noButtonText",   "No");
		
		JFrame f=new JFrame();
		f.setSize(900, 600);//window default size
		f.setResizable(false);
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		
		//set window title centering
		f.setFont(new Font("System", Font.PLAIN, 14));
		Font font = f.getFont();
		FontMetrics fm = f.getFontMetrics(font);
        int x = fm.stringWidth("Course Management System");
        int y = fm.stringWidth(" ");
        int z = f.getWidth()/2 - (x/2);
        int w = z/y;
        String pad ="";
        pad = String.format("%"+w+"s", pad);
        f.setTitle(pad+"Course Management System");
		f.setLocationRelativeTo(null);
		//Container con=f.getContentPane();	
		//con.setLayout(new GridLayout(2,1));
		f.setLayout(null);
		f.setVisible(true);
		initStuWindow(f);
		f.setVisible(true);
	}
	
	public static void initStuWindow(JFrame f) throws SQLException{
		JPanel jp=new JPanel();
		String sw=getCurSemesterAndWeek();
		systemSemester=Integer.valueOf(sw.split(",")[0]);
		systemWeek=Integer.valueOf(sw.split(",")[1]);
		jlabel=new JLabel("Name: "+userName+", Semester: "+systemSemester+", Week: "+systemWeek);
		JButton btnViewScore=new JButton("view performance");
		JButton btnCourse=new JButton("view course");
		jp.add(jlabel);
		jp.add(btnViewScore);
		jp.add(btnCourse);
		jp.setBounds(0, 10, 900, 30);
		
		Vector columnNames=new Vector();
		columnNames.add("course name");
		columnNames.add("semester");
		columnNames.add("maxinum load");
		columnNames.add("lecturer");
		columnNames.add("curNumber");
		//columnNames.add("operation");
		ResultSet rs=getCourses();
		Vector rowData = new Vector();
		while(rs.next()){
             Vector row=new Vector();
             row.add(rs.getString(1));
             row.add(rs.getInt(2));
             row.add(rs.getInt(3));
             row.add(rs.getString(4));
             row.add(rs.getInt(5));
             rowData.add(row);
         }
		
		JPanel pbtm=new JPanel();
		pbtm.setBounds(0, 450, 900, 100);
		f.add(pbtm);
		JLabel labelBottom=new JLabel();
		pbtm.add(labelBottom);
		JButton btnBottom=new JButton();
		btnBottom.setVisible(false);
		pbtm.add(btnBottom);
		JTable jt;
		tableModel = new DefaultTableModel(rowData,columnNames);
		jt = new JTable(tableModel);//new DefaultTableModel()
		btnBottom.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(btnBottom.getText().equals("Enrol")){
					enrolCourse(userName,curCourseName,curCourseSemester);
					btnBottom.setText("Withdraw");
					Vector row=(Vector) rowData.get(curRowSelected);
					row.set(4, ((Integer) row.get(4))+1);
					tableModel=new DefaultTableModel(rowData,columnNames);
					jt.setModel(tableModel);
					curCourseNumber+=1;
				
				}
				else if(btnBottom.getText().equals("Withdraw")){
					withDrawCourse(userName,curCourseName,curCourseSemester);
					btnBottom.setText("Enrol");
					Vector row=(Vector) rowData.get(curRowSelected);
					row.set(4, ((Integer) row.get(4))-1);
					tableModel=new DefaultTableModel(rowData,columnNames);
					jt.setModel(tableModel);
					curCourseNumber-=1;
				}
			}
			
		});
		
		
		jt.setRowSelectionAllowed(true);
		//jt.getColumnModel().getColumn(5).setCellEditor(new MyButtonEditor());
		//jt.getColumnModel().getColumn(5).setCellRenderer(new MyButtonRender());
		JScrollPane jsp=new JScrollPane(jt);
		f.add(jsp);
		
		jt.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int row = jt.rowAtPoint(evt.getPoint());
		        int col = jt.columnAtPoint(evt.getPoint());
		        if (row >= 0 && col >= 0) {
		        	curCourseName=(String) jt.getValueAt(row, 0);
		        	curCourseSemester=Integer.valueOf(String.valueOf(jt.getValueAt(row, 1)));
		        	curCoursemaxLoad=Integer.valueOf(String.valueOf(jt.getValueAt(row, 2)));
		        	curCourseNumber=Integer.valueOf(String.valueOf(jt.getValueAt(row, 4)));
		        	curRowSelected=row;
		        	labelBottom.setText("The course you selected is "+curCourseName+" at semester "+curCourseSemester+". Its maximum load is "
		        			+ ""+curCoursemaxLoad);
		        	try {
		        		if(checkIfStuTakenCourseAtSemester(curCourseName,curCourseSemester)){
		        			if(checkIfStuCanWithdrawCourse()){
		        				btnBottom.setEnabled(true);
								btnBottom.setText("Withdraw");
								btnBottom.setVisible(true);
		        			}
		        			else{
		        				btnBottom.setEnabled(false);
								btnBottom.setText("Withdraw");
								btnBottom.setVisible(true);
		        			}
		        		}
		        		else if(checkIfStuCanSelectedCourse()){
							btnBottom.setEnabled(true);
							btnBottom.setText("Enrol");
							btnBottom.setVisible(true);
						}
						else{
							btnBottom.setEnabled(false);
							btnBottom.setText("Enrol");
							btnBottom.setVisible(true);
						}
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    }
		});
		btnViewScore.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				viewPerformance(jt,f);
			}
			
		});
		btnCourse.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					viewCourses(jt,f);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		jsp.setBounds(0, 40, 900, 400);	
		f.add(jp);
		f.setVisible(true);
	}
	private static void viewCourses(JTable table,JFrame f) throws SQLException{
		Vector columnNames=new Vector();
		columnNames.add("course name");
		columnNames.add("semester");
		columnNames.add("maxinum load");
		columnNames.add("lecturer");
		columnNames.add("curNumber");
		ResultSet rs=getCourses();
		rowData = new Vector();
		while(rs.next()){  
	         Vector row=new Vector();  
	         row.add(rs.getString(1));  
	         row.add(rs.getInt(2));  
	         row.add(rs.getInt(3));  
	         row.add(rs.getString(4));  
	         row.add(rs.getInt(5));
	         rowData.add(row);  
	     }
		tableModel = new DefaultTableModel(rowData,columnNames);
		table.setModel(tableModel);		
	}
	private static void viewPerformance(JTable table,JFrame f){
		rowData=new Vector();
		Vector columnNames=new Vector();
		columnNames.add("student name");
		columnNames.add("course name");
		columnNames.add("course semester");
		columnNames.add("score");
		try {
			Statement stmt=SqlOp.getConnection();
			String sql="select * from studentcourse where score is not null and stuName='"+userName+"' order by stuCourseSemester desc";
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()){
				Vector row = new Vector();
				row.add(rs.getString(1));
				row.add(rs.getString(2));
				row.add(rs.getInt(3));
				row.add(rs.getDouble(4));
				rowData.add(row);
			}
			tableModel=new DefaultTableModel(rowData,columnNames);
			table.setModel(tableModel);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		f.setVisible(true);
	}
	private static ResultSet getCourses(){
		String sql="select * from course order by semester desc";
		Statement stmt;
		try {
			stmt = SqlOp.getConnection();
			ResultSet rs=stmt.executeQuery(sql);
			return rs;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	private static void enrolCourse(String stuName, String courseName,int stuCourseSemester){
		String sql="insert into studentcourse (stuName,stuCourseName,stuCourseSemester) values ('"+stuName+"','"+courseName+"',"+String.valueOf(stuCourseSemester)+")";
		Statement stmt;
		try {
			stmt = SqlOp.getConnection();
			boolean rs=stmt.execute(sql);
			sql="update course set curNumber=curNumber+1 where courseName='"+courseName+"' and semester="+stuCourseSemester;
			rs=stmt.execute(sql);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static boolean checkIfStuCanWithdrawCourse(){
		if(curCourseSemester<systemSemester) return false;
		if(curCourseSemester==systemSemester&&systemWeek>3) return false;
		return true;
	}
	private static boolean checkIfStuCanSelectedCourse() throws ClassNotFoundException, SQLException{
		if(curCourseSemester<systemSemester) return false;
		if(curCoursemaxLoad<=curCourseNumber) return false;
		if(curCourseSemester==systemSemester&&systemWeek>2) return false;
		String preCourses=getPrerequisites(curCourseName);
		if(preCourses.isEmpty()) return true;
		String[] preCourseArr = preCourses.split(" ");
		for(int i=0;i<preCourseArr.length;i++){
			if(!checkIfStuTakenCourse(preCourseArr[i])) return false;
		}
		return true;
	}
	private static boolean checkIfStuTakenCourseAtSemester(String course,int semester) throws ClassNotFoundException, SQLException{
		String sql="select * from studentcourse where stuName='"+userName+"' and stuCourseName='"+course+"' and stuCourseSemester="+semester;
		Statement stmt=SqlOp.getConnection();
		ResultSet rs=stmt.executeQuery(sql);
		if(rs.next()){
			return true;
		}
		else{
			return false;
		}
	}
	//check if a student has taken a course
		private static boolean checkIfStuTakenCourse(String course) throws ClassNotFoundException, SQLException{
			String sql="select * from studentcourse where stuName='"+userName+"' and stuCourseName='"+course+"'";
			Statement stmt=SqlOp.getConnection();
			ResultSet rs=stmt.executeQuery(sql);
			if(rs.next()){
				return true;
			}
			else{
				return false;
			}
		}
	private static void withDrawCourse(String stuName, String courseName,int stuCourseSemester){
		String sql="delete from studentcourse where stuName='"+stuName+"' and stuCourseName='"+courseName+"' and stuCourseSemester="+stuCourseSemester;
		Statement stmt;
		try {
			stmt = SqlOp.getConnection();
			boolean rs=stmt.execute(sql);
			sql="update course set curNumber=curNumber-1 where courseName='"+courseName+"' and semester="+stuCourseSemester;
			rs=stmt.execute(sql);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static String getPrerequisites(String courseName){
		String sql="select preCourseName from prerequisites where courseName='"+courseName+"'";
		String result="";
		try {
			Statement stmt=SqlOp.getConnection();
			ResultSet rs=stmt.executeQuery(sql);
			
			while(rs.next()){
				String semester=rs.getString(1);
				result+=semester+" ";
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.trim();
		
	}
	private static String getCurSemesterAndWeek(){
		String sql="select * from semesterandweek";
		try {
			Statement stmt=SqlOp.getConnection();
			ResultSet rs=stmt.executeQuery(sql);
			if(rs.next()){
				int semester=rs.getInt(1);
				int week=rs.getInt(2);
				return semester+","+week;
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
}

