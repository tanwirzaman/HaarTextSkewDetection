package edu.usu.csatl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class TextSkewDetector {

	static int Haar_Iters=2;
	static final String SOURCE_PATH="INPUT_PATH";
	static final String DEST_PATH="DESTINATION_PATH";
	static final String TEXT_CHUNK_PATH="TEXT_CHUNK_PATH";
	static double x1,y1,x2,y2,x3,y3,x4,y4;
	static double skewAngle;
	static final double  contourAreaThreshold=THRESH_VALUE;
	
	static Runtime runTime; 
	static String METHODTYPE="";
	
		
	public static void main(String[] args) throws IOException, InterruptedException {		
		final File ImageFolder = new File(SOURCE_PATH);
		File dir = new File(DEST_PATH);
		dir.mkdir();
		for (final File fileEntry : ImageFolder.listFiles()) {
			String filepath=fileEntry.getAbsolutePath();
			System.out.println("Processing: "+filepath);
			java.util.Date dateS= new java.util.Date();
			boolean Projection=false;
			String filename=fileEntry.getName().replaceFirst("[.][^.]+$", "");
						
			double contourArea=ContourDetect.detectContour(filepath,fileEntry.getName(),DEST_PATH);
			
			if(contourArea>contourAreaThreshold)
			{
				METHODTYPE="Contour";
				Projection=true;
				
			}
			else
			{
				Projection=true;
				HaarTextSkew.detectTextSkew(filepath,DEST_PATH);
				BlobDetector.findSkewAngle(filepath,fileEntry.getName(),DEST_PATH);
				METHODTYPE="Blob";
			
			}
			
			Mat mat = Highgui.imread(filepath);
			SkewTextChunker.SkewTextChunk(filename,mat, x1, y1, x2, y2, x3, y3, x4, y4, skewAngle);
			clearAll();
			
			
		}
		System.out.println("Done");
	}
	
	public static void clearAll()
    {
    	x1=0;y1=0;x2=0;y2=0;x3=0;y3=0;x4=0;y4=0;
    	skewAngle=0;
    }


}
