package com.pl;

class Position {
    private int index;
    private int line;
    private int col;
    private int fileLength;
    public Position(int idx, int lin, int col, int len){
        this.index = idx;
        this.line = lin;
        this.col = col;
        this.fileLength = len;
    }
    public void advancePosition(char c){
        index += 1;
        col += 1;

        if(c == '\n' && index < fileLength){
            line += 1;
            col = 0;
        }
    }

    public int getIndex() {
        return index;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "Position{" +
                "index=" + index +
                ", line=" + line +
                ", col=" + col +
                ", fileLength=" + fileLength +
                '}';
    }

    public Position getPosition(){
        return new Position(index, line, col, fileLength);
    }
}
