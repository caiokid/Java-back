package com.barbearia.util;

/**
 * Helper para montar a URL pública completa de uma imagem a partir do
 * nome do arquivo guardado no banco.
 */
public class ImagemUrlHelper {

    private ImagemUrlHelper() {} // classe utilitária — não instanciar

    /**
     * Monta a URL de exibição da imagem.
     *
     *  - nomeImagem null            → retorna null (funcionário sem foto)
     *  - já começa com "http"       → retorna como está (URL externa já pronta)
     *  - caso contrário             → baseUrl + "/images/" + nomeImagem
     *
     * Ex: buildImageUrl("http://localhost:8080", "joao.jpg")
     *     → "http://localhost:8080/images/joao.jpg"
     */
    public static String buildImageUrl(String baseUrl, String nomeImagem) {
        if (nomeImagem == null || nomeImagem.isBlank()) {
            return null;
        }
        if (nomeImagem.startsWith("http")) {
            return nomeImagem;
        }
        return baseUrl + "/images/" + nomeImagem;
    }
}
