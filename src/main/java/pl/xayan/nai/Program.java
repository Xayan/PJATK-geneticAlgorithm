package pl.xayan.nai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Program {
    public static void main(String[] args) {
        JFrame mainWindow = new JFrame();
        mainWindow.setTitle("NAI");
        mainWindow.setSize(400, 200);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new GridLayout(0, 2, 20, 10));

        JLabel populationCountLabel = new JLabel("Number of populations:");
        JTextField populationCountField = new JTextField("500");

        JLabel populationMembersLabel = new JLabel("Number of individuals:");
        JTextField populationMembersField = new JTextField("100");

        JLabel cityCountLabel = new JLabel("Number of cities:");
        JTextField cityCountField = new JTextField("50");

        JLabel blankLabel = new JLabel();
        JButton submitButton = new JButton("Start simulation");

        submitButton.addActionListener(e -> {
            Simulation simulation = new Simulation();

            simulation.setPopulationCount(Integer.parseInt(populationCountField.getText()));
            simulation.setPopulationMembers(Integer.parseInt(populationMembersField.getText()));
            simulation.setCityCount(Integer.parseInt(cityCountField.getText()));

            simulation.prepare();
        });

        mainWindow.add(cityCountLabel);
        mainWindow.add(cityCountField);
        mainWindow.add(populationCountLabel);
        mainWindow.add(populationCountField);
        mainWindow.add(populationMembersLabel);
        mainWindow.add(populationMembersField);
        mainWindow.add(blankLabel);
        mainWindow.add(submitButton);

        mainWindow.setVisible(true);
    }
}
