package kr.co.pawpaw.api;

import kr.co.pawpaw.domainrdb.term.domain.Term;
import kr.co.pawpaw.domainrdb.term.service.command.TermCommand;
import kr.co.pawpaw.domainrdb.term.service.query.TermQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {
    private final TermCommand termCommand;
    private final TermQuery termQuery;

    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        Map<Long, Term> newTerm = Map.of(
            1L, Term.builder()
                .title("만 14세 이상입니다.")
                .content("만 14세 이상입니다.")
                .order(1L)
                .required(true)
                .build(),
            2L, Term.builder()
                .title("서비스 통합이용약관 동의")
                .content("서비스 통합이용약관 동의")
                .order(2L)
                .required(true)
                .build(),
            3L, Term.builder()
                .title("위치정보 이용약관 동의")
                .content("위치정보 이용약관 동의")
                .order(3L)
                .required(true)
                .build(),
            4L, Term.builder()
                .title("개인정보 수집 및 이용 동의")
                .content("개인정보 수집 및 이용 동의")
                .order(4L)
                .required(false)
                .build()
        );

        List<Term> oldTerm = termQuery.findAll();
        Set<Long> oldOrder = oldTerm.stream()
            .map(Term::getOrder)
            .collect(Collectors.toSet());

        newTerm.forEach((order, term) -> {
           if (!oldOrder.contains(order)) {
               termCommand.save(term);
           }
        });

        oldTerm.forEach(term -> {
            Term findTerm = newTerm.get(term.getOrder());
            if (Objects.nonNull(findTerm)) {
                term.update(findTerm.getTitle(), findTerm.getContent(), findTerm.getRequired(), findTerm.getOrder());
            } else {
                termCommand.deleteById(term.getId());
            }
        });
    }
}