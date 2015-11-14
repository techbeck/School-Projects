import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Ballot extends JPanel {
	private int ballotNumber;
	private String ballotName;
	private JButton[] candidates;
	private HashSet<JButton> clicked;

	public Ballot(String num, String name, String[] options) {
		ballotNumber = Integer.parseInt(num);
		ballotName = name;
		JLabel label = new JLabel(ballotName, SwingConstants.CENTER);
		//setAlignmentX(Component.CENTER_ALIGNMENT);
		add(label, BorderLayout.CENTER);
		candidates = new JButton[options.length];
		setLayout(new GridLayout(candidates.length +1,1));
		for (int i = 0; i < candidates.length; i++) {
			candidates[i] = new JButton(options[i]);
			candidates[i].setFont(new Font("Serif", Font.PLAIN, 15));
			candidates[i].addActionListener(new MyListener());
			candidates[i].setEnabled(false);
			add(candidates[i]);
		}
		clicked = new HashSet<JButton>();
	}

	public void enableBallot() {
		for(int i = 0; i < candidates.length; i++) {
			candidates[i].setEnabled(true);
		}
	}

	public void disableBallot() {
		for(int i = 0; i < candidates.length; i++) {
			candidates[i].setEnabled(false);
			MyListener listener = (MyListener) candidates[i].getActionListeners()[0];
			listener.reset();
			candidates[i].setForeground(Color.BLACK);
		}
	}

	class MyListener implements ActionListener {
		public void reset() {
			if (clicked.size() > 0) {
				clicked.removeAll(clicked);
			}
		}
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			if (!clicked.contains(source)) {
				source.setForeground(Color.RED);
				clicked.add(source);
			} else if (clicked.contains(source)) {
				source.setForeground(Color.BLACK);
				clicked.remove(source);
			}
			if (clicked.size() > 1) {
				Iterator iter = clicked.iterator();;
				while (iter.hasNext()) {
					JButton extra = (JButton) iter.next();
					extra.setForeground(Color.BLACK);
					iter.remove();
				}
			}
		}
	}
}