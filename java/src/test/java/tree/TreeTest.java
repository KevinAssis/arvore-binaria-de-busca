package tree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para a classe Arvore utilizando Integer.
 */
class TreeTest {
    private final int size = 100;
    private final int max = 10000;
    private final ArrayList<Integer> numbers = Demo.randomArray(size, max);

    // Métodos para auxiliar os testes.

    /**
     * Executa Acao em todos os nós abaixo de no, começando pelas folhas e subindo.
     * @param action O que deve ser executado em cada nó.
     * @param node Nó raiz da subárvore.
     */
    private void executeBottomTop(Consumer<Node<Integer>> action, Node<Integer> node) {

        // Primeiro nos filhos.
        if (!node.isEmpty()) {

            if (!node.getLeft().isEmpty()) {
                executeBottomTop(action, node.getLeft());
            }

            if (!node.getRight().isEmpty()) {
                executeBottomTop(action, node.getRight());
            }

        }

        // Depois no próprio nó
        action.accept(node);

    }

    // Balanceamento

    /**
     * Verifica o balanceamento de um nó.
     * @param node Nó que vai ser analisado.
     */
    private void balanceTest(Node<Integer> node) {

        if (!node.isEmpty()) {
            node.getLeft().updadeHeight();
            node.getRight().updadeHeight();
        }

        int balanceFactor = node.getBalanceFactor();

        // Uma árvore balanceada tem fatores de balanço entre 1 e -1;
        assertTrue(balanceFactor <= 1);
        assertTrue(balanceFactor >= -1);
    }

    /**
     * Verifica se a árvore se mantém balanceada após várias inserções e remoções.
     */
    @Test
    public void balanceAfterInsert() {

        Tree<Integer> tree =  new Tree<>();

        // Insere os itens da lista de números e verifica o balanceamento a cada inserção.
        numbers.forEach(n -> {
            tree.insert(n);
            executeBottomTop(this::balanceTest, tree.getRoot());
        });

        Collections.shuffle(numbers);

        // Remove todos os itens da lista e verifica o balanceamento a cada remoção.
        numbers.forEach(n -> {
            tree.remove(n);
            executeBottomTop(this::balanceTest, tree.getRoot());
        });

    }

    // Altura da árvore

    /**
     * Método recursivo que calcula a altura deste nó por percorrer todos os seus decendentes.
     * A fórmula usada é a mesma da classe Arvore, porém, é calculada no momento do teste
     * para verificar se as alturas armazenadas nos nós estão corretas.
     * @param node Nó cuja altura deve ser calculada.
     * @return Máximo entre as alturas dos nós filhos + 1.
     */
    private int calculateNodeHeight(Node<Integer> node) {
        if (node.isEmpty()) { return 0; }
        return Math.max(
                calculateNodeHeight(node.getLeft()),
                calculateNodeHeight(node.getRight())
        ) + 1;
    }

    /**
     * Compara a altura calculada no momento do teste com a
     * altura calculada quando a àrvore foi modificada.
     */
    private void nodeHeigthTest(Node<Integer> node) {
        assertEquals(node.getHeight(), calculateNodeHeight(node));
    }

    /**
     * Verifica se a árvore calcula a altura de cada nó corretamente a cada alteração.
     */
    @Test
    public void treeHeigthTest() {

        Tree<Integer> tree = new Tree<>();

        numbers.forEach(n -> {
            tree.insert(n);
            executeBottomTop(this::nodeHeigthTest, tree.getRoot());
        });

        Collections.shuffle(numbers);

        numbers.forEach(n -> {
            tree.remove(n);
            executeBottomTop(this::nodeHeigthTest, tree.getRoot());
        });

    }

    // Contagem

    /**
     * Verifica se a variável contagem da árvore é calculada corretamente.
     */
    @Test
    public void sizeFieldTest() {

        Tree<Integer> tree = new Tree<>();
        int expectedSize = 0;

        for (int n : numbers) {
            tree.insert(n);
            expectedSize++;
            assertEquals(expectedSize, tree.getSize());
        }

        Collections.shuffle(numbers);

        for (int n : numbers) {
            tree.remove(n);
            expectedSize--;
            assertEquals(expectedSize, tree.getSize());
        }
    }

    // Inserção repetida

    /**
     * Testa se um elemento pode ser inserido duas vezes e o que acontece quando é feita essa tentativa.
     */
    @Test
    public void repeatedInsertionTest() {

        Tree<Integer> tree = new Tree<>();

        numbers.forEach(n -> {
            boolean result =  tree.insert(n);
            // Inserção bem sucedida.
            assertTrue(result);
        });

        Collections.shuffle(numbers);

        numbers.forEach(n -> {
            boolean result = tree.insert(n);
            // Inserção repetida.
            assertFalse(result);
        });

        // Se a árvore não aceita números duplicados, ela aceitou apenas uma vez cada número da lista.
        assertEquals(tree.getSize(), size);

    }

    // Remoção de elemento não presente.

    /**
     * Testa o que acontece quando se tenta remover um elemento que não está na árvore.
     */
    @Test
    public void missingElementRemovalTest() {

        Tree<Integer> tree = new Tree<>();

        // Metade dos números vai estar presente e a outra metade não.
        int middleIndex = size / 2;

        // Insere a primeira metade da lista de números.
        for (int i = 0; i < middleIndex; i++) {
            int n = numbers.get(i);
            tree.insert(n);
        }

        // Tenta remover a segunda metade da lista, que não está na árvore.
        for (int i = middleIndex; i < size; i++) {
            int n = numbers.get(i);
            boolean result = tree.remove(n);
            assertFalse(result);
        }

        // Espera-se que a contagem não tenha sido alterada
        // pelas tentativas de remover elementos que não estão na árvore.
        assertEquals(middleIndex, tree.getSize());

        // Espera-se que os números inseridos ainda estejam na árvore.
        for (int i  = 0; i < middleIndex; i++) {
            int n  = numbers.get(i);
            boolean resultado = tree.contains(n);
            assertTrue(resultado);
        }

        // Espera-se que os números que nunca foram inseridos não estejam na árvore.
        for (int i = middleIndex; i < size; i++) {
            int n = numbers.get(i);
            boolean result = tree.contains(n);
            assertFalse(result);
        }

    }
}