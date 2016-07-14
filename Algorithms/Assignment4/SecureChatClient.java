/*
Rebecca Addison
Assignment 4
CS1501
*/

import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.math.*;

public class SecureChatClient extends JFrame implements Runnable, ActionListener {

    public static final int PORT = 8765;

    ObjectOutputStream myWriter;
    ObjectInputStream myReader;
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName, serverName;
	Socket connection;
    BigInteger serverE, serverN;
    String encType;
    SymCipher cipher;

    public SecureChatClient()
    {
        try {
            serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
            InetAddress addr =
                    InetAddress.getByName(serverName);
            connection = new Socket(addr, PORT);   // Connect to server with new
                                                   // Socket

            try {       // Get Writer and Reader
                myWriter = new ObjectOutputStream(connection.getOutputStream());
                myWriter.flush();
                myReader = new ObjectInputStream(connection.getInputStream()); 
            } catch (Exception ex) {
                System.out.println("Problem getting read/write");
                System.exit(1);
            }

            try {
                serverE = (BigInteger) myReader.readObject();
                serverN = (BigInteger) myReader.readObject();
                encType = (String) myReader.readObject();
            } catch (Exception ex) {
                System.out.println("Problem getting encryption information");
                System.exit(1);
            }
            System.out.print("E received from server: ");
            System.out.println(serverE);
            System.out.print("N received from server: ");
            System.out.println(serverN);
            System.out.print("Encryption type: ");
            if (encType.equals("Sub")) {
                cipher = new Substitute();
                System.out.println("Substitute");
            } else {
                cipher = new Add128();
                System.out.println("Add128");
            }
            byte[] key = cipher.getKey();
            System.out.print("Key: ");
            for (int i = 0; i < key.length; i++) {
                System.out.print(key[i] + " ");
            }
            System.out.println("\n");
            BigInteger bigKey = new BigInteger(1, key);
            if (bigKey.compareTo(BigInteger.ZERO) < 0) {
                System.out.println("PROBLEM ? \n\n\n\n\n\n");
            }
            BigInteger encKey = bigKey.modPow(serverE, serverN);
            try {
                myWriter.writeObject(encKey);
            } catch (Exception ex) {
                System.out.println("Problem sending key");
                System.exit(1);
            }

            myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
            
            byte[] name = cipher.encode(myName);
            try {
                myWriter.writeObject(name);   // Send name to Server.  Server will need
            } catch (Exception ex) {      // this to announce sign-on and sign-off of clients
                System.out.println("Problem sending name");
                System.exit(1);
            }

            this.setTitle(myName);      // Set title to identify chatter

            Box b = Box.createHorizontalBox();  // Set up graphical environment for
            outputArea = new JTextArea(8, 30);  // user
            outputArea.setEditable(false);
            b.add(new JScrollPane(outputArea));

            outputArea.append("Welcome to the Chat Group, " + myName + "\n");

            inputField = new JTextField("");  // This is where user will type input
            inputField.addActionListener(this);

            prompt = new JLabel("Type your messages below:");
            Container c = getContentPane();

            c.add(b, BorderLayout.NORTH);
            c.add(prompt, BorderLayout.CENTER);
            c.add(inputField, BorderLayout.SOUTH);

            Thread outputThread = new Thread(this);  // Thread is to receive strings
            outputThread.start();                    // from Server

    		addWindowListener(
                    new WindowAdapter()
                    {
                        public void windowClosing(WindowEvent e)
                        { try {
                              byte[] close = cipher.encode("CLIENT CLOSING");
                              myWriter.writeObject(close);
                              System.exit(0);
                          } catch (Exception ex) {
                              System.out.println("Problem sending close message");
                              System.exit(1);
                          }
                         }
                    }
                );

            setSize(500, 200);
            setVisible(true);

        }
        catch (Exception e)
        {
            System.out.println("Problem starting client!");
            System.exit(1);
        }
    }

    public void run() 
    {
        while (true)
        {
             try {
                byte[] msgBytes = (byte[]) myReader.readObject();
                System.out.print("Read byte array: ");
                for (int i = 0; i < msgBytes.length; i++) {
                    System.out.print(msgBytes[i] + " ");
                }
                System.out.println();
                String currMsg = cipher.decode(msgBytes);
                byte[] dcdBytes = currMsg.getBytes();
                System.out.print("Decoded byte array: ");
                for (int i = 0; i < dcdBytes.length; i++) {
                    System.out.print(dcdBytes[i] + " ");
                }
                System.out.println();
                System.out.print("Decoded string: ");
                System.out.println(currMsg);
                System.out.println();
			    outputArea.append(currMsg+"\n");
             }
             catch (Exception e)
             {
                System.out.println(e +  ", closing client!");
                break;
             }
        }
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e) 
    {
        String currMsg = e.getActionCommand();      // Get input value
        inputField.setText("");
        currMsg = myName + ": " + currMsg;
        System.out.print("Sending string: ");
        System.out.println(currMsg);
        System.out.print("Plain byte array: ");
        byte[] msgBytes = currMsg.getBytes();
        for (int i = 0; i < msgBytes.length; i++) {
            System.out.print(msgBytes[i] + " ");
        }
        System.out.println();
        byte[] encBytes = cipher.encode(currMsg);
        System.out.print("Encoded byte array: ");
        for (int i = 0; i < encBytes.length; i++) {
            System.out.print(encBytes[i] + " ");
        }
        System.out.println("\n");
        try {
            myWriter.writeObject(encBytes);
        }                       // Add name and send it to Server
        catch (Exception ex) {
            System.out.println("Problem sending message");
        }
    } 

    public static void main(String [] args)  throws IOException 
    {
         SecureChatClient JR = new SecureChatClient();
         JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}