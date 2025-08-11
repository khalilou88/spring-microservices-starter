package com.example.core.vault.service;

import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

@Service
public class VaultService {

    private static final Logger logger = LoggerFactory.getLogger(VaultService.class);

    private final VaultTemplate vaultTemplate;

    public VaultService(VaultTemplate vaultTemplate) {
        this.vaultTemplate = vaultTemplate;
    }

    public Optional<String> getSecret(String path, String key) {
        try {
            VaultResponse response = vaultTemplate.read(path);
            if (response != null && response.getData() != null) {
                Object value = response.getData().get(key);
                return Optional.ofNullable(value != null ? value.toString() : null);
            }
        } catch (Exception e) {
            logger.error("Error reading secret from path: {} key: {}", path, key, e);
        }
        return Optional.empty();
    }

    public void writeSecret(String path, Map<String, Object> secrets) {
        try {
            vaultTemplate.write(path, secrets);
            logger.info("Successfully wrote secrets to path: {}", path);
        } catch (Exception e) {
            logger.error("Error writing secrets to path: {}", path, e);
            throw new RuntimeException("Failed to write secrets to Vault", e);
        }
    }

    public boolean deleteSecret(String path) {
        try {
            vaultTemplate.delete(path);
            logger.info("Successfully deleted secrets at path: {}", path);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting secrets at path: {}", path, e);
            return false;
        }
    }
}
