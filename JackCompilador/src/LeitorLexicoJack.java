/// ///////////////////////////////////////////////Blibliotecas///////////////////////////////////////////////
import java.io.*;
import java.util.*;
import java.util.regex.*;
/// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class LeitorLexicoJack {
    private String conteudo;
    private List<String> tokens;
    private int indiceTokenAtual;

    public LeitorLexicoJack(File arquivoEntrada) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (Scanner scanner = new Scanner(arquivoEntrada)) {
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append("\n");
            }
        }

        // Remove comentários logo na leitura para facilitar a tokenização
        this.conteudo = sb.toString().replaceAll("//.*|/\\*([\\s\\S]*?)\\*/", " ");

        this.tokens = new ArrayList<>();
        this.indiceTokenAtual = -1;
        tokenizar();
    }
    /// ///////////////////////////////////////////////Tokenização///////////////////////////////////////////////
    private void tokenizar() {
        // Esta regex separa: Strings entre aspas | Símbolos | Números | Palavras/ID
        String regex = "\"[^\"\\n]*\"|[\\{\\}\\(\\)\\[\\]\\.,;+\\-\\*/&\\|<>=~]|\\d+|[\\w_]+";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.conteudo);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }
    }
/// /////////////////////////////////////////////////////////////////////////////////////////////////////////////