/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import simulator.Domain;


public class MultiLineCellRenderer extends JTextArea implements TableCellRenderer{
    
	private static final long serialVersionUID = -1813140822032247388L;
	
	private Domain _domain;
	
	public MultiLineCellRenderer(Domain domain) {
    setLineWrap(true);
    setWrapStyleWord(true);
    setOpaque(true);
    _domain = domain;
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      setBackground(table.getSelectionBackground());
    } else {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }
    setFont(table.getFont());
    if (hasFocus) {
      setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
      if (table.isCellEditable(row, column)) {
        setForeground(UIManager.getColor("Table.focusCellForeground"));
        setBackground(UIManager.getColor("Table.focusCellBackground"));
      }
    } else {
      setBorder(new EmptyBorder(1, 2, 1, 2));
    }
    setText((value == null) ? "" : value.toString());
    //if(value.toString().contains("Sensor"))
    	colorCells(table, row, column);
    
    return this;
  }
  
  private void colorCells(JTable table, int row, int column) {
	for(int i = 0; i < _domain.getDomainSize(); i++)
		for(int j = 0; j < _domain.getDomainSize(); j++){
			if(calcDistance(row, column, i, j) <= _domain.getScanDistance() && table.getValueAt(i, j).toString().contains("Sensor"))
				setBackground(Color.LIGHT_GRAY);
		}
  }

private double calcDistance(int row1 , int column1, int row2 , int column2) {
			double latitudeDiff = row1 - row2;
			double longitudeDiff = column1 - column2;
			return Math.sqrt(latitudeDiff * latitudeDiff + longitudeDiff * longitudeDiff);
  }
}
