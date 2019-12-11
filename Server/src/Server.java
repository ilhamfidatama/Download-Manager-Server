
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS R.O.G
 */
public class Server {
    
    private static final int PORT = 2107;
    private static final String PATH = "E:\\KULIAH\\SEMESTER 7\\PROGNET\\Data\\";
    
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("konek : "+listener.getLocalPort());
        try {
            while (true) {
                new Services(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
        
    }
    
    private static class Services extends Thread{
        private Socket socket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private ArrayList<String> allFiles;
        
        public Services(Socket socket) {
            this.socket = socket;
        }
        
        public void run(){
            try {
                input = new ObjectInputStream(socket.getInputStream());
                output = new ObjectOutputStream(socket.getOutputStream());
                
                while(true){
                    String filename;
                    String perintah = (String) input.readObject();
                    System.out.println("a");
                    //proses upload file dari client untuk di simpan di server
                    if(perintah.equals("UPLOAD")){
                        System.out.println("UPLOAD");
                        BufferedInputStream file = (BufferedInputStream) input.readObject();
                        filename = (String) input.readObject();
                        paketUploadFile(file, filename);
                    }
                    //proses untuk mengirimkan file yang diminta oleh client
                    else if(perintah.equals("DOWNLOAD")){
                        System.out.println("DOWNLOAD");
                        filename = (String) input.readObject();
                        BufferedInputStream file = paketPengirimanFile(filename);
                        output.writeObject("FILE-DOWNLOADED");
                        output.writeObject(file);
                    }
                    //proses untuk mengirimkan seluruh file yang dimiliki server kepada client
                    else if(perintah.equals("GET-ALL")){
                        System.out.println("GET-ALL");
                        final File folder = new File(PATH);
                        readAllFiles(folder);
                        output.writeObject("ALL_FILES");
                        output.writeObject(allFiles);
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void readAllFiles(final File folder){
            for(final File file : folder.listFiles()){
                if (file.isDirectory()) {
                    readAllFiles(file);
                } else {
                    allFiles.add(file.getName());
                }
            }
        }
        
        public BufferedInputStream paketPengirimanFile(String filename){
            BufferedInputStream file = null;
            try {
                file = new BufferedInputStream(new FileInputStream(PATH+filename));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            return file;
        }
        
        public void paketUploadFile(BufferedInputStream file, String filename){
            BufferedOutputStream upload = null;
            try {
                upload = new BufferedOutputStream(new FileOutputStream(PATH+filename));
                int byteRead;
                while((byteRead = file.read()) != -1){
                    upload.write(byteRead);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
