package tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Árvore binária de busca.
 * Balanceada automaticamente por meio do algoritmo AVL.
 * @param <T> Tipo dos elementos armazenados na árvore.
 */
public class Tree<T extends Comparable<T>> {

    private Node<T> root = new Node<>(null);
    private int size = 0;

    // Getters
    public Node<T> getRoot() { return root; }

    public int getSize() { return size; }

    /**
     * Função recursiva que desce na árvore até encontrar o elemento, comparando ele a cada nó.
     * @param element Elemento procurado.
     * @param node Nó que será examinado nesta iteração.
     * @return O nó contendo o elemento, se encontrado,
     * ou o nó vazio onde o elemento deveria ser inserido, se não encontrado.
     */
    private Node<T> find(T element, Node<T> node) {

        if (node.isEmpty()) {
            // Elemento não foi encontrado, mas pode ser adicionado nesta posição.
            return node;
        }

        int diff = node.getElement().compareTo(element);

        if (diff > 0) {
            // Elemento vem antes deste nó.
            return find(element, node.getLeft());
        } else if (diff < 0) {
            // Elemento vem depois deste nó.
            return find(element, node.getRight());
        } else {
            // Elemento encontrado neste nó.
            return node;
        }
    }

    /**
     * Verifica se elemento está presente na árvore.
     * @param element Elemento procurado.
     */
    public boolean contains(T element) {
        // Se o método encontrar retorna um nó vazio, isso significa que o elemento não foi encontrado.
        return !find(element, root).isEmpty();
    }

    /**
     * Insere este elemento na árvore. Balanceia a árvore caso necessário.
     * @return True se o elemento foi inserido com sucesso, false se já existe na árvore.
     */
    public boolean insert(T element) {

        Node<T> node = find(element, root);

        // Se o nó não é vazio, o elemento já existe.
        // A árvore não aceita com duplicados.
        if (!node.isEmpty()) { return false; }

        node.setElement(element);

        size += 1;

        // Balanceia a subárvore modificada e todos os nós acima.
        if (node.parent != null) {
            balance(node.parent);
        }

        return true;
    }

    /**
     * Remove o nó da árvore. Balanceia a árvore se necessário.
     * @param node Nó a remover.
     */
    private void remove(Node<T> node) {

        // Em 3 dos próximos 4 casos a modificação mais profunda na árvore
        // é no nó removido.
        Node<T> nodeToBalance = node.parent;

        if (node.getHeight() == 1) {
            // Altura = 1 significa o mesmo que
            // esquerda.isVazio() && direita.isVazio().
            // Nó folha
            node.clear();

        } else if (node.getLeft().isEmpty()) {
            // Tem apenas um filho à direita.
            replaceNode(node, node.getRight());
            // Garbage Collector pode liberar o espaço de no.

        } else if (node.getRight().isEmpty()) {
            // Tem apenas um filho à esquerda.
            replaceNode(node, node.getLeft());
            // Garbage Collector pode liberar o espaço de no.

        } else {
            // Tem dois filhos.

            // Encontra nós mais à direita na subárvore esquerda.
            Node<T> predecessor = node.getLeft();
            while (!predecessor.getRight().isEmpty()) {
                predecessor =  predecessor.getRight();
            }

            // Coloca o elemento mais à direita da subárvore esquerda no lugar do elemento removido.
            node.setElement(predecessor.getElement());
            // A árvore será balanceada quando o predecessor for removido.
            nodeToBalance = null;
            // Como o elemento predecessor está no lugar do elemento removido, ele pode ser excluido.
            remove(predecessor);
            // Anulando a modificação dupla da contagem devido à remoção do predecessor.
            size += 1;

            // Garbage Collector pode liberar o espaço de no.
        }

        size -= 1;

        if (nodeToBalance != null) {
            balance(nodeToBalance);
        }

    }

    /**
     * Remove elemento da árvore se ele existir. Balanceia a árvore se necessário.
     * @param element Elemento a ser removido.
     * @return True se o elemento foi removido, false se não está presente.
     */
    public boolean remove(T element) {

        Node<T> node = find(element, root);

        if (node.isEmpty()) { return false; }

        remove(node);
        return true;
    }

