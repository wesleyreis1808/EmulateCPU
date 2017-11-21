/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emulatecpu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author WesleyReis
 */
public class AcessMemory {

    static int stackPointer = 5;

    public static void main(String[] args) throws IOException {
        AcessMemory.stackPointer = contaLinha();
        //setWord(1);
        System.out.println(getWord(2));
    }

    public static String getWord(int address) {
        RandomAccessFile arquivo;
        String texto = null;
        try {

            File file = new File("src\\emulatecpu\\memory.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            ArrayList<String> txt = new ArrayList();
     
            for(int i = 0; i < stackPointer;i++){
                String v = reader.readLine();
                txt.add(v);
                //System.out.println(v);
            }    
            reader.close();
            
            return txt.get(address-1);
        } catch (Exception ex) {
            Logger.getLogger(AcessMemory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void setWord(int val, int address) {
        RandomAccessFile arquivo;
        try {
            AcessMemory.stackPointer = contaLinha();

            File file = new File("src\\emulatecpu\\memory.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            ArrayList<String> txt = new ArrayList();
     
            for(int i = 0; i < stackPointer;i++){
                String v = reader.readLine();
                txt.add(v);
                //System.out.println(v);
            }    
            reader.close();
            
            
            String numero = Integer.toBinaryString(val);
            int tam = 32 - numero.length();

            for (int i = 0; i < tam; i++) {
                numero = "0" + numero;
            }
            
            txt.set(address-1, numero);
            
            //System.out.println(address + "   " + numero);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            
            for (String string : txt) {
                writer.write(string);
                writer.newLine();
            }
            
            writer.close();
            

        } catch (Exception ex) {
            Logger.getLogger(AcessMemory.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String getProgram() {
        RandomAccessFile arquivo;
        String texto = null;
        try {
            URL url = AcessMemory.class.getResource("program.txt");
            arquivo = new RandomAccessFile(new File(url.toURI()), "r");

            arquivo.seek((CPU.registros[OpCodes.PC] * 34));
            CPU.registros[OpCodes.PC] += 1;

            texto = arquivo.readLine();
            if (texto == null) {
                return null;
            }

            if (texto.equalsIgnoreCase("")) {
                CPU.run = false;
            }

            arquivo.close();
            return texto;
        } catch (Exception ex) {
            Logger.getLogger(AcessMemory.class.getName()).log(Level.SEVERE, null, ex);

        }

        CPU.run = false;
        return null;
    }

    public static int contaLinha() {
        LineNumberReader lineCounter;
        try {
            lineCounter = new LineNumberReader(new InputStreamReader(new FileInputStream("src\\emulatecpu\\memory.txt")));

            String nextLine = null;

            while ((nextLine = lineCounter.readLine()) != null) {
                if (nextLine == null) {
                    break;
                }
                //System.out.println(nextLine);
            }
            //System.out.println("Total number of line in this file " + lineCounter.getLineNumber());
            return lineCounter.getLineNumber();
        } catch (Exception done) {
            done.printStackTrace();
        }
        return -1;
    }

}
