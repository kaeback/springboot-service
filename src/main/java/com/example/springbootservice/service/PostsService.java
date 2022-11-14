package com.example.springbootservice.service;

import com.example.springbootservice.domain.posts.Posts;
import com.example.springbootservice.domain.posts.PostsRepository;
import com.example.springbootservice.web.dto.PostsListResponseDto;
import com.example.springbootservice.web.dto.PostsResponseDto;
import com.example.springbootservice.web.dto.PostsSaveRequestDto;
import com.example.springbootservice.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requesDto) {
        return postsRepository.save(requesDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    public List<PostsListResponseDto> findAllDesc() {
//        return postsRepository.findAllDesc().stream()
//                .map(PostsListResponseDto::new)
//                .collect(Collectors.toList());
        List<Posts> result = postsRepository.findAllDesc();
        List<PostsListResponseDto> list = new ArrayList<>();
        for (Posts posts : result) {
            list.add(new PostsListResponseDto(posts));
        }

        return list;
    }

    @Transactional
    public void delete (Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습ㄴ다. id=" + id));

        postsRepository.delete(posts);
    }

}
