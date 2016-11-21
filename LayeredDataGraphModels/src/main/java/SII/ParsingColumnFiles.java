package SII;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

import java.io.*;
import java.util.ArrayList;

/**
 * Adi Sidapara
 * <p>
 * 11/14/16
 */
public class ParsingColumnFiles {
    public static final String PATH = "/Users/adisidapara/Downloads/Classwork/BRCA/Oncotated/";
    public static void main(String[] args) throws IOException {
        ArrayList<String> files = fileDirectory();
        for(int i = 0; i < files.size(); i++) {
            System.out.println(files.get(i));
            commitNodes(parseFile(PATH + files.get(i)));
        }
        //System.out.println(tableData(parseFile(PATH))[0].length);
    }
    public static void commitNodes(ArrayList<String []> list){
        Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "root" ) );
        Session session = driver.session();

        for(int i = 1; i < list.size(); i++){
            String ENTREZId = list.get(i)[1];
            String HUGOSym = list.get(i)[0];
            String chromosomeNum = list.get(i)[3];
            String varClass = list.get(i)[7];
            String command = "CREATE (n:SNIP { name: '" + HUGOSym + "', EntrezID: '" + ENTREZId+ "', chromosome: '" + chromosomeNum + "', variantClass: '" + varClass + "'})";
            session.run( command );

        }
        session.close();
        driver.close();
    }
    public static ArrayList<String[]> parseFile(String filename) {

        String csvFile = filename;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = "\t";
        ArrayList<String[]> fileColumns = new ArrayList<String[]>();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] row = line.split(cvsSplitBy);
                fileColumns.add(row);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileColumns;
    }
    public static String[][] tableData(ArrayList<String []> list){
        String[][] table = new String[list.size()][list.get(0).length];
        for(int x = 0; x < list.size(); x++){
            for(int y = 0; y < list.get(0).length; y++){
                table[x][y] = list.get(x)[y];
            }
        }
        return table;
    }
    public static ArrayList<String> fileDirectory() throws IOException {
        Runtime r = Runtime.getRuntime();
        Process p = r.exec("cd /Users/adisidapara/Downloads/Classwork/BRCA/Oncotated & ls");

        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        ArrayList <String> fileNames = new ArrayList<String>();
        while(b.readLine() != null){
            System.out.println(b.readLine());
            fileNames.add(b.readLine());

        }
        return fileNames;
    }
}
