package ru.skillbox.socialnetwork.service.admin;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.skillbox.socialnetwork.data.dto.admin.PersonStatisticResponse;
import ru.skillbox.socialnetwork.data.dto.admin.StatisticRequest;
import ru.skillbox.socialnetwork.data.dto.admin.PostStatisticResponse;
import ru.skillbox.socialnetwork.data.entity.Person;
import ru.skillbox.socialnetwork.data.entity.Post;
import ru.skillbox.socialnetwork.data.repository.PersonRepo;
import ru.skillbox.socialnetwork.data.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AdminServiceTest {
    @Autowired
    private AdminService adminService;

    @MockBean
    private PostRepository postRepository;
    @MockBean
    private PersonRepo personRepository;

    private static Post postFirst;
    private static Post postSecond;
    private static Person personFirst;
    private static Person personSecond;


    @BeforeAll
    static void init(){
        personFirst = new Person();
        personFirst.setId(1L);
        personFirst.setBirthTime(LocalDateTime.of(1993, 12, 11, 0, 0));
        personFirst.setRegTime(LocalDateTime.of(2021, 1, 1, 1, 0));

        personSecond = new Person();
        personSecond.setId(2L);
        personSecond.setBirthTime(LocalDateTime.of(2000, 1, 1, 0, 0));
        personSecond.setRegTime(LocalDateTime.of(2011, 2, 2, 2, 0));

        postFirst = new Post();
        postFirst.setTime(LocalDateTime.of(2021, 1, 1, 1, 0));

        postSecond = new Post();
        postSecond.setTime(LocalDateTime.of(2011, 2, 2, 2, 0));
    }

    @Test
    void getPostStatistic_With_graphPeriod_years() {
          Mockito.when(postRepository.findAllByTimeBetweenDates(Mockito.any(), Mockito.any())).thenReturn(List.of(postFirst, postSecond));
          Mockito.when(postRepository.count()).thenReturn(2L);
          Mockito.when(postRepository.findAll()).thenReturn(List.of(postFirst, postSecond));
          StatisticRequest request = StatisticRequest.builder()
                .dateFromGraph("2011-01-03T02:00:00 04:00")
                .dateToGraph("2021-01-03T01:00:19 04:00")
                .graphPeriod("years")
                .dateFromDiagram("2011-01-03T02:00:00 04:00")
                .dateToDiagram("2021-01-03T01:00:19 04:00")
                .diagramPeriod("allTime")
                .build();

        PostStatisticResponse response = adminService.getPostStatistic(request);
        assertEquals(2, response.getTotalPostCount());
        assertEquals(2, response.getFoundPostCount());
        assertEquals(11, response.getGraphData().size());
        assertEquals(24, response.getPostsByHour().size());
        assertEquals(50.0, response.getPostsByHour().get(1));
        assertEquals(50.0, response.getPostsByHour().get(2));
    }

    @Test
    void getPostStatistic_With_graphPeriod_months() {
        Mockito.when(postRepository.findAllByTimeBetweenDates(Mockito.any(), Mockito.any())).thenReturn(List.of(postFirst));
        Mockito.when(postRepository.count()).thenReturn(2L);
        Mockito.when(postRepository.findAll()).thenReturn(List.of(postFirst, postSecond));
        StatisticRequest request = StatisticRequest.builder()
                .dateFromGraph("2020-01-03T02:00:00 04:00")
                .dateToGraph("2021-01-03T01:00:19 04:00")
                .graphPeriod("months")
                .dateFromDiagram("2011-01-03T02:00:00 04:00")
                .dateToDiagram("2021-01-03T01:00:19 04:00")
                .diagramPeriod("allTime")
                .build();

        PostStatisticResponse response = adminService.getPostStatistic(request);
        assertEquals(2, response.getTotalPostCount());
        assertEquals(1, response.getFoundPostCount());
        assertEquals(12, response.getGraphData().size());
        assertEquals(24, response.getPostsByHour().size());
        assertEquals(50.0, response.getPostsByHour().get(1));
        assertEquals(50.0, response.getPostsByHour().get(2));
    }

    @Test
    void getPostStatistic_With_graphPeriod_days() {
        Mockito.when(postRepository.findAllByTimeBetweenDates(Mockito.any(), Mockito.any())).thenReturn(List.of(postFirst));
        Mockito.when(postRepository.count()).thenReturn(2L);
        Mockito.when(postRepository.findAll()).thenReturn(List.of(postFirst, postSecond));
        StatisticRequest request = StatisticRequest.builder()
                .dateFromGraph("2021-01-01T00:00:00 04:00")
                .dateToGraph("2021-01-03T01:00:19 04:00")
                .graphPeriod("days")
                .dateFromDiagram("2021-01-01T02:00:00 04:00")
                .dateToDiagram("2021-01-03T01:00:19 04:00")
                .diagramPeriod("allTime")
                .build();

        PostStatisticResponse response = adminService.getPostStatistic(request);
        assertEquals(2, response.getTotalPostCount());
        assertEquals(1, response.getFoundPostCount());
        assertEquals(3, response.getGraphData().size());
        assertEquals(24, response.getPostsByHour().size());
        assertEquals(50.0, response.getPostsByHour().get(1));
        assertEquals(50.0, response.getPostsByHour().get(2));
    }


}