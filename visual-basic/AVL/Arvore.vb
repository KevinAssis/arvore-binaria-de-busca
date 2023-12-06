Imports System.Collections
Imports System.Text
Imports System.Runtime.CompilerServices

<Assembly: InternalsVisibleTo("Teste")>

''' <summary>
''' Árvore binária de busca balanceada automaticamente por meio do algoritmo AVL.
''' </summary>
''' <typeparam name="T">Tipo dos elementos armazenados na árvore.</typeparam>
Public Class Arvore(Of T As IComparable)

    Friend Raiz As New No(Of T)(Nothing)
    Public ReadOnly Property Contagem

    ''' <summary>
    ''' Função recursiva que desce na árvore até encontrar o elemento
    ''' comparando ele a cada nó.
    ''' </summary>
    ''' <param name="Elemento">Elemento procurado</param>
    ''' <param name="EsteNo">Nó que será examinado nesta iteração.</param>
    ''' <returns>
    '''     O nó contendo o elemento, se encontrado, 
    '''     ou o nó vazio onde o elemento deveria ser inserido, se não encontrado.
    ''' </returns>
    Private Function Encontrar(Elemento As T, EsteNo As No(Of T)) As No(Of T)

        If (EsteNo.Vazio) Then
            ' Elemento não foi encontrado, mas pode ser adicionado nesta posição.
            Return EsteNo
        End If

        Dim Diferenca = EsteNo.Elemento.CompareTo(Elemento)

        If Diferenca > 0 Then
            ' Elemento vem antes deste nó.
            Return Encontrar(Elemento, EsteNo.Esquerda)
        ElseIf Diferenca < 0 Then
            ' Elemento vem depois deste nó.
            Return Encontrar(Elemento, EsteNo.Direita)
        Else
            ' Elemento encontrado neste nó.
            Return EsteNo
        End If

    End Function

    ''' <summary>
    ''' Verifica se Elemento está presente na árvore.
    ''' </summary>
    ''' <param name="Elemento">Elemento procurado.</param>
    Public Function Contem(Elemento As T) As Boolean
        ' Se o método Encontrar retorna um nó vazio, isso significa que Elemento não foi encontrado.
        Return Not Encontrar(Elemento, Raiz).Vazio
    End Function

    ''' <summary>
    ''' Insere este elemento na árvore. Balanceia a árvore caso necessário.
    ''' </summary>
    ''' <param name="Elemento"></param>
    ''' <returns>True se o elemento foi inserido com sucesso, false se já existe na árvore.</returns>
    Public Function Inserir(Elemento As T) As Boolean
        Dim NoEncontrado = Encontrar(Elemento, Raiz)

        ' Se o nó não é vazio o elemento já existe.
        ' A árvore não aceita elementos duplicados.
        If Not NoEncontrado.Vazio Then Return False

        NoEncontrado.Elemento = Elemento

        _Contagem += 1

        ' Balanceia a subárvore modificada e todos os nós acima.
        If NoEncontrado.Pai IsNot Nothing Then
            Balancear(NoEncontrado.Pai)
        End If

        Return True

    End Function

    ''' <summary>
    ''' Remove o nó da árvore. Balanceia a árvore se necessário.
    ''' </summary>
    ''' <param name="NoARemover"></param>
    Private Sub Remover(NoARemover As No(Of T))

        ' Em 3 dos próximos 4 casos a modificação mais profunda na árvore é no nó removido.
        Dim NoABalancear = NoARemover.Pai

        If NoARemover.Altura = 1 Then
            ' Altura = 1 significa o mesmo que Esquerda.Vazio And Direita.Vazio
            ' Nó folha.
            NoARemover.Esvaziar()

        ElseIf NoARemover.Esquerda.Vazio Then
            ' Tem apenas um filho à direita.
            SubstituirNo(NoARemover, NoARemover.Direita)
            ' Garbage Collector pode liberar o espaço de NoEncontrado.

        ElseIf NoARemover.Direita.Vazio Then
            ' Tem apenas um filho à esquerda.
            SubstituirNo(NoARemover, NoARemover.Esquerda)
            ' Garbage Collector pode liberar o espaço de NoEncontrado.

        Else
            ' Tem dois filhos.

            ' Encontra nó mais à direita na subárvore esquerda.
            Dim Predecessor = NoARemover.Esquerda
            Do Until Predecessor.Direita.Vazio
                Predecessor = Predecessor.Direita
            Loop

            ' Coloca o elemento mais à direita da subárvore esquerda no lugar do elemento removido.
            NoARemover.Elemento = Predecessor.Elemento
            ' A árvore será balanceada quando Predecessor for removido.
            NoABalancear = Nothing
            ' Como o elemento predecessor está no lugar do elemento removido, ele pode ser excluido.
            Remover(Predecessor)
            ' Anulando a modificação dupla da Contagem devido à remoção do Predecessor.
            _Contagem += 1

            ' Garbage Collector pode liberar o espaço de NoEncontrado.

        End If

        _Contagem -= 1

        If (NoABalancear IsNot Nothing) Then
            Balancear(NoABalancear)
        End If

    End Sub

    ''' <summary>
    ''' Remove o elemento da árvore se ele existir. Balanceia a árvore se necessário.
    ''' </summary>
    ''' <param name="Elemento"></param>
    ''' <returns>True se o elemento foi removido, False se não está presente.</returns>
    Public Function Remover(Elemento As T) As Boolean
        Dim NoEncontrado = Encontrar(Elemento, Raiz)

        If NoEncontrado.Vazio Then Return False

        Remover(NoEncontrado)
        Return True

    End Function

    ' Balanceamento

    ''' <summary>
    ''' Verifica o balanço do nó N e, dependendo do valor, realiza rotações para balancear a subárvore.
    ''' </summary>
    ''' <param name="N">Nó que será verificado e balanceado.</param>
    Private Sub Balancear(N As No(Of T))

        N.AtualizarAltura()

        Dim Balanco = N.FatorBalanco

        ' Se -1 <= FatorBalanco <= 1 o nó já está balanceado e nada precisa ser feito.
        If Balanco < -1 Then
            RotacaoADireita(N)
        ElseIf Balanco > 1 Then
            RotacaoAEsquerda(N)
        End If

        If N.Pai IsNot Nothing Then
            Balancear(N.Pai)
        End If

    End Sub

    ' Rotacoes
    ''' <summary>
    ''' O filho à direita se torna a nova raiz da subárvore.
    ''' <code>
    '''       A                 B
    '''   x       B    =>   A       C
    ''' x   x   x   C
    ''' </code>
    ''' </summary>
    ''' <param name="A">Nó que vai ser rotacionado.</param>
    Private Sub RotacaoAEsquerda(A As No(Of T))

        ' Rotação dupla se o balanço de C é menor que 0
        '       A
        '   x       C
        ' x   x   B   x
        If A.Direita.FatorBalanco < 0 Then RotacaoADireita(A.Direita)

        ' Agora que o balanço de B é maior ou igual a 0 fazer uma rotação simples.
        '       A
        '   x       B
        ' x   x   x   C

        Dim B = A.Direita

        ' O filho à esquerda de B, mesmo que seja nó vazio, se torna filho à direita de A.
        A.Direita = B.Esquerda

        ' Se A era raiz da árvore, agora B se torna raiz.
        SubstituirNo(A, B)

        ' A se torna filho à esquerda de B
        B.Esquerda = A

        ' Resultado
        '       B
        '   A       C

        ' Os nós acima desta subárvore serão atualizados mais tarde porque Balancear() chama AtualizarAltura para todos eles.
        A.AtualizarAltura()
        B.AtualizarAltura()
        B.Direita.AtualizarAltura()

    End Sub

    ''' <summary>
    ''' O filho à esquerda se torna a nova raiz da subárvore.
    ''' <code>
    '''       C                 B
    '''   B       x    =>   A       C
    ''' A   x   x   x
    ''' </code>
    ''' </summary>
    ''' <param name="C">Nó que vai ser rotacionado.</param>
    Private Sub RotacaoADireita(C As No(Of T))

        ' Rotação dupla se o balanço de A é maior que 0.
        '       C
        '   A       x
        ' x   B   x   x
        If C.Esquerda.FatorBalanco > 0 Then RotacaoAEsquerda(C.Esquerda)

        ' Agora que o balanço de B é menor ou igual a 0 fazer uma rotação simples.
        '       C
        '   B       x
        ' A   x   x   x

        Dim B = C.Esquerda

        ' O filho à direita de B, mesmo que seja nó vazio, se torna filho à esquerda de C.
        C.Esquerda = B.Direita

        ' Se C era raiz da árvore, agora B se torna raiz.
        SubstituirNo(C, B)

        ' C se torna filho à direita de B.
        B.Direita = C

        ' Resultado
        '       B
        '   A       C

        ' Os nós acima desta subárvore serão atualizados mais tarde porque Balancear() chama AtualizarAltura para todos eles.
        B.Esquerda.AtualizarAltura()
        B.AtualizarAltura()
        C.AtualizarAltura()

    End Sub

    ''' <summary>
    ''' Atualiza o Pai de Novo por colocá-lo no lugar de Antigo e atualiza a raiz da árvore se necessário.
    ''' </summary>
    ''' <param name="Antigo">Nó que deve ser substituido.</param>
    ''' <param name="Novo">Nó que vai ser colocado na posição de antigo.</param>
    Private Sub SubstituirNo(Antigo As No(Of T), Novo As No(Of T))

        ' Se Antigo era a raiz da árvore, agora Novo se torna raiz.
        If Antigo.Pai Is Nothing Then
            Raiz = Novo
            Novo.Pai = Nothing
            ' Se Antigo era um filho à esquerda, Novo o substitui. Compara por referência.
        ElseIf Antigo.Pai.Esquerda Is Antigo Then
            Antigo.Pai.Esquerda = Novo
            ' Se Antigo era filho à direita, Novo o substitui.
        Else
            Antigo.Pai.Direita = Novo
        End If

    End Sub

    ' Métodos auxiliares para Strings.

    ''' <summary>
    ''' Representação da árvore na vertical. Nó direito fica abaixo do esquerdo.
    ''' Ocupa menos espaço que a horizontal.
    ''' </summary>
    Public Function ToStringVertical() As String

        Dim Resultado As New StringBuilder

        ToStringVerticalRecursivo(Resultado, 0, Raiz, "*")

        Return Resultado.ToString

    End Function

    ''' <summary>
    ''' Adiciona cada nó à Representação após a indentação e um indicador de esquerda ou direita.
    ''' </summary>
    ''' <param name="Representacao">StringBuilder que contém a representação da árvore.</param>
    ''' <param name="Nivel">Profundidade do nó que será representada como indentação. 0 para raiz.</param>
    ''' <param name="EsteNo"></param>
    Private Sub ToStringVerticalRecursivo(ByRef Representacao As StringBuilder, Nivel As Integer, EsteNo As No(Of T), Posicao As String)

        If EsteNo.Vazio Then Return

        Representacao.Append(
            StrDup(Nivel, "  ") +
            Posicao + " " +
            EsteNo.Elemento.ToString +
            vbNewLine
        )

        ToStringVerticalRecursivo(Representacao, Nivel + 1, EsteNo.Esquerda, "E")
        ToStringVerticalRecursivo(Representacao, Nivel + 1, EsteNo.Direita, "D")

    End Sub

    ''' <summary>
    ''' Represtentação da árvore em texto na horizontal.
    ''' Nós irmãos ficam lado a lado.
    ''' </summary>
    Public Overrides Function ToString() As String

        If Raiz.Vazio Then Return ""

        ' Para cada Linha da representação é feita uma lista de nós do nível correspondente da árvore
        Dim Linhas As New ArrayList() From {
            New ArrayList From {
                Raiz
            }
        }
        ' A String que será retornada.
        Dim Resultado As New StringBuilder("")
        ' Número de caracteres necessários para representar o maior elemento da árvore.
        Dim LarguraNo = Raiz.Elemento.ToString.Length
        ' Se um dos nós deste nível tem filhos então o loop continua.
        Dim Continuar = True

        While Continuar

            Dim ProximaLinha = New ArrayList
            Continuar = False

            For Each N In Linhas(Linhas.Count - 1)

                If IsNothing(N) Then
                    ProximaLinha.AddRange({Nothing, Nothing})
                Else
                    ' Largura deste nó.
                    Dim NLength = N.Elemento.ToString.Length
                    If NLength > LarguraNo Then
                        LarguraNo = NLength
                    End If

                    ' Prepara a próxima Linha.
                    If N.Esquerda.Vazio Then
                        ProximaLinha.Add(Nothing)
                    Else
                        ProximaLinha.Add(N.Esquerda)
                        Continuar = True
                    End If
                    If N.Direita.Vazio Then
                        ProximaLinha.Add(Nothing)
                    Else
                        ProximaLinha.Add(N.Direita)
                        Continuar = True
                    End If
                End If
            Next

            If Continuar Then
                Linhas.Add(ProximaLinha)
            End If

        End While

        Dim NumNosUltimaLinha = Linhas(Linhas.Count - 1).Count

        ' Largura da última linha em função do espaço de um nó.
        ' Espaçamento na ultima linha é igual a um nó vazio.
        Dim LarguraArvore = (2 * NumNosUltimaLinha) - 1

        Dim EspacoVazio = StrDup(LarguraNo, " ")
        Dim NoVazio = StrDup(LarguraNo, "-")
        Dim Profundidade = Linhas.Count

        For I = 0 To Linhas.Count - 1
            Dim Linha = Linhas(I)

            ' Espaço entre nós em função do espaço de um nó. Ex.: 1 nó vazio, 2 nós vazio, ...
            Dim Espacamento = Math.Pow(2, (Profundidade - I)) - 1


            ' Espaço que é adicionado à esquerda para centralizar o nó nesta linha.
            '       Lagura da árvore em nós.
            '       Número de nós desta linha -
            '       Espaço ocupado nesta linha entre os nós em função do espaço de um nó.
            Dim LarguraDestaLinha = LarguraArvore -
                    Linha.Count -
                    ((Linha.Count - 1) * Espacamento)

            ' Adiciona a indentação antes da linha.
            Resultado.Append(StrDup(
                    LarguraDestaLinha \ 2,
                EspacoVazio
            ))

            For J = 0 To Linha.Count - 1
                Dim N = Linha(J)
                If IsNothing(N) Then
                    Resultado.Append(NoVazio)
                Else
                    Resultado.Append(N.Elemento.ToString.PadLeft(LarguraNo))
                End If
                If J <> Linha.Count - 1 Then
                    Resultado.Append(StrDup(CInt(Espacamento), EspacoVazio))
                End If
            Next
            Resultado.Append(vbNewLine)
        Next

        Return Resultado.ToString


    End Function

End Class