    // Balanceamento

    /**
     * Verifica o balanço do nó e, dependendo do valor, realiza rotações para balancear a subárvore.
     * @param node Nó que será verificado e balanceado.
     */
    private void balance(Node<T> node) {

        node.updadeHeight();

        int balanceFactor = node.getBalanceFactor();

        // Se -1 <= fatorBalanco <= 1, o nó já está balanceado e nada precisa ser feito.
        if (balanceFactor < -1) {
            rightRotation(node);
        } else if (balanceFactor > 1) {
            leftRotation(node);
        }

        if (node.parent != null) {
            balance(node.parent);
        }

    }

    // Rotações

    /**
     * O filho à direita se torna a nova raiz da subárvore.
     * <pre>
     *       a                 b
     *   x       b    =>   a       c
     * x   x   x   c
     * </pre>
     * @param a Nó que vai ser rotacionado.
     */
    private void leftRotation(Node<T> a) {

        /* Rotação dupla se o balanço de c é menor do que 0.
         *       a
         *   x       c
         * x   x   b   x
         */
        if (a.getRight().getBalanceFactor() < 0) { rightRotation(a.getRight()); }

        /* Agora que o balanço de B é maior ou igual a 0 fazer uma rotação simples.
         *       a
         *   x       b
         * x   x   x   c
         */

        Node<T> b = a.getRight();

        // O filho à esquerda de b, mesmo que seja nó vazio, se torna filho à direita de a.
        a.setRight(b.getLeft());

        // Se a era raiz da árvore, agora b se torna raiz.
        replaceNode(a, b);

        // a se torna filho à esquerda de b.
        b.setLeft(a);

        /* Resultado
         *       b
         *   a       c
         */

        // Os nós acima desta subárvore serão atualizados mais tarde
        // porque balancear chama atualizarAltura para todos eles.
        a.updadeHeight();
        b.updadeHeight();
        b.getRight().updadeHeight();
    }

    /**
     * O filho à esquerda se torna a nova raiz da subárvore.
     * <pre>
     *       c                 b
     *   b       x    =>   a       c
     * a   x   x   x
     * </pre>
     * @param c Nó que vai ser rotacionado.
     */
    private void rightRotation(Node<T> c) {

        /* Rotação dupla se o balanço de A é maior que 0.
         *       c
         *   a       x
         * x   b   x   x
         */
        if (c.getLeft().getBalanceFactor() > 0) { leftRotation(c.getLeft()); }

        /* Agora que o balanço de B é menor ou igual a 0 fazer uma rotação simples.
         *       c
         *   b       x
         * a   x   x   x
         */

        Node<T> b = c.getLeft();

        // O filho à direita de b, mesmo que seja nó vazio, se torna filho à direita de c.
        c.setLeft(b.getRight());

        // Se c era raiz da árvore, agora b se torna raiz.
        replaceNode(c, b);

        // a se torna filho à esquerda de b.
        b.setRight(c);

        /* Resultado
         *       b
         *   a       c
         */

        // Os nós acima desta subárvore serão atualizados mais tarde
        // porque balancear chama atualizarAltura para todos eles.
        b.updadeHeight();
        c.updadeHeight();
        b.getLeft().updadeHeight();
    }

    /**
     * Atualiza o pai de novo por colocá-lo no lugar de antigo e
     * atualiza a raiz da árvore se necessário.
     * @param oldNode Nó que deve ser substituido.
     * @param newNode Nó que vai ser colocado na posição de antigo.
     */
    private void replaceNode(Node<T> oldNode, Node<T> newNode) {

        // Se antigo era raiz da árvore, agora novo se torna raiz.
        if (oldNode.parent == null) {
            root = newNode;
            newNode.parent = null;
        // Se antigo era um filho à esquerda, novo o substitui.
        // Compara por referência.
        } else if (oldNode.parent.getLeft() == oldNode) {
            oldNode.parent.setLeft(newNode);
        // Se antigo era um filho à direita, novo o substitui.
        } else {
            oldNode.parent.setRight(newNode);
        }
    }

    // Métodos auxiliares para Strings.

