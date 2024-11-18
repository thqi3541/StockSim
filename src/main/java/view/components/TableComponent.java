package view.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;

public class TableComponent extends JTable {
    private static final Font TABLE_FONT = new Font("Lucida Sans", Font.PLAIN, 14);
    private static final Font HEADER_FONT = new Font("Lucida Sans", Font.BOLD, 14);
    private static final int ROW_HEIGHT = 30;
    private static final int HEADER_HEIGHT = 30;
    private final double[] columnProportions;

    public TableComponent(DefaultTableModel model, double[] columnProportions) {
        super(model);
        this.columnProportions = columnProportions;

        setupTableStyle();
        setupHeaderStyle();
    }

    private void setupTableStyle() {
        setFont(TABLE_FONT);
        setRowHeight(ROW_HEIGHT);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setShowGrid(false);
        setGridColor(Color.LIGHT_GRAY);
        setFillsViewportHeight(true);
        getTableHeader().setReorderingAllowed(true);
    }

    private void setupHeaderStyle() {
        JTableHeader header = getTableHeader();
        header.setFont(HEADER_FONT);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, HEADER_HEIGHT));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
    }

    public void adjustColumnWidths() {
        int totalWidth = getParent().getWidth();
        for (int i = 0; i < getColumnCount() && i < columnProportions.length; i++) {
            TableColumn column = getColumnModel().getColumn(i);
            int width = (int) (totalWidth * columnProportions[i]);
            column.setPreferredWidth(width);
            column.setMinWidth(50); // Set minimum width to prevent columns from disappearing
        }
    }
}
