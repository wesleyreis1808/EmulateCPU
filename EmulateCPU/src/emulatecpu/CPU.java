package emulatecpu;

import static emulatecpu.CPU.data;
import java.util.HashMap;

public class CPU {

    // registros
    static int starting_address = 0;
    static boolean run = true;
    static String instruction;
    static Data data = new Data();
    static int inst_type;
    static Integer[] registros = new Integer[32];
    static HashMap<Integer,Word> cache= new HashMap<>();

    public static void main(String args[]) {

        registros[OpCodes.PC] = starting_address;
        registros[OpCodes.ZERO] = 0;
        
        while(run){
            instruction = AcessMemory.getProgram();
            
            if(instruction == null) break;
            
            inst_type = getInstType(instruction);
            data = findData(instruction,data,inst_type);
            execute(instruction,inst_type,data);
        }
    }

    private static int getInstType(String inst) {
        String tipo = inst.substring(0, 6);
        return Integer.parseInt(tipo, 2);
    }
    
    private static Data findData(String inst,Data data,int type){
        if(type == OpCodes.TIPO_R){
            data.reg_A = Integer.parseInt(instruction.substring(6, 11), 2);
            data.reg_B = Integer.parseInt(inst.substring(11, 16), 2);
            data.reg_destino = Integer.parseInt(instruction.substring(16, 21), 2);
            data.deslocamento = Integer.parseInt(instruction.substring(21, 26), 2);
            data.funcao = Integer.parseInt(inst.substring(26, 32), 2);
        }else{
            data.reg_destino = Integer.parseInt(instruction.substring(6, 11), 2);
            data.reg_origem = Integer.parseInt(inst.substring(11, 16), 2);
            data.endereco = Integer.parseInt(inst.substring(16, 32), 2);
            data.enderecoS = inst.substring(16, 32);
        }
            
        return data;
    }

    private static void execute(String inst, int type, Data data) {
        if (type == OpCodes.TIPO_R) {

            switch (data.funcao) {// Instruções do tipo R
                case OpCodes.SOMA:
                    soma(data.reg_A, data.reg_B, data.reg_destino, data.deslocamento);
                    System.out.printf("SOMA entre registrador %d e %d salvo no %d\n",data.reg_A, data.reg_B,data.reg_destino);
                    break;
                case OpCodes.SUB:
                    subtracao(data.reg_A, data.reg_B, data.reg_destino, data.deslocamento);
                    System.out.printf("SUB entre registrador %d e %d salvo no %d\n",data.reg_A, data.reg_B,data.reg_destino);
                    break;
                case OpCodes.DIV:
                    divisao(data.reg_A, data.reg_B, data.reg_destino, data.deslocamento);
                    System.out.printf("DIV entre registrador %d e %d salvo no %d\n",data.reg_A, data.reg_B,data.reg_destino);
                    break;
                case OpCodes.MULT:
                    multiplicacao(data.reg_A, data.reg_B, data.reg_destino, data.deslocamento);
                    System.out.printf("MULT entre registrador %d e %d salvo no %d\n",data.reg_A, data.reg_B,data.reg_destino);
                    break;
            }
        } else {// Instruções do tipo I
            switch (type) {
                case OpCodes.SAVE:
                    save(data.reg_destino, data.reg_origem, data.endereco);
                    System.out.printf("SAVE regitrador %d no endereço %d\n",data.reg_origem, data.endereco);
                    break;
                case OpCodes.LOAD:
                    if(!verificaCache(cache, data.reg_destino, data.enderecoS)){
                        load(data.reg_destino, data.reg_origem, data.endereco);
                        System.out.println("Miss");
                    }else{
                        System.out.println("Hit");
                    }
                    System.out.printf("LOAD para o regitrador %d do endereço %d\n",data.reg_destino, data.endereco);
                    break;
            }
        }
    }
    
    public static boolean verificaCache(HashMap cache,int reg_destino,String endereco){
        
        int tag = Integer.parseInt(endereco.substring(0, 24));
        int indice = Integer.parseInt(endereco.substring(24, 31));
        int palavra = Integer.parseInt(endereco.substring(31, 32));
        
        Word word;
        Integer dados;
        
       if(cache.isEmpty()) return false; // miss
       else{
           word = (Word) cache.get(indice);
           if(word != null){ // hit
               dados = verificaWord(word,tag,palavra);
               if(dados != null){
                   registros[reg_destino] = dados;
                   return true;
               }
           }
       }
        return false;
    }
    
    public static Integer verificaWord(Word word,int tag, int palavra){
        if(tag == Integer.parseInt(word.getTag())){
            if(palavra == 0){
                return Integer.parseInt(word.getPalavra1());
            }
            else{
                return Integer.parseInt(word.getPalavra2());
            }
        }
        
        return null;
    }
    

    public static void soma(int reg_A, int reg_B, int reg_destino, int deslocamento){
        registros[data.reg_destino] = (int)registros[reg_A] + registros[reg_B];
    }
    
    public static void subtracao(int reg_A, int reg_B, int reg_destino, int deslocamento){
        registros[reg_destino] = (int)registros[reg_A] - registros[reg_B];
    }
    public static void divisao(int reg_A, int reg_B, int reg_destino, int deslocamento){
        registros[reg_destino] = (int)registros[reg_A] / registros[reg_B];
    }
    public static void multiplicacao(int reg_A, int reg_B, int reg_destino, int deslocamento){
        registros[reg_destino] = (int)registros[reg_A] * registros[reg_B];
    }
    
    public static void load(int reg_destino, int reg_origem, int endereco) {
        int valor = Integer.parseInt(AcessMemory.getWord(endereco).substring(0 + reg_origem, 32 + reg_origem), 2);
        registros[reg_destino] = valor;
    }
    
    public static void save(int reg_destino, int reg_origem, int endereco){
        AcessMemory.setWord(registros[reg_origem]);
    }
    


}
