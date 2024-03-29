
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS R.O.G
 */
public class DownloadManager extends javax.swing.JFrame implements Runnable{

    private Socket socket;
    private DataOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final String IP_ADDRESS = "localhost";
    private final int PORT = 2107;
    private static final String PATH = "E:\\KULIAH\\SEMESTER 7\\PROGNET\\client\\";
    private ArrayList<String> allFiles = new ArrayList<>();
    private Boolean running = false;
    
    public DownloadManager() {
        initComponents();
    }
    
    @Override
    public void run() {
        try{
            socket = new Socket(IP_ADDRESS, PORT);
            System.out.println("socket : "+socket.getRemoteSocketAddress());
            inputStream = new ObjectInputStream(socket.getInputStream());  //pintu penerimaan data dari server
            outputStream = new DataOutputStream(socket.getOutputStream()); //pintu untuk mengirimkan data/perintah ke server
            
            //meminta seluruh nama files yang ada di server
            outputStream.writeUTF("GET-ALL");//memberikan perintah ke server untuk memberikan seluruh data nama file yang dimiliki
            String perintah = (String) inputStream.readObject(); //menerima perintah bawaan dari server
            allFiles = (ArrayList<String>) inputStream.readObject(); //menerima data nama file dari server
            updateTabel();
            
            running = true;
//            String perintah;
            while(running){
                perintah = (String) inputStream.readObject(); //menerima perintah dari server
                System.out.println(perintah);
                if(perintah.equals("ALL_FILES")){
                    allFiles = (ArrayList<String>) inputStream.readObject(); //menerima data nama file dari server
                    updateTabel();
                }else if(perintah.equals("FILE-DOWNLOADED")){
                    System.out.println("file-downloaded");
                    byte[] data = (byte[]) inputStream.readObject(); //menerima byte data file yang ingin di download dari server
                    String fileName = fileNameText.getText();
                    paketDownloadFile(data, fileName);
                }
            }
        }catch(IOException | ClassNotFoundException ex){
            System.out.println("catch : "+ex.getMessage());
        }
    }
    
    public void paketDownloadFile(byte[] data, String filename) throws IOException{
        File file = new File(PATH+filename);
        Path write = Files.write(file.toPath(), data);
        System.out.println("path : "+write.toString());
    }
    
    public void updateTabel(){
        DefaultTableModel tabel = (DefaultTableModel) tabelData.getModel();
        tabel.setRowCount(allFiles.size());
        int baris = 0;
        for(String fileName : allFiles){
            tabel.setValueAt(baris, baris, 0);
            tabel.setValueAt(fileName, baris, 1);
            baris++;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabelData = new javax.swing.JTable();
        fileNameText = new javax.swing.JLabel();
        btnDownload = new javax.swing.JButton();
        downloadProgress = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Download Manager");
        setPreferredSize(new java.awt.Dimension(800, 600));

        tabelData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Nama File"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelData);
        if (tabelData.getColumnModel().getColumnCount() > 0) {
            tabelData.getColumnModel().getColumn(0).setResizable(false);
            tabelData.getColumnModel().getColumn(1).setResizable(false);
        }

        btnDownload.setText("Download");
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(downloadProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fileNameText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDownload, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(fileNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31)
                .addComponent(downloadProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadActionPerformed
        String fileName = fileNameText.getText();
        try {
            outputStream.writeUTF("DOWNLOAD");
            outputStream.writeUTF(fileName);
        } catch (IOException ex) {
            Logger.getLogger(DownloadManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDownloadActionPerformed

    private void tabelDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDataMouseClicked
        int baris = tabelData.getSelectedRow();
        String fileName = tabelData.getModel().getValueAt(baris, 1).toString();
        fileNameText.setText(fileName);
    }//GEN-LAST:event_tabelDataMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DownloadManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DownloadManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DownloadManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DownloadManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        DownloadManager screen = new DownloadManager();
        screen.setVisible(true);
        new Thread(screen).start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDownload;
    private javax.swing.JProgressBar downloadProgress;
    private javax.swing.JLabel fileNameText;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelData;
    // End of variables declaration//GEN-END:variables

}
