package Game.Actions.TravelAction;

import Exceptions.InvalidActionException;
import Exceptions.RolladieException;
import Functionalities.Storage;
import Functionalities.UIHandler;
import Game.Actions.Action;
import Game.Events.EventType;
import Game.Game;

public abstract class TravelAction extends Action {

    public boolean execute(Game game, Storage storage, UIHandler ui) throws RolladieException {
        if (game.getEventType() == EventType.TRAVEL) {
            executeTravelAction(game, storage, ui);
            return false;
        } else {
            throw new InvalidActionException();
        }
    }

    public abstract void executeTravelAction(Game game, Storage storage, UIHandler ui) throws RolladieException;
}
