package view.components;

import view.FontManager;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.Color;
import java.awt.Dimension;

public class TableComponent extends JTable {

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
        FontManager.Instance().useRegular(this, 14f);
        setRowHeight(ROW_HEIGHT);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setShowGrid(false);
        setFillsViewportHeight(true);
        getTableHeader().setReorderingAllowed(true);
    }

    private void setupHeaderStyle() {
        JTableHeader header = getTableHeader();
        header.setPreferredSize(
                new Dimension(header.getPreferredSize().width, HEADER_HEIGHT));
        FontManager.Instance().useBold(header, 14f);
        header.setOpaque(false);
        header.setBackground(new Color(245, 245, 245));
        header.setForeground(Color.BLACK);
    }

    public void adjustColumnWidths() {
        int totalWidth = getParent().getWidth();
        for (int i = 0; i < getColumnCount() && i < columnProportions.length;
             i++) {
            TableColumn column = getColumnModel().getColumn(i);
            int width = (int) (totalWidth * columnProportions[i]);
            column.setPreferredWidth(width);
            column.setMinWidth(
                    50); // Set minimum width to prevent columns from disappearing
        }
    }
}
