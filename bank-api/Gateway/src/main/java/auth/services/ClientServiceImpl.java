package auth.services;

import auth.domains.models.Client;
import auth.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client create(Client client) {
        if (clientRepository.existsByUsername(client.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        return clientRepository.save(client);
    }

    public Client getByUsername(String username) {
        return clientRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }


    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }


    public Client getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public void deauthorize() {
        clientRepository.deleteByUsername(getCurrentUser().getUsername());
    }
}