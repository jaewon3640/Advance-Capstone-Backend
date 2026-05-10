package com.factcheck.repository;

import com.factcheck.domain.AnalysisSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisSectionRepository extends JpaRepository<AnalysisSection, Long> {
    List<AnalysisSection> findByAnalysisResultId(Long resultId);
}
