package kr.co.pawpaw.api.service.term;

import kr.co.pawpaw.api.dto.term.CreateTermRequest;
import kr.co.pawpaw.api.dto.term.TermResponse;
import kr.co.pawpaw.api.dto.term.UpdateTermRequest;
import kr.co.pawpaw.common.exception.term.NotFoundTermException;
import kr.co.pawpaw.domainrdb.term.domain.Term;
import kr.co.pawpaw.domainrdb.term.service.command.TermCommand;
import kr.co.pawpaw.domainrdb.term.service.query.TermQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermService {
    private final TermQuery termQuery;
    private final TermCommand termCommand;

    @Transactional
    public void createTerm(final CreateTermRequest request) {
        termCommand.save(request.toEntity());
    }

    public List<TermResponse> getAllTerms() {
        return termQuery.findByOrderNotNullOrderByOrder()
            .stream()
            .map(TermResponse::of)
            .collect(Collectors.toList());
    }

    public TermResponse getTerm(final Long id) {
        return termQuery.findById(id)
            .map(TermResponse::of)
            .orElseThrow(NotFoundTermException::new);
    }

    @Transactional
    public void updateTerm(
        final Long termId,
        final UpdateTermRequest request
    ) {
        Term term = termQuery.findById(termId)
            .orElseThrow(NotFoundTermException::new);

        term.update(request.getTitle(), request.getContent(), request.getRequired(), request.getOrder());
    }

    @Transactional
    public void deleteTerm(final Long termId) {
        termCommand.deleteById(termId);
    }
}
