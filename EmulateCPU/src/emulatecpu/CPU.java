package emulatecpu;

public class CPU {

    // registros
    static int starting_address = 0;
    static boolean run = true;
    static String instruction;
    static int inst_type;
    static Integer[] registros = new Integer[32];

    public static void main(String args[]) {

        registros[OpCodes.PC] = starting_address;
        registros[OpCodes.ZERO] = 0;
        
        while(run){
            instruction = AcessMemory.getProgram();
            
            if(instruction == null || !instruction.equals("")) break;
            
            inst_type = getInstType(instruction);
            execute(instruction,inst_type);
        }
    }

    private static int getInstType(String inst) {
        String tipo = inst.substring(0, 6);
        return Integer.parseInt(tipo, 2);
    }

    private static void execute(String inst, int type) {
        if (type == OpCodes.TIPO_R) {

            int reg_A = Integer.parseInt(instruction.substring(6, 11), 2);
            int reg_B = Integer.parseInt(inst.substring(11, 16), 2);
            int reg_destino = Integer.parseInt(instruction.substring(16, 21), 2);
            int deslocamento = Integer.parseInt(instruction.substring(21, 26), 2);
            int funcao = Integer.parseInt(inst.substring(26, 32), 2);

            switch (funcao) {// Instruções do tipo R
                case OpCodes.SOMA:
                    soma(reg_A, reg_B, reg_destino, deslocamento);
                    break;
                case OpCodes.SUB:
                    subtracao(reg_A, reg_B, reg_destino, deslocamento);
                    break;
                case OpCodes.DIV:
                    divisao(reg_A, reg_B, reg_destino, deslocamento);
                    break;
                case OpCodes.MULT:
                    multiplicacao(reg_A, reg_B, reg_destino, deslocamento);
                    break;
            }
        } else {// Instruções do tipo I
            int reg_destino = Integer.parseInt(instruction.substring(6, 11), 2);
            int reg_origem = Integer.parseInt(inst.substring(11, 16), 2);
            int endereco = Integer.parseInt(inst.substring(16, 32), 2);

            switch (type) {
                case OpCodes.SAVE:
                    save(reg_destino, reg_origem, endereco);
                    break;
                case OpCodes.LOAD:
                    load(reg_destino, reg_origem, endereco);
                    break;
            }
        }
    }

    public static void soma(int reg_A, int reg_B, int reg_destino, int deslocamento){
        registros[reg_destino] = (int)registros[reg_A] + registros[reg_B];
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
