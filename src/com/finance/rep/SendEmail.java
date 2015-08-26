package com.finance.rep;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/SendEmail")
public class SendEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	Connection con;
	int status;
	
    public SendEmail() {
        super();
    }

    
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","scott","tiger");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("text/html");
		
		String accNo=request.getParameter("accountNumber");
		
		try {
			PreparedStatement ps=con.prepareStatement("SELECT * FROM DTLIST WHERE ACCOUNTNUMBER=?");
			ps.setString(1, accNo);
			
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				status=rs.getInt(5);
			}
			
			ps.close();
			
			java.util.Date utilDate = new Date();
			
			// Convert it to java.sql.Date
			java.sql.Date date = new java.sql.Date(utilDate.getTime());
			
			
			PreparedStatement ps1=con.prepareStatement("INSERT INTO newaction VALUES(?,?,?,?)");
			ps1.setString(1, accNo);
			ps1.setInt(2, status);
			ps1.setDate(3, date);
			ps1.setString(4, "email");
			
			ResultSet rs1=ps1.executeQuery();
			
			HttpSession session=request.getSession();
			session.setAttribute("message", "message");
			
			response.sendRedirect("http://localhost:8080/RepLogic/RepPage.html#menu1");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
