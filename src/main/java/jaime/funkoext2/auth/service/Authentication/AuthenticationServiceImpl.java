package jaime.funkoext2.auth.service.Authentication;

import jaime.funkoext2.auth.dto.JwtAuthResponse;
import jaime.funkoext2.auth.dto.UserSignInRequest;
import jaime.funkoext2.auth.dto.UserSignUpRequest;
import jaime.funkoext2.auth.exception.AuthSingInInvalid;
import jaime.funkoext2.auth.exception.UserAuthNameOrEmailExisten;
import jaime.funkoext2.auth.exception.UserDiferentePasswords;
import jaime.funkoext2.auth.repository.AuthUsersRepository;
import jaime.funkoext2.auth.service.jwt.JwtService;
import jaime.funkoext2.user.models.Role;
import jaime.funkoext2.user.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthUsersRepository authUsersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServiceImpl(AuthUsersRepository authUsersRepository, PasswordEncoder passwordEncoder,
                                     JwtService jwtService, AuthenticationManager authenticationManager) {
        this.authUsersRepository = authUsersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registra un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     */
    @Override
    public JwtAuthResponse signUp(UserSignUpRequest request) {
        log.info("Creando usuario: {}", request);
        if (request.getPassword().contentEquals(request.getPasswordComprobacion())) {
            User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .nombre(request.getNombre())
                    .apellidos(request.getApellidos())
                    .roles(Stream.of(Role.USER).collect(Collectors.toSet()))
                    .build();
            try {
                // Salvamos y devolvemos el token
                var userStored = authUsersRepository.save(user);
                return JwtAuthResponse.builder().token(jwtService.generateToken(userStored)).build();
            } catch (DataIntegrityViolationException ex) {
                throw new UserAuthNameOrEmailExisten("El usuario con username " + request.getUsername() + " o email " + request.getEmail() + " ya existe");
            }
        } else {
            throw new UserDiferentePasswords("Las contraseñas no coinciden");

        }
    }

    /**
     * Autentica un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     */
    @Override
    public JwtAuthResponse signIn(UserSignInRequest request) {
        log.info("Autenticando usuario: {}", request);
        // Autenticamos y devolvemos el token
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = authUsersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthSingInInvalid("Usuario o contraseña incorrectos"));
        var jwt = jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(jwt).build();
    }
}