    /**
     * Representação da árvore na vertical. Nó direito fica abaixo do esquerdo.
     * Ocupa menos espaço que a horizontal.
     */
    public String toStringVertical() {

        StringBuilder stringBuilder = new StringBuilder();

        recursiveVerticalToString(stringBuilder, 0, root, '*');

        return stringBuilder.toString();
    }

    /**
     * Adiciona cada nó à representação após a indentação e um indicador de esquerda ou direita.
     * @param stringBuilder StringBuilder que contém a representação da árvore.
     * @param level Profundidade do nó que será representada como indentação. 0 para raiz.
     */
    private void recursiveVerticalToString(StringBuilder stringBuilder, int level, Node<T> node, char position) {

        if (node.isEmpty()) { return; }

        stringBuilder.append(
                "%s%c %s%n"
                        .formatted(" ".repeat(level), position, node.getElement().toString())
        );

        recursiveVerticalToString(stringBuilder, level + 1, node.getLeft(), 'E');
        recursiveVerticalToString(stringBuilder, level + 1, node.getRight(), 'D');

    }

    /**
     * Representação da árvore em texto na horizontal.
     * Nós irmãos ficam lado a lado.
     */
    public String toString() {
        if (root.isEmpty()) { return ""; }

        // Para cada linha da representação é feita uma lista de nós do nível correspondente da árvore.
        ArrayList<ArrayList<Node<T>>> lines = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of(root))
                )
        );

        // A ‘String’ que será retornada.
        StringBuilder stringBuilder = new StringBuilder();
        // Número de caracteres necessários para representar o maior elemento da árvore.
        int maxWidth =  root.getElement().toString().length();
        boolean hasNext = true;

        while (hasNext) {
            ArrayList<Node<T>> nextLine =  new ArrayList<>();
            hasNext = false;

            for (Node<T> node : lines.get(lines.size() - 1)) {
                if (node == null) {

                    nextLine.add(null);
                    nextLine.add(null);

                } else {

                    // Largura deste nó.
                    int nodeWidth = node.getElement().toString().length();
                    if (nodeWidth > maxWidth) {
                        maxWidth = nodeWidth;
                    }

                    // Prepara a próxima linha.
                    if (node.getLeft().isEmpty()) {
                        nextLine.add(null);
                    } else {
                        nextLine.add(node.getLeft());
                        hasNext = true;
                    }
                    if (node.getRight().isEmpty()) {
                        nextLine.add(null);
                    } else {
                        nextLine.add(node.getRight());
                        hasNext = true;
                    }

                }
            }

            if (hasNext) {
                lines.add(nextLine);
            }
        }

        int lastLineSize = lines.get(lines.size() - 1).size();

        // Largura da última linha em função do espaço de um nó.
        // Espaçamento entre nós na última linha é um nó vazio.
        int treeWidth =  (2 * lastLineSize) - 1;

        String emptySpace = " ".repeat(maxWidth);
        String emptyNode = "-".repeat(maxWidth);
        int depth = lines.size();

        for (int i = 0; i < depth; i++) {
            ArrayList<Node<T>> line =  lines.get(i);

            // Espaço entre nós em função do espaço de um nó. Ex.: 1 nó vazio, 2 nós vazios, ...
            int spacing =  (int) Math.pow(2, (depth - i)) - 1;

            // Espaço que é adicionado à esquerda para centralizar o nó nesta linha.
            int lineWidth =
                    treeWidth - // Largura da árvore em nós.
                            line.size() - // Número de nós desta linha.
                            ((line.size() - 1) * spacing); // Espaço ocupado nesta linha entre os nós em função do espaço de um nó.

            // Adiciona a indentação antes da linha.
            stringBuilder.append(
                   emptySpace.repeat(lineWidth / 2)
            );

            for (int j = 0; j < line.size(); j++) {
                Node<T> node = line.get(j);
                if (node == null) {
                    stringBuilder.append(emptyNode);
                } else {
                    stringBuilder.append(String.format("%" + maxWidth + "s", node.getElement()));
                }
                if (j != line.size() - 1) {
                    stringBuilder.append(emptySpace.repeat(spacing));
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

}
