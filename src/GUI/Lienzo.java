package GUI;

import PathPlanning.Map;
import PathPlanning.Node;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;


/**
 * Paint the map, route and robot position
 * @author Pablo Muñoz
 */
public class Lienzo extends javax.swing.JPanel
{
    /**
     * Create a new map visualization
     * @param dem DTM
     * @param route route to display
     * @param zoom zoom level
     * @param par parent component
     */
    public Lienzo(Map dem, ArrayList route, int zoom, Visor par)
    {
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        if(zoom > 0)
            scale = zoom;
        cols = dem.get_cols();
        rows = dem.get_rows();
        if(dem.get_isCornernode())
        {
            cols--; rows--;
        }
        cornernode(dem.get_isCornernode());
        map = dem;
        path = route;
        visu = 0;
        parent = par;
        robotXposition = -1;
        robotYposition = -1;
        robotOrientation = 0;
        dalt = map.max_alt()-map.min_alt();
        if(dalt == 0) dalt = 1;
        
        if(map.get_rows() > map.get_cols())
            scalemeter=map.get_cols()/20;
        else
            scalemeter=map.get_rows()/20;
        scalemeter = (int)((float)Math.round(scalemeter/RES_SCALE)*RES_SCALE / map.get_scale());
        if(scalemeter > MAX_SCALE)
            scalemeter = MAX_SCALE;
        else if(scalemeter < MIN_SCALE)
            scalemeter = MIN_SCALE;
        
        if(parent != null)
        {
            addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                @Override
                public void mouseMoved(java.awt.event.MouseEvent evt) {
                    formMouseMoved(evt);
                }
            });
            this.addMouseListener(new java.awt.event.MouseListener() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    formMouseClicked(evt);
                }
                @Override
                public void mousePressed(MouseEvent e) { }
                @Override
                public void mouseReleased(MouseEvent e) { }
                @Override
                public void mouseEntered(MouseEvent e) { }
                @Override
                public void mouseExited(MouseEvent e) { }
            });
        }
    }
     
    /**
     * Set the scale for the visualization
     * @param s scale between 1 and 100
     */
    public void changeScale(int s)
    {
        if(s > 0)
        {
            scale = s;
            cornernode(map.get_isCornernode());
            this.setSize(cols*scale+fonts*3, rows*scale+2);
            this.setPreferredSize(new java.awt.Dimension(this.getWidth(), this.getHeight()));
            this.revalidate();
        }
    }
    
    /**
     * Save the current view on a png image
     * @param imgname file name
     * @param originalSize set scale to 1 before saving
     */
    public void savePng(String imgname, boolean originalSize)
    {
        int previousscale = scale;
        if(originalSize)
            scale = 1;
        this.setSize(cols*scale+fonts*3, rows*scale+2);
        BufferedImage bImg = new BufferedImage(cols*scale+fonts*3, rows*scale+2, BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();
        paint(cg);
        try{
            ImageIO.write(bImg, "png", new java.io.File(imgname+".png"));
        }catch(java.io.IOException e) {
            System.out.println(e.toString());
        }
        scale = previousscale;
        this.setSize(cols*scale+fonts*3, rows*scale+2);
    }
    
    /**
     * Change the robot position. Computes orientation based on previous pos and new pos
     * @param x new X coordinate
     * @param y new Y coordinate
     */
    public void updateRobotPosition(int x, int y)
    {
        robotOrientation = Math.atan2(-(y-robotYposition), x-robotXposition);
        robotXposition = x;
        robotYposition = y;
        paint(this.getGraphics());
    }
    
    /**
     * Set the text with information of the node in which the mouse is now
     * @param evt mouse event
     */
    private void formMouseMoved(java.awt.event.MouseEvent evt) {
        int x = evt.getPoint().x / scale;
        int y = evt.getPoint().y / scale;
        String text = "";
        if(map.valid_pos(x, y))
            text = "("+x+", "+y+", "+map.get_node(x, y).getZ()+")["+map.get_tcost(x, y)+"]|"+map.get_node(x,y).A+"|<"+map.get_node(x,y).S+">";
        parent.setPositionText(text);
    }
    
    /**
     * On click, set the start position (left) or goal (right) 
     * @param evt mouse click event
     */
    private void formMouseClicked(java.awt.event.MouseEvent evt) {
        int x = evt.getPoint().x / scale;
        int y = evt.getPoint().y / scale;
        if(!map.valid_pos(x, y) || map.get_node(x, y) == null || map.get_node(x, y).isObstacle() || map.get_tcost(x, y) > Map.MAX_COST)
            return;
        if(evt.isMetaDown())
        {
            parent.searchConfig.setGoal(x,y);
            parent.setPositionText("Goal point set: ("+x+","+y+")");
        }
        else
        {
            parent.searchConfig.setStart(x,y);
            parent.setPositionText("Initial point set: ("+x+","+y+")");
        }
    }   
  
   /**
    * The component paint method
    * @param g 
    */ 
   @Override
   public void paint(Graphics g)
   {
       g.setColor(Color.WHITE);
       g.fillRect(0, 0, getWidth(), getHeight());
       // Show image map
       for(int i=0; i<cols; i++)
            for(int j=0; j<rows; j++)
                switch(visu)
                {
                    case ALTITUDE: // The altitude is always correspond to a node, while the cost depends on the corner-node desp
                    {
                        if(map.get_node(i, j).getZ() == Float.MIN_VALUE)
                            g.setColor(Color.GRAY);
                        else
                            g.setColor(Color.getHSBColor(H, S, (float)1.0-(map.get_node(i, j).getZ()-map.min_alt())/dalt));
                        g.fillRect(i*scale, j*scale, scale, scale);
                        break;
                    }
                    case COSTS:
                    {
                        g.setColor(Color.getHSBColor(H, S, (float)1.0-(map.get_tcost(i,j)/Map.MAX_COST)));
                        g.fillRect(i*scale, j*scale, scale, scale);
                        break;
                    }
                    case EXPANDED:
                    {
                        if(map.get_node(i, j).isExpanded())
                            g.setColor(Color.GREEN);
                        else
                            g.setColor(Color.RED);
                        g.fillRect(i*scale, j*scale, scale, scale);
                        break;
                    }
                    case SLOPE:
                    {
                        g.setColor(Color.getHSBColor(H, S, (float)1.0-(map.get_node(i,j).S/(float)map.get_maxslope())));
                        g.fillRect(i*scale, j*scale, scale, scale);
                        break;
                    }
                }

       g.setColor(Color.GRAY);
       for(int i=0; i<cols; i++)
            for(int j=0; j<rows; j++)
             if(map.get_tcost(i, j) > Map.MAX_COST)
                 //g.fillRect(i*scale+desp, j*scale+desp, scale+desp, scale+desp);
                 g.fillRect(i*scale, j*scale, scale, scale);
       
       // Show lines if scale is big enough
       if(scale > 3)
       {
           for(int i=0; i<=cols; i++)
               g.drawLine(i*scale, 0, i*scale, rows*scale);
           for(int j=0; j<=rows; j++)
               g.drawLine(0, j*scale, cols*scale, j*scale);
       }
       
       // Show path
       int sch = scale/4;
       int x0,y0,x1=0,y1=0;
       g.setColor(Color.RED);
       if(path != null)
       {
        for(int i = 0; i < path.size()-1; i++)
        {
           if(path.get(i).getClass() == Node.class) // Check type of list
           {
               x0 = ((Node)path.get(i)).getX();
               y0 = ((Node)path.get(i)).getY();
               x1 = ((Node)path.get(i+1)).getX();
               y1 = ((Node)path.get(i+1)).getY();
           }
           else
           {
               x0 = (int)((double[])path.get(i))[0];
               y0 = (int)((double[])path.get(i))[1];
               x1 = (int)((double[])path.get(i+1))[0];
               y1 = (int)((double[])path.get(i+1))[1];
           }
           g.drawLine(x0*scale+desp, y0*scale+desp, x1*scale+desp, y1*scale+desp);
           g.fillOval(x0*scale-(sch/2)+desp, y0*scale-(sch/2)+desp, sch, sch);
        } // Last dot
        g.setColor(Color.BLUE);
        g.fillOval(x1*scale-(sch/2)+desp, y1*scale-(sch/2)+desp, sch, sch);
       }
       
       g.setColor(Color.BLACK);
       String mintext = "0";
       String maxtext = "0";
       Color mincolor = Color.GREEN;
       Color maxcolor = Color.RED;
       switch(visu)
       {
           case ALTITUDE:
           {
               if(legend) g.drawString("DTM:",cols*scale+3, fonts*4-2);
               mincolor = Color.getHSBColor(H, S, (float)1.0-(map.max_alt()-map.min_alt())/dalt);
               maxcolor = Color.getHSBColor(H, S, (float)1.0-(map.min_alt()-map.min_alt())/dalt);
               mintext = Float.toString(map.max_alt()/map.get_scale());
               maxtext = Float.toString(map.min_alt()/map.get_scale());
               break;
           }
           case COSTS:
           {
               if(legend) g.drawString("Costs:",cols*scale+3, fonts*4-2);
               mincolor = Color.getHSBColor(H, S, (float)1.0);
               maxcolor = Color.getHSBColor(H, S, (float)(1/Map.MAX_COST));
               mintext = "1.0";
               maxtext = Float.toString(Map.MAX_COST);
               break;
           }
           case EXPANDED:
           {
               if(legend) g.drawString("Expanded:",cols*scale+3, fonts*4-2);
               mintext = "Yes";
               maxtext = "No";
               break;
           }
           case SLOPE:
           {
               if(legend) g.drawString("Slope:",cols*scale+3, fonts*4-2);
               mincolor = Color.getHSBColor(H, S, (float)1.0);
               maxcolor = Color.getHSBColor(H, S, (float)(1/map.get_maxslope()));
               mintext = "0º";
               maxtext = Float.toString((float)map.get_maxslope())+"º";
               break;
           }
       }
       if(legend)
       {
           // Show axis
           g.setColor(Color.BLACK);
           g.drawString(" \u2192 X",cols*scale+3, fonts+1);
           g.drawString("\u2193 Y",cols*scale+3, fonts*2);
           // Texts and squares
           g.setColor(mincolor);
           g.fillRect(cols*scale+2, fonts*4, fonts, fonts);
           g.drawString(mintext,cols*scale+3, fonts*6-2);
           g.setColor(maxcolor);
           g.fillRect(cols*scale+2, fonts*6, fonts, fonts);
           g.drawString(maxtext,cols*scale+2, fonts*8-2);
           // SCALE
           g.setColor(Color.WHITE);
           g.fillRect(cols*scale-scalemeter*scale, 10, scalemeter*scale, fonts+2);
           g.setColor(Color.BLACK);
           mintext = scalemeter+" m";
           g.drawString(mintext,cols*scale-scalemeter*scale, fonts+fonts/2);
       }

       // Robot position
       if(robotXposition >= 0 && robotXposition < map.get_cols() && robotYposition >= 0 && robotYposition < map.get_rows())
       {
           g.setColor(Color.BLUE);
           if(scale > 3)
               ((java.awt.Graphics2D)g).setStroke(new java.awt.BasicStroke(3.0f));
           else
               ((java.awt.Graphics2D)g).setStroke(new java.awt.BasicStroke(1.0f));
           double sin = Math.sin(robotOrientation);
           double cos = Math.cos(robotOrientation);
           int rx = robotXposition*scale;
           int ry = robotYposition*scale;
           double tx1 = -scale/3;
           double tx2 = -scale/3;
           double tx3 = 2*scale/3;
           double ty1 = -scale/3;
           double ty2 = +scale/3;
           double ty3 = 0;
           int[] vx = new int[]{rx+(int)(tx1*cos+ty1*sin), rx+(int)(tx2*cos+ty2*sin), rx+(int)(tx3*cos+ty3*sin)};
           int[] vy = new int[]{ry+(int)(-tx1*sin+ty1*cos), ry+(int)(-tx2*sin+ty2*cos), ry+(int)(-tx3*sin+ty3*cos) };           
           g.fillPolygon(vx, vy, 3);
       }
   }

   /**
    * Set the visualization between corner and center node representation
    * @param corner true for corner-node 
    */
   public final void cornernode(boolean corner)
   {
       if(!corner)
           desp = scale / 2;
       else
           desp = 0;
   }
   
   /**
    * Set a new route
    * @param newpath the new route to display
    */
   public void changePath(ArrayList newpath)
   {
       path = newpath;
   }
   
   /**
    * Enable or disable legend (altitude/cost and size)
    * @param enablelegend true to enable legend
    */
   public void enableLegend(boolean enablelegend)
   {
       legend = enablelegend;
   }
   
   
   // Class variables
   private int cols = 10;
   private int rows = 10;
   private int scale = 1;
   private int scalemeter = 0;
   private int fonts = 15;
   private int desp = 0;
   private boolean legend = true;
   private int visu = ALTITUDE;
   float dalt;
   public Map map;
   private ArrayList path;
   private Visor parent;
   
   // Robot position
   private int robotXposition;
   private int robotYposition;
   private double robotOrientation;
   
   private static final float H = (float)0.1;
   private static final float S = (float)1.0;
   private static final int MIN_SCALE = 1;
   private static final int MAX_SCALE = 500;
   private static final int RES_SCALE = 50;
   private static final int ALTITUDE = 0;
   private static final int COSTS = 1;
   private static final int EXPANDED = 2;
   private static final int SLOPE = 3;
}