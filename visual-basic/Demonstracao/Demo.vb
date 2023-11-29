Imports System.Linq
Imports System.Runtime.CompilerServices
Imports Arvore

<Assembly: InternalsVisibleTo("Teste")>

''' <summary>
''' Módulo de demonstração que insere e remove elementos da árvore.
''' </summary>
Module Demo
    ReadOnly RNG = New Random()
    ReadOnly NL = Environment.NewLine

    Sub Main()

        ' O método chamado para a exibição no console é Arvore.ToString(), que a exibe na horizontal.
        ' Caso a janela do console seja pequena, modifique o zoom ou o tamanho e digite r para exibir os resultados novamente.
        ' Outra opção é usar o método ToStringVertical, que ocupa menos espaço na horizontal, mas o resultado é muito menos legível.

        Console.WriteLine(
            "Essa é uma demonstração do funcionamento da árvore." + NL +
            "São adicionados e depois removidos 20 números entre 0 e 99 a um objeto Arvore(Of Integer)."
        )

        Dim Resposta = ConsoleKey.R

        While Resposta = ConsoleKey.R
            ' Cria uma lista de 20 inteiros aleatórios entre 0 e 99.
            Dim Numeros() = ArrayAleatorio(20, 99)

            ' Transfere a lista para uma árvore.
            Dim Arv = ArrayParaArvore(Numeros)

            ' Remove todos os itens da árvore.
            RemoverListaArvore(Numeros, Arv)

            Console.WriteLine(
                "Aperte R para executar novamente." + NL +
                "Qualquer outra tecla para terminar o programa." + NL +
                "Caso a formatação esteja confusa redimensione a janela e aperte R." + NL
            )

            Resposta = Console.ReadKey().Key
        End While

    End Sub

    ''' <summary>
    ''' Produz um array de inteiros aleatórios. Mínimo 0.
    ''' </summary>
    ''' <param name="Tamanho">Tamanho do array.</param>
    ''' <param name="Maximo">Valor máximo permitido.</param>
    ''' <returns>Um Array de Integer contendo a lista aleatória.</returns>
    Friend Function ArrayAleatorio(Tamanho As Integer, Maximo As Integer) As Integer()
        ' Essa forma de produzir o array evita repetições.
        Return Enumerable.Range(0, Maximo).OrderBy(Function(r) RNG.Next).
            Take(Tamanho).ToArray
    End Function

    ''' <summary>
    ''' Transfere os elementos de um array para uma árvore.
    ''' </summary>
    ''' <param name="Numeros">Array de Integer a ser colocado na árvore.</param>
    ''' <returns>Arvore(Of Integer) contendo os elementos do array.</returns>
    Friend Function ArrayParaArvore(Numeros As Integer()) As Arvore(Of Integer)

        Dim Arv As New Arvore(Of Integer)
        Console.WriteLine(NL + "Adicionando os números a uma árvore vazia." + NL)

        For Each N In Numeros
            Console.WriteLine($"Inserindo {N}{NL}")
            Arv.Inserir(N)
            Console.WriteLine(Arv)
        Next

        Return Arv

    End Function

    ''' <summary>
    ''' Remove os elementos de um array que estiverem presentes em uma árvore.
    ''' </summary>
    ''' <param name="Numeros">Array de Integer contendo os elementos a serem removidos.</param>
    ''' <param name="Arv">Arvore(Of Integer) de onde devem ser removidos os elementos.</param>
    Friend Sub RemoverListaArvore(Numeros As Integer(), Arv As Arvore(Of Integer))

        Console.WriteLine("Removendo números da árvore." + NL)

        For Each N In Numeros
            Console.WriteLine($"Removendo {N}{NL}")
            Arv.Remover(N)
            Console.WriteLine(Arv)
        Next

    End Sub

End Module
