package pl.krasmap.iam.infrastructure.out;

import org.springframework.stereotype.Component;
import pl.krasmap.iam.application.domain.UserSubmission;
import pl.krasmap.iam.application.port.out.GetUserSubmissionsInterface;

import java.util.List;

@Component
public class GetUserSubmissionsFromContext implements GetUserSubmissionsInterface {

    @Override
    public List<UserSubmission> GetUserSubmissions(int userId) {
        return List.of();
    }
}
