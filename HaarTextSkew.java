package edu.usu.csatl;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;



public class HaarTextSkew {
    
	static String HOME="";
	
	static final Scalar COLOR_GREEN = new Scalar(0, 255, 0);
	static final Scalar COLOR_RED = new Scalar(0, 0, 255);
	
    
    public static ArrayList<double[][]> test_ordered_haar(double[][] data, int n, int num_steps_forward, int num_steps_back) {
        ArrayList<double[][]> transform = TwoDHaar.orderedFastHaarWaveletTransformForNumIters(data, n, num_steps_forward);
             
        return transform;
       
    }
    
    
    
   public static void  detectTextSkew(String file, String destPath)
   {
	  
	   try{
	   Mat mat = Highgui.imread(file);	   
	   double[][] imgArr = TwoDMatHaar.get2DPixelArrayFromMat(mat);		
	   int n = (int) (Math.log(imgArr[0].length) / Math.log(2.0));

	   
	   ArrayList<double[][]> transform=HaarTextSkew.test_ordered_haar(imgArr,n, TextSkewDetector.Haar_Iters, 1);
	   for(int k=1;k<=4;k++)
	   {
		   double[][] array=transform.get(transform.size()-k);
		   
		   Mat newMat=new Mat((int)(imgArr.length/Math.pow(2,TextSkewDetector.Haar_Iters)),(int)(imgArr.length/Math.pow(2,TextSkewDetector.Haar_Iters)),CvType.CV_16S);
		   for (int i=0; i<array.length; i++)
		   {
			   for(int j=0; j<array.length; j++)
			   {
				   newMat.put(i, j, array[i][j]);    				
			   }    			
		   }
		   Imgproc.threshold(newMat, newMat, 5, 255, Imgproc.THRESH_BINARY);
		   switch (k) { 
		   case 1:
			   break;
		   case 2:	
			   Imgproc.dilate(newMat, newMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4,4)));
			   Imgproc.erode(newMat, newMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3)));
			   Imgproc.erode(newMat, newMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3)));
			   
			   int left_crop=0,right_crop=0;
			   boolean set=false;
			   Highgui.imwrite(destPath+"/DC_out.jpg",newMat);
			   break;
		   case 3:		
			   Highgui.imwrite(destPath+"/VC_out.jpg",newMat);
			   break;
		   case 4:	
			   Highgui.imwrite(destPath+"/HC_out.jpg",newMat);
			   break;			
		   }
		   mat=null;
	   }
	   }
	   catch(Exception e)
	   {
		 e.printStackTrace(); 
	   }
	   
	   
	   
   }
   

	private static void DrawProjection(int colNum,double projection,Mat image) {
		final Point pt1 = new Point(-1, 0);
        final Point pt2 = new Point();        
        pt1.x = colNum;
        pt2.x = colNum;
        pt2.y = image.height();
        Core.line(image, pt1, pt2, new Scalar(0,0,0));
	}
	
} 

