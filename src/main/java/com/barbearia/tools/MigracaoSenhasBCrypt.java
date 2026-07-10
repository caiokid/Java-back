package com.barbearia.tools;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * SCRIPT TEMPORÁRIO — migra senhas em texto puro para hash BCrypt.
 *
 * ⚠️ APAGUE esta classe depois de rodar a migração com sucesso.
 *
 * Segurança:
 *  - DRY-RUN por padrão: apenas CONTA e LOGA, sem nenhum UPDATE no banco.
 *  - Só executa os UPDATEs se a propriedade "migracao.senhas.aplicar=true".
 *  - NUNCA re-hashea uma senha que já é BCrypt ($2a$/$2b$/$2y$) → idempotente.
 *
 * Como usar:
 *  1. Rode normalmente (mvn spring-boot:run)         → DRY-RUN, mostra a contagem.
 *  2. Confira os números nos logs.
 *  3. Rode com a flag para aplicar de fato:
 *       mvn spring-boot:run -Dspring-boot.run.arguments=--migracao.senhas.aplicar=true
 *     (ou adicione migracao.senhas.aplicar=true no application.properties)
 *  4. Rode o DRY-RUN de novo: deve mostrar 0 a converter (idempotência).
 *  5. APAGUE esta classe.
 */
@Component
public class MigracaoSenhasBCrypt implements CommandLineRunner {

    private final JdbcTemplate jdbc;
    private final boolean aplicar;

    public MigracaoSenhasBCrypt(JdbcTemplate jdbc,
                                @Value("${migracao.senhas.aplicar:false}") boolean aplicar) {
        this.jdbc = jdbc;
        this.aplicar = aplicar;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=========================================================");
        System.out.println(" MIGRAÇÃO DE SENHAS → BCrypt   (modo: "
                + (aplicar ? "APLICAR UPDATES" : "DRY-RUN / somente contagem") + ")");
        System.out.println("=========================================================");

        // (tabela, coluna PK, coluna senha)
        migrarTabela("clientes", "id_cliente", "senha");
        migrarTabela("funcionarios", "id_funcionario", "senha");

        System.out.println("=========================================================");
        if (!aplicar) {
            System.out.println(" DRY-RUN concluído. Nenhuma senha foi alterada.");
            System.out.println(" Para aplicar de verdade, rode com: --migracao.senhas.aplicar=true");
        } else {
            System.out.println(" Migração APLICADA. Lembre-se de APAGAR a classe MigracaoSenhasBCrypt.");
        }
        System.out.println("=========================================================\n");
    }

    /** Processa uma tabela: conta texto puro x já hasheada e, se aplicar=true, converte. */
    private void migrarTabela(String tabela, String colId, String colSenha) {
        String sql = "SELECT " + colId + " AS id, " + colSenha + " AS senha FROM " + tabela;
        List<Map<String, Object>> linhas = jdbc.queryForList(sql);

        int jaHasheadas = 0;   // pulados (já BCrypt)
        int textoPuro = 0;     // candidatos à conversão
        int convertidos = 0;   // efetivamente atualizados
        int nulasVazias = 0;   // senha nula/vazia → ignorada

        for (Map<String, Object> linha : linhas) {
            Object id = linha.get("id");
            Object senhaObj = linha.get("senha");
            String senha = senhaObj == null ? null : senhaObj.toString();

            if (senha == null || senha.isBlank()) {
                nulasVazias++;
                continue;
            }
            if (ehBCrypt(senha)) {
                jaHasheadas++; // JÁ está correta → PULA (não re-hashea)
                continue;
            }

            // Aqui é texto puro → precisa converter
            textoPuro++;
            if (aplicar) {
                String hash = BCrypt.hashpw(senha, BCrypt.gensalt());
                jdbc.update("UPDATE " + tabela + " SET " + colSenha + " = ? WHERE " + colId + " = ?",
                        hash, id);
                convertidos++;
            }
        }

        System.out.println("\nTabela: " + tabela);
        System.out.println("  Total de registros .......... " + linhas.size());
        System.out.println("  Já em BCrypt (pulados) ...... " + jaHasheadas);
        System.out.println("  Senha nula/vazia (ignorada) . " + nulasVazias);
        if (aplicar) {
            System.out.println("  Texto puro CONVERTIDOS ...... " + convertidos);
        } else {
            System.out.println("  Texto puro A CONVERTER ...... " + textoPuro + "  (seriam atualizados)");
        }
    }

    /** True se a senha já está no formato de hash BCrypt. */
    private boolean ehBCrypt(String senha) {
        return senha.startsWith("$2a$") || senha.startsWith("$2b$") || senha.startsWith("$2y$");
    }
}
