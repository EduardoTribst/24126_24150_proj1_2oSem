import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import static java.lang.System.*;

public class Manutencao {
    enum Ordens {porRa, porNome, porCurso, porMedia};
    private static Ordens ordemAtual = Ordens.porRa;
    //private static Estudante[] estud;       // vetor de estudantes
    private static int quantosEstudantes;   // tamanho lógico do vetor estud
    //private static BufferedReader arquivoDeEntrada;
    //private static BufferedWriter arquivoDeSaida;
    static Scanner leitor = new Scanner(in);
    static boolean continuarPrograma = true;
    static int onde; // índice resultante da pesquisa binária
    //-------------------------------------------------------
    static ManterEstudantes manEstud = new ManterEstudantes();
    static String[] materias;

    public static void main(String[] args) throws Exception {
        //estud = new Estudante[3];  // 50 - tamanho físico
        for (int ind=0; ind < 3; ind++)
            manEstud.incluirNoFinal(new Estudante());// criar objetos Estudante vazios no vetor
        quantosEstudantes = 0; // tamanho lógico (vetor vazio)
        manEstud.leituraDosDados("dadosEstudantes.txt"); //TALVEZ DEPOIS VER O ARQUIVO COM O SCANNER
        if (continuarPrograma) {
            seletorDeOpcoes();
            manEstud.gravarDados("dadosEstudantes.txt");//TALVEZ DEPOIS VER O ARQUIVO COM O SCANNER
        }
        out.println("\nPrograma encerrado.");
    }


    public static void seletorDeOpcoes() throws Exception {
        int opcao = 0;
        do {
            out.println("Opções:\n");
            out.println("0 - Terminar programa");
            out.println("1 - Incluir estudante");
            out.println("2 - Listar estudantes");
            out.println("3 - Excluir estudante");
            out.println("4 - Listar situações");
            out.println("5 - Digitar notas de estudante");
            out.println("6 - Ordenar por curso");
            out.println("7 - Ordenar por nome");
            out.println("8 - Ordenar por média");
            out.print("\nSua opção: ");
            opcao = leitor.nextInt();
            leitor.nextLine();      // necessário após nextInt() para poder ler strings a seguir
            switch(opcao) {
                case 1 : incluirEstudante(); break;
                case 2 : listarEstudantes(); break;
                case 3 : excluirEstudante(); break;
                case 4 : listarSituacoes();  break;
                case 5 : digitarNotas(); break;
                case 6 : ordenarPorCurso(); break;
                case 7 : ordenarPorNome(); break;
                case 8 : ordenarPorMedia(); break;
            }
        }
        while (opcao != 0);
    }


    public static void incluirEstudante() throws Exception {
        if (ordemAtual != Ordens.porRa)
            manEstud.ordenar();

        out.println("Incluir Estudante\n");
        out.print("Curso : ");
        String curso = leitor.nextLine();
        out.print("RA    : ");
        String ra = leitor.nextLine();
        out.print("Nome  : ");
        String nome = leitor.nextLine();
        Estudante umEstudante = new Estudante(curso, ra, nome);
        if (manEstud.existe(umEstudante))  // ajusta a variável onde
            JOptionPane.showMessageDialog(null,"Estudante repetido!");
        else
        {
            manEstud.incluirEmOrdem(umEstudante);  // última posição usada
        }
    }


    public static void excluirEstudante() throws Exception {
        if (ordemAtual != Ordens.porRa)
            manEstud.ordenar();
        out.println("Excluir Estudante\n");
        out.print("RA    : ");
        String ra = leitor.nextLine();
        Estudante umEstudante = new Estudante(" ", ra, " ");
        if (!manEstud.existe(umEstudante))  // ajusta a variável onde
            JOptionPane.showMessageDialog(null,"Estudante não encontrado!");
        else  // achou o estudante procurado, no índice "onde" do vetor
        {
            manEstud.excluir(onde);  // última posição usada
        }
    }


    public static void listarEstudantes() throws Exception{
        out.println("\n\nListagem de Estudantes\n");
        int contLinha = 0;  // contador de linhas
        for (int ind = 0; ind < quantosEstudantes; ind++)
        {
            out.println(manEstud.valorDe(ind));

            if (++contLinha >= 20) {       // se exibiu 20 linhas, espera Enter
                out.print("\n\nTecle [Enter] para prosseguir: ");
                leitor.nextLine();
                contLinha = 0;      // reinicia o contador de linhas
            }
        }
        out.print("\n\nTecle [Enter] para prosseguir: ");
        leitor.nextLine();
    }

