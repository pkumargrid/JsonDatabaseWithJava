package create.database.constants;

public enum UserQuery {
    GET,SET,DELETE,EXIT;

    private int index;
    private String data;

    public void setIndex(int index){
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public void setData(String data){ 
        this.data = data;
    }

    public String getData() {
        return this.data;
    }

}
