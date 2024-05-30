import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;
import java.util.Comparator;
import java.util.regex.PatternSyntaxException;
 

public class BookManager extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private BufferedImage backgroundImage;
    private Connection conn;

    public BookManager() {
        setTitle("Book Manager");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

    
        backgroundImage = loadImage("background.jpg");

         
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        String[] columnNames = {"#", "Title", "Author", "Year", "Genre", "Actions"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;  
            }
        };
        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    ((JComponent) c).setOpaque(true);
                    c.setBackground(new Color(255, 255, 255, 180)); 
                }
                return c;
            }
        };
        table.setOpaque(false);
        table.setBackground(new Color(255, 255, 255, 180));  
        table.setRowHeight(30);

         
        TableColumn column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column.setMaxWidth(50);
        column.setMinWidth(50);

         
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setComparator(1, Comparator.comparing(Object::toString));
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255, 255, 255, 128));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topPanel.setOpaque(false);

        JPanel searchPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255, 255, 255, 128));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        searchPanel.setOpaque(false);
        searchField = new JTextField();
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterBooks();
            }
        });
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(e -> addBook());
        searchPanel.add(addButton, BorderLayout.EAST);

        topPanel.add(searchPanel, BorderLayout.CENTER);

        backgroundPanel.add(topPanel, BorderLayout.NORTH);

         
        table.getColumn("Actions").setCellRenderer(new PanelRenderer());
        table.getColumn("Actions").setCellEditor(new PanelEditor());

         
        connectToDatabase();
        loadBooksFromDatabase();
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void connectToDatabase() {
        try {
             
            String url = "jdbc:sqlite:books.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBooksFromDatabase() {
        String sql = "SELECT id, title, author, year, genre, description FROM books";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    model.getRowCount() + 1,
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("year"),
                    rs.getString("genre"),
                    rs.getString("description")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showInfo(int row) {
        if (row >= 0) {
            String title = model.getValueAt(row, 1).toString();
            String author = model.getValueAt(row, 2).toString();
            String year = model.getValueAt(row, 3).toString();
            String genre = model.getValueAt(row, 4).toString();
            String description = model.getValueAt(row, 5).toString();
            JOptionPane.showMessageDialog(this, "Title: " + title + "\nAuthor: " + author + "\nYear: " + year + "\nGenre: " + genre + "\nDescription: " + description);
        }
    }

    private void deleteBook(int row) {
        if (row >= 0) {
            int id = getIdFromRow(row);
            model.removeRow(row);
            updateRowNumbers();
            deleteBookFromDatabase(id);
        }
    }

    private int getIdFromRow(int row) {
        String title = model.getValueAt(row, 1).toString();
        String author = model.getValueAt(row, 2).toString();
        try {
            String sql = "SELECT id FROM books WHERE title = ? AND author = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void deleteBookFromDatabase(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addBook() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField genreField = new JTextField();
        JTextField descriptionField = new JTextField();

        Object[] fields = {
            "Title:", titleField,
            "Author:", authorField,
            "Year:", yearField,
            "Genre:", genreField,
            "Description:", descriptionField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String author = authorField.getText();
            int year = Integer.parseInt(yearField.getText());
            String genre = genreField.getText();
            String description = descriptionField.getText();
            addBookToDatabase(title, author, year, genre, description);
            model.setRowCount(0);  
            loadBooksFromDatabase(); 
        }
    }

    private void addBookToDatabase(String title, String author, int year, String genre, String description) {
        String sql = "INSERT INTO books(title, author, year, genre, description) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, year);
            pstmt.setString(4, genre);
            pstmt.setString(5, description);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateRowNumbers() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0);
        }
    }

    private void filterBooks() {
        String regex = searchField.getText();
        TableRowSorter<?> sorter = (TableRowSorter<?>) table.getRowSorter();
        if (regex.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(new RowFilter<TableModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                        for (int i = 1; i < entry.getValueCount() - 1; i++) {  
                            if (entry.getStringValue(i).toLowerCase().contains(regex.toLowerCase())) {
                                return true;
                            }
                        }
                        return false;
                    }
                });
            } catch (PatternSyntaxException e) {
                sorter.setRowFilter(null);
            }
        }
    }

    private class PanelRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panel.setOpaque(false);
            JButton infoButton = new JButton("Info");
            infoButton.addActionListener(e -> showInfo(row));
            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(e -> deleteBook(row));
            panel.add(infoButton);
            panel.add(deleteButton);
            return panel;
        }
    }

    private class PanelEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private final JButton infoButton;
        private final JButton deleteButton;

        public PanelEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panel.setOpaque(false);
            infoButton = new JButton("Info");
            deleteButton = new JButton("Delete");

            infoButton.addActionListener(e -> showInfo(table.getSelectedRow()));
            deleteButton.addActionListener(e -> deleteBook(table.getSelectedRow()));

            panel.add(infoButton);
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return panel;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BookManager().setVisible(true);
        });
    }
}
