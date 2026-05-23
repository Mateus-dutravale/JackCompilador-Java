package Main.java;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class LeitorLexicoJack {
    private List<Token> tokens;
    private int indiceTokenAtual;

    private static final Set<String> PALAVRAS_CHAVE = new HashSet<>(Arrays.asList(
            "class", "constructor", "function", "method", "field", "static", "var",
            "int", "char", "boolean", "void", "true", "false", "null", "this",
            "let", "do", "if", "else", "while", "return"
    ));

    private static final String SIMBOLOS = "{}()[].,;+-*/&|<>=~";

    public LeitorLexicoJack(File arquivoEntrada) throws IOException {
        this.tokens = new ArrayList<>();
        this.indiceTokenAtual = -1;
        tokenizar(arquivoEntrada);
    }

    private void tokenizar(File arquivo) throws IOException {
        String regex = "\"[^\"\\n]*\"|[\\{\\}\\(\\)\\[\\]\\.,;+\\-\\*/&\\|<>=~]|\\d+|[\\w_]+";
        Pattern pattern = Pattern.compile(regex);

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            int numeroLinha = 1;
            boolean emComentarioBloco = false;

            while ((linha = br.readLine()) != null) {
                // Remove comentários de linha (//)
                int posComentarioLinha = linha.indexOf("//");
                if (posComentarioLinha != -1) {
                    linha = linha.substring(0, posComentarioLinha);
                }

                // Lida com comentários de bloco (/* ... */)
                linha = linha.replaceAll("/\\*.*?\\*/", " "); // Na mesma linha

                if (emComentarioBloco) {
                    if (linha.contains("*/")) {
                        linha = linha.substring(linha.indexOf("*/") + 2);
                        emComentarioBloco = false;
                    } else {
                        numeroLinha++;
                        continue;
                    }
                }
                if (linha.contains("/*")) {
                    emComentarioBloco = true;
                    linha = linha.substring(0, linha.indexOf("/*"));
                }

                // Extrai os tokens da linha limpa
                Matcher matcher = pattern.matcher(linha);
                while (matcher.find()) {
                    String lexema = matcher.group();
                    TokenType tipo = identificarTipo(lexema);
                    tokens.add(new Token(tipo, lexema, numeroLinha));
                }
                numeroLinha++;
            }
        }
    }

    private TokenType identificarTipo(String t) {
        if (PALAVRAS_CHAVE.contains(t)) return TokenType.KEYWORD;
        if (t.length() == 1 && SIMBOLOS.contains(t)) return TokenType.SYMBOL;
        if (t.startsWith("\"")) return TokenType.STRING_CONSTANT;
        if (Character.isDigit(t.charAt(0))) return TokenType.INTEGER_CONSTANT;
        return TokenType.IDENTIFIER;
    }

    public boolean temMaisTokens() {
        return indiceTokenAtual < tokens.size() - 1;
    }

    public void avancar() {
        if (temMaisTokens()) {
            indiceTokenAtual++;
        }
    }

    // Retorna o objeto Token completo (com linha, tipo e lexema)
    public Token tokenAtual() {
        return tokens.get(indiceTokenAtual);
    }

    // Métodos de atalho para não quebrar o MecanismoCompilacao imediatamente
    public String obterLexema() {
        return tokenAtual().getLexeme();
    }
}