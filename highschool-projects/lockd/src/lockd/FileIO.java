package lockd;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author calumpatrick
 * @date 01/20/2017
 * @title FileIO.java
 * @purpose Handles manipulation of data.
 *
 */

public class FileIO {
    
    static List<String[]> dataRowsList = new ArrayList<>();
    static String fileHeader = "service,username,password";
   
    /*
     * @purpose: Inserts a String[] element to dataRowsList.
     * @params: String service: Name of service added to String[].
                String usr: Username added to String[].
                String pwd: Password added to String[].
     */
    public final static void addItem(String service, String usr, String pwd) {
        dataRowsList.add(new String[]{service, usr, pwd});
    }
    
    /*
     * @purpose: Modify an element of dataRowsList.
     * @params: int index: Index of element to be removed.
     */
    public final static void removeItem(int index) {
        dataRowsList.remove(index);
    }
    
    /*
     * @purpose: Modified a String[] element of dataRowsList
     * @params: int item: Index of dataRowsList element to be modified.
                int field: Index of array element inside dataRowsList element to be modified
                String content: Updated data to be inserted.
     */
    public final static void modifyItem(int item, int field, String content) {
        String[] row = dataRowsList.get(item);
        row[field] = content;
        dataRowsList.set(item, row);
    }

    /*
     * @purpose: Convert data.csv to List
     * @param: String data: Contains all of the decrypted contents of the locker
     */
    public final static void indexData(String data) {
        dataRowsList.clear();
        Scanner fs = new Scanner(data);
        if (fs.hasNextLine()) {
            fs.nextLine();
        } else {
            String user = System.getProperty("user.name");
            addItem("Example", user, "password");
        }
        while (fs.hasNext()) {
            dataRowsList.add(fs.nextLine().split(","));
        }
        fs.close();
    }

    /*
     * @purpose: Retrieve a String[] element from dataRowsList
     * @params: int index: Index of element to be retrieved.
     * @returns: String[] element of dataRowsList at given index.
     */
    public final static String[] retrieveItem(int index) {
        return dataRowsList.get(index);
    }
}
