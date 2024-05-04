package br.com.alura.TabelaFip.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Veiculo(
        @JsonAlias("Valor")String valor,
        @JsonAlias("marca")String marca,
        @JsonAlias("Modelo")String modelo,
        @JsonAlias("AnoModelo")Integer anoModelo,
        @JsonAlias("Combustivel")String combustivel
) {
}
