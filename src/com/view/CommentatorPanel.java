package com.view;

import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CommentatorPanel extends JPanel {
    private JTextArea commentatorTextArea;
    public CommentatorPanel() {
        setLayout(new BorderLayout());
        commentatorTextArea = new JTextArea();
        commentatorTextArea.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(commentatorTextArea);

        commentatorTextArea.setEditable(false);
        commentatorTextArea.setFocusable(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        commentatorTextArea.setLineWrap(false);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void writeComments(String comments){
        commentatorTextArea.setText(comments);
    }
}
