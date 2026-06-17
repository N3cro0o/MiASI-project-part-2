package pl.krasmap.interaction.application.service;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import pl.krasmap.iam.application.domain.data.User;
import pl.krasmap.iam.application.service.HoldUserRepo;
import pl.krasmap.interaction.application.domain.review.ReviewUser;

@Service
public class GetUserService {

    AnnotationConfigApplicationContext krasnalContext;

    public GetUserService() {
        krasnalContext = new AnnotationConfigApplicationContext();
        krasnalContext.scan("pl.krasmap.iam");
        krasnalContext.refresh();
    }

    private HoldUserRepo GetIamUserContext() {
        return krasnalContext.getBean(HoldUserRepo.class);
    }

    public ReviewUser GetUser(int userId) {
        var service = GetIamUserContext();
        User userToMap = service.GetUser(0);
        return new ReviewUser(userToMap.id(), userToMap.email());
    }
}
