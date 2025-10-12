package com.snugplace.demo.unit;


import com.snugplace.demo.Controllers.AccommodationController;
import com.snugplace.demo.DTO.Accommodation.CreateAccommodationDTO;
import com.snugplace.demo.Model.Accommodation;
import com.snugplace.demo.Repository.AccommodationRepository;
import com.snugplace.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Accommodation Controller Test")
class AccommodationControllerTest {

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private Accommodation accommodationService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AccommodationController accommodationController;

    private CreateAccommodationDTO accommodationDTO;

    /*
    @BeforeEach
    void setUp(){
        setupTestData();
    }

    private void setupTestData() {
        accommodationDto = new CreateAccommodationDTO(
                1, "Cozy Apartment", "A comfortable apartment", "Medellín", "123 Main St",
                6.2442, -75.5812, 120000.0, 4,
                Arrays.asList("WiFi", "Kitchen"), "https://example.com/image.jpg"
        );*/



}
