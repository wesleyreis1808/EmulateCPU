package emulatecpu;

public class CPU {

    // registros
    static int starting_address = 0;
    static boolean run = true;
    static String instruction;
    static Data data = new Data();
    static int inst_type;
    static Integer[] registros = new Integer[32];

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
        }
            
        return data;
    }

    private static void execute(String inst, int type, Data data) {
        if (type == OpCodes.TIPO_R) {

            switch (data.funcao) {// Instruções do tipo R
                case OpCodes.SOMA:
                    soma(data.reg_A, data.reg_B, data.reg_destino, data.deslocamento);
                    break;
                case OpCodes.SUB:
                    subtracao(data.reg_A, data.reg_B, data.reg_destino, data.deslocamento);
                    break;
                case OpCodes.DIV:
                    divisao(data.reg_A, data.reg_B, data.reg_destino, data.deslocamento);
                    break;
                case OpCodes.MULT:
                    multiplicacao(data.reg_A, data.reg_B, data.reg_destino, data.deslocamento);
                    break;
            }
        } else {// Instruções do tipo I
            switch (type) {
                case OpCodes.SAVE:
                    save(data.reg_destino, data.reg_origem, data.endereco);
                    break;
                case OpCodes.LOAD:
                    load(data.reg_destino, data.reg_origem, data.endereco);
                    break;
            }
        }
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
