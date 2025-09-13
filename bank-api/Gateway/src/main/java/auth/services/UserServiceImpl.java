package auth.services;

import auth.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl {

    @Value("${bank-api.controllers.user-url}")
    private String userControllerUrl;

    @Value("${bank-api.controllers.bank-account-url}")
    private String bankAccountControllerUrl;

    private final RestTemplate restTemplate;
    private final ClientServiceImpl clientService;

    @Autowired
    public UserServiceImpl(RestTemplate restTemplate, ClientServiceImpl clientService) {
        this.restTemplate = restTemplate;
        this.clientService = clientService;
    }

    public ResponseUserDTO getUser(String login) {
        String url = userControllerUrl + "/{login}";
        return restTemplate.getForObject(url, ResponseUserDTO.class, login);
    }

    public ResponseBankAccountDTO getBankAccount(String login) {
        String url = bankAccountControllerUrl + "/{id}";
        return restTemplate.getForObject(url, ResponseBankAccountDTO.class, login);
    }

    public void updateFriendList(String login, FriendRequest friendRequest) {
        String url = bankAccountControllerUrl + "/{login}/update-friend";
        restTemplate.postForObject(url, friendRequest, Void.class, login);
    }

    public ResponseTransactionDTO topUp(String login, TransactionDTO transactionDTO) {
        String url = bankAccountControllerUrl + "/{id}/top-up";
        return restTemplate.postForObject(url, transactionDTO, ResponseTransactionDTO.class, login);
    }

    public ResponseTransactionDTO withdraw(String login, TransactionDTO transactionDTO) {
        String url = bankAccountControllerUrl + "/{id}/withdraw";
        return restTemplate.postForObject(url, transactionDTO, ResponseTransactionDTO.class, login);
    }

    public ResponseTransactionDTO transferMoney(String fromAccountId, String toAccountId, TransactionDTO transactionDTO) {
        String url = bankAccountControllerUrl + "/{fromAccountId}/transfer/{toAccountId}";
        return restTemplate.postForObject(url, transactionDTO, ResponseTransactionDTO.class, fromAccountId, toAccountId);
    }

    @Transactional
    public void deauthorize() {
        clientService.deauthorize();
    }
}
