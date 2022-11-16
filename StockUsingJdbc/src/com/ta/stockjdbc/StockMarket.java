package com.ta.stockjdbc;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;



public class StockMarket extends JFrame {
	Properties prob=new Properties();

    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JLabel label;
    private JPanel contentPane;
    static String input;
    static double numberOfShares;
    static double purchasePrice;
    static double purchaseCommission;
    static double salesPrice;
    static double salesCommission;
    static double profit;
    static double amount;
    static String UserName;
    static String Password;
    static String stock;
	static double currentbalance;
    static DecimalFormat df = new DecimalFormat("0.00");
    
    static int AccountBalance=2000; 
    static double Fund=2000;
    static int count=1;

    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StockMarket frame = new StockMarket();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    
   
    public StockMarket() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 800, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 46));
        lblNewLabel.setBounds(423, 13, 273, 93);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        textField.setBounds(481, 170, 281, 68);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        passwordField.setBounds(481, 286, 281, 68);
        contentPane.add(passwordField);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBackground(Color.BLACK);
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblUsername.setBounds(250, 166, 193, 52);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblPassword.setBounds(250, 286, 193, 52);
        contentPane.add(lblPassword);

        btnNewButton = new JButton("Login");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnNewButton.setBounds(545, 392, 162, 73);
        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String userName = textField.getText();
                String password = passwordField.getText();
                try {
                	
                  InputStream filein=new FileInputStream("C:\\Users\\User\\eclipse-workspace\\StockUsingJdbc\\src\\Db.properties");
                  prob.load(filein);
                  String url=   prob.getProperty("url");
                  String username=  prob.getProperty("username");
                  String pass=   prob.getProperty("Pass");
                   // Connection connection=DriverManager.getConnection("jdbc:sqlserver://192.168.168.12:1433;databaseName=New_joinee_2022","NewJoinee2022", "P@ssw0rd");
                   Connection connection=DriverManager.getConnection(url, username, pass);
                	PreparedStatement st = (PreparedStatement) connection
                        .prepareStatement("SELECT NAME,PASSWORD FROM HARI_CUSTOMER_TABLE WHERE NAME=? AND PASSWORD=?");

                    st.setString(1, userName);
                    st.setString(2, password);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        dispose();
                        JOptionPane.showMessageDialog(btnNewButton, "You have successfully logged in");
                        Choose();
                    } else {
                        JOptionPane.showMessageDialog(btnNewButton, "Wrong Username & Password");
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                } catch (IOException e1) {
					
					e1.printStackTrace();
				}
            }
        });

        contentPane.add(btnNewButton);

        label = new JLabel("");
        label.setBounds(0, 0, 1008, 562);
        contentPane.add(label);
    }
   
	   
    
	static void Choose() throws IOException
	{
		int quantity = 0;
		input = JOptionPane.showInputDialog("Enter the Product Stock you want to purchase?");
	        stock = input;
		  try
		  {
	     
	            Connection connection = (Connection) DriverManager.getConnection("jdbc:sqlserver://192.168.168.12:1433;databaseName=New_joinee_2022","NewJoinee2022", "P@ssw0rd");
	            Statement stmt=(Statement) connection.createStatement();

                String Sql="SELECT * FROM HARI_STOCKAVB_TABLE WHERE STOCKNAME='"+stock+"'";
                
                ResultSet rs=stmt.executeQuery(Sql);

              if(rs.next()) {
	     		
	        	input = JOptionPane.showInputDialog("How many shares did you purchase?");
    	  		numberOfShares = Integer.parseInt(input);
    	  		
    	  		String shares="SELECT AVAILABLESTOCK FROM HARI_STOCKAVB_TABLE WHERE STOCKNAME='"+stock+"'";
    	  ResultSet  resultSet=stmt.executeQuery(shares);
    	  	  while (resultSet.next())
              {       
                  quantity = (resultSet.getInt("AVAILABLESTOCK"));
              }
    	  		
    	  		if(numberOfShares<=quantity)
    	  		{
    	  			input = JOptionPane.showInputDialog("What was the price per share?");
  					purchasePrice = Double.parseDouble(input);

  					Commision();
    	  		}
    	  		else
    	  		{
    	  			JOptionPane.showMessageDialog(null,"Stocks Insufficient", "Error", JOptionPane.INFORMATION_MESSAGE);
    	  			JOptionPane.showMessageDialog(null,"Available Stocks :"+quantity,"Error", JOptionPane.INFORMATION_MESSAGE);
    	  			
    	  			Choose();
    	  		}
    	  		
              }
              else
              {
            		JOptionPane.showMessageDialog(null,"Enter Valid Stock name", "Error", JOptionPane.INFORMATION_MESSAGE);
            		Choose();
              }
	     		
    	  		
	       }
		  catch(Exception e)
	  		
	  		{
	  			System.out.println(e.getMessage());
	  		}
	         
	         
	        }
    
            static void sellchoice() throws IOException
             {
            	 input = JOptionPane.showInputDialog("Do You Want to  sell");
     	           String Choice = String.valueOf(input);
     	        
            	 if(Choice.equalsIgnoreCase("Yes"))
        	        {
        	        	
        	        	     sell();
        	        	 	
        	        }
        	        else if(Choice.equalsIgnoreCase("NO"))
        	        {
        	        System.exit(0);	
        	        }
                 
        	        
        	        else if(Choice.isBlank())
        	        {
        	        	JOptionPane.showMessageDialog(null,"Choice is Blank", "Error", JOptionPane.INFORMATION_MESSAGE);
        	        	sellchoice();
        	        }
        	        else if(!Choice.equalsIgnoreCase("yes") && !Choice.equalsIgnoreCase("No"))
        	        {
        	        	JOptionPane.showMessageDialog(null,"Choice Not Valid", "Error", JOptionPane.INFORMATION_MESSAGE);
        	        	sellchoice();
        	        }
        	        else
        	        {
        	        	JOptionPane.showMessageDialog(null,"Invalid", "Error", JOptionPane.INFORMATION_MESSAGE);
        	        	sellchoice();
        	        }
               
               
                   
             }
    
      static void Commision()
      {
        
         input = JOptionPane.showInputDialog("What was the purchase commission fee?");
         purchaseCommission = Double.parseDouble(input);
         amount=(numberOfShares*purchasePrice)+purchaseCommission;
     
                           try
                           {
                       if(amount<=AccountBalance)
                                  {
                                	   AccountBalance-=amount;
                                       JOptionPane.showMessageDialog(null, "Your Account Balance Rs." + df.format(AccountBalance), "Stock Profit App", JOptionPane.INFORMATION_MESSAGE);
                                       sellchoice();
                                  }
                           
                                 else
                                 {

                     	        	JOptionPane.showMessageDialog(null,"Insufficient Balace", "Error", JOptionPane.INFORMATION_MESSAGE);
                     	        	JOptionPane.showMessageDialog(null,"Enter the Purchase limit less than or Equal of Rs."+df.format(AccountBalance), "Error", JOptionPane.INFORMATION_MESSAGE);
                     	        	Choose();
                                 }
                           }
                           catch(Exception e)
                           {
               	        	JOptionPane.showMessageDialog(null,"Invalid value", "Login", JOptionPane.INFORMATION_MESSAGE);

                           }
                                 
        }
         
          	static void sell() throws IOException
          	{
                 
          	input = JOptionPane.showInputDialog("How much did you sell each share for?");
          	salesPrice = Double.parseDouble(input);
         
          			input = JOptionPane.showInputDialog("What was the selling commission fee?");
          			salesCommission = Double.parseDouble(input);
         
          			profit =((numberOfShares * salesPrice) +salesCommission)-amount; 
         
        
        
          				if(profit > 0 )
          				{
          					JOptionPane.showMessageDialog(null, "Your Stock Profit  Rs." + df.format(profit), "Stock Profit App", JOptionPane.INFORMATION_MESSAGE);

          					AccountBalance+=profit;
          					JOptionPane.showMessageDialog(null, "Your Account Balance After Profit  Rs." + df.format(AccountBalance), "Stock Profit App", JOptionPane.INFORMATION_MESSAGE);

          					Choose();
          					System.exit(0);
            
          				} 
          				else if(profit<0)
          				{
          					JOptionPane.showMessageDialog(null, "Your Stock after Loss  Rs." + df.format(profit), "Stock Profit App", JOptionPane.INFORMATION_MESSAGE);

          					AccountBalance-=profit;
          					JOptionPane.showMessageDialog(null, "Your Account Balance After Loss  Rs." + df.format(AccountBalance), "Stock Profit App", JOptionPane.INFORMATION_MESSAGE);
	                        if(AccountBalance<0)    
	                        {
	                        	Accoutstatus();
	                        	
	                        }
	                        else
	                        {
	                        	Choose();
	                        	System.exit(0);
	                        }
          				}
        
                }
      
       
      static void Accoutstatus() throws IOException
      {
    
    	  
    	   if(AccountBalance>0 && count==1)
          {
            JOptionPane.showMessageDialog(null, "Low Balance Rs." + df.format(AccountBalance), "Stock Profit App", JOptionPane.INFORMATION_MESSAGE);

          	AccountBalance+=Fund;
           
          	JOptionPane.showMessageDialog(null, "Bonus Fund Credicted Rs." + df.format(AccountBalance), "Stock Profit App", JOptionPane.INFORMATION_MESSAGE);

          	
          	count++;
       	Choose();
          }
          else if(AccountBalance>0 && count==2)
          {
              JOptionPane.showMessageDialog(null, "Insufficient Account Fund Rs." + df.format(AccountBalance), "Stock Profit App", JOptionPane.INFORMATION_MESSAGE);
              JOptionPane.showMessageDialog(null,"Thanks", "End", JOptionPane.INFORMATION_MESSAGE);
     	       System.exit(0);
           }
          
          System.exit(0);
      }
    
}