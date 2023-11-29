package arvore;

import java.util.*;

/**
 * Classe de demonstração que insere e remove elementos da árvore.
 */
public class Demonstracao {
    private static final Random random = new Random();

    public static void main(String[] args) {

        /*
         * O método chamado para a exibição no console é Arvore.toString(), que exibe na horizontal.
         * Caso a janela do console seja pequena, modifique o zoom ou o tamanho da janela e digite R + Enter para exibir os resultados novamente.
         * Outra opção é usar o método toStringVertical, que ocupa menos espaço na horizontal, mas o resultado é muito menos legível.
         */

        System.out.println(
                """
                Essa é uma demonstração do funcionamento da árvore.
                São adicionados e depois removidos 20 números entre 0 e 99 a um objeto Arvore<Integer>.
                """
        );
        Scanner scanner = new Scanner(System.in);
        String resposta = "r";

        while (resposta.equals("r")) {
            // Cria uma lista de 20 inteiros aleatórios entre 0 e 99;
            ArrayList<Integer> numeros =  arrayAleatorio(20, 99);

            // Transfere a lista para uma árvore.
            Arvore<Integer> arvore =  arrayParaArvore(numeros);

            // Remove todos os itens da árvore.
            removerListaArvore(numeros, arvore);

            System.out.println(
                    """
                    Digite R + Enter para executar novamente.
                    Qualquer outra tecla para terminar o programa.
                    Caso a formatação esteja confusa redimensione a janela e digite R.
                    """
            );

            resposta = scanner.next().toLowerCase();

        }

        scanner.close();

    }

    /**
     * Produz um array de inteiros aleatorios, sem repetição, entre 0 e maximo.
     * @param maximo Limite superior, não incluido, para os valores do array.
     * @param tamanho Número de itens no array.
     */
    public static ArrayList<Integer> arrayAleatorio(int tamanho, int maximo) {
        if (tamanho > maximo) {
            throw new IllegalArgumentException("O tamanho da lista não pode ser maior que o valor máximo pois não há repetição.");
        }
        Set<Integer> linkedHashSet =  new LinkedHashSet<>();
        while (linkedHashSet.size() < tamanho) {
            linkedHashSet.add(random.nextInt(maximo));
        }
        return new ArrayList<>(linkedHashSet);
    }

    /**
     * Transfere os elementos de um array para uma árvore.
     * @param numeros Array de Integer a ser colocado na árvore.
     * @return Arvore<Integer> contendo os elementos do array.
     */
    private static Arvore<Integer> arrayParaArvore(ArrayList<Integer> numeros) {

        Arvore<Integer> arvore =  new Arvore<>();
        System.out.println("Adicionando os números a uma árvore vazia.");

        for (Integer n: numeros) {
            System.out.printf("Inserindo %s%n", n);
            arvore.inserir(n);
            System.out.println(arvore);
        }

        return arvore;
    }

    /**
     * Remove de uma árvore os elementos de um array que estiverem presentes na árvore.
     * @param numeros Array de Integer contendo os elementos a serem removidos.
     * @param arvore Arvore<Integer> de onde devem ser removidos os elementos.
     */
    private static void removerListaArvore(ArrayList<Integer> numeros, Arvore<Integer> arvore) {

        System.out.println("Removendo números da árvore.");

        for (Integer n: numeros) {
            System.out.printf("Removendo %s%n", n);
            arvore.remover(n);
            System.out.println(arvore);
        }

    }
}
