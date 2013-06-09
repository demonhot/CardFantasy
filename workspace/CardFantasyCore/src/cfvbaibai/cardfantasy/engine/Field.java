package cfvbaibai.cardfantasy.engine;

import java.util.ArrayList;
import java.util.List;

import cfvbaibai.cardfantasy.CardFantasyRuntimeException;

public class Field extends CardPile {

    private Player owner;
    public Field(Player owner) {
        this.owner = owner;
    }
    
    public Player getOwner() {
        return this.owner;
    }

    public List<CardInfo> getAliveCards() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (CardInfo card : getCards()) {
            if (card != null) {
                result.add(card);
            }
        }
        return result;
    }
    
    public CardInfo getCard(int i) {
        if (i < 0) {
            throw new CardFantasyRuntimeException("Invalid parameter i: " + i + ". i must be a positive integer.");
        }
        if (i >= size()) {
            return null;
        } else {
            return this.getCards().get(i);
        }
    }

    public CardInfo expelCard(int i) {
        if (i < 0 || i >= getCards().size()) {
            throw new CardFantasyRuntimeException("Invalid parameter i: " + i + ". Card count = " + size());
        }
        CardInfo expelledCard = getCards().get(i);
        getCards().set(i, null);
        return expelledCard;
    }

    public void compact() {
        int i = 0;
        while (i < size()) {
            if (getCards().get(i) == null) {
                getCards().remove(i);
            } else {
                ++i;
            }
        }
    }
}
