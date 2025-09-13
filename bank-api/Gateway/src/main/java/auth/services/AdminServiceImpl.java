package auth.services;

import auth.domains.enums.HairColor;
import auth.domains.models.Client;
import auth.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static auth.domains.enums.Role.ROLE_ADMIN;
import static auth.domains.enums.Role.ROLE_USER;

@Service
public class AdminServiceImpl {

    @Value("${bank-api.controllers.user-url}")
    private String userControllerUrl;

    @Value("${bank-api.controllers.bank-account-url}")
    private String bankAccountControllerUrl;

    private final RestTemplate restTemplate;
    private final ClientServiceImpl clientService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(RestTemplate restTemplate, PasswordEncoder passwordEncoder,
                            ClientServiceImpl clientService) {
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
        this.clientService = clientService;
    }

    @Transactional
    public ResponseUserDTO createUser(SignUpRequest signUpRequest) {
        Client client = Client.builder()
                .role(ROLE_USER)
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .username(signUpRequest.getUsername())
                .build();

        clientService.create(client);

        String url = userControllerUrl;
        return restTemplate.postForObject(url, signUpRequest, ResponseUserDTO.class);
    }

    public ResponseUserDTO createAdmin(SignUpRequest signUpRequest) {
        Client client = Client.builder()
                .role(ROLE_ADMIN)
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .username(signUpRequest.getUsername())
                .build();

        clientService.create(client);

        String url = userControllerUrl;
        return restTemplate.postForObject(url, signUpRequest, ResponseUserDTO.class);
    }

    public List<ResponseUserDTO> getUsersWithFilter(HairColor hairColor, String sex) {
        String url = userControllerUrl + "/list-with-filter";
        ResponseEntity<List<ResponseUserDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResponseUserDTO>>() {},
                hairColor.name(), sex
        );
        return response.getBody();
    }

    public ResponseUserDTO getUser(String login) {
        String url = userControllerUrl + "/{login}";
        return restTemplate.getForObject(url, ResponseUserDTO.class, login);
    }

    public List<ResponseBankAccountDTO> getAllBankAccounts() {
        String url = bankAccountControllerUrl;
        ResponseEntity<List<ResponseBankAccountDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResponseBankAccountDTO>>() {}
        );
        return response.getBody();
    }

    public List<ResponseTransactionDTO> getOperations(String login) {
        String url = bankAccountControllerUrl + "/{id}/operations";
        ResponseEntity<List<ResponseTransactionDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResponseTransactionDTO>>() {}
        );
        return response.getBody();
    }

    public ResponseBankAccountDTO createBankAccount(BankAccountDTO bankAccountDTO) {
        String url = bankAccountControllerUrl;
        return restTemplate.postForObject(url, null, ResponseBankAccountDTO.class, bankAccountDTO);
    }

    public void deauthorize() {
        clientService.deauthorize();
    }
}
