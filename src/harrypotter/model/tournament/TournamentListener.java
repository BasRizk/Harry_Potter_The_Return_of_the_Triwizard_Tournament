package harrypotter.model.tournament;

import java.io.IOException;
import java.util.ArrayList;

import harrypotter.exceptions.InvalidTargetCellException;
import harrypotter.exceptions.OutOfBordersException;
import harrypotter.model.character.Champion;

public interface TournamentListener {
	void intializeFirstTask();
	void intializeSecondTask(ArrayList<Champion> winners);
	void intializeThirdTask(ArrayList<Champion> winners);
	void showWinner(Champion winner);
	void gameOver();
	void showActionEffect(int damageAmount);
	void awarePlayerOfForeignTreasure(int x, int y);
	void showCurrentWinners(Champion currentChamp);
}
