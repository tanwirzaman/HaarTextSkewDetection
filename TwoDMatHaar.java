package edu.usu.csatl;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;


public class TwoDMatHaar {

	static final void displayMat(Mat mat) {
		//mat.convertTo(mat, CvType.CV_64FC3);
		for (int r = 0; r < mat.rows(); r++) {
			System.out.print("Row " + r + ": ");
			for (int c = 0; c < mat.rows(); c++) {
				System.out.print(mat.get(r, c)[0] + " ");
			}
			System.out.println();
		}
	}
	
	static double log(int x, int base) {
		return (Math.log(x) / Math.log(base));
	}

	static final boolean isPowOf2(int n) {
		if ( n < 1 )
			return false;
		else {
			double pOf2 = TwoDMatHaar.log(n, 2);
			return Math.abs(pOf2 - (int)pOf2) == 0;
		}
	}
	
   static final int smallestPowOf2LargerThanX(int n) {
		
		int rslt = n+1;
		while ( !TwoDMatHaar.isPowOf2(rslt) ) { rslt++; }
		return rslt;
	}

	static final int largestPowOf2SmallerThanX(int n) {
		
		int rslt = n-1;
		while ( !TwoDMatHaar.isPowOf2(rslt) ) { rslt--; }
		return rslt;
	}
	
	static final double[][] get2DPixelArrayFromMat(Mat mat) {
		int num_rows = mat.rows();
		int num_cols = mat.cols();
		
		
		if ( num_rows == 0 || num_cols == 0 ) {
			System.out.println("empty mat");
			return null;
		}
		
		if ( !TwoDMatHaar.isPowOf2(num_rows) ) {
			//System.out.println("check get2DPixelArrayFromMat()");
			//num_rows = TwoDMatHaar.largestPowOf2SmallerThanX(num_rows);
			num_rows = TwoDMatHaar.smallestPowOf2LargerThanX(num_rows);
			//System.out.println("num_rows = " + num_rows);
		}
		
		if ( !TwoDMatHaar.isPowOf2(num_cols) ) {
			//num_cols = TwoDMatHaar.largestPowOf2SmallerThanX(num_cols);
			num_cols = TwoDMatHaar.smallestPowOf2LargerThanX(num_cols);
			//System.out.println("num_cols = " + num_cols);
		}
		
		
		//int dim = Math.min(num_rows, num_cols);
		int dim = Math.max(num_rows, num_cols);
		num_rows = dim;
		num_cols = dim;
		
		//System.out.println("dim = " + dim);
		
		double[][] imgArr = new double[dim][dim];

		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if(mat.get(i, j)!=null)
					imgArr[i][j] = mat.get(i, j)[0];
				else
					imgArr[i][j] = 255;
			}
		}
		
		System.out.println("ImgArr's num_rows = " + imgArr.length);
		System.out.println("ImgArr's num_cols = " + imgArr[0].length);
		return imgArr;
	}
	
	static final double[][] get2DPixelArrayFromMatAt(Mat mat, int x, int y, int size) {

		double[][] imgArr = new double[size][size];
		
		int r = 0;
		int c = 0;
		for (int i = x; i < x + size; i++) {
			c = 0;
			for (int j = y; j < y + size; j++) {
				//System.out.println("get2DPixelArrayFromMatAt(): " + i + ", " + j);
				imgArr[r][c++] = mat.get(i, j)[0];
			}
			r++;
		}
		
		//System.out.println("ImgArr's num_rows = " + imgArr.length);
		//System.out.println("ImgArr's num_cols = " + imgArr[0].length);
		return imgArr;
	}
	
	static final Mat getMatFrom2DPixelArray(Mat mat, double[][] array) {
		// steps is the num_steps_forward
		Mat newMat = new Mat(mat.rows(), mat.cols(), CvType.CV_16S);
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++) {
				newMat.put(i, j, array[i][j]);
			}
		}
		return newMat;
	}

	// Mat is of size 2^n x 2^n.
	public static ArrayList<double[][]> orderedFastHaarWaveletTransformForNumIters(
			Mat mat, int n, int num_iters) {
		double[][] pixels = TwoDMatHaar.get2DPixelArrayFromMat(mat);
		ArrayList<double[][]> rslt = TwoDHaar
				.orderedFastHaarWaveletTransformForNumIters(pixels, n,
						num_iters);
		pixels = null;
		return rslt;
	}
	

     
    
}
