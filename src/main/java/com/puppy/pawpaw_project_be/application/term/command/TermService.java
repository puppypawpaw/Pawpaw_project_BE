package com.puppy.pawpaw_project_be.application.term.command;

import com.puppy.pawpaw_project_be.domain.term.domain.Term;
import com.puppy.pawpaw_project_be.domain.term.domain.repository.TermRepository;
import com.puppy.pawpaw_project_be.domain.term.dto.request.CreateTermRequest;
import com.puppy.pawpaw_project_be.domain.term.dto.request.UpdateTermRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.puppy.pawpaw_project_be.exception.term.NotFoundTermException;

@Service
@RequiredArgsConstructor
public class TermService {
    private final TermRepository termRepository;

    @Transactional
    public void createTerm(final CreateTermRequest request) {
        termRepository.save(request.toEntity());
    }

    @Transactional
    public void updateTerm(
        final Long termId,
        final UpdateTermRequest request
    ) {
        Term term = termRepository.findById(termId)
            .orElseThrow(NotFoundTermException::new);

        term.update(request);
    }

    @Transactional
    public void deleteTerm(final Long termId) {
        termRepository.deleteById(termId);
    }
}
