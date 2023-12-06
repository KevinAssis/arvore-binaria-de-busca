''' <summary>
''' Classe representando nó que só pode ser usada numa classe Arvore deste projeto.
''' </summary>
''' <typeparam name="T">Tipo do valor armazenado nos nós.</typeparam>
Public Class No(Of T As IComparable)
    Private _Valor As T

    Private _Esquerda As No(Of T)
    Private _Direita As No(Of T)
    Public Pai As No(Of T)

    ' Construtor.

    ''' <summary>
    ''' Cria um nó vazio e atribui o seu pai.
    ''' </summary>
    ''' <param name="Pai"></param>
    Public Sub New(Pai As No(Of T))
        Me.Pai = Pai
        _Vazio = True
    End Sub

    ' Propriedades.

    ''' <summary>
    ''' Elemento associado a este nó.
    ''' </summary>
    Public Property Valor As T
        Get
            Return _Valor
        End Get
        Set(Valor As T)
            _Valor = Valor

            If _Vazio Then
                ' Para cada folha com um elemento são criados nós vazios como filhos.
                _Esquerda = New No(Of T)(Me)
                _Direita = New No(Of T)(Me)
                _Vazio = False
                AtualizarAltura()
            End If

        End Set
    End Property

    ''' <summary>
    ''' Folhas possuem filhos vazios que não armazenam elementos. Essa variável indica se esse é o caso.
    ''' </summary>
    Public ReadOnly Property Vazio As Boolean

    ''' <summary>
    ''' Altura da subárvore que tem este nó como raiz. Máximo entre as alturas das subárvores direita e esquerda mais 1.
    ''' Folhas tem altura 1. Nós vazios tem altura 0.
    ''' </summary>
    Public ReadOnly Property Altura As Integer

    ''' <summary>
    ''' Nó filho à esquerda deste nó. Set atualiza Pai do parâmetro.
    ''' </summary>
    Public Property Esquerda As No(Of T)
        Get
            Return _Esquerda
        End Get
        Set(Valor As No(Of T))
            _Esquerda = Valor
            Valor.Pai = Me
        End Set
    End Property

    ''' <summary>
    ''' Nó filho à direita deste nó. Set atualiza Pai do parâmetro.
    ''' </summary>
    Public Property Direita As No(Of T)
        Get
            Return _Direita
        End Get
        Set(Valor As No(Of T))
            _Direita = Valor
            Valor.Pai = Me
        End Set
    End Property

    ''' <summary>
    ''' Calcula o fator de balanço da subárvore que tem este nó como raiz. Altura da subárvore direita - altura da subárvore esquerda.
    ''' Se o fator for maior que 1 ou menor que -1 a árvore não está balanceada corretamente.
    ''' </summary>
    ''' <returns>Fator de balanço calculado.</returns>
    Public ReadOnly Property FatorBalanco As Long
        Get
            If _Vazio Then Return 0
            Return (Direita.Altura - Esquerda.Altura)
        End Get
    End Property

    ''' <summary>
    ''' Calcula a altura desta subárvore, que é igual ao máximo das alturas das subárvores direita e esquerda mais 1.
    ''' </summary>
    Public Sub AtualizarAltura()
        If Vazio Then Return
        _Altura = Math.Max(Esquerda.Altura, Direita.Altura) + 1
    End Sub

    ''' <summary>
    ''' Retorna todos os valores ao padrão de um nó vazio.
    ''' </summary>
    Public Sub Esvaziar()
        _Valor = Nothing
        _Vazio = True
        _Esquerda = Nothing
        _Direita = Nothing
        _Altura = 0
    End Sub

End Class
