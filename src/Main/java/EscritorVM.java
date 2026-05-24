package Main.java;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class EscritorVM {

    private PrintWriter escritor;

    public EscritorVM(File arquivoSaida) throws IOException {
        this.escritor = new PrintWriter(arquivoSaida);
    }

    public void escreverPush(String segmento, int indice) {
        escritor.println("push " + segmento + " " + indice);
    }

    public void escreverPop(String segmento, int indice) {
        escritor.println("pop " + segmento + " " + indice);
    }

    public void escreverAritmetica(String comando) {
        escritor.println(comando);
    }

    public void escreverLabel(String label) {
        escritor.println("label " + label);
    }

    public void escreverGoto(String label) {
        escritor.println("goto " + label);
    }

    public void escreverIf(String label) {
        escritor.println("if-goto " + label);
    }

    public void escreverChamada(String nomeFuncao, int numeroArgumentos) {
        escritor.println("call " + nomeFuncao + " " + numeroArgumentos);
    }

    public void escreverFuncao(String nomeFuncao, int numeroVariaveisLocais) {
        escritor.println("function " + nomeFuncao + " " + numeroVariaveisLocais);
    }

    public void escreverRetorno() {
        escritor.println("return");
    }

    public void fechar() {
        escritor.close();
    }
}