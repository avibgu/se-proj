/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import actor.Entity;
import configuration.ConfigurationManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import simulator.Domain;
import simulator.Location;

/**
 *
 * @author Shai Cantor
 */
public class World extends javax.swing.JFrame implements Observer{

    private static final long serialVersionUID = 7902455422644793244L;
	private Controller _controller;
    private Domain _domain;
    private Vector<Vector<Entity>> _entities;
    /**
     * Creates new form World
     */
    public World(Controller controller) {
        _controller = controller;
        _controller.getDomain().getObservers().add(this);
        _domain = controller.getDomain();
        _entities = _domain.getEntities();
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        domainTable = new javax.swing.JTable();
        stopButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        messageArea = new javax.swing.JTextArea();
        RestartButton = new javax.swing.JButton();
        stopLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        domainTable.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        domainTable.setRowHeight(domainTable.getRowHeight() * 2);
        domainTable.setDefaultRenderer(String.class, new MultiLineCellRenderer(_domain));
        String [][] locations = new String[_domain.getDomainSize()][_domain.getDomainSize()];
        for(int i = 0; i < locations.length; i++){
            for(int j = 0; j < locations.length; j++){
                locations[i][j] = "";
            }
        }
        for(int entityIndex = 0; entityIndex < _entities.size(); entityIndex++)
        for(Entity entity : _entities.elementAt(entityIndex)){
            Location l = entity.getLocation();
            int latitude = l.getLatitude();
            int longitude = l.getLongitude();
            locations[latitude][longitude] = entity.toString();
        }
        String[] names = new String[]{"0", "1", "2", "3", "4", "5", "6"};
        DefaultTableModel model = new DefaultTableModel(locations, names){
			private static final long serialVersionUID = 1297866531965024542L;

			@Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }

            @SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int columnIndex) {
                return String.class;
            }
        };
        domainTable.setModel(model
        );
        domainTable.setAlignmentX(2.0F);
        domainTable.setAlignmentY(1.0F);
        domainTable.setFocusable(false);
        domainTable.setIntercellSpacing(new java.awt.Dimension(3, 3));
        domainTable.setRowHeight(70);
        jScrollPane1.setViewportView(domainTable);

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        messageArea.setColumns(20);
        messageArea.setEditable(false);
        messageArea.setFont(new java.awt.Font("Calibri", 0, 14));
        messageArea.setRows(5);
        messageArea.setText("Messages\n");
        jScrollPane2.setViewportView(messageArea);

        RestartButton.setText("Restart");
        RestartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RestartButtonActionPerformed(evt);
            }
        });

        stopLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
        stopLabel.setText("System Stopped");
        stopLabel.setVisible(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setText("Items are shown as if they are moved only if they are close to one of the sensors(length <= 2)");

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stopLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(RestartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 869, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stopLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RestartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        _controller.start();
    }//GEN-LAST:event_startButtonActionPerformed

    private void RestartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RestartButtonActionPerformed
        _controller.stop();
        ConfigurationManager config = new ConfigurationManager();
        Domain domain = config.loadParameters();
        Controller controller = new Controller(domain);
        new World(controller).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_RestartButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        _controller.stop();
        stopLabel.setVisible(true);
    }//GEN-LAST:event_stopButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(World.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(World.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(World.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(World.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        ConfigurationManager config = new ConfigurationManager();
        Domain domain = config.loadParameters();
        final Controller controller = new Controller(domain);
        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new World(controller).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton RestartButton;
    private javax.swing.JTable domainTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea messageArea;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JLabel stopLabel;
    // End of variables declaration//GEN-END:variables

    @SuppressWarnings("unchecked")
	@Override
    public void update(Observable entity, Object visibleItems) {
        if(entity == null && visibleItems != null){//itemScanner has executed this method
            //update table for each visible item
        	for (Entity e : (Vector<Entity>) visibleItems) {
				updateSignalEntity(e);
			}
        }
        if(entity != null && visibleItems == null){//locationMonitor has executed this method
            //update table for the agent's new location
        	updateSignalEntity((Entity)entity);
        }
    }
    
    public synchronized void updateSignalEntity(Entity entity){
        //Location oldLocation = entity.getOldLocation();
        Location oldLocation = entity.getLastRepLocation();
        Location newLocation = entity.getLocation();
        if(!newLocation.equals(oldLocation) && oldLocation != null && !entity.updated())
        	changeCellRepresentation(oldLocation, newLocation, entity);
    }
    
    public void changeCellRepresentation(Location oldLocation, Location newLocation, Entity entity){
    	String oldLocationRep = domainTable.getModel().getValueAt(oldLocation.getLatitude(), oldLocation.getLongitude()).toString();
        String newLocationRep = domainTable.getModel().getValueAt(newLocation.getLatitude(), newLocation.getLongitude()).toString();
    	
        if(newLocationRep.equals(""))
    		domainTable.getModel().setValueAt(entity.toString(), newLocation.getLatitude(), newLocation.getLongitude());
    	else{
    		newLocationRep = newLocationRep.concat("\n" + entity.toString());
    		domainTable.getModel().setValueAt(newLocationRep, newLocation.getLatitude(), newLocation.getLongitude());
    	}
    	if(oldLocation != null){
	    	String[] entitiesRep = oldLocationRep.split("\n");
	    	if(entitiesRep.length == 1){
	    		domainTable.getModel().setValueAt("", oldLocation.getLatitude(), oldLocation.getLongitude());
	    	}
	    	else{
	    		String newRep = "";
	    		for (int i = 0; i < entitiesRep.length - 1; i++) {
					if(!entitiesRep[i].equals(entity.toString()))
						newRep = newRep.concat(entitiesRep[i] + "\n");
				}
	    		if(!entitiesRep[entitiesRep.length - 1].equals(entity.toString()))
	    			newRep = newRep.concat(entitiesRep[entitiesRep.length - 1]);
	    		else{
	    			newRep = newRep.substring(0, newRep.length() - 1);
	    		}
	    			
	    		domainTable.getModel().setValueAt(newRep, oldLocation.getLatitude(), oldLocation.getLongitude());
	    	}
            String first = entity.toString() + " moved from (" + oldLocation.getLatitude() + "," + oldLocation.getLongitude() + ") to (";
            String second = newLocation.getLatitude() + "," + newLocation.getLongitude() + ")";
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String third = "at " + dateFormat.format(date);
            messageArea.append(first + second + " " + third + '\n');
    	}
    	entity.update();
    }
}