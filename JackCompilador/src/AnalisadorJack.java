import java.io.*;
import java.util.*;

public class AnalisadorJack {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java AnalisadorJack <arquivo.jack ou diretorio>");
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
}