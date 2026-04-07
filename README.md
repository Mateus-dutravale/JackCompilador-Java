# 🏛️ Analisador Léxico - Linguagem Jack (Nand2Tetris)

Projeto desenvolvido para a disciplina de **Compiladores** - Universidade Federal do Maranhão (UFMA).

## 👥 Integrantes e Matrículas
* **Gabryella Cruz Sousa** - Matrícula: 20250013701
* **Mateus Dutra Vale** - Matrícula: 20250071302

## 💻 Linguagem e Tecnologias
* **Linguagem:** Java
* **Ferramentas:** IntelliJ IDEA / Git
* **Projeto Base:** Projeto 10 do curso Nand2Tetris

---

## 🚀 Instruções para Compilar e Executar

### 1. Pré-requisitos
* Java JDK instalado (versão 11 ou superior recomendada).
* Git para clonagem do repositório.

### 2. Compilação
No terminal, dentro do diretório com os arquivos `.java`, execute: 
```bash
javac AnalisadorJack.java
``` 

### 3. Execução Manual
   O analisador aceita tanto um arquivo individual quanto um diretório completo:

```Bash
# Processar um único arquivo
java AnalisadorJack caminho/para/seu/Arquivo.jack

# Processar todos os arquivos .jack de uma pasta
java AnalisadorJack caminho/para/pasta/
```
O programa gerará arquivos *T.xml contendo os tokens identificados no mesmo diretório de origem.

## ✅ Validação e Testes
Para facilitar a correção e garantir que a saída está rigorosamente dentro do padrão exigido pelo curso, utilizamos um script de automação.

Utilizando o testa_tudo.bat
Este arquivo é o validador oficial do nosso projeto. Ele automatiza o processo de compilação, execução e comparação de resultados.

No Windows, execute o arquivo diretamente:
```
testa_tudo.bat
```
O script irá:

* Compilar as classes Java.

* Rodar o AnalisadorJack sobre as pastas de teste padrão.

* Comparar os arquivos gerados com os arquivos de referência (comparação de strings).

## 📂 Estrutura Principal
* AnalisadorJack.java: Classe driver que gerencia os arquivos de entrada e saída.

* LeitorLexicoJack.java: Implementação da lógica de tokenização.

* testa_tudo.bat: Script de automação e validação.