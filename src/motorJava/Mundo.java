package motorJava;

// import java.io.BufferedReader;
import java.io.BufferedWriter;
// import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
// import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

public class Mundo {

    private int idPessoa;
    private int idParcelas;
    private int qntdParcelasPorAgricultor;
    private int quantidadeJogadores;
    private int rodada;
    private double poluicaoMundo;

    private Scanner scanner = new Scanner(System.in);

    private ArrayList<Empresario> empresarios;
    private ArrayList<Agricultor> agricultores;
    private ArrayList<FiscalAmbiental> fiscais;
    private ArrayList<Prefeito> prefeitos;
    private ArrayList<Vereador> vereadores;

    private String separadorCSV;
    private ArrayList<Double> saldosAnteriores;
    private ArrayList<ArrayList<String>> transferenciasSent;
    private ArrayList<ArrayList<String>> transferenciasReceived;
    // private String[] transferenciasRodada;

    public Mundo() {
        this.idPessoa = 1;
        this.idParcelas = 1;
        this.qntdParcelasPorAgricultor = 6;
        this.quantidadeJogadores = 10;
        this.rodada = 1;
        this.poluicaoMundo = (double) 0.2;

        this.empresarios = new ArrayList<>();
        this.agricultores = new ArrayList<>();
        this.fiscais = new ArrayList<>();
        this.prefeitos = new ArrayList<>();
        this.vereadores = new ArrayList<>();

        this.separadorCSV = ";";
        this.saldosAnteriores = new ArrayList<>();

        int qnt = this.quantidadeJogadores + 6;
        this.transferenciasSent = new ArrayList<ArrayList<String>>(qnt);
        this.transferenciasReceived = new ArrayList<ArrayList<String>>(qnt);

        for (int i = 0; i < qnt; i++) {
            this.transferenciasSent.add(new ArrayList<String>());
            this.transferenciasReceived.add(new ArrayList<String>());
        }

    }

    public double getPoluicaoMundo() {
        return this.poluicaoMundo;
    }

    public int getTipoPessoaById(int id) {
        int aux = this.quantidadeJogadores;
        if (id > 0 && id < 5) {
            return 1;
        } else if (id > 4 && id < aux + 1) {
            return 2;
        } else if (id > aux && id < aux + 3) {
            return 3;
        } else if (id > aux + 2 && id < aux + 5) {
            return 4;
        } else if (id > aux + 4 && id < aux + 7) {
            return 5;
        } else {
            return 0;
        }
    }

    public String[][] criaListaTodos(int comeco, int idChamador) {
        int qntdEmp = 4;
        int qntdAgr = this.quantidadeJogadores - 4;
        int qntdFis = 2;
        int qntdPref = 2;
        int qntdVer = 2;

        int aux = getTipoPessoaById(idChamador);

        if (aux == 1) {
            qntdEmp--;
        } else if (aux == 2) {
            qntdAgr--;
        } else if (aux == 3) {
            qntdFis--;
        } else if (aux == 4) {
            qntdPref--;
        } else if (aux == 5) {
            qntdVer--;
        }

        String[][] lista = {
            {this.criaListaEmpresarios(comeco, idChamador), Integer.toString(qntdEmp)},
            {this.criaListaAgricultores(comeco, idChamador), Integer.toString(qntdAgr)},
            {this.criaListaFiscais(comeco, idChamador), Integer.toString(qntdFis)},
            {this.criaListaPrefeitos(comeco, idChamador), Integer.toString(qntdPref)},
            {this.criaListaVereadores(comeco, idChamador), Integer.toString(qntdVer)}
        };

        return lista;
    }

    public String[] criaListaEmpAgr(int comeco, int idChamador) {
        int qntdEmp = 4;
        int qntdAgr = this.quantidadeJogadores - 4;

        int aux = getTipoPessoaById(idChamador);

        if (aux == 1) {
            qntdEmp--;
        } else if (aux == 2) {
            qntdAgr--;
        }

        qntdEmp = qntdEmp / 2;
        qntdAgr = qntdAgr / 2;

        String alternativas = "";
        alternativas += this.criaListaEmpresarios(comeco, idChamador);
        comeco += qntdEmp;
        alternativas += this.criaListaAgricultores(comeco, idChamador);
        comeco += qntdAgr - 1;

        String[] lista = {alternativas, Integer.toString(comeco)};

        return lista;
    }

    public String[] criaListaEmpAgr(int comeco, String cidade, int idChamador) {
        int qntdEmp = 4;
        int qntdAgr = this.quantidadeJogadores - 4;

        int aux = getTipoPessoaById(idChamador);

        if (aux == 1) {
            qntdEmp--;
        } else if (aux == 2) {
            qntdAgr--;
        }

        qntdEmp = qntdEmp / 2;
        qntdAgr = qntdAgr / 2;

        String alternativas = "";
        alternativas += this.criaListaEmpresarios(comeco, cidade, idChamador);
        comeco += qntdEmp;
        alternativas += this.criaListaAgricultores(comeco, cidade, idChamador);
        comeco += qntdAgr - 1;

        String[] lista = {alternativas, Integer.toString(comeco)};

        return lista;
    }

    public String[] criaListaEmpAgr(int comeco, String cidade, int[] idChamadores) {
        String alternativas = "";

        //---------------
        for (Empresario emp : this.empresarios) {
            boolean estaArray = false;
            for (int id : idChamadores) {
                if(emp.getId() == id) estaArray = true;
            }
            if (!estaArray && emp.getCidade().equals(cidade)) {
                alternativas += "(" + comeco + ") " + emp.getNome() + "\n";
                comeco++;
            }
        }
        //---------------

        //---------------
        for (Agricultor agr : this.agricultores) {
            boolean estaArray = false;
            for (int id : idChamadores) {
                if(agr.getId() == id) estaArray = true;
            }
            if (!estaArray && agr.getCidade().equals(cidade)) {
                alternativas += "(" + comeco + ") " + agr.getNome() + "\n";
                comeco++;
            }
        }
        //---------------

        String[] lista = {alternativas, Integer.toString(comeco)};

        return lista;
    }

    public void transferirDinheiros(int idChamador) throws IOException {
        boolean voltaMenu = false;
        while (!voltaMenu) {
            int tipoPessoaChamadora = getTipoPessoaById(idChamador);
            Pessoa pessoaChamadora = null;
            int tipoPessoaDestino = 0;
            int idPessoaDestino = 0;
            String[][] listas = this.criaListaTodos(1, idChamador);
            Pessoa pessoaDestino = null;
            String nomePessoaChamadora = "";
            String nomePessoaDestino = "";

            if (tipoPessoaChamadora == 1) {
                Empresario empresario = this.getEmpresarioById(idChamador);
                nomePessoaChamadora = empresario.getNome();
                pessoaChamadora = empresario;
            } else if (tipoPessoaChamadora == 2) {
                Agricultor agricultor = this.getAgricultorById(idChamador);
                nomePessoaChamadora = agricultor.getNome();
                pessoaChamadora = agricultor;
            } else if (tipoPessoaChamadora == 3) {
                FiscalAmbiental fis = this.getFiscalById(idChamador);
                nomePessoaChamadora = fis.getNome() + " (Fiscal Ambiental)";
                pessoaChamadora = fis;
            } else if (tipoPessoaChamadora == 4) {
                Prefeito prefeito = this.getPrefeitoById(idChamador);
                nomePessoaChamadora = prefeito.getNome() + " (Prefeito)";
                pessoaChamadora = prefeito;
            } else if (tipoPessoaChamadora == 5) {
                Vereador vereador = this.getVereadorById(idChamador);
                nomePessoaChamadora = vereador.getNome() + " (Vereador)";
                pessoaChamadora = vereador;
            }

            System.out.println("Para quem?");
            tipoPessoaDestino = askAnswer(this.scanner, "(1) Empresario\n(2) Agricultor\n(3) Fiscal\n(4) Prefeito\n(5) Vereador\n(6) Voltar", 6);

            boolean trocaTipoPessoa = false;
            if (tipoPessoaDestino == 6) {
                voltaMenu = true;
                trocaTipoPessoa = true;
            }

            while (!trocaTipoPessoa) {
                int quantidadeAlternativas = Integer.parseInt(listas[tipoPessoaDestino - 1][1]) + 1;
                String alternativas = listas[tipoPessoaDestino - 1][0] + "(" + quantidadeAlternativas + ") Troca Tipo Pessoa";
                idPessoaDestino = askAnswer(this.scanner, alternativas, quantidadeAlternativas);

                if (idPessoaDestino == quantidadeAlternativas) {
                    trocaTipoPessoa = true;
                } else {
                    if (tipoPessoaDestino == 1) {

                        if (tipoPessoaDestino == tipoPessoaChamadora) {
                            if (idPessoaDestino >= idChamador) {
                                idPessoaDestino++;
                            }
                        }
                        Empresario empresario = this.getEmpresarioById(idPessoaDestino);
                        nomePessoaDestino = empresario.getNome();
                        pessoaDestino = empresario;
                    } else if (tipoPessoaDestino == 2) {
                        idPessoaDestino += 4;
                        if (tipoPessoaDestino == tipoPessoaChamadora) {
                            if (idPessoaDestino >= idChamador) {
                                idPessoaDestino++;
                            }
                        }
                        Agricultor agricultor = this.getAgricultorById(idPessoaDestino);
                        nomePessoaDestino = agricultor.getNome();
                        pessoaDestino = agricultor;
                    } else if (tipoPessoaDestino == 3) {
                        idPessoaDestino += (this.quantidadeJogadores);
                        if (tipoPessoaDestino == tipoPessoaChamadora) {
                            if (idPessoaDestino >= idChamador) {
                                idPessoaDestino++;
                            }
                        }
                        FiscalAmbiental fis = this.getFiscalById(idPessoaDestino);
                        nomePessoaDestino = fis.getNome() + " (Fiscal Ambiental)";
                        pessoaDestino = fis;
                    } else if (tipoPessoaDestino == 4) {
                        idPessoaDestino += (this.quantidadeJogadores + 2);
                        if (tipoPessoaDestino == tipoPessoaChamadora) {
                            if (idPessoaDestino >= idChamador) {
                                idPessoaDestino++;
                            }
                        }
                        Prefeito prefeito = this.getPrefeitoById(idPessoaDestino);
                        nomePessoaDestino = prefeito.getNome() + " (Prefeito)";
                        pessoaDestino = prefeito;
                    } else if (tipoPessoaDestino == 5) {
                        idPessoaDestino += (this.quantidadeJogadores + 4);
                        if (tipoPessoaDestino == tipoPessoaChamadora) {
                            if (idPessoaDestino >= idChamador) {
                                idPessoaDestino++;
                            }
                        }
                        Vereador vereador = this.getVereadorById(idPessoaDestino);
                        nomePessoaDestino = vereador.getNome() + " (Vereador)";
                        pessoaDestino = vereador;
                    }

                    System.out.println("Quantos Dinheiros?");
                    double dinheiros = askDouble(this.scanner);

                    // this.atualizaArquivoTRANSFERENCIA(idChamador, idPessoaDestino, dinheiros);

                    this.transferenciasSent.get(pessoaChamadora.getId()-1).add("Mandou D$ " + dinheiros + " para " + nomePessoaDestino);
                    this.transferenciasReceived.get(idPessoaDestino-1).add("Recebeu D$ " + dinheiros + " de " + nomePessoaChamadora);

                    boolean retorno = pessoaChamadora.negociacaoCapital(dinheiros, pessoaDestino);

                    this.colocaArquivoLog("" + retorno + ". " + "D$ " + dinheiros + " transferidos de " + nomePessoaChamadora + " para " + nomePessoaDestino + ".");

                    this.colocaLogCSV("transferencia" + this.separadorCSV + nomePessoaChamadora + this.separadorCSV + nomePessoaDestino + this.separadorCSV + dinheiros);

                    System.out.println("" + retorno + ". " + "D$ " + dinheiros + " transferidos de " + nomePessoaChamadora + " para " + nomePessoaDestino + ".");

                    voltaMenu = true;
                    trocaTipoPessoa = true;
                }
            }

        }

    }

    /**
     * Metodos referentes a classe de Empresario
     */
    public void criaEmpresario(String setor, String nome) {
        Empresario emp;
        if (setor.equals("semente") || setor.equals("maquina")) {
            if (setor.equals("semente")) {
                emp = new Empresario(this.idPessoa, 0, nome, "Atlantis");
            } else {
                emp = new Empresario(this.idPessoa, 2, nome, "Atlantis");
            }
        } else {
            if (setor.equals("fertilizante")) {
                emp = new Empresario(this.idPessoa, 1, nome, "Cidadela");
            } else {
                emp = new Empresario(this.idPessoa, 3, nome, "Cidadela");
            }
        }
        this.empresarios.add(emp);
        this.idPessoa++;
    }

    public Empresario getEmpresarioById(int id) {
        for (Empresario emp : this.empresarios) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }

    public void imprimeEmpresarios() {
        for (Empresario emp : this.empresarios) {
            emp.consultaDados();
        }
    }

    public String criaListaEmpresarios(int comeco) {
        String lista = "";
        for (Empresario emp : this.empresarios) {
            lista += "(" + comeco + ") " + emp.getNome() + "\n";
            comeco++;
        }
        return lista;
    }

    public String criaListaEmpresarios(int comeco, int idChamador) {
        String lista = "";
        for (Empresario emp : this.empresarios) {
            if (emp.getId() != idChamador) {
                lista += "(" + comeco + ") " + emp.getNome() + "\n";
                comeco++;
            }
        }
        return lista;
    }

