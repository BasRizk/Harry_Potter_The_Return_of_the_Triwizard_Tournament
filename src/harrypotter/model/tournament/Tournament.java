package harrypotter.model.tournament;

import harrypotter.model.character.Champion;
import harrypotter.model.magic.DamagingSpell;
import harrypotter.model.magic.HealingSpell;
import harrypotter.model.magic.RelocatingSpell;
import harrypotter.model.magic.Spell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Tournament implements TaskListener {

	private ArrayList<Champion> champions;
	private ArrayList<Spell> spells;
	private FirstTask firstTask;
	private SecondTask secondTask;
	private ThirdTask thirdTask;
	private TournamentListener listener;

	public Tournament() throws IOException {

		champions = new ArrayList<Champion>();
		spells = new ArrayList<Spell>();
		loadSpells("Spells.csv");

	}

	public ArrayList<Champion> getChampions() {
		return champions;
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public FirstTask getFirstTask() {
		return firstTask;
	}

	public SecondTask getSecondTask() {
		return secondTask;
	}

	public ThirdTask getThirdTask() {
		return thirdTask;
	}

	public void addChampion(Champion c) {
		champions.add(c);
	}

	private void loadSpells(String filePath) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {

			String[] content = line.split(",");
			switch (content[0]) {

			case "DMG":
				spells.add(new DamagingSpell(content[1], Integer
						.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3])));
				break;

			case "HEL":
				spells.add(new HealingSpell(content[1], Integer
						.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3])));
				break;

			case "REL":
				spells.add(new RelocatingSpell(content[1], Integer
						.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3])));
				break;

			}

			line = br.readLine();

		}

		br.close();

	}

	public void beginTournament() throws IOException {

		firstTask = new FirstTask(champions);
		firstTask.setListener(this);
		listener.intializeFirstTask();

	}

	public void onFinishingFirstTask(ArrayList<Champion> winners)
			throws IOException {

		if (!winners.isEmpty()) {
			secondTask = new SecondTask(winners);
			secondTask.setListener(this);
			listener.intializeSecondTask(winners);
		}else{
			listener.gameOver();
		}
	}

	public void onFinishingSecondTask(ArrayList<Champion> winners)
			throws IOException {

		if (!winners.isEmpty()) {
			thirdTask = new ThirdTask(winners);
			thirdTask.setListener(this);
			listener.intializeThirdTask(winners);
		}else{
			listener.gameOver();
		}

	}

	public void onFinishingThirdTask(Champion winner) {

		listener.showWinner(winner);

	}

	public TournamentListener getListener() {
		return listener;
	}

	public void setListener(TournamentListener listener) {
		this.listener = listener;
	}

	@Override
	public void actionEffect(int damageAmount) {
		this.listener.showActionEffect(damageAmount);
		
	}

	@Override
	public void encounteringForiegnTreasure(int x, int y) {
		this.listener.awarePlayerOfForeignTreasure(x,y);
	}

	@Override
	public void championWon(Champion currentChamp) {
		this.listener.showCurrentWinners(currentChamp);
		
	}

}
