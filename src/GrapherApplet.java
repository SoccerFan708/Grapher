import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.text.DecimalFormat;

public class GrapherApplet extends Applet implements ActionListener, KeyListener{
   private int CANVAS_WIDTH;
   private int CANVAS_HEIGHT;
   private  int GRAPH_WIDTH;
   private  int GRAPH_HEIGHT;
   private int W_GRIDS;
   private int H_GRIDS;
   private int left; private int up;
   private int right;private int bottom;
   int yGridUnit;int xGridUnit;
   double Xmin; double Xmax;
   double Ymin; double Ymax;
   String minY; String maxY;
   String minX; String maxX;
   String origin;
   double[][] DataPoints = {{-10, 100}, {-9, 81}, {-5, 25}, {-4, 16}, {0, 0}, {4, 16}, {5, 25}, {5.9, 34.81}, {10, 100}};
   private int[][][] Coordinates;
   private Color[] DataColors = {Color.blue, Color.red, Color.green};
   int Yaxis; int Xaxis;
   int imaginaryYaxis; int imaginaryXaxis;
   double yP; double xP;
   boolean displayPoints;
   boolean displayLabels;
   DecimalFormat df;
   int displacement;  
	
   BufferedImage Canvas;
 
   public void SET_DEFAULTS(){
      CANVAS_WIDTH = CANVAS_HEIGHT = 450;
      GRAPH_WIDTH = GRAPH_HEIGHT = 400;
      
      Xmin = 0; //-10;
      Xmax = 10;
      Ymin = 0;//-10;
      Ymax = 10;
      double yUnit = Ymax-Ymin;
      double xUnit = Xmax-Xmin;
      standardize(xUnit, yUnit);  
   
      left = (CANVAS_WIDTH-GRAPH_WIDTH)/2;
      up = (CANVAS_HEIGHT-GRAPH_HEIGHT)/2;  
      if(Math.abs(Xmin) == Math.abs(Xmax)){
         Yaxis = GRAPH_WIDTH/2;
      }
      else{
         Yaxis = (int)(-((Xmin/xP)*xGridUnit));  
      }
      if(Math.abs(Ymin) == Math.abs(Ymax)){
         Xaxis = GRAPH_HEIGHT/2;
      }
      else{
         Xaxis = (int)((((Ymax*yGridUnit))/yP));
      }
      getImaginaryAxes();
      displayPoints = true;
      displayLabels = true;
      plotGraph(DataPoints);
      
      df = new DecimalFormat("0.000");
      df.setMinimumFractionDigits(3);
      df.setMinimumIntegerDigits(1);
   
      maxX = df.format(Xmax);
      minX = df.format(Xmin);
      maxY = df.format(Ymax);
      minY = df.format(Ymin);
      origin = "0";
      displacement = 5;
   }
   
   public void standardize(double xUnit, double yUnit){
      if(yUnit<10 || yUnit>40){
         double temp = getPowerOfTen(yUnit);
         double temp2 = yUnit/temp;
         for(int i=10;i>=1;i--){
            int remainder = 10%i;
            if(remainder == 0 && ((temp2*i) >=10 && (temp2*i)<=40)){
               System.out.println("_initialyUnit: "+yUnit);
               yUnit = temp2*i;
               System.out.println(temp2*i+"::_powerOfTen: "+temp+"_i: "+i);
               yP = temp/i;
               break;
            }
         }
      }
      else{
         yP = 1;
      }
      
      yGridUnit = (int)(GRAPH_HEIGHT/yUnit);
      H_GRIDS = GRAPH_HEIGHT/yGridUnit;
      if(H_GRIDS*yGridUnit<GRAPH_HEIGHT){
         H_GRIDS += 1;
      }
      	
      if(xUnit<10 || xUnit>40){
         double temp = getPowerOfTen(xUnit);
         double temp2 = xUnit/temp;
         for(int i=10;i>=1;i--){
            int remainder = 10%i;
            if(remainder == 0 && ((temp2*i) >=10 && (temp2*i)<=40)){
               System.out.println("_initialxUnit: "+xUnit);
               xUnit = temp2*i;
               System.out.println(temp2*i+"::_powerOfTen: "+temp+"_i: "+i);
               xP = temp/i;
               break;
            }
         }
      }
      else{
         xP = 1;
      }
      	
      xGridUnit = (int)(GRAPH_WIDTH/xUnit);
      W_GRIDS = GRAPH_WIDTH/xGridUnit;
      if(W_GRIDS*xGridUnit<GRAPH_WIDTH){
         W_GRIDS += 1;
      }    
      
   }
   
