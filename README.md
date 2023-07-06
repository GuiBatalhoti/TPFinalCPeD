# TPFinalCPeD

Trabalho Final da Disciplina de Computação Paralela e Distribuída. 

O objetivo do projeto era implementar um "Compute Engine", implementando três diferentes serviços.

Os serviços implementados foram:

1. PDI - Encontrando bordas de uma imagem com uma máscara Laplaciano da Gaussiana;
2. Criptografia - Criptografar um texto passado dentro de um arquivo de texto;
3. Análise de Dados - Calcular a média e desvio padrão do dataset e retornar um gráfico;

## PDI
A implementação feita recebe o caminho até o local da imagem (PNG) como terceiro argumento do programa. O programa irá retornar uma imagem com as bordas encontradas. E na sequência será salvo em outra imagem.

## Criptografia
A implementação feita recebe o caminho até o local do arquivo de texto como quarto argumento do programa. O programa irá retornar o texto criptografado. E na sequência será salvo em outro arquivo.

## Análise de Dados
A implementação feita recebe o caminho até o local do dataset como quinto argumento do programa. O programa irá retornar um gráfico com a média e desvio padrão do dataset. E na sequência será salvo em outro arquivo.

>O dataset é uma modificação do seguinte [link](https://www.kaggle.com/datasets/michals22/coffee-dataset), com a modificação de retirar algumas linhas. 

## Como executar
Para executar o projeto, são necessários três terminais abertos no diretório do projeto.

Primeiro é necessário exportar o "classpath" do projeto. Para isso, execute o comando abaixo:

```bash
export CLASSPATH=$CLASSPATH:<diretório do projeto>
```

Para compilar o projeto, execute o comando abaixo:

```bash
javac compute/*.java
jar cvf classes/compute.jar compute/*.class
javac -cp ./classes/compute.jar:./lib/jcommon-1.0.23.jar:./lib/jfreechart-1.5.4.jar engine/ComputeEngine.java
javac -cp ./classes/compute.jar:./lib/jcommon-1.0.23.jar:./lib/jfreechart-1.5.4.jar client/ComputeCode.java client/DIP.java client/Cryptography.java client/DataAnalysis.java
```

Para executar o projeto, execute os comandos abaixo em cada terminal:

1. Terminal do rmi registry:
```bash
rmiregistry
```

2. Terminal do servidor:
```bash
java -cp "$(pwd)":"$(pwd)/classes/compute.jar":"$(pwd)/lib/jcommon-1.0.23.jar":"$(pwd)/lib/jfreechart-1.5.4.jar" -Djava.rmi.server.hostname=localhost -Djava.security.policy=server.policy -Djava.rmi.server.codebase=file:"$(pwd)/classes/compute.jar" engine.ComputeEngine localhost 1099
```

3. Terminal do cliente:
```bash
java -cp "$(pwd)":"$(pwd)/classes/compute.jar":"$(pwd)/lib/jcommon-1.0.23.jar":"$(pwd)/lib/jfreechart-1.5.4.jar" -Djava.rmi.server.codebase=file:"$(pwd)/classes" -Djava.security.policy=client.policy client.ComputeCode localhost 1099 "inputs/image.png" "inputs/text.txt" "inputs/Coffee_domestic_consumption.csv"
```

># Observações
>## Inputs
>Todos os inputs estão na diretório "inputs" do projeto. Assim, todos os outputs serão salvos na diretório "outputs".
>Caso haja mudança nos inputs da imagem para PDI e do arquivo de texto para Criptografia, não havarão problemas de execução. Porém, no caso do dataset, a leitura do CSV não funcionará e será necessário mudar o código para que o dataset seja lido corretamente.
>## lib
>Os arquivos .jar estão na diretório "lib" do projeto. São utilizados apenas para a Análise de Dados.
>## Sistema Operacional 
> O projeto foi desenvolvido utilizando o Java 11. E sistema operacional Ubuntu 22.04 LTS. 