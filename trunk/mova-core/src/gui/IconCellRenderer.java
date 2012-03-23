
package gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import simulator.NewDomain;
import type.AgentType;
import type.ItemType;

public class IconCellRenderer  implements TableCellRenderer{
    //private NewDomain _domain;
    ImageIcon _agentIcon;
    ImageIcon _sensorIcon;
    ImageIcon _itemIcon;
    ImageIcon _personIcon;
    ImageIcon _bgImage;

    public IconCellRenderer(NewDomain domain){
        //_domain = domain;
        _agentIcon = new ImageIcon(getClass().getResource("icons/agent.png"));
        _sensorIcon = new ImageIcon(getClass().getResource("icons/sensor.gif"));
        _itemIcon = new ImageIcon(getClass().getResource("icons/item.gif"));
        _personIcon = new ImageIcon(getClass().getResource("icons/person.gif"));
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cell = new JLabel();

        cell.setBackground(new Color(0,0,0,0));

        if(isAgent((String)value))
            cell.setIcon(_agentIcon);
        else if(isSensor((String)value))
             cell.setIcon(_sensorIcon);
        else if(isItem((String)value))
             cell.setIcon(_itemIcon);
        else
            cell.setText((String)value);

        cell.setOpaque(false);
        if((row == 9 && column == 4) || (row == 10 && column == 4) || (row == 10 && column == 8) || (row == 11 && column == 7) || (row == 13 && column == 9) || (row == 13 && column == 10))
            cell.setIcon(_personIcon);
        if((row == 4 && column == 18) || (row == 4 && column == 19) || (row == 4 && column == 20) || (row == 3 && column == 18) || (row == 3 && column == 19) || (row == 3 && column == 20))
            cell.setIcon(_personIcon);
        
        return cell;
    }

    private boolean isAgent(String value) {
        try{
            @SuppressWarnings("unused")
			AgentType type = AgentType.valueOf((String)value);
            return true;
        }
        catch(IllegalArgumentException e){
            return false;
        }
    }

    private boolean isSensor(String value) {
        return value.contains("Sensor");
    }

    private boolean isItem(String value) {
        try{
            @SuppressWarnings("unused")
			ItemType type = ItemType.valueOf((String)value);
            return true;
        }
        catch(IllegalArgumentException e){
            return false;
        }
    }

//    private void colorCells(String value, JLabel cell, JTable table, int row, int column) {
//		for(int i = 0; i < table.getRowCount(); i++)
//			for(int j = 0; j < table.getColumnCount(); j++){
//				if(calcDistance(row, column, i, j) <= _domain.getScanDistance() && ((String)table.getValueAt(i, j)).contains("Sensor"))
//					cell.setBackground(Color.GREEN);
//			}
//    }

//    private double calcDistance(int row1 , int column1, int row2 , int column2) {
//		double latitudeDiff = row1 - row2;
//		double longitudeDiff = column1 - column2;
//		return Math.sqrt(latitudeDiff * latitudeDiff + longitudeDiff * longitudeDiff);
//    }
}
