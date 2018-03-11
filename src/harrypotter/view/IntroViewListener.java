package harrypotter.view;

import java.util.ArrayList;

import harrypotter.model.character.Champion;

public interface IntroViewListener {
    public void champDone(ArrayList<Champion> c);
    public void spellDone();
}
