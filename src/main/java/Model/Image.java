package Model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder

public class Image {
    private String url;
    private Accommodation accommodation;
    private String id;
    private LocalDate uploadedTime;
    private boolean isMainImage;
}
