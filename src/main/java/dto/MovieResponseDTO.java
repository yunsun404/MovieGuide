package dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieResponseDTO {
	private int page;              
    private List<MovieDTO> results; 
    private int total_pages;       
    private int total_results;   
}
