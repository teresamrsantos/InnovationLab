package pt.uc.dei.paj.general;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {

	private static final Random RANDOM = new SecureRandom();
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	/*
	 * A contagem de iteração é o número de vezes que a senha é ''hashed'' durante a
	 * derivação da chave simétrica. Quanto maior o número, mais difícil será
	 * validar uma suposição de senha e derivar a chave correta. É usado em conjunto
	 * com o salt que é usado para prevenir roubos de senhas. A contagem de
	 * iterações deve ser a mais alta possível, sem diminuir muito a velocidade do
	 * sistema.
	 */
	private static final int ITERATIONS = 10000;

	/* The key length is the length in bits of the derived symmetric key */
	private static final int KEY_LENGTH = 256;

	public static String getSalt(int length) {
		StringBuilder returnValue = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return new String(returnValue);
	}

	public static byte[] hash(char[] password, byte[] salt) {
		// Constructor that takes a password, salt, iteration count,
		// and to-be-derived key length for generating PBEKey of variable-key-size PBE
		// ciphers.

		/*
		 * A criptografia baseada em senha (PBE) é uma forma de geração de chave
		 * simétrica que transforma uma string de entrada (uma senha) em uma chave de
		 * criptografia binária usando várias técnicas de embaralhamento de dados.
		 */

		PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
		Arrays.fill(password, Character.MIN_VALUE);
		try {
			// The PBKDF2WithHmacSHA1 will produce a hash length of 160 bits.
			// HMACSHA1 é um tipo de algoritmo de hash com chave que é construído a
			// partir da função de hash SHA1 e usado como um HMAc/ código de autenticação de
			// mensagem baseado em hash.
			// O processo HMAC mistura uma chave secreta com os dados da mensagem, faz o
			// hash do resultado com a função de hash,
			// mistura esse valor de hash com a chave secreta novamente e, em seguida,
			// aplica a função de hash uma segunda vez.
			// O hash de saída tem 160 bits de comprimento.

			/*
			 * Key factories are used to convert keys (opaque cryptographic keys of type
			 * Key) into key specifications (transparent representations of the underlying
			 * key material), and vice versa.
			 */
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			// Returns the key in its primary encoding format, or null if this key does not
			// support encoding.
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}

	public static String generateSecurePassword(String password, String salt) {

		String returnValue = null;
		byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
	
		returnValue = Base64.getEncoder().encodeToString(securePassword);
		return returnValue;
	}

	public static boolean verifyUserPassword(String providedPassword, String securedPassword, String salt) {
		boolean returnValue = false;

		// Generate New secure password with the same salt
		String newSecurePassword = generateSecurePassword(providedPassword, salt);

		// Check if two passwords are equal
		returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

		return returnValue;
	}
}