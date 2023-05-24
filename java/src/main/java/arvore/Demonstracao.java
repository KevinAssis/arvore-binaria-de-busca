package arvore;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

/**
 * Classe de demonstração que insere e remove elementos da árvore.
 */
public class Demonstracao {
    private static final Random random = new Random();

    /**
     * Produz um array de inteiros aleatorios, sem repetição, entre 0 e maximo.
     *
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
}
