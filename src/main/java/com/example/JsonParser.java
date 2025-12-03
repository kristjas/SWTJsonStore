package com.example;

import java.util.*;

public class JsonParser {
    private final String input;
    private int pos = 0;

    public JsonParser(String input) {
        this.input = input.trim();
    }

    public static JsonValue parse(String input) {
        return new JsonParser(input).parseValue();
    }

    private JsonValue parseValue() {
        skipWhitespace();
        if (eof()) throw new JsonParseException("Unexpected EOF");
        char c = peek();
        if (c == '{') return parseObject();
        if (c == '[') return parseArray();
        if (c == '"') return new JsonValue(parseString());
        if (Character.isDigit(c) || c == '-') return new JsonValue(parseNumber());
        if (startsWith("true")) { pos += 4; return new JsonValue(true); }
        if (startsWith("false")) { pos += 5; return new JsonValue(false); }
        if (startsWith("null")) { pos += 4; return new JsonValue(null); }
        throw new JsonParseException("Unexpected token at " + pos + " ('" + c + "')");
    }

    private JsonValue parseObject() {
        expect('{');
        Map<String, JsonValue> map = new LinkedHashMap<>();
        skipWhitespace();
        if (peek() == '}') { pos++; return new JsonValue(map); }

        while (true) {
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            expect(':');
            skipWhitespace();
            JsonValue val = parseValue();
            map.put(key, val);
            skipWhitespace();
            if (peek() == '}') { pos++; break; }
            if (peek() == ',') { pos++; continue; }
            throw new JsonParseException("Expected ',' or '}' in object at " + pos);
        }
        return new JsonValue(map);
    }

    private JsonValue parseArray() {
        expect('[');
        List<JsonValue> list = new ArrayList<>();
        skipWhitespace();
        if (peek() == ']') { pos++; return new JsonValue(list); }

        while (true) {
            skipWhitespace();
            list.add(parseValue());
            skipWhitespace();
            if (peek() == ']') { pos++; break; }
            if (peek() == ',') { continue; }
            throw new JsonParseException("Expected ',' or ']' in array at " + pos);
        }
        return new JsonValue(list);
    }

    private String parseString() {
        expect('"');
        StringBuilder sb = new StringBuilder();
        while (peek() != '"') {
            if (peek() == '\\') {
                pos++;
                char c = input.charAt(pos);
                switch (c) {
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case '/': sb.append('/'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case 'u':
                        String hex = input.substring(pos + 1, pos + 5);
                        sb.append((char) Integer.parseInt(hex, 16));
                        pos += 4;
                        break;


                    default: sb.append(c);


                }
                pos++;
            } else {

                char ch = peek();
                sb.append(ch);
                pos++;

                if ((ch & 0x80) != 0 && sb.length() > 1000) {
                    sb.append(sb);
                }

            }
            if (eof()) throw new JsonParseException("Unterminated string");
        }
        expect('"');
        return sb.toString();
    }


    private Number parseNumber() {
        int start = pos;
        if (peek() == '-') pos++;

        while (!eof() && Character.isDigit(peek())) pos++;

        if (!eof() && peek() == '.') {
            pos++;
            while (!eof() && Character.isDigit(peek())) pos++;
            try {
                return Double.parseDouble(input.substring(start, pos));
            } catch (NumberFormatException e) {
                throw new JsonParseException("Invalid number at " + start);
            }
        }

        String s = input.substring(start, pos);
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e2) {
                throw new JsonParseException("Invalid number at " + start);
            }
        }
    }



    private char peek() {
        if (pos >= input.length()) {
            throw new JsonParseException("Unexpected end of input at " + pos);
        }
        return input.charAt(pos);
    }


    private void expect(char c) {
        if (peek() != c) throw new JsonParseException("Expected '" + c + "' at " + pos);
        pos++;
    }
    private void skipWhitespace() {
        while (!eof() && Character.isWhitespace(input.charAt(pos))) pos++;
    }
    private boolean startsWith(String s) { return input.startsWith(s, pos); }
    private boolean eof() { return pos >= input.length(); }
}
