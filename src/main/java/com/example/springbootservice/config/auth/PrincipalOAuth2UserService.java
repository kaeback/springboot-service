package com.example.springbootservice.config.auth;

import com.example.springbootservice.domain.user.Role;
import com.example.springbootservice.domain.user.User;
import com.example.springbootservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리 메소드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration: " + userRequest.getClientRegistration());
        System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code를 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> loadUser 메소드 호출 -> 구글로부터 회원 프로필을 받아준다.

        System.out.println("getAttributes: " + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId(); // google
        String email = oAuth2User.getAttribute("email");

        User user = User.builder()
                .name(oAuth2User.getAttribute("name"))
                .email(oAuth2User.getAttribute("email"))
                .picture(oAuth2User.getAttribute("picture"))
                .role(Role.USER)
                .provider(provider)
                .build();

        Optional<User> findUser = userRepository.findByEmail(email);
        if (!findUser.isPresent()) {
            userRepository.save(user);
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

}
