package edu.usu.csatl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class BlobDetector {

	static String HOME="";	
	static double WIDTH,HEIGHT;
	
		
	public static void findSkewAngle(String filepath, String filename, String destPath) {
		clearAll();
		
		try{
		ArrayList<Point> pointList=new ArrayList<Point>();  
		
		Mat matDC=Highgui.imread(destPath+"/DC_out.jpg");	
		Mat matVC=Highgui.imread(destPath+"/VC_out.jpg");	
		Mat matHC=Highgui.imread(destPath+"/HC_out.jpg");
		
		Mat matFinal=Highgui.imread(filepath);
		
		for(int i=0;i<matDC.rows();i++)
		{
			for(int j=0;j<matDC.cols();j++)
			{		
				double val=(matDC.get(i, j)[0]*0.6 + matVC.get(i, j)[0]*0.2 + matHC.get(i, j)[0]*0.2);
				if(val==255)
				{
					pointList.add(new Point(j,i));
				}				
			}			
		}
		TextSkewDetector.skewAngle=getBoundingBoxAngle(filename,matDC,matFinal,destPath,pointList);
		
		
		}
		catch(Exception e){
			
		}
		
	}
	
	public static double getBoundingBoxAngle(String filename, Mat mat,Mat finalMat,String destPath, ArrayList<Point> pointList)
	{
		try{
		MatOfPoint2f points=new MatOfPoint2f();	
		//System.out.println("pointList.size():"+pointList.size());
		points.fromList(pointList);
		RotatedRect rrect=Imgproc.minAreaRect(points);
		double angle=rrect.angle;
		System.out.println("Orig Angle: "+angle);
		/*if (angle <= -45.0)
			angle += 90.0;	*/	
		if (angle <= -44.0)
			angle =-angle;
		else
			angle=90-(angle);
		
		Point[] vertices=new Point[4];
		rrect.points(vertices);		
		
		HEIGHT=Math.floor(rrect.size.height*Math.pow(2, TextSkewDetector.Haar_Iters));
		WIDTH=Math.floor(rrect.size.width*Math.pow(2, TextSkewDetector.Haar_Iters));
		
		for(int i=0;i<4;++i)
		{
			if(vertices[i].x<0)
				vertices[i].x=0;
			if(vertices[i].y<0)
				vertices[i].y=0;			
			Core.line(mat,vertices[i], vertices[(i+1)%4],new Scalar(0,255,0),5);
		}
		if(angle<=90)
		{
		for(int i=0;i<4;++i)
		{		
			double x=(int)vertices[i].x*Math.pow(2, TextSkewDetector.Haar_Iters);
			double y=(int)vertices[i].y*Math.pow(2, TextSkewDetector.Haar_Iters);
			
			switch(i){
			case 0:
				TextSkewDetector.x4=x;
				TextSkewDetector.y4=y;
				break;
			case 1:
				TextSkewDetector.x3=x;
				TextSkewDetector.y3=y;
				break;
			case 2:
				TextSkewDetector.x1=x;
				TextSkewDetector.y1=y;
				break;
			case 3:
				TextSkewDetector.x2=x;
				TextSkewDetector.y2=y;
				break;
			}
		}
		}
		else
		{
			HEIGHT=Math.floor(rrect.size.width*Math.pow(2, TextSkewDetector.Haar_Iters));
			WIDTH=Math.floor(rrect.size.height*Math.pow(2, TextSkewDetector.Haar_Iters));
			
			for(int i=0;i<4;++i)
			{
						
				double x=(int)vertices[i].x*Math.pow(2, TextSkewDetector.Haar_Iters);
				double y=(int)vertices[i].y*Math.pow(2, TextSkewDetector.Haar_Iters);
				
				switch(i){
				case 0:
					TextSkewDetector.x3=x;
					TextSkewDetector.y3=y;
					break;
				case 1:
					TextSkewDetector.x1=x;
					TextSkewDetector.y1=y;
					break;
				case 2:
					TextSkewDetector.x2=x;
					TextSkewDetector.y2=y;
					break;
				case 3:
					TextSkewDetector.x4=x;
					TextSkewDetector.y4=y;
					break;
				}
			}
		}
		
		Highgui.imwrite(destPath+"/"+filename+"_rect"+angle+".jpg",mat);
		
		return angle;
		}
		catch(Exception e){
			return 0;
		}
	}
	
	public static void clearAll()
    {
    	
    	WIDTH=0;HEIGHT=0;    	
    }
	
		
	public static void appendLog(String destPath,String text)
	{       
	   File logFile = new File(destPath+"/textlog.txt");
	   if (!logFile.exists())
	   {
	      try
	      {
	         logFile.createNewFile();
	      } 
	      catch (IOException e)
	      {        
	         e.printStackTrace();
	      }
	   }
	   try
	   {
	      //BufferedWriter for performance, true to set append to file flag
	      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
	      buf.append(text);
	      buf.newLine();
	      buf.close();
	   }
	   catch (IOException e)
	   {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	   }
	  }
	
	
}
