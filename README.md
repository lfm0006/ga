# Project GENALG
Project GENALG - Classes and examples in Java for Genetic Algorithms

This project contains an experimental Java classes set for implementing Genetic Algorithms examples, classical approach and permutation. I’ve been used it in my AI classes over the last two years. The central idea was to stress the GA using many variables and nodes in the examples, checking out the results from many runs and evaluating the trade-off between time and generations. Further benchmarking with other implementations are foreseen.

Core code is divided as follows:
- GenAlg.java: class for traditional GA, according Goldberg’s book.
- GenAlgOrder.java:  class for permutation GA.
- Utility.java: auxiliary class for printing the results.
- IFunctionFitness.java: interface for implementing custom objective functions classes.
- Graph.java: class for automatic generating of circle graphs, useful for testing permutation GA.

Classical GA examples are:
- Example1.java: 2-variables equation optimization.
- Example2.java: 5-variables equation optimization.
- Example3.java: 10-variables equation optimization.

Permutation GA examples are:
- ExOrder1.java: 8-nodes graph, 20 individuals, simple run, static graph.
- ExOrder2.java: 8-nodes graph, 60 individuals, simple run, static graph.
- ExOrder3.java: 8-nodes graph, 30 individuals, 1000 runs, text file output, static graph.
- ExOrder4.java: 15-nodes graph, 30 individuals, 1000 runs, text file output, static graph.
- ExOrder5.java: 20-nodes graph, 30 individuals, 1000 runs, text file output, static graph.
- ExOrder6.java: 5-nodes up to 20-nodes graph, 30 individuals, 1000 runs, text file output, static graph.
- ExOrder7.java: 5-nodes up to 30-nodes graph, 30 individuals, 1000 runs, text file output, static graph.
- ExOrder8.java: 5-nodes up to 40-nodes graph, 30 individuals, 1000 runs, text file output, automatically generated graph.

Please, in using this work or publishing papers, don’t forget to cite in the references. Any help or suggestion is welcome.

Luciano F. de Medeiros, Dr.

http://lattes.cnpq.br/9300962438570183
