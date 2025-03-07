import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ToDoListApp {
    private JFrame frame;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskInput;
    private static final String FILE_NAME = "tasks.txt";

    public ToDoListApp() {
        frame = new JFrame("To-Do List");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        taskListModel = new DefaultListModel<>();
        loadTasks();
        taskList = new JList<>(taskListModel);
        taskList.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(taskList);
        
        taskInput = new JTextField();
        JButton addButton = new JButton("Add Task");
        JButton deleteButton = new JButton("Delete Task");
        JButton completeButton = new JButton("Complete Task");
        JButton moveUpButton = new JButton("Move Up");
        JButton moveDownButton = new JButton("Move Down");
        
        addButton.addActionListener(event -> addTask(taskInput.getText()));
        deleteButton.addActionListener(e -> deleteTask());
        completeButton.addActionListener(e -> markTaskComplete());
        moveUpButton.addActionListener(e -> moveTaskUp());
        moveDownButton.addActionListener(e -> moveTaskDown());
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(taskInput);
        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(completeButton);
        panel.add(moveUpButton);
        panel.add(moveDownButton);
        
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        
        frame.setVisible(true);
    }
    
    private void addTask(String task) {
        if (!task.isEmpty()) {
            taskListModel.addElement(task);
            animateTask(task);
            taskInput.setText("");
            saveTasks();
        }
    }
    
    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            taskListModel.remove(selectedIndex);
            saveTasks();
        }
    }
    
    private void markTaskComplete() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String completedTask = "<html><strike>" + taskListModel.get(selectedIndex) + "</strike></html>";
            taskListModel.set(selectedIndex, completedTask);
            saveTasks();
        }
    }
    
    private void moveTaskUp() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex > 0) {
            String task = taskListModel.get(selectedIndex);
            taskListModel.remove(selectedIndex);
            taskListModel.add(selectedIndex - 1, task);
            taskList.setSelectedIndex(selectedIndex - 1);
            saveTasks();
        }
    }
    
    private void moveTaskDown() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex < taskListModel.size() - 1) {
            String task = taskListModel.get(selectedIndex);
            taskListModel.remove(selectedIndex);
            taskListModel.add(selectedIndex + 1, task);
            taskList.setSelectedIndex(selectedIndex + 1);
            saveTasks();
        }
    }
    
    private void animateTask(String task) {
        JLabel taskLabel = new JLabel(task);
        taskLabel.setFont(new Font("Arial", Font.BOLD, 16));
        taskLabel.setForeground(Color.BLUE);
        frame.add(taskLabel, BorderLayout.NORTH);
        
        Timer timer = new Timer(50, new ActionListener() {
            int y = 0;
            public void actionPerformed(ActionEvent e) {
                taskLabel.setLocation(taskLabel.getX(), y);
                y += 5;
                if (y > 50) {
                    frame.remove(taskLabel);
                    frame.revalidate();
                    frame.repaint();
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }
    
    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskListModel.size(); i++) {
                writer.write(taskListModel.getElementAt(i) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                taskListModel.addElement(line);
            }
        } catch (IOException e) {
            System.out.println("No previous tasks found.");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListApp::new);
    }
}
