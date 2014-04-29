package com.kentropy.components;

//Example1b.java
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

public class Example1b {
	private static final String window_title = "Edit table display";
	private static final int locate_X = 50;
	private static final int locate_Y = 50;
	private static final int window_width = 320;
	private static final int window_height = 160;
	private static final int table_width = 300;
	private static final int table_height = 120;
	private static final int table_row_height = 20;
	// data
	Object[] colNames = { "name", "amount", "size", "color", "note" };

	Object[][] rowData = { { "A01", "20", "large", "white", "soft" },
			{ "K01", "5", "thin", "red", "strong" },
			{ "U01", "100", "middle", "yellow", "cheap" },
			{ "S01", "2.5", "middle", "black", "quality" }, };

	// main
	public static void main(final String args[]) {
		final Example1b sample = new Example1b();
	}

	// constructor
	public Example1b() {
		final JFrame f = new JFrame(Example1b.window_title);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JTable t = new JTable(this.rowData, this.colNames);
		t.setRowHeight(Example1b.table_row_height);
		t.setGridColor(Color.gray);

		for (int i = 0; i < t.getColumnCount(); ++i) {
			final TableColumn tc = t.getColumn(t.getColumnName(i));
			tc.setCellRenderer(new MyRenderer());
		}

		final JScrollPane sp = new JScrollPane();
		sp.getViewport().setView(t);
		sp.setPreferredSize(new Dimension(Example1b.table_width,
				Example1b.table_height));

		final JPanel p = new JPanel();
		p.add(sp);
		f.getContentPane().add(p, BorderLayout.CENTER);

		f.setBounds(Example1b.locate_X, Example1b.locate_Y,
				Example1b.window_width, Example1b.window_height);
		f.setVisible(true);
	}

	// CellRenderer
	class MyRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(final JTable table,
				final Object value, final boolean isSelected,
				final boolean hasFocus, final int row, final int column) {
			switch (column) {
			case 1:
				this.setHorizontalAlignment(SwingConstants.RIGHT);
				break;
			case 2:
				this.setHorizontalAlignment(SwingConstants.CENTER);
				break;
			case 3:
				this.setHorizontalAlignment(SwingConstants.CENTER);
				switch (row) {
				case 0:
					this.setBackground(Color.black);
					this.setForeground(Color.white);
					break;
				default:
					this.setBackground(Color.yellow);
					this.setForeground(Color.red);
					break;
				}
			default:
				break;
			}
			return super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
		}
	}
}
