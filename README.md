# Árvore binária de busca
Uma árvore binária de busca implementando o algoritmo AVL em Visual Basic.
Cada diretório contem a implementação em uma linguagem diferente e um README explicando como executar a demonstração.

## Funcionamento
### Árvore binária de busca
É uma estrutura de dados que permite encontrar rapidamente um elemento por meio de uma busca binária. O primeiro item inserido na árvore se torna sua raiz. 
Novos itens são adicionados de forma que o filho da esquerda é sempre menor do que o pai e o da direita é sempre maior do que o pai, de acordo com a forma que são ordenados. Em uma árvore binária sempre há 0, 1 ou 2 filhos. 
Para exemplificar podemos usar números inteiros, mas as implementações desse repositório são genéricas para qualquer tipo de dado que possa ser ordenado.
```
      21
  11      88
09  13  75  99
```
11 é filho à esquerda de 21 por ser menor e 88 é filho à direita por ser maior. Para encontrar o número 75 um algoritmo poderia iniciar verificando que 75 é maior do que a raiz, descendo ao filho da direita e verificando que 75 é menor do que 88, sem precisar passar pela maioria dos itens da árvore.
### Algoritmo AVL
Ao inserir ou remover itens é possível que uma parte da árvore se torne mais profunda do que a outra, o que a torna desbalanceada. Isso pode torna a busca menos eficiente. Para resolver esse problema o Algoritimo AVL, criado por Adelson-Velsky e Landis, realiza rotações entre os nós da árvore sempre que a diferença entre a subárvore da esquerda e a subárvore da direita de qualquer nó é maior que 1 ou menor que -1.
O fator de balanço é definido como altura da subárvore direita - altura da subárvore esquerda.
Abaixo, x significa um valor que não tem importância para essa rotação.
#### Rotação à esquerda
É executada se um nó possui fator de balanço maior que 1, ou seja, a subárvore da direita é maior do que da esquerda com uma diferença de mais de 1 nível.

Se o filho da direita, B, do nó desbalanceado tem fator de balanço igual ou maior a zero, é executada a rotação à esquerda simples.
```
      A                 B
  x       B    =>   A       C
x   x   x   C
```
Se B, o filho da direita do nó desbalanceado, possui fator de balanço menor que zero, antes dessa rotação à esquerda é preciso realizar uma rotação à direita em B.
```
      A                 A
  x       C    =>   x       B
x   x   B   x     x   x   x   C
```
E então é realizada a rotação à esquerda resultando em:
```
    B
A       C
```
#### Rotação à direita
É executada se um nó possui fator de balanço menor que -1, ou seja, a subárvore da esquerda é maior do que a da direita com uma diferença de mais de 1 nível.
 
Se o filho da esquerda, B, do nó desbalanceado tem fator de balanço igual ou menor do que zero, é executada a rotação à direita simples.
```
      C                 B
  B       x    =>   A       C
A   x   x   x
```
Se B, o filho da esquerda do nó desbalanceado, possui fator de balanço maior que 0, antes dessa rotação à direita é preciso realizar uma rotação à esquerda em B.
```
      C                 C
  A       x    =>   B       x
x   B   x   x     A   x   x   x
```
E então é realizada a rotação à direita resultando em:
```
    B
A       C
```

## Implementações
Abaixo estão os links para os READMEs de cada linguagem:
* [Visual Basic](visual-basic/README.md)