   public double getPowerOfTen(double Unit){
      double P = 0;
      //System.out.println("p0: "+(Math.pow(5, 2)));
      for(int d=-10;d<10;d++){
         //System.out.println("p1: "+P);
         double temp1 = Math.pow(10, d);
         double temp2 = Math.pow(10, d+1);
         //System.out.println("temp1: "+temp2);
         if(Unit>=temp1 && Unit<temp2){
            P = temp1;
            //System.out.println("p2: "+P);
            break;
         }
      }
      return P;
   }  
	
   public void init(){
      SET_DEFAULTS();
      setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
      addKeyListener(this);
   }
   
   public void actionPerformed(ActionEvent evt){
   }
   
   public void keyReleased(KeyEvent kvt){
      int code = kvt.getKeyCode();
      if(code == kvt.VK_DOWN){
         Xaxis -= displacement; 
         updateLabels(0, -displacement);
      }
      if(code == kvt.VK_UP){
         Xaxis += displacement;
         updateLabels(0, displacement);
      }
      
      if(code == kvt.VK_LEFT){
         Yaxis += displacement; 
         updateLabels(-displacement, 0);
      }
   
      if(code == kvt.VK_RIGHT){
         Yaxis -= displacement;
         updateLabels(displacement, 0);
      }
      //getImaginaryAxes();
      if(code == kvt.VK_SHIFT){
         if(displayPoints){
            displayPoints = false;
         }
         else{
            displayPoints = true;
         }
         if(displayLabels){
            displayLabels = false;
         }
         else{
            displayLabels = true;
         }
      }
      repaint();
   }
   public void updateLabels(double Xdisplacement, double Ydisplacement){
      double Y = (Ydisplacement/yGridUnit)*yP;
      Ymax = Ymax+Y;
      maxY = df.format(Ymax);
      Ymin = Ymin+Y;
      minY = df.format(Ymin);
      double X = (Xdisplacement/xGridUnit)*xP;
      Xmax = Xmax+X;
      maxX = df.format(Xmax);
      Xmin = Xmin+X;
      minX = df.format(Xmin);
   }
   
   public void keyPressed(KeyEvent kvt){
   }
   
   public void keyTyped(KeyEvent kvt){
   }
   
   public void update(Graphics g){
      paint(g);
   }
	
   public void paint(Graphics g){
      drawGraph();
      Graphics2D g2 = (Graphics2D)g;
      g2.drawImage(Canvas, null, 0, 0);
   }
   
   private void drawGraph(){
      Canvas = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB);  
      Graphics2D CanvasG = (Graphics2D)Canvas.getGraphics();
      BufferedImage graph = new BufferedImage(GRAPH_WIDTH, GRAPH_HEIGHT, BufferedImage.TYPE_INT_ARGB);
      Graphics gC = graph.getGraphics();
      gC.setColor(Color.white);
      gC.fillRect(0, 0, GRAPH_WIDTH, GRAPH_HEIGHT);
      gC.setColor(Color.lightGray);
      
      //Vertical grid Lines   
      getImaginaryAxes();
      int y_s = H_GRIDS/2;
      int x_s = W_GRIDS/2;
      for(int w=0;w<=(W_GRIDS/2);w++){
         int leftOrigin = imaginaryYaxis-(w*xGridUnit);
         int rightOrigin = imaginaryYaxis+(w*xGridUnit);
         if(leftOrigin>=0){
            gC.drawLine(leftOrigin, 0, leftOrigin, GRAPH_HEIGHT);
         }
         else{
            x_s += 1;  
            int temp = imaginaryYaxis+(x_s*xGridUnit);
            gC.drawLine(temp, 0, temp, GRAPH_HEIGHT);
         }
         
         if(rightOrigin<=GRAPH_WIDTH){
            gC.drawLine(rightOrigin, 0, rightOrigin, GRAPH_HEIGHT);
         }
         else{
            x_s += 1;  
            int temp = imaginaryYaxis-(x_s*xGridUnit);
            gC.drawLine(temp, 0, temp, GRAPH_HEIGHT);
         }
      }   
    
    //Horizontal grid Lines  
      for(int h=0;h<=(H_GRIDS/2);h++){
         int overOrigin =imaginaryXaxis-(h*yGridUnit);
         int underOrigin = imaginaryXaxis+(h*yGridUnit);   
         if(overOrigin>=0){
            gC.drawLine(0, overOrigin, GRAPH_WIDTH, overOrigin);
         }
         else{
            y_s += 1;
            int temp = imaginaryXaxis+(y_s*(yGridUnit));
            //if(temp<= GRAPH_HEIGHT){
            gC.drawLine(0, temp, GRAPH_WIDTH, temp);  
            //}
         }
         
         if(underOrigin<=GRAPH_HEIGHT){
            gC.drawLine(0, underOrigin, GRAPH_WIDTH, underOrigin);
         }
         else{
            y_s += 1;
            int temp = imaginaryXaxis-(y_s*(yGridUnit));
            //if(temp>=0){
            gC.drawLine(0, temp, GRAPH_WIDTH, temp);  
            //}
         }
      }
      
