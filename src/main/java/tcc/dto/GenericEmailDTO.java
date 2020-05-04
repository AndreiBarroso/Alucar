package tcc.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericEmailDTO {

    private String destination;
    private String subject;
    private String fragmentName;
    private String fragmentPath;
    private Object object;

}
