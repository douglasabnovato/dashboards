// Acesso somente leitura ao histórico mensal
package com.learntech.dashboards.api.metricas;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricaMensalRepository extends JpaRepository<MetricaMensalEntity, Integer> {
}
// fim de MetricaMensalRepository.java
