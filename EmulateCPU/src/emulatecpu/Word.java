/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emulatecpu;

/**
 *
 * @author WesleyReis
 */
public class Word {
    
    private String validacao;   //1
    private String palavra1;    //
    private String palavra2;    //
    private String tag;         //

    public Word(String instru) {
        
    }

    public String getValidacao() {
        return validacao;
    }

    public void setValidacao(String validacao) {
        this.validacao = validacao;
    }

    public String getPalavra1() {
        return palavra1;
    }

    public void setPalavra1(String palavra1) {
        this.palavra1 = palavra1;
    }

    public String getPalavra2() {
        return palavra2;
    }

    public void setPalavra2(String palavra2) {
        this.palavra2 = palavra2;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    
    
}
