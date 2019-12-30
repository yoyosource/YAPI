package yapi.encryption.passwordtable;

public class PWCharacterLowerCase extends PWObject {

    public PWCharacterLowerCase() {
        super('a');
    }

    @Override
    public boolean update() {
        if (getChar() == 'z') {
            setChar('a');
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
