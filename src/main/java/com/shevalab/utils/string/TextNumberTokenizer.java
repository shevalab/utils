package com.shevalab.utils.string;

class TextNumberTokenizer {
    private String text;
    private int pos = 0;
    TokenType state;

    public TextNumberTokenizer(String text) {
        this.state = TokenType.UNDEFINED;
        this.text = text;
    }

    public boolean hasNextToken() {
        return this.text != null && this.pos < this.text.length();
    }

    public Token getNextToken() {
        Token token = (new Token()).setType(this.state);

        while(this.hasNextToken()) {
            char c = this.text.charAt(this.pos++);
            if (TokenType.TEXT.equals(this.state)) {
                if (this.isNumber(c)) {
                    this.state = TokenType.NUMBER;
                    --this.pos;
                    break;
                }

                token.addChar(c);
            } else if (TokenType.NUMBER.equals(this.state)) {
                if (!this.isNumber(c)) {
                    this.state = TokenType.TEXT;
                    --this.pos;
                    break;
                }

                token.addChar(c);
            } else {
                this.state = this.isNumber(c) ? TokenType.NUMBER : TokenType.TEXT;
                token.setType(this.state);
                token.addChar(c);
            }
        }

        return token;
    }

    private boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }
}
