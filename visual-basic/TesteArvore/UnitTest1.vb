Imports Arvore
Imports Demonstracao

''' <summary>
''' Testes utilizando Integer.
''' </summary>
<TestClass()> Public Class UnitTest1
    Const Tamanho = 100
    Const Maximo = 10000
    ReadOnly Numeros = ArrayAleatorio(Tamanho, Maximo)
    ReadOnly Ran = New Random()

    ' Subs para auxiliar os testes.

    ''' <summary>
    ''' Executa Acao em todos os nós de abaixo de NoRaiz, começando pelas folhas e subindo.
    ''' </summary>
    ''' <param name="Acao">O que deve ser executado.</param>
    ''' <param name="NoRaiz">Nó raiz da subárvore.</param>
    Private Sub ExecutarDeBaixoParaCima(Acao As Action(Of No(Of Integer)), NoRaiz As No(Of Integer))

        ' Primeiro no filhos.
        If Not NoRaiz.Vazio Then

            If Not NoRaiz.Esquerda.Vazio Then
                ExecutarDeBaixoParaCima(Acao, NoRaiz.Esquerda)
            End If

            If Not NoRaiz.Direita.Vazio Then
                ExecutarDeBaixoParaCima(Acao, NoRaiz.Direita)
            End If

        End If

        ' Depois no próprio nó.
        Acao(NoRaiz)

    End Sub

    ''' <summary>
    ''' Ordena a lista de números aleatóriamente.
    ''' </summary>
    Private Sub EmbaralharNumeros()

        For I = 0 To Tamanho - 1
            Dim J = Ran.Next(0, Tamanho - 1)
            Dim Temporario = Numeros(J)
            Numeros(J) = Numeros(I)
            Numeros(I) = Temporario
        Next

    End Sub

    ' Balanceamento

    ''' <summary>
    ''' Verifica o balanceamento de um nó.
    ''' </summary>
    ''' <param name="NoArv">Nó que vai ser analisado.</param>
    Private Sub TestarBalanceamento(NoArv As No(Of Integer))

        If Not NoArv.Vazio Then

            ' Recalcular alturas dos filhos.
            NoArv.Esquerda.AtualizarAltura()
            NoArv.Direita.AtualizarAltura()

        End If

        Dim FatorDeBalanco = NoArv.FatorBalanco()

        ' Uma árvore balanceada tem fatores de balanço entre 1 e -1.
        Assert.IsTrue(FatorDeBalanco <= 1)
        Assert.IsTrue(FatorDeBalanco >= -1)

    End Sub

    ''' <summary>
    ''' Verifica se a árvore se mantém balanceada após várias inserções e remoções.
    ''' </summary>
    <TestMethod()> Public Sub BalancementoAoInserir()

        Dim Arv = New Arvore(Of Integer)

        ' Insere os itens da lista de números e verifica o balanceamento a cada inserção.
        For I = 0 To Tamanho - 1
            Dim N = Numeros(I)
            Arv.Inserir(N)
            ' Verifica o balanceamento de todos os nós da árvore.
            ExecutarDeBaixoParaCima(AddressOf TestarBalanceamento, Arv.Raiz)
        Next

        EmbaralharNumeros()

        ' Remove todos os itens da lista e verifica o balanceamento a cada remoção.
        For I = 0 To Tamanho - 1
            Dim N = Numeros(I)
            Arv.Remover(N)
            ' Verifica o balanceamento de todos os nós da árvore.
            ExecutarDeBaixoParaCima(AddressOf TestarBalanceamento, Arv.Raiz)
        Next

    End Sub

    ' AlturaArvore

    ''' <summary>
    ''' Calcula a altura deste nó por percorrer todos os seus decendentes.
    ''' A fórmula usada é a mesma da classe Arvore, porém é calculada no momento do teste 
    ''' para verificar se as alturas armazenadas nos nós estão corretas.
    ''' </summary>
    ''' <param name="NoArv">Nó cuja altura deve ser calculada.</param>
    ''' <returns>Máximo entre as alturas dos nós filhos + 1.</returns>
    Private Function CalcularAlturaDeUmNo(NoArv As No(Of Integer)) As Integer
        If NoArv.Vazio Then Return 0
        Return Math.Max(CalcularAlturaDeUmNo(NoArv.Direita), CalcularAlturaDeUmNo(NoArv.Esquerda)) + 1
    End Function

    ''' <summary>
    ''' Compara a altura calculada no momento do teste com a altura calculada quando a àrvore foi modificada.
    ''' </summary>
    ''' <param name="NoArv"></param>
    Private Sub TestarAlturaDeUmNo(NoArv As No(Of Integer))
        Assert.AreEqual(NoArv.Altura, CalcularAlturaDeUmNo(NoArv))
    End Sub

    ''' <summary>
    ''' Verifica se a árvore está calculando a altura de cada nó corretamente a cada alteração.
    ''' </summary>
    <TestMethod()> Public Sub AlturaArvore()

        Dim Arv = New Arvore(Of Integer)

        ' Insere os números da lista e verifica as alturas a cada inserção.
        For I = 0 To Tamanho - 1
            Dim N = Numeros(I)
            Arv.Inserir(N)
            ' Verifica a altura de todos os nós da árvore.
            ExecutarDeBaixoParaCima(AddressOf TestarAlturaDeUmNo, Arv.Raiz)
        Next

        EmbaralharNumeros()

        ' Remove todos os números da lista e verifica as alturas a cada remoção.
        For I = 0 To Tamanho - 1
            Dim N = Numeros(I)
            Arv.Remover(N)
            ' Verifica a altura de todos os nós da árvore.
            ExecutarDeBaixoParaCima(AddressOf TestarAlturaDeUmNo, Arv.Raiz)
        Next

    End Sub

    ' Contagem

    ''' <summary>
    ''' Verifica se a variável Contagem da árvore está sendo calculada corretamente.
    ''' </summary>
    <TestMethod()> Public Sub Contagem()

        Dim Arv = New Arvore(Of Integer)
        ' Quantos nós deveriam estar presentes na árvore.
        Dim ContagemEsperada = 0

        ' Insere todos os números da lista e verifica a contagem a cada inserção.
        For I = 0 To Tamanho - 1
            Dim N = Numeros(I)
            Arv.Inserir(N)
            ContagemEsperada += 1
            Assert.AreEqual(Arv.Contagem, ContagemEsperada)
        Next

        EmbaralharNumeros()

        ' Remove todos os números da lista e verifica a contagem a cada remoção.
        For I = 0 To Tamanho - 1
            Dim N = Numeros(I)
            Arv.Remover(N)
            ContagemEsperada -= 1
            Assert.AreEqual(Arv.Contagem, ContagemEsperada)
        Next

    End Sub

    ' Inserção repetida.

    ''' <summary>
    ''' Testa se um valor pode ser inserido duas vezes e o que acontece quando é feita essa tentativa.
    ''' </summary>
    <TestMethod()> Public Sub InsercaoRepetida()
        Dim Arv = New Arvore(Of Integer)

        ' Insere todos os números da lista.
        For Each N As Integer In Numeros
            Dim ResultadoDaInsercao = Arv.Inserir(N)
            ' O resultado de uma inserção bem sucedida é True.
            Assert.IsTrue(ResultadoDaInsercao)
        Next

        EmbaralharNumeros()

        ' Insere novamente todos os números da lista.
        For Each N As Integer In Numeros
            Dim ResultadoDaInsercao = Arv.Inserir(N)
            ' O resultado de uma inserção repetida é False.
            Assert.IsFalse(ResultadoDaInsercao)
        Next

        ' Se a árvore não aceita números duplicados, ela aceitou apenas uma vez cada número da lista.
        Assert.AreEqual(Arv.Contagem, Tamanho)

    End Sub

    ' Remoção de valor não presente.

    ''' <summary>
    ''' Testa o que acontece quando se tenta remover um valor que não está na árvore.
    ''' </summary>
    <TestMethod()> Public Sub RemocaoDeValorNaoPresente()
        Dim Arv = New Arvore(Of Integer)
        ' Metade da árvore vai ser estar presente e outra metade não.
        Dim IndiceMetade As Integer = Tamanho / 2

        ' Insere a primeira metade da lista de números.
        For I = 0 To IndiceMetade - 1
            Dim N = Numeros(I)
            Arv.Inserir(N)
        Next

        ' Tenta remover a segunda metade da lista, que não está na árvore.
        For I = IndiceMetade To Tamanho - 1
            Dim N = Numeros(I)
            Assert.IsFalse(Arv.Remover(N))
        Next

        ' Espera-se a contagem não tenha sido alterada pelas tentativas de remover valores que não estão na árvore.
        Dim ContagemEsperada = IndiceMetade
        Assert.AreEqual(ContagemEsperada, Arv.Contagem)

        ' Espera-se que os números inseridos ainda estejam na árvore.
        For I = 0 To IndiceMetade - 1
            Dim N = Numeros(I)
            Assert.IsTrue(Arv.Contem(N))
        Next

        ' Espera-se que os números que nunca foram inseridos não estejam na árvore.
        For I = IndiceMetade To Tamanho - 1
            Dim N = Numeros(I)
            Assert.IsFalse(Arv.Contem(N))
        Next

    End Sub

End Class