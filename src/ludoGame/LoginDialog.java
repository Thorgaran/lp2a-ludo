package ludoGame;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

import com.therolf.miniServer.Client;
import com.therolf.miniServer.Message;
import com.therolf.miniServer.MessageType;

@SuppressWarnings("serial")
public class LoginDialog extends JDialog {
	JLabel feedbackLabel;
	ArrayList<JTextField> textFields = new ArrayList<JTextField>(3);
	JButton loginButton;
	
	private boolean loggedIn = false;
	
	LoginDialog(JFrame menu) {
		super(menu, "Multiplayer login", true);
		
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		// Add server feedback label
		JLabel feedbackLabel = new JLabel("<html>Fill both boxes before pressing \"connect\"</html>");
		this.feedbackLabel = feedbackLabel;
		
		feedbackLabel.setPreferredSize(new Dimension(200, 60));
		
		feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		feedbackLabel.setFont(feedbackLabel.getFont().deriveFont(16.0f));
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 2;
		this.getContentPane().add(feedbackLabel, c);
		
		// Add IP line
		this.addInputLine(1, "Server IP:");

		// Add Port line
		this.addInputLine(2, "Port:");
		
		// Add connect button
		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton connectButton = (JButton)e.getSource();
				LoginDialog loginDialog = (LoginDialog) SwingUtilities.getWindowAncestor(connectButton);
	    		
				// Attempt to read ip and port fields and connect to the server
				try {					
					String ip = loginDialog.textFields.get(0).getText();
					int port = Integer.valueOf(loginDialog.textFields.get(1).getText());
					
					loginDialog.feedbackLabel.setText("<html>" + Game.dyeText("INFO: ", Color.BLUE)
            			+ "trying to connect to " + ip + ":" + port + "</html>");
					
					Client client = new Client(ip, port);
					Game.setClient(client);
					
					loginDialog.feedbackLabel.setText("<html>" 
						+ Game.dyeText("Connexion successful! ", Color.GREEN)
						+ "Please select a username</html>");
					
					// Change enabled fields and buttons
					loginDialog.textFields.get(0).setEnabled(false);
		            loginDialog.textFields.get(1).setEnabled(false);
					connectButton.setEnabled(false);
					
					loginDialog.textFields.get(2).setEnabled(true);
					loginButton.setEnabled(true);
				}
				catch (NumberFormatException nfErr) {
					// Display error message
		            loginDialog.feedbackLabel.setText("<html>" + Game.dyeText("ERROR: ", Color.RED)
		            	+ "please enter a valid port value</html>");
		        }
				catch (IOException ioErr) {
					// Display error message
		            loginDialog.feedbackLabel.setText("<html>" + Game.dyeText("ERROR: ", Color.RED)
		            	+ "connexion failed (error contents: '" + ioErr.getMessage() + "')</html>");
		        }
	    	}
	    });
		
		c.gridx = 1;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		this.getContentPane().add(connectButton, c);
		
		// Add Username line
		this.addInputLine(4, "Username:");
		this.textFields.get(2).setEnabled(false);
		
		// Add login button
		JButton loginButton = new JButton("Login");
		this.loginButton = loginButton;
		
		loginButton.setEnabled(false);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton loginButton = (JButton)e.getSource();
				LoginDialog loginDialog = (LoginDialog) SwingUtilities.getWindowAncestor(loginButton);
	    		
				// Attempt to send username to the server
				try {					
					String pseudo = loginDialog.textFields.get(2).getText();
					
					loginDialog.feedbackLabel.setText("<html>" + Game.dyeText("INFO: ", Color.BLUE)
        				+ "sending username to the server...</html>");
					
					Game.getClient().send(MessageType.Login, pseudo);
					
					Message serverResponse = Game.getClient().read();
					if (serverResponse.getType() == MessageType.AuthFailed) {
						loginDialog.feedbackLabel.setText("<html>" + serverResponse.getContents() + "</html>");
					}
					else if (serverResponse.getType() != MessageType.AuthSuccessful) {
	                	throw new IOException("Unexpected server response (message type: '"
	                		+ serverResponse.getType().name() + "')");
	                }
					else {
						// Authentication successful!
						loginDialog.setLoggedIn();
						dispose();
					}
				}
				catch (IOException ioErr) {
					// Display error message
		            loginDialog.feedbackLabel.setText("<html>" + Game.dyeText("ERROR: ", Color.RED)
		            	+ ioErr.getMessage() + "</html>");
		        }
	    	}
	    });
		
		c.gridx = 1;
		c.gridy = 5;
		c.gridheight = 1;
		c.gridwidth = 1;
		this.getContentPane().add(loginButton, c);
		
		// Add cancel button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Close client if already connected to the server
				if (Game.getClient() != null) {
					Game.getClient().close();
					Game.setClient(null);
				}
	    		dispose();
	    	}
	    });
		
		c.gridx = 0;
		c.gridy = 5;
		c.gridheight = 1;
		c.gridwidth = 1;
		this.getContentPane().add(cancelButton, c);
		
		this.pack();
		//this.setSize(new Dimension(180, 180));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
		
		this.setVisible(true);
	}
	
	private void addInputLine(int row, String labelText) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = row;
		c.gridheight = 1;
		c.gridwidth = 1;
		
		// Add text field description label
		JLabel fieldLabel = new JLabel(labelText);
		
		fieldLabel.setFont(fieldLabel.getFont().deriveFont(14.0f));
		
		c.gridx = 0;
		this.getContentPane().add(fieldLabel, c);
		
		// Add input text field
		JTextField textField = new JTextField(20);
		this.textFields.add(textField);
		
		textField.setFont(fieldLabel.getFont().deriveFont(14.0f));
		
		c.gridx = 1;
		this.getContentPane().add(textField, c);
	}
	
	public void setLoggedIn() {
		this.loggedIn = true;
	}
	
	public boolean isLoggedIn() {
		return this.loggedIn;
	}
}