   	 //DRAW AXES
      gC.setColor(Color.black);
      gC.drawLine(Yaxis, 0, Yaxis, GRAPH_HEIGHT);
      gC.drawLine(0, Xaxis, GRAPH_WIDTH, Xaxis);
      
   //GraphPoints
      //****************
      //gC.setColor(Color.red);
      for(int c=0;c<Coordinates.length;c++){
         Color currentColor = DataColors[c];
         gC.setColor(currentColor);
         for(int p=0;p<Coordinates[c].length;p++){
            int[] temp = Coordinates[c][p];
            if(displayPoints){
               gC.fillOval((Yaxis+temp[0]-3), (Xaxis+temp[1]-3), 6, 6);
            }
            if((p+1)<Coordinates[c].length){
               gC.drawLine((Yaxis+temp[0]), (Xaxis+temp[1]), (Yaxis+Coordinates[c][p+1][0]), (Xaxis+Coordinates[c][p+1][1]));
            }
         }
      }
   	/***********************LABELS*************
      y_s = H_GRIDS/2;
      for(int y=0;y<=(H_GRIDS/2);y++){
         int tOrigin = (imaginaryXaxis-(y*yGridUnit));
         int uOrigin = imaginaryXaxis+(y*yGridUnit);
         if(tOrigin>=0){
            double temp = ((Xaxis-tOrigin)/yGridUnit)*yP;
            String label = df.format(temp);
            int X = label.length()*7;
            gC.setColor(Color.red);
            gC.drawString(label, (Yaxis-X), (tOrigin+10));
         }
         else{
            y_s += 1;
            tOrigin = imaginaryXaxis+(y_s*(yGridUnit));
            double temp = ((Xaxis-tOrigin)/yGridUnit)*yP;
            String label = df.format(temp);
            int X = label.length()*7;
            gC.setColor(Color.red);
            gC.drawString(label, (Yaxis-X), (tOrigin+10));
         }
         if(uOrigin<=GRAPH_HEIGHT){
            double temp = ((Xaxis-uOrigin)/yGridUnit)*yP;
            String label = df.format(temp);
            int X = label.length()*7;
            gC.setColor(Color.red);
            if(y>0){
               gC.drawString(label, (Yaxis-X), (uOrigin+10));
            }
            else{
               gC.drawString(label, (Yaxis-X), (uOrigin+10));
            }
         }
         else{
            y_s += 1;
            uOrigin = imaginaryXaxis-(y_s*(yGridUnit));
            double temp = ((Xaxis-uOrigin)/yGridUnit)*yP;
            String label = df.format(temp);
            int X = label.length()*7;
            System.out.println(label+":"+label.length());
            gC.setColor(Color.red);
            gC.drawString(label, (Yaxis-X), (uOrigin+10));
         }
         //System.out.println("above: "+tOrigin+"under: "+uOrigin);
      }
      
      x_s = W_GRIDS/2;
      for(int x=0;x<=(W_GRIDS/2);x++){
         int lOrigin = (imaginaryYaxis-(x*xGridUnit));
         int rOrigin = imaginaryYaxis+(x*xGridUnit);
         if(lOrigin>=0){
            double temp = ((Yaxis-lOrigin)/xGridUnit)*xP;
            String label = df.format(temp);
            int X = label.length()*7;
            gC.setColor(Color.red);
            gC.drawString(label, (lOrigin-X), (Xaxis+10));
         }
         else{
            x_s += 1;
            lOrigin = imaginaryYaxis+(x_s*(xGridUnit));
            double temp = ((Yaxis-lOrigin)/xGridUnit)*xP;
            String label = df.format(temp);
            int X = label.length()*7;
            gC.setColor(Color.red);
            gC.drawString(label, (lOrigin-X), (Xaxis+10));
         }
         if(rOrigin<=GRAPH_WIDTH){
            double temp = ((Yaxis-rOrigin)/xGridUnit)*xP;
            String label = df.format(temp);
            int X = label.length()*7;
            gC.setColor(Color.red);
            if(x>0){
               gC.drawString(label, (rOrigin-X), (Xaxis+10));
            }
            else{
               gC.drawString(label, (rOrigin-X), (Xaxis+10));
            }
         }
         else{
            y_s += 1;
            rOrigin = imaginaryYaxis-(x_s*(xGridUnit));
            double temp = ((Yaxis-rOrigin)/xGridUnit)*xP;
            String label = df.format(temp);
            int X = label.length()*7;
            System.out.println(label+":"+label.length());
            gC.setColor(Color.red);
            gC.drawString(label, (rOrigin-X), (Xaxis+10));
         }
         //System.out.println("above: "+tOrigin+"under: "+uOrigin);
      }	
   	********************************************************************************/
      if(displayLabels){
         gC.setColor(Color.black);
         int topX = getYlabelX(maxY.length());  
         gC.drawString(maxY, topX, 10);
      
         int bottomX = getYlabelX(minY.length());
         gC.drawString(minY, bottomX, 400);
      
         int leftY = getXlabelY(minX.length());
         gC.drawString(minX, 0, leftY);
      
         int rightY = getXlabelY(maxX.length());	
         gC.drawString(maxX, GRAPH_WIDTH-(maxX.length()*7), rightY);
      
         if(Xaxis>0 && Xaxis<=GRAPH_HEIGHT && Yaxis>=0 && Yaxis<GRAPH_WIDTH){
            gC.drawString(origin, Yaxis+1, Xaxis+10);
         }
      }
      CanvasG.drawImage(graph, null, left, up);
      //DRAW BORDER
      CanvasG.setColor(Color.black);
      CanvasG.drawRect(left, up, GRAPH_WIDTH, GRAPH_HEIGHT);  
   }
   
   public int getYlabelX(int labelLength){
      int X = Yaxis-(labelLength*7);
      if(X<((minX.length()*7)+7)){
         X = (minX.length()*7)+7;
      }
      else if(X>(GRAPH_WIDTH-((labelLength*7)+(maxX.length()*7)+7))){
         X = GRAPH_WIDTH-((labelLength*7)+(maxX.length()*7)+7);
      }
      return X;
   }
   
	
   public int getXlabelY(int labelLength){
      int Y = Xaxis+10;
      if(Y<20){
         Y = 20;
      }
      else if(Y>GRAPH_HEIGHT-10){
         Y = GRAPH_HEIGHT-10;
      }
      return Y;
   }
   
   public void getImaginaryAxes(){
      if(Yaxis>=0 && Yaxis<=GRAPH_WIDTH){
         imaginaryYaxis = Yaxis;
      }
      else{
         if(Yaxis<0){
            int Y = Math.abs(Yaxis)-(Math.abs(Yaxis/GRAPH_WIDTH)*GRAPH_WIDTH);
            imaginaryYaxis = GRAPH_WIDTH-Y;
         }  
         if(Yaxis>GRAPH_WIDTH){
            int Y = Yaxis-((Yaxis/GRAPH_WIDTH)*GRAPH_WIDTH);
            imaginaryYaxis = Y;
         }
      }
      
      if(Xaxis>=0 && Xaxis<=GRAPH_HEIGHT){
         imaginaryXaxis = Xaxis;
      }
      else{
         if(Xaxis<0){
            int X = Math.abs(Xaxis)-(Math.abs(Xaxis/GRAPH_HEIGHT)*GRAPH_HEIGHT);
            imaginaryXaxis = GRAPH_HEIGHT-X;
         }  
         if(Xaxis>GRAPH_HEIGHT){
            int X = Xaxis-((Xaxis/GRAPH_HEIGHT)*GRAPH_HEIGHT);
            imaginaryXaxis = X;
         }
      }
   }
     	
   
   public void plotGraph(double[][] Data){
      int[][] pts = new int[Data.length][Data[0].length];
      
      for(int x=0;x<Data.length;x++){
         pts[x][0] = (int)(((Data[x][0]/xP)*xGridUnit));
         pts[x][1] = (int)(-((Data[x][1]/yP)*yGridUnit));
      }
   
      int L0 = 1;
      int L1 = Data.length;
      int L2 = Data[0].length;
      if(Coordinates == null){
         Coordinates = new int[L0][L1][L2];
         Coordinates[0] = pts;
      }
      else{
         L0 += 1;
         if(L1<Coordinates[0].length){
            L1 = Coordinates[0].length;
         }
         if(L2<Coordinates[0][0].length){
            L2 = Coordinates[0][0].length;
         }
         int [][][] temp = new int[L0][L1][L2];
         for(int l=0;l<L0;l++){
            for(int m=0;m<L1;m++){
               if(l<(L0-1)){
                  if(m<Coordinates[0].length){
                     temp[l][m] = Coordinates[l][m];
                  }
                  else{
                     temp[l][m] = Coordinates[l][Coordinates[0].length-1] ; 
                  }
               }
               else{
                  temp[l] = pts;  
               }
            }
         }
         Coordinates = temp;
      }
   }

}
