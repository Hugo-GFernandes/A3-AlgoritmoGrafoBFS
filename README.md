# Projeto Base Java — Visualizador de Grafos e Algoritmos

Este projeto é uma casca em Java/Swing para trabalhos práticos de grafos.

Os alunos devem implementar um dos algoritmos:

1. BFS — Busca em Largura
2. DFS — Busca em Profundidade
3. Dijkstra — Menor caminho com pesos não negativos
4. Bellman-Ford — Menor caminho com pesos negativos e detecção de ciclo negativo

## Características

- Interface gráfica desktop em Java Swing
- Não depende de Maven, Gradle, JavaFX, pandas, numpy ou bibliotecas externas
- Criação dinâmica de vértices e arestas
- Grafo direcionado ou não direcionado
- Arestas ponderadas
- Visualização gráfica do grafo
- Visualização da lista de adjacência
- Visualização da matriz de adjacência
- Importação/exportação em JSON simples
- Contrato pronto para os alunos implementarem algoritmos

## Requisitos

- Java JDK 21 ou superior

## Como compilar e executar no macOS/Linux

Na raiz do projeto:

```bash
mkdir -p out
find src/main/java -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out br.edu.mca.grafos.App
```

## Como compilar e executar no Windows PowerShell

```powershell
mkdir out
Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object { $_.FullName } > sources.txt
javac -d out @sources.txt
java -cp out br.edu.mca.grafos.App
```

## Onde implementar os algoritmos

Cada grupo deve implementar apenas um arquivo:

```text
src/main/java/br/edu/mca/grafos/algorithms/
```

Arquivos:

- BfsAlgorithm.java
- DfsAlgorithm.java
- DijkstraAlgorithm.java
- BellmanFordAlgorithm.java

Cada algoritmo deve implementar o método:

```java
AlgorithmResult run(Graph graph, String source, String target);
```

## Formato JSON para importar grafos

```json
{
  "directed": false,
  "nodes": ["A", "B", "C"],
  "edges": [
    {"source": "A", "target": "B", "weight": 2},
    {"source": "B", "target": "C", "weight": 5}
  ]
}
```
