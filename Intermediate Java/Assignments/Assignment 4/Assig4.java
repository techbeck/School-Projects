/*
Rebecca Addison
CS 0401
Lecture Section 1060
Lab Section 1080
*/

/**
Questions:
- does Ballot need to be a separate file?
	- if so, should Voter be in a separate file too?
- for Boolean.parseBoolean(String s) assume nothing but true or false will be input?
- wonky alignment
- should I use Path stuff instead of File .renameTo()
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
	private File votersFile;

	public Assig4(String ballotsName) throws IOException {
		votersFile = new File("voters.txt");
		Scanner votersReader = returnScanner(votersFile, "Voter file not found.");
		voters = new ArrayList<Voter>();
		while (votersReader.hasNext()) {
			voters.add(new Voter(votersReader.nextLine()));
		}
		votersReader.close();
		File ballotsFile = new File(ballotsName);
		Scanner ballotsReader = returnScanner(ballotsFile, "Ballots file not found.");
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

		window = new JFrame("Voting Program v1.1");
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
			boolean repeat = false;
			do {
				String voterID = JOptionPane.showInputDialog(null, "Please enter your voter ID");
				if (voterID != null && voterID.length() != 0) {
					int id = Integer.parseInt(voterID);
					boolean valid = false;
					boolean found = false;
					for(int i = 0; i < voters.size(); i++) {
						if (id == voters.get(i).getID()) {
							found = true;
							if (voters.get(i).hasVoted()) {
								JOptionPane.showMessageDialog(null, voters.get(i).getName() + ", you have already voted!");
								break;
							} else {
								valid = true;
								voter = voters.get(i);
								break;
							}
						}
					}
					if (!found) {
						int response = JOptionPane.showConfirmDialog(null, id + " is not a valid id.\nWould you like to register?");
						if (response == 0) {
							registerNewVoter();
							valid = true;
						} else {
							JOptionPane.showMessageDialog(null, "Without a valid voter ID, you can't vote.");
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
					repeat = false;
				} else {
					repeat = true;
					JOptionPane.showMessageDialog(null, "That's not a valid input.");
				}
			} while (repeat);
		}
	}

	class VoteListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int response = JOptionPane.showConfirmDialog(null, "Please confirm your vote");
			if (response != 0) {
				return;
			}
			recordBallots();
			loginButton.setEnabled(true);
			voteButton.setEnabled(false);
			recordVoter();
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
		public String toString() {
			return id + ":" + name + ":" + voted;
		}
	}

	public void registerNewVoter() {
		boolean repeat = false;
		String name = null;
		int id = 0;
		do {
			name = JOptionPane.showInputDialog(null, "What's your name?");
			if (name == null || name.length() == 0) {
				repeat = true;
				JOptionPane.showMessageDialog(null, "That's not a valid input.");
			} else {
				repeat = false;
			}
		} while (repeat);
		do {
			Random randomNum = new Random();
			id = randomNum.nextInt(9000) + 1000;
			for (Voter v: voters) {
				if (v.getID() == id) {
					repeat = true;
					break;
				}
				repeat = false;
			}
		} while (repeat);
		JOptionPane.showMessageDialog(null, name + ", your id is " + id + ".\nPlease record it for future use.");
		String voterInfo = id + ":" + name + ":" + "false";
		voter = new Voter(voterInfo);
		voters.add(voter);
	}

	public Scanner returnScanner(File fileName, String errorMessage) {
		if (!fileName.exists()) {
			JOptionPane.showMessageDialog(null, errorMessage);
			System.exit(1);
		}
		Scanner scan = null;
		try {
			scan = new Scanner(fileName);
		} catch (Exception ex) {}
		return scan;
	}

	public void recordBallots() {
		for (int i = 0; i < ballots.size(); i++) {
			String ballotNumber = ballots.get(i).getBallotNumber();
			File ballotF = new File(ballotNumber + ".txt");
			Scanner ballotR = returnScanner(ballotF, "Ballot " + ballotNumber + " file not found.");
			PrintWriter ballotW = null;
			try {
				ballotW = new PrintWriter("tempB.txt");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Something went wrong saving the ballot.");
				System.exit(1);
			}
			Ballot.Candidate[] candidates = ballots.get(i).accessCandidates();
			Ballot.Candidate selected = ballots.get(i).getSelected();
			for (int j = 0; j < candidates.length; j++) {
				String candidate = ballotR.nextLine();
				String[] split = candidate.split(":");
				int numVotes = Integer.parseInt(split[1]);
				candidates[j].initialVotes(numVotes);
				if (candidates[j].equals(selected)) {
					candidates[j].addVote();
				}
				ballotW.println(candidates[j].getName() + ":" + candidates[j].getVotes());
			}
			ballotR.close();
			ballotW.close();
			File tempFileB = new File("tempB.txt");
			tempFileB.renameTo(ballotF);
			ballots.get(i).disableBallot();
		}
	}

	public void recordVoter() {
		voter.vote();
		PrintWriter voterW = null;
		try {
			voterW = new PrintWriter("tempV.txt");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Something went wrong saving the voter info.");
			System.exit(1);
		}
		for (Voter v: voters) {
			voterW.println(v);
		}
		voterW.close();
		File tempFileV = new File("tempV.txt");
		tempFileV.renameTo(votersFile);
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