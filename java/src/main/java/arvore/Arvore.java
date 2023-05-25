package arvore;

/**
 * Árvore binária de busca que é balanceada automaticamente por meio do algoritmo AVL.
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

        No<T> noEncontrado =  encontrar(valor, raiz);

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
     * Remove valor da árvore se ele existir. Balanceia a árvore se necessário.
     * @return True se o valor foi removido, false se não está presente.
     */
    public boolean remover(T valor) {
        // TODO Implementar remover
        return false;
    }

    // Balanceamento

    /**
     * Verifica o balanço do nó e, dependendo do valor, realiza rotações para tornar esta subárvore balanceada.
     * @param no Nó que será verificado e balanceado.
     */
    private void balancear(No<T> no) {
        // TODO Implementar balancear
    }

    //Rotações
    // TODO Implementar rotações

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
