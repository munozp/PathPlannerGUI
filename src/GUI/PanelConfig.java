package GUI;

import PathPlanning.*;
import Control.ConnectionThread;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.text.ParseException;
import javax.swing.JButton;


/**
 * How to change the map. 
 * @warning Do it after initialization!
 * 
 * Generate a new map with random values for Z and transversal costs. 
 * Map constructor interface
 *   ncols number of columns of the DEM.
 *   nrows number of rows of the DEM.
 *   maxz maximum Z value for nodes. 0 for plain terrain.
 *   maxc maximum traversal cost. 0 for zero uniform cost terrain.
 *   dmode mode for transversal cost grid. cornernode mode is used for D* and similar ones.
 *   lin altitude interpolation method of the terrain. True for lineal, false for quadratic.
   map = new Map(30, 20, 0, 1, false, true);
 * Change cost of a cell
   map.cost[1][1] = Map.MAX_COST;
 * Change altitude of a node. Greater or equal to 0. If Z < 0, then is an obstacle.
   map.get_node(1, 1).setZ(5);
 * After map generation
   parent.lienzo.changeMap(map);
 * Use Lienzo.COST to visualize costmap or Lienzo.ALTITUDE for DEM.
   parent.lienzo.visu = Lienzo.COSTS;
   parent.lienzo.repaint();
 */


/**
 *
 * @author Pablo Muñoz
 */
public class PanelConfig extends javax.swing.JPanel {

    /**
     * Creates new form SearchConfig
     */
    public PanelConfig() {
        initComponents();
    }
    
    /**
     * Constructor for the configuration panel
     * @param dem reference to the DTM used
     * @param p the parent component
     */
    public PanelConfig(Map dem, Visor p)
    {
        initComponents();
        map = dem;
        parent = p;
        fieldSlope.setText(String.valueOf(map.ZM));
        commitFloatField(fieldSlope);
        fieldRobotIPFocusLost(null);
    }
    