    public String criaListaEmpresarios(int comeco, String cidade, int idChamador) {
        String lista = "";
        for (Empresario emp : this.empresarios) {
            if (emp.getCidade().equals(cidade) && emp.getId() != idChamador) {
                lista += "(" + comeco + ") " + emp.getNome() + "\n";
                comeco++;
            }
        }
        return lista;
    }

    public int getTipoProdutoById(int id) {
        if (id <= 0) {
            return 0;
        } else if (id < 4) {
            return 1;
        } else if (id < 7) {
            return 2;
        } else if (id < 11) {
            return 3;
        } else if (id < 14) {
            return 4;
        }
        return 0;
    }

    public void venda() {
        boolean terminarVenda = false;
        while (!terminarVenda) {
            System.out.println("");

            String listaAgricultores = criaListaAgricultores(1);
            listaAgricultores += "(" + (this.quantidadeJogadores - 3) + ") Terminar Venda\n";
            int agr = askAnswer(this.scanner, listaAgricultores, this.quantidadeJogadores - 3);
            boolean trocaAgr = false;

            if (agr == (this.quantidadeJogadores - 3)) {
                terminarVenda = true;
                trocaAgr = true;
            }

            while (!trocaAgr) {

                int parcela = askAnswer(this.scanner, "(1) P1\n(2) P2\n(3) P3\n(4) P4\n(5) P5\n(6) P6\n(7) Comprar para outro Agricultor", 7);
                boolean trocaParcela = false;

                if (parcela == 7) {
                    trocaAgr = true;
                    trocaParcela = true;
                }
                parcela--;
                while (!trocaParcela) {

                    String[] produtos = {
                        "(1) Hortalica\n(2) Arroz\n(3) Soja\n",
                        "(4) F. Comum\n(5) F. Premium\n(6) F. Super Premium\n",
                        "(7) Maquinas 1\n(8) Maquinas 2\n(9) Maquinas 3",
                        "(10) Pulverizador",
                        "\n(11) A. Comum\n(12) A. Premium\n(13) A. Super Premium"
                    };

                    System.out.println("");
                    if (!this.agricultores.get(agr - 1).checkProdutoParcela(parcela, 1)) {
                        System.out.println(produtos[0]);
                    }

                    if (!this.agricultores.get(agr - 1).checkProdutoParcela(parcela, 2)) {
                        System.out.println(produtos[1]);
                    }

                    if (!this.agricultores.get(agr - 1).checkProdutoParcela(parcela, 3) && !this.agricultores.get(agr - 1).checkProdutoParcela(parcela, 4)) {
                        System.out.println(produtos[2]);
                        if (!this.agricultores.get(agr - 1).getPulverizadorParcela(parcela)) {
                            System.out.println(produtos[3]);
                        }
                        System.out.println(produtos[4]);
                    } else if (!this.agricultores.get(agr - 1).getPulverizadorParcela(parcela)) {
                        System.out.println(produtos[3]);
                    }

                    if (this.agricultores.get(agr - 1).checkProdutoParcela(parcela, 1)
                            && this.agricultores.get(agr - 1).checkProdutoParcela(parcela, 2)
                            && this.agricultores.get(agr - 1).checkProdutoParcela(parcela, 3)
                            && this.agricultores.get(agr - 1).getPulverizadorParcela(parcela)
                            && this.agricultores.get(agr - 1).checkProdutoParcela(parcela, 4)) {
                        trocaParcela = true;
                    } else {
                        boolean trocaProd = false;

                        while (!trocaProd) {
                            System.out.println("\n(14) Comprar para outra Parcela\n");
                            int idProd = askAnswer(this.scanner, "", 14);
                            if (idProd == 14) {
                                trocaParcela = true;
                                trocaProd = true;
                            } else {
                                if (((idProd > 0 && idProd < 4) && (this.agricultores.get(agr - 1).checkProdutoParcela(parcela, 1) == true))
                                        || ((idProd > 3 && idProd < 7) && (this.agricultores.get(agr - 1).checkProdutoParcela(parcela, 2) == true))
                                        || ((idProd == 10) && (this.agricultores.get(agr - 1).getPulverizadorParcela(parcela) == true))
                                        || (((idProd > 6 && idProd < 10)) && (this.agricultores.get(agr - 1).checkProdutoParcela(parcela, 3) == true))
                                        || ((idProd > 10 && idProd < 14) && (this.agricultores.get(agr - 1).checkProdutoParcela(parcela, 4) == true))) {
                                    System.out.println("Voce ja escolheu um produto desse tipo. Va em Agricultores > Troca Produto se deseja colocar outro desse tipo nessa parcela.");
                                    trocaProd = true;
                                } else {
                                    int tipoProduto = this.getTipoProdutoById(idProd);

                                    int preco = askAnswer(this.scanner, "Preco da venda:\n (1) Abaixo (- D$5)\n (2) Normal\n (3) Acima (+ D$5)\n (4) Voltar para Produtos", 4);

                                    if (preco != 4) {

                                        int prod = this.empresarios.get(tipoProduto - 1).getIndiceProdutoById(idProd);

                                        this.empresarios.get(tipoProduto - 1).venderAlugar(prod, this.agricultores.get(agr - 1), parcela, preco - 1, this.poluicaoMundo);

                                        String precoString = "";
                                        if (preco == 1) {
                                            precoString = "baixo";
                                        } else if (preco == 2) {
                                            precoString = "normal";
                                        } else if (preco == 3) {
                                            precoString = "alto";
                                        }

                                        this.colocaArquivoLog("Empresario " + this.empresarios.get(tipoProduto - 1).getNome() + " vendeu/alugou o produto " + this.empresarios.get(tipoProduto - 1).getTipoProduto(prod) + " para o agricultor " + this.agricultores.get(agr - 1).getNome() + " na parcela " + (parcela + 1) + " pelo preco " + precoString + "");

                                        this.colocaLogCSV("venda/aluguel" + this.separadorCSV + this.empresarios.get(tipoProduto - 1).getNome() + this.separadorCSV + this.agricultores.get(agr - 1).getNome() + this.separadorCSV + (parcela + 1) + this.separadorCSV + this.empresarios.get(tipoProduto - 1).getTipoProduto(prod) + this.separadorCSV + precoString);

                                        System.out.println("Empresario " + this.empresarios.get(tipoProduto - 1).getNome() + " vendeu/alugou o produto " + this.empresarios.get(tipoProduto - 1).getTipoProduto(prod) + " para o agricultor " + this.agricultores.get(agr - 1).getNome() + " na parcela " + (parcela + 1) + " pelo preco " + precoString + "");

                                        trocaProd = true;

                                    } else {
                                        trocaProd = true;
                                    }
                                }
                            }
                        }

                    }
                }

            }

        }

    }

    /**
     * Metodos referentes a classe de Agricultor
     */
    public void criaAgricultor(String nome, String cidade) {
        Agricultor agr = new Agricultor(this.idPessoa, this.qntdParcelasPorAgricultor, nome, cidade, this.idParcelas);
        this.agricultores.add(agr);
        this.idPessoa++;
        this.idParcelas += this.qntdParcelasPorAgricultor;
    }

    public Agricultor getAgricultorById(int id) {
        for (Agricultor agr : this.agricultores) {
            if (agr.getId() == id) {
                return agr;
            }
        }
        return null;
    }

    public void imprimeAgricultores() {
        for (Agricultor agr : this.agricultores) {
            agr.consultaDados();
        }
    }

    public String criaListaAgricultores(int comeco) {
        String lista = "";
        for (Agricultor agr : this.agricultores) {
            lista += "(" + comeco + ") " + agr.getNome() + "\n";
            comeco++;
        }
        return lista;
    }

    public String criaListaAgricultores(int comeco, int idChamador) {
        String lista = "";
        for (Agricultor agr : this.agricultores) {
            if (agr.getId() != idChamador) {
                lista += "(" + comeco + ") " + agr.getNome() + "\n";
                comeco++;
            }
        }
        return lista;
    }

    public String criaListaAgricultores(int comeco, String cidade, int idChamador) {
        String lista = "";
        for (Agricultor agr : this.agricultores) {
            if (agr.getCidade().equals(cidade) && agr.getId() != idChamador) {
                lista += "(" + comeco + ") " + agr.getNome() + "\n";
                comeco++;
            }
        }
        return lista;
    }

    public void devolver() {
        boolean voltaMenu = false;
        while (!voltaMenu) {
            String listaAgricultores = criaListaAgricultores(1);
            int numAlternativas = this.quantidadeJogadores - 3;
            listaAgricultores += "(" + numAlternativas + ") Voltar Menu Principal\n";
            int agr = askAnswer(this.scanner, listaAgricultores, numAlternativas);

            boolean trocaAgr = false;

            if (agr == numAlternativas) {
                System.out.println("Voltando para o Menu Principal...");
                voltaMenu = true;
                trocaAgr = true;
            }

            while (!trocaAgr) {
                int parcela = askAnswer(this.scanner, "(1) P1\n(2) P2\n(3) P3\n(4) P4\n(5) P5\n(6) P6\n(7) Devolver de outro Agricultor", 7);

                boolean trocaPar = false;

                if (parcela == 7) {
                    trocaAgr = true;
                    trocaPar = true;
                }

                while (!trocaPar) {
                    String[] produtos = this.agricultores.get(agr - 1).criaListaProdutosParcela(1, parcela - 1);
                    produtos[0] += "(" + produtos[2] + ") Escolher outra Parcela\n";

                    if (produtos[1].equals("")) {
                        int prod = askAnswer(this.scanner, produtos[0], 5);

                        boolean trocaProd = false;

                        if (prod == Integer.parseInt(produtos[2])) {
                            trocaPar = true;
                            trocaProd = true;
                        } else if ((this.agricultores.get(agr - 1).checkProdutoParcela(parcela - 1, prod) != true && prod != 4)
                                || (this.agricultores.get(agr - 1).getPulverizadorParcela(parcela - 1) != true && prod == 4)) {
                            System.out.println("Valor indisponivel. Tente novamente, por favor.");
                            trocaProd = true;
                        }

                        while (!trocaProd) {
                            int preco = askAnswer(this.scanner, "(1) Baixo\n(2) Normal\n(3) Alto\n(4) Escolher outro Produto\n", 4);

                            if (preco == 4) {
                                trocaProd = true;
                            } else {

                                double precoFinal = 0;
                                int prodId = this.agricultores.get(agr - 1).getIdProdutoParcela(prod - 1, parcela - 1);
                                int emp = getTipoProdutoById(prodId) - 1;

                                String precoString = "normal";

                                if (preco == 1) {
                                    precoFinal -= 5;
                                    precoString = "baixo";
                                } else if (preco == 3) {
                                    precoFinal += 5;
                                    precoString = "alto";
                                }

                                this.agricultores.get(agr - 1).devolveProdutoParcela(prod - 1, parcela - 1, this.empresarios.get(emp), precoFinal);

                                this.colocaArquivoLog("Agricultor " + this.agricultores.get(agr - 1).getNome() + " devolveu o produto " + this.empresarios.get(emp).getTipoProduto(this.empresarios.get(emp).getIndiceProdutoById(prodId)) + " para o empresario " + this.empresarios.get(emp).getNome() + " oriundo da parcela " + parcela + " pelo preco " + precoString + "");

                                this.colocaLogCSV("devolucao" + this.separadorCSV + this.agricultores.get(agr - 1).getNome() + this.separadorCSV + parcela + this.separadorCSV + this.empresarios.get(emp).getNome() + this.separadorCSV + this.empresarios.get(emp).getTipoProduto(this.empresarios.get(emp).getIndiceProdutoById(prodId)) + this.separadorCSV + precoString);

                                System.out.println("Agricultor " + this.agricultores.get(agr - 1).getNome() + " devolveu o produto " + this.empresarios.get(emp).getTipoProduto(this.empresarios.get(emp).getIndiceProdutoById(prodId)) + " para o empresario " + this.empresarios.get(emp).getNome() + " oriundo da parcela " + parcela + " pelo preco " + precoString + "");
                                trocaProd = true;
                            }

                        }
                    } else {
                        System.out.println(produtos[1]);
                        trocaPar = true;
                    }

                }

            }

        }

    }

    /**
     * Metodos referentes a classe de FiscalAmbiental
     */
    public void criaFiscal(int idEleito, String nomeEleito) {
        FiscalAmbiental fis;
        if (idEleito % 2 != 0) {
            fis = new FiscalAmbiental(this.idPessoa, nomeEleito, "Atlantis", idEleito);
        } else {
            fis = new FiscalAmbiental(this.idPessoa, nomeEleito, "Cidadela", idEleito);
        }
        this.fiscais.add(fis);
        this.idPessoa++;
    }

    public FiscalAmbiental getFiscalById(int id) {
        for (FiscalAmbiental fis : this.fiscais) {
            if (fis.getId() == id) {
                return fis;
            }
        }
        return null;
    }

    public void imprimeFiscais() {
        this.fiscais.forEach(fis -> {
            fis.consultaDados();
        });
    }

    public String imprimeFiscaisString(int etapa) {
        String dados = "";
        for (FiscalAmbiental fis : this.fiscais) {
            dados += "\n========================================\nRodada: " + this.rodada + ". Etapa: " + etapa + ".\n";

            Pessoa pessoa = null;
            int tipoPessoa = getTipoPessoaById(fis.getIdEleito());
            if (tipoPessoa == 1) {
                Empresario emp = getEmpresarioById(fis.getIdEleito());
                pessoa = emp;
            } else if (tipoPessoa == 2) {
                Agricultor agr = getAgricultorById(fis.getIdEleito());
                pessoa = agr;
            }
            double saldoVelho = fis.getSaldo();
            fis.finalizarRodada(pessoa);

            dados += "Saldo antes de transferir para conta pessoal: " + saldoVelho + ".\n" + fis.consultaDadosString() + "Poluicao Mundial: " + this.poluicaoMundo + "\n========================================\n";
        }
        return dados;
    }

