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

public class Lecturer {

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
	public Lecturer(String _userName,String _userType){
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
		f.setLayout(null);
		f.setVisible(true);
		
		initLecturerWindows(f);
		
		
		f.setVisible(true);
	}
	
	public static void initLecturerWindows(JFrame f) throws SQLException, ClassNotFoundException{
		JPanel jp=new JPanel();
		String sw=getCurSemesterAndWeek();
		systemSemester=Integer.valueOf(sw.split(",")[0]);
		systemWeek=Integer.valueOf(sw.split(",")[1]);
		jlabel=new JLabel("Name: "+userName+", Semester: "+systemSemester+", Week: "+systemWeek);
		jp.add(jlabel);
		jp.setBounds(0, 10, 900, 40);
		JButton btnViewCourse= new JButton("View Courses");
		JButton btnViewScore= new JButton("View Performance");
		jp.add(btnViewCourse);
		jp.add(btnViewScore);
		rowData=new Vector();
		Vector columnNames=new Vector();
		columnNames.add("student name");
		columnNames.add("course name");
		columnNames.add("course semester");
		columnNames.add("score");
		String sql="select stuName,stuCourseName,stuCourseSemester,score from studentcourse,course where courseName=stuCourseName and lecturerName='"+userName+"' order by stuCourseSemester desc";
	    Statement stmt=SqlOp.getConnection();
	    ResultSet rs=stmt.executeQuery(sql);
		while(rs.next()){  
             Vector row=new Vector();
             row.add(rs.getString(1));
             row.add(rs.getString(2));
             row.add(rs.getInt(3));
             row.add(rs.getDouble(4));
             rowData.add(row);
         }
		tableModel = new DefaultTableModel(rowData,columnNames);
		JTable jt = new JTable(tableModel);
		JScrollPane jsp=new JScrollPane(jt);
		f.add(jsp);
		jsp.setBounds(0, 60, 900, 400);		
		f.add(jp);
		f.setVisible(true);
		
		btnViewScore.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				viewPerformance(jt,f);
				
			}
			
		});
		btnViewCourse.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				getLectureCourses(jt,f);
			}
		});
		tableModel.addTableModelListener(new TableModelListener(){

			@Override
			public void tableChanged(TableModelEvent e) {
				// TODO Auto-generated method stub
				int row=e.getFirstRow();
				Vector rowV=(Vector) rowData.get(row);
				String sName=(String)rowV.get(0);
				String cName=(String) rowV.get(1);
				String scSemester=String.valueOf(rowV.get(2));
				String score=String.valueOf(rowV.get(3));
				if(scSemester.equals(String.valueOf(systemSemester))){
					String sql="update studentcourse set score="+score+" where stuName='"+sName+"' and stuCourseName='"+cName+"' and stuCourseSemester="+scSemester;
					Statement stmt;
					try {
						stmt = SqlOp.getConnection();
						boolean rs=stmt.execute(sql);
					} catch (ClassNotFoundException | SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
				else if(Integer.valueOf(scSemester)<systemSemester){
					String sql="update studentcourse set score="+score+" where stuName='"+sName+"' and stuCourseName='"+cName+"' and stuCourseSemester="+scSemester+" and score=-1";
					Statement stmt;
					try {
						stmt = SqlOp.getConnection();
						boolean rs=stmt.execute(sql);
					} catch (ClassNotFoundException | SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
			}
			
		});
		jt.putClientProperty("terminateEditOnFocusLost", true);
		f.setVisible(true);
	}
	private static void getLectureCourses(JTable table,JFrame f){
		rowData=new Vector();
		Vector columnNames=new Vector();
		columnNames.add("student name");
		columnNames.add("course name");
		columnNames.add("course semester");
		columnNames.add("score");
		try {
			Statement stmt=SqlOp.getConnection();
			String sql="select stuName,stuCourseName,stuCourseSemester,score from studentcourse,course where courseName=stuCourseName and lecturerName='"+userName+"' order by stuCourseSemester desc";
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
		tableModel.addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent e) {
				// TODO Auto-generated method stub
				int row=e.getFirstRow();
				Vector rowV=(Vector) rowData.get(row);
				String sName=(String)rowV.get(0);
				String cName=(String) rowV.get(1);
				String scSemester=String.valueOf(rowV.get(2));
				String score=String.valueOf(rowV.get(3));
				if(scSemester.equals(String.valueOf(systemSemester))){
					String sql="update studentcourse set score="+score+" where stuName='"+sName+"' and stuCourseName='"+cName+"' and stuCourseSemester="+scSemester;
					Statement stmt;
					try {
						stmt = SqlOp.getConnection();
						boolean rs=stmt.execute(sql);
					} catch (ClassNotFoundException | SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
			}
		});
		f.setVisible(true);
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
			String sql="select * from studentcourse where score is not null order by stuCourseSemester desc";
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
