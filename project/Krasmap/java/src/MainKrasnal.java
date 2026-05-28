import krasnal.logic.KrasnalCore;

import java.time.LocalDateTime;

public class MainKrasnal {
    final KrasnalCore krasnal = new KrasnalCore();

    public static void main(String[] args) {
        System.out.printf("%s: KRASNALMAP INIT\n", LocalDateTime.now());
    }
}