    public String criaListaFiscais(int comeco) {
        String lista = "";
        for (FiscalAmbiental fis : this.fiscais) {
            lista += "(" + comeco + ") " + fis.getNome() + " - " + fis.getCidade() + "\n";
            comeco++;
        }
        return lista;
    }

    public String criaListaFiscais(int comeco, int idChamador) {
        String lista = "";
        for (FiscalAmbiental fis : this.fiscais) {
            if (fis.getId() != idChamador) {
                lista += "(" + comeco + ") " + fis.getNome() + " - " + fis.getCidade() + "\n";
                comeco++;
            }
        }
        return lista;
    }

    public String criaListaFiscais(int comeco, String cidade) {
        String lista = "";
        for (FiscalAmbiental fis : this.fiscais) {
            if (fis.getCidade().equals(cidade)) {
                lista += "(" + comeco + ") " + fis.getNome() + "\n";
                comeco++;
            }
        }
        return lista;
    }

    public String getPedidosFiscais(int etapa) {
        String pedidos = "";
        for (FiscalAmbiental fis : this.fiscais) {
            pedidos += "\n========================================\nRodada: " + this.rodada + ". Etapa: " + etapa + ".\nID: " + fis.getId() + "\nNome: " + fis.getNome() + "\nFiscal de " + fis.getCidade() + "\nPedidos:\n" + fis.getPedidos() + "\n========================================\n";
        }
        return pedidos;
    }

    public void multar(int idFiscal, int idMultado, int tipoMulta) {
        FiscalAmbiental fis = getFiscalById(idFiscal);
        int cidade = 0;
        if (fis.getCidade().equals("Cidadela")) {
            cidade = 1;
        }
        int tipoMultado = getTipoPessoaById(idMultado);
        String nomeMultado = "";

        double multa = 0;

        if (tipoMultado == 1) {
            Empresario multado = getEmpresarioById(idMultado);
            multa = fis.multar(multado, this.prefeitos.get(cidade), tipoMulta);
            nomeMultado = multado.getNome();
        } else if (tipoMultado == 2) {
            Agricultor multado = getAgricultorById(idMultado);
            multa = fis.multar(multado, this.prefeitos.get(cidade), tipoMulta);
            nomeMultado = multado.getNome();
        }
        this.colocaArquivoLog("Fiscal " + fis.getNome() + " multou a pessoa de nome " + nomeMultado + " em D$ " + multa + "");
        this.colocaLogCSV("multa" + this.separadorCSV + fis.getNome() + this.separadorCSV + nomeMultado + this.separadorCSV + multa);
        System.out.println("Fiscal " + fis.getNome() + " multou a pessoa de nome " + nomeMultado + " em D$ " + multa + "");

    }

    /**
     * Cargos Politicos
     */
    public void criaCargosPoliticos() {
        this.fiscais.add(0, new FiscalAmbiental(this.idPessoa, " ", "Atlantis", 0));
        this.idPessoa++;
        this.fiscais.add(1, new FiscalAmbiental(this.idPessoa, " ", "Cidadela", 0));
        this.idPessoa++;
        this.prefeitos.add(0, new Prefeito(this.idPessoa, "", "Atlantis"));
        this.idPessoa++;
        this.prefeitos.add(1, new Prefeito(this.idPessoa, "", "Cidadela"));
        this.idPessoa++;
        this.vereadores.add(0, new Vereador(this.idPessoa, "", "Atlantis", 0));
        this.idPessoa++;
        this.vereadores.add(1, new Vereador(this.idPessoa, "", "Cidadela", 0));
        this.idPessoa++;
    }

    public void eleicao(){
        int idEleito = 0;
        String[] lista;
        boolean condicao = false;

        int altFis = 0;
        int altPref = 0;
        int altVer = 0;

        while (!condicao) {
            System.out.println("Quem foi eleito em Atlantis como Fiscal?");
            lista = this.criaListaEmpAgr(1, "Atlantis", 0);
            altFis = askAnswer(this.scanner, lista[0], Integer.parseInt(lista[1]));
            idEleito = altFis * 2 - 1;
            condicao = this.eleger(idEleito, 0);
        }
        condicao = false;

        while (!condicao) {
            System.out.println("Quem foi eleito em Atlantis como Prefeito?");
            lista = this.criaListaEmpAgr(1, "Atlantis", this.fiscais.get(0).getIdEleito());
            altPref = askAnswer(this.scanner, lista[0], Integer.parseInt(lista[1]));
            idEleito = altPref * 2 - 1;
            if (altPref >= altFis) {
                idEleito += 2;
            }
            condicao = this.eleger(idEleito, 1);
        }
        condicao = false;

        while (!condicao) {
            System.out.println("Quem foi eleito em Atlantis como Vereador?");
            int[] idChamadores = {this.fiscais.get(0).getIdEleito(), this.prefeitos.get(0).getIdEleito()};
            lista = this.criaListaEmpAgr(1, "Atlantis", idChamadores);
            altVer = askAnswer(this.scanner, lista[0], Integer.parseInt(lista[1]));
            idEleito = altVer * 2 - 1;
            if(altFis == 1){
                if(altVer >= altPref) idEleito += 4;
                else idEleito += 2;
            }
            else if(altFis == 2){
                if(altVer >= altPref) idEleito += 4;
                else if((altVer >= (altPref/2)) && (altPref != 2)) idEleito += 2;
            }
            else if(altFis == 3){
                if(((altVer >= altPref) && (altPref != 1) ) || ((altVer > altPref) && (altPref == 1))) idEleito += 4;
                else if(altVer <= Math.round(altPref/2) && (altPref != 1)) idEleito += 0;
                else idEleito += 2;
            }
            else if(altFis == 4 || altFis == 5){ // Caso altFis = 5, eh o mesmo que altFis = 4, mas sem o else if
                if(altVer < altPref) idEleito += 0;
                else if(altVer == 3 && altFis != 5) idEleito += 4;
                else idEleito += 2;
            }
            condicao = this.eleger(idEleito, 2);
        }
        condicao = false;

        while (!condicao) {
            System.out.println("Quem foi eleito em Cidadela como Fiscal?");
            lista = this.criaListaEmpAgr(1, "Cidadela", 0);
            altFis = askAnswer(this.scanner, lista[0], Integer.parseInt(lista[1]));
            idEleito = altFis * 2;
            condicao = this.eleger(idEleito, 0);
        }
        condicao = false;

        while (!condicao) {
            System.out.println("Quem foi eleito em Cidadela como Prefeito?");
            lista = this.criaListaEmpAgr(1, "Cidadela", this.fiscais.get(1).getIdEleito());
            altPref = askAnswer(this.scanner, lista[0], Integer.parseInt(lista[1]));
            idEleito = altPref * 2;
            if (altPref >= altFis) {
                idEleito += 2;
            }
            condicao = this.eleger(idEleito, 1);
        }
        condicao = false;

        while (!condicao) {
            System.out.println("Quem foi eleito em Cidadela como Vereador?");
            int[] idChamadores = {this.fiscais.get(1).getIdEleito(), this.prefeitos.get(1).getIdEleito()};
            lista = this.criaListaEmpAgr(1, "Cidadela", idChamadores);
            altVer = askAnswer(this.scanner, lista[0], Integer.parseInt(lista[1]));
            idEleito = altVer * 2;
            if(altFis == 1){
                if(altVer >= altPref) idEleito += 4;
                else idEleito += 2;
            }
            else if(altFis == 2){
                if(altVer >= altPref) idEleito += 4;
                else if(altVer >= 2) idEleito += 2;
            }
            else if(altFis == 3){
                if(((altVer >= altPref) && (altPref != 1) ) || ((altVer > altPref) && (altPref == 1))) idEleito += 4;
                else if(altVer <= Math.round(altPref/2) && (altPref != 1)) idEleito += 0;
                else idEleito += 2;
            }
            else if(altFis == 4 || altFis == 5){ // Caso altFis = 5, eh o mesmo que altFis = 4, mas sem o else if
                if(altVer < altPref) idEleito += 0;
                else if(altVer == 3 && altFis != 5) idEleito += 4;
                else idEleito += 2;
            }
            condicao = this.eleger(idEleito, 2);
        }
        condicao = false;
    }

    public boolean eleger(int idEleito, int cargo) {
        int tipo = 0;
        Pessoa pessoa = null;
        String cidade = "";
        String cargoString = "";

        tipo = getTipoPessoaById(idEleito);
        if(tipo == 1) {
            pessoa = getEmpresarioById(idEleito);
        }
        else if(tipo == 2) {
            pessoa = getAgricultorById(idEleito);
        }

        if (cargo == 0){
            if (idEleito % 2 != 0) {
                this.fiscais.get(0).eleger(pessoa.getId(), pessoa.getNome());
                cidade = "Atlantis";
            } else {
                this.fiscais.get(1).eleger(pessoa.getId(), pessoa.getNome());
                cidade = "Cidadela";
            }
            cargoString = "Fiscal";
        }
        else if (cargo == 1) { // Prefeito
            if (idEleito % 2 != 0) {
                this.prefeitos.get(0).eleger(pessoa.getId(), pessoa.getNome());
                cidade = "Atlantis";
            } else {
                this.prefeitos.get(1).eleger(pessoa.getId(), pessoa.getNome());
                cidade = "Cidadela";
            }
            cargoString = "Prefeito";
        }
        else if (cargo == 2){
            if (idEleito % 2 != 0) {
                this.vereadores.get(0).eleger(pessoa.getId(), pessoa.getNome());
                cidade = "Atlantis";
            } else {
                this.vereadores.get(1).eleger(pessoa.getId(), pessoa.getNome());
                cidade = "Cidadela";
            }
            cargoString = "Vereador";
        }

        this.colocaArquivoLog("" + pessoa.getNome().substring(0, 1).toUpperCase() + pessoa.getNome().substring(1).toLowerCase() + " eleito como " + cargoString + " na cidade de " + cidade + "");
        this.colocaLogCSV("eleicao" + this.separadorCSV + cargoString + this.separadorCSV + cidade + this.separadorCSV + pessoa.getNome());
        System.out.println("\"" + pessoa.getNome().substring(0, 1).toUpperCase() + pessoa.getNome().substring(1).toLowerCase() + "\" eleito como " + cargoString + " na cidade de " + cidade + ".");

        return true;
    }

    /**
     * Metodos referentes a classe de Prefeito
     */
    public Prefeito getPrefeitoById(int id) {
        for (Prefeito pref : this.prefeitos) {
            if (pref.getId() == id) {
                return pref;
            }
        }
        return null;
    }

    public void imprimePrefeitos() {
        this.prefeitos.forEach(pref -> {
            pref.consultaDados();
        });
    }

    public String criaListaAcoesPrefeito(int comeco) {
        String dados = "";
        dados += this.prefeitos.get(0).criaListaAcoes(comeco);

        return dados;
    }

    public String criaListaPrefeitos(int comeco) {
        String lista = "";
        for (Prefeito pref : this.prefeitos) {
            lista += "(" + comeco + ") " + pref.getNome() + " - " + pref.getCidade() + "\n";
            comeco++;
        }
        return lista;
    }

    public String criaListaPrefeitos(int comeco, int idChamador) {
        String lista = "";
        for (Prefeito pref : this.prefeitos) {
            if (pref.getId() != idChamador) {
                lista += "(" + comeco + ") " + pref.getNome() + " - " + pref.getCidade() + "\n";
                comeco++;
            }
        }
        return lista;
    }

    public String criaListaPrefeitos(int comeco, String cidade) {
        String lista = "";
        for (Prefeito pref : this.prefeitos) {
            if (pref.getCidade().equals(cidade)) {
                lista += "(" + comeco + ") " + pref.getNome() + "\n";
                comeco++;
            }
        }
        return lista;
    }

    public void cobrarImpostos() {
        double imposto = 0;
        for (Empresario emp : this.empresarios) {
            if (emp.getCidade().equals("Atlantis")) {
                imposto = this.prefeitos.get(0).cobrarImposto(emp);

                this.colocaArquivoLog("Prefeito " + this.prefeitos.get(0).getNome() + " cobrou um imposto de D$" + imposto + " do empresario " + emp.getNome() + "");

                this.colocaLogCSV("imposto" + this.separadorCSV + "prefeito " + this.prefeitos.get(0).getNome() + this.separadorCSV + emp.getNome() + this.separadorCSV + imposto);
            } else {
                imposto = this.prefeitos.get(1).cobrarImposto(emp);
                this.colocaArquivoLog("Prefeito " + this.prefeitos.get(1).getNome() + " cobrou um imposto de D$" + imposto + " do empresario " + emp.getNome() + "");
                this.colocaLogCSV("imposto" + this.separadorCSV + "prefeito " + this.prefeitos.get(1).getNome() + this.separadorCSV + emp.getNome() + this.separadorCSV + imposto);
            }
        }
        for (Agricultor agr : this.agricultores) {
            if (agr.getCidade().equals("Atlantis")) {
                imposto = this.prefeitos.get(0).cobrarImposto(agr);
                this.colocaArquivoLog("Prefeito " + this.prefeitos.get(0).getNome() + " cobrou um imposto de D$" + imposto + " do agricultor " + agr.getNome() + "");
                this.colocaLogCSV("imposto" + this.separadorCSV + "prefeito " + this.prefeitos.get(0).getNome() + this.separadorCSV + agr.getNome() + this.separadorCSV + imposto);
            } else {
                imposto = this.prefeitos.get(1).cobrarImposto(agr);
                this.colocaArquivoLog("Prefeito " + this.prefeitos.get(1).getNome() + " cobrou um imposto de D$" + imposto + " do agricultor " + agr.getNome() + "");
                this.colocaLogCSV("imposto" + this.separadorCSV + "prefeito " + this.prefeitos.get(1).getNome() + this.separadorCSV + agr.getNome() + this.separadorCSV + imposto);
            }
        }
    }