    /**
     * Handles a message received through the robot connection
     * @param message the received message
     */
    public void handleReceivedData(String message) {
        if(message == null || message.isEmpty())
            return;
        // TODO PARSE MESSAGE AND PERFORM OPERATIONS 
        System.out.println(message);
        // Example of updating robot position with a message "POS,x,y"
        // Receiving a map after MAP,ROWS,COLS
        // Each row is received as ROW ROW# c c ...
        // Map reception end when received ENDMAP
        try{
            java.util.StringTokenizer strtok = new java.util.StringTokenizer(message, ",");
            String orden = strtok.nextToken();
            if(orden.startsWith("POS"))
            {
                int x = Integer.parseInt(strtok.nextToken());
                int y = Integer.parseInt(strtok.nextToken());
                updateRobotPosition(x,y);
            }
            else if(orden.startsWith("MAP"))
            {
                int rows = Integer.parseInt(strtok.nextToken());
                int cols = Integer.parseInt(strtok.nextToken());
                map = new Map(cols, rows, 0, 8, false, true);
                System.out.println(map.toString());
            }
            else if(orden.startsWith("ROW"))
            {
                int row = Integer.parseInt(strtok.nextToken());
                java.util.StringTokenizer strtokrow = new java.util.StringTokenizer(strtok.nextToken(), " ");
                for(int col=0; col<map.get_cols(); col++)
                    map.cost[col][row] = Integer.parseInt(strtokrow.nextToken());
            }
            else if(orden.startsWith("ENDMAP"))
            {
                map.expand_costmap((short)1);
                parent.lienzo.changeMap(map);
                parent.lienzo.visu = Lienzo.COSTS;
                parent.lienzo.repaint();
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    /**
     * Set the new robot position
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void updateRobotPosition(int x, int y)
    {
        parent.lienzo.updateRobotPosition(x, y); 
    }
    
    /**
     * Set initial position
     * @param x
     * @param y 
     */
    public void setStart(int x, int y)
    {
        parent.setEnabled(false);
        if(x > 0 && x <= map.get_cols())
            fieldStartX.setValue(x);
        if(y > 0 && y <= map.get_rows())
            fieldStartY.setValue(y);
    }
    
    /**
     * Set goal position
     * @param x
     * @param y 
     */
    public void setGoal(int x, int y)
    {
        if(x > 0 && x <= map.get_cols())
            fieldGoalX.setValue(x);
        if(y > 0 && y <= map.get_rows())
            fieldGoalY.setValue(y);
    }
    
    /**
     * Search a new route
     * @param algorithm reference to the algorithm used
     */
    public void startSearch(PathPlanning.SearchMethod algorithm)
    {
        parent.setPositionText("Searching path...");
        map.restart_map(false);
        algorithm.search();
        if(algorithm.get_path() != null)
        {
            parent.updateRoute(map, algorithm.get_path(), algorithm.toString()+"\n"+algorithm.path_info());
            parent.setPositionText("Path found");
        }
        else
        {
            parent.setPositionText("No path found");
        }
    }
        
    /**
     * Compute the maximum slope allowed from the text fields
     */
    public void computeMaxSlope()
    {
        float maxslope = Float.parseFloat(fieldSlope.getText());
        if(maxslope > 0 && map.ZM != maxslope)
        {
            map.ZM = maxslope;
            map.force_compute_slopemap();
        }
        map.compute_slopemap();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelStart = new javax.swing.JLabel();
        labelSepStart = new javax.swing.JLabel();
        labelGoal = new javax.swing.JLabel();
        labelSepGoal = new javax.swing.JLabel();
        labelSlope = new javax.swing.JLabel();
        labelSlopeUnit = new javax.swing.JLabel();
        buttonSearch = new javax.swing.JButton();
        fieldStartX = new javax.swing.JFormattedTextField();
        fieldGoalX = new javax.swing.JFormattedTextField();
        fieldStartY = new javax.swing.JFormattedTextField();
        fieldGoalY = new javax.swing.JFormattedTextField();
        fieldSlope = new javax.swing.JFormattedTextField();
        fieldSlopeP = new javax.swing.JFormattedTextField();
        labelSlopePUnit = new javax.swing.JLabel();
        buttonExecute = new javax.swing.JButton();
        buttonRotate = new javax.swing.JButton();
        labelRotate = new javax.swing.JLabel();
        fieldRotate = new javax.swing.JFormattedTextField();
        labelRotateUnit = new javax.swing.JLabel();
        labelForward = new javax.swing.JLabel();
        fieldForward = new javax.swing.JFormattedTextField();
        labelForwardUnit = new javax.swing.JLabel();
        buttonForward = new javax.swing.JButton();
        buttonInfo = new javax.swing.JButton();
        labelStart1 = new javax.swing.JLabel();
        fieldRobotIP = new javax.swing.JTextField();
        buttonConnect = new javax.swing.JToggleButton();

        labelStart.setText("Start position:");
        labelStart.setToolTipText("Start position (x , y).");

        labelSepStart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelSepStart.setText(",");

        labelGoal.setText("Goal position:");
        labelGoal.setToolTipText("Goal position (x , y).");

        labelSepGoal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelSepGoal.setText(",");

        labelSlope.setText("Maximum slope:");
        labelSlope.setToolTipText("Maximum terrain slope allowed. Use -1 to disable slope consideration during search.");

        labelSlopeUnit.setText("º");

        buttonSearch.setText("SEARCH ROUTE");
        buttonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSearchActionPerformed(evt);
            }
        });

        fieldStartX.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        fieldGoalX.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        fieldStartY.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        fieldGoalY.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        fieldSlope.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.000"))));
        fieldSlope.setText("-1");
        fieldSlope.setToolTipText("Use a negative value to invalidate constraint");
        fieldSlope.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldSlopeFocusLost(evt);
            }
        });

        fieldSlopeP.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.000"))));
        fieldSlopeP.setToolTipText("Use a negative value to invalidate constraint");
        fieldSlopeP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldSlopePFocusLost(evt);
            }
        });

        labelSlopePUnit.setText("%");

        buttonExecute.setText("EXECUTE ROUTE");
        buttonExecute.setEnabled(false);
        buttonExecute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExecuteActionPerformed(evt);
            }
        });

        buttonRotate.setText("Rotate");
        buttonRotate.setEnabled(false);
        buttonRotate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRotateActionPerformed(evt);
            }
        });

        labelRotate.setText("Rotate:");
        labelRotate.setToolTipText("Maximum terrain slope allowed. Use -1 to disable slope consideration during search.");

        fieldRotate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.000"))));
        fieldRotate.setText("0");
        fieldRotate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldRotateFocusLost(evt);
            }
        });

        labelRotateUnit.setText("º");

        labelForward.setText("Move forward:");
        labelForward.setToolTipText("Maximum terrain slope allowed. Use -1 to disable slope consideration during search.");

        fieldForward.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.000"))));
        fieldForward.setText("0");
        fieldForward.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldForwardFocusLost(evt);
            }
        });

        labelForwardUnit.setText("m");

        buttonForward.setText("Move forward");
        buttonForward.setEnabled(false);
        buttonForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonForwardActionPerformed(evt);
            }
        });

        buttonInfo.setText("ROUTE INFO");
        buttonInfo.setEnabled(false);
        buttonInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonInfoActionPerformed(evt);
            }
        });

        labelStart1.setText("Robot IP:port");
        labelStart1.setToolTipText("Start position (x , y).");

        fieldRobotIP.setText("127.0.0.1:4444");
        fieldRobotIP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldRobotIPFocusLost(evt);
            }
        });

        buttonConnect.setText("CONNECT");
        buttonConnect.setEnabled(false);
        buttonConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonConnectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addContainerGap(8, Short.MAX_VALUE)
                                    .addComponent(labelSlope, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(labelGoal, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(labelStart, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(fieldStartX, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldGoalX, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelSepGoal, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelSepStart, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fieldGoalY, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldStartY, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(fieldSlope, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(labelSlopeUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(fieldSlopeP, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(labelSlopePUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(labelForward, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fieldForward, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelForwardUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonForward, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(labelRotate, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fieldRotate, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelRotateUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonRotate, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(buttonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(buttonExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(buttonInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelStart1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldRobotIP, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonConnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelStart1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldRobotIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonConnect))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelStart, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSepStart, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldStartX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldStartY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelGoal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSepGoal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldGoalX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldGoalY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSlope, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSlopeUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldSlope, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldSlopeP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSlopePUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonInfo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonExecute))
                    .addComponent(buttonSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRotate, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldRotate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRotateUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonRotate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelForward, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldForward, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelForwardUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonForward))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSearchActionPerformed
        int xs, ys, xg, yg;
        if(fieldStartX.getText().isEmpty() || fieldStartY.getText().isEmpty() || fieldGoalX.getText().isEmpty() || fieldGoalY.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please, provide the coordinates.", "Invalid coordinates", JOptionPane.ERROR_MESSAGE);
            return;
        }
        commitFloatField(fieldStartX); xs = Integer.parseInt(fieldStartX.getText());
        commitFloatField(fieldStartY); ys = Integer.parseInt(fieldStartY.getText());
        commitFloatField(fieldGoalX);  xg = Integer.parseInt(fieldGoalX.getText());
        commitFloatField(fieldGoalY);  yg = Integer.parseInt(fieldGoalY.getText());
        if(!map.valid_pos(xs, ys) || !map.valid_pos(xg, yg))
        {
            JOptionPane.showMessageDialog(this, "X coordinates shall be in [0, "+(map.get_cols()-1)+"]\nY coordinates shall be in [0, "+(map.get_rows()-1)+"]", "Invalid coordinates", JOptionPane.ERROR_MESSAGE);
            return;
        }
        commitFloatField(fieldSlope);
        float maxslope = Float.parseFloat(fieldSlope.getText());
        // TODO Modify params to configure search:
        // Last three params:
        /** Heuristics definitions. 
         * H_EUCLIDEAN  
         * H_EUCLIDEAN_Z
         * H_MANHATTAN
         * H_OCTILE
         * H_OCTILE_Z
         * H_ALPHA
         * H_ALPHA2
        */
        // withz true for use Z values.
        // withc true for use transversal costs.
        pathplanner = new Dana(map, map.get_node(xs, ys), map.get_node(xg, yg), Heuristics.H_EUCLIDEAN_Z, false, true);
        map.ZM = maxslope;
        startSearch(pathplanner);
        setEnableExecute();
    }//GEN-LAST:event_buttonSearchActionPerformed

    private void fieldSlopeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldSlopeFocusLost
        if(fieldSlope.getText().isEmpty())
            return;
        commitFloatField(fieldSlope);
        fieldSlopeP.setValue(Geometry.slopePercent(Float.parseFloat(fieldSlope.getText())));
    }//GEN-LAST:event_fieldSlopeFocusLost

    private void fieldSlopePFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldSlopePFocusLost
        if(fieldSlopeP.getText().isEmpty())
            return;
        commitFloatField(fieldSlopeP);
        fieldSlope.setValue(Geometry.slopeAngle(Float.parseFloat(fieldSlopeP.getText())));
    }//GEN-LAST:event_fieldSlopePFocusLost

    private void fieldRotateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldRotateFocusLost
        if(fieldRotate.getText().isEmpty())
            return;
        commitFloatField(fieldRotate);
        Double rotation = Double.parseDouble(fieldRotate.getText());
        if(rotation < -180 || rotation > 180)
        {
            JOptionPane.showMessageDialog(this, "Rotation must be in [-180º,180º]", "Invalid rotation", JOptionPane.ERROR_MESSAGE);
            fieldRotate.setText("0");
        }
        setEnableButton(buttonRotate, fieldRotate.getText());
    }//GEN-LAST:event_fieldRotateFocusLost

    private void fieldForwardFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldForwardFocusLost
        if(fieldForward.getText().isEmpty())
            return;
        commitFloatField(fieldForward);
        Double distance = Double.parseDouble(fieldForward.getText());
        if(distance < -10 || distance > 10)
        {
            JOptionPane.showMessageDialog(this, "Distance must be in [-10,10]m", "Invalid distance", JOptionPane.ERROR_MESSAGE);
            fieldForward.setText("0");
        }
        setEnableButton(buttonForward, fieldForward.getText());
    }//GEN-LAST:event_fieldForwardFocusLost

    private void buttonInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonInfoActionPerformed
        JOptionPane.showMessageDialog(parent, parent.routeInfo, "Route info", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_buttonInfoActionPerformed

    private void buttonRotateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRotateActionPerformed
        // TODO EXECUTE ROTATION (send command to robot)
        System.out.println("EXECUTE ROTATION OF "+fieldRotate.getText()+"º");
        // Example of a command
        robot.send("ROTATE:"+String.valueOf(fieldRotate.getText()), true);
    }//GEN-LAST:event_buttonRotateActionPerformed

    private void buttonForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonForwardActionPerformed
        // TODO EXECUTE FORWARD (send command to robot)
        System.out.println("EXECUTE FORWARD OF "+fieldRotate.getText()+"dm");
        // Example of a command
        robot.send("FORWARD:"+String.valueOf(fieldForward.getText()), true);
    }//GEN-LAST:event_buttonForwardActionPerformed

    private void fieldRobotIPFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldRobotIPFocusLost
        String ip = fieldRobotIP.getText();
        if(ip.isEmpty())
            return;
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        int colon = ip.indexOf(':');
        boolean correct;
        String addr = ip.substring(0, colon);
        String port = ip.substring(colon+1, ip.length());
        int portn = 0;
        try{
            portn = Integer.valueOf(port);
        }catch(NumberFormatException e){
            portn = -1;
        }
        correct = colon > 6 && portn > 0 && portn < 65355 && addr.matches(PATTERN);
        buttonConnect.setEnabled(correct);
        if(!buttonConnect.isEnabled())
            JOptionPane.showMessageDialog(parent, "Malformed robot IP or wrong port number", "Invalid IP", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_fieldRobotIPFocusLost

    private void buttonConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonConnectActionPerformed
        // CONNECT TO THE ROBOT
        if(buttonConnect.isSelected())
        {
            robot = new ConnectionThread(this);
            boolean connected = robot.connect(fieldRobotIP.getText());
            if(!connected)
            {
                JOptionPane.showMessageDialog(parent, "Failed to connect with the robot", "Error", JOptionPane.ERROR_MESSAGE);
                buttonConnect.setSelected(false);
                robot = null;
                return;
            }
            robot.start();
            // When connected, change button text
            buttonConnect.setText("DISSCONNECT");
            setEnableExecute();
        }
        else
        {
            // DISCONNECT THE ROBOT (CLOSE CONNECTION)
            if(robot != null)
            {
                robot.endConnection();
                try {
                    robot.join();
                } catch (InterruptedException ex) { }
                robot = null;
            }
            // When dissconnected, change button text
            buttonConnect.setText("CONNECT");
            setEnableExecute();
        }
    }//GEN-LAST:event_buttonConnectActionPerformed

    private void buttonExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExecuteActionPerformed
        // TODO EXECUTE THE ROUTE
        ArrayList<Node> route = pathplanner.get_path();
        
        // TODO IMPLEMENT REAL FUNCTIONALITY
        // Example of sending route to the robot
        for(int i=0; i<route.size(); i++)
        {
            robot.send("NODE:"+String.valueOf(route.get(i).getX())+","+route.get(i).getY(), true);
            try {
                Thread.sleep(2000);
            }catch(InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
    }//GEN-LAST:event_buttonExecuteActionPerformed
      
    private void setEnableButton(JButton button, String val) {
        button.setEnabled(buttonConnect.isSelected() && (val == null || Double.parseDouble(val) != 0));
    }
    
    private void setEnableExecute() {
        boolean pathfound = false;
        if(pathplanner != null)
            pathfound = !pathplanner.get_path().isEmpty();
        buttonInfo.setEnabled(pathfound);
        buttonExecute.setEnabled(pathfound && buttonConnect.isSelected());
        fieldForwardFocusLost(null);
        fieldRotateFocusLost(null);
    }
    
    private void commitFloatField(javax.swing.JFormattedTextField f)
    {
        try{
            f.setText(f.getText().replace(',', '.'));
            f.commitEdit();
        }catch(ParseException ex){ }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton buttonConnect;
    private javax.swing.JButton buttonExecute;
    private javax.swing.JButton buttonForward;
    private javax.swing.JButton buttonInfo;
    private javax.swing.JButton buttonRotate;
    private javax.swing.JButton buttonSearch;
    private javax.swing.JFormattedTextField fieldForward;
    private javax.swing.JFormattedTextField fieldGoalX;
    private javax.swing.JFormattedTextField fieldGoalY;
    private javax.swing.JTextField fieldRobotIP;
    private javax.swing.JFormattedTextField fieldRotate;
    private javax.swing.JFormattedTextField fieldSlope;
    private javax.swing.JFormattedTextField fieldSlopeP;
    private javax.swing.JFormattedTextField fieldStartX;
    private javax.swing.JFormattedTextField fieldStartY;
    private javax.swing.JLabel labelForward;
    private javax.swing.JLabel labelForwardUnit;
    private javax.swing.JLabel labelGoal;
    private javax.swing.JLabel labelRotate;
    private javax.swing.JLabel labelRotateUnit;
    private javax.swing.JLabel labelSepGoal;
    private javax.swing.JLabel labelSepStart;
    private javax.swing.JLabel labelSlope;
    private javax.swing.JLabel labelSlopePUnit;
    private javax.swing.JLabel labelSlopeUnit;
    private javax.swing.JLabel labelStart;
    private javax.swing.JLabel labelStart1;
    // End of variables declaration//GEN-END:variables
    private Visor parent;
    protected SearchMethod pathplanner;
    private Map map;
    private ConnectionThread robot;
    
}
