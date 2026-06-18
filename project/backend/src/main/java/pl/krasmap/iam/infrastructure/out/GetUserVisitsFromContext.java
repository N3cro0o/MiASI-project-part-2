package pl.krasmap.iam.infrastructure.out;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.krasmap.iam.application.domain.data.stats.UserVisits;
import pl.krasmap.iam.application.port.out.GetUserVisitsInterface;
import pl.krasmap.interaction.application.domain.data.fav.VisitedKrasnal;
import pl.krasmap.interaction.application.port.in.RequestVisitedInterface;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetUserVisitsFromContext implements GetUserVisitsInterface {

    private final RequestVisitedInterface visitFetch;

    public GetUserVisitsFromContext(@Lazy RequestVisitedInterface visit){
        visitFetch = visit;
    }

    @Override
    public List<UserVisits> GetUserVisits(int userId) {
        List<UserVisits> l = new ArrayList<>();
        for (VisitedKrasnal k : visitFetch.GetVisitedForUser(userId)){
            l.add(UserVisits.From(k));
        }
        return l;
    }
}
