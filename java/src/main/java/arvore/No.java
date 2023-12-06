package arvore;

/**
 * Classe representando nó que só pode ser usada numa classe Arvore deste pacote.
 * @param <T> Tipo do valor armazenado nos nós.
 */
public class No<T extends Comparable<T>> {
    /**
     * Elemento associado a este nó.
     */
    private T valor;
    /**
     * Filho da esquerda.
     */
    private No<T> esquerda;
    /**
     * Filho da direita.
     */
    private No<T> direita;
    protected No<T> pai;
    /**
     * Folhas possuem filhos vazios que não armazenam elementos.
     * Essa variável indica se esse é o caso.
     */
    private boolean vazio;
    /**
     * Altura da subárvore que tem este nó como raiz.
     * Máximo entre as alturas das subárvores direita e esquerda mais 1.
     * Folhas têm altura 1. Nós vazios têm altura 0.
     */
    private int altura = 0;

    // Construtor

    /**
     * Cria um nó vazio e atribui o seu pai.
     */
    public No(No<T> pai) {
        this.pai = pai;
        vazio = true;
    }

    // Getters

    public T getValor() { return valor; }

    public No<T> getEsquerda() { return esquerda; }

    public No<T> getDireita() { return direita; }

    public boolean isVazio() { return vazio; }

    public int getAltura() { return altura; }

    /**
     * Calcula o fator de balanço da subárvore que tem este nó como raiz.
     * Altura da subárvore direita - altura da subárvore esquerda.
     * Se o fator for maior do que 1 ou menor do que -1 a árvore não está balanceada corretamente.
     * @return Fator de balanço calculado.
     */
    public int getFatorBalanco() {
        if (vazio) { return 0; }
        return direita.altura - esquerda.altura;
    }

    // Setters

    /**
     * Atribui o elemento deste nó. Cada nó folha com um elemento possui nós vazios como filhos.
     */
    public void setValor(T valor) {
        this.valor = valor;

        if (vazio) {
            esquerda = new No<>(this);
            direita = new No<>(this);
            vazio = false;
            atualizarAltura();
        }
    }

    /**
     * Modifica o filho da esquerda e atribui este nó(this) como pai do argumento esquerda.
     */
    public void setEsquerda(No<T> esquerda) {
        this.esquerda = esquerda;
        esquerda.pai = this;
    }

    /**
     * Modifica o filho da direita e atribui este nó(this) como pai do argumento direita.
     */
    public void setDireita(No<T> direita) {
        this.direita = direita;
        direita.pai = this;
    }

    // Outros métodos.

    /**
     * Calcula a altura desta subárvore, que é igual ao máximo das alturas das subárvores direita e esquerda mais 1.
     */
    public void atualizarAltura() {
        if (vazio) { return; }
        altura =  Math.max(esquerda.altura, direita.altura) + 1;
    }

    /**
     * Retorna todos os valores ao padrão de um nó vazio.
     */
    public void esvaziar() {
        vazio = true;
        valor = null;
        esquerda = null;
        direita = null;
        altura = 0;
    }

}
