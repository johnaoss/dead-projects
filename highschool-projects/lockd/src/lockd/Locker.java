package lockd;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.swing.JOptionPane;

/**
 * @author johnandrewoss
 * @date 01/11/2017
 * @title Locker.java
 * @purpose Handles locking and unlocking of lockers.
 *
 */

public class Locker {

    private boolean unlocked = false;
    private String secretKey;
    private String data;
    private String hash;

    public Locker(String secretkey) {
        this.secretKey = secretkey;
        try {
            this.data = new String(Files.readAllBytes(Paths.get("data.csv")));
            this.hash = new String(Files.readAllBytes(Paths.get("hash.txt")));     
        } catch (Exception e) {
            initFiles();
            wipeLocker();
        }
    }

    /*
     * @purpose: Unlocks the contents of the locker.
     * @returns: The decrypted contents of the locker, or NULL if something went
                 horribly wrong. 
     * @see: Crypto for the implementation of decryption.
     */
    public final String unlock() {
        try {
            if(this.hash == null || "".equals(this.hash)){
                wipeLocker();
                JOptionPane.showMessageDialog(null, "Thanks for settng your password!\n"
                        + "Please login again to continue.");
                return null;
            }
            if (hashesEqual() && unlocked == false) {
                this.data = Crypto.decrypt(this.data, this.secretKey);
                unlocked = true;
                return this.data;
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Password");
            }
        } catch (Exception e) {
            System.out.println("Error while unlocking: " + e.toString());
        }
        return null;
    }
    
    /*
     * @purpose: Initializes the files if there's nothing there.
     */
    public final void initFiles() {
        try {
            if (!Files.exists(Paths.get("data.csv"))) {
                Files.createFile(Paths.get("data.csv"));
            }
            if (!Files.exists(Paths.get("hash.txt"))) {
                Files.createFile(Paths.get("hash.txt"));
            }
        } catch (Exception e) {
            System.out.println("Error while initializing: " + e.toString());
        }
    }

    /*
     * @purpose: Locks the locker.
     * @see: Crypto.encrypt for the implementation.
     */
    public final void lock() {
        try {
            if (unlocked) {
                this.data = Crypto.encrypt(this.data, this.secretKey);
                unlocked = false;
                byte[] d = this.data.getBytes(Charset.forName("UTF-8"));
                Files.write(Paths.get("data.csv"), d, StandardOpenOption.TRUNCATE_EXISTING);
                byte[] h = this.hash.getBytes(Charset.forName("UTF-8"));
                Files.write(Paths.get("hash.txt"), h, StandardOpenOption.TRUNCATE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("Error while locking: " + e.toString());
        }
    }

    /*
     * @purpose: Saves the file
     * @param: String data: contains the CSV in plaintext.
     * @returns: Crypto.encrypt for the implementation.
     */
    public final void saveFile(String data) {
        this.data = data;
        this.lock();
        this.unlock();
    }

    /*
     * @purpose: Wipes the locker if the hash was deleted.
     * @see: Makes deleting the hash actually bad.
     */
    private void wipeLocker() {
        try{
            byte[] d = Crypto.sha256hash(this.secretKey).getBytes("UTF-8");
            //null and null char don't work as they are not enough for Crypto to decrypt.
            byte[] s = "".getBytes("UTF-8");
            Files.write(Paths.get("hash.txt"), d, StandardOpenOption.TRUNCATE_EXISTING);
            Files.write(Paths.get("data.csv"), s, StandardOpenOption.TRUNCATE_EXISTING);   
        }catch(Exception e){
            System.out.println("Error during wiping: " + e.toString());
        }
    }
    
    /*
     * @purpose: Checks to see if the hash equals the saved hash.
     * @returns: True if the hashes are equal, false if they are not.
     * @see: Crypto.encrypt for the implementation.
     */
    private boolean hashesEqual() {
        return Crypto.sha256hash(this.secretKey).equals(this.hash);
    }
    
    /*
     * @purpose: Sets the SecretKey for changing the master password
     * @param: String s: The SecretKey to be stored.
     */
    public void setCryptoKey(String s){
        this.secretKey = s;
        this.hash = Crypto.sha256hash(s);
    }
}