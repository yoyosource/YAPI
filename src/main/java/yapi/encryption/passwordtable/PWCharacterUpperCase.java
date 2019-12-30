package yapi.encryption.passwordtable;

public class PWCharacterUpperCase extends PWObject {

    public PWCharacterUpperCase() {
        super('A');
    }

    @Override
    public boolean update() {
        if (getChar() == 'Z') {
            setChar('A');
            return true;
        }
        setChar((char)(getChar() + 1));
        return false;
    }

    @Override
    public char getChar() {
        return super.getChar();
    }
}
