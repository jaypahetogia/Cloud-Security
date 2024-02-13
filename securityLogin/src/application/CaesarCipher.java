package application;


public class CaesarCipher {

	
	public static String encrypt(String text, int shift) {
		StringBuilder result = new StringBuilder();
		for(char character:text.toCharArray()) {
			if(Character.isLetter(character)) {
				char base = Character.isLowerCase(character)? 'a':'A';
				char shifted = (char) (((character-base + shift)%26)+base);
				result.append(shifted);
				
			} else {
				result.append(character);
			}
		}
		return result.toString();
	}
	
	public static String decrypt(String text, int shift) {
		return encrypt(text, 26 - shift);
	}
}
