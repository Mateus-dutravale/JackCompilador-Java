/// ///////////////////////////////////////////////Blibliotecas///////////////////////////////////////////////
import java.io.*;
import java.util.*;
import java.util.regex.*;

/// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class LeitorLexicoJack {
    private String conteudo;
    private List<String> tokens;
    private int indiceTokenAtual;

    /// ///////////////////////////////////////////////Dicionário Jack///////////////////////////////////////////
    private static final Set<String> PALAVRAS_CHAVE = new HashSet<>(Arrays.asList(
            "class", "constructor", "function", "method", "field", "static", "var",
            "int", "char", "boolean", "void", "true", "false", "null", "this",
            "let", "do", "if", "else", "while", "return"
    ));

    private static final String SIMBOLOS = "{}()[].,;+-*/&|<>=~";

    /// ///////////////////////////////////////////////Construtor///////////////////////////////////////////////
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
        String regex = "\"[^\"\\n]*\"|[\\{\\}\\(\\)\\[\\]\\.,;+\\-\\*/&\\|<>=~]|\\d+|[\\w_]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.conteudo);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }
    }

    /// ///////////////////////////////////////////////Navegação e Tipos/////////////////////////////////////////
    public boolean temMaisTokens() {
        return indiceTokenAtual < tokens.size() - 1;
    }

    public void avancar() {
        if (temMaisTokens()) {
            indiceTokenAtual++;
        }
    }

    public String obterToken() {
        return tokens.get(indiceTokenAtual);
    }

    public String tipoToken() {
        String t = obterToken();

        if (PALAVRAS_CHAVE.contains(t)) return "keyword";
        if (t.length() == 1 && SIMBOLOS.contains(t)) return "symbol";
        if (t.startsWith("\"")) return "stringConstant";
        if (Character.isDigit(t.charAt(0))) return "integerConstant";
        return "identifier";
    }
}