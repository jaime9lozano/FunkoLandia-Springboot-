package jaime.funkoext2.auth.service.Authentication;

import jaime.funkoext2.auth.dto.JwtAuthResponse;
import jaime.funkoext2.auth.dto.UserSignInRequest;
import jaime.funkoext2.auth.dto.UserSignUpRequest;

public interface AuthenticationService {
    JwtAuthResponse signUp(UserSignUpRequest request);

    JwtAuthResponse signIn(UserSignInRequest request);
}
