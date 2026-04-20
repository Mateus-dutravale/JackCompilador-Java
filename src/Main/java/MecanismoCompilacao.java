import java.io.*;

public class MecanismoCompilacao {
    private LeitorLexicoJack leitor;
    private PrintWriter escritor;
    private int nivelIdentacao = 0;

    /////////////////////////////////////////////////Construtor///////////////////////////////////////////////
    public MecanismoCompilacao(File entrada, File saida) throws IOException {
        this.leitor = new LeitorLexicoJack(entrada);
        this.escritor = new PrintWriter(saida);

        // Posiciona no primeiro token para iniciar a análise
        if (leitor.temMaisTokens()) {
            leitor.avancar();
        }
    }

    /////////////////////////////////////////////////Finalização///////////////////////////////////////////////
    public void fechar() {
        escritor.close();
    }
}