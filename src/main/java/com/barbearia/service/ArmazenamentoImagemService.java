package com.barbearia.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Responsável pelo ARMAZENAMENTO FÍSICO das imagens na pasta "images/".
 * Guarda só o arquivo no disco e devolve o NOME gerado (que o banco vai persistir).
 *
 * Equivalente ao Multer do Node: gera um nome único e salva na pasta.
 */
@Service
public class ArmazenamentoImagemService {

    // Pasta na raiz do projeto (mesma usada pelo StaticResourceConfig)
    private static final Path PASTA_IMAGENS = Paths.get("images");

    /**
     * Valida que é uma imagem, gera um nome único e grava o arquivo em images/.
     * @return o nome do arquivo salvo (para guardar no banco).
     */
    public String salvar(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Nenhum arquivo enviado");
        }

        // Aceita apenas imagens (content-type começando com "image/")
        String contentType = arquivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("O arquivo enviado não é uma imagem válida");
        }

        // Nome único: timestamp + nome original "limpo" (igual ao Date.now() + nome do Multer)
        String original = arquivo.getOriginalFilename();
        if (original == null || original.isBlank()) {
            original = "imagem";
        }
        original = original.replaceAll("[^a-zA-Z0-9._-]", "_"); // remove caracteres problemáticos
        String nomeFinal = System.currentTimeMillis() + "-" + original;

        try {
            // Garante que a pasta existe
            Files.createDirectories(PASTA_IMAGENS);
            // Grava os bytes do upload no disco
            Path destino = PASTA_IMAGENS.resolve(nomeFinal);
            Files.write(destino, arquivo.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar a imagem: " + e.getMessage(), e);
        }

        return nomeFinal;
    }

    /** True se existe um arquivo com esse nome na pasta images/ (ignora null/branco). */
    public boolean existe(String nomeArquivo) {
        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            return false;
        }
        return Files.isRegularFile(PASTA_IMAGENS.resolve(nomeArquivo));
    }
}
