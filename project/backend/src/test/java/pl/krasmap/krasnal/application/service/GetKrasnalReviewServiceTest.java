package pl.krasmap.krasnal.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krasmap.krasnal.application.domain.KrasnalReview;
import pl.krasmap.krasnal.application.port.out.GetKrasnalReviewInterface;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetKrasnalReviewServiceTest {

    @Mock
    private GetKrasnalReviewInterface port;

    @InjectMocks
    private GetKrasnalReviewService service;

    @Test
    void testGetKrasnalReviews_callsPortAndReturnsList() {
        int krasnalId = 34;
        KrasnalReview kr1 = mock(KrasnalReview.class);
        KrasnalReview kr2 = mock(KrasnalReview.class);
        List<KrasnalReview> reviews = List.of(kr1, kr2);

        when(port.GetAllReviews(krasnalId)).thenReturn(reviews);

        List<KrasnalReview> result = service.GetKrasnalReviews(krasnalId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(kr1, result.get(0));
        verify(port, times(1)).GetAllReviews(krasnalId);
        verifyNoMoreInteractions(port);
    }

    @Test
    void testGetKrasnalReviews_whenNoReviews_returnsEmptyList() {
        int krasnalId = 32;
        when(port.GetAllReviews(krasnalId)).thenReturn(Collections.emptyList());

        List<KrasnalReview> result = service.GetKrasnalReviews(krasnalId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(port, times(1)).GetAllReviews(krasnalId);
    }
}
