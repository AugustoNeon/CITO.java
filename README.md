# CITO - Médicos e Pacientes

Trabalho de Programação Orientada a Objetos (RA2 e RA3).

Aplicação que gerencia médicos, pacientes e consultas. É formada por dois programas:

- **P1** (`cito.app.ProgramaP1`): lê os arquivos `.csv` da pasta `dados`, cria os objetos e salva tudo em formato binário (`dados/base.dat`).
- **P2** (`cito.app.ProgramaP2`): lê o arquivo binário, restaura os objetos em memória e abre uma interface gráfica (Swing) com duas abas (interface do médico e interface do paciente) para fazer as pesquisas. Os resultados são gravados na pasta `resultados`.

## Como executar

É necessário ter o Java instalado (JDK 17 ou superior).

### Jeito mais simples
Dê um duplo-clique em **`EXECUTAR.bat`**. Ele compila (se ainda não estiver compilado), gera a base com o P1 (se ainda não existir) e abre a interface gráfica.

### Rodando os programas separadamente
1. `compilar.bat` — compila o código para a pasta `bin`
2. `rodar_p1.bat` — roda o P1 e gera `dados/base.dat`
3. `rodar_p2.bat` — roda o P2 (interface gráfica)

### Pela linha de comando
```
javac -encoding UTF-8 -d bin -sourcepath src src\cito\app\ProgramaP1.java src\cito\app\ProgramaP2.java
java -cp bin cito.app.ProgramaP1
java -cp bin cito.app.ProgramaP2
```

## Pastas

- `src/` — código fonte (pacotes `cito.modelo`, `cito.excecoes`, `cito.persistencia`, `cito.servico`, `cito.app`, `cito.gui`)
- `dados/` — arquivos de entrada (`medicos.csv`, `pacientes.csv`, `consultas.csv`) e o binário gerado (`base.dat`)
- `resultados/` — arquivos de saída gerados pela execução
- `bin/` — classes compiladas (gerada automaticamente; não vai para o repositório)

## Arquivos de entrada

| Arquivo | Formato |
|---|---|
| `medicos.csv` | `codigo;nome` |
| `pacientes.csv` | `cpf;nome` |
| `consultas.csv` | `data;horario;codigoMedico;cpfPaciente` |

O `pacientes.csv` tem de propósito duas linhas inválidas (um CPF com dígito verificador errado e uma linha sem o nome) para mostrar o tratamento de erros. Esses cadastros são recusados e listados em `resultados/cadastros_rejeitados.txt`.

## Arquivos de saída

| Arquivo | Conteúdo |
|---|---|
| `cadastros_rejeitados.txt` | cadastros recusados na carga (gerado pelo P1) |
| `resultados_parciais.txt` | resultado de cada pesquisa, com data e hora |
| `resultados_finais.txt` | resumo do fim da sessão |

O arquivo `MAPEAMENTO_REQUISITOS.txt` mostra onde cada item das fichas de avaliação RA2 e RA3 está atendido no código.
