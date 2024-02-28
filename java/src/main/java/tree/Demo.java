package tree;

import java.util.*;

/**
 * Classe de demonstração que insere e remove elementos da árvore.
 */
public class Demo {
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
        String input = "r";

        while (input.equals("r")) {
            // Cria uma lista de 20 inteiros aleatórios entre 0 e 99;
            ArrayList<Integer> numbers =  randomArray(20, 99);

            // Transfere a lista para uma árvore.
            Tree<Integer> tree =  arrayToTree(numbers);

            // Remove todos os itens da árvore.
            removeItemsFromTree(numbers, tree);

            System.out.println(
                    """
                    Digite R + Enter para executar novamente.
                    Qualquer outra tecla para terminar o programa.
                    Caso a formatação esteja confusa redimensione a janela e digite R.
                    """
            );

            input = scanner.next().toLowerCase();

        }

        scanner.close();

    }

    /**
     * Produz um array de inteiros aleatorios, sem repetição, entre 0 e maximo.
     * @param max Limite superior, não incluido, para os valores do array.
     * @param size Número de itens no array.
     */
    public static ArrayList<Integer> randomArray(int size, int max) {

        int acceptableSize = Math.min(size, max);

        Set<Integer> linkedHashSet =  new LinkedHashSet<>();
        while (linkedHashSet.size() < acceptableSize) {
            linkedHashSet.add(random.nextInt(max));
        }
        return new ArrayList<>(linkedHashSet);
    }

    /**
     * Transfere os elementos de um array para uma árvore.
     * @param numbers ArrayList de Integer a ser colocado na árvore.
     * @return Arvore<Integer> contendo os elementos do array.
     */
    private static Tree<Integer> arrayToTree(ArrayList<Integer> numbers) {

        Tree<Integer> tree =  new Tree<>();
        System.out.println("Adicionando os números a uma árvore vazia.");

        for (Integer n: numbers) {
            System.out.printf("Inserindo %s%n", n);
            tree.insert(n);
            System.out.println(tree);
        }

        return tree;
    }

    /**
     * Remove de uma árvore os elementos de um array que estiverem presentes na árvore.
     * @param numbers ArrayList<Integer> contendo os elementos a serem removidos.
     * @param tree Arvore<Integer> de onde devem ser removidos os elementos.
     */
    private static void removeItemsFromTree(ArrayList<Integer> numbers, Tree<Integer> tree) {

        System.out.println("Removendo números da árvore.");

        for (Integer n: numbers) {
            System.out.printf("Removendo %s%n", n);
            tree.remove(n);
            System.out.println(tree);
        }

    }
}
