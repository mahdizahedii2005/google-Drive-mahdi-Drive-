package server;

public class outPutOfRegex {
    private String newLine;
    private String result;

    public outPutOfRegex (String newLine, String result) {
        this.newLine = newLine;
        this.result = result;
    }

    public String getResult () {
        return result;
    }

    public String getNewLine () {
        return newLine;
    }
}
