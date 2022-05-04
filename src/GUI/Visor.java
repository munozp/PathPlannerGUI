package GUI;

import PathPlanning.Map;
import java.util.ArrayList;
import javax.swing.JSlider;


/**
 *
 * @author Pablo Muñoz
 */
public class Visor extends javax.swing.JLayeredPane {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Visor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {           
            javax.swing.JFrame frame = new javax.swing.JFrame("PathPlanner");
            frame.setBounds(0, 0, 1440, 960);
            GUI.Visor guimap;
            guimap = new GUI.Visor(args);
            guimap.setSize(frame.getSize());
            frame.add(guimap);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        });
    }
    
    /**
     * Create a new visor with a single DTM
     * @param args DTM and cost file
     */
    public Visor(String args[]) {
        Map dtm;
        dtm = switch (args.length) {
            case 1 -> new Map(args[1], null, true);
            case 2 -> new Map(args[1], args[2], true);
            default -> new Map(9,9,9,1,true,true);
        };
        initComponents();
        init2(dtm, null, 10, "");
    }  
     
    /** 
     * After components initialization
     */
    private void init2(Map map, ArrayList path, int zoom, String data)
    {
        // Initial zoom
        if(zoom < sliderZoom.getMinimum() || zoom > sliderZoom.getMaximum())
            sliderZoom.setValue(sliderZoom.getMinimum());
        else
            sliderZoom.setValue(zoom); // Call event handler
        sliderZoom.setToolTipText("Zoom: "+sliderZoom.getValue());
        // Create the image and set the scroll
        scrollMap.setSize(this.getSize());
        scrollMap.setPreferredSize(this.getSize());
        lienzo = new Lienzo(map, path, zoom, this);
        lienzo.setBounds(0, 0, lienzo.getHeight(), lienzo.getWidth());
        scrollMap.setViewportView(lienzo);
        // Configuration panel
        searchConfig = new PanelConfig(map, this);
        paneCharts.add(searchConfig);
    }
        
    /** Change the panel size and updates the map size/scroll. */
    public boolean setSize(Object[] size)
    {
        if(size.length < 2)
            return false;
        int width = (Integer)size[0];
        int height = (Integer)size[1];
        this.setSize(width, height);
        this.setPreferredSize(new java.awt.Dimension(width-10, height-10));
        this.revalidate();
        return true;
    }
 
    /**
     * Update route from path planner
     * @param map current map
     * @param path route points
     * @param data route information
     */
    public void updateRoute(Map map, ArrayList path, String data)
    {
        if(path != null)
            labelPos.setText("Path found!");
        else
            labelPos.setText("NO PATH FOUND");
        lienzo.changePath(path);
        lienzo.repaint();
        routeInfo = data;
    }
    
    /**
     * Set the label with the position of the mouse
     * @param text 
     */
    public void setPositionText(String text)
    {
        labelPos.setText(text);
    }   
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sliderZoom = new javax.swing.JSlider();
        labelZoom = new javax.swing.JLabel();
        labelPos = new javax.swing.JLabel();
        buttonSave = new javax.swing.JButton();
        splitPanel = new javax.swing.JSplitPane();
        scrollMap = new javax.swing.JScrollPane();
        paneCharts = new javax.swing.JLayeredPane();

        setBackground(new java.awt.Color(204, 204, 204));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(1024, 768));
        setName("Map"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1280, 720));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        sliderZoom.setMajorTickSpacing(5);
        sliderZoom.setMaximum(50);
        sliderZoom.setMinimum(1);
        sliderZoom.setMinorTickSpacing(1);
        sliderZoom.setToolTipText("");
        sliderZoom.setValue(1);
        sliderZoom.setMinimumSize(new java.awt.Dimension(200, 24));
        sliderZoom.setPreferredSize(new java.awt.Dimension(200, 24));
        sliderZoom.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderZoomStateChanged(evt);
            }
        });

        labelZoom.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelZoom.setText("Zoom");

        labelPos.setFont(new java.awt.Font("Ubuntu Condensed", 1, 15)); // NOI18N
        labelPos.setToolTipText("Position");
        labelPos.setBorder(null);

        buttonSave.setText("Save image");
        buttonSave.setToolTipText("Save the full map with the current scale in a PNG picture.");
        buttonSave.setMaximumSize(new java.awt.Dimension(66, 29));
        buttonSave.setMinimumSize(new java.awt.Dimension(66, 29));
        buttonSave.setPreferredSize(new java.awt.Dimension(66, 24));
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });

        splitPanel.setDividerLocation(1050);
        splitPanel.setResizeWeight(1.0);
        splitPanel.setToolTipText("");
        splitPanel.setLastDividerLocation(1050);
        splitPanel.setMinimumSize(new java.awt.Dimension(10, 10));
        splitPanel.setName(""); // NOI18N
        splitPanel.setPreferredSize(new java.awt.Dimension(1200, 650));
        splitPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                splitPanelPropertyChange(evt);
            }
        });

        scrollMap.setViewportBorder(null);
        scrollMap.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        scrollMap.setPreferredSize(new java.awt.Dimension(800, 800));
        splitPanel.setLeftComponent(scrollMap);

        paneCharts.setMaximumSize(new java.awt.Dimension(600, 32767));
        paneCharts.setName(""); // NOI18N
        splitPanel.setRightComponent(paneCharts);

        setLayer(sliderZoom, javax.swing.JLayeredPane.DEFAULT_LAYER);
        setLayer(labelZoom, javax.swing.JLayeredPane.DEFAULT_LAYER);
        setLayer(labelPos, javax.swing.JLayeredPane.DEFAULT_LAYER);
        setLayer(buttonSave, javax.swing.JLayeredPane.DEFAULT_LAYER);
        setLayer(splitPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(labelZoom, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sliderZoom, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelPos, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buttonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(splitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1279, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelPos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(sliderZoom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelZoom, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(splitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void sliderZoomStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderZoomStateChanged
       JSlider js = (JSlider)evt.getSource();
       int zoom = js.getValue();
       js.setToolTipText("Zoom: "+zoom);
       if(lienzo != null)
       {
            lienzo.changeScale(zoom);
            refreshInterface();
       }
    }//GEN-LAST:event_sliderZoomStateChanged

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if(lienzo != null)
            refreshInterface();
    }//GEN-LAST:event_formComponentResized

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
        String filename = (String)javax.swing.JOptionPane.showInputDialog(this, "Input image filename:", "Save image",
                javax.swing.JOptionPane.QUESTION_MESSAGE, null, null, "image");
        if(filename != null)
            lienzo.savePng(filename, false);
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void splitPanelPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_splitPanelPropertyChange
        if(splitPanel.getWidth()>0 && splitPanel.getWidth()-splitPanel.getDividerLocation() < WS+splitPanel.getDividerSize())
            splitPanel.setDividerLocation(splitPanel.getWidth());
        refreshInterface();
    }//GEN-LAST:event_splitPanelPropertyChange
   
    /**
     * General update of the interface
     */
    public void refreshInterface()
    {
        if(lienzo == null)
            return;
        lienzo.changeScale(sliderZoom.getValue());
        lienzo.repaint();
        if(paneCharts.getComponentCount() <= 0)
            return;
        int HC = (splitPanel.getHeight()-HS) / 3;
        java.awt.Component c = paneCharts.getComponent(0);
        c.setBounds(1, 0, paneCharts.getWidth()-1, HS);
        for(int i=1; i<paneCharts.getComponentCount(); i++)
        {
            c = paneCharts.getComponent(i);
            c.setBounds(1, HS+HC*(i-1), paneCharts.getWidth()-1, HC);
        }
    }
    
    /**
     * Save the current view into a png image
     * @param filename output image file name
     */    
    public void savePng(String filename) {
        System.out.println("Generating png...");
        lienzo.savePng(filename, true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonSave;
    private javax.swing.JLabel labelPos;
    private javax.swing.JLabel labelZoom;
    private javax.swing.JLayeredPane paneCharts;
    private javax.swing.JScrollPane scrollMap;
    private javax.swing.JSlider sliderZoom;
    private javax.swing.JSplitPane splitPanel;
    // End of variables declaration//GEN-END:variables
    protected PanelConfig searchConfig;
    protected Lienzo lienzo;
    protected String routeInfo;
    /** Width for the configuration menu. */
    private static int WS = 270;
    /** Height for the configuration menu. */
    private static int HS = 290;

}
