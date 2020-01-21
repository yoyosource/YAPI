package yapi.encryption.passwordtable;

public class PWAnyCharacter extends PWObject {

    public PWAnyCharacter() {
        super(' ');
    }

    @Override
    public boolean update() {
        if (getChar() == '~') {
            setChar(' ');
            return true;
        }
        setChar((char)(getChar() + 1));
        return false;
    }
}
