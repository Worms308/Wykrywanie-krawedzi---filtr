package program;

import java.util.ArrayList;
import java.util.Scanner;

import conversion.ImageConventer;

public class Main {

	public static void main(String[] args) {
		final int filterSize = 7;
		ImageConventer.filter = new double[filterSize][filterSize];
		for (int i=0;i<filterSize;++i)
			for (int j=0;j<filterSize;++j)
				ImageConventer.filter[i][j] = 1;
		
		String filename;
		if (args.length == 1)
			filename = args[1];
		else {
			Scanner scanner = new Scanner(System.in);
			System.out.print("Podaj nazw� pliku: ");
			filename = scanner.nextLine();
			scanner.close();
		}
		
		System.out.println("Przetwarzanie...");
		
		ArrayList<Double> png = ImageConventer.getArrayList(filename);
		
		if (png != null){
			System.out.println("Wczytano obraz poprawnie.");
			
			ImageConventer.setImgFromArray(png, "gray.png");
			System.out.println("Przekonwertowano na skale szaro�ci.");
			
			double[][] tmp = ImageConventer.matrixFromArray(png);
			double[][] filtered = ImageConventer.filterMatrix(tmp);
			ImageConventer.setImgFromArray(ImageConventer.arrayFromMatrix(filtered), "blured.png");
			System.out.println("Przefiltrowano obraz.");
			
			double[][] negative1 = ImageConventer.shiftMatrix(filtered, 2, 2);
			double[][] negative2 = ImageConventer.shiftMatrix(filtered, -2, -2);
			System.out.println("Wygenerowano negatywy.");
			negative1 = ImageConventer.negativeMatrix(negative1);
			negative2 = ImageConventer.negativeMatrix(negative2);
			
			double[][] first = ImageConventer.addMatrixes(tmp, negative1);
			double[][] second = ImageConventer.addMatrixes(tmp, negative2);
			System.out.println("Zrobiono sume orygina�u i negatyw�w.");
			double[][] finalResult = ImageConventer.maxFromMatrix(first, second);
			ImageConventer.setImgFromArray(ImageConventer.arrayFromMatrix(finalResult), "borders.png");
			System.out.println("Wygenerowano kraw�dzie na obrazie.");
		}
	}

}
