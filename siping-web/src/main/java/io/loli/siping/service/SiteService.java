package io.loli.siping.service;

import io.loli.siping.dao.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SiteService {

    @Autowired
    private SiteRepository siteRepository;

    public boolean validateSecret(Long id, String secret) {
        return siteRepository.findById(id).filter(s -> s.getSecret().equals(secret)).isPresent();
    }
}
