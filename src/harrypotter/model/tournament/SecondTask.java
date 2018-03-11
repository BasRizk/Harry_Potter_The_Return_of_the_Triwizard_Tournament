package harrypotter.model.tournament;

import harrypotter.exceptions.InCooldownException;
import harrypotter.exceptions.InvalidTargetCellException;
import harrypotter.exceptions.OutOfBordersException;
import harrypotter.model.character.Champion;
import harrypotter.model.character.HufflepuffWizard;
import harrypotter.model.character.Wizard;
import harrypotter.model.world.Cell;
import harrypotter.model.world.Direction;
import harrypotter.model.world.EmptyCell;
import harrypotter.model.world.Merperson;
import harrypotter.model.world.ObstacleCell;
import harrypotter.model.world.TreasureCell;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SecondTask extends Task {

	private ArrayList<Champion> winners;

	public SecondTask(ArrayList<Champion> champions) throws IOException {

		super(champions);
		Collections.shuffle(champions);
		generateMap();

		winners = new ArrayList<Champion>();
		setCurrentChamp(getChampions().get(0));

	}

	public void generateMap() {

		Cell[][] map = getMap();

		initializeAllEmpty();

		allocateChampions();

		int count = 0;
		while (count < 40) {
			int randomX = (int) (Math.random() * 10);
			int randomY = (int) (Math.random() * 10);
			if (map[randomX][randomY] instanceof EmptyCell) {
				int hp = (int) ((Math.random() * 101) + 200);
				int dmg = (int) ((Math.random() * 201) + 100);
				map[randomX][randomY] = new ObstacleCell(new Merperson(hp, dmg));
				count++;
			}
		}

		count = 0;
		while (count < getChampions().size()) {
			int randomX = (int) (Math.random() * 10);
			int randomY = (int) (Math.random() * 10);
			if (map[randomX][randomY] instanceof EmptyCell) {
				map[randomX][randomY] = new TreasureCell(getChampions().get(
						count));
				count++;
			}
		}

		allocatePotions();

	}

	public void moveForward() throws IOException, OutOfBordersException,
			InvalidTargetCellException {

		Wizard current = (Wizard) getCurrentChamp();

		Point location = current.getLocation();

		Point newLocation = new Point(location.x - 1, location.y);
		if (newLocation.x < 0) {
			throw new OutOfBordersException(
					"Cannot move beyond the front border.");
		}

		Cell next = getMap()[newLocation.x][newLocation.y];

		if (next instanceof TreasureCell
				&& ((TreasureCell) next).getOwner() == current) {

			getMap()[location.x][location.y] = new EmptyCell();
			getMap()[newLocation.x][newLocation.y] = new EmptyCell();
			current.setLocation(new Point(newLocation.x, newLocation.y));

			getChampions().remove(current);
			winners.add((Champion) current);

			endTurn();

		} else if (next instanceof TreasureCell) {
			super.getListener().encounteringForiegnTreasure(newLocation.x,newLocation.y);
			throw new InvalidTargetCellException(
					"Cannot move to another champion's treasure");

		} else {
			super.moveForward();
		}
	}

	public void moveBackward() throws IOException, OutOfBordersException,
			InvalidTargetCellException {

		Wizard current = (Wizard) getCurrentChamp();

		Point location = current.getLocation();

		Point newLocation = new Point(location.x + 1, location.y);
		if (newLocation.x > 9) {
			throw new OutOfBordersException(
					"Cannot move beyond the back border.");
		}

		Cell next = getMap()[newLocation.x][newLocation.y];

		if (next instanceof TreasureCell
				&& ((TreasureCell) next).getOwner() == current) {

			getMap()[location.x][location.y] = new EmptyCell();
			getMap()[newLocation.x][newLocation.y] = new EmptyCell();
			current.setLocation(new Point(newLocation.x, newLocation.y));

			getChampions().remove(current);
			winners.add((Champion) current);

			endTurn();

		} else if (next instanceof TreasureCell) {

			throw new InvalidTargetCellException(
					"Cannot move to another champion's treasure");

		} else {
			super.moveBackward();
		}
	}

	public void moveLeft() throws IOException, OutOfBordersException,
			InvalidTargetCellException {

		Wizard current = (Wizard) getCurrentChamp();

		Point location = current.getLocation();

		Point newLocation = new Point(location.x, location.y - 1);
		if (newLocation.y < 0) {
			throw new OutOfBordersException(
					"Cannot move beyond the left border.");
		}

		Cell next = getMap()[newLocation.x][newLocation.y];

		if (next instanceof TreasureCell
				&& ((TreasureCell) next).getOwner() == current) {

			getMap()[location.x][location.y] = new EmptyCell();
			getMap()[newLocation.x][newLocation.y] = new EmptyCell();
			current.setLocation(new Point(newLocation.x, newLocation.y));

			getChampions().remove(current);
			winners.add((Champion) current);

			endTurn();

		} else if (next instanceof TreasureCell) {

			throw new InvalidTargetCellException(
					"Cannot move to another champion's treasure");

		} else {
			super.moveLeft();
		}
	}

	public void moveRight() throws IOException, OutOfBordersException,
			InvalidTargetCellException {

		Wizard current = (Wizard) getCurrentChamp();

		Point location = current.getLocation();

		Point newLocation = new Point(location.x, location.y + 1);
		if (newLocation.y > 9) {
			throw new OutOfBordersException(
					"Cannot move beyond the right border.");
		}

		Cell next = getMap()[newLocation.x][newLocation.y];

		if (next instanceof TreasureCell
				&& ((TreasureCell) next).getOwner() == current) {

			getMap()[location.x][location.y] = new EmptyCell();
			getMap()[newLocation.x][newLocation.y] = new EmptyCell();
			current.setLocation(new Point(newLocation.x, newLocation.y));
			super.getListener().championWon(getCurrentChamp());
			getChampions().remove(current);
			winners.add((Champion) current);
			
			endTurn();

		} else if (next instanceof TreasureCell) {

			throw new InvalidTargetCellException(
					"Cannot move to another champion's treasure");

		} else {
			super.moveRight();
		}
	}

	public void finalizeAction() throws IOException {

		Wizard current = (Wizard) getCurrentChamp();

		setAllowedMoves(getAllowedMoves() - 1);

		if (getAllowedMoves() == 0) {

			if (!(current instanceof HufflepuffWizard && isTraitActivated()))
				if (getChampions().contains(getCurrentChamp()))
					encounterMerPerson();

			endTurn();
		}

	}

	public void endTurn() throws IOException {

		if (getChampions().size() == 0) {
			if (getListener() != null)
				getListener().onFinishingSecondTask(winners);

		} else {
			super.endTurn();
		}

	}

	public void encounterMerPerson() {

		Wizard current = (Wizard) getCurrentChamp();

		int currentX = current.getLocation().x;
		int currentY = current.getLocation().y;

		int newHp = current.getHp();

		ObstacleCell cell = null;
		if (currentX + 1 <= 9
				&& getMap()[currentX + 1][currentY] instanceof ObstacleCell) {

			cell = (ObstacleCell) getMap()[currentX + 1][currentY];
			newHp = newHp - ((Merperson) (cell.getObstacle())).getDamage();
			super.getListener().actionEffect(((Merperson) ((cell).getObstacle())).getDamage());

		}
		if (currentX - 1 >= 0
				&& getMap()[currentX - 1][currentY] instanceof ObstacleCell) {

			cell = (ObstacleCell) getMap()[currentX - 1][currentY];
			newHp = newHp - ((Merperson) ((cell).getObstacle())).getDamage();
			super.getListener().actionEffect(((Merperson) ((cell).getObstacle())).getDamage());

		}
		if (currentY - 1 >= 0
				&& getMap()[currentX][currentY - 1] instanceof ObstacleCell) {

			cell = (ObstacleCell) getMap()[currentX][currentY - 1];
			newHp = newHp - ((Merperson) ((cell).getObstacle())).getDamage();
			super.getListener().actionEffect(((Merperson) ((cell).getObstacle())).getDamage());

		}
		if (currentY + 1 <= 9
				&& getMap()[currentX][currentY + 1] instanceof ObstacleCell) {

			cell = (ObstacleCell) getMap()[currentX][currentY + 1];
			newHp = newHp - ((Merperson) ((cell).getObstacle())).getDamage();
			super.getListener().actionEffect(((Merperson) ((cell).getObstacle())).getDamage());

		}

		if (newHp <= 0) {

			current.setHp(0);
			getChampions().remove(getCurrentChamp());
			getMap()[currentX][currentY] = new EmptyCell();

		} else {
			current.setHp(newHp);
		}
		

	}

	public void onSlytherinTrait(Direction d) throws IOException,
			InCooldownException, OutOfBordersException,
			InvalidTargetCellException {

		Wizard current = (Wizard) getCurrentChamp();
		super.onSlytherinTrait(d);
		current.setTraitCooldown(4);

	}

	public void onHufflepuffTrait() throws InCooldownException {

		Wizard current = (Wizard) getCurrentChamp();
		super.onHufflepuffTrait();
		current.setTraitCooldown(6);

	}

	public Object onRavenclawTrait() throws InCooldownException {

		Wizard current = (Wizard) getCurrentChamp();

		if (current.getTraitCooldown() > 0) {
			throw new InCooldownException(current.getTraitCooldown());
		}

		ArrayList<Direction> result = new ArrayList<Direction>();

		int x = 0;
		int y = 0;
		for (int i = 0; i < getMap().length; i++) {
			for (int j = 0; j < getMap()[i].length; j++) {

				Cell c = getMap()[i][j];

				if (c instanceof TreasureCell) {
					if (((TreasureCell) c).getOwner() == getCurrentChamp()) {

						x = i;
						y = j;
						break;

					}
				}
			}
		}

		int currentX = current.getLocation().x;
		int currentY = current.getLocation().y;

		if (y > currentY)
			result.add(Direction.RIGHT);
		else if (y < currentY)
			result.add(Direction.LEFT);

		if (x > currentX)
			result.add(Direction.BACKWARD);
		else if (x < currentX)
			result.add(Direction.FORWARD);

		setTraitActivated(true);

		current.setTraitCooldown(7);

		return result;

	}

	public ArrayList<Champion> getWinners() {
		return winners;
	}

	public void setWinners(ArrayList<Champion> winners) {
		this.winners = winners;
	}

}
