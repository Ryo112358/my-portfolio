import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Ryo112358
 * @title CSV (w/ header record) to XML Converter
 *
 * @about Converts a *uniform* CSV file to XML format, uniform meaning each row/tuple contains the same number of cells.
 * @input CSV data file with header record
 * @directions Modify string variables on lines 25-29 to specify filenames and desired name of table and rows.
 * @demo Example files included in directory. Have a look!
 *
 * @disclaimer Sample data taken from Veekun's Pokedex project. Check it out for a boatload of Pokemon info!
 * @commandline To create a command line version, replace variables on lines 25-29 with args[0-3]... and remember the order!
 */
public class CSVHtoXML {

	public static void main(String[] args) throws FileNotFoundException {

		// File variables - change as needed! ----------------------------------
		String csv = "dataH.csv";
		String xml = "exampleH.xml";

		String tablename = "TABLE";
		String rowname = tablename + "-ROW";

		ArrayList<ArrayList<String>> csvdata = new ArrayList<ArrayList<String>>();
		ArrayList<String> columnHeaders = new ArrayList<String>();

		// Parse CSV file for headers-------------------------------------------
		parseHeaderFile(csv, columnHeaders);

		// Parse CSV file ------------------------------------------------------
		parseCSV(csv, csvdata, columnHeaders.size());

		// Write XML File-------------------------------------------------------
		writeXML(xml, tablename, rowname, csvdata, columnHeaders);

		System.out.print("The XML file has been successfully created!");
	}

	// XML element methods ------------------------------------------------------

	public static String createXMLElement(String colHeader, String data) {
		String element = openTag(colHeader) + data + closeTag(colHeader);
		return element;
	}

	public static String openTag(String header) {
		String tag = "<" + header + ">";
		return tag;
	}

	public static String closeTag(String header) {
		String tag = "</" + header + ">";
		return tag;
	}

	// Functional methods -------------------------------------------------------

	public static boolean parseHeaderFile(String csv, ArrayList<String> fields) throws FileNotFoundException {
		Scanner parseHeaders = new Scanner(new File(csv));
		Scanner headerRecord = new Scanner(parseHeaders.nextLine());
		headerRecord.useDelimiter(",");

		while (headerRecord.hasNext()) {
			fields.add(headerRecord.next());
		}

		parseHeaders.close();
		headerRecord.close();
		return true;
	}

	public static boolean parseCSV(String csv, ArrayList<ArrayList<String>> rowData, int numFields) throws FileNotFoundException {
		Scanner parseData = new Scanner(new File(csv));

		for (int i = 0; parseData.hasNextLine(); i++) {
			rowData.add(new ArrayList<String>());

			Scanner parseRow = new Scanner(parseData.nextLine());
			parseRow.useDelimiter(",");

			for (int j = 0; j < numFields; j++) {
				addCell(parseRow, rowData, i);
			}

			parseRow.close();
		}

		parseData.close();
		return true;
	}

	public static boolean addCell(Scanner parseRow, ArrayList<ArrayList<String>> rowData, int index) {

		try {
			String cell = parseRow.next();
			// System.out.println("\"" + cell + "\"");
			rowData.get(index).add(cell);

		} catch(NoSuchElementException nsee) {
			rowData.get(index).add("");
		}

		return true;
	}

	public static boolean writeXML(String xml, String tablename, String rowname, ArrayList<ArrayList<String>> csvdata, ArrayList<String> columnHeader) throws FileNotFoundException {
		PrintWriter outputFile = new PrintWriter(xml);

		//XML Head
        outputFile.println ("<?xml version=\"1.0\"?>");
        outputFile.println (openTag(tablename));

        //XML Body
        for (int i = 0; i < csvdata.size(); i++) {

        	outputFile.print(openTag(rowname));

			for (int j = 0; j < columnHeader.size(); j++) {
				outputFile.print(createXMLElement(columnHeader.get(j).toUpperCase(), csvdata.get(i).get(j)));
			}

			outputFile.println(closeTag(rowname));
		}

        //XML End
        outputFile.println(closeTag(tablename));
        outputFile.close();

        return true;
	}
}
