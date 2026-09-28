// Falha de comunicação com a API (fora do ar, tempo esgotado ou resposta inesperada)
package com.learntech.dashboards.web.api;

public class ApiIndisponivelException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // guarda a causa original para o log
    public ApiIndisponivelException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
// fim de ApiIndisponivelException.java
