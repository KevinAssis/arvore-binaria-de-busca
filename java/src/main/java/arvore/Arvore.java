package arvore;

/**
 * Árvore binária de busca que é balanceada automaticamente por meio do algoritmo AVL.
 * @param <T> Tipo dos valores armazenados na árvore.
 */
public class Arvore<T extends Comparable<T>> {

    /**
     * Função recursiva que desce na árvore até encontrar o valor, comparando ele a cada nó.
     * @param valor Valor procurado.
     * @param no Nó que está sendo examinado nesta iteração.
     * @return Nó encontrado ou, se não encontrar, nó vazio onde o valor deveria ser inserido.
     */
    public No<T> encontrar(T valor, No<T> no) {
        // TODO Implementar encontrar
        return null;
    }

    /**
     * Verifica se valor está presente na árvore.
     */
    public boolean contem(T valor) {
        // TODO Implementar contem
        return false;
    }

    /**
     * Insere este valor na árvore. Balanceia caso necessário.
     * @return True se o valor foi inserido com sucesso, false se já existe na árvore.
     */
    public boolean inserir(T valor) {
        // TODO Implementar inserir
        return false;
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
    public void balancear(No<T> no) {
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
