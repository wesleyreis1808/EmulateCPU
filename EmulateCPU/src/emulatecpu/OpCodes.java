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
public final class OpCodes {
    static final int ZERO = 0;    
    
    //Registros
    static final int PC = 1;                  // registro Program Counter, aponta para próxima instrução a ser executada
    static final int AC = 2;                  // acumulador, registro para efetuar a aritmetca
    static final int IR = 3;                // registro que guarda a instrução corrente IR
    static final int data_loc = 4;            // endereço dos dados, -1 sem endereço
    
    static final int S1 = 16;                  //
    static final int S2 = 17;                  //
    static final int S3 = 18;                  // registros para uso geral
    static final int S4 = 19;                  //
    static final int S5 = 20;                  //   
    
    static int TIPO_R = 0;
    
    //Operações Aritméticas 1~15
    static final int SOMA = 1;
    static final int SUB = 2;
    static final int MULT = 3;
    static final int DIV = 4;
    static final int MAIOR = 5;
    static final int MENOR = 6;
    
    //Operações Lógicas 16~32
    static final int OR = 16;
    static final int AND = 17;
    static final int NOT = 18;
    static final int XOR = 19;
    
    //Operações de transporte 32
    static final int LOAD = 32;
    static final int SAVE = 33;
    
    
}
