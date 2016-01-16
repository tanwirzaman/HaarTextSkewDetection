package edu.usu.csatl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class ContourDetect {

	
	static double WIDTH,HEIGHT;
	static List<MatOfPoint> contours = new ArrayList<MatOfPoint>();  
	static ArrayList<Point> pointList=new ArrayList<Point>();  
    
    
    public static double detectContour(String filepath, String filename, String destPath){
    	clearAll();
    	double MAX_AREA;
    	Mat orig=Highgui.imread(filepath);
    	Mat mat=orig.clone();
    	Imgproc.cvtColor(mat, mat,Imgproc.COLOR_BGR2GRAY);
    	Imgproc.Canny(mat, mat, 100, 200);
    	
    	Imgproc.dilate(mat, mat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));
    	Imgproc.erode(mat, mat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));
    	Mat out=orig.clone();
    	Imgproc.findContours(mat, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

    	List<Double> areaList=new ArrayList<Double>();
    	for(int i = 0; i < contours.size(); i++)
    	{
    		areaList.add(Imgproc.contourArea(contours.get(i)));
    	}
    	Collections.sort(areaList);
    	MAX_AREA=areaList.get(areaList.size()-1);
    
    	if(MAX_AREA <=20000)
    		return MAX_AREA;
    	for (int i = 0; i < contours.size(); i++)
    	{
    
    		if (Imgproc.contourArea(contours.get(i)) == MAX_AREA)
    			Imgproc.drawContours(mat, contours, i, new Scalar(255),-1);        	
    		else
    			Imgproc.drawContours(mat, contours, i, new Scalar(0),-1);
    	}
    	for(int i=0;i<mat.rows();i++)
    	{
    		for(int j=0;j<mat.cols();j++)
    		{	
    			if(mat.get(i, j)[0]==255)			
    			{
    				pointList.add(new Point(j,i));
    			}				
    		}			
    	}    	
    
    	TextSkewDetector.skewAngle=getBoundingBoxAngle(filename,out,destPath,pointList);
   
    	return MAX_AREA;
    }
    
    public static double getBoundingBoxAngle(String filename, Mat mat,String destPath, ArrayList<Point> pointList)
	{
		try{
		MatOfPoint2f points=new MatOfPoint2f();	
		points.fromList(pointList);
		RotatedRect rrect=Imgproc.minAreaRect(points);
		double angle=rrect.angle;
		if (angle <= -44.0)
			angle =-angle;
		else
			angle=90-(angle);
		
		Point[] vertices=new Point[4];
		rrect.points(vertices);		
		
		HEIGHT=Math.floor(rrect.size.height);
		WIDTH=Math.floor(rrect.size.width);
		
		for(int i=0;i<4;++i)
		{
			if(vertices[i].x<0)
				vertices[i].x=0;
			if(vertices[i].y<0)
				vertices[i].y=0;
			//Core.circle(mat, vertices[i], 5, new Scalar(0,255,0));
			Core.line(mat,vertices[i], vertices[(i+1)%4],new Scalar(0,255,0),5);
			
		}
		if(angle<=90)
		{
			for(int i=0;i<4;++i)
			{

				double x=(int)vertices[i].x;
				double y=(int)vertices[i].y;

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
			HEIGHT=Math.floor(rrect.size.width);
			WIDTH=Math.floor(rrect.size.height);
			

			for(int i=0;i<4;++i)
			{

				double x=(int)vertices[i].x;
				double y=(int)vertices[i].y;

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
    	contours.clear();  
    	pointList.clear();  
       
    }
	
}
