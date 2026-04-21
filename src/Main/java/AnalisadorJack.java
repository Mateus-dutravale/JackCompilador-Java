import java.io.*;
import java.util.*;

public class AnalisadorJack {

    public static void main(String[] args) {
        String caminho = (args.length > 0) ? args[0] : ".";

        File entrada = new File(caminho);

        // Verifica se o caminho existe
        if (!entrada.exists()) {
            System.out.println("Erro: O caminho '" + entrada.getAbsolutePath() + "' nao existe.");
            return;
        }

        List<File> arquivosJack = new ArrayList<>();

        // Lógica para identificar se é um arquivo único ou uma pasta
        if (entrada.isFile() && entrada.getName().endsWith(".jack")) {
            arquivosJack.add(entrada);
        } else if (entrada.isDirectory()) {
            File[] arquivos = entrada.listFiles((dir, nome) -> nome.endsWith(".jack"));
            if (arquivos != null) arquivosJack.addAll(Arrays.asList(arquivos));
        }

        for (File f : arquivosJack) {
            processarArquivo(f);
        }
    }

    private static void processarArquivo(File arquivoEntrada) {
        // Agora o nome de saída será .xml (sem o T), conforme pedido pelo professor
        String nomeSaida = arquivoEntrada.getAbsolutePath().replace(".jack", "P.xml");

        try {
            // Criamos o seu motor de compilação em português
            MecanismoCompilacao motor = new MecanismoCompilacao(arquivoEntrada, new File(nomeSaida));

            // Chamamos a função inicial que você criou (Passo 3)
            System.out.println("Compilando estrutura sintática: " + arquivoEntrada.getName());
            motor.compilarClasse();

            // Fecha o arquivo para salvar as alterações
            motor.fechar();

            System.out.println("Arquivo XML gerado com sucesso: " + nomeSaida);

        } catch (IOException e) {
            System.err.println("Erro ao processar o analisador sintático: " + e.getMessage());
        }
    }
}