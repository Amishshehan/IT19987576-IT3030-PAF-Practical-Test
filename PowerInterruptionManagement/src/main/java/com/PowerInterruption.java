package com;

import java.sql.*;

public class PowerInterruption {
	
	// A common method to connect to the DB
	
		private Connection connect() {
			Connection con = null;

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				// Provide the correct details: DBServer/DBName, username, password
				con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/paf", "root", "root");
				System.out.println("Successfully connected");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.print("Error connected");
			}
			return con;
		}

		public String insertPowerInterruption(String name, String pnumber, String intype, String inloca, String remarks) {
			String output = "";
			try {
				Connection con = connect();

				if (con == null) {
					return "Error while connecting to the database for inserting.";
				}

				// create a prepared statement
				String query = " insert into inquiries(`inquiryID`, `customerName`,`phoneNumber`,`inquiryType`,`inquiryLocation`,`remarks`)"
						+ " values (?, ?, ?, ?, ?, ?)";

				PreparedStatement preparedStmt = con.prepareStatement(query);

				// binding values
				preparedStmt.setInt(1, 0);			
				preparedStmt.setString(2, name);
				preparedStmt.setInt(3, Integer.parseInt(pnumber));
				preparedStmt.setString(4, intype);
				preparedStmt.setString(5, inloca);
				preparedStmt.setString(6, remarks);			

				// execute the statement
				preparedStmt.execute();
				con.close();

				String newInquiries = readPowerInterruption();
				output = "{\"status\":\"success\", \"data\": \"" +
				newInquiries + "\"}";
			}
			catch (Exception e)
			{
				output = "{\"status\":\"error\", \"data\":"
						+ "\"Error while inserting the inquirie.\"}";
				System.err.println(e.getMessage());
			}
			
			return output;
		}

		public String readPowerInterruption()
		{
			String output = "";
		try
		{
			Connection con = connect();
			
			if (con == null)
		{
				return "Error while connecting to the database for reading.";
		}
			// Prepare the html table to be displayed
			output = "<table class=' table table-sm table-bordered table-white text-nowrap table-hover' >"
					+ "<thead class='thead-dark'>"
						+ "<tr>"
							+ "<th>Customer Name</th>"
							+ "<th>Phone Number</th>" 
							+ "<th>Inquiry Type</th>"
							+ "<th>Inquiry Location</th>" 							
							+ "<th>Remarks</th>"
							+ "<th>Update</th>"
							+ "<th>Remove</th>"
						+ "</tr>"
					+ "</thead>";	
			

			
			String query = "select * from inquiries";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			// iterate through the rows in the result set
			while (rs.next())
			{				
				String inquiryID = Integer.toString(rs.getInt("inquiryID"));
				String customerName = rs.getString("customerName");
				String phoneNumber = Integer.toString(rs.getInt("phoneNumber"));
				String inquiryType = rs.getString("inquiryType");
				String inquiryLocation = rs.getString("inquiryLocation");
				String remarks = rs.getString("remarks");
				
			// Add into the html table
				output += "<tr><td><input id='hidInquirieIDUpdate' name='hidInquirieIDUpdate' type='hidden' value='" + inquiryID + "'>"
						+ customerName + "</td>";
				output += "<td>" + phoneNumber + "</td>";
				output += "<td>" + inquiryType + "</td>";
				output += "<td>" + inquiryLocation + "</td>";
				output += "<td>" + remarks + "</td>";
				
				
			// buttons							
				output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary' data-itemid='" + inquiryID + "'></td>"
						+ "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-itemid='" + inquiryID + "'></td></tr>";
					
			}
			con.close();
			// Complete the html table
			output += "</table>";
			}
		
		catch (Exception e)
		{
			output = "Error while reading the inquiries.";
			System.err.println(e.getMessage());
			
		}
			return output;
	}

		public String updatePowerInterruption(String ID, String name, String pnumber, String intype, String inloca, String remarks) 
			{
			String output = "";
			
			try {
					Connection con = connect();
		
					if (con == null) 
					{
						return "Error while connecting to the database for updating.";
					}
		
					// create a prepared statement
					String query = "UPDATE inquiries SET customerName=?,phoneNumber=?,inquiryType=?,inquiryLocation=?,remarks=? " + 
							"WHERE inquiryID=?";

					PreparedStatement preparedStmt = con.prepareStatement(query);
		
					// binding values
					preparedStmt.setString(1, name);
					preparedStmt.setInt(2, Integer.parseInt(pnumber));
					preparedStmt.setString(3, intype);
					preparedStmt.setString(4, inloca);
					preparedStmt.setString(5, remarks);
					preparedStmt.setInt(6, Integer.parseInt(ID));
		
					// execute the statement
					preparedStmt.execute();
					con.close();
		
					String newInquiries = readPowerInterruption();
					output = "{\"status\":\"success\", \"data\": \"" +
					newInquiries + "\"}";
				}
				catch (Exception e)
				{
					output = "{\"status\":\"error\", \"data\":\"Error while updating the inquirie.\"}";
					System.err.println(e.getMessage());
				}
			
				return output;
			}

		public String deletePowerInterruption(String inquiryID) 
		{
			String output = "";

			try 
			{
				Connection con = connect();
				if (con == null) {
					return "Error while connecting to the database for deleting.";
				}

				// create a prepared statement
				String query = "delete from inquiries where inquiryID=?";

				PreparedStatement preparedStmt = con.prepareStatement(query);

				// binding values
				preparedStmt.setInt(1, Integer.parseInt(inquiryID));

				// execute the statement
				preparedStmt.execute();
				con.close();

				String newInquiries = readPowerInterruption();
				output = "{\"status\":\"success\", \"data\": \"" +
				newInquiries + "\"}";
			}
			catch (Exception e)
			{
				output = "{\"status\":\"error\", \"data\":\"Error while deleting the inquirie.\"}";
				System.err.println(e.getMessage());
			}
			return output;
		}
}
