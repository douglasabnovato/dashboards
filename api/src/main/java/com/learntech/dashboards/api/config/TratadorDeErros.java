// Converte exceções de regra e de entrada em respostas Problem Details (RFC 9457) sem stack trace
package com.learntech.dashboards.api.config;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.learntech.dashboards.core.kanban.WipExcedidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class TratadorDeErros {

    // regra de negócio violada ou valor inválido: 400
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public ProblemDetail invalido(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // parâmetro de URL com tipo errado (ex.: fase=abc, projeto=XYZ): 400
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail tipoErrado(MethodArgumentTypeMismatchException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Valor inválido para " + e.getName());
    }

    // JSON malformado ou rejeitado pelo construtor do record: 400 com a mensagem da regra
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail corpoInvalido(HttpMessageNotReadableException e) {
        String detalhe = e.getCause() instanceof ValueInstantiationException vie && vie.getCause() != null
                ? vie.getCause().getMessage() : "Corpo da requisição inválido";
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detalhe);
    }

    // tarefa inexistente: 404
    @ExceptionHandler(NoSuchElementException.class)
    public ProblemDetail naoEncontrado(NoSuchElementException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // WIP Limit: 409, com a tarefa que ocupa a coluna Doing
    @ExceptionHandler(WipExcedidoException.class)
    public ProblemDetail wip(WipExcedidoException e) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        p.setProperty("tarefaAtiva", e.tarefaAtiva());
        return p;
    }
}
// fim de TratadorDeErros.java
