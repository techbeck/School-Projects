import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Ballot extends JPanel {
	private String ballotNumber;
	private String ballotName;
	private Candidate[] candidates;
	private Candidate selected;

	public Ballot(String num, String name, String[] options) {
		ballotNumber = num;
		ballotName = name;
		JLabel label = new JLabel(ballotName, SwingConstants.CENTER);
		//setAlignmentX(Component.CENTER_ALIGNMENT);
		add(label, BorderLayout.CENTER);
		candidates = new Candidate[options.length];
		setLayout(new GridLayout(candidates.length +1,1));
		for (int i = 0; i < candidates.length; i++) {
			candidates[i] = new Candidate(options[i]);
			add(candidates[i]);
		}
		selected = null;
	}

	class Candidate extends JButton {
		private String candidateName;
		private int numVotes;
		public Candidate(String name) {
			candidateName = name;
			setText(candidateName);
			setFont(new Font("Serif", Font.PLAIN, 15));
			addActionListener(new CandidateListener());
			setEnabled(false);
		}
		public void initialVotes(int initial) {
			numVotes = initial;
		}
		public void addVote() {
			numVotes++;
		}
		public String getName() {
			return candidateName;
		}
		public int getVotes() {
			return numVotes;
		}
	}

	class CandidateListener implements ActionListener {
		public void reset() {
			selected = null;
		}
		public void actionPerformed(ActionEvent e) {
			Candidate source = (Candidate) e.getSource();
			if (selected == null) {
				source.setForeground(Color.MAGENTA);
				selected = source;
				return;
			}
			if (selected == source) {
				source.setForeground(Color.BLACK);
				selected = null;
				return;
			}
			selected.setForeground(Color.BLACK);
			source.setForeground(Color.MAGENTA);
			selected = source;
		}
	}

	public void enableBallot() {
		for(int i = 0; i < candidates.length; i++) {
			candidates[i].setEnabled(true);
		}
	}

	public void disableBallot() {
		for(int i = 0; i < candidates.length; i++) {
			candidates[i].setEnabled(false);
			CandidateListener listener = (CandidateListener) candidates[i].getActionListeners()[0];
			listener.reset();
			candidates[i].setForeground(Color.BLACK);
		}
	}

	public String getBallotNumber() {
		return ballotNumber;
	}

	public Candidate[] accessCandidates() {
		return candidates;
	}

	public Candidate getSelected() {
		return selected;
	}
}