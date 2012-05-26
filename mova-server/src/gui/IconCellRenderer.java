
package gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import simulator.NewDomain;

public class IconCellRenderer  implements TableCellRenderer{
    //private NewDomain _domain;
    ImageIcon _agentIcon;
    ImageIcon _sensorIcon;
    ImageIcon _itemIcon;
    ImageIcon _personIcon;
    ImageIcon _bgImage;
    ImageIcon _agentItem;
    ImageIcon _itemDisabledIcon;
    ImageIcon _itemBusyIcon;

    public IconCellRenderer(NewDomain domain){
        //_domain = domain;
        _agentIcon = new ImageIcon(getClass().getResource("icons/agent.png"));
        _sensorIcon = new ImageIcon(getClass().getResource("icons/sensor.gif"));
        _itemIcon = new ImageIcon(getClass().getResource("icons/item.gif"));
        _personIcon = new ImageIcon(getClass().getResource("icons/person.gif"));
        _agentItem = new ImageIcon(getClass().getResource("icons/Picture2.png"));
        _itemDisabledIcon = new ImageIcon(getClass().getResource("icons/disabledIcon.gif"));
        _itemBusyIcon = new ImageIcon(getClass().getResource("icons/busyItem.gif"));
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cell = new JLabel();

        cell.setBackground(new Color(0,0,0,0));

        if(isAgent((String)value) && isItem((String)value))
        	cell.setIcon(_agentItem);
    	else if(isAgent((String)value))
            cell.setIcon(_agentIcon);
        else if(isSensor((String)value))
             cell.setIcon(_sensorIcon);
        else if(isItem((String)value)){
        	String val = (String)value;
        	if(val.contains("busy"))
        		cell.setIcon(_itemBusyIcon);
        	else if(val.contains("disabled"))
        		cell.setIcon(_itemDisabledIcon);
        	else
        		cell.setIcon(_itemIcon);
        }
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
//        if(value.contains("COORDINATOR") || value.contains("SECURITY_OFFICER") || value.contains("SECRETARY") || 
//        		value.contains("NETWORK_MANAGER") || value.contains("SOUND_MANAGER"))
    	if(value.contains("Agent"))
        	return true;
        else
        	return false;
    }

    private boolean isSensor(String value) {
        return value.contains("Sensor");
    }

    private boolean isItem(String value) {
//    	if(value.contains("BOARD") || value.contains("LAPTOP") || value.contains("MOUSE") || 
//        		value.contains("CABLE") || value.contains("SPEAKER") || value.contains("STAND") ||
//        		value.contains("LAZER_CURSOR"))
    	if(value.contains("Item"))
        	return true;
        else
        	return false;
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
