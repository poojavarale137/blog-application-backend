/*
 * package com.blog.app.utils;
 * 
 * 
 * import io.jsonwebtoken.security.Keys; import
 * io.jsonwebtoken.SignatureAlgorithm; import javax.crypto.SecretKey; import
 * java.util.Base64;
 * 
 * public class GenerateJwtKey { public static void main(String[] args) {
 * SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
 * System.out.println("Key length (bits): " + (key.getEncoded().length * 8));
 * System.out.println("Base64 encoded key:");
 * System.out.println(Base64.getEncoder().encodeToString(key.getEncoded())); } }
 */