    public static void listarSituacoes() throws Exception {
        out.println("\n\nSituação estudantil\n");
        String situacao = "";
        for (int indice = 0; indice < quantosEstudantes; indice++)
        {
            double mediaDesseEstudante = manEstud.valorDe(indice).mediaDasNotas();
            if (mediaDesseEstudante < 5)
                situacao = "Não promovido(a)";
            else
                situacao = "Promovido(a)    ";

            out.printf(
                    "%4.1f %16s "+manEstud.valorDe(indice)+"\n", mediaDesseEstudante,
                    situacao);
        }
        out.print("\n\nTecle [Enter] para prosseguir: ");
        leitor.nextLine();
    }


    private static void ordenarPorCurso() throws Exception {
        for (int lento=0; lento < quantosEstudantes; lento++)
            for (int rapido=lento+1; rapido < quantosEstudantes; rapido++)
                if (manEstud.valorDe(lento).getCurso().compareTo(manEstud.valorDe(rapido).getCurso()) > 0)
                    manEstud.trocar(lento, rapido);
        ordemAtual = Ordens.porCurso;
    }


    private static void ordenarPorNome() throws Exception{
        for (int lento=0; lento < quantosEstudantes; lento++)
            for (int rapido=lento+1; rapido < quantosEstudantes; rapido++)
                if (manEstud.valorDe(lento).getNome().compareTo(manEstud.valorDe(rapido).getNome()) > 0)
                    manEstud.trocar(lento, rapido);
        ordemAtual = Ordens.porNome;
    }

    private static void ordenarPorMedia() throws Exception{
        for (int lento=0; lento < quantosEstudantes; lento++) {
            double mediaAtual = manEstud.valorDe(lento).mediaDasNotas();
            for (int rapido=lento+1; rapido < quantosEstudantes; rapido++)
                if (mediaAtual > manEstud.valorDe(rapido).mediaDasNotas())
                    manEstud.trocar(lento, rapido);
            ordemAtual = Ordens.porMedia;
        }
    }
    private static void digitarNotas() {
        out.println("Digitação de notas de estudante:\n");
        out.print("Digite o RA do(a) estudante desejado(a): ");
        String raEstudante = leitor.nextLine();
        try {
            Estudante estProc = new Estudante("00", raEstudante, "A");
            if (!manEstud.existe(estProc))
                out.println("Não há um(a) estudante com esse RA!");
            else {  // se RA foi encontrado, variável onde contém seu índice
                out.print("Quantidade de notas a serem digitadas: ");
                int quant = leitor.nextInt(); // depois de ler int, ler nextline()
                leitor.nextLine(); // necessário após nextInt() para poder ler strings a seguir

                manEstud.valorDe(onde).setQuantasNotas(quant);
                double nota;
                for (int indNota = 0; indNota < quant; indNota++) {
                    do {
                        out.printf("Digite a %da. nota:", indNota + 1);
                        nota = leitor.nextDouble();
                        if (nota >= 0 && nota <= 10)
                            break;  // sai do do-while
                        out.println("Nota inválida. Digite novamente:");
                    } while (true);
                    manEstud.valorDe(onde).setNota(nota, indNota);
                }
            }
        }
        catch (Exception erro) {
            out.println("Não foi possivel criar objeto Estudante.");
            out.println(erro.getMessage());
        }
    }


    private static void maisEstudantesAprovados() throws Exception{
        int qual = 0;
        int[] aprovados = new int[manEstud.getQtosDados()];
        for (int i = 0; i<manEstud.getQtosDados(); i++){
            for (int ind = 0; ind < manEstud.valorDe(i).getQuantasNotas(); ind ++){
                if (manEstud.valorDe(i).getNotas()[ind] >= 5)
                    aprovados[ind] ++;
            }
        }
        //Ver qual o indice do maior valor do vetor aprovados
        //Não sei e essa lógica funciona mes não posso acabar agora
        out.println("Disciplina com mais estudantes aprovados: ");
    }
}
