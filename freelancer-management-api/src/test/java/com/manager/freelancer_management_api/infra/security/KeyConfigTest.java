package com.manager.freelancer_management_api.infra.security;

import com.manager.freelancer_management_api.exceptions.PrivateKeyLoadException;
import com.manager.freelancer_management_api.exceptions.PublicKeyLoadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeyConfigTest {

    @Mock
    private Resource publicKeyResource;

    @Mock
    private Resource privateKeyResource;

    @InjectMocks
    private KeyConfig keyConfig;

    @Test
    void testPublicKey_Success() throws Exception {
        String publicKeyTestContent = "-----BEGIN PUBLIC KEY-----\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDYiZwdw8S1lwVTAwgJfSkpjIS6\n" +
                "B5YH6HhJkaZzaJMYkR0pOuBtGADJCejyZJxcfy6G3u6iG6J5hl/vuO8PIbC9pZKT\n" +
                "Hn1MKX/8mRVOyJgYtCtelo0rBncapG0IYb6IUNrcmhLdMJprc/OtWyrLpdZqvAxz\n" +
                "jNTbwfe3ik5+cLkQVQIDAQAB\n" +
                "-----END PUBLIC KEY-----";
        when(publicKeyResource.getInputStream())
                .thenReturn(new ByteArrayInputStream(publicKeyTestContent.getBytes(StandardCharsets.UTF_8)));

        RSAPublicKey publicKey = keyConfig.publicKey();

        assertNotNull(publicKey);
        assertEquals("RSA", publicKey.getAlgorithm());
    }

    @Test
    void testPublicKey_invalidKey_ThrowsPublicKeyLoadException() throws Exception{
        when(publicKeyResource.getInputStream()).thenThrow(new PublicKeyLoadException("Invalid public key."));

        assertThrows(PublicKeyLoadException.class, () -> keyConfig.publicKey());
    }

    @Test
    void testPrivateKey_Success() throws Exception {
        String privateKeyTestContent = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC0hAiaZJ/O6kYT\n" +
                "ZKlRlkXYsLXL+qwIUbzBSCrEemS8tKTJLtUPaCi7QiY7Mg9vocOqcsOkMiitKb5i\n" +
                "pSK3Ux9NyXwrL3IDeuea+JsZzqbY51jsryr9U50k148u1vMJ4Xt1yyWqOOQvpM4A\n" +
                "iN+q1rPS+/LcIlAGuJH02cfqISalSStz+hdz8DgIgR1zStHjpIrCfWXeFT9Y/hLh\n" +
                "+14oaoiyet0LcOuICXiSN2FSN+mz53SW+Nb/1j39UjpvLzDU8VMH56Aw9h+YlxS+\n" +
                "/xxyr8U5no0DFt+dGPIy7w+e2iYoinckIC0FNj0T15mL0rkbiE+rfCa9mu0v6tgh\n" +
                "Q5xe7UjrAgMBAAECggEABBUGSS4xJxom9CFAL14r0uqr02Kus/rs8j8zLOw1GoM3\n" +
                "ZuTihuUZiRpF0LjP/J3WoorBpnMC/zSLSlVRd/XESMHflBApxh3oCRoFqK+19d3v\n" +
                "2PblhDYsaWVwyOgUNcj1V4r0SCDvKOEPLHW4uncObAwKENLMnmvj5ANdKZ0GXt5l\n" +
                "b3ufrfSleb3Jzc9MtCiKRtaGpQqu2klzdzRaiSIs5iIhZUyS//nO7rNBVEYOPC2M\n" +
                "24YEEQO1bM3T31qyISn6CTcMtQyPdZzY9ptDCGzKj/iKu7isIgkALCCbQFTbHJaK\n" +
                "Ud8ePcbzCkEfRzay8c6uvy9OxhRAlJx65aKs5UTcLQKBgQDYODltJAn6bPUBFRja\n" +
                "KJw83+nulH6x7TG+60w2VWmkEc28cyBj3RZO05EksQawQyXDXK/yIpBfySFQ4Xa4\n" +
                "KKzMIE2GdyLF+b5yHZa4lgbUtZ6YGQrLr5ePdu5ENbd6UeTBLHYMLyMrTfEh/huJ\n" +
                "ZsVIqNrUImzm5lJKXVrt2yDDbQKBgQDVui9ThBh6cn0QP3zo3AvQ9HQm6Fm0UhJI\n" +
                "WJ86aDRy9ddlf/hEmO6eYsjwqsOz5InBHj4pwHh9b2bJcHzYh+ffH0nlPkX7VCju\n" +
                "xknKsxQJraDdcKdBEWrHbzbY5RD20jjYfsiJ/iUfgrlgm6u5UtDmdIo3wnMaTt8E\n" +
                "WHpVMnQutwKBgDTlZF8n/uzt9O5j2+WbI8XcvZ0pZOlgbtgXNljggmyqElv1CkqE\n" +
                "Du4P4iVIzasXckieB0lEXW+hg+4GioW5JbT2fiDUSoSGeanvRS05PsiQ+hCZp8N2\n" +
                "3KwzW00CmzVojX8YwnRun7r7YW+Ae/ocU01YSdi151CBrkYVGqawAg7tAoGARm9L\n" +
                "dXyNDJ16dcZWhe8uvbgyJzhfh7ucZtc98mOxFUnLqJmZ2hGEYhIwIpBEWp6wf0JW\n" +
                "PjA+BSiXeT9nBX0Uf0WydqglLAS6UKZsXy7RJbdg0llz5U+2Hu+Z5bTn+E+jiEWQ\n" +
                "FVn6Jf5Q1XO4DiUqzygM/P9KK5teh/IYrscn3tUCgYBiQqfPpk+caM8Dxux7pWAE\n" +
                "4cEZH4Eobj4T1Dc7OdlcEKyPzILBMiq9oorNV6fEwbKQZOaXMcpJrhF8YWQ/dTlY\n" +
                "ZndiWdLocTLy10Q94E3G3PwACLZDmi/3t6UNC19T5fZsnEw6rzjo+wsg2w9yQHzv\n" +
                "NFkvZGR1/WTIyo82K8BWlQ==\n" +
                "-----END PRIVATE KEY-----\n";

        when(privateKeyResource.getInputStream()).thenReturn(new ByteArrayInputStream(privateKeyTestContent.getBytes(StandardCharsets.UTF_8)));

        RSAPrivateKey privateKey = keyConfig.privateKey();

        assertNotNull(privateKey);
        assertEquals("RSA", privateKey.getAlgorithm());
    }

    @Test
    void testPrivateKey_invalidKey_ThrowsKeyLoadException() throws Exception{
        when(privateKeyResource.getInputStream()).thenThrow(new PrivateKeyLoadException("Invalid private key."));

        assertThrows(PrivateKeyLoadException.class, () -> keyConfig.privateKey());
    }
}