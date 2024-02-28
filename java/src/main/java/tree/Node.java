package tree;

/**
 * Classe representando nó que só pode ser usada numa classe Tree deste pacote.
 * @param <T> Tipo do elemento armazenado nos nós.
 */
class Node<T extends Comparable<T>> {
    /**
     * Elemento associado a este nó.
     */
    private T element;
    /**
     * Filho da esquerda.
     */
    private Node<T> left;
    /**
     * Filho da direita.
     */
    private Node<T> right;
    Node<T> parent;
    /**
     * Altura da subárvore que tem este nó como raiz.
     * Máximo entre as alturas das subárvores direita e esquerda mais 1.
     * Folhas têm altura 1. Nós vazios têm altura 0.
     */
    private int height = 0;

    // Construtor

    /**
     * Cria um nó vazio e atribui o seu pai.
     */
    Node(Node<T> parent) {
        this.parent = parent;
    }

    // Getters

    T getElement() { return element; }

    Node<T> getLeft() { return left; }

    Node<T> getRight() { return right; }

    boolean isEmpty() { return (right == null && left == null); }

    int getHeight() { return height; }

    /**
     * Calcula o fator de balanço da subárvore que tem este nó como raiz.
     * Altura da subárvore direita - altura da subárvore esquerda.
     * Se o fator for maior do que 1 ou menor do que -1 a árvore não está balanceada corretamente.
     * @return Fator de balanço calculado.
     */
    int getBalanceFactor() {
        if (isEmpty()) { return 0; }
        return right.height - left.height;
    }

    // Setters

    /**
     * Atribui o elemento deste nó. Cada nó folha com um elemento possui nós vazios como filhos.
     */
    void setElement(T element) {
        this.element = element;

        if (isEmpty()) {
            left = new Node<>(this);
            right = new Node<>(this);
            updadeHeight();
        }
    }

    /**
     * Modifica o filho da esquerda e atribui este nó(this) como pai do argumento esquerda.
     */
    void setLeft(Node<T> left) {
        this.left = left;
        left.parent = this;
    }

    /**
     * Modifica o filho da direita e atribui este nó(this) como pai do argumento direita.
     */
    void setRight(Node<T> right) {
        this.right = right;
        right.parent = this;
    }

    // Outros métodos.

    /**
     * Calcula a altura desta subárvore, que é igual ao máximo das alturas das subárvores direita e esquerda mais 1.
     */
    void updadeHeight() {
        if (isEmpty()) { return; }
        height =  Math.max(left.height, right.height) + 1;
    }

    /**
     * Retorna todos os valores ao padrão de um nó vazio.
     */
    void clear() {
        element = null;
        left = null;
        right = null;
        height = 0;
    }

}