    /**
     * Metodos referentes a classe de Vereador
     */
    public Vereador getVereadorById(int id) {
        for (Vereador ver : this.vereadores) {
            if (ver.getId() == id) {
                return ver;
            }
        }
        return null;
    }

    public void imprimeVereadores() {
        this.vereadores.forEach(ver -> {
            ver.consultaDados();
        });
    }

    public String imprimeVereadoresString(int etapa) {
        String dados = "";
        for (Vereador ver : this.vereadores) {
            dados += "\n========================================\nRodada: " + this.rodada + ". Etapa: " + etapa + ".\n" + "";

            Pessoa pessoa = null;
            int tipoPessoa = getTipoPessoaById(ver.getIdEleito());
            if (tipoPessoa == 1) {
                Empresario emp = getEmpresarioById(ver.getIdEleito());
                pessoa = emp;
            } else if (tipoPessoa == 2) {
                Agricultor agr = getAgricultorById(ver.getIdEleito());
                pessoa = agr;
            }
            double saldoVelho = ver.getSaldo();
            ver.finalizarRodada(pessoa);

            dados += "Saldo antes de transferir para conta pessoal: " + saldoVelho + ".\n" + ver.consultaDadosString() + "Poluicao Mundial: " + this.poluicaoMundo + "\n========================================\n";
        }
        return dados;
    }

    public String criaListaVereadores(int comeco) {
        String lista = "";
        for (Vereador ver : this.vereadores) {
            lista += "(" + comeco + ") " + ver.getNome() + " - " + ver.getCidade() + "\n";
            comeco++;
        }
        return lista;
    }

    public String criaListaVereadores(int comeco, int idChamador) {
        String lista = "";
        for (Vereador ver : this.vereadores) {
            if (ver.getId() != idChamador) {
                lista += "(" + comeco + ") " + ver.getNome() + " - " + ver.getCidade() + "\n";
                comeco++;
            }
        }
        return lista;
    }

    public String criaListaVereadores(int comeco, String cidade) {
        String lista = "";
        for (Vereador ver : this.vereadores) {
            if (ver.getCidade().equals(cidade)) {
                lista += "(" + comeco + ") " + ver.getNome() + "\n";
                comeco++;
            }
        }
        return lista;
    }

    /**
     * Metodos em relacao do jogo
     */
    public void comecarJogo() {
        System.out.println("COMECANDO");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String data = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(timestamp.getTime());
        this.saveGame("\n\n" + data + "");
        this.saveGame("\n");

        String aux = "";
        boolean condicao = false;

        System.out.println("Qual a quantidade de jogadores? (minimo 10 - nao contar os cargos eleitorais)");
        do {
            aux = this.scanner.next();
            if (Pattern.matches("[0-9][0-9]", aux)) {
                this.quantidadeJogadores = Integer.parseInt(aux);
                if (this.quantidadeJogadores > 9) {
                    condicao = true;
                }
            } else {
                System.out.println("Quantidade ou valor invalidos. Por favor, tente novamente.");
            }
        } while (!condicao);
        condicao = false;

        this.saveGame("" + this.quantidadeJogadores + "");

        String[] nomes = {"EmpSem", "EmpFer", "EmpMaq", "EmpAgr"};

        System.out.println("Criando empresarios.....");
        String setor;
        for (int i = 1; i < 5; i++) {
            if (i == 1) {
                setor = "semente";
            } else if (i == 2) {
                setor = "fertilizante";
            } else if (i == 3) {
                setor = "maquina";
            } else {
                setor = "agrotoxico";
            }

            criaEmpresario(setor, nomes[i - 1]);
        }
        System.out.println("Empresarios criados.");

        int resposta = 0;
        System.out.println("Hora de criar os Agricultores.");

        System.out.println("A quantidade de parcelas a ser separada para cada um eh " + this.qntdParcelasPorAgricultor + " parcelas. Gostaria de mudar? (1 - Sim. 2 - Nao)");
        do {
            aux = this.scanner.next();
            if (Pattern.matches("[1-2]?", aux)) {
                resposta = Integer.parseInt(aux);
                if (resposta == 1) {
                    this.saveGame("1");
                    System.out.println("Qual a quantidade de parcelas desejadas? (no minimo 6, maximo 10)");
                    do {
                        aux = this.scanner.next();
                        if (Pattern.matches("[0-9]", aux) || Pattern.matches("[0-9][0-9]", aux)) {
                            this.qntdParcelasPorAgricultor = Integer.parseInt(aux);
                            if (qntdParcelasPorAgricultor > 5 && qntdParcelasPorAgricultor < 11) {
                                this.saveGame("" + this.qntdParcelasPorAgricultor + "");
                                condicao = true;
                            }
                        } else {
                            System.out.println("Quantidade ou valor invalidos. Por favor, tente novamente.");
                        }
                    } while (!condicao);
                } else if (resposta == 2) {
                    condicao = true;
                    this.saveGame("2");
                }
            } else {
                System.out.println("Quantidade ou valor invalidos. Por favor, tente novamente.");
            }
        } while (!condicao);
        condicao = false;

        String nome;
        int numNome = 1;
        for (int i = 0; i < this.quantidadeJogadores - 4; i++) {
            if (i % 2 == 0) {
                nome = "AT" + numNome;
                criaAgricultor(nome, "Atlantis");
            } else {
                nome = "CD" + numNome;
                criaAgricultor(nome, "Cidadela");
                numNome++;
            }
        }
        System.out.println("Agricultores criados.");

        this.colocaArquivoLog("\n===============================================\n" + data + "\nRodada:" + this.rodada);
        this.colocaLogCSV("rodada " + this.rodada);

        System.out.println("Fiscais, Prefeitos e Vereadores serao decididos por votacao pelos jogadores na primeira etapa da primeira rodada.\n");

        this.criaCargosPoliticos();
        // this.criaArquivoSALDO();
        this.criaHistoricoSaldos();
        this.limpaHistoricoTransferencias();
        // this.criaArquivoTRANSFERENCIA();
        System.out.println("Termino do processo de criacao do jogo. Tenha um otimo jogo!");
    }

