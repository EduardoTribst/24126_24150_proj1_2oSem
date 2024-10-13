import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import static java.lang.System.*;

public class Manutencao {
    enum Ordens {porRa, porNome, porCurso, porMedia};
    private static Ordens ordemAtual = Ordens.porRa;
    //private static Estudante[] estud;       // vetor de estudantes
    //private static int quantosEstudantes;   // tamanho lógico do vetor estud
    //private static BufferedReader arquivoDeEntrada;
    //private static BufferedWriter arquivoDeSaida;
    static Scanner leitor = new Scanner(in);
    static boolean continuarPrograma = true;
    static int onde; // índice resultante da pesquisa binária
    //-------------------------------------------------------
    static ManterEstudantes manEstud = new ManterEstudantes();
    static String[] materias;
    static int qtsMaterias;

    public static void main(String[] args) throws Exception {
        manEstud.inicializaVetor(50); // tamanho físico inicial: 50
        for (int ind=0; ind < 3; ind++)
            //out.println("Entrou no for de preencher");
            manEstud.incluirNoFinal(new Estudante());// criar objetos Estudante vazios no vetor
        manEstud.setQtosDados(0); // tamanho lógico (vetor vazio)
        manEstud.leituraDosDados("DadosEstudantes.txt"); //TALVEZ DEPOIS VER O ARQUIVO COM O SCANNER
        leituraDadosMaterias("DadosMateriasTeste.txt");
        if (continuarPrograma) {
            seletorDeOpcoes();
            //manEstud.gravarDados("../DadosEstudantes.txt");//TALVEZ DEPOIS VER O ARQUIVO COM O SCANNER
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
            out.println("9 - Mostrar estatísticas");

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
                case 9: estatisticas();
            }
        }
        while (opcao != 0);
    }

    public static void leituraDadosMaterias(String caminhoArq){
        qtsMaterias = manEstud.dados[0].getQuantasNotas();
        out.println(qtsMaterias);
        materias = new String[qtsMaterias]; // A quantidade de matérias é a mesma da de notas
        try {
            BufferedReader arq = new BufferedReader(new FileReader(caminhoArq));
            for (int i=0; i<qtsMaterias; i++){
                materias[i] = arq.readLine();
            }
            arq.close();
        }
        catch(Exception erro){
            out.println("erro na leitura de materias");
            out.println(erro.getMessage());
        }
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
        for (int ind = 0; ind < manEstud.getQtosDados(); ind++)
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
        for (int indice = 0; indice < manEstud.getQtosDados(); indice++)
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
        for (int lento=0; lento < manEstud.getQtosDados(); lento++)
            for (int rapido=lento+1; rapido < manEstud.getQtosDados(); rapido++)
                if (manEstud.valorDe(lento).getCurso().compareTo(manEstud.valorDe(rapido).getCurso()) > 0)
                    manEstud.trocar(lento, rapido);
        ordemAtual = Ordens.porCurso;
    }


    private static void ordenarPorNome() throws Exception{
        for (int lento=0; lento < manEstud.getQtosDados(); lento++)
            for (int rapido=lento+1; rapido < manEstud.getQtosDados(); rapido++)
                if (manEstud.valorDe(lento).getNome().compareTo(manEstud.valorDe(rapido).getNome()) > 0)
                    manEstud.trocar(lento, rapido);
        ordemAtual = Ordens.porNome;
    }

    private static void ordenarPorMedia() throws Exception{
        for (int lento=0; lento < manEstud.getQtosDados(); lento++) {
            double mediaAtual = manEstud.valorDe(lento).mediaDasNotas();
            for (int rapido=lento+1; rapido < manEstud.getQtosDados(); rapido++)
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

    private static void estatisticas(){
        int[] qtsAprovadosPorDisciplina = new int[qtsMaterias];
        int indEstudanteMaiorMedia = 0;
        double maiorMediaEstudante = 0; 
        double[] mediasPorDisciplina = new double[qtsMaterias];
        double maior = -1.0;
        double menor = 11.0;

        // percorre todos os estudantes e todas as suas notas
        for (int i=0; i<manEstud.getQtosDados(); i++){ 
            for (int j=0; j<qtsMaterias; j++){
                if (manEstud.dados[i].getNotas()[j] >= 5){
                    qtsAprovadosPorDisciplina[j]++;
                }
                mediasPorDisciplina[j] += manEstud.dados[i].getNotas()[j];
            }
            // vê se é a maior média
            double media = manEstud.dados[i].mediaDasNotas();
            if (media > maiorMediaEstudante){
                maiorMediaEstudante = media;
                indEstudanteMaiorMedia = i;
            }
        }

        int indDisciplinaMaiorAprovacao = 0;
        int indDisciplinaMenorAprovacao = 0;
        int maiorNumeroAprovados = -1;
        int maiorNumeroRetidos = -1;
        int indDisciplinaMaiorMedia = 0;
        int indDisciplinaMenorMedia = 0;
        // percorre o vetor das medias das materias para pegar a maior e a menor
        for (int i=0; i<qtsMaterias; i++){
            mediasPorDisciplina[i] = mediasPorDisciplina[i] / manEstud.getQtosDados(); // tira a média
            // verificar quais  são as menores e maiores médias
            if (mediasPorDisciplina[i] < menor){
                indDisciplinaMenorMedia = i;
                menor = mediasPorDisciplina[i];   
            }
            if (mediasPorDisciplina[i] > maior){
                indDisciplinaMaiorMedia = i;
                maior = mediasPorDisciplina[i];   
            }
            // utilização do vetor qtsAprovados por disciplina
            // isso é possível porque qtsAprovados tem length de qtsMaterias
            if (qtsAprovadosPorDisciplina[i] > maiorNumeroAprovados){
                maiorNumeroAprovados = qtsAprovadosPorDisciplina[i];
                indDisciplinaMaiorAprovacao = i;
            }
            // A quantidade de retidos é a qtdeTotal-qtdeAprovados
            if (manEstud.getQtosDados() - qtsAprovadosPorDisciplina[i] > maiorNumeroRetidos){
                maiorNumeroRetidos = manEstud.getQtosDados() - qtsAprovadosPorDisciplina[i]; 
                indDisciplinaMenorAprovacao = i;
            }
        }
        // Output com as estatísticas:
        out.println("A disciplina com maior aprovação foi "+ materias[indDisciplinaMaiorAprovacao]);
        out.println("A disciplina com maior retenção foi "+ materias[indDisciplinaMenorAprovacao]);
        out.println("O estudante com a meior média foi " + manEstud.dados[indEstudanteMaiorMedia].getNome());
        out.println("A matéria de sua maior nota foi "+ materias[manEstud.dados[indEstudanteMaiorMedia].getIndiceMaiorNota()]);
        out.println("A matéria de sua menor nota foi "+ materias[manEstud.dados[indEstudanteMaiorMedia].getIndiceMenorNota()]);
        out.println("Médias por disciplina");
        for (int i=0; i<qtsMaterias; i++){
            out.print(materias[i] + " ");
        }
        out.println(); // quebra a linha
        for (int i=0; i<qtsMaterias; i++){
            out.printf("%.2f   ",mediasPorDisciplina[i]);
        }
        out.println(); // quebra a linha
        out.println("Na matéria com a menor média, a melhor nota foi de "+
        manEstud.dados[getIndiceEstudanteMaiorNotaPorDisciplina(indDisciplinaMenorMedia)].getNome());
        out.println("Na matéria com a maior média, a pior nota foi de "+
        manEstud.dados[getIndiceEstudanteMenorNotaPorDisciplina(indDisciplinaMaiorMedia)].getNome()) ;
    }

    private static int getIndiceEstudanteMaiorNotaPorDisciplina(int indiceDaDisciplina){
        double maiorNota = -1;
        int indEstudMaiorNota = -1;
        for (int i=0; i<manEstud.qtosDados; i++){
            if (manEstud.dados[i].getNotas()[indiceDaDisciplina] > maiorNota){
                maiorNota = manEstud.dados[i].getNotas()[indiceDaDisciplina];
                indEstudMaiorNota = i;
            }
        }
        return indEstudMaiorNota;
    }

    private static int getIndiceEstudanteMenorNotaPorDisciplina(int indiceDaDisciplina){
        double menorNota = 11; //Maior do qua a maior nota possível para ser substituida por qualquer
        int indEstudMenorNota = -1;
        for (int i=0; i<manEstud.qtosDados; i++){
            if (manEstud.dados[i].getNotas()[indiceDaDisciplina] < menorNota){
                menorNota = manEstud.dados[i].getNotas()[indiceDaDisciplina];
                indEstudMenorNota = i;
            }
        }
        return indEstudMenorNota;
    }

}
