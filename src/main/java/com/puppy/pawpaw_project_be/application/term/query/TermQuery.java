package com.puppy.pawpaw_project_be.application.term.query;

import com.puppy.pawpaw_project_be.domain.term.domain.repository.TermRepository;
import com.puppy.pawpaw_project_be.domain.term.dto.response.TermResponse;
import com.puppy.pawpaw_project_be.exception.term.NotFoundTermException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TermQuery {
    private final TermRepository termRepository;

    public TermResponse getTerm(final Long termId) {
        return termRepository.findById(termId)
            .map(TermResponse::of)
            .orElseThrow(NotFoundTermException::new);
    }

    public List<TermResponse> getAllTerms() {
        return termRepository.findByOrderNotNullOrderByOrder()
            .stream()
            .map(TermResponse::of)
            .collect(Collectors.toList());
    }
}
