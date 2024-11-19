import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreams {
    private JTextArea OTextArea, FTextArea;
    private JTextField SearchTextField;
    private File SelectedFile;

    public DataStreams() {

        //JFRAME
        JFrame Frame = new JFrame("Data Streams");
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setSize(500,500);

        //TEXT AREAS
        OTextArea = new JTextArea();
        FTextArea = new JTextArea();
        OTextArea.setEditable(false);
        FTextArea.setEditable(false);
        SearchTextField = new JTextField(20);

        //BUTTONS
        JButton LoadButton = new JButton("Load File");
        JButton SearchButton = new JButton("Search");
        JButton QuitButton = new JButton("Quit");

        LoadButton.addActionListener(e -> LoadFile());
        SearchButton.addActionListener(e -> SearchFile ());
        QuitButton.addActionListener(e -> System.exit(0));

        //BOTTOM PANEL
        JPanel BPanel = new JPanel();
        BPanel.add(new JLabel("Search"));
        BPanel.add(SearchTextField);
        BPanel.add(LoadButton);
        BPanel.add(SearchButton);
        BPanel.add(QuitButton);

        //SPLIT PANE
        JSplitPane SplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(OTextArea),
                new JScrollPane(FTextArea));
        SplitPane.setDividerLocation(300);

        Frame.add(SplitPane, BorderLayout.CENTER);
        Frame.add(BPanel, BorderLayout.SOUTH);

        Frame.setVisible(true);
    }
    private void LoadFile() {

        JFileChooser FileChooser = new JFileChooser();
        if (FileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            SelectedFile = FileChooser.getSelectedFile();
            try (Stream<String> lines = Files.lines(SelectedFile.toPath())){
                OTextArea.setText(lines.collect(Collectors.joining("\n")));
            }catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error Loading File: " + e.getMessage());
            }
        }
    }
    private void SearchFile() {
        if (SelectedFile == null) {
            JOptionPane.showMessageDialog(null, "Load File");
            return;
        }

        //SEARCH FIELD
        String SearchString = SearchTextField.getText().toLowerCase();
        if (SearchString.isEmpty()) {
            try (Stream<String> lines = Files.lines(SelectedFile.toPath())) {
                String FilterdContent = lines.filter(Line -> Line.toLowerCase().contains(SearchString)).collect(Collectors.joining("\n"));
                FTextArea.setText(FilterdContent);
            }catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error Loading File: " + e.getMessage());
            }
        }else{
            JOptionPane.showMessageDialog(null, "Search");
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DataStreams::new);
    }
}