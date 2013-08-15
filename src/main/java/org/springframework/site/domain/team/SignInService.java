package org.springframework.site.domain.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.stereotype.Service;

@Service
public class SignInService {

	public static final String SPRING_METADATA_GROUP_ID = "435080";
	private static final String IS_MEMBER_URL = "https://api.github.com/teams/{team}/members/{user}";
	private final TeamService teamService;

	@Autowired
	public SignInService(TeamService teamService) {
		this.teamService = teamService;
	}

	public MemberProfile getOrCreateMemberProfile(Long githubId, GitHub gitHub) {
		GitHubUserProfile remoteProfile = gitHub.userOperations().getUserProfile();

		return teamService.createOrUpdateMemberProfile(githubId,
				remoteProfile.getUsername(),
				remoteProfile.getProfileImageUrl(),
				remoteProfile.getName());
	}

	public boolean isSpringMember(String userId, GitHub gitHub) {
		ResponseEntity<Void> response = gitHub.restOperations().getForEntity(IS_MEMBER_URL, Void.class, SPRING_METADATA_GROUP_ID, userId);
		return response.getStatusCode() == HttpStatus.NO_CONTENT;
	}
}
