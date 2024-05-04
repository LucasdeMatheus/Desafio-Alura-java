package br.com.alura.TabelaFip.principal;

import br.com.alura.TabelaFip.model.Dados;
import br.com.alura.TabelaFip.model.Modelo;
import br.com.alura.TabelaFip.model.Veiculo;
import br.com.alura.TabelaFip.service.ConsumoApi;
import br.com.alura.TabelaFip.service.ConverteDados;

import java.util.Comparator;
import java.util.Scanner;

public class Principal {
    private final Scanner sc = new Scanner(System.in);
    private final ConverteDados converter = new ConverteDados();

    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";
    private ConsumoApi consumoApi = new ConsumoApi();

    public void exibeMenu() {
        String tipoVeiculo = "";
        System.out.println("""
                ------------------- Bem vindo a minha aplicação de Veiculos! -------------------
                                
                Para começar, escolha qual tipo de veículo deseja pesquisar:
                1- motos
                2- carros
                """);
        String opcao = "";
        boolean continuar = true;
        while(continuar) {
            opcao = sc.nextLine();
            switch (opcao) {
                case "1":
                    System.out.println("\nótimo, você escolheu motos!");
                    tipoVeiculo = "motos";
                    continuar = false;
                    break;
                case "2":
                    System.out.println("\nótimo, você escolheu carros!");
                    tipoVeiculo = "carros";
                    continuar = false;
                    break;
                default:
                    System.out.println("Escolha um numero valido!");
            }
        }
        String json = consumoApi.obterDados(ENDERECO + tipoVeiculo + "/marcas");
        var marcas = converter.obterLista(json, Dados.class);
        marcas.stream()
                        .sorted(Comparator.comparing(Dados::codigo))
                                .forEach(System.out::println);


        System.out.println("""
                
                Acima está todos as marcas existentes de %s!
                Agora digite o NOME da marca que deseja consultar:
                
                """.formatted(tipoVeiculo));
        String codigoMarca = "";
        String nomeMarca = "";

        while (true) {
            nomeMarca = sc.nextLine();

            int startIndex = json.indexOf("\"nome\":\"" + nomeMarca + "\"");
            if (startIndex != -1) {
                startIndex = json.lastIndexOf("\"codigo\":\"", startIndex);
                int endIndex = json.indexOf("\"", startIndex + 10);
                codigoMarca = json.substring(startIndex + 10, endIndex);
                System.out.println("O código da marca " + nomeMarca + " é: " + codigoMarca);
                break;
            } else {
                System.out.println("Marca não encontrada.");
            }
        }
        json = consumoApi.obterDados(ENDERECO + tipoVeiculo + "/marcas/" + codigoMarca + "/modelos");
        var modelo = converter.obterDados(json, Modelo.class);
        System.out.println(json);
        modelo.modelos().stream()
                        .forEach(System.out::println);
        System.out.println(json);
        // https://parallelum.com.br/fipe/api/v1/motos/marcas/242/modelos

        System.out.println("""
                
                Acima está todos os modelos existentes de %s!
                Agora, da mesma forma, escolha um modelo pelo seu codigo.
                
                """.formatted(nomeMarca));
        String codigoModelo = sc.nextLine();


        json = consumoApi.obterDados(ENDERECO + tipoVeiculo + "/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos");
        System.out.println(json);
        var anos = converter.obterLista(json, Dados.class);
        anos.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);
        // https://parallelum.com.br/fipe/api/v1/carros/marcas/59/modelos/5940/anos
        // /api/v1/carros/marcas/59/modelos/5940/ano

        System.out.println("""
                
                Acima está todos os anos existentes!
                Agora, escolha um ano para ver sua descrição.
                
                """);
        String ano = sc.nextLine();
        json = consumoApi.obterDados(ENDERECO + tipoVeiculo + "/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos/" + ano);
        Veiculo veiculo = converter.obterDados(json, Veiculo.class);
        System.out.println(veiculo);
    }
}
