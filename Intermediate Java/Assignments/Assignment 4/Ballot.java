import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Ballot extends JPanel {
	private int ballotNumber;
	private String ballotName;
	private JButton[] candidates;

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
		private boolean clicked = false;
		public void reset() {
			clicked = false;
		}
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			clicked = !clicked;
			if (clicked) {
				source.setForeground(Color.RED);
			}
			if (!clicked) {
				source.setForeground(Color.BLACK);
			}
		}
	}
}