package pl.poznan.put;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class KeyConfig {

    private static final Logger logger = LoggerFactory.getLogger(KeyConfig.class);

    @Value("${jwt.public.key.path}")
    private String publicKeyPath;

    @Bean
    public RSAPublicKey publicKey() throws Exception {
        logger.info("Rozpoczynanie ładowania klucza publicznego RSA z pliku: {}", publicKeyPath);

        File keyFile = new File(publicKeyPath);
        if (!keyFile.exists()) {
            logger.error("Plik klucza publicznego nie został znaleziony: {}", publicKeyPath);
            throw new IllegalArgumentException("Plik klucza publicznego nie został znaleziony: " + publicKeyPath);
        }

        StringBuilder publicKeyPEM = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(keyFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                publicKeyPEM.append(line).append("\n");
            }
        } catch (Exception e) {
            logger.error("Błąd podczas odczytu pliku klucza publicznego: {}", e.getMessage());
            throw e;
        }

        String cleared = publicKeyPEM.toString()
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        logger.debug("Oczyszczony klucz publiczny (pierwsze 30 znaków): {}",
                cleared.substring(0, Math.min(cleared.length(), 30)) + "...");

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(cleared);
            logger.info("Klucz publiczny został pomyślnie zdekodowany.");
        } catch (IllegalArgumentException ex) {
            logger.error("Błąd podczas dekodowania klucza publicznego: {}", ex.getMessage());
            throw ex;
        }

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        RSAPublicKey rsaPublicKey;
        try {
            rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            logger.info("Klucz publiczny RSA został pomyślnie wygenerowany.");
        } catch (Exception e) {
            logger.error("Błąd podczas generowania RSAPublicKey: {}", e.getMessage());
            throw e;
        }

        return rsaPublicKey;
    }

    @Bean
    public JwtVerifierFilter jwtVerifierFilter(RSAPublicKey publicKey) {
        logger.info("Tworzenie beana JwtVerifierFilter z issuer 'https://elogin.put.poznan.pl'.");
        JwtVerifierFilter filter = new JwtVerifierFilter(publicKey, "elogin.put.poznan.pl");
        logger.debug("Bean JwtVerifierFilter został utworzony.");
        return filter;
    }
}
