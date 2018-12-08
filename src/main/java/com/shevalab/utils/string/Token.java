package com.shevalab.utils.string;

public class Token implements Comparable<Token> {
    private TokenType type;
    private String text;

    public Token() {
        this.type = TokenType.UNDEFINED;
    }

    public int compareTo(Token t) {
        if (TokenType.TEXT.equals(this.type)) {
            return TokenType.NUMBER.equals(t.type) ? 1 : this.getText().compareTo(t.getText());
        } else {
            return TokenType.TEXT.equals(t.type) ? -1 : this.getNumber().compareTo(t.getNumber());
        }
    }

    public String getText() {
        if (this.text == null) {
            this.text = "";
        }

        return this.text;
    }

    public Token setText(String text) {
        this.text = text;
        return this;
    }

    public Integer getNumber() {
        return Integer.parseInt(this.text);
    }

    public Token addChar(char c) {
        this.setText(this.getText() + c);
        return this;
    }

    public TokenType getType() {
        return this.type;
    }

    public Token setType(TokenType type) {
        this.type = type;
        return this;
    }
}
