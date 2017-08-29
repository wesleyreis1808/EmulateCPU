/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emulatecpu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author WesleyReis
 */
public class AcessMemory {

    static int stackPointer = 2;

    public static void main(String[] args) throws IOException {
       
    }

    public static String getWord(int address) {
        RandomAccessFile arquivo;
        String texto = null;
        try {
            URL url = AcessMemory.class.getResource("memory.txt");
            arquivo = new RandomAccessFile(new File(url.toURI()), "r");
            
            arquivo.seek((address * 34));
            texto = arquivo.readLine();
        } catch (Exception ex) {
            Logger.getLogger(AcessMemory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return texto;
    }

    public static void setWord(int val) {
        RandomAccessFile arquivo;
        try {
            URL url = AcessMemory.class.getResource("memory.txt");
            arquivo = new RandomAccessFile(new File(url.toURI()), "rw");
            
            String numero = Integer.toBinaryString(val);
            int tam = 32 - numero.length();

            for (int i = 0; i < tam; i++) {
                numero = "0" + numero;
            }
            numero = "\n" + numero;

            stackPointer++;
            arquivo.seek((stackPointer * 34));
            arquivo.writeBytes(numero);

        } catch (Exception ex) {
            Logger.getLogger(AcessMemory.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String getProgram() {
        RandomAccessFile arquivo;
        String texto = null;
        try {
            URL url = AcessMemory.class.getResource("memory.txt");
            arquivo = new RandomAccessFile(new File(url.toURI()), "r");
            
            arquivo.seek((CPU.registros[OpCodes.PC] * 34));
            CPU.registros[OpCodes.PC] += 1;            
            
            texto = arquivo.readLine();
            if(texto == null) return null;
            
            if(texto.equalsIgnoreCase(""))  CPU.run = false;
            
            return texto;
        } catch (Exception ex) {
            Logger.getLogger(AcessMemory.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        CPU.run = false;
        return null;
    }

}
