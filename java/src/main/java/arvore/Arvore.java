package arvore;

/**
 * Árvore binária de busca.
 * Balanceada automaticamente por meio do algoritmo AVL.
 * @param <T> Tipo dos valores armazenados na árvore.
 */
public class Arvore<T extends Comparable<T>> {

    private No<T> raiz = new No<>(null);
    private int contagem = 0;

    // Getters
    public No<T> getRaiz() { return raiz; }

    public int getContagem() { return contagem; }

    /**
     * Função recursiva que desce na árvore até encontrar o valor, comparando ele a cada nó.
     * @param valor Valor procurado.
     * @param no Nó que será examinado nesta iteração.
     * @return O nó contendo o valor, se encontrado,
     * ou o nó vazio onde o valor deveria ser inserido, se não encontrado.
     */
    private No<T> encontrar(T valor, No<T> no) {

        if (no.isVazio()) {
            // valor não foi encontrado, mas pode ser adicionado nesta posição.
            return no;
        }

        int diferenca = no.getValor().compareTo(valor);

        return switch (diferenca) {

            // valor encontrado neste nó.
            case 0 -> no;

            // valor é maior do que este nó.
            case -1 -> encontrar(valor, no.getDireita());

            // Case 1. valor é menor do que este nó.
            default -> encontrar(valor, no.getEsquerda());

        };
    }

    /**
     * Verifica se valor está presente na árvore.
     */
    public boolean contem(T valor) {
        // Se encontrar retorna um nó vazio, isso significa que o valor não foi encontrado.
        return !encontrar(valor, raiz).isVazio();
    }

    /**
     * Insere este valor na árvore. Balanceia caso necessário.
     * @return True se o valor foi inserido com sucesso, false se já existe na árvore.
     */
    public boolean inserir(T valor) {

        No<T> noEncontrado = encontrar(valor, raiz);

        // Se o nó não é vazio, o valor já existe.
        // A árvore não aceita valores duplicados.
        if (!noEncontrado.isVazio()) { return false; }

        noEncontrado.setValor(valor);

        contagem += 1;

        // Balanceia a subárvore modificada e todos os nós acima.
        if (noEncontrado.pai != null) {
            balancear(noEncontrado.pai);
        }

        return true;
    }

    /**
     * Remove o nó da árvore. Balanceia a árvore se necessário.
     */
    private void remover(No<T> no) {

        // Em 3 dos próximos 4 casos a modificação mais profunda na árvore
        // é no nó removido.
        No<T> noABalancear = no.pai;

        if (no.getAltura() == 1) {
            // Altura = 1 significa o mesmo que
            // esquerda.isVazio() && direita.isVazio().
            no.esvaziar();
        } else if (no.getEsquerda().isVazio()) {
            // Tem apenas um filho à direita.
            substituirNo(no, no.getDireita());
            // Garbage Collector pode liberar o espaço de no.
        } else if (no.getDireita().isVazio()) {
            // Tem apenas um filho à esquerda.
            substituirNo(no, no.getEsquerda());
            // Garbage Collector pode liberar o espaço de no.
        } else {
            // Tem dois filhos.

            // Encontra nós mais à direita na subárvore esquerda.
            No<T> predecessor = no.getEsquerda();
            while (!predecessor.getDireita().isVazio()) {
                predecessor =  predecessor.getDireita();
            }

            // Coloca o valor mais alto da subárvore esquerda no lugar do valor removido.
            no.setValor(predecessor.getValor());
            // A árvore será balanceada quando o predecessor for removido.
            noABalancear = null;
            // Como o valor do predecessor está no lugar do valor removido, ele pode ser excluido.
            remover(predecessor);
            // Anulando a modificação dupla da contagem devido à remoção do predecessor.
            contagem += 1;

            // Garbage Collector pode liberar o espaço de no.
        }

        contagem -= 1;

        if (noABalancear != null) {
            balancear(noABalancear);
        }

    }

    /**
     * Remove valor da árvore se ele existir. Balanceia a árvore se necessário.
     * @return True se o valor foi removido, false se não está presente.
     */
    public boolean remover(T valor) {

        No<T> noEncontrado = encontrar(valor, raiz);

        if (noEncontrado.isVazio()) { return false; }

        remover(noEncontrado);
        return true;
    }

