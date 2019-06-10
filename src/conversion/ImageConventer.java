package conversion;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageConventer {

	public static int dimensionX = 25;
	public static int dimensionY = 25;
	public static double filter[][];
	
	
	private ImageConventer() {
		
	}
	
	public static double[][] maxFromMatrix(double[][] first, double[][] second) {
		double[][] result = new double[dimensionX][dimensionY];
		for (int i=0;i<dimensionX;++i)
			for (int j=0;j<dimensionY;++j){
				result[i][j] = Math.max(first[i][j], second[i][j]);
			}
		return result;
	}
	public static ArrayList<Double> getArrayList(String path) {
		BufferedImage image;
		try {
			image = ImageIO.read(new File(path));
			dimensionX = image.getWidth();
			dimensionY = image.getHeight();
			ArrayList<Double> result = new ArrayList<Double>();
			for (int i=0;i<dimensionX;++i){
				for (int j=0;j<dimensionY;++j){
					int tmp = (image.getRGB(i, j));
					float r = new Color(tmp).getRed();
					float g = new Color(tmp).getGreen();
					float b = new Color(tmp).getBlue();
					double color = (r+b+g)/765;

					result.add(color);
				}
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setImgFromArray(ArrayList<Double> data, String path) {
		if (data == null){
			System.err.println("Unable to make PNG");
			return;
		}
		BufferedImage image = new BufferedImage(dimensionX, dimensionY, BufferedImage.TYPE_3BYTE_BGR);
		for (int i=0;i<dimensionX*dimensionY;++i){
			float color = (float)(1*data.get(i));
			image.setRGB((int)i/dimensionY, i%dimensionY, new Color(color, color, color).getRGB());
		}
		File outputfile = new File(path);
		try {
			ImageIO.write(image, "png", outputfile);
			//System.out.println("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static double[][] matrixFromArray(ArrayList<Double> data) {
		double result[][] = new double[dimensionX][dimensionY];
		for (int i=0;i<dimensionX*dimensionY;++i){
			result[(int)i/dimensionY][i%dimensionY] = data.get(i);
		}
		return result;
	}
	
	public static double[][] shiftMatrix(double[][] matrix, int x, int y) {
		double[][] result = new double[dimensionX][dimensionY];
		if (x > 0 && y > 0){
			for (int i=0;i<dimensionX-y;++i)
				for (int j=0;j<dimensionY-x;++j){
					result[i][j] = matrix[i+y][j+x];
				}
		} else {
			for (int i=dimensionX-1;i>=0-y;--i)
				for (int j=dimensionY-1;j>=0-x;--j){
					result[i][j] = matrix[i+y][j+x];
				}
		}
		return result;
	}
	
	public static double[][] negativeMatrix(double[][] matrix) {
		for (int i=0;i<dimensionX;++i)
			for (int j=0;j<dimensionY-1;++j){
				matrix[i][j] = matrix[i][j] * (-1);
			}
		return matrix;
	}
	
	public static double[][] addMatrixes(double[][] first, double[][] second) {
		double[][] result = new double[dimensionX][dimensionY];
		for (int i=0;i<dimensionX;++i)
			for (int j=0;j<dimensionY-1;++j){
				double tmp = first[i][j] + second[i][j];
				if (tmp > 0 && tmp < 1.0)		result[i][j] = tmp;
				else 							result[i][j] = 0;
			}
		return result;
	}
	
	public static ArrayList<Double> arrayFromMatrix(double[][] data) {
		ArrayList<Double> result = new ArrayList<Double>();
		for (int i=0;i<dimensionX;++i)
			for (int j=0;j<dimensionY;++j)
				result.add(data[i][j]);
		return result;
	}
	
	private static double calcFilter(double[][] data, int x, int y) {
		double result = 0;
		double divide = 0;
		for (int i=0;i<filter[0].length;++i)
			for (int j=0;j<filter[0].length;++j){
				result += data[x + i - filter[0].length/2][y + j - filter[0].length/2] * filter[i][j];
				divide += filter[i][j];
			}
		return result/divide;
	}
	
	public static double[][] filterMatrix(double[][] data) {
		double result[][] = new double[dimensionX][dimensionY];
		for (int i=filter[0].length/2;i<dimensionX-filter[0].length/2;++i)
			for (int j=filter[0].length/2;j<dimensionY-filter[0].length/2;++j){
				double tmp = calcFilter(data, i, j);
				if (tmp >= 0 && tmp <= 1.0)	result[i][j] = tmp;
				else 			result[i][j] = 0.0;
			}
		for (int i=0;i<dimensionX;++i)
			for (int j=0;j<dimensionY;++j){
				if (i < filter[0].length/2 || j < filter[0].length/2 || i + 1 > dimensionX  - filter[0].length/2 || j + 1 > dimensionY - filter[0].length/2)
					result[i][j] = data[i][j];
			}
		return result;
	}
}
