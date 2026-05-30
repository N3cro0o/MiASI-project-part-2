package pl.krasmap.krasnal.application.service;

import org.springframework.stereotype.Service;
import pl.krasmap.krasnal.application.domain.krasnal.Krasnal;

@Service
public class HoldKrasnalRepo {

    public Krasnal ReturnDummyKrasnal() {
        return Krasnal.dummy();
    }

}