    public void saveGame(String comando) {
        String fileName = "saves/saves.txt";

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(comando + " ");
            writer.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ioe2) {
                    //
                }
            }
        }
    }

    public void colocaArquivoLog(String comando) {
        String fileName = "arquivoslog/log.txt";

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(comando);
            writer.newLine();
            writer.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ioe2) {
                    //
                }
            }
        }
    }

    public void colocaLogCSV(String comando) {
        String fileName = "arquivoslog/log.csv";

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(comando);
            writer.newLine();
            writer.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ioe2) {
                    //
                }
            }
        }
    }

    public void fechaRodadaLog() {
        String infos = "";
        String infosCSV = "";
        for (Empresario emp : this.empresarios) {
            infos += emp.getNome() + ": " + ((emp.getPoluicao() / 10000) * 100) + " / D$ " + emp.getSaldo() + "\n";
            infosCSV += emp.getNome() + this.separadorCSV + ((emp.getPoluicao() / 10000) * 100) + this.separadorCSV + emp.getSaldo() + "\n";
        }
        for (Agricultor agr : this.agricultores) {
            infos += agr.getNome() + ": " + ((agr.getPoluicao() / 10000) * 100) + " / D$ " + agr.getSaldo() + "\n";
            infosCSV += agr.getNome() + this.separadorCSV + ((agr.getPoluicao() / 10000) * 100) + this.separadorCSV + agr.getSaldo() + "\n";
        }
        for (FiscalAmbiental fis : this.fiscais) {
            infos += "Fiscal " + fis.getNome() + ": D$ " + fis.getSaldo() + "\n";
            infosCSV += "fiscal " + fis.getNome() + this.separadorCSV + fis.getSaldo() + "\n";
        }
        for (Prefeito pref : this.prefeitos) {
            infos += "Prefeito " + pref.getNome() + ": D$ " + pref.getCaixa() + "\n";
            infosCSV += "prefeito " + pref.getNome() + this.separadorCSV + pref.getCaixa() + "\n";
        }
        int aux = 0;
        for (Vereador ver : this.vereadores) {
            infos += "Vereador " + ver.getNome() + ": D$ " + ver.getSaldo();
            infosCSV += "vereador " + ver.getNome() + this.separadorCSV + ver.getSaldo();
            if (aux == 0) {
                infos += "\n";
                infosCSV += "\n";
            }
            aux++;
        }
        this.colocaArquivoLog("Poluicoes/Saldo dos jogadores:\n" + infos);
        this.colocaLogCSV(infosCSV);
        this.colocaArquivoLog("Poluicao Mundial: " + (this.poluicaoMundo * 100));
        String poluicaoMundo = "mundo" + this.separadorCSV + (this.poluicaoMundo * 100);

        this.colocaLogCSV(poluicaoMundo);
    }

    // MÉTODOS PARA O TRATAMENTO DE TRANSAcoES E SALDOS

    private void criaHistoricoSaldos(){
        this.saldosAnteriores.clear();

        for (Empresario emp : this.empresarios) {
            this.saldosAnteriores.add(emp.getSaldo());
        }
        for (Agricultor agr : this.agricultores) {
            this.saldosAnteriores.add(agr.getSaldo());
        }
        for (FiscalAmbiental fis : this.fiscais) {
            this.saldosAnteriores.add(fis.getSaldo());
        }
        for (Prefeito pref : this.prefeitos) {
            this.saldosAnteriores.add(pref.getCaixa());
        }
        for (Vereador ver : this.vereadores) {
            this.saldosAnteriores.add(ver.getSaldo());
        }
    }

    private void limpaHistoricoTransferencias(){
        for (ArrayList<String> transf : this.transferenciasSent) {
            transf.clear();
        }
        for (ArrayList<String> transf : this.transferenciasReceived) {
            transf.clear();
        }
        // System.out.println("Tamanho apos limpo: " + this.transferenciasSent.size());
    }

    // private void criaArquivoSALDO() {
    //     String arq = "arquivosAcoes/saldo.txt";
    //     PrintWriter inicializa = null;
    //     try {
    //         inicializa = new PrintWriter(arq, "UTF-8");
    //     } catch (Exception e) {
    //         System.out.println(e);
    //         e.printStackTrace();
    //     }
    //     String id_saldo = "";
    //     id_saldo = this.empresarios.stream().map((emp) -> emp.getId() + " " + emp.getSaldo() + "\n").reduce(id_saldo, String::concat);
    //     id_saldo = this.agricultores.stream().map((agr) -> agr.getId() + " " + agr.getSaldo() + "\n").reduce(id_saldo, String::concat);
    //     id_saldo = this.fiscais.stream().map((fis) -> fis.getId() + " " + fis.getSaldo() + "\n").reduce(id_saldo, String::concat);
    //     id_saldo = this.prefeitos.stream().map((pre) -> pre.getId() + " " + pre.getCaixa() + "\n").reduce(id_saldo, String::concat);
    //     id_saldo = this.vereadores.stream().map((ver) -> ver.getId() + " " + ver.getSaldo() + "\n").reduce(id_saldo, String::concat);
    //     inicializa.println(id_saldo);
    //     inicializa.close();
    // }

    // private void atualizaArquivoSALDO() throws IOException {
    //     String arq = "arquivosAcoes/saldo.txt";
    //     Writer limpa = new FileWriter(arq);
    //     limpa.flush();
    //     String id_saldo = "";
    //     id_saldo = this.empresarios.stream().map((emp) -> emp.getId() + " " + emp.getSaldo() + "\n").reduce(id_saldo, String::concat);
    //     id_saldo = this.agricultores.stream().map((agr) -> agr.getId() + " " + agr.getSaldo() + "\n").reduce(id_saldo, String::concat);
    //     id_saldo = this.fiscais.stream().map((fis) -> fis.getId() + " " + fis.getSaldo() + "\n").reduce(id_saldo, String::concat);
    //     id_saldo = this.prefeitos.stream().map((pre) -> pre.getId() + " " + pre.getCaixa() + "\n").reduce(id_saldo, String::concat);
    //     id_saldo = this.vereadores.stream().map((ver) -> ver.getId() + " " + ver.getSaldo() + "\n").reduce(id_saldo, String::concat);
    //     try (PrintWriter esc = new PrintWriter(arq, "UTF-8")) {
    //         esc.println(id_saldo);
    //         esc.close();
    //     }
    //     limpa.close();
    // }

    // private String getSaldoAnteriorById(int id) throws IOException {
    //     BufferedReader leitor = new BufferedReader(new FileReader("arquivosAcoes/saldo.txt"));
    //     String linha = "";
    //     String[] id_saldo = new String[2];
    //     String saldoAnterior = "";
    //     linha = leitor.readLine();
    //     while (true) {
    //         if (linha != null) {
    //             id_saldo = linha.split(" ");
    //             if (id_saldo[0].equals(Integer.toString(id))) {
    //                 saldoAnterior = id_saldo[1];
    //             }
    //         } else {
    //             break;
    //         }
    //         linha = leitor.readLine();
    //     }
    //     leitor.close();
    //     return saldoAnterior;
    // }

    // private void criaArquivoTRANSFERENCIA() {
    //     String arq = "arquivosAcoes/transferencia.txt";
    //     PrintWriter limpa = null;
    //     try {
    //         limpa = new PrintWriter(arq, "UTF-8");
    //     } catch (Exception e) {
    //         System.out.println(e);
    //         e.printStackTrace();
    //     }
    //     limpa.flush();
    //     limpa.close();
    // }

    // private void limpaArquivoTRANSFERENCIA() throws IOException {
    //     String arq = "arquivosAcoes/transferencia.txt";
    //     Writer limpa = new FileWriter(arq);
    //     limpa.flush();
    //     limpa.close();
    // }

    // private void atualizaArquivoTRANSFERENCIA(int idChamador, int idDestino, double valor) throws IOException {
    //     String arq = "arquivosAcoes/transferencia.txt";
    //     String neg = idChamador + " " + idDestino + " " + valor;
    //     String trans = "";
    //     BufferedReader leitor = new BufferedReader(new FileReader(arq));
    //     String contentData = "";
    //     contentData = leitor.readLine();
    //     while (true) {
    //         if (contentData != null) {
    //             trans += contentData + "\n";
    //         } else {
    //             break;
    //         }
    //         contentData = leitor.readLine();
    //     }
    //     leitor.close();
    //     trans += neg;
    //     try {
    //         PrintWriter atualiza = new PrintWriter(arq, "UTF-8");
    //         atualiza.println(trans);
    //         atualiza.close();
    //     } catch (Exception e) {
    //         System.out.println(e);
    //         e.printStackTrace();
    //     }

    // }

    // private String getTransferenciaById(int idChamador) throws IOException {
    //     BufferedReader leitor = new BufferedReader(new FileReader("arquivosAcoes/transferencia.txt"));
    //     String linha = "";
    //     String[] idC_idD_valor = new String[3];
    //     String trans = "";
    //     linha = leitor.readLine();
    //     while (true) {
    //         int idDestino;
    //         if (linha != null) {
    //             idC_idD_valor = linha.split(" ");
    //             if (idC_idD_valor[0].equals(Integer.toString(idChamador))) {
    //                 idDestino = Integer.parseInt(idC_idD_valor[1]);
    //                 switch (this.getTipoPessoaById(idDestino)) {
    //                     case 1:
    //                         Empresario emp = this.getEmpresarioById(idDestino);
    //                         trans += "Transferencia efetuada para Empresario: " + emp.getNome() + "  " + "Valor: " + idC_idD_valor[2] + "\n";
    //                         break;
    //                     case 2:
    //                         Agricultor agr = this.getAgricultorById(idDestino);
    //                         trans += "Transferencia efetuada para Agricultor: " + agr.getNome() + "  " + "Valor: " + idC_idD_valor[2] + "\n";
    //                         break;
    //                     case 3:
    //                         FiscalAmbiental fis = this.getFiscalById(idDestino);
    //                         trans += "Transferencia efetuada para Fiscal: " + fis.getNome() + "  " + "Valor: " + idC_idD_valor[2] + "\n";
    //                         break;
    //                     case 4:
    //                         Prefeito pre = this.getPrefeitoById(idDestino);
    //                         trans += "Transferencia efetuada para Prefeito: " + pre.getNome() + "  " + "Valor: " + idC_idD_valor[2] + "\n";
    //                         break;
    //                     default:
    //                         Vereador ver = this.getVereadorById(idDestino);
    //                         trans += "Transferencia efetuada para Vereador: " + ver.getNome() + "  " + "Valor: " + idC_idD_valor[2] + "\n";
    //                         break;
    //                 }
    //             }
    //             if (idC_idD_valor[1].equals(Integer.toString(idChamador))) {
    //                 idDestino = Integer.parseInt(idC_idD_valor[0]);
    //                 switch (this.getTipoPessoaById(idDestino)) {
    //                     case 1:
    //                         Empresario emp = this.getEmpresarioById(idDestino);
    //                         trans += "Recebeu do Empresario: " + emp.getNome() + "  " + "Valor: " + idC_idD_valor[2] + "\n";
    //                         break;
    //                     case 2:
    //                         Agricultor agr = this.getAgricultorById(idDestino);
    //                         trans += "Recebeu do Agricultor: " + agr.getNome() + "  " + "Valor: " + idC_idD_valor[2] + "\n";
    //                         break;
    //                     case 3:
    //                         FiscalAmbiental fis = this.getFiscalById(idDestino);
    //                         trans += "Recebeu do Fiscal: " + fis.getNome() + "  " + "Valor: " + idC_idD_valor[2] + "\n";
    //                         break;
    //                     case 4:
    //                         Prefeito pre = this.getPrefeitoById(idDestino);
    //                         trans += "Recebeu do Prefeito: " + pre.getNome() + "  " + "Valor: " + idC_idD_valor[2] + "\n";
    //                         break;
    //                     default:
    //                         Vereador ver = this.getVereadorById(idDestino);
    //                         trans += "Recebeu do Vereador: " + ver.getNome() + "  " + "Valor: " + idC_idD_valor[2] + "\n";
    //                         break;
    //                 }
    //             }
    //         } else {
    //             break;
    //         }
    //         linha = leitor.readLine();
    //     }
    //     leitor.close();
    //     return trans;
    // }

    //ARQUIVOS DE SAIDA
    private String setArquivoEmp(Empresario emp, int etapa) throws IOException {
        String rd = "========== Rodada: " + this.rodada + "\n";
        // String neg = this.getTransferenciaById(emp.getId());
        String neg = "* Tranferidos:\n";
        if(this.transferenciasSent.get(emp.getId()-1).size() == 0){
            neg += "Nao efetuou nenhuma transferencia\n";
        }
        else{
            for (String sent : this.transferenciasSent.get(emp.getId()-1)) {
                neg += sent + "\n";
            }
        }
        neg += "* Recebidos:\n";
        if(this.transferenciasReceived.get(emp.getId()-1).size() == 0){
            neg += "Nao recebeu nenhuma transferencia\n";
        }
        else{
            for (String received : this.transferenciasReceived.get(emp.getId()-1)) {
                neg += received + "\n";
            }
        }

        String info = "";
        info += "Nome: " + emp.getNome() + "\n\n";
        // info += "Saldo anterior: " + this.getSaldoAnteriorById(emp.getId()) + "\n\n";
        info += "Saldo anterior: " + this.saldosAnteriores.get(emp.getId()-1) + "\n\n";
        info += "* Creditos:\n";
        info += "Vendas: " + (double)emp.getProdutividade() + "\n\n";
        info += "* Debitos:\n";
        info += "Imposto pago: " + emp.getImposto() + "\n";
        if(emp.getMulta() > 0) info += "Multa: " + emp.getMulta() + "\n";
        info += "\n";
        info += "Transferencias:\n" + neg;
        // if ("".equals(neg)) {
        //     info += "Nao efetuou/recebeu nenhuma transferencia\n";
        // } else {
        //     info += "" + neg + "\n";
        // }
        info += "\n";

        int cidade = (emp.getCidade().equals("Atlantis")) ? 0 : 1;

        info += "Acoes Ambientais usadas pelo prefeito nessa rodada:\n" + this.prefeitos.get(cidade).getAcoesUsadasString() + "\n";
        info += "Mudancas de taxas feitas pelo prefeito nessa rodada:\n" + this.prefeitos.get(cidade).getTaxasMudadasString() + "\n";

        info += "Saldo atual: " + emp.getSaldo() + "\n\n";
        info += "Poluicao pessoal: " + emp.getPoluicao() + "\n";
        info += "Poluicao causada no Mundo: " + (emp.getPoluicao() / 1000) + "%\n";
        info += "Poluicao Mundial: " + (this.poluicaoMundo*100) + "%\n";

        return (rd + info);

    }

    private String setArquivoAgr(Agricultor agr, int etapa) throws IOException {
        String rd = "========== Rodada: " + this.rodada + "\n";
        // String neg = this.getTransferenciaById(agr.getId());
        String neg = "* Tranferidos:\n";
        if(this.transferenciasSent.get(agr.getId()-1).size() == 0){
            neg += "Nao efetuou nenhuma transferencia\n";
        }
        else{
            for (String sent : this.transferenciasSent.get(agr.getId()-1)) {
                neg += sent + "\n";
            }
        }
        neg += "* Recebidos:\n";
        if(this.transferenciasReceived.get(agr.getId()-1).size() == 0){
            neg += "Nao recebeu nenhuma transferencia\n";
        }
        else{
            for (String received : this.transferenciasReceived.get(agr.getId()-1)) {
                neg += received + "\n";
            }
        }

        String info = "";
        info += "Nome: " + agr.getNome() + "\n\n";
        // info += "Saldo anterior: " + this.getSaldoAnteriorById(agr.getId()) + "\n\n";
        info += "Saldo anterior: " + this.saldosAnteriores.get(agr.getId()-1) + "\n\n";
        info += "* Creditos:\n";
        info += "Produtividade: " + agr.getProdutividade() + "\n\n";
        info += "* Debitos:\n";
        info += "Imposto pago: " + agr.getImposto() + "\n";
        info += "Gastos: " + agr.getGastos() + "\n";
        if(agr.getMulta() > 0) info += "Multa: " + agr.getMulta() + "\n";
        info += "\n";
        info += "Transferencias:\n" + neg;
        // if ("".equals(neg)) {
        //     info += "Nao efetuou/recebeu nenhuma transferencia\n";
        // } else {
        //     info += "" + neg + "\n";
        // }
        info += "\n";

        int cidade = (agr.getCidade().equals("Atlantis")) ? 0 : 1;

        info += "Acoes Ambientais usadas pelo prefeito nessa rodada:\n" + this.prefeitos.get(cidade).getAcoesUsadasString() + "\n";
        info += "Mudancas de taxas feitas pelo prefeito nessa rodada:\n" + this.prefeitos.get(cidade).getTaxasMudadasString() + "\n";

        agr.finalizarSaldo();

        info += "Saldo atual: " + agr.getSaldo() + "\n\n";
        info += "Poluicao pessoal: " + agr.getPoluicao() + "\n";
        info += "Poluicao causada no mundo: " + (agr.getPoluicao() / 1000) + "%\n";
        info += "Poluicao mundial: " + (this.poluicaoMundo * 100) + "%\n";
        info += "\n";

        String infoParcela = "DADOS DAS PARCELAS: \n==========\n";
        infoParcela += agr.printContentParcelasString(etapa);

        return (rd + info + infoParcela);
    }

    private String setArquivoFiscal(FiscalAmbiental fis, int etapa) throws IOException {
        String rd = "========== Rodada: " + this.rodada + "\n";
        // String neg = this.getTransferenciaById(fis.getId());
        String neg = "* Tranferidos:\n";
        if(this.transferenciasSent.get(fis.getId()-1).size() == 0){
            neg += "Nao efetuou nenhuma transferencia\n";
        }
        else{
            for (String sent : this.transferenciasSent.get(fis.getId()-1)) {
                neg += sent + "\n";
            }
        }
        neg += "* Recebidos:\n";
        if(this.transferenciasReceived.get(fis.getId()-1).size() == 0){
            neg += "Nao recebeu nenhuma transferencia\n";
        }
        else{
            for (String received : this.transferenciasReceived.get(fis.getId()-1)) {
                neg += received + "\n";
            }
        }
        
        String info = "";
        info += "Fiscal de " + fis.getCidade() + "\n";
        info += "Nome: " + fis.getNome() + "\n\n";
        // info += "Saldo anterior: " + this.getSaldoAnteriorById(fis.getId()) + "\n\n";
        info += "Saldo anterior: " + this.saldosAnteriores.get(fis.getId() - 1) + "\n\n";
        info += "Transferencias:\n" + neg;
        // if ("".equals(neg)) {
        //     info += "Nao efetuou/recebeu nenhuma transferencia\n";
        // } else {
        //     info += "" + neg + "\n";
        // }
        info += "\n";

        if(etapa == 1) info += "Pedidos feitos ao fiscal:\n" + fis.getPedidos() + "\n\n";

        int tipoPessoa = this.getTipoPessoaById(fis.getIdEleito());
        Pessoa pessoa;

        if(tipoPessoa == 1){
            pessoa = getEmpresarioById(fis.getIdEleito());
        }
        else{
            pessoa = getAgricultorById(fis.getIdEleito());
        }

        fis.finalizarRodada(pessoa);

        info += "Saldo atual: " + fis.getSaldo() + "\n\n";
        info += "Poluicao mundial: " + (this.poluicaoMundo * 100) + "%\n";

        String empresarios = "\n==========\nEMPRESARIOS:";
        for (Empresario emp : this.empresarios) {
            if (emp.getCidade().equals(fis.getCidade())) {
                empresarios += "\n==========\n\n";
                empresarios += "Empresario: " + emp.getNome() + "\n";
                empresarios += "Setor: " + emp.getSetor() + "\n";
                empresarios += "Poluicao: " + emp.getPoluicao() + "\n";
                if(etapa == 2){
                    empresarios += "Multa paga: ";
                    if(emp.getMulta() > 0) empresarios += emp.getMulta() + "\n";
                    else empresarios += "Nao foi cobrado multa\n";
                }
            }
        }

        String agricultores = "\n==========\nAGRICULTORES:";
        for (Agricultor agr : this.agricultores) {
            if (agr.getCidade().equals(fis.getCidade())) {
                agricultores += "\n==========\n\n";
                agricultores += "Agricultor: " + agr.getNome() + "\n";
                agricultores += "Poluicao media: " + agr.getPoluicao() + "\n";

                if(etapa == 2){
                    agricultores += "Multa paga: ";
                    if(agr.getMulta() > 0) agricultores += agr.getMulta() + "\n";
                    else agricultores += "Nao foi cobrado multa\n";
                }

                String parcelas = "===== PARCELAS =====\n";
                parcelas += agr.printContentParcelasString(etapa-1);

                agricultores += parcelas;
            }
        }
        return (rd + info + empresarios + agricultores);
    }

    private String setArquivoPrefeito(Prefeito pre, int etapa) throws IOException {
        double impostos = 0;
        double multas = 0;

        String empresarios = "\n==========\nEMPRESARIOS:";
        for (Empresario emp : this.empresarios) {
            if (emp.getCidade().equals(pre.getCidade())) {
                empresarios += "\n==========\n\n";
                empresarios += "Empresario: " + emp.getNome() + "\n";
                empresarios += "Setor: " + emp.getSetor() + "\n";
                empresarios += "Produtividade: " + emp.getProdutividade() + "\n";
                empresarios += "Imposto: " + emp.getImposto() + "\n";
                empresarios += "Poluicao pessoal: " + emp.getPoluicao() + "\n";

                if(etapa == 2){
                    empresarios += "Multa: ";
                    if(emp.getMulta() > 0) empresarios += "" + emp.getMulta() + "\n";
                    else empresarios += "Nao pagou multa\n";
                }

                impostos += emp.getImposto();
                multas += emp.getMulta();
            }
        }

        String agricultores = "\n==========\nAGRICULTORES:";
        for (Agricultor agr : this.agricultores) {
            if (agr.getCidade().equals(pre.getCidade())) {
                agricultores += "\n==========\n\n";
                agricultores += "Agricultor: " + agr.getNome() + "\n";
                agricultores += "Produtividade: " + agr.getProdutividade() + "\n";
                agricultores += "Imposto: " + agr.getImposto() + "\n";
                agricultores += "Poluicao media pessoal: " + agr.getPoluicao() + "\n";

                if(etapa == 1){
                    agricultores += agr.printContentParcelasString(2);
                }

                else if(etapa == 2){
                    agricultores += "Multa: ";
                    if(agr.getMulta() > 0) agricultores += "" + agr.getMulta() + "\n";
                    else agricultores += "Nao pagou multa\n";
                }

                impostos += agr.getImposto();
                multas += agr.getMulta();
            }
        }
        

        String rd = "========== Rodada: " + this.rodada + "\n";
        // String neg = this.getTransferenciaById(pre.getId());
        String neg = "* Tranferidos:\n";
        if(this.transferenciasSent.get(pre.getId()-1).size() == 0){
            neg += "Nao efetuou nenhuma transferencia\n";
        }
        else{
            for (String sent : this.transferenciasSent.get(pre.getId()-1)) {
                neg += sent + "\n";
            }
        }
        neg += "* Recebidos:\n";
        if(this.transferenciasReceived.get(pre.getId()-1).size() == 0){
            neg += "Nao recebeu nenhuma transferencia\n";
        }
        else{
            for (String received : this.transferenciasReceived.get(pre.getId()-1)) {
                neg += received + "\n";
            }
        }

        String info = "";
        info += "Prefeito de " + pre.getCidade() + "\n";
        info += "Nome: " + pre.getNome() + "\n\n";
        // info += "Caixa anterior: " + this.getSaldoAnteriorById(pre.getId()) + "\n";
        info += "Caixa anterior: " + this.saldosAnteriores.get(pre.getId()-1) + "\n";
        info += "Impostos arrecados no ano: " + impostos + "\n";
        if(etapa == 2) info += "Multas arrecadadas no ano: " + multas + "\n";
        info += "\n";
        info += "Transferencias:\n" + neg;
        // if ("".equals(neg)) {
        //     info += "Nao efetuou/recebeu nenhuma transferencia\n";
        // } else {
        //     info += "" + neg + "\n";
        // }
        info += "\n";

        if(etapa == 2){
            info += "Acoes Ambientais usadas nessa rodada:\n" + pre.getAcoesUsadasString() + "\n";
            info += "Mudancas de taxas nessa rodada:\n" + pre.getTaxasMudadasString() + "\n";
        }

        //pre.finalizarRodada();
        info += "Caixa da prefeitura de " + pre.getCidade() + ": " + pre.getCaixa() + "\n\n";
        info += "Poluicao mundial: " + (this.poluicaoMundo * 100) + "%\n";

        return (rd + info + empresarios + agricultores);
    }

    private String setArquivoVereador(Vereador ver, int etapa) throws IOException {
        String rd = "========== Rodada: " + this.rodada + "\n";
        String info = "";
        info += "Vereador de " + ver.getCidade() + "\n";
        info += "Nome: " + ver.getNome() + "\n\n";
        // info += "Saldo anterior: " + this.getSaldoAnteriorById(ver.getId()) + "\n\n";
        info += "Saldo anterior: " + this.saldosAnteriores.get(ver.getId()-1) + "\n\n";

        // String neg = this.getTransferenciaById(ver.getId());
        String neg = "* Tranferidos:\n";
        if(this.transferenciasSent.get(ver.getId()-1).size() == 0){
            neg += "Nao efetuou nenhuma transferencia\n";
        }
        else{
            for (String sent : this.transferenciasSent.get(ver.getId()-1)) {
                neg += sent + "\n";
            }
        }
        neg += "* Recebidos:\n";
        if(this.transferenciasReceived.get(ver.getId()-1).size() == 0){
            neg += "Nao recebeu nenhuma transferencia\n";
        }
        else{
            for (String received : this.transferenciasReceived.get(ver.getId()-1)) {
                neg += received + "\n";
            }
        }
        // if ("".equals(neg)) {
        //     info += "Nao efetuou/recebeu nenhuma transferencia\n";
        // } else {
        //     info += "" + neg + "\n";
        // }
        info += "Transferencias:\n" + neg;
        info += "\n";

        int cidade = (ver.getCidade().equals("Atlantis")) ? 0 : 1;

        if(etapa == 2){
            info += "Acoes Ambientais usadas pelo prefeito nessa rodada:\n" + this.prefeitos.get(cidade).getAcoesUsadasString() + "\n";
            info += "Mudancas de taxas feitas pelo prefeito nessa rodada:\n" + this.prefeitos.get(cidade).getTaxasMudadasString() + "\n";
        }

        int tipoPessoa = this.getTipoPessoaById(ver.getIdEleito());
        Pessoa pessoa;

        if(tipoPessoa == 1){
            pessoa = getEmpresarioById(ver.getIdEleito());
        }
        else{
            pessoa = getAgricultorById(ver.getIdEleito());
        }
        ver.finalizarRodada(pessoa);

        info += "Saldo atual: " + ver.getSaldo() + "\n\n";
        info += "Poluicao mundial: " + (this.poluicaoMundo * 100) + "%\n";

        return (rd + info);
    }

    public void setArquivos(int etapa) throws IOException {
    	String pageBreak = "\014";
        if (etapa == 1) {
            String fileContent = "";

            for (FiscalAmbiental fis : this.fiscais) {
                fileContent += "=================================================\n";
                fileContent += this.setArquivoFiscal(fis, etapa);
                fileContent += "=================================================\n" + pageBreak;
            }
            for (Prefeito pre : this.prefeitos) {
                fileContent += "=================================================\n";
                fileContent += this.setArquivoPrefeito(pre, etapa);
                fileContent += "=================================================\n" + pageBreak;
            }
            for (Vereador ver : this.vereadores) {
                fileContent += "=================================================\n";
                fileContent += this.setArquivoVereador(ver, etapa);
                fileContent += "=================================================\n" + pageBreak;
            }

            String arq = "arquivosResumo/r" + this.rodada + "e" + etapa + ".txt";
            try (PrintWriter esc = new PrintWriter(arq, "UTF-8")) {
                esc.println(fileContent);
                esc.close();
            }
        } else {
            String fileContent = "";

            for (Empresario emp : this.empresarios) {
                fileContent += "=================================================\n";
                fileContent += this.setArquivoEmp(emp, etapa);
                fileContent += "=================================================\n" + pageBreak;
            }
            for (Agricultor agr : this.agricultores) {
                fileContent += "=================================================\n";
                fileContent += this.setArquivoAgr(agr, etapa);
                fileContent += "=================================================\n" + pageBreak;
            }
            for (FiscalAmbiental fis : this.fiscais) {
                fileContent += "=================================================\n";
                fileContent += this.setArquivoFiscal(fis, etapa);
                fileContent += "=================================================\n" + pageBreak;
            }
            for (Prefeito pre : this.prefeitos) {
                fileContent += "=================================================\n";
                fileContent += this.setArquivoPrefeito(pre, etapa);
                fileContent += "=================================================\n" + pageBreak;
            }
            for (Vereador ver : this.vereadores) {
                fileContent += "=================================================\n";
                fileContent += this.setArquivoVereador(ver, etapa);
                fileContent += "=================================================\n" + pageBreak;
            }

            String arq = "arquivosResumo/r" + this.rodada + "e" + etapa + ".txt";
            try (PrintWriter esc = new PrintWriter(arq, "UTF-8")) {
                esc.println(fileContent);
                esc.close();
            }
        }
    }

    private int askAnswer(Scanner scanner, String opcoes, int opcaoMaxima) {
        System.out.println(opcoes);
        String valueString;
        int valueInt = 0;
        boolean numero = false;
        boolean valido = false;
        while (!numero && !valido) {
            valueString = scanner.next();
            if (Pattern.matches("[0-9]?", valueString) || Pattern.matches("[0-9][0-9]?", valueString)) {
                numero = true;
                valueInt = Integer.parseInt(valueString);
                if (!(valueInt < 1) && !(valueInt > opcaoMaxima)) {
                    valido = true;
                } else {
                    System.out.println("Valor incorreto. Escolha novamente.");
                    numero = false;
                }
            } else {
                System.out.println("Valor incorreto. Escolha novamente.");
            }
        }

        this.saveGame("" + valueInt + "");
        return valueInt;
    }

    private double askDouble(Scanner scanner) {
        double value = (double) 0;
        boolean condicao = false;
        while (!condicao) {

            try {
                value = scanner.nextDouble();
                condicao = true;
            } catch (Exception e) {
                System.out.println("Tente novamente!");
            }

        }

        String valueStr = Double.toString(value);
        if (valueStr.indexOf(".") != -1) {
            valueStr = valueStr.replaceAll("\\.", ",");
        }

        this.saveGame("" + valueStr + "");
        return value;
    }

    public boolean menu(int etapa) throws IOException {
        boolean ficarWhile = true;
        String primeiraPergunta = "Rodada: " + this.rodada + ". Etapa: " + etapa + "\n";
        if (etapa == 1) {
            primeiraPergunta += " (1) Empresario\n (2) Agricultor\n (3) Mundo";
            String[] segundaPergunta = {
                " (1) Imprimir Empresario(s)\n (2) Vender\n (3) Transferir Dinheiros\n (4) Voltar", //Empresario (1)
                " (1) Imprimir Agricultor(es)\n (2) Imprimir detalhes das Parcelas\n (3) Transferir Dinheiros\n (4) Fazer pedido para Fiscal\n (5) Devolver Produto\n (6) Voltar", //Agricultor (2)
                " (1) Terminar Etapa 1\n (2) Eleicao\n (3) Salvar jogo\n (4) Carregar jogo\n (5) Voltar" //Mundo (3)
            };

            final int menuSelection = askAnswer(this.scanner, primeiraPergunta, 3);
            switch (menuSelection) {
                case 1: { // Empresario
                    int respostas = 0;
                    boolean continuar = false;
                    int empOption = 0;

                    while (!continuar) {
                        empOption = askAnswer(this.scanner, segundaPergunta[menuSelection - 1], 4);

                        if (empOption == 1) { // Imprimir Empresario
                            boolean voltaMenu = false;
                            while (!voltaMenu) {
                                respostas = askAnswer(this.scanner, " (1) Somente um\n (2) Todos\n (3) Voltar", 3);
                                if (respostas == 1) { // Somente um
                                    System.out.println("Qual empresario?");
                                    int emp = askAnswer(this.scanner, " (1) Semente\n (2) Fertilizante\n (3) Maquina\n (4) Agrotoxico\n (5) Voltar", 5);
                                    if (emp != 5) {
                                        this.empresarios.get(emp - 1).consultaDados();
                                        continuar = true;
                                        voltaMenu = true;
                                    }
                                } else if (respostas == 2) { // Todos
                                    this.imprimeEmpresarios();
                                    continuar = true;
                                    voltaMenu = true;
                                } else { // Voltar da opcao 1
                                    continuar = false;
                                    voltaMenu = true;
                                }
                            }
                        } else if (empOption == 4) { // Voltar para menu principal da etapa 1
                            System.out.println("Voltando.\n");
                            continuar = true;
                        } else if (empOption == 2) { // Venda
                            this.venda();
                            continuar = true;
                        } else {
                            System.out.println("Qual empresario?");
                            int emp = askAnswer(this.scanner, " (1) Semente\n (2) Fertilizante\n (3) Maquina\n (4) Agrotoxico\n (5) Voltar", 5);

                            if (emp == 5) { // Voltar das opcoes 2 e 3
                                continuar = false;
                            } else if (empOption == 3) { // Transferir Dinheiros
                                this.transferirDinheiros(emp);
                                continuar = true;
                            }

                        }
                    }

                    break;
                }
                case 2: { // Agricultor
                    boolean continuar = false;
                    int agrOption;

                    while (!continuar) {
                        agrOption = askAnswer(this.scanner, segundaPergunta[menuSelection - 1], 6);

                        int respostas = 0;
                        if (agrOption == 1) { // Imprimir Agricultor
                            boolean voltaMenu = false;
                            while (!voltaMenu) {
                                respostas = askAnswer(this.scanner, " (1) Somente um\n (2) Todos\n (3) Voltar", 3);
                                if (respostas == 1) { // Somente um
                                    System.out.println("Qual agricultor?");
                                    int agr = askAnswer(this.scanner, this.criaListaAgricultores(1), (this.quantidadeJogadores - 4));

                                    if (agr != (this.quantidadeJogadores - 4)) {
                                        this.agricultores.get(agr - 1).consultaDados();
                                        continuar = true;
                                        voltaMenu = true;
                                    }
                                } else if (respostas == 2) { // Todos
                                    this.imprimeAgricultores();
                                    continuar = true;
                                    voltaMenu = true;
                                } else {
                                    continuar = false; // Voltar da opcao 1
                                    voltaMenu = true;
                                }
                            }
                        } else if (agrOption == 5) {
                            this.devolver();
                            continuar = true;
                        } else if (agrOption == 6) { // Voltar para o menu principal
                            System.out.println("Voltando.\n");
                            continuar = true;
                        } else if (agrOption == 4) { // Fazer pedido para Fiscal
                            boolean voltaMenu = false;
                            while (!voltaMenu) {
                                String pedidos = " (1) Pedir Selo Verde\n (2) Reportar outro agricultor\n (3) Voltar";
                                int resposta = askAnswer(this.scanner, pedidos, 3);

                                if (resposta == 3) { // Voltar da opcao 4
                                    voltaMenu = true;
                                } else {
                                    boolean trocaPedido = false;
                                    while (!trocaPedido) {
                                        System.out.println("Qual agricultor?");
                                        String lista = this.criaListaAgricultores(1);
                                        int quantidade = this.quantidadeJogadores - 3;
                                        lista += "(" + quantidade + ") Fazer outro tipo de Pedido";
                                        int agr = askAnswer(this.scanner, lista, quantidade);

                                        boolean trocaAgr = false;

                                        if (agr == quantidade) {
                                            trocaPedido = true;
                                            trocaAgr = true;
                                        }

                                        while (!trocaAgr) {
                                            String cidade = this.agricultores.get(agr - 1).getCidade();
                                            int idAgr = this.agricultores.get(agr - 1).getId();
                                            String nomeAgr = this.agricultores.get(agr - 1).getNome();
                                            FiscalAmbiental fis = null;

                                            String pedido = "";

                                            if (cidade.equals("Atlantis")) {
                                                fis = this.fiscais.get(0);
                                            } else {
                                                fis = this.fiscais.get(1);
                                            }

                                            boolean fazerPedido = false;

                                            if (resposta == 1) {
                                                pedido += "pedir Selo Verde.";
                                                trocaAgr = true;
                                                trocaPedido = true;
                                                fazerPedido = true;
                                            } else if (resposta == 2) {
                                                System.out.println("Reportar qual agricultor?");
                                                String listaAgrs = this.criaListaAgricultores(1, idAgr);
                                                int quantidadeAlternativas = this.quantidadeJogadores - 4;
                                                listaAgrs += "(" + quantidadeAlternativas + ") Trocar Agricultor";

                                                int agrRep = askAnswer(this.scanner, listaAgrs, quantidadeAlternativas);

                                                if (agrRep == quantidadeAlternativas) {
                                                    trocaAgr = true;
                                                } else {
                                                    if (agrRep >= agr) {
                                                        agrRep++;
                                                    }
                                                    agrRep--;

                                                    //int idReportado = this.agricultores.get(agrRep).getId();
                                                    String nomeReportado = this.agricultores.get(agrRep).getNome();

                                                    pedido += "reportar o agricultor de nome " + nomeReportado + ".";
                                                    trocaAgr = true;
                                                    trocaPedido = true;
                                                    fazerPedido = true;
                                                }

                                            }
                                            if (fazerPedido) {
                                                fis.adicionaPedido(nomeAgr, pedido);
                                                this.colocaArquivoLog("Pedido \"" + pedido + "\" do " + nomeAgr + " feito para o Fiscal " + fis.getNome() + "");
                                                this.colocaLogCSV("pedido para fiscal" + this.separadorCSV + nomeAgr + this.separadorCSV + "fiscal " + fis.getNome() + this.separadorCSV + pedido);
                                                System.out.println("Pedido \"" + pedido + "\" do " + nomeAgr + " feito para o Fiscal " + fis.getNome() + "");
                                                continuar = true;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            System.out.println("Qual agricultor?");
                            String lista = this.criaListaAgricultores(1);
                            int quantidade = this.quantidadeJogadores - 3;
                            String pergunta = lista + "(" + quantidade + ") Voltar";
                            int agr = askAnswer(this.scanner, pergunta, quantidade);

                            if (agr == this.quantidadeJogadores - 3) { // Voltar das opcoes 2-4
                                continuar = false;
                            } else if (agrOption == 2) { // Imprimir detalhes parcelas
                                boolean voltaMenu = false;
                                while (!voltaMenu) {
                                    respostas = askAnswer(this.scanner, " (1) Somente um\n (2) Todos\n (3) Voltar", 3);
                                    if (respostas == 1) { // Somente um
                                        System.out.println("Qual parcela?");
                                        String opcaoParcelas = "(1) P1\n(2) P2\n(3) P3\n(4) P4\n(5) P5\n(6) P6\n(7) Comprar para outro Agricultor";
                                        int parcela = askAnswer(this.scanner, opcaoParcelas, 7);

                                        if (parcela != 7) {
                                            parcela--;
                                            this.agricultores.get(agr - 1).printContentParcela(parcela);
                                            continuar = true;
                                            voltaMenu = true;
                                        }
                                    } else if (respostas == 2) { // Todos
                                        this.agricultores.get(agr - 1).printContentParcelas();
                                        continuar = true;
                                        voltaMenu = true;
                                    } else {
                                        continuar = false; // Voltar da opcao 1
                                        voltaMenu = true;
                                    }
                                }
                            } else if (agrOption == 3) { // Transferir Dinheiros
                                agr += 4;
                                this.transferirDinheiros(agr);
                                continuar = true;
                            }

                        }
                    }

                    break;
                }
                case 3: { // Mundo
                    int mundoOption = askAnswer(this.scanner, segundaPergunta[menuSelection - 1], 5);
                    if (mundoOption == 1) { // Terminar Etapa
                        if (this.prefeitos.get(0).getNome() != "") {
                            System.out.println("Tem certeza?");
                            int sair = askAnswer(this.scanner, " (1) Sim\n (2) Nao", 2);
                            if (sair == 1) {
                                System.out.println("\nTerminando.\n");
                                ficarWhile = false;
                                double poluicaoCausada = 0;
                                for (Agricultor agr : this.agricultores) {
                                    agr.plantar(this.poluicaoMundo);
                                    poluicaoCausada += agr.calculaPoluicao();
                                }
                                for (Empresario emp : this.empresarios) {
                                    poluicaoCausada += emp.calculaPoluicao();
                                }
                                this.cobrarImpostos();
                                System.out.println("Impostos cobrados.");
                                for (Prefeito pref : this.prefeitos) {
                                    pref.receberContribuicoes();
                                    System.out.println("Contribuicoes recebidas pelo Prefeito de " + pref.getCidade() + ".");
                                }
                                this.poluicaoMundo += poluicaoCausada;
                                System.out.println("Poluicao causada no mundo de " + (poluicaoCausada * 100) + "%.");
                                System.out.println("Poluicao mundial: " + (this.poluicaoMundo * 100) + "%.");
                                this.setArquivos(etapa);

                            } else { // Voltar para o menu principal
                                System.out.println("Cancelado.");
                            }
                        } else { // Volta para menu principal por nao ter prefeito e vereador setado ainda
                            System.out.println("\nNao foram escolhidos Prefeitos e Vereadores ainda. Por favor, escolha-os para poder passar para a segunda etapa.\n");
                        }

                    } else if (mundoOption == 2) { // Eleicao
                        this.eleicao();
                    } else if (mundoOption == 5) { // Voltar
                        System.out.println("Voltando.\n");
                    } else { // 2-3
                        System.out.println("Em breve.");
                    }

                    break;
                }
            }
        } else {
            primeiraPergunta += " (1) Fiscal\n (2) Prefeito\n (3) Vereador\n (4) Mundo";
            String[] segundaPergunta = {
                " (1) Imprimir Fiscais\n (2) Multar\n (3) Transferir Dinheiros\n (4) Selo Verde\n (5) Voltar", //Fiscal (1)
                " (1) Imprimir Prefeitos\n (2) Usar acoes ambientais\n (3) Mudar Taxa Imposto\n (4) Transferir Dinheiros\n (5) Voltar", //Prefeito (2)
                " (1) Imprimir Vereadores\n (2) Transferir Dinheiros\n (3) Voltar", //Vereador (3)
                " (1) Terminar Rodada\n (2) Salvar jogo\n (3) Carregar jogo\n (4) Voltar" //Mundo (4)
            };

            final int menuSelection = askAnswer(this.scanner, primeiraPergunta, 4);

            switch (menuSelection) {
                case 1: { // Fiscal
                    boolean continuar = false;
                    while (!continuar) {
                        int fisOption = askAnswer(this.scanner, segundaPergunta[menuSelection - 1], 5);
                        String listaFis = criaListaFiscais(1) + "(3) Voltar";

                        if (fisOption == 1) { // Imprimir Fiscais
                            int respostas = askAnswer(this.scanner, " (1) Somente um\n (2) Todos\n (3) Voltar", 3);
                            if (respostas == 1) { // Somente um
                                System.out.println("Qual fiscal?");
                                int fis = askAnswer(this.scanner, listaFis, 2);

                                if (fis == 3) { // voltar opcao 1
                                    continuar = false;
                                } else {
                                    this.fiscais.get(fis - 1).consultaDados();
                                    continuar = true;
                                }
                            } else if (respostas == 2) { // Todos
                                this.imprimeFiscais();
                                continuar = true;
                            } else {
                                continuar = false; // Voltar da opcao 1
                            }
                        } else if (fisOption == 5) { // Voltar para menu inicial
                            System.out.println("Voltando.\n");
                            continuar = true;
                        } else {
                            boolean voltaMenu = false;

                            while (!voltaMenu) {
                                System.out.println("Qual fiscal?");
                                int fis = askAnswer(this.scanner, listaFis, 3);

                                if (fis == 3) {
                                    voltaMenu = true;
                                } else {
                                    int idFis = this.fiscais.get(fis - 1).getId();

                                    if (fisOption == 2) { // Multar
                                        String cidadeFis = this.fiscais.get(fis - 1).getCidade();

                                        boolean trocaFiscal = false;
                                        while (!trocaFiscal) {
                                            System.out.println("Multar quem da cidade de " + cidadeFis + "?");
                                            String[] lista = criaListaEmpAgr(1, cidadeFis, 0);
                                            int quantidade = Integer.parseInt(lista[1]) + 1;
                                            lista[0] += "(" + quantidade + ") Voltar";
                                            int idMultado = askAnswer(this.scanner, lista[0], quantidade);

                                            boolean trocaAgr = false;
                                            if (idMultado == quantidade) {
                                                trocaFiscal = true;
                                                trocaAgr = true;
                                            }

                                            while (!trocaAgr) {
                                                if (cidadeFis.equals("Atlantis")) {
                                                    idMultado = idMultado * 2 - 1;
                                                } else {
                                                    idMultado = idMultado * 2;
                                                }

                                                System.out.println("Que tipo de multa?");
                                                int tipoMulta = askAnswer(this.scanner, "(1) Multa leve\n(2) Multa media\n(3) Multa alta\n(4) Trocar o Agricultor", 4);

                                                if (tipoMulta == 4) {
                                                    trocaAgr = true;
                                                } else {

                                                    this.multar(idFis, idMultado, tipoMulta);
                                                    continuar = true;
                                                    trocaAgr = true;
                                                }

                                            }

                                        }

                                    } else if (fisOption == 3) {// Transferir Dinheiros
                                        this.transferirDinheiros(idFis);
                                        continuar = true;
                                    } else if (fisOption == 4) { // Selo Verde
                                        boolean trocaFiscal = false;

                                        while (!trocaFiscal) {
                                            int darSelo = askAnswer(this.scanner, " (1) Dar Selo Verde\n (2) Tirar Selo Verde\n (3) Trocar Fiscal", 3);

                                            boolean trocaTiraDarSelo = false;

                                            if (darSelo == 3) {
                                                trocaFiscal = true;
                                                trocaTiraDarSelo = true;
                                            }

                                            while (!trocaTiraDarSelo) {
                                                boolean selo = (darSelo == 1) ? true : false;

                                                String cidadeFis = this.fiscais.get(fis - 1).getCidade();
                                                System.out.println("Para qual agricultor da cidade de " + cidadeFis + "?");
                                                String lista = criaListaAgricultores(1, cidadeFis, 0);
                                                lista += "(" + (((this.quantidadeJogadores - 4) / 2) + 1) + ") Voltar";
                                                int agr = askAnswer(this.scanner, lista, (((this.quantidadeJogadores - 4) / 2) + 1));

                                                boolean trocaAgr = false;

                                                if (agr == (((this.quantidadeJogadores - 4) / 2) + 1)) {
                                                    trocaTiraDarSelo = true;
                                                    trocaAgr = true;
                                                }
                                                else{
                                                    if(cidadeFis.equals("Atlantis")) agr = (agr*2) - 1;
                                                    else agr = agr*2;
                                                }                                                

                                                while (!trocaAgr) {
                                                    System.out.println("Em qual Parcela?");
                                                    String opcaoParcelas = " (1) P1\n (2) P2\n (3) P3\n (4) P4\n (5) P5\n (6) P6\n (7) Voltar";
                                                    int parcela = askAnswer(this.scanner, opcaoParcelas, this.qntdParcelasPorAgricultor + 1);

                                                    if (parcela == 7) {
                                                        trocaAgr = true;
                                                    }
                                                    else {
                                                        parcela--;

                                                        boolean tentativa = this.fiscais.get(fis - 1).setSeloVerde(this.agricultores.get(agr - 1), parcela, selo);

                                                        if (selo && tentativa) {
                                                            System.out.println("" + this.fiscais.get(fis - 1).getNome() + " deu Selo Verde para o Agricultor " + this.agricultores.get(agr - 1).getNome() + " na Parcela " + (parcela + 1) + ".");

                                                            this.colocaArquivoLog("" + this.fiscais.get(fis - 1).getNome() + " deu Selo Verde para o Agricultor " + this.agricultores.get(agr - 1).getNome() + " na Parcela " + (parcela + 1) + "");
                                                            this.colocaLogCSV("deu selo" + this.separadorCSV + this.fiscais.get(fis - 1).getNome() + this.separadorCSV + this.agricultores.get(agr - 1).getNome() + this.separadorCSV + (parcela + 1));
                                                        } else if (selo && !tentativa) {
                                                            System.out.println("" + this.fiscais.get(fis - 1).getNome() + " tentou dar Selo Verde para o Agricultor " + this.agricultores.get(agr - 1).getNome() + " na Parcela " + (parcela + 1) + ", mas essa parcela tinha agrotoxico.");
                                                            System.out.println("Tente outra parcela.");
                                                        } else {
                                                            System.out.println("" + this.fiscais.get(fis - 1).getNome() + " tirou o Selo Verde do Agricultor " + this.agricultores.get(agr - 1).getNome() + " na Parcela " + (parcela + 1) + ".");

                                                            this.colocaArquivoLog("" + this.fiscais.get(fis - 1).getNome() + " tirou o Selo Verde do Agricultor " + this.agricultores.get(agr - 1).getNome() + " na Parcela " + (parcela + 1) + "");
                                                            this.colocaLogCSV("tirou selo" + this.separadorCSV + this.fiscais.get(fis - 1).getNome() + this.separadorCSV + this.agricultores.get(agr - 1).getNome() + this.separadorCSV + (parcela + 1));
                                                        }

                                                        continuar = true;
                                                    }

                                                }
                                            }
                                        }

                                    }
                                }

                            }

                        }

                    }

                    break;
                }
                case 2: { // Prefeito
                    boolean continuar = false;

                    while (!continuar) {
                        int prefOption = askAnswer(this.scanner, segundaPergunta[menuSelection - 1], 5);
                        String listaPref = criaListaPrefeitos(1);
                        listaPref += "(3) Voltar para o Menu Principal";
                        if (prefOption == 1) { // Imprimir Prefeitos
                            boolean voltaMenuPrefeito = false;

                            while (!voltaMenuPrefeito) {
                                int respostas = askAnswer(this.scanner, " (1) Somente um\n (2) Todos\n (3) Voltar", 3);
                                if (respostas == 1) { // Somente um
                                    boolean trocaPref = false;
                                    while (!trocaPref) {
                                        System.out.println("Qual prefeito?");
                                        int pref = askAnswer(this.scanner, listaPref, 3);

                                        if (pref == 3) {
                                            trocaPref = true;
                                        } else {
                                            this.prefeitos.get(pref - 1).consultaDados();
                                            continuar = true;
                                        }
                                    }
                                } else if (respostas == 2) { // Todos
                                    this.imprimePrefeitos();
                                    continuar = true;
                                } else {
                                    voltaMenuPrefeito = true; // Voltar da opcao 1
                                }
                            }

                        } else if (prefOption == 5) { // Voltar menu principal
                            System.out.println("Voltando..");
                            continuar = true;
                        } else {
                            boolean voltaMenu = false;
                            while (!voltaMenu) {
                                System.out.println("Qual prefeito?");
                                int pref = askAnswer(this.scanner, listaPref, 3);
                                if (pref == 3) {
                                    voltaMenu = true;
                                } else {
                                    if (prefOption == 2) { // Usar acoes ambientais
                                        boolean trocaPref = false;
                                        while (!trocaPref) {
                                            System.out.println("Qual acao ambiental usar nessa rodada?");
                                            String acoes = criaListaAcoesPrefeito(1);
                                            acoes += "(4) Escolher outro Prefeito";
                                            int acao = askAnswer(this.scanner, acoes, 4);

                                            if (acao == 4) {
                                                trocaPref = true;
                                            } else {
                                                this.prefeitos.get(pref - 1).setUsarAcao(acao, this.poluicaoMundo);
                                                this.colocaArquivoLog("Prefeito " + this.prefeitos.get(pref - 1).getNome() + " investiu na Acao Ambiental " + this.prefeitos.get(pref - 1).getTipoAcao(acao) + "");
                                                this.colocaLogCSV("usa acao" + this.separadorCSV + "prefeito " + this.prefeitos.get(pref - 1).getNome() + this.separadorCSV + this.prefeitos.get(pref - 1).getTipoAcao(acao));
                                                System.out.println("Prefeito " + this.prefeitos.get(pref - 1).getNome() + " investiu na Acao Ambiental " + this.prefeitos.get(pref - 1).getTipoAcao(acao) + ".");
                                                continuar = true;
                                            }
                                        }
                                    } else if (prefOption == 3) { // Mudar Taxa
                                        boolean voltaMenuPrefeito = false;

                                        while (!voltaMenuPrefeito) {
                                            System.out.println("Qual Taxa?");
                                            int tipoTaxa = askAnswer(this.scanner, " (1) Taxa 1 (Prod = 0)\n (2) Taxa 2 (Prod <= 120)\n (3) Taxa 3 (Prod > 120)\n (4) Voltar Menu do Prefeito", 4);

                                            boolean trocaTipoTaxa = false;

                                            if (tipoTaxa == 4) {
                                                trocaTipoTaxa = true;
                                                voltaMenuPrefeito = true;
                                            }

                                            while (!trocaTipoTaxa) {
                                                System.out.println("Para quanto?");
                                                String valorTaxa = "";
                                                if (tipoTaxa == 1) {
                                                    valorTaxa = " (1) D$ 5\n (2) D$ 10\n (3) D$ 15\n (4) Troca Tipo da Taxa";
                                                } else if (tipoTaxa == 2) {
                                                    valorTaxa = " (1) 5%\n (2) 10%\n (3) 15%\n (4) Troca Tipo da Taxa";
                                                } else if (tipoTaxa == 3) {
                                                    valorTaxa = " (1) 25%\n (2) 30%\n (3) 35%\n (4) Troca Tipo da Taxa";
                                                }
                                                int novaTaxa = askAnswer(this.scanner, valorTaxa, 4);

                                                if (novaTaxa == 4) {
                                                    trocaTipoTaxa = true;
                                                } else {
                                                    double taxa = 0;
                                                    if (tipoTaxa == 1) {
                                                        if (novaTaxa == 1) {
                                                            taxa = (double) 5;
                                                        } else if (novaTaxa == 2) {
                                                            taxa = (double) 10;
                                                        } else if (novaTaxa == 3) {
                                                            taxa = (double) 15;
                                                        }
                                                    } else if (tipoTaxa == 2) {
                                                        if (novaTaxa == 1) {
                                                            taxa = (double) 0.05;
                                                        } else if (novaTaxa == 2) {
                                                            taxa = (double) 0.1;
                                                        } else if (novaTaxa == 3) {
                                                            taxa = (double) 0.15;
                                                        }
                                                    } else if (tipoTaxa == 3) {
                                                        if (novaTaxa == 1) {
                                                            taxa = (double) 0.25;
                                                        } else if (novaTaxa == 2) {
                                                            taxa = (double) 0.30;
                                                        } else if (novaTaxa == 3) {
                                                            taxa = (double) 0.35;
                                                        }
                                                    }

                                                    this.prefeitos.get(pref - 1).mudarTaxa(tipoTaxa, taxa);

                                                    String taxaString = (tipoTaxa == 1) ? "" + taxa + "" : "" + (taxa * 100) + "%";

                                                    this.colocaArquivoLog("Prefeito " + this.prefeitos.get(pref - 1).getNome() + " trocou a taxa do tipo " + tipoTaxa + " para " + taxaString + "");

                                                    this.colocaLogCSV("troca taxa" + this.separadorCSV + this.prefeitos.get(pref - 1).getNome() + this.separadorCSV + tipoTaxa + this.separadorCSV + taxaString);

                                                    System.out.println("Prefeito " + this.prefeitos.get(pref - 1).getNome() + " trocou a taxa do tipo " + tipoTaxa + " para " + taxaString + ".");

                                                    trocaTipoTaxa = true;
                                                }
                                            }
                                        }
                                    } else if (prefOption == 4) { // Transferir dinheiros
                                        int idPref = this.prefeitos.get(pref - 1).getId();
                                        this.transferirDinheiros(idPref);
                                        continuar = true;
                                    }
                                }

                            }

                        }
                    }
                    break;
                }
                case 3: { // Vereador
                    boolean continuar = false;

                    while (!continuar) {
                        int verOption = askAnswer(this.scanner, segundaPergunta[menuSelection - 1], 3);
                        String listaVereadores = criaListaVereadores(1);
                        if (verOption == 1) { // Imprimir Vereadores
                            int respostas = askAnswer(this.scanner, " (1) Somente um\n (2) Todos\n (3) Voltar", 3);
                            if (respostas == 1) { // Somente um
                                System.out.println("Qual verador?");
                                int ver = askAnswer(this.scanner, listaVereadores, 2);

                                this.vereadores.get(ver - 1).consultaDados();
                                continuar = true;
                            } else if (respostas == 2) { // Todos
                                this.imprimeVereadores();
                                continuar = true;
                            } else {
                                continuar = false; // Voltar da opcao 1
                            }
                        } else if (verOption == 2) {// Transferir Dinheiros
                            System.out.println("Qual vereador?");
                            int ver = askAnswer(this.scanner, listaVereadores, 2);
                            int idVer = this.vereadores.get(ver - 1).getId();
                            this.transferirDinheiros(idVer);
                            continuar = true;
                        } else if (verOption == 3) { // Voltar para menu principal
                            System.out.println("Voltando");
                            continuar = true;
                        }
                    }

                    break;
                }
                case 4: { // Mundo
                    int mundoOption = askAnswer(this.scanner, segundaPergunta[menuSelection - 1], 4);
                    if (mundoOption == 1) { // Terminar Rodada
                        System.out.println("Tem certeza?");
                        int sair = askAnswer(this.scanner, " (1) Sim\n (2) Nao", 2);
                        if (sair == 1) {
                            System.out.println("\nTerminando.\n");
                            ficarWhile = false;
                            double poluicaoReduzida = 0;
                            for (Prefeito pref : this.prefeitos) {
                                poluicaoReduzida += pref.usarAcoes();
                                pref.receberContribuicoes();
                            }
                            this.poluicaoMundo *= (1 - poluicaoReduzida);
                            System.out.println("Poluicao mundial reduzida em " + (poluicaoReduzida * 100) + "%.\nPoluicao Mundial: " + (this.poluicaoMundo * 100) + "%.");

                            this.setArquivos(etapa);

                            this.fechaRodadaLog();

                            this.rodada++;
                            // this.limpaArquivoTRANSFERENCIA();
                            this.limpaHistoricoTransferencias();
                            // this.atualizaArquivoSALDO();
                            this.criaHistoricoSaldos();
                            for (Empresario emp : this.empresarios) {
                                emp.iniciaRodada();
                            }
                            for (Agricultor agr : this.agricultores) {
                                agr.iniciaRodada();
                            }
                            for(Prefeito pref : this.prefeitos){
                                pref.iniciaRodada();
                                if( (this.rodada+1)%2 == 0 ) pref.setNome("");
                            }
                        } else { // Voltar para o menu principal
                            System.out.println("Cancelado.");
                        }

                    } else if (mundoOption == 4) { // Voltar
                        System.out.println("Voltando.\n");
                    } else { // 2-3
                        System.out.println("Em breve.");
                    }

                    break;
                }
            }
        }
        return ficarWhile;
    }

    public static void main(String[] args) throws IOException {
        Mundo mundo = new Mundo();

        mundo.comecarJogo();

        do {

            boolean continuar = true;
            if (mundo.rodada != 1) {
                mundo.colocaArquivoLog("\n===============================================\nRodada:" + mundo.rodada);
                mundo.colocaLogCSV("rodada " + mundo.rodada);
            }
            while (continuar) {
                continuar = mundo.menu(1);
            }

            continuar = true;
            while (continuar) {
                continuar = mundo.menu(2);
            }
            mundo.colocaArquivoLog("===============================================");

        } while (mundo.getPoluicaoMundo() < 100);

        System.out.println("\n\nJogo terminado.");

    }

}
