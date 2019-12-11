
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
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
    private static final String PATH = "E:\\KULIAH\\SEMESTER 7\\PROGNET\\server\\";
    
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
        private DataInputStream input;
        private ObjectOutputStream output;
        private ArrayList<String> allFiles = new ArrayList<>();
        private Boolean running = false;
        
        public Services(Socket socket) {
            this.socket = socket;
        }
        
        public void run(){
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new ObjectOutputStream(socket.getOutputStream());
                
                while(true){
                    String filename;
                    String perintah = (String) input.readUTF();
                    System.out.println("perintah : "+perintah);
                    if(perintah.equals("DOWNLOAD")){
                        filename = (String) input.readUTF();
                        byte[] data = paketPengirimanFile(filename);
                        System.out.println("file download:"+data.length);
                        output.writeObject("FILE-DOWNLOADED");
                        output.writeObject(data);
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
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void readAllFiles(final File folder){
            System.out.println("list:"+folder.listFiles().length);
            for(final File file : folder.listFiles()){
                if (file.isDirectory()) {
                    readAllFiles(file);
                } else {
                    System.out.println("file name "+file.getName());
                    allFiles.add(file.getName());
                }
            }
        }
        
        public byte[] paketPengirimanFile(String filename) throws IOException{
            byte[] data = null;
            File file = new File(PATH+filename);
            data = Files.readAllBytes(file.toPath());
            return data;
        }
    }
}
