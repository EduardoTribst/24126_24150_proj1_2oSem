import java.io.*;
import static java.lang.System.out;
public class ManterEstudantes implements ManterDados {
    int qtosDados,
            posicaoAtual;
    Estudante[] dados;
    Situacao situacao;
    public void leituraDosDados(String nomeArquivo) throws IOException {
        try {
            posicaoAtual = 0;
            BufferedReader arquivoDeEntrada = new BufferedReader(
                    new FileReader("c:\\temp\\dadosEstudantes.txt"));
            String linhaDoArquivo = "";
            try {
                boolean parar = false;
                while (! parar) {
                    Estudante novoDado = new Estudante();

                    try {

                        if (novoDado.leuLinhaDoArquivo(arquivoDeEntrada) ) {
                            incluirNoFinal(novoDado);
                        }
                        else

                            parar = true;
                    }

                    catch (Exception erroDeLeitura) {

                        out.println(erroDeLeitura.getMessage());

                        parar = true;

                    }
                }
                arquivoDeEntrada.close();
            }
            catch (IOException erroDeIO) {
                out.println(erroDeIO.getMessage());
            }
        }
        catch (FileNotFoundException erro) {
            out.println(erro.getMessage());
        }
    }
    public void gravarDados(String nomeArquivo) throws IOException {
        BufferedWriter arquivoDeSaida = new BufferedWriter(
                new FileWriter("c:\\temp\\dadosEstudantes.txt"));
        for (int indice=0; indice < qtosDados; indice++)
            arquivoDeSaida.write(dados[indice].formatoDeArquivo());
        arquivoDeSaida.close();
    }
    public Boolean existe(Estudante dadoProcurado) {
        for (int i = 0; i<qtosDados; i++){
            if (dados[i] == dadoProcurado)
                return true;
        }
        return false;
    }

    public void incluirNoFinal(Estudante novoDado) {
        dados[qtosDados++] = novoDado; //Coloca em qtosDados e depois o incrementa
    }

    public void incluirEm(Estudante novoDado, int posicaoDeInclusao) {
        Estudante aIncluir = novoDado;

        for (int i = posicaoDeInclusao; i<qtosDados; i++){
            Estudante auxiliar = dados[i];
            dados[i] = aIncluir;
            aIncluir = auxiliar;

        }
    }

    public void excluir(int posicaoDeExclusao) {
        qtosDados --;
        for(int i = posicaoDeExclusao; i<qtosDados; i++){
            dados[i] = dados[i+1]; //DEVE ESTOURAR O LIMITE DE ESTUDANTES
        }
    }

    public Estudante valorDe(int indiceDeAcesso) { ... }

    public void alterar(int posicaoDeAlteracao, Estudante novoDado) {
        dados[posicaoDeAlteracao] = novoDado;
    }

    public void trocar(int origem, int destino) {
        Estudante auxiliar = dados[origem];
        dados[origem] = dados[destino];
        dados[destino] = auxiliar;
    }

    public void ordenar() { ... }

    public Boolean estaVazio() {
        if (qtosDados == 0){
            return true;
        }
        return false;
    }

    public Boolean estaNoInicio() {
        if(posicaoAtual == 0){
            return true;
        }
        return false;
    }

    public Boolean estaNoFim() {
        if(posicaoAtual == qtosDados-1){
            return true;
        }
        return false;

    }

    public void irAoInicio() {
        posicaoAtual = 0;
    }

    public void irAoFim() {
        posicaoAtual = qtosDados;
    }

    public void irAoAnterior() {
        posicaoAtual --;
    }

    public void irAoProximo() {
        posicaoAtual ++; //ACHO QUE É ISSO
    }

    public int getPosicaoAtual() {
        return posicaoAtual;
    }

    public void setPosicaoAtual(int novaPosicao) {
        posicaoAtual = novaPosicao;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao novaSituacao) {
        situacao = novaSituacao;
    }
}