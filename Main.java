class Main {
	public static void main(String[] args) {
		// Initialize the scanner with the input file
		try{
			CoreScanner skim = new CoreScanner(args[0]);

			// Print the token stream
			while (skim.currentToken() != Core.EOS && skim.currentToken() != Core.ERROR) {
				// Pring the current token, with any extra data
				System.out.print(skim.currentToken());
				if (skim.currentToken() == Core.ID) {
					String value = skim.getId();
					System.out.print("[" + value + "]");
				} else if (skim.currentToken() == Core.CONST) {
					int value = skim.getConst();
					System.out.print("[" + value + "]");
				} else if (skim.currentToken() == Core.STRING) {
					String value = skim.getString();
					System.out.print("[" + value + "]");
				}
				System.out.print("\n");

				// Advance to the next token
				skim.nextToken();
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}