    // Balanceamento

    /**
     * Verifica o balanço do nó e, dependendo do valor, realiza rotações para tornar esta subárvore balanceada.
     * @param no Nó que será verificado e balanceado.
     */
    private void balancear(No<T> no) {

        no.atualizarAltura();

        // Substitui a propriedade para evitar recalcular.
        int balanco = no.getFatorBalanco();

        // Se -1 <= fatorBalanco <= 1, o nó já está balanceado e nada precisa ser feito.
        if (balanco < -1) {
            rotacaoADireita(no);
        } else if (balanco > 1) {
            rotacaoAEsquerda(no);
        }

        if (no.pai != null) {
            balancear(no.pai);
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
    private void rotacaoAEsquerda(No<T> a) {

        /* Rotação dupla se o balanço de c é menor do que 0.
         *       a
         *   x       c
         * x   x   b   x
         */
        if (a.getDireita().getFatorBalanco() < 0) { rotacaoADireita(a.getDireita()); }

        /* Agora que o balanço de B é maior ou igual a 0 fazer uma rotação simples.
         *       a
         *   x       b
         * x   x   x   c
         */

        // Salvando referência a b antes de substituií-lo.
        No<T> b = a.getDireita();

        // O filho à esquerda de b, mesmo que seja nó vazio, se torna filho à direita de a.
        a.setDireita(b.getEsquerda());

        // Se a era raiz da árvore, agora b se torna raiz.
        substituirNo(a, b);

        // a se torna filho à esquerda de b.
        b.setEsquerda(a);

        /* Resultado
         *       b
         *   a       c
         */

        // Atualizando as alturas dos nós afetados, a, b e c.
        // Os nós acima desta subárvore serão atualizados mais tarde
        // porque balancear chama atualizarAltura para todos eles.
        a.atualizarAltura();
        b.atualizarAltura();
        b.getDireita().atualizarAltura();
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
    private void rotacaoADireita(No<T> c) {

        /* Rotação dupla se o balanço de A é maior que 0.
         *       c
         *   a       x
         * x   b   x   x
         */
        if (c.getEsquerda().getFatorBalanco() > 0) { rotacaoAEsquerda(c.getEsquerda()); }

        /* Agora que o balanço de B é menor ou igual a 0 fazer uma rotação simples.
         *       c
         *   b       x
         * a   x   x   x
         */

        // Salvando referência a b antes de substituií-lo.
        No<T> b = c.getEsquerda();

        // O filho à direita de b, mesmo que seja nó vazio, se torna filho à direita de c.
        c.setEsquerda(b.getDireita());

        // Se c era raiz da árvore, agora b se torna raiz.
        substituirNo(c, b);

        // a se torna filho à esquerda de b.
        b.setDireita(c);

        /* Resultado
         *       b
         *   a       c
         */

        // Atualizando as alturas dos nós afetados, a, b e c.
        // Os nós acima desta subárvore serão atualizados mais tarde
        // porque balancear() chama atualizarAltura para todos eles.
        b.atualizarAltura();
        c.atualizarAltura();
        b.getEsquerda().atualizarAltura();
    }

    /**
     * Atualiza o pai de novo por colocá-lo no lugar de antigo e
     * atualiza a raiz da árvore se necessário.
     */
    private void substituirNo(No<T> antigo, No<T> novo) {

        // Se antigo era raiz da árvore, agora novo se torna raiz.
        if (antigo.pai == null) {
            raiz = novo;
            novo.pai = null;
        // Se antigo era um filho à esquerda, novo o substitui.
        } else if (antigo.pai.getEsquerda().equals(antigo)) {
            antigo.pai.setEsquerda(novo);
        // Se antigo era um filho à direita, novo o substitui.
        } else {
            antigo.pai.setDireita(novo);
        }
    }

    // Métodos auxiliares para Strings.

    /**
     * Representação da árvore na vertical. Nó direito fica abaixo do esquerdo.
     * Ocupa menos espaço que a horizontal.
     */
    public String toStringVertical() {
        // TODO Implementar toStringVertical
        return "";
    }

    /**
     * Representação da árvore em texto na horizontal.
     * Nós irmãos ficam lado a lado.
     */
    public String toString() {
        // TODO Implementar toString
        return "";
    }

}
