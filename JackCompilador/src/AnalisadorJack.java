import java.io.*;
import java.util.*;

public class AnalisadorJack {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("C:\\Users\\cactu\\PROJETOS\\java\\JackCompilador-Java\\JackCompilador\\src\\test\\ArrayTest\\Main.jack");
            return;
        }

        File entrada = new File(args[0]);
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
        // Define o nome de saída Main.jack -> MainT.xml
        String nomeSaida = arquivoEntrada.getAbsolutePath().replace(".jack", "T.xml");

        try {
            LeitorLexicoJack leitor = new LeitorLexicoJack(arquivoEntrada);
            PrintWriter escritor = new PrintWriter(new FileWriter(nomeSaida));

            escritor.println("<tokens>");

            // LOOP de integração
            while (leitor.temMaisTokens()) {
                leitor.avancar();
                String tipo = leitor.tipoToken();
                String valor = leitor.obterToken();

                // Tratamento especial para Strings
                if (tipo.equals("stringConstant")) {
                    valor = valor.substring(1, valor.length() - 1);
                }

                // Escreve a linha no XML chamando a função de escapar
                escritor.println("<" + tipo + "> " + escapar(valor) + " </" + tipo + ">");
            }

            escritor.println("</tokens>");
            escritor.close();
            System.out.println("Arquivo gerado: " + nomeSaida);

        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private static String escapar(String valor) {
        return valor.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}