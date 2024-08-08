package wad.seoul_nolgoat.web.search;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wad.seoul_nolgoat.service.search.SearchService;
import wad.seoul_nolgoat.web.search.dto.request.PossibleCategoriesConditionDto;
import wad.seoul_nolgoat.web.search.dto.request.SearchConditionDto;
import wad.seoul_nolgoat.web.search.dto.response.CombinationDto;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/search")
@RestController
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/all")
    public ResponseEntity<List<CombinationDto>> searchAll(@ModelAttribute SearchConditionDto searchConditionDto) {
        return ResponseEntity
                .ok(searchService.searchAll(searchConditionDto));
    }

    @GetMapping("/possible/categories")
    public ResponseEntity<List<String>> searchPossibleCategories(
            @ModelAttribute PossibleCategoriesConditionDto possibleCategoriesConditionDto) {
        return ResponseEntity
                .ok(searchService.searchPossibleCategories(possibleCategoriesConditionDto));
    }
}
