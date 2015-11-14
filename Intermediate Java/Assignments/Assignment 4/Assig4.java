/*
Rebecca Addison
CS 0401
Lecture Section 1060
Lab Section 1080
*/

/**
Questions:
- does Ballot need to be a separate file?
- for Boolean.parseBoolean(String s) assume nothing but true or false will be input?
- wonky alignment
- handling invalid voter id (ignore/message/etc)
- handling multiple selections (have to deselect in order to select new, or automatically(how?))?
- for write-in candidates, how/when should they be added to the ballot?
*/

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Assig4 {
	private JFrame window;
	private ArrayList<Ballot> ballots;
	private ArrayList<Voter> voters;
	private JButton loginButton;
	private JButton voteButton;
	private Voter voter;

	public Assig4(String ballotsName) throws IOException {
		File votersFile = new File("voters.txt");
		if (!votersFile.exists()) {
			JOptionPane.showMessageDialog(null, "Voter file not found.");
			System.exit(1);
		}
		Scanner votersReader = new Scanner(votersFile);
		voters = new ArrayList<Voter>();
		while (votersReader.hasNext()) {
			voters.add(new Voter(votersReader.nextLine()));
		}
		votersReader.close();
		File ballotsFile = new File(ballotsName);
		if (!ballotsFile.exists()) {
			JOptionPane.showMessageDialog(null, "Ballot file not found.");
			System.exit(1);
		}
		Scanner ballotsReader = new Scanner(ballotsFile);
		ballots = new ArrayList<Ballot>();
		int numBallots = ballotsReader.nextInt();
		ballotsReader.nextLine();
		for (int i = 0; i < numBallots; i++) {
			String ballot = ballotsReader.nextLine();
			String[] split1 = ballot.split(":");
			String[] split2 = split1[2].split(",");
			ballots.add(new Ballot(split1[0], split1[1], split2));
		}
		ballotsReader.close();
		JPanel loginPanel = new JPanel();
		loginButton = new JButton("Login to vote");
		loginButton.addActionListener(new LoginListener());
		loginPanel.add(loginButton);

		JPanel votePanel = new JPanel();
		voteButton = new JButton("Cast your vote");
		voteButton.setEnabled(false);
		voteButton.addActionListener(new VoteListener());
		votePanel.add(voteButton);

		window = new JFrame("Voting Program v1.0");
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		for(int i = 0; i < numBallots; i++) {
			window.add(ballots.get(i));
		}
		window.add(loginPanel);
		window.add(votePanel);
		window.setLayout(new GridLayout(1,numBallots +2));
		window.setVisible(true);
		window.pack();
	}

	class LoginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String voterID = JOptionPane.showInputDialog(null, "Please enter your voter id");
			if (voterID != null && voterID.length() != 0) {
				int id = Integer.parseInt(voterID);
				boolean valid = false;
				for(int i = 0; i < voters.size(); i++) {
					if (id == voters.get(i).getID()) {
						valid = true;
						voter = voters.get(i);
					}
				}
				if (valid) {
					JOptionPane.showMessageDialog(null, voter.getName() + ", please make your choices");
					for (int i = 0; i < ballots.size(); i++) {
						ballots.get(i).enableBallot();
					}
					voteButton.setEnabled(true);
					loginButton.setEnabled(false);
				}
			}
		}
	}

	class VoteListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int response = JOptionPane.showConfirmDialog(null, "Please confirm your vote");
			if (response == 0) {
				loginButton.setEnabled(true);
				for (int i = 0; i < ballots.size(); i++) {
					ballots.get(i).disableBallot();
				}
				voteButton.setEnabled(false);
			}
			voter.vote();
		}
	}

	class Voter {
		private int id;
		private String name;
		private boolean voted;
		public Voter(String info) {
			String[] voterInfo = info.split(":");
			id = Integer.parseInt(voterInfo[0]);
			name = voterInfo[1];
			voted = Boolean.parseBoolean(voterInfo[2]);
		}
		public int getID() {
			return id;
		}
		public String getName() {
			return name;
		}
		public boolean hasVoted() {
			return voted;
		}
		public void vote() {
			voted = true;
		}
	}

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("Enter ballot text file name.");
			System.exit(1);
		}
		if (args.length == 0) {
			System.out.println("Enter ballot text file name.");
			System.exit(1);
		}
		String name = args[0];
		new Assig4(name);
	}
}