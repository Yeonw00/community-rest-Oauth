package com.example.community.rest.community_rest.user;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.community.rest.community_rest.jpa.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauthUser = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId(); // google, facebook 등
        String providerId = oauthUser.getAttribute("sub"); // Google의 고유 ID
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        // 기존 사용자 조회
        Optional<User> existingUser = userRepository.findByGoogleId(providerId);
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get(); // 기존 사용자
        } else {
            // 신규 사용자 저장
            user = new User();
            user.setGoogleId(providerId);
            user.setEmailAddress(email);
            user.setUsername(name);
            user.setPassword(null);
            userRepository.save(user);
        }

        return oauthUser;
    }
}