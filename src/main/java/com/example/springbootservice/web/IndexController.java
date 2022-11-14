package com.example.springbootservice.web;

import com.example.springbootservice.config.auth.PrincipalDetails;
import com.example.springbootservice.service.PostsService;
import com.example.springbootservice.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal PrincipalDetails userDetails, Model model) {
        if (userDetails != null) {
            log.info("userDetails: {}", userDetails.getUser().getEmail());
            log.info("role: {}", userDetails.getUser().getRole().getKey());
        }
        model.addAttribute("posts", postsService.findAllDesc());
        return "index";
    }

    @GetMapping("/posts/save")
    public String postSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
