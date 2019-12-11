
import java.io.BufferedInputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS R.O.G
 */
public class Paket {
    String perintah;
    BufferedInputStream paket;
    
    public Paket(String perintah, BufferedInputStream paket){
        this.perintah = perintah;
        this.paket = paket;
    }
}
