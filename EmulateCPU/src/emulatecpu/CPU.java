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
    static HashMap<Integer, Word> cache = new HashMap<>();

    public static void main(String args[]) {

        registros[OpCodes.PC] = starting_address;
        registros[OpCodes.ZERO] = 0;

        while (run) {
            instruction = AcessMemory.getProgram();

            if (instruction == null) {
                break;
            }

            inst_type = getInstType(instruction);
            data = findData(instruction, data, inst_type);
            execute(instruction, inst_type, data);
        }
    }

    private static int getInstType(String inst) {
        String tipo = inst.substring(0, 6);
        return Integer.parseInt(tipo, 2);
    }

    private static Data findData(String inst, Data data, int type) {
        if (type == OpCodes.TIPO_R) {
            data.reg_A = Integer.parseInt(instruction.substring(6, 11), 2);
            data.reg_B = Integer.parseInt(inst.substring(11, 16), 2);
            data.reg_destino = Integer.parseInt(instruction.substring(16, 21), 2);
            data.deslocamento = Integer.parseInt(instruction.substring(21, 26), 2);
            data.funcao = Integer.parseInt(inst.substring(26, 32), 2);
        } else {
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
                    System.out.printf("SOMA entre registrador %d e %d salvo no %d valor %d\n", data.reg_A, data.reg_B, data.reg_destino, registros[data.reg_destino]);
                    break;
                case OpCodes.SUB:
                    subtracao(data.reg_A, data.reg_B, data.reg_destino, data.deslocamento);
                    System.out.printf("SUB entre registrador %d e %d salvo no %d valor %d\n", data.reg_A, data.reg_B, data.reg_destino, registros[data.reg_destino]);
                    break;
                case OpCodes.DIV:
                    divisao(data.reg_A, data.reg_B, data.reg_destino, data.deslocamento);
                    System.out.printf("DIV entre registrador %d e %d salvo no %d valor %d\n", data.reg_A, data.reg_B, data.reg_destino, registros[data.reg_destino]);
                    break;
                case OpCodes.MULT:
                    multiplicacao(data.reg_A, data.reg_B, data.reg_destino, data.deslocamento);
                    System.out.printf("MULT entre registrador %d e %d salvo no %d valor %d\n", data.reg_A, data.reg_B, data.reg_destino, registros[data.reg_destino]);
                    break;
            }
        } else {// Instruções do tipo I
            switch (type) {
                case OpCodes.SAVE:
                    save(data.reg_destino, data.reg_origem, data.endereco, data.enderecoS);
                    System.out.printf("SAVE regitrador %d no endereço %d valor %d\n", data.reg_origem, data.endereco, CPU.registros[data.reg_origem]);
                    break;
                case OpCodes.LOAD:
                    if (!verificaCache(cache, data.reg_destino, data.enderecoS)) {
                        load(data.reg_destino, data.reg_origem, data.endereco, data.enderecoS);
                        System.out.println("Miss");
                        verificaCache(cache, data.reg_destino, data.enderecoS);
                    } else {
                        System.out.println("Hit");
                    }
                    System.out.printf("LOAD para o regitrador %d do endereço %d valor %d\n", data.reg_destino, data.endereco, CPU.registros[data.reg_destino]);
                    break;
            }
        }
    }

    public static boolean verificaCache(HashMap cache, int reg_destino, String endereco) {

        int tag = Integer.parseInt(endereco.substring(0, 8)); //
        int indice = Integer.parseInt(endereco.substring(8, 15)); // 
        int palavra = Integer.parseInt(endereco.substring(15, 16));// 1 bit

        Word word;
        Integer dados;

        if (cache.isEmpty()) {
            return false; // miss
        } else {
            word = (Word) cache.get(indice);
            if (word != null) { // hit
                if (word.isValidacao()) {
                    dados = verificaWord(word, tag, palavra);
                    if (dados != null) {
                        registros[reg_destino] = dados;
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static Integer verificaWord(Word word, int tag, int palavra) {
        if (tag == Integer.parseInt(word.getTag())) {
            if (palavra == 0) {
                return Integer.parseInt(word.getPalavra1(), 2);
            } else {
                return Integer.parseInt(word.getPalavra2(), 2);
            }
        }

        return null;
    }

    public static void soma(int reg_A, int reg_B, int reg_destino, int deslocamento) {
        registros[data.reg_destino] = (int) registros[reg_A] + registros[reg_B];
    }

    public static void subtracao(int reg_A, int reg_B, int reg_destino, int deslocamento) {
        registros[data.reg_destino] = (int) registros[reg_A] - registros[reg_B];
    }

    public static void divisao(int reg_A, int reg_B, int reg_destino, int deslocamento) {
        registros[data.reg_destino] = (int) registros[reg_A] / registros[reg_B];
    }

    public static void multiplicacao(int reg_A, int reg_B, int reg_destino, int deslocamento) {
        registros[data.reg_destino] = (int) registros[reg_A] * registros[reg_B];
    }

    public static void load(int reg_destino, int reg_origem, int endereco, String enderecoS) {

        int tag = Integer.parseInt(enderecoS.substring(0, 8)); //
        int indice = Integer.parseInt(enderecoS.substring(8, 15)); // 
        if (endereco % 2 == 0) {
            int palavra1 = Integer.parseInt(AcessMemory.getWord(endereco), 2);
            String p2 = AcessMemory.getWord(endereco + 1);
            int palavra2;
            if (p2 != null) {
                palavra2 = Integer.parseInt(p2, 2);
            } else {
                palavra2 = 0;
            }
            Word word = new Word(tag, palavra1, palavra2);
            CPU.cache.put(indice, word);

        } else {
            int palavra1 = Integer.parseInt(AcessMemory.getWord(endereco - 1), 2);
            int palavra2 = Integer.parseInt(AcessMemory.getWord(endereco), 2);
            Word word = new Word(tag, palavra1, palavra2);
            CPU.cache.put(indice, word);
        }
        //int valor = Integer.parseInt(AcessMemory.getWord(endereco).substring(0 + reg_origem, 32 + reg_origem), 2);
        //registros[reg_destino] = valor;
    }

    public static void save(int reg_destino, int reg_origem, int endereco, String enderecoS) {

        int indice = Integer.parseInt(enderecoS.substring(8, 15)); // 

        Word word;
        Integer dados;

        if (!cache.isEmpty()) {
            word = (Word) cache.get(indice);
            if (word != null) { // hit
                word.setValidacao(false);
                 System.out.println("Invalida Dado");
            }
        }

        AcessMemory.setWord(registros[reg_origem], endereco);

    }

}
