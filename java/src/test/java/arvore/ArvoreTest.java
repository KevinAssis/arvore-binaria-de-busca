package arvore;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para a classe Arvore utilizando int.
 */
class ArvoreTest {
    private final int tamanho = 100;
    private final int maximo = 10000;
    private final ArrayList<Integer> numeros = Demonstracao.arrayAleatorio(tamanho, maximo);
    private final Random random = new Random();

    // Métodos para auxiliar os testes.

    /**
     * Executa Acao em todos os nós abaixo de no, começando pelas folhas e subindo.
     * @param acao O que deve ser executado em cada nó.
     * @param no Nó raiz da subárvore.
     */
    private void executarDeBaixoParaCima(Consumer<No<Integer>> acao, No<Integer> no) {

        // Primeiro nos filhos.
        if (!no.isVazio()) {

            if (!no.getEsquerda().isVazio()) {
                executarDeBaixoParaCima(acao, no.getEsquerda());
            }

            if (!no.getDireita().isVazio()) {
                executarDeBaixoParaCima(acao, no.getDireita());
            }

        }

        // Depois no próprio nó
        acao.accept(no);

    }

    // Balanceamento

    /**
     * Verifica o balanceamento de um nó.
     * @param no Nó que vai ser analisado.
     */
    private void testarBalanceamento(No<Integer> no) {

        if (!no.isVazio()) {
            // Recalcula alturas dos filhos.
            no.getEsquerda().atualizarAltura();
            no.getDireita().atualizarAltura();
        }

        int fatorDeBalanco = no.getFatorBalanco();

        // Uma árvore balanceada tem fatores de balanço entre 1 e -1;
        assertTrue(fatorDeBalanco <= 1);
        assertTrue(fatorDeBalanco >= -1);
    }

    /**
     * Verifica se a árvore se mantém balanceada após várias inserções e remoções.
     */
    @Disabled
    @Test
    public void balanceamentoAoInserir() {

        Arvore<Integer> arvore =  new Arvore<>();

        // Insere os itens da lista de números e verifica o balanceamento a cada inserção.
        numeros.forEach(n -> {
            arvore.inserir(n);
            executarDeBaixoParaCima(this::testarBalanceamento, arvore.getRaiz());
        });

        Collections.shuffle(numeros);

        // Remove todos os itens da lista e verifica o balanceamento a cada remoção.
        numeros.forEach(n -> {
            arvore.remover(n);
            executarDeBaixoParaCima(this::testarBalanceamento, arvore.getRaiz());
        });

    }

    // Altura da árvore

    /**
     * Função recursiva que calcula a altura deste nó por percorrer todos os seus decendentes.
     * A fórmula usada é a mesma da classe Arvore, porém é calculada no momento do teste
     * para verificar se as alturas armazenadas nos nós estão corretas.
     * @param no Nó cuja altura deve ser calculada.
     * @return Máximo entre as alturas dos nós filhos + 1.
     */
    private int calcularAlturaDeUmNo(No<Integer> no) {
        if (no.isVazio()) { return 0; }
        return Math.max(
                calcularAlturaDeUmNo(no.getEsquerda()),
                calcularAlturaDeUmNo(no.getDireita())
        ) + 1;
    }

    /**
     * Compara a altura calculada no momento do teste com a
     * altura calculada quando a àrvore foi modificada.
     */
    private void testarAlturaDeUmNo(No<Integer> no) {
        assertEquals(no.getAltura(), calcularAlturaDeUmNo(no));
    }

    /**
     * Verifica se a árvore está calculando a altura de cada nó corretamente a cada alteração.
     */
    @Disabled
    @Test
    public void alturaArvore() {

        Arvore<Integer> arvore = new Arvore<>();

        numeros.forEach(n -> {
            arvore.inserir(n);
            executarDeBaixoParaCima(this::testarAlturaDeUmNo, arvore.getRaiz());
        });

        Collections.shuffle(numeros);

        numeros.forEach(n -> {
            arvore.remover(n);
            executarDeBaixoParaCima(this::testarAlturaDeUmNo, arvore.getRaiz());
        });

    }

    // Contagem

    /**
     * Verifica se a variável contagem da árvore está sendo calculada corretamente.
     */
    @Disabled
    @Test
    public void contagem() {

        Arvore<Integer> arvore = new Arvore<>();
        int contagemEsperada = 0;

        for (int n : numeros) {
            arvore.inserir(n);
            contagemEsperada++;
            assertEquals(contagemEsperada, arvore.getContagem());
        }

        Collections.shuffle(numeros);

        for (int n : numeros) {
            arvore.remover(n);
            contagemEsperada--;
            assertEquals(contagemEsperada, arvore.getContagem());
        }
    }

    // Inserção repetida

    /**
     * Testa se um valor pode ser inserido duas vezes e o que acontece quando é feita essa tentativa.
     */
    @Disabled
    @Test
    public void insercaoRepetida() {

        Arvore<Integer> arvore = new Arvore<>();

        // Insere todos os números da lista.
        numeros.forEach(n -> {
            boolean resultado =  arvore.inserir(n);
            // O resultado de um inserção bem sucedida é true.
            assertTrue(resultado);
        });

        Collections.shuffle(numeros);

        // Insere novamente todos os números da lista.
        numeros.forEach(n -> {
            boolean resultado = arvore.inserir(n);
            // O resultado de uma inserção repetida é false.
            assertFalse(resultado);
        });

        // Se a árvore não aceita números duplicados, ela aceitou apenas uma vez cada número da lista.
        assertEquals(arvore.getContagem(), tamanho);

    }

    // Remoção de valor não presente.

    /**
     * Testa o que acontece quando se tenta remover um valor que não está na árvore.
     */
    @Disabled
    @Test
    public void remocaoDeValorNaoPresente() {

        Arvore<Integer> arvore = new Arvore<>();

        // Metade dos números vai estar presente e a outra metade não.
        int indiceMetade = tamanho / 2;

        // Insere a primeira metade da lista de números.
        for (int i = 0; i < indiceMetade; i++) {
            int n = numeros.get(i);
            arvore.inserir(n);
        }

        // Tenta remover a segunda metade da lista, que não está na árvore.
        for (int i = indiceMetade; i < tamanho; i++) {
            int n = numeros.get(i);
            boolean resultado = arvore.remover(n);
            assertFalse(resultado);
        }

        // Espera-se que a contagem não tenha sido alterada
        // pelas tentativas de remover valores que não estão na árvore.
        assertEquals(indiceMetade, arvore.getContagem());

        // Espera-se que os números inseridos ainda estejam na árvore.
        for (int i  = 0; i < indiceMetade; i++) {
            int n  = numeros.get(i);
            boolean resultado = arvore.contem(n);
            assertTrue(resultado);
        }

        // Espera-se que os números que nunca foram inseridos não estejam na árvore.
        for (int i = indiceMetade; i < tamanho; i++) {
            int n = numeros.get(i);
            boolean resultado = arvore.contem(n);
            assertFalse(resultado);
        }

    }
}