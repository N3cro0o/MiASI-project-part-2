package pl.krasmap.submission.infrastructure.out;

import org.springframework.stereotype.Component;
import pl.krasmap.krasnal.application.port.in.RequestKrasnalInterface;
import pl.krasmap.submission.application.domain.ReviewKrasnal;
import pl.krasmap.submission.application.port.out.GetKrasnalInterface;

@Component
public class GetKrasnalFromContext implements GetKrasnalInterface {

    private final RequestKrasnalInterface krasnalIn;

    public GetKrasnalFromContext(RequestKrasnalInterface krasnal){
        krasnalIn = krasnal;
    }

    @Override
    public void AddNewKrasnal(ReviewKrasnal krasnal) {
        krasnalIn.AddNewKrasnal(krasnal.name(), krasnal.description(), krasnal.position(), krasnal.category());
    }
